package common.pack;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import common.CommonStatic;
import common.io.PackLoader;
import common.io.PackLoader.ZipDesc;
import common.io.assets.Admin.StaticPermitted;
import common.io.assets.AssetLoader;
import common.io.json.JsonDecoder;
import common.pack.Context.ErrType;
import common.pack.PackData.DefPack;
import common.pack.PackData.PackDesc;
import common.pack.PackData.UserPack;
import common.pack.Source.Workspace;
import common.pack.Source.ZipSource;
import common.util.Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UserProfile {

	private static final String REG_POOL = "_pools";
	private static final String REG_STATIC = "_statics";
	protected static final String CURRENT_PACK = "_current_pack";

	@StaticPermitted(StaticPermitted.Type.ENV)
	private static UserProfile profile = null;

	public static boolean canRemove(String id) {
		for (Entry<String, UserPack> ent : profile().packmap.entrySet())
			if (ent.getValue().desc.dependency.contains(id))
				return false;
		return true;
	}

	/**
	 * get all available items for a pack, except castle
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<T> getAll(String pack, Class<T> cls) {
		List<PackData> list = new ArrayList<>();
		list.add(profile().def);
		if (pack != null && !pack.equals(Identifier.DEF)) {
			UserPack userpack = profile().packmap.get(pack);
			list.add(userpack);
			for (String dep : userpack.desc.dependency)
				list.add(getPack(dep));
		}
		List ans = new ArrayList<>();
		for (PackData data : list)
			data.getList(cls, (r, l) -> ans.addAll(l.getList()), null);
		return ans;
	}

	/**
	 * get all packs, including default pack
	 */
	public static Collection<PackData> getAllPacks() {
		List<PackData> ans = new ArrayList<>();
		ans.add(getBCData());
		ans.addAll(getUserPacks());
		return ans;
	}

	public static DefPack getBCData() {
		return profile().def;
	}

	/**
	 * get a PackData from a String
	 */
	public static PackData getPack(String str) {
		UserProfile profile = profile();
		if (str.equals(Identifier.DEF))
			return profile.def;
		if (profile.fixpending != null && profile.fixpending.containsKey(str))
			return profile.fixpending.get(str).data;
		if (profile.pending != null && profile.pending.containsKey(str))
			return profile.pending.get(str);
		if (profile.packmap.containsKey(str))
			return profile.packmap.get(str);
		return null;
	}

	public static boolean isOlderPack(UserPack pack, String ver) {
		int thisVersion = Data.getVer(ver);
		int thatVersion = Data.getVer(pack.desc.BCU_VERSION);

		System.out.println(thatVersion);

		return thatVersion < thisVersion;
	}

	/**
	 * get a set registered in the Registrar
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Set<T> getPool(String id, Class<T> cls) {
		Map<String, Set> pool = getRegister(REG_POOL, Set.class);
		Set<T> ans = pool.get(id);
		if (ans == null)
			pool.put(id, ans = new LinkedHashSet<>());
		return ans;
	}

	/**
	 * get a map registered in the Registrar
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, T> getRegister(String id, Class<T> cls) {
		Map<String, T> ans = (Map<String, T>) profile().registers.get(id);
		if (ans == null)
			profile().registers.put(id, ans = new LinkedHashMap<>());
		return ans;
	}

	/**
	 * get a variable registered in the Registrar
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getStatic(String id, Supplier<T> def) {
		Map<String, Object> pool = getRegister(REG_STATIC, Object.class);
		T ans = (T) pool.get(id);
		if (ans == null)
			pool.put(id, ans = def.get());
		return ans;
	}

	/**
	 * get a UserPack from a String
	 */
	public static UserPack getUserPack(String str) {
		UserProfile profile = profile();
		if (profile.fixpending != null && profile.fixpending.containsKey(str))
			return profile.fixpending.get(str).data;
		if (profile.pending != null && profile.pending.containsKey(str))
			return profile.pending.get(str);
		if (profile.packmap.containsKey(str))
			return profile.packmap.get(str);
		return null;
	}

	/**
	 * get all UserPack
	 */
	public static Collection<UserPack> getUserPacks() {
		return profile().packmap.values();
	}

	public static UserPack initJsonPack(String id) throws Exception {
		File f = CommonStatic.ctx.getWorkspaceFile("./" + id + "/pack.json");
		File folder = f.getParentFile();
		if (folder.exists()) {
			if (!CommonStatic.ctx.confirmDelete(f))
				return null;
			Context.delete(f);
		}
		folder.mkdirs();
		Context.check(f.createNewFile(), "create", f);
		UserPack p = new UserPack(id);
		profile().packmap.put(id, p);
		return p;
	}

	public static void loadPacks(Consumer<Double> prog) {
		UserProfile profile = profile();

		if (profile.pending == null) {
			profile.pending = new HashMap<>();
		}

		if (profile.fixpending == null) {
			profile.fixpending = new HashMap<>();
		}

		CommonStatic.ctx.noticeErr(() -> VerFixer.fix(profile.fixpending), ErrType.FATAL,
				"failed to convert old format");
		profile.fixpending = null;
		File packs = CommonStatic.ctx.getAuxFile("./packs");
		File workspace = CommonStatic.ctx.getWorkspaceFile(".");
		if (packs.exists())
			for (File f : packs.listFiles())
				if (f.getName().endsWith(".pack.bcuzip")) {
					UserPack pack = CommonStatic.ctx.noticeErr(() -> readZipPack(f), ErrType.WARN,
							"failed to load external pack " + f, () -> setStatic(CURRENT_PACK, null));

					if (pack != null) {
						UserPack p = profile.pending.put(pack.desc.id, pack);

						if (p != null) {
							CommonStatic.ctx.printErr(ErrType.WARN, ((ZipSource) p.source).getPackFile().getName()
									+ " has same ID with " + ((ZipSource) pack.source).getPackFile().getName());
						}
					}
				}

		if (workspace.exists())
			for (File f : workspace.listFiles())
				if (f.isDirectory()) {
					File main = CommonStatic.ctx.getWorkspaceFile("./" + f.getName() + "/pack.json");
					if (!main.exists() || main.length() == 0)
						continue;
					UserPack pack = CommonStatic.ctx.noticeErr(() -> readJsonPack(main), ErrType.WARN,
							"failed to load workspace pack " + f);
					if (pack != null)
						profile.pending.put(pack.desc.id, pack);
				}
		Set<UserPack> queue = new HashSet<>(profile.pending.values());
		int tot = queue.size();
		int ind = 0;
		while (queue.removeIf(profile::add))
			prog.accept(1.0 * (ind++) / tot);
		profile.pending = null;
		profile.packlist.addAll(profile.failed);
	}

	public static UserProfile profile() {
		if (profile == null) {
			profile = new UserProfile();
			if (CommonStatic.ctx != null)
				CommonStatic.ctx.initProfile();
		}
		return profile;
	}

	public static UserPack readJsonPack(File f) throws Exception {
		File folder = f.getParentFile();
		Reader r = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8);
		JsonElement elem = JsonParser.parseReader(r);
		r.close();
		PackDesc desc = JsonDecoder.decode(elem.getAsJsonObject().get("desc"), PackDesc.class);

		if (Data.getVer(desc.BCU_VERSION) > Data.getVer(AssetLoader.CORE_VER)) {
			CommonStatic.ctx.printErr(ErrType.WARN, "Pack " + f.getName() + " core version (" + desc.BCU_VERSION
					+ ") is higher than BCU core version (" + AssetLoader.CORE_VER + ")");
		}

		UserPack data = new UserPack(new Workspace(folder.getName()), desc, elem);

		return data;
	}

	public static UserPack readZipPack(File f) throws Exception {
		ZipDesc zip = PackLoader.readPack(CommonStatic.ctx::preload, f);

		if (Data.getVer(zip.desc.BCU_VERSION) > Data.getVer(AssetLoader.CORE_VER)) {
			CommonStatic.ctx.printErr(ErrType.WARN, "Pack " + f.getName() + " core version (" + zip.desc.BCU_VERSION
					+ ") is higher than BCU core version (" + AssetLoader.CORE_VER + ")");
		}

		Reader r = new InputStreamReader(zip.readFile("./pack.json"), StandardCharsets.UTF_8);

		ZipSource zs = new ZipSource(zip);

		setStatic(CURRENT_PACK, zs);

		JsonElement elem = JsonParser.parseReader(r);

		UserPack data = new UserPack(zs, zip.desc, elem);
		r.close();

		setStatic(CURRENT_PACK, null);

		return data;
	}

	public static void remove(UserPack pack) {
		profile().packmap.remove(pack.desc.id);
		profile().packlist.remove(pack);
	}

	public static void setStatic(String id, Object val) {
		getRegister(REG_STATIC, Object.class).put(id, val);
	}

	public static void unloadAllUserPacks() {
		for (UserPack pack : getUserPacks()) {
			pack.unregister();
		}

		profile().packmap.clear();
		profile().packlist.clear();
		profile().failed.clear();
	}

	public static void unloadPack(UserPack pack) {
		pack.unregister();;

		profile().packmap.remove(pack.getSID());
		profile().packlist.remove(pack);
		profile().failed.remove(pack);
	}

	/**
	 * Unregister object from registers
	 * 
	 * @param id ID of registered object
	 */
	public static void unregister(String id) {
		profile().registers.remove(id);
	}

	public final DefPack def = new DefPack();
	public final Map<String, UserPack> packmap = new HashMap<>();

	public final Set<UserPack> packlist = new HashSet<>();

	public final Set<UserPack> failed = new HashSet<>();

	private final Map<String, Map<String, ?>> registers = new HashMap<>();

	private Map<String, VerFixer> fixpending = new HashMap<>();
	public Map<String, UserPack> pending = new HashMap<>();

	private UserProfile() {
	}

	/**
	 * return true if the pack is attempted to load and should be removed from the
	 * loading queue
	 */
	private boolean add(UserPack pack) {
		packlist.add(pack);
		if (!canAdd(pack))
			return false;
		if (!CommonStatic.ctx.noticeErr(pack::load, ErrType.WARN, "failed to load pack " + pack.desc, () -> setStatic(CURRENT_PACK, null))) {
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