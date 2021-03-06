package common.pack;

import common.CommonStatic;
import common.CommonStatic.ImgReader;
import common.battle.data.CustomEnemy;
import common.battle.data.CustomUnit;
import common.io.InStream;
import common.pack.Context.ErrType;
import common.pack.PackData.PackDesc;
import common.pack.PackData.UserPack;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.system.fake.ImageBuilder;
import common.system.files.FDFile;
import common.system.files.FileData;
import common.util.Data;
import common.util.anim.AnimCE;
import common.util.anim.AnimCI;
import common.util.pack.Background;
import common.util.stage.CastleImg;
import common.util.stage.MapColc.PackMapColc;
import common.util.stage.Music;
import common.util.unit.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static common.pack.Source.SourceAnimLoader.*;

@SuppressWarnings("deprecation")
public abstract class VerFixer extends Source {

	public static class IdFixer {

		private final Class<?> ent;

		public IdFixer(Class<?> cls) {
			ent = cls == null ? AbEnemy.class : cls;
		}

		public Class<?> parse(int val, Class<?> cls) {
			if (cls == Data.Proc.THEME.class)
				return Background.class;
			else if (ent == Unit.class)
				return ent;
			else
				return val % 1000 < 500 ? Enemy.class : EneRand.class;
		}

	}

	public static class VerFixerException extends Exception {

		private static final long serialVersionUID = 1L;

		public VerFixerException(String str) {
			super(str);
		}

	}

	@Deprecated
	private static class EnemyFixer extends VerFixer {

		public EnemyFixer(String id, ImgReader r) {
			super(id);
			this.r = r;
		}

		@Override
		protected void load() throws VerFixerException, IOException {
			data.desc.name = is.nextString();
			loadEnemies(is.subStream());
			loadCastles(is.subStream());
			loadBackgrounds(is.subStream());
			loadUnits(is.subStream());
			data.mc = new PackMapColc(data, is);
			loadMusics();
			is.close();
			is = null;
		}

		private AnimCE loadAnim(InStream is) throws VerFixerException {
			int ver = Data.getVer(is.nextString());
			if (ver != 401)
				throw new VerFixerException("DIYAnim expects version 401, got " + ver);
			int type = is.nextInt();
			if (type == 0) {
				String id = is.nextString();
				AnimCE anim = new AnimCE(new ResourceLocation(ResourceLocation.LOCAL, id));

				AnimCE.map().put(id, anim);

				return anim;
			}
			else if (type == 1)
				return decodeAnim(id, is.subStream(), r);
			throw new VerFixerException("DIYAnim expects type 0 or 1, got " + 2);
		}

		private void loadBackgrounds(InStream is) throws VerFixerException {
			int ver = Data.getVer(is.nextString());
			if (ver != 400)
				throw new VerFixerException("expect bg store version to be 400, got " + ver);
			File f = CommonStatic.def.route("./res/img/" + id + "/bg/");
			if (f.exists()) {
				File[] fs = f.listFiles();
				for (File fi : fs) {
					String str = fi.getName();
					if (str.length() != 7)
						continue;
					if (!str.endsWith(".png"))
						continue;
					int val = -1;
					try {
						val = Integer.parseInt(str.substring(0, 3));
					} catch (NumberFormatException e) {
						continue;
					}
					File fx = CommonStatic.ctx.getWorkspaceFile("./" + id + "/backgrounds/" + str);
					Context.renameTo(fi, fx);
					VImg bimg = ImageBuilder.toVImg(fx);
					if (val >= 0 && bimg != null)
						data.bgs.set(val, new Background(new Identifier<>(id, Background.class, val), bimg));
				}
			}
			int n = is.nextInt();
			for (int i = 0; i < n; i++) {
				int ind = is.nextInt();
				Background bg = data.bgs.get(ind);
				if (bg == null)
					continue;
				bg.top = is.nextInt() > 0;
				bg.ic = is.nextInt();
				for (int j = 0; j < 4; j++) {
					int p = is.nextInt();
					bg.cs[j] = new int[] { p >> 16 & 255, p >> 8 & 255, p & 255 };
				}
			}
		}

		private void loadCastles(InStream is) throws VerFixerException {
			int ver = Data.getVer(is.nextString());
			if (ver != 307)
				throw new VerFixerException("expect castle store version to be 307, got " + ver);
			is.nextInt();
			File f = CommonStatic.def.route("./res/img/" + id + "/cas/");
			if (f.exists()) {
				File[] fs = f.listFiles();
				for (File fi : fs) {
					String str = fi.getName();
					if (str.length() != 7)
						continue;
					if (!str.endsWith(".png"))
						continue;
					int val = -1;

					try {
						val = Integer.parseInt(str.substring(0, 3));
					} catch (NumberFormatException e) {
						e.printStackTrace();
						continue;
					}
					File fx = CommonStatic.ctx.getWorkspaceFile("./" + id + "/castles/" + str);
					Context.renameTo(fi, fx);
					VImg bimg = ImageBuilder.toVImg(fx);
					if (val >= 0 && bimg != null)
						data.castles.set(val, new CastleImg(new Identifier<>(id, CastleImg.class, val), bimg));
				}
			}
		}

		private void loadEnemies(InStream is) throws VerFixerException {
			int ver = Data.getVer(is.nextString());
			if (ver != 402)
				throw new VerFixerException("expect enemy store version to be 402, got " + ver);
			UserProfile.setStatic(Identifier.STATIC_FIXER, new IdFixer(AbEnemy.class));
			int len = is.nextInt();
			for (int i = 0; i < len; i++) {
				CustomEnemy ce = new CustomEnemy();
				ce.fillData(402, is);
				int hash = is.nextInt();
				InStream anim = is.subStream();
				String na = is.nextString();
				AnimCE ac = loadAnim(anim);
				Enemy e = new Enemy(new Identifier<>(id, Enemy.class, hash), ac, ce);
				e.name = na;
				data.enemies.set(hash % 1000, e);

			}
			int n = is.nextInt();
			for (int i = 0; i < n; i++) {
				int hash = is.nextInt();
				EneRand e = new EneRand(new Identifier<>(id, EneRand.class, hash));
				e.zread(is.subStream());
				data.randEnemies.set(hash, e);
			}
		}

		private void loadMusics() {
			File f = CommonStatic.def.route("./res/img/" + id + "/music/");
			if (f.exists() && f.isDirectory()) {
				File[] fs = f.listFiles();
				for (File fi : fs) {
					String str = fi.getName();
					if (str.length() != 7)
						continue;
					if (!str.endsWith(".ogg"))
						continue;
					int val = -1;
					try {
						val = Integer.parseInt(str.substring(0, 3));
					} catch (NumberFormatException e) {
						e.printStackTrace();
						continue;
					}
					File fx = CommonStatic.ctx.getWorkspaceFile("./" + id + "/musics/" + str);
					Context.renameTo(fi, fx);
					if (val >= 0)
						data.musics.set(val, new Music(new Identifier<>(id, Music.class, val), new FDFile(fx)));
				}
			}
		}

		private void loadUnits(InStream is) throws VerFixerException {
			int ver = Data.getVer(is.nextString());
			if (ver != 401)
				throw new VerFixerException("expect unit store version to be 401, got " + ver);
			UserProfile.setStatic(Identifier.STATIC_FIXER, new IdFixer(Unit.class));
			int n = is.nextInt();
			for (int i = 0; i < n; i++) {
				int ind = is.nextInt();
				UnitLevel ul = new UnitLevel(new Identifier<>(id, UnitLevel.class, ind), is);
				data.unitLevels.set(ind, ul);
			}
			n = is.nextInt();
			for (int i = 0; i < n; i++) {
				int ind = is.nextInt();
				Unit u = new Unit(new Identifier<>(id, Unit.class, ind));
				Identifier<UnitLevel> lvid = Identifier.parseInt(is.nextInt(), UnitLevel.class);
				u.lv = lvid.get();
				u.lv.units.add(u);
				u.max = is.nextInt();
				u.maxp = is.nextInt();
				u.rarity = is.nextInt();
				int m = is.nextInt();
				u.forms = new Form[m];
				for (int j = 0; j < m; j++) {
					String name = is.nextString();
					AnimCE ac = loadAnim(is.subStream());
					CustomUnit cu = new CustomUnit();
					cu.fillData(401, is);
					u.forms[j] = new Form(u, j, name, ac, cu);
				}
				data.units.set(ind, u);
			}
			UserProfile.setStatic(Identifier.STATIC_FIXER, null);
		}

	}

	@Deprecated
	private static class PackFixer extends VerFixer {

		public PackFixer(String id, ImgReader r) {
			super(id);
			this.r = r;
		}

		@Deprecated
		@Override
		protected void load() throws Exception {
			data.desc.name = is.nextString();
			loadEnemies(is.subStream());
			loadUnits(is.subStream());
			loadCastles(is.subStream());
			loadBackgrounds(is.subStream());
			loadMusics(is.subStream());
			data.mc = new PackMapColc(data, is);
			is.close();
			is = null;
		}

		private void loadBackgrounds(InStream is) throws Exception {
			int ver = Data.getVer(is.nextString());
			if (ver != 400)
				throw new VerFixerException("expect bg store version to be 400, got " + ver);
			int n = is.nextInt();
			for (int i = 0; i < n; i++) {
				int ind = is.nextInt();
				VImg vimg = ImgReader.readImg(is, r);
				vimg.name = Data.trio(ind);
				writeImgs(vimg, "backgrounds", vimg.name + ".png");
				Background bg = new Background(new Identifier<>(id, Background.class, ind), vimg);
				data.bgs.set(ind, bg);
				bg.top = is.nextInt() > 0;
				bg.ic = is.nextInt();
				for (int j = 0; j < 4; j++) {
					int p = is.nextInt();
					bg.cs[j] = new int[] { p >> 8 & 255, p >> 8 & 255, p & 255 };
				}
			}
		}

		private void loadCastles(InStream is) throws Exception {
			int ver = Data.getVer(is.nextString());
			if (ver != 307)
				throw new VerFixerException("expect castle store version to be 307, got " + ver);
			int n = is.nextInt();
			for (int i = 0; i < n; i++) {
				int val = is.nextInt();
				VImg vimg = ImgReader.readImg(is, r);
				vimg.name = Data.trio(val);
				writeImgs(vimg, "castles", vimg.name + ".png");
				data.castles.set(val, new CastleImg(new Identifier<>(id, CastleImg.class, val), vimg));
			}
		}

		private void loadEnemies(InStream is) throws VerFixerException {
			int ver = Data.getVer(is.nextString());
			if (ver != 402)
				throw new VerFixerException("expect enemy store version to be 402, got " + ver);
			UserProfile.setStatic(Identifier.STATIC_FIXER, new IdFixer(AbEnemy.class));
			int n = is.nextInt();
			for (int i = 0; i < n; i++) {
				int hash = is.nextInt();
				String str = is.nextString();
				CustomEnemy ce = new CustomEnemy();
				ce.fillData(ver, is);
				AnimCE ac = decodeAnim(".temp_" + id, is.subStream(), r);
				Enemy e = new Enemy(new Identifier<>(id, Enemy.class, hash % 1000), ac, ce);
				e.name = str;
				data.enemies.set(hash % 1000, e);
			}
			n = is.nextInt();
			for (int i = 0; i < n; i++) {
				int hash = is.nextInt();
				EneRand e = new EneRand(new Identifier<>(id, EneRand.class, hash % 1000));
				e.zread(is.subStream());
				data.randEnemies.set(hash % 1000, e);
			}
		}

		private void loadMusics(InStream is) throws VerFixerException {
			int ver = Data.getVer(is.nextString());
			if (ver != 307)
				throw new VerFixerException("expect music store version to be 307, got " + ver);
			int n = is.nextInt();
			for (int i = 0; i < n; i++) {
				int val = is.nextInt();
				File f = ImgReader.loadMusicFile(is, r, Integer.parseInt(id), val);
				File fx = CommonStatic.ctx.getWorkspaceFile("./.temp_" + id + "/musics/" + Data.trio(val) + ".ogg");
				Context.renameTo(f, fx);
				data.musics.set(val, new Music(new Identifier<>(id, Music.class, val), new FDFile(fx)));
			}
		}

		private void loadUnits(InStream is) throws VerFixerException {
			int ver = Data.getVer(is.nextString());
			if (ver != 401)
				throw new VerFixerException("expect unit store version to be 401, got " + ver);
			UserProfile.setStatic(Identifier.STATIC_FIXER, new IdFixer(Unit.class));
			int n = is.nextInt();
			for (int i = 0; i < n; i++) {
				int ind = is.nextInt();
				UnitLevel ul = new UnitLevel(new Identifier<>(id, UnitLevel.class, ind), is);
				data.unitLevels.set(ind, ul);
			}
			n = is.nextInt();
			for (int i = 0; i < n; i++) {
				int ind = is.nextInt();
				Unit u = new Unit(new Identifier<>(id, Unit.class, ind));
				u.lv = Identifier.parseInt(is.nextInt(), UnitLevel.class).get();
				u.lv.units.add(u);
				u.max = is.nextInt();
				u.maxp = is.nextInt();
				u.rarity = is.nextInt();
				int m = is.nextInt();
				u.forms = new Form[m];
				for (int j = 0; j < m; j++) {
					String name = is.nextString();
					AnimCE ac = decodeAnim(".temp_" + id, is.subStream(), r);
					CustomUnit cu = new CustomUnit();
					cu.fillData(401, is);
					u.forms[j] = new Form(u, j, name, ac, cu);
				}
				data.units.set(ind, u);
			}
			UserProfile.setStatic(Identifier.STATIC_FIXER, null);
		}

		private void writeImgs(VImg img, String type, String name) throws IOException {
			String path = "./.temp_" + id + "/" + type + "/" + name;
			File f = CommonStatic.ctx.getWorkspaceFile(path);
			if(!f.exists())
				Context.check(f);
			String format = name.endsWith(".png") ? "PNG" : "";
			FakeImage.write(img.getImg(), format, f);
			img.unload();
		}
	}

	public static void fix(Map<String, VerFixer> map) throws Exception {
		transform();
		readPacks(map);

		//Close all ISStream before deleting
		for(VerFixer fix : map.values()) {
			if(fix.is != null)
				fix.is.close();
		}

		Context.delete(CommonStatic.def.route("./res"));
		Context.delete(CommonStatic.def.route("./pack"));
	}

	private static VerFixer fix_bcuenemy(InStream is, ImgReader r) throws VerFixerException {
		int ver = Data.getVer(is.nextString());
		if (ver < 400)
			throw new VerFixerException("unexpected bcuenemy data version: " + ver + ", requires 400");
		PackDesc desc = new PackDesc(Data.hex(is.nextInt()));
		int n = is.nextByte();
		for (int i = 0; i < n; i++)
			desc.dependency.add(Data.hex(is.nextInt()));
		EnemyFixer fix = new EnemyFixer(desc.id, r);
		UserPack ans = new UserPack(desc, fix);
		fix.data = ans;
		fix.is = is;
		return fix;
	}

	private static VerFixer fix_bcupack(InStream is, ImgReader r) throws Exception {
		int ver = Data.getVer(is.nextString());
		if (ver != 402)
			throw new VerFixerException("unexpected bcupack data version: " + ver + ", requires 402");
		InStream head = is.subStream();
		PackDesc desc = new PackDesc(Data.hex(head.nextInt()));
		int n = head.nextByte();
		for (int i = 0; i < n; i++)
			desc.dependency.add(Data.hex(head.nextInt()));
		desc.BCU_VERSION = Data.revVer(head.nextInt());
		if (!desc.BCU_VERSION.startsWith("4.11")) // FIXME throw
			new VerFixerException("unexpected pack BCU version: " + desc.BCU_VERSION + ", requires 4.11.x");
		desc.time = head.nextString();
		desc.version = head.nextInt();
		desc.author = head.nextString();
		PackFixer fix = new PackFixer(desc.id, r);
		UserPack ans = new UserPack(desc, fix);
		fix.data = ans;
		fix.is = is;
		return fix;
	}

	private static void move(String a, String b) {
		File f = new File(a);
		if (!f.exists())
			return;
		File bf = new File(b);
		if (!bf.getParentFile().exists())
			bf.getParentFile().mkdirs();
		f.renameTo(new File(b));
	}

	@Deprecated
	private static void readPacks(Map<String, VerFixer> map) throws Exception {
		File f = CommonStatic.def.route("./pack/");
		Set<String> packs = new HashSet<>();
		if (f.exists()) {
			for (File file : f.listFiles()) {
				String str = file.getName();
				if (!str.endsWith(".bcupack"))
					continue;
				try {
					VerFixer pack = fix_bcupack(CommonStatic.def.readBytes(file), CommonStatic.def.getReader(file));
					map.put(pack.id, pack);
					packs.add(pack.id);
				} catch (VerFixerException e) {
					CommonStatic.ctx.noticeErr(e, ErrType.WARN, "The pack "+file.getName()+" has outdated format. Re-export this pack via BCU v4");
					throw e;
				}

			}
		}
		List<VerFixer> list = new ArrayList<>();
		list.addAll(map.values());
		f = CommonStatic.def.route("./res/enemy/");
		if (f.exists())
			for (File file : f.listFiles()) {
				String str = file.getName();
				if (!str.endsWith(".bcuenemy"))
					continue;
				VerFixer fix = fix_bcuenemy(CommonStatic.def.readBytes(file), CommonStatic.def.getReader(file));
				list.removeIf(p -> p.id == fix.id);
				list.add(fix);
				map.put(fix.id, fix);
			}
		while (list.size() > 0) {
			int rem = 0;
			for (VerFixer p : list) {
				String loadProg;

				if(p.data != null) {
					if(p.data.editable) {
						loadProg = "Fixing "+p.data.getSID()+".bcuenemy";
					} else {
						loadProg = "Fixing "+p.data.getSID()+".bcupack";
					}
				} else {
					loadProg = null;
				}

				CommonStatic.ctx.loadProg(loadProg);

				boolean all = true;
				for (String pre : p.data.desc.dependency)
					if (!pre.equals("000000") && (!map.containsKey(pre) || map.get(pre).is != null))
						all = false;
				if (all) {
					p.data.desc.dependency.remove("000000");
					p.load();
					if (p instanceof PackFixer) {
						Workspace w = new Workspace(".temp_" + p.id);
						p.data.source = w;
						w.save(p.data);
						w.export(p.data, p.data.desc.author, null, (d) -> {
						});
						File src = CommonStatic.ctx.getWorkspaceFile("./.temp_" + p.id);
						Context.delete(src);
					} else {
						Workspace w = new Workspace(p.id);
						p.data.source = w;
						w.save(p.data);
					}
					rem++;
				}
			}
			list.removeIf(p -> p.is == null);
			if (rem == 0) {
				for (VerFixer p : list)
					CommonStatic.ctx.printErr(ErrType.WARN, "Failed to load " + p.data.desc);
				break;
			}
		}
	}

	private static void transform() throws IOException {
		File f = CommonStatic.def.route("./res/anim/");
		if (!f.exists())
			return;
		for (String fi : f.list()) {
			String pa = "./res/anim/" + fi + "/";
			String pb = "./workspace/_local/animations/" + fi + "/";
			move(pa + fi + ".png", pb + SP);
			move(pa + "edi.png", pb + EDI);
			move(pa + "uni.png", pb + UNI);
			move(pa + fi + ".imgcut", pb + IC);
			move(pa + fi + ".mamodel", pb + MM);
			move(pa + fi + "00.maanim", pb + MA[0]);
			move(pa + fi + "01.maanim", pb + MA[1]);
			move(pa + fi + "02.maanim", pb + MA[2]);
			move(pa + fi + "03.maanim", pb + MA[3]);
			move(pa + fi + "_zombie00.maanim", pb + MA[4]);
			move(pa + fi + "_zombie01.maanim", pb + MA[5]);
			move(pa + fi + "_zombie02.maanim", pb + MA[6]);
		}
	}

	@Deprecated
	ImgReader r;

	UserPack data;

	InStream is;

	private VerFixer(String id) {
		super(id);
	}

	@Override
	public void delete() {
	}

	@Override
	public FileData getFileData(String str) {
		return null;
	}

	@Override
	public String[] listFile(String str) {
		return null;
	}

	@Override
	public AnimCI loadAnimation(String name) {
		return null;
	}

	@Override
	public VImg readImage(String path, int ind) {
		return null;
	}

	@Override
	public InputStream streamFile(String path) {
		return null;
	}

	@Deprecated
	protected AnimCE decodeAnim(String target, InStream is, ImgReader r) {
		AnimLoader al = CommonStatic.def.loadAnim(is, r);
		ResourceLocation id = al.getName();
		id.pack = target;
		if (!target.equals(ResourceLocation.LOCAL))
			Workspace.validate(Source.ANIM, id);
		AnimCE ce = new AnimCE(al);
		ce.check();
		new SourceAnimSaver(id, ce).saveAll();
		return new AnimCE(id);
	}

	protected abstract void load() throws Exception;

}
