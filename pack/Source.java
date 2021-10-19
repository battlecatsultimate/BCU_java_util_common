package common.pack;

import common.CommonStatic;
import common.io.PackLoader;
import common.io.PackLoader.ZipDesc;
import common.io.assets.Admin.StaticPermitted;
import common.io.json.JsonClass;
import common.io.json.JsonDecoder;
import common.io.json.JsonEncoder;
import common.io.json.JsonField;
import common.pack.Context.ErrType;
import common.pack.PackData.UserPack;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.system.files.FDFile;
import common.system.files.FileData;
import common.system.files.VFile;
import common.util.Data;
import common.util.anim.*;
import common.util.pack.Background;
import common.util.stage.CastleImg;
import common.util.stage.Replay;
import common.util.stage.Stage;
import common.util.stage.StageMap;
import common.util.unit.Enemy;
import common.util.unit.Form;
import common.util.unit.Trait;
import common.util.unit.Unit;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

public abstract class Source {

	public interface AnimLoader {
		VImg getEdi();

		ImgCut getIC();

		MaAnim[] getMA();

		MaModel getMM();

		ResourceLocation getName();

		FakeImage getNum();

		int getStatus();

		VImg getUni();
	}

	public interface SourceLoader {

		FileData loadFile(BasePath base, ResourceLocation id, String str);

	}

	@JsonClass
	public static class ResourceLocation {

		public static final String LOCAL = "_local";

		@JsonField
		public String pack, id;

		@JsonClass.JCConstructor
		public ResourceLocation() {

		}

		public ResourceLocation(String pack, String id) {
			this.pack = pack;
			this.id = id;
		}

		@JsonClass.JCGetter
		public AnimCI getAnim() {
			if (pack.equals(LOCAL))
				return AnimCE.map().get(id);

			return UserProfile.getUserPack(pack).source.loadAnimation(id);
		}

		public String getPath(BasePath type) {
			return "./" + pack + "/" + type.toString() + "/" + id;
		}

		@JsonClass.JCGetter
		public Replay getReplay() {
			if (pack.equals(LOCAL))
				return Replay.getMap().get(id);
			Source s = UserProfile.getUserPack(pack).source;
			String path = "./" + BasePath.REPLAY.toString() + "/" + id + ".replay";
			return Data.err(() -> Replay.read(s.getFileData(path).getStream()));
		}

		@Override
		public String toString() {
			return pack + "/" + id;
		}

		@JsonDecoder.OnInjected
		public void onInjectSource() {
			Object zip = UserProfile.getStatic(UserProfile.CURRENT_PACK, () -> null);

			if (this.pack.equals(LOCAL) && zip instanceof ZipSource) {
				this.pack = ((ZipSource) zip).id;
				this.id = "_mapped_" + this.id;
			}
		}

	}

	@StaticPermitted
	public static class SourceSoulLoader implements Source.AnimLoader {

		public static final String IC = "imgcut.txt";
		public static final String MM = "mamodel.txt";
		public static final String MA = "maanim_soul.txt";
		public static final String SP = "sprite.png";
		public static final String EDI = "icon_display.png";

		private final ResourceLocation id;
		private final SourceLoader loader;
		private final BasePath base;

		public SourceSoulLoader(BasePath base, ResourceLocation id, Source.SourceLoader loader) {
			this.id = id;
			this.base = base;
			this.loader = loader == null ? Workspace::loadAnimFile : loader;
		}

		@Override
		public VImg getEdi() {
			FileData edi = loader.loadFile(base, id, EDI);
			if (edi == null)
				return null;
			return new VImg(FakeImage.read(edi));
		}

		@Override
		public ImgCut getIC() {
			return ImgCut.newIns(loader.loadFile(base, id, IC));
		}

		@Override
		public MaAnim[] getMA() {
			return new MaAnim[] { MaAnim.newIns(loader.loadFile(base, id, MA)) };
		}

		@Override
		public MaModel getMM() {
			return MaModel.newIns(loader.loadFile(base, id, MM));
		}

		@Override
		public ResourceLocation getName() {
			return id;
		}

		@Override
		public FakeImage getNum() {
			return FakeImage.read(loader.loadFile(base, id, SP));
		}

		@Override
		public int getStatus() {
			return id.pack.equals("_local") ? 0 : 1;
		}

		@Override
		public VImg getUni() {
			return null;
		}
	}

	@StaticPermitted
	public static class SourceAnimLoader implements Source.AnimLoader {

		public static final String IC = "imgcut.txt";
		public static final String MM = "mamodel.txt";
		public static final String[] MA = { "maanim_walk.txt", "maanim_idle.txt", "maanim_attack.txt", "maanim_kb.txt",
				"maanim_burrow_down.txt", "maanim_burrow_move.txt", "maanim_burrow_up.txt" };
		public static final String SP = "sprite.png";
		public static final String EDI = "icon_display.png";
		public static final String UNI = "icon_deploy.png";

		private final ResourceLocation id;
		private final Source.SourceLoader loader;
		private final BasePath base;

		public SourceAnimLoader(BasePath base, ResourceLocation id, Source.SourceLoader loader) {
			this.id = id;
			this.base = base;
			this.loader = loader == null ? Workspace::loadAnimFile : loader;
		}

		@Override
		public VImg getEdi() {
			FileData edi = loader.loadFile(base, id, EDI);
			if (edi == null)
				return null;
			return new VImg(FakeImage.read(edi));
		}

		@Override
		public ImgCut getIC() {
			return ImgCut.newIns(loader.loadFile(base, id, IC));
		}

		@Override
		public MaAnim[] getMA() {
			MaAnim[] ans = new MaAnim[MA.length];
			for (int i = 0; i < MA.length; i++)
				ans[i] = MaAnim.newIns(loader.loadFile(base, id, MA[i]));
			return ans;
		}

		@Override
		public MaModel getMM() {
			return MaModel.newIns(loader.loadFile(base, id, MM));
		}

		@Override
		public ResourceLocation getName() {
			return id;
		}

		@Override
		public FakeImage getNum() {
			return FakeImage.read(loader.loadFile(base, id, SP));
		}

		@Override
		public int getStatus() {
			return id.pack.equals("_local") ? 0 : 1;
		}

		@Override
		public VImg getUni() {
			FileData uni = loader.loadFile(base, id, UNI);
			if (uni == null)
				return null;
			return new VImg(FakeImage.read(uni));
		}

	}

	public static class SourceAnimSaver {

		private final ResourceLocation id;
		private final AnimCI anim;

		public SourceAnimSaver(ResourceLocation name, AnimCI animCI) {
			this.id = name;
			this.anim = animCI;
		}

		/**
		 * Delete animation
		 * @param unload If this variable is true, it means that this method is called for completely deleting process
		 */
		public void delete(boolean unload) {
			if(unload)
				anim.unload();

			CommonStatic.ctx.noticeErr(
					() -> Context.delete(CommonStatic.ctx.getWorkspaceFile("./" + id.pack + "/animations/" + id.id)),
					ErrType.ERROR, "failed to delete animation: " + id);
		}

		public void saveAll() {
			saveData();
			saveImgs();
		}

		public void saveData() {
			try {
				write("imgcut.txt", anim.imgcut::write);
				write("mamodel.txt", anim.mamodel::write);
				write("maanim_walk.txt", anim.anims[0]::write);
				write("maanim_idle.txt", anim.anims[1]::write);
				write("maanim_attack.txt", anim.anims[2]::write);
				write("maanim_kb.txt", anim.anims[3]::write);
				write("maanim_burrow_down.txt", anim.anims[4]::write);
				write("maanim_burrow_move.txt", anim.anims[5]::write);
				write("maanim_burrow_up.txt", anim.anims[6]::write);
			} catch (IOException e) {
				CommonStatic.ctx.noticeErr(e, ErrType.ERROR, "Error during saving animation data: " + anim);
			}
		}

		public void saveIconDeploy() {
			if (anim.getUni() != null)
				CommonStatic.ctx.noticeErr(() -> write("icon_deploy.png", anim.getUni().getImg()), ErrType.ERROR,
						"Error during saving deploy icon: " + id);
		}

		public void saveIconDisplay() {
			if (anim.getEdi() != null)
				CommonStatic.ctx.noticeErr(() -> write("icon_display.png", anim.getEdi().getImg()), ErrType.ERROR,
						"Error during saving display icon: " + id);
		}

		public void saveImgs() {
			saveSprite();
			saveIconDisplay();
			saveIconDeploy();
		}

		public void saveSprite() {
			CommonStatic.ctx.noticeErr(() -> write("sprite.png", anim.getNum()), ErrType.ERROR,
					"Error during saving sprite sheet: " + id);
		}

		private void write(String type, Consumer<PrintStream> con) throws IOException {
			File f = CommonStatic.ctx.getWorkspaceFile("./" + id.pack + "/animations/" + id.id + "/" + type);
			Context.check(f);
			PrintStream ps = new PrintStream(f);
			con.accept(ps);
			ps.close();
		}

		private void write(String type, FakeImage img) throws IOException {
			File f = CommonStatic.ctx.getWorkspaceFile("./" + id.pack + "/animations/" + id.id + "/" + type);
			Context.check(f);
			Context.check(FakeImage.write(img, "PNG", f), "save", f);
		}

	}

	public static class Workspace extends Source {

		public static List<AnimCE> loadAnimations(String id) {
			if (id == null)
				id = ResourceLocation.LOCAL;
			File folder = CommonStatic.ctx.getWorkspaceFile("./" + id + "/" + BasePath.ANIM.toString() + "/");
			List<AnimCE> list = new ArrayList<>();
			if (!folder.exists() || !folder.isDirectory())
				return list;
			File[] files = folder.listFiles();
			Arrays.sort(files);
			for (File f : files) {
				String path = "./" + id + "/" + BasePath.ANIM.toString() + "/" + f.getName() + "/sprite.png";

				if (AnimCE.map().containsKey(f.getName()))
					list.add(AnimCE.map().get(f.getName()));
				else if (f.isDirectory() && CommonStatic.ctx.getWorkspaceFile(path).exists()) {
					AnimCE anim = new AnimCE(new ResourceLocation(id, f.getName()));

					list.add(anim);

					AnimCE.map().put(f.getName(), anim);
				}
			}
			return list;
		}

		public static void loadSouls(String id) { // TODO
			if (id == null)
				id = ResourceLocation.LOCAL;
			File folder = CommonStatic.ctx.getWorkspaceFile("./" + id + "/" + BasePath.SOUL.toString() + "/");
			if (!folder.exists() || !folder.isDirectory())
				return;
			File[] files = folder.listFiles();
			for (File f : files) {
				String path = "./" + id + "/" + BasePath.SOUL.toString() + "/" + f.getName() + "/sprite.png";
				//TODO
			}
		}

		public static void saveLocalAnimations() {
			AnimCE.map().values().forEach(AnimCE::save);
		}

		public static void saveWorkspace() {
			for (UserPack up : UserProfile.getUserPacks())
				if (up.source instanceof Workspace)
					CommonStatic.ctx.noticeErr(() -> ((Workspace) up.source).save(up), ErrType.WARN,
							"failed to save pack " + up.desc.name);
		}

		public static void validate(BasePath folder, ResourceLocation rl) {
			String id = rl.id;
			int num = 0;
			while (CommonStatic.ctx.getWorkspaceFile("./" + rl.pack + "/" + folder.toString() + "/" + rl.id).exists())
				rl.id = id + "_" + (num++);
		}

		public static String validateString(String str) {
			if (str == null || str.length() == 0)
				str = "no_name";
			str = str.replaceAll("[^0-9a-z_]", "_");
			if (str.charAt(0) < '0')
				str = "a_" + str;
			return str;
		}

		public static String validateWorkspace(String str) {
			String id = validateString(str);
			int num = 0;
			while (CommonStatic.ctx.getWorkspaceFile("./" + str).exists())
				str = id + "_" + (num++);
			return id;
		}

		public static String generatePackID() {
			String format = "abcdefghijklmnopqrstuvwxyz0123456789";
			Random random = new Random();

			StringBuilder result = new StringBuilder();

			while (result.length() < 8) {
				char ch = format.charAt((int) (random.nextFloat() * format.length()));

				result.append(ch);
			}

			return result.toString();
		}

		private static FileData loadAnimFile(BasePath base, ResourceLocation id, String str) {
			String path = "./" + id.pack + "/" + base.toString() + "/" + id.id + "/" + str;
			File f = CommonStatic.ctx.getWorkspaceFile(path);
			if (!f.exists())
				return null;
			return new FDFile(f);
		}

		public Workspace(String id) {
			super(id);
		}

		@Override
		public void delete() {
			try {
				Context.delete(getFile(""));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void export(UserPack pack, String password, String parentPassword, Consumer<Double> prog) throws Exception {
			ArrayList<AnimCE> anims = new ArrayList<>();

			for (Enemy e : pack.enemies) {
				AnimCE anim = (AnimCE) e.anim;
				if (anim.id.pack.equals(ResourceLocation.LOCAL)) {
					if(!anims.contains(anim)) {
						anims.add(anim);
					} else {
						anim.id.pack = ResourceLocation.LOCAL;
						anim.id.id = anim.id.id.replaceAll("^_mapped_", "");
					}

					new SourceAnimSaver(new ResourceLocation(pack.getSID(), "_mapped_"+anim.id.id), anim).saveAll();

					anim.id.pack = pack.getSID();
					anim.id.id = "_mapped_"+anim.id.id;
				}
				if (anim.id.pack.startsWith(".temp_"))
					anim.id.pack = anim.id.pack.substring(6);
			}
			for (Unit u : pack.units)
				for (Form f : u.forms) {
					AnimCE anim = (AnimCE) f.anim;
					if (anim.id.pack.equals(ResourceLocation.LOCAL)) {
						if(!anims.contains(anim)) {
							anims.add(anim);
						} else {
							anim.id.pack = ResourceLocation.LOCAL;
							anim.id.id = anim.id.id.replaceAll("^_mapped_", "");
						}

						new SourceAnimSaver(new ResourceLocation(pack.getSID(), "_mapped_"+anim.id.id), anim).saveAll();

						anim.id.pack = pack.getSID();
						anim.id.id = "_mapped_"+anim.id.id;
					}
					if (anim.id.pack.startsWith(".temp_"))
						anim.id.pack = anim.id.pack.substring(6);
				}
			for (StageMap sm : pack.mc.maps)
				for (Stage st : sm.list)
					for (Replay rep : st.recd)
						if (rep != null && rep.rl.pack.startsWith(".temp_")) {
							rep.rl.pack = rep.rl.pack.substring(6);
						}
			save(pack);
			String star = id.startsWith(".temp_") ? "./packs/" : "./exports/";
			File tar = CommonStatic.ctx.getAuxFile(star + pack.getSID() + ".pack.bcuzip");
			File dst = CommonStatic.ctx.getAuxFile(star + ".pack.bcuzip.temp");
			File src = CommonStatic.ctx.getWorkspaceFile("./" + id);
			if (tar.exists())
				Context.delete(tar);
			Context.check(dst);

			PackData.PackDesc desc = pack.desc.clone();

			if (parentPassword != null) {
				desc.parentPassword = PackLoader.getMD5(parentPassword.getBytes(StandardCharsets.UTF_8), 16);
			} else {
				desc.parentPassword = null;
			}

			PackLoader.writePack(dst, src, desc, password, prog);
			Context.renameTo(dst, tar);

			for(AnimCE anim : anims) {
				anim.id.pack = ResourceLocation.LOCAL;
				anim.id.id = anim.id.id.replaceAll("^_mapped_", "");
			}
		}

		public File getBGFile(Identifier<Background> id) {
			return getFile("./" + BasePath.BG.toString() + "/" + Data.trio(id.id) + ".png");
		}

		public File getCasFile(Identifier<CastleImg> id) {
			return getFile("./" + BasePath.CASTLE.toString() + "/" + Data.trio(id.id) + ".png");
		}

		public File getTraitIconFile(Identifier<Trait> id) {
			return getFile("./" + TRAITICON + "/" + Data.trio(id.id) + ".png");
		}

		@Override
		public FileData getFileData(String string) {
			return new FDFile(getFile(string));
		}

		@Override
		public String[] listFile(String path) {
			return getFile(path).list();
		}

		@Override
		public AnimCE loadAnimation(String name) {
			return new AnimCE(new ResourceLocation(id, name));
		}

		@Override
		public VImg readImage(String path, int ind) {
			VFile vf = VFile.getFile(getFile(path + "/" + Data.trio(ind) + ".png"));

			if(vf == null) {
				return null;
			} else {
				return new VImg(vf);
			}
		}

		@Override
		public InputStream streamFile(String path) throws IOException {
			return new FileInputStream(getFile(path));
		}

		public OutputStream writeFile(String path) throws IOException {
			File f = getFile(path);
			Context.check(f);
			return new FileOutputStream(f);
		}

		protected void save(UserPack up) throws IOException {
			File f = getFile("pack.json");
			Context.check(f);
			OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8);
			fw.write(JsonEncoder.encode(up).toString());
			fw.close();
		}

		private File getFile(String path) {
			return CommonStatic.ctx.getWorkspaceFile("./" + id + "/" + path);
		}

	}

	public static class ZipSource extends Source {

		public final ZipDesc zip;

		public ZipSource(ZipDesc desc) {
			super(desc.desc.id);
			zip = desc;
		}

		@Override
		public void delete() {
			zip.delete();
		}

		@Override
		public FileData getFileData(String string) {
			return zip.tree.find(string).getData();
		}

		public File getPackFile() {
			return zip.getZipFile();
		}

		@Override
		public String[] listFile(String path) {
			VFile dir = zip.tree.find(path);
			if (dir == null)
				return null;
			Collection<VFile> col = dir.list();
			if (col == null)
				return null;
			String[] ans = new String[col.size()];
			int i = 0;
			for (VFile vf : col) {
				ans[i++] = vf.name;
			}
			return ans;
		}

		@Override
		public AnimCI loadAnimation(String name) {
			return new AnimCI(new SourceAnimLoader(BasePath.ANIM, new ResourceLocation(id, name), this::loadAnimationFile));
		}

		@Override
		public VImg readImage(String path, int ind) {
			String fullPath = path.startsWith("./") ? path + "/" + Data.trio(ind) + ".png"
					: "./" + path + "/" + Data.trio(ind) + ".png";

			return new VImg(zip.tree.find(fullPath));
		}

		@Override
		public InputStream streamFile(String path) throws Exception {
			return zip.readFile(path);
		}

		public Workspace unzip(String password, Consumer<Double> prog) throws Exception {
			if (!zip.match(PackLoader.getMD5(password.getBytes(StandardCharsets.UTF_8), 16)))
				return null;
			File f = CommonStatic.ctx.getWorkspaceFile("./" + id + "/pack.json");
			File folder = f.getParentFile();
			if (folder.exists()) {
				if (!CommonStatic.ctx.confirmDelete(f))
					return null;
				Context.delete(f);
			}
			if (!folder.exists())
				Context.check(folder.mkdirs(), "create", folder);
			if (!f.exists())
				Context.check(f.createNewFile(), "create", f);
			Workspace ans = new Workspace(id);
			zip.unzip(id -> {
				File file = ans.getFile(id);
				Context.check(file);
				return file;
			}, prog);
			return ans;
		}

		private FileData loadAnimationFile(BasePath base, ResourceLocation id, String path) {
			VFile vf = zip.tree.find("./" + base.toString() + "/" + id.id + "/" + path);
			return vf == null ? null : vf.getData();
		}

	}

	public static enum BasePath {
		ANIM("animations"),
		BG("backgrounds"),
		CASTLE("castles"),
		MUSIC("musics"),
		REPLAY("replays"),
		SOUL("souls"),
		TRAIT("traitIcons");

		private final String path;

		BasePath(String str) {
			path = str;
		}

		public String toString() {
			return path;
		}

	}

	public final String id;

	public Source(String id) {
		this.id = id;
	}

	public abstract void delete();

	public abstract FileData getFileData(String string);

	public abstract String[] listFile(String path);

	public abstract AnimCI loadAnimation(String name);

	/**
	 * read images from file. Use it
	 */
	//TODO: might be able to use BasePath for path
	public abstract VImg readImage(String path, int ind);

	/**
	 * used for streaming music. Do not use it for images and small text files
	 */
	public abstract InputStream streamFile(String path) throws Exception;

}
