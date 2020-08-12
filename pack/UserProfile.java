package common.pack;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.Map.Entry;

import common.io.PackLoader;
import common.io.PackLoader.ZipDesc;
import common.io.json.JsonDecoder;
import common.pack.Context.ErrType;
import common.pack.FixIndexList.FixIndexMap;
import common.pack.PackData.DefPack;
import common.pack.PackData.Identifier;
import common.pack.PackData.Indexable;
import common.pack.PackData.PackDesc;
import common.pack.PackData.UserPack;
import common.pack.Source.Workspace;
import common.pack.Source.ZipSource;
import common.util.pack.Background;
import common.util.pack.Soul;
import common.util.stage.CastleImg;
import common.util.stage.CastleList;
import common.util.stage.Music;
import common.util.unit.AbEnemy;
import common.util.unit.EneRand;
import common.util.unit.Enemy;
import common.util.unit.Unit;

public class UserProfile {

	private static final String REG_POOL = "_pools";
	private static final String REG_STATIC = "_statics";

	// FIXME load it into register
	private static UserProfile profile;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<T> getAll(String pack, Class<T> cls) {
		List<PackData> list = new ArrayList<>();
		list.add(profile.def);
		if (pack != null) {
			UserPack userpack = profile.packmap.get(pack);
			list.add(userpack);
			for (String dep : userpack.desc.dependency)
				list.add(getPack(dep));
		}
		List ans = new ArrayList<>();
		for (PackData data : list)
			getList(data, cls, l -> ans.addAll(l.getList()));
		return ans;
	}

	public static DefPack getBCData() {
		return profile.def;
	}

	public static PackData getPack(String str) {
		if (str.equals(Identifier.DEF))
			return profile.def;
		return profile.packmap.get(str);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Set<T> getPool(String id, Class<T> cls) {
		Map<String, Set> pool = getRegister(REG_POOL, Set.class);
		Set<T> ans = pool.get(id);
		if (ans == null)
			pool.put(id, ans = new HashSet<>());
		return ans;
	}

	@SuppressWarnings("unchecked")
	public static <T> Map<String, T> getRegister(String id, Class<T> cls) {
		Map<String, T> ans = (Map<String, T>) profile.registers.get(id);
		if (ans == null)
			profile.registers.put(id, ans = new HashMap<>());
		return ans;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getStatic(String id, Supplier<T> def) {
		Map<String, Object> pool = getRegister(REG_STATIC, Object.class);
		T ans = (T) pool.get(id);
		if (ans == null)
			pool.put(id, ans = def.get());
		return ans;
	}

	public static UserPack getUserPack(String id) {
		return profile.packmap.get(id);
	}

	public static PackData.UserPack initJsonPack(String id) throws Exception {
		File f = Source.ctx.getWorkspaceFile("./" + id + "/pack.json");
		File folder = f.getParentFile();
		if (folder.exists()) {
			if (!Source.ctx.confirmDelete())
				return null;
			Context.delete(f);
		}
		Context.check(folder.mkdirs(), "create", folder);
		Context.check(f.createNewFile(), "create", f);
		return new PackData.UserPack(id);
	}

	public static Collection<UserPack> packs() {
		return profile.packmap.values();
	}

	public static UserPack readJsonPack(File f) throws Exception {
		File folder = f.getParentFile();
		Reader r = new FileReader(f);
		JsonElement elem = JsonParser.parseReader(r);
		r.close();
		PackDesc desc = JsonDecoder.decode(elem.getAsJsonObject().get("desc"), PackDesc.class);
		UserPack data = new UserPack(new Workspace(folder.getName()), desc, elem);
		return data;
	}

	public static UserPack readZipPack(File f) throws Exception {
		ZipDesc zip = PackLoader.readPack(Source.ctx::preload, f);
		Reader r = new InputStreamReader(zip.readFile("./main.pack.json"));
		JsonElement elem = JsonParser.parseReader(r);
		UserPack data = new UserPack(new ZipSource(zip), zip.desc, elem);
		r.close();
		return data;
	}

	public static void setStatic(String id, Object val) {
		getRegister(REG_STATIC, Object.class).put(id, val);
	}

	@SuppressWarnings("unchecked")
	static <T extends Indexable<?>> T get(Identifier<T> id) {
		if (id.cls == CastleImg.class)
			return (T) CastleList.map().get(id.pack).get(id.id);
		PackData data = getPack(id.pack);
		if (data == null)
			return null;
		Object[] ans = new Object[1];
		getList(data, id.cls, l -> ans[0] = ans[0] == null ? l.get(id.id) : ans[0]);
		return (T) ans[0];
	}

	@SuppressWarnings({ "rawtypes" })
	private static void getList(PackData data, Class cls, Consumer<FixIndexMap> func) {
		if (cls == Unit.class)
			func.accept(data.units);
		if (cls == Enemy.class || cls == AbEnemy.class)
			func.accept(data.enemies);
		if (cls == EneRand.class || cls == AbEnemy.class)
			func.accept(data.randEnemies);
		if (cls == Background.class)
			func.accept(data.bgs);
		if (cls == Soul.class)
			func.accept(data.souls);
		if (cls == Music.class)
			func.accept(data.musics);
	}

	public String username;

	public byte[] password;

	public final DefPack def = new DefPack();

	public final Map<String, UserPack> packmap = new HashMap<>();
	public final Set<UserPack> packlist = new HashSet<>();
	public final Set<UserPack> failed = new HashSet<>();
	private final Map<String, Map<String, ?>> registers = new HashMap<>();
	private Map<String, UserPack> pending = new HashMap<>();

	public UserProfile() {
		// TODO load username and password
		Source.ctx.noticeErr(VerFixer::fix, ErrType.FATAL, "failed to convert old format");
		File packs = Source.ctx.getPackFolder();
		File workspace = Source.ctx.getWorkspaceFile(".");

		if (packs.exists())
			for (File f : packs.listFiles())
				if (f.getName().endsWith(".pack.bcuzip")) {
					UserPack pack = Source.ctx.noticeErr(() -> readZipPack(f), ErrType.WARN,
							"failed to load external pack " + f);
					if (pack != null)
						pending.put(pack.desc.id, pack);
				}

		if (workspace.exists())
			for (File f : packs.listFiles())
				if (f.isDirectory()) {
					File main = Source.ctx.getWorkspaceFile("./" + f.getName() + "/main.pack.json");
					if (!main.exists())
						continue;
					UserPack pack = Source.ctx.noticeErr(() -> readJsonPack(main), ErrType.WARN,
							"failed to load workspace pack " + f);
					if (pack != null)
						pending.put(pack.desc.id, pack);
				}
		Set<UserPack> queue = new HashSet<>(pending.values());
		while (queue.removeIf(this::add))
			;
		pending = null;
		packlist.addAll(failed);
	}

	public boolean canRemove(String id) {
		for (Entry<String, UserPack> ent : packmap.entrySet())
			if (ent.getValue().desc.dependency.contains(id))
				return false;
		return true;
	}

	public void remove(UserPack pack) {
		packmap.remove(pack.desc.id);
		packlist.remove(pack);
	}

	/**
	 * return true if the pack is attempted to load and should be removed from the
	 * loading queue
	 */
	private boolean add(UserPack pack) {
		packlist.add(pack);
		if (!canAdd(pack))
			return false;
		if (!Source.ctx.noticeErr(pack::load, ErrType.WARN, "failed to load pack " + pack.desc)) {
			failed.add(pack);
			return true;
		}
		packmap.put(pack.desc.id, pack);
		return true;
	}

	private boolean canAdd(UserPack s) {
		for (String dep : s.desc.dependency)
			if (!packmap.containsKey(dep))
				return false;
		return true;
	}

}