package common.pack;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.google.gson.JsonParser;

import common.CommonStatic;
import common.battle.data.DataEnemy;
import common.io.json.JsonClass;
import common.io.json.JsonDecoder;
import common.io.json.JsonField;
import common.io.json.JsonClass.JCConstructor;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonClass.RType;
import common.io.json.JsonField.GenType;
import common.pack.FixIndexList.FixIndexMap;
import common.pack.PackLoader.ZipDesc;
import common.pack.Source.Workspace;
import common.pack.Source.ZipSource;
import common.system.files.FDFile;
import common.system.files.VFile;
import common.util.Data;
import common.util.pack.Background;
import common.util.pack.Soul;
import common.util.stage.CastleList.PackCasList;
import common.util.stage.CharaGroup;
import common.util.stage.Limit;
import common.util.stage.MapColc.PackMapColc;
import common.util.stage.Music;
import common.util.unit.EneRand;
import common.util.unit.Enemy;
import common.util.unit.Unit;
import common.util.unit.UnitLevel;

@JsonClass(read = RType.FILL)
public class PackData {

	public static class DefPack extends PackData {

		public final Map<Integer, CharaGroup> cgmap = new HashMap<>();

		public DefPack() {
			loadSoul();
			loadEnemies();
			loadUnits();
			loadCharaGroup();
			loadLimit();
			loadMusic();
		}

		private void loadCharaGroup() {
			Queue<String> qs = VFile.readLine("./org/data/Charagroup.csv");
			qs.poll();
			for (String str : qs) {
				String[] strs = str.split(",");
				int id = CommonStatic.parseIntN(strs[0]);
				int type = CommonStatic.parseIntN(strs[2]);
				Identifier[] units = new Identifier[strs.length - 3];
				for (int i = 3; i < strs.length; i++)
					units[i - 3] = Identifier.parseInt(CommonStatic.parseIntN(strs[i]));
				cgmap.put(id, new CharaGroup.DefCG(id, type, units));
			}
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

		private void loadLimit() {
			Queue<String> qs = VFile.readLine("./org/data/Stage_option.csv");
			qs.poll();
			for (String str : qs)
				new Limit.DefLimit(str.split(","));
		}

		private void loadMusic() {
			File dict = Source.ctx.getAssetFile("./music/");
			if (!dict.exists())
				return;
			File[] fs = dict.listFiles();
			for (File f : fs) {
				String str = f.getName();
				if (str.length() != 7)
					continue;
				if (!str.endsWith(".ogg"))
					continue;
				int id = CommonStatic.parseIntN(str.substring(0, 3));
				if (id == -1)
					continue;
				musics.set(id, new Music(Identifier.parseInt(id), new FDFile(f)));
			}
		}

		private void loadSoul() {
			String pre = "./org/battle/soul/";
			String mid = "/battle_soul_";
			for (int i = 0; i < 13; i++)
				souls.add(new Soul(pre + Data.trio(i) + mid + Data.trio(i), i));
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

	@JsonClass(read = RType.FILL)
	public static class UserPack extends PackData {

		@JsonField
		public final PackDesc desc;

		@JsonField(gen = GenType.FILL)
		public PackMapColc mc;

		@JsonField(gen = GenType.FILL)
		public PackCasList castles;

		public final Source source;

		public boolean editable;

		/** for old reading method only */
		@Deprecated
		public UserPack(PackDesc desc, Source s) {
			this.desc = desc;
			source = s;
		}

		public UserPack(Source s) {
			desc = null;
			source = s;
			editable = source instanceof Workspace;
			mc = new PackMapColc(this);
		}

		/** for generating new pack only */
		public UserPack(String id) {
			desc = new PackDesc(id);
			source = new Workspace(id);
			castles = new PackCasList(this);
		}

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

	public final FixIndexMap<Enemy> enemies = new FixIndexMap<>(Enemy.class);
	public final FixIndexMap<EneRand> randEnemies = new FixIndexMap<>(EneRand.class);
	public final FixIndexMap<Unit> units = new FixIndexMap<>(Unit.class);
	public final FixIndexMap<UnitLevel> unitLevels = new FixIndexMap<>(UnitLevel.class);
	public final FixIndexMap<Soul> souls = new FixIndexMap<>(Soul.class);
	public final FixIndexMap<Background> bgs = new FixIndexMap<>(Background.class);
	public final FixIndexList<Music> musics = new FixIndexList<>(Music.class);

}
