package common.pack;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.Map.Entry;

import common.pack.Context.ErrType;
import common.pack.PackData.DefPack;
import common.pack.PackData.Identifier;
import common.pack.PackData.UserPack;
import common.util.pack.Background;
import common.util.pack.Soul;
import common.util.unit.AbEnemy;
import common.util.unit.Unit;

public class UserProfile {

	// FIXME load it into register
	private static UserProfile profile;

	public static DefPack getBCData() {
		return profile.def;
	}

	public static Background getBG(Identifier id) {
		PackData pack = getPack(id.pack);
		if (pack == null)
			return null;
		return pack.bgs.get(id.id);
	}

	public static AbEnemy getEnemy(Identifier id) {
		PackData pack = getPack(id.pack);
		if (pack == null)
			return null;
		AbEnemy ans = pack.enemies.get(id.id);
		if (ans != null)
			return ans;
		return pack.randEnemies.get(id.id);
	}

	public static PackData getPack(String str) {
		if (str.equals(Identifier.DEF))
			return profile.def;
		return profile.packmap.get(str);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Set<T> getPool(String id, Class<T> cls) {
		Map<String, Set> pool = getRegister("_pools", Set.class);
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

	public static Soul getSoul(Identifier id) {
		PackData pack = getPack(id.pack);
		if (pack == null)
			return null;
		return pack.souls.get(id.id);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getStatic(String id, Supplier<T> def) {
		Map<String, Object> pool = getRegister("_statics", Object.class);
		T ans = (T) pool.get(id);
		if (ans == null)
			pool.put(id, ans = def.get());
		return ans;
	}

	public static Unit getUnit(Identifier id) {
		PackData pack = getPack(id.pack);
		if (pack == null)
			return null;
		return pack.units.get(id.id);
	}

	public static PackData.UserPack initJsonPack(String id) throws Exception {
		File f = Source.ctx.getWorkspaceFile("./" + id + "/main.pack.json");
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

	public String username;
	public byte[] password;
	public final DefPack def = new DefPack();

	public final Map<String, UserPack> packmap = new HashMap<>();

	public final Set<UserPack> packlist = new HashSet<>();

	public final Set<UserPack> failed = new HashSet<>();

	private final Map<String, Map<String, ?>> registers = new HashMap<>();

	public UserProfile() {
		// TODO load username and password
		File packs = Source.ctx.getPackFolder();
		File workspace = Source.ctx.getWorkspaceFile(".");
		Map<String, UserPack> set = new HashMap<>();
		if (packs.exists())
			for (File f : packs.listFiles())
				if (f.getName().endsWith(".pack.bcuzip"))
					try {
						UserPack s = PackData.readZipPack(f);
						set.put(s.desc.id, s);
					} catch (Exception e) {
						Source.ctx.noticeErr(e, ErrType.WARN, "failed to load external pack " + f);
					}
		if (workspace.exists())
			for (File f : packs.listFiles())
				if (f.isDirectory()) {
					File main = Source.ctx.getWorkspaceFile("./" + f.getName() + "/main.pack.json");
					if (!main.exists())
						continue;
					try {
						UserPack s = PackData.readJsonPack(main);
						set.put(s.desc.id, s);
					} catch (Exception e) {
						Source.ctx.noticeErr(e, ErrType.WARN, "failed to load workspace pack " + f);
					}
				}
		failed.addAll(set.values());
		while (failed.removeIf(this::add))
			;
		packlist.addAll(failed);
	}

	public boolean add(UserPack pack) {
		packlist.add(pack);
		if (!canAdd(pack))
			return false;
		packmap.put(pack.desc.id, pack);
		return true;
	}

	public boolean canAdd(UserPack s) {
		for (String dep : s.desc.dependency)
			if (!packmap.containsKey(dep))
				return false;
		return true;
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

}