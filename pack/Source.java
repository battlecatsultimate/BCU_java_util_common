package common.pack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;

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
import common.system.files.FileData;
import common.util.Data;
import common.util.anim.AnimCE;
import common.util.anim.AnimCI;
import common.util.anim.ImgCut;
import common.util.anim.MaAnim;
import common.util.anim.MaModel;
import common.util.unit.AbEnemy;
import main.MainBCU;

public abstract class Source {

	public static interface AnimLoader {
		public VImg getEdi();

		public ImgCut getIC();

		public MaAnim[] getMA();

		public MaModel getMM();

		public Identifier getName();

		public FakeImage getNum();

		public int getStatus();

		public VImg getUni();
	}

	public static interface Context {

		public static interface RunExc {

			public void run() throws Exception;

		}

		public static interface SupExc<T> {

			public T get() throws Exception;

		}

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

		public File getWorkspaceFile(String relativePath);

		public void noticeErr(Exception e, String str);

		public default void noticeErr(RunExc r, String str) {
			try {
				r.run();
			} catch (Exception e) {
				noticeErr(e, str);
			}
		}

		public default <T> T noticeError(SupExc<T> r, String str) {
			try {
				return r.get();
			} catch (Exception e) {
				noticeErr(e, str);
				return null;
			}
		}

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

		public String pack;

		public String id;

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

		@Override
		public String toString() {
			return pack + "/" + id;
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

	public static class SourceAnimLoader implements Source.AnimLoader {

		public static interface SourceLoader {

			public FileData loadFile(Identifier id, String str);

		}

		private static final String IC = "imgcut.txt";
		private static final String MM = "mamodel.txt";
		private static final String[] MA = { "maanim_walk.txt", "maanim_idle.txt", "maanim_attack.txt", "maanim_kb.txt",
				"maanim_burrow_down.txt", "maanim_burrow_move.txt", "maanim_burrow_up.txt" };
		private static final String SP = "sprite.png";
		private static final String EDI = "icon_display.png";
		private static final String UNI = "icon_deploy.png";

		private final Identifier id;
		private final SourceLoader loader;

		public SourceAnimLoader(Identifier id, SourceLoader loader) {
			this.id = id;
			this.loader = loader == null ? Workspace::loadFile : loader;
		}

		@Override
		public VImg getEdi() {
			FileData edi = loader.loadFile(id, EDI);
			if (edi == null)
				return null;
			return ctx.noticeError(() -> new VImg(FakeImage.read(edi)), "failed to read Display Icon" + id);
		}

		@Override
		public ImgCut getIC() {
			return ImgCut.newIns(loader.loadFile(id, IC));
		}

		@Override
		public MaAnim[] getMA() {
			MaAnim[] ans = new MaAnim[MA.length];
			for (int i = 0; i < MA.length; i++)
				ans[i] = MaAnim.newIns(loader.loadFile(id, MA[i]));
			return ans;
		}

		@Override
		public MaModel getMM() {
			return MaModel.newIns(loader.loadFile(id, MM));
		}

		@Override
		public Identifier getName() {
			return id;
		}

		@Override
		public FakeImage getNum() {
			return ctx.noticeError(() -> FakeImage.read(loader.loadFile(id, SP)), "failed to read sprite sheet " + id);
		}

		@Override
		public int getStatus() {
			return id.pack.equals("_local") ? 0 : 1;
		}

		@Override
		public VImg getUni() {
			FileData uni = loader.loadFile(id, UNI);
			if (uni == null)
				return null;
			return ctx.noticeError(() -> new VImg(FakeImage.read(uni)), "failed to read deploy icon " + id);
		}

	}

	public static class SourceAnimSaver {

		private final Identifier id;
		private final AnimCE anim;

		public SourceAnimSaver(Identifier id, AnimCE anim) {
			this.id = id;
			this.anim = anim;
		}

		public void delete() {
			ctx.noticeErr(() -> Context.delete(ctx.getWorkspaceFile("./" + id.pack + "/animations/" + id.id)),
					"failed to delete animation: " + id);
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
				ctx.noticeErr(e, "Error during saving animation data: " + anim);
			}
		}

		public void saveIconDeploy() {
			if (anim.getUni() != null)
				ctx.noticeErr(() -> write("icon_deploy.png", anim.getUni().getImg()),
						"Error during saving deploy icon: " + id);
		}

		public void saveIconDisplay() {
			if (anim.getEdi() != null)
				ctx.noticeErr(() -> write("icon_display.png", anim.getEdi().getImg()),
						"Error during saving display icon: " + id);
		}

		public void saveImgs() {
			saveSprite();
			saveIconDisplay();
			saveIconDeploy();
		}

		public void saveSprite() {
			ctx.noticeErr(() -> write("sprite.png", anim.getNum()), "Error during saving sprite sheet: " + id);
		}

		private void write(String type, Consumer<PrintStream> con) throws IOException {
			File f = ctx.getWorkspaceFile("./" + id.pack + "/animations/" + id.id + "/" + type);
			Context.check(f);
			PrintStream ps = new PrintStream(f);
			con.accept(ps);
			ps.close();
		}

		private void write(String type, FakeImage img) throws IOException {
			File f = ctx.getWorkspaceFile("./" + id.pack + "/animations/" + id.id + "/" + type);
			Context.check(f);
			Context.check(FakeImage.write(img, "PNG", f), "save", f);
		}

	}

	public static class UserProfile {

		//FIXME load it into register
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

		public static List<AnimCI> loadAnimations(String id) {
			File folder = ctx.getWorkspaceFile("./" + id + "/animations/");
			List<AnimCI> list = new ArrayList<>();
			if (!folder.exists() || !folder.isDirectory())
				return list;
			for (File f : folder.listFiles()) {
				String path = "./" + id + "/animations/" + f.getName() + "/sprite.png";
				if (f.isDirectory() && ctx.getWorkspaceFile(path).exists())
					list.add(new AnimCI(new SourceAnimLoader(new Identifier(id, f.getName()), null)));
			}
			return list;
		}

		private static FileData loadFile(Identifier id, String str) {
			return new FDFile(ctx.getWorkspaceFile("./" + id.pack + "/animations/" + id.id + "/" + str));
		}

		private File folder;

		private Workspace(File root, PackData data) {
			super(data);
			folder = root;
		}

		@Override
		public AnimCI loadAnimation(String name) {
			return new AnimCI(new SourceAnimLoader(new Identifier(folder.getName(), name), null));
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

	}

	public static class ZipSource extends Source {

		private final ZipDesc zip;

		private ZipSource(ZipDesc desc, PackData data) {
			super(data);
			zip = desc;
		}

		@Override
		public AnimCI loadAnimation(String name) {
			return new AnimCI(new SourceAnimLoader(new Identifier(data.desc.id, name), this::loadAnimationFile));
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
			File f = ctx.getWorkspaceFile("./" + data.desc.id + "/main.pack.json");
			File folder = f.getParentFile();
			if (folder.exists()) {
				if (!ctx.confirmDelete())
					return null;
				Context.delete(f);
			}
			Context.check(folder.mkdirs(), "create", folder);
			Context.check(f.createNewFile(), "create", f);
			UserProfile.profile.remove(this);
			Workspace ans = new Workspace(folder, data);
			zip.unzip(id -> {
				File file = ans.getFile(id);
				Context.check(file.createNewFile(), "create", file);
				return file;
			});
			UserProfile.profile.add(ans);
			return ans;
		}

		private FileDesc loadAnimationFile(Identifier id, String path) {
			return zip.tree.find("./animations/" + id.id + "/" + path).getData();
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

	public abstract AnimCI loadAnimation(String name);

	/** read images from file. Use it */
	public abstract FakeImage readImage(String path) throws Exception;

	/** used for streaming music. Do not use it for images and small text files */
	public abstract InputStream streamFile(String path) throws Exception;

}
