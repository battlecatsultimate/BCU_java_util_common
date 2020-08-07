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
import common.system.fake.FakeImage;
import common.system.fake.ImageBuilder;
import common.util.Data;
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

		public UserProfile(Context ctx) {
			// TODO load username and password
			File packs = ctx.getPackFolder();
			File workspace = ctx.getWorkspaceFile(".");
			Map<String, Source> set = new HashMap<>();
			if (packs.exists())
				for (File f : packs.listFiles())
					if (f.getName().endsWith(".pack.bcuzip"))
						try {
							Source s = readZipPack(ctx, f);
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
							Source s = readJsonPack(ctx, main);
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

		private File folder;

		private Workspace(File root, PackData data) {
			super(data);
			folder = root;
		}

		@Override
		public FakeImage readImage(Context ctx, String path) throws IOException {
			return ImageBuilder.builder.build(getFile(ctx, path));
		}

		@Override
		public InputStream streamFile(Context ctx, String path) throws IOException {
			return new FileInputStream(getFile(ctx, path));
		}

		public OutputStream writeFile(Context ctx, String path) throws IOException {
			File f = getFile(ctx, path);
			Context.check(f);
			return new FileOutputStream(f);
		}

		private File getFile(Context ctx, String path) {
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
		public FakeImage readImage(Context ctx, String path) throws Exception {
			return zip.tree.find(path).getData().getImg();
		}

		@Override
		public InputStream streamFile(Context ctx, String path) throws Exception {
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
				File file = ans.getFile(ctx, id);
				Context.check(file.createNewFile(), "create", file);
				return file;
			});
			profile.add(ans);
			return ans;
		}

	}

	public static Workspace initJsonPack(Context ctx, String id) throws Exception {
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

	public static Workspace readJsonPack(Context ctx, File f) throws Exception {
		File folder = f.getParentFile();
		Reader r = new FileReader(f);
		PackData data = JsonDecoder.decode(JsonParser.parseReader(r), PackData.class);
		return new Workspace(folder, data);
	}

	public static ZipSource readZipPack(Context ctx, File f) throws Exception {
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
	public abstract FakeImage readImage(Context ctx, String path) throws Exception;

	/** used for streaming music. Do not use it for images and small text files */
	public abstract InputStream streamFile(Context ctx, String path) throws Exception;

}
