package common.pack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonParser;

import common.io.json.JsonClass;
import common.io.json.JsonClass.JCConstructor;
import common.io.json.JsonClass.NoTag;
import common.pack.PackLoader.ZipDesc;
import common.pack.PackLoader.ZipDesc.FileDesc;
import common.io.json.JsonDecoder;
import common.io.json.JsonField;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.system.fake.ImageBuilder;
import common.system.files.FDFile;
import common.util.Data;
import common.util.anim.AnimCE;
import common.util.anim.AnimCI;
import common.util.anim.ImgCut;
import common.util.anim.MaAnim;
import common.util.anim.MaModel;
import common.util.unit.AbEnemy;
import main.MainBCU;

public abstract class Source {

	public static interface Context {

		public static void check(boolean bool, String str, File f) throws IOException {
			if (bool)
				throw new IOException("failed to " + str + " file " + f);
		}

		public static void check(File f) throws IOException {
			if (!f.getParentFile().exists())
				check(f.getParentFile().mkdirs(), "create", f);
			if (!f.exists())
				check(f.createNewFile(), "create", f);
		}

		public static void delete(File f) throws IOException {
			if (f == null || !f.exists())
				return;
			if (f.isDirectory())
				for (File i : f.listFiles())
					delete(i);
			check(!f.delete(), "delete", f);

		}

		public boolean confirmDelete();

		public File getLangFile(String file);

		public File getPackFolder();

		public <T> T getStore(Class<T> cls);

		public File getWorkspaceFile(String relativePath);

		public void noticeErr(Exception e, String str);

		public boolean preload(FileDesc desc);

	}

	@JsonClass(bypass = true)
	public static class EntryStore<T extends Indexable> {

		@JsonField
		public final ArrayList<T> list = new ArrayList<>();

		public EntryStore() {
		}

		public int add(T t) {
			list.add(t);
			return list.size() - 1;
		}

		public T get(String id) {
			for (T t : list)
				if (t.getID().id.equals(id))
					return t;
			return null;
		}

		public boolean remove(T t) {
			return list.remove(t);
		}

	}

	@JsonClass(noTag = NoTag.LOAD)
	public static class Identifier implements Comparable<Identifier> {

		public static Identifier parseInt(int v) {
			int pack = v / 1000;
			int id = v % 1000;
			return new Identifier("" + pack, "" + id);
		}

		public final String pack;

		public final String id;

		public Identifier() {
			pack = null;
			id = null;
		}

		public Identifier(String pack, String id) {
			this.pack = pack;
			this.id = id;
		}

		@Override
		public int compareTo(Identifier o) {
			int val = pack.compareTo(o.pack);
			if (val != 0)
				return val;
			return id.compareTo(o.id);
		}

		public boolean equals(Identifier o) {
			return pack.equals(o.pack) && id.equals(o.id);
		}
	}

	public static interface Indexable {

		public Identifier getID();

	}

	@JsonClass
	public static class PackData {

		@JsonField
		public final PackDesc desc;

		// TODO enemy list, unit list, unit level list, bg list, castle list, music
		// list, MapColc

		/** for generating new pack only */
		private PackData(String id) {
			desc = new PackDesc(id);
		}

	}

	@JsonClass(noTag = NoTag.LOAD)
	public static class PackDesc {
		public String BCU_VERSION;
		public String id;
		public String author;
		public String name;
		public String desc;
		@JsonField(generic = String.class)
		public ArrayList<String> dependency;

		@JCConstructor
		public PackDesc() {
		}

		private PackDesc(String id) {
			BCU_VERSION = Data.revVer(MainBCU.ver);
			this.id = id;
			this.dependency = new ArrayList<>();
		}
	}

	public static class UserProfile {

		private static UserProfile profile;

		public static AbEnemy getEnemy(Identifier id) {
			Source pack = getProfile().packmap.get(id.pack);
			if (pack == null)
				return null;
			return null;// pack.data.enemylist.get(id.id);
		}

		public static UserProfile getProfile() {
			return profile;
		}

		public String username;

		public byte[] password;
		public final Map<String, Source> packmap = new HashMap<>();
		public final Set<Source> packlist = new HashSet<>();

		public final Set<Source> failed = new HashSet<>();

		public UserProfile() {
			// TODO load username and password
			File packs = ctx.getPackFolder();
			File workspace = ctx.getWorkspaceFile(".");
			Map<String, Source> set = new HashMap<>();
			if (packs.exists())
				for (File f : packs.listFiles())
					if (f.getName().endsWith(".pack.bcuzip"))
						try {
							Source s = readZipPack(f);
							set.put(s.data.desc.id, s);
						} catch (Exception e) {
							ctx.noticeErr(e, "failed to load external pack " + f);
						}
			if (workspace.exists())
				for (File f : packs.listFiles())
					if (f.isDirectory()) {
						File main = ctx.getWorkspaceFile("./" + f.getName() + "/main.pack.json");
						if (!main.exists())
							continue;
						try {
							Source s = readJsonPack(main);
							set.put(s.data.desc.id, s);
						} catch (Exception e) {
							ctx.noticeErr(e, "failed to load workspace pack " + f);
						}
					}
			failed.addAll(set.values());
			while (failed.removeIf(this::add))
				;
			packlist.addAll(failed);
		}

		public boolean add(Source pack) {
			packlist.add(pack);
			if (!canAdd(pack))
				return false;
			packmap.put(pack.data.desc.id, pack);
			pack.loadable = true;
			return true;
		}

		public boolean canAdd(Source s) {
			for (String dep : s.data.desc.dependency)
				if (!packmap.containsKey(dep))
					return false;
			return true;
		}

		public boolean canRemove(String id) {
			for (Entry<String, Source> ent : packmap.entrySet())
				if (ent.getValue().data.desc.dependency.contains(id))
					return false;
			return true;
		}

		public void remove(Source pack) {
			packmap.remove(pack.data.desc.id);
			packlist.remove(pack);
		}

	}

	public static class Workspace extends Source {

		public static class SourceAnimLoader implements AnimCI.AnimLoader {

			private final File ic, mm, sp, edi, uni;
			private final File[] ma = new File[7];
			private final Identifier id;

			private SourceAnimLoader(Identifier id) {
				this.id = id;
				sp = loadFile("sprite.png", id.id + ".png");
				edi = loadFile("icon_display.png", "edi.png");
				uni = loadFile("icon_deploy.png", "uni.png");
				ic = loadFile("imgcut.txt", id.id + ".imgcut");
				mm = loadFile("mamodel.txt", id.id + ".mamodel");
				ma[0] = loadFile("maanim_walk.txt", id.id + "00.maanim");
				ma[1] = loadFile("maanim_idle.txt", id.id + "01.maanim");
				ma[2] = loadFile("maanim_attack.txt", id.id + "02.maanim");
				ma[3] = loadFile("maanim_kb.txt", id.id + "03.maanim");
				ma[4] = loadFile("maanim_burrow_down.txt", id.id + "_zombie00.maanim");
				ma[5] = loadFile("maanim_burrow_move.txt", id.id + "_zombie01.maanim");
				ma[6] = loadFile("maanim_burrow_up.txt", id.id + "_zombie02.maanim");
			}

			private File loadFile(String... options) {
				File def = null;
				for (String str : options) {
					File f = ctx.getWorkspaceFile("./" + id.pack + "/animations/" + id.id + "/" + str);
					if (def == null)
						def = f;
					if (f.exists())
						return f;
				}
				return def;
			}

			@Override
			public VImg getEdi() {
				if (!edi.exists())
					return null;
				try {
					return new VImg(FakeImage.read(edi));
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			public ImgCut getIC() {
				return ImgCut.newIns(new FDFile(ic));
			}

			@Override
			public MaAnim[] getMA() {
				MaAnim[] ans = new MaAnim[ma.length];
				for(int i=0;i<ma.length;i++)
					ans[i] = MaAnim.newIns(new FDFile(ma[i]));
				return ans;
			}

			@Override
			public MaModel getMM() {
				return MaModel.newIns(new FDFile(mm));
			}

			@Override
			public String getName() {
				return id.id;
			}

			@Override
			public FakeImage getNum(boolean load) {
				try {
					return FakeImage.read(sp);
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			public int getStatus() {
				return id.pack.equals("_local") ? 0 : 1;
			}

			@Override
			public VImg getUni() {
				if (!uni.exists())
					return null;
				try {
					return new VImg(FakeImage.read(uni));
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}

			public void saveNum() {
				
			}
			
		}

		public static AnimCI[] loadAnimations(String id) throws Exception {
			File folder = ctx.getWorkspaceFile("./" + id + "/animations/");
			if (!folder.exists() || !folder.isDirectory())
				return new AnimCE[0];
			List<AnimCI> list = new ArrayList<>();
			for (File f : folder.listFiles()) {
				String path = "./" + id + "/animations/" + f.getName() + "/sprite.png";
				if (f.isDirectory() && ctx.getWorkspaceFile(path).exists())
					list.add(new AnimCI(new SourceAnimLoader(new Identifier(id, f.getName()))));
			}
			return list.toArray(new AnimCI[0]);
		}

		private File folder;

		private Workspace(File root, PackData data) {
			super(data);
			folder = root;
		}

		@Override
		public FakeImage readImage(String path) throws IOException {
			return ImageBuilder.builder.build(getFile(path));
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

		private File getFile(String path) {
			return ctx.getWorkspaceFile("./" + folder.getName() + "/" + path);
		}

		@Override
		public AnimCI[] loadAnimations() throws Exception {
			return loadAnimations("./" + folder.getName());
		}

	}

	public static class ZipSource extends Source {

		private final ZipDesc zip;

		private ZipSource(ZipDesc desc, PackData data) {
			super(data);
			zip = desc;
		}

		@Override
		public FakeImage readImage(String path) throws Exception {
			return zip.tree.find(path).getData().getImg();
		}

		@Override
		public InputStream streamFile(String path) throws Exception {
			return zip.readFile(path);
		}

		public Workspace unzip(Context ctx, String password) throws Exception {
			if (!zip.match(PackLoader.getMD5(password.getBytes(), 16)))
				return null;
			UserProfile profile = ctx.getStore(UserProfile.class);
			File f = ctx.getWorkspaceFile("./" + data.desc.id + "/main.pack.json");
			File folder = f.getParentFile();
			if (folder.exists()) {
				if (!ctx.confirmDelete())
					return null;
				Context.delete(f);
			}
			Context.check(folder.mkdirs(), "create", folder);
			Context.check(f.createNewFile(), "create", f);
			profile.remove(this);
			Workspace ans = new Workspace(folder, data);
			zip.unzip(id -> {
				File file = ans.getFile(id);
				Context.check(file.createNewFile(), "create", file);
				return file;
			});
			profile.add(ans);
			return ans;
		}

		@Override
		public AnimCI[] loadAnimations() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

	}

	public static Context ctx;

	public static Workspace initJsonPack(String id) throws Exception {
		File f = ctx.getWorkspaceFile("./" + id + "/main.pack.json");
		File folder = f.getParentFile();
		if (folder.exists()) {
			if (!ctx.confirmDelete())
				return null;
			Context.delete(f);
		}
		Context.check(folder.mkdirs(), "create", folder);
		Context.check(f.createNewFile(), "create", f);
		return new Workspace(folder, new PackData(id));
	}

	public static Workspace readJsonPack(File f) throws Exception {
		File folder = f.getParentFile();
		Reader r = new FileReader(f);
		PackData data = JsonDecoder.decode(JsonParser.parseReader(r), PackData.class);
		return new Workspace(folder, data);
	}

	public static ZipSource readZipPack(File f) throws Exception {
		ZipDesc zip = PackLoader.readPack(ctx::preload, f);
		Reader r = new InputStreamReader(zip.readFile("./main.pack.json"));
		PackData data = JsonDecoder.decode(JsonParser.parseReader(r), PackData.class);
		r.close();
		return new ZipSource(zip, data);
	}

	public final PackData data;

	private boolean loadable;

	private Source(PackData da) {
		data = da;
	}

	public boolean loadable() {
		return loadable;
	}

	/** read images from file. Use it */
	public abstract FakeImage readImage(String path) throws Exception;

	/** used for streaming music. Do not use it for images and small text files */
	public abstract InputStream streamFile(String path) throws Exception;

	public abstract AnimCI[] loadAnimations() throws Exception;

}
