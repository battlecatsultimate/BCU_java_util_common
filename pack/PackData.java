package common.pack;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;

import com.google.gson.JsonParser;

import common.battle.data.DataEnemy;
import common.io.json.JsonClass;
import common.io.json.JsonDecoder;
import common.io.json.JsonField;
import common.io.json.JsonClass.JCConstructor;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonClass.RType;
import common.pack.PackLoader.ZipDesc;
import common.pack.Source.Context;
import common.pack.Source.Workspace;
import common.pack.Source.ZipSource;
import common.system.FixIndexList;
import common.system.VImg;
import common.system.files.FileData;
import common.system.files.VFile;
import common.util.Data;
import common.util.pack.Background;
import common.util.unit.AbEnemy;
import common.util.unit.EneRand;
import common.util.unit.Enemy;
import common.util.unit.Unit;
import common.util.unit.UnitLevel;

@JsonClass(read = RType.FILL)
public class PackData {

	public static class DefPack extends PackData {

		public DefPack() {

		}

		private void loadEnemies() {
			VFile.get("./org/enemy/").list().forEach(p -> enemies.add(new Enemy(p)));
			Queue<String> qs = VFile.readLine("./org/data/t_unit.csv");
			qs.poll();
			qs.poll();
			for (Enemy e : enemies.getList())
				((DataEnemy) e.de).fillData(qs.poll().split("//")[0].trim().split(","));
			qs = VFile.readLine("./org/data/enemy_dictionary_list.csv");
			for (String str : qs)
				enemies.get(Integer.parseInt(str.split(",")[0])).inDic = true;
		}

		private void loadUnits() {
			VFile.get("./org/unit").list().forEach(p -> units.add(new Unit(p)));
			Queue<String> qs = VFile.readLine("./org/data/unitlevel.csv");
			List<Unit> lu = units.getList();
			FixIndexList<UnitLevel> l = unitLevels;
			for (Unit u : lu) {
				String[] strs = qs.poll().split(",");
				int[] lv = new int[20];
				for (int i = 0; i < 20; i++)
					lv[i] = Integer.parseInt(strs[i]);
				UnitLevel ul = new UnitLevel(lv);
				if (!l.contains(ul)) {
					ul.id = new Identifier("_default", Data.trio(l.size()));
					l.add(ul);
				}
				int ind = l.indexOf(ul);
				u.lv = l.get(ind);
				u.lv.units.add(u);
			}
			UnitLevel.def = l.get(2);
			qs = VFile.readLine("./org/data/unitbuy.csv");
			for (Unit u : lu) {
				String[] strs = qs.poll().split(",");
				u.rarity = Integer.parseInt(strs[13]);
				u.max = Integer.parseInt(strs[50]);
				u.maxp = Integer.parseInt(strs[51]);
				u.info.fillBuy(strs);
			}
		}

	}

	@JsonClass(noTag = NoTag.LOAD)
	public static class Identifier implements Comparable<Identifier>, Cloneable {

		public static final String DEF = "_default";

		public static Identifier parseInt(int v) {
			String pack = v / 1000 == 0 ? DEF : Data.hex(v / 1000);
			int id = v % 1000;
			return new Identifier(pack, Data.trio(id));
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
		public Identifier clone() {
			try {
				return (Identifier) super.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				return null;
			}
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

	@JsonClass(noTag = NoTag.LOAD)
	public static class PackDesc {
		public String BCU_VERSION;
		public String id;
		public String author;
		public String name;
		public String desc;
		public String time;
		public int version;
		@JsonField(generic = String.class)
		public ArrayList<String> dependency;

		@JCConstructor
		@Deprecated
		public PackDesc() {
		}

		public PackDesc(String id) {
			BCU_VERSION = "";// FIXME ver Data.revVer(MainBCU.ver);
			this.id = id;
			this.dependency = new ArrayList<>();
		}
	}

	public static class UserPack extends PackData {

		@JsonField
		public final PackDesc desc;

		public final Source source;

		// TODO MapColc

		/** for old reading method only */
		@Deprecated
		public UserPack(PackDesc desc, Source s) {
			this.desc = desc;
			source = s;
		}

		@JCConstructor
		public UserPack(Source s) {
			desc = null;
			source = s;
		}

		/** for generating new pack only */
		private UserPack(String id) {
			desc = new PackDesc(id);
			source = new Workspace(id);
		}

	}

	public static class UserProfile {

		public static Background getBG(Identifier theme) {
			// TODO Auto-generated method stub
			return null;
		}

		public static AbEnemy getEnemy(Identifier id) {
			UserPack pack = profile.packmap.get(id.pack);
			if (pack == null)
				return null;
			return null;// FIXME pack.enemies.get(id.id);
		}

		public static Unit getUnit(Identifier id) {
			// TODO Auto-generated method stub
			return null;
		}

		public String username;
		public byte[] password;
		public final DefPack def = new DefPack();

		public final Map<String, UserPack> packmap = new HashMap<>();

		public final Set<UserPack> packlist = new HashSet<>();

		public final Set<UserPack> failed = new HashSet<>();

		public UserProfile() {
			// TODO load username and password
			File packs = Source.ctx.getPackFolder();
			File workspace = Source.ctx.getWorkspaceFile(".");
			Map<String, UserPack> set = new HashMap<>();
			if (packs.exists())
				for (File f : packs.listFiles())
					if (f.getName().endsWith(".pack.bcuzip"))
						try {
							UserPack s = readZipPack(f);
							set.put(s.desc.id, s);
						} catch (Exception e) {
							Source.ctx.noticeErr(e, "failed to load external pack " + f);
						}
			if (workspace.exists())
				for (File f : packs.listFiles())
					if (f.isDirectory()) {
						File main = Source.ctx.getWorkspaceFile("./" + f.getName() + "/main.pack.json");
						if (!main.exists())
							continue;
						try {
							UserPack s = readJsonPack(main);
							set.put(s.desc.id, s);
						} catch (Exception e) {
							Source.ctx.noticeErr(e, "failed to load workspace pack " + f);
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

	// FIXME load it into register
	public static UserProfile profile;

	public static UserPack initJsonPack(String id) throws Exception {
		File f = Source.ctx.getWorkspaceFile("./" + id + "/main.pack.json");
		File folder = f.getParentFile();
		if (folder.exists()) {
			if (!Source.ctx.confirmDelete())
				return null;
			Context.delete(f);
		}
		Context.check(folder.mkdirs(), "create", folder);
		Context.check(f.createNewFile(), "create", f);
		return new UserPack(id);
	}

	public static UserPack readJsonPack(File f) throws Exception {
		File folder = f.getParentFile();
		Reader r = new FileReader(f);
		UserPack data = JsonDecoder.inject(JsonParser.parseReader(r), UserPack.class,
				new UserPack(new Workspace(folder.getName())));
		r.close();
		return data;
	}

	public static UserPack readZipPack(File f) throws Exception {
		ZipDesc zip = PackLoader.readPack(Source.ctx::preload, f);
		Reader r = new InputStreamReader(zip.readFile("./main.pack.json"));
		UserPack data = JsonDecoder.inject(JsonParser.parseReader(r), UserPack.class, new UserPack(new ZipSource(zip)));
		r.close();
		return data;
	}

	public final FixIndexList<Enemy> enemies = new FixIndexList<>(Enemy.class);
	public final FixIndexList<EneRand> randEnemies = new FixIndexList<>(EneRand.class);
	public final FixIndexList<Unit> units = new FixIndexList<>(Unit.class);
	public final FixIndexList<UnitLevel> unitLevels = new FixIndexList<>(UnitLevel.class);
	public final FixIndexList<Background> bgs = new FixIndexList<>(Background.class);
	public final FixIndexList<VImg> castles = new FixIndexList<>(VImg.class);
	public final FixIndexList<FileData> musics = new FixIndexList<>(FileData.class);

}
