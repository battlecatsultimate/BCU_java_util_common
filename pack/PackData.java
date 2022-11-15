package common.pack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import common.CommonStatic;
import common.battle.Treasure;
import common.battle.data.DataEnemy;
import common.battle.data.Orb;
import common.battle.data.PCoin;
import common.io.assets.AssetLoader;
import common.io.json.Dependency;
import common.io.json.FieldOrder.Order;
import common.io.json.JsonClass;
import common.io.json.JsonClass.JCConstructor;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonClass.RType;
import common.io.json.JsonDecoder;
import common.io.json.JsonField;
import common.io.json.JsonField.GenType;
import common.pack.FixIndexList.FixIndexMap;
import common.pack.Source.Workspace;
import common.system.VImg;
import common.system.files.FDFile;
import common.system.files.VFile;
import common.system.files.VFileRoot;
import common.util.Data;
import common.util.Res;
import common.util.anim.AnimCE;
import common.util.anim.AnimUD;
import common.util.pack.*;
import common.util.lang.MultiLangData;
import common.util.pack.*;
import common.util.pack.bgeffect.BackgroundEffect;
import common.util.stage.CastleList.PackCasList;
import common.util.stage.*;
import common.util.stage.MapColc.DefMapColc;
import common.util.stage.MapColc.PackMapColc;
import common.util.unit.*;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;

@JsonClass(read = RType.FILL, noTag = NoTag.LOAD)
public abstract class PackData implements IndexContainer {

	public static class DefPack extends PackData {

		public VFileRoot root = new VFileRoot(".");

		protected DefPack() {

		}

		@Override
		public String getSID() {
			return Identifier.DEF;
		}

		public void load(Consumer<String> progress, Consumer<Double> bar) {
			progress.accept("loading basic images");
			Res.readData();
			Trait.addBCTraits();
			progress.accept("loading cannon data");
			Treasure.readCannonCurveData();
			progress.accept("loading enemies");
			loadEnemies(bar);
			progress.accept("loading units");
			loadUnits(bar);
			progress.accept("loading auxiliary data");
			Combo.readFile();
			PCoin.read();
			progress.accept("loading effects");
			EffAnim.read();
			progress.accept("loading backgrounds");
			Background.read();
			BackgroundEffect.read();
			progress.accept("loading cat castles");
			NyCastle.read();
			progress.accept("loading souls");
			loadSoul();
			progress.accept("loading stages");
			DefMapColc.read();
			RandStage.read();
			loadCharaGroup();
			loadLimit();
			CastleImg.loadBossSpawns();
			progress.accept("loading orbs");
			Orb.read();
			progress.accept("loading musics");
			loadMusic();
			progress.accept("process data");
			this.traits.reset();
			this.enemies.reset();
			this.randEnemies.reset();
			this.units.reset();
			this.unitLevels.reset();
			this.groups.reset();
			this.lvrs.reset();
			this.bgs.reset();
			this.musics.reset();
			this.combos.reset();
			for (CastleList cl : CastleList.map().values())
				cl.reset();
			for (MapColc mc : MapColc.values()) {
				mc.maps.reset();
				for (StageMap sm : mc.maps)
					sm.list.reset();
			}
		}

		@Override
		public String toString() {
			return "Default BC Data";
		}

		private void loadCharaGroup() {
			Queue<String> qs = VFile.readLine("./org/data/Charagroup.csv");
			qs.poll();
			for (String str : qs) {
				String[] strs = str.split(",");
				int id = CommonStatic.parseIntN(strs[0]);
				int type = CommonStatic.parseIntN(strs[2]);
				@SuppressWarnings("unchecked")
				Identifier<Unit>[] units = new Identifier[strs.length - 3];
				for (int i = 3; i < strs.length; i++)
					units[i - 3] = Identifier.parseInt(CommonStatic.parseIntN(strs[i]), Unit.class);
				groups.set(id, new CharaGroup(id, type, units));
			}
		}

		private void loadEnemies(Consumer<Double> bar) {
			int i = 0;
			Collection<VFile> list = VFile.get("./org/enemy/").list();
			for (VFile p : list) {
				enemies.add(new Enemy(p));
				bar.accept(1.0 * (i++) / list.size());
			}
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
			File dict = CommonStatic.ctx.getAssetFile("./music/");
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
				musics.set(id, new Music(Identifier.parseInt(id, Music.class), 0, new FDFile(f)));
			}
		}

		private void loadSoul() {
			String pre = "./org/battle/soul/";

			int soulNumber = 0;

			VFile soulFolder = VFile.get(pre);

			if(soulFolder == null)
				return;

			for(VFile vf : soulFolder.list()) {
				if(vf == null)
					continue;

				if(vf.getData() == null && vf.name.matches("\\d{3}")) {
					soulNumber = Math.max(soulNumber, CommonStatic.safeParseInt(vf.name));
				}
			}

			String mid = "/battle_";
			for (int i = 0; i < soulNumber + 1; i++) {
				String path = pre + Data.trio(i) + mid;
				AnimUD anim = new AnimUD(path, "soul_" + Data.trio(i), null, null);
				Identifier<Soul> identifier = new Identifier<>(Identifier.DEF, Soul.class, i);
				souls.add(new Soul(identifier, anim));
			}
			String dem = "demonsoul"; // TODO identify if anim is enemy or not in demon soul name in effect page
			demonSouls.add(new DemonSoul(0, new AnimUD(pre + dem + mid, "demonsoul_" + Data.duo(0), null, null), true));
			demonSouls.add(new DemonSoul(0, new AnimUD(pre + dem + mid, "demonsoul_" + Data.duo(0), null, null), false));
		}

		private void loadUnits(Consumer<Double> bar) {
			int x = 0;
			Collection<VFile> list = VFile.get("./org/unit").list();
			Queue<String> qs = VFile.readLine("./org/data/unitbuy.csv");
			for (VFile p : list) {
				String[] strs = qs.poll().split(",");

				Unit u = new Unit(p, new int[]{Integer.parseInt(strs[strs.length - 2]), Integer.parseInt(strs[strs.length - 1])});
				u.rarity = Integer.parseInt(strs[13]);
				u.max = Integer.parseInt(strs[50]);
				u.maxp = Integer.parseInt(strs[51]);
				u.info.fillBuy(strs);

				units.add(u);
				bar.accept(1.0 * (x++) / list.size());
			}
			qs = VFile.readLine("./org/data/unitlevel.csv");
			List<Unit> lu = units.getList();
			FixIndexList<UnitLevel> l = unitLevels;
			for (Unit u : lu) {
				String[] strs = qs.poll().split(",");
				int[] lv = new int[20];
				for (int i = 0; i < 20; i++)
					lv[i] = Integer.parseInt(strs[i]);
				UnitLevel ul = new UnitLevel(lv);
				if (!l.contains(ul)) {
					ul.id = new Identifier<>(Identifier.DEF, UnitLevel.class, l.size());
					l.add(ul);
				}
				int ind = l.indexOf(ul);
				u.lv = l.get(ind);
				l.get(ind).units.add(u);
			}
			CommonStatic.getBCAssets().defLv = l.get(2);
		}

	}

	@JsonClass(noTag = NoTag.LOAD)
	public static class PackDesc {
		public String BCU_VERSION;
		public String id;
		public String author;

		@JsonField(io = JsonField.IOType.R)
		public String name;
		@JsonField(generic = MultiLangData.class)
		public MultiLangData names = new MultiLangData();

		public String desc;
		public String time;
		public int version;
		public boolean allowAnim = false;
		public byte[] parentPassword;
		@JsonField(generic = String.class)
		public ArrayList<String> dependency;

		@JCConstructor
		@Deprecated
		public PackDesc() {
		}

		public PackDesc(String id) {
			BCU_VERSION = AssetLoader.CORE_VER;
			this.id = id;
			this.dependency = new ArrayList<>();
		}

		@Override
		public String toString() {
			return names.toString() + " - " + id;
		}

		@Override
		public PackDesc clone() {
			PackDesc desc = new PackDesc(id);

			desc.author = author;
			desc.names.put(names.toString());
			desc.desc = this.desc;
			desc.time = time;
			desc.version = version;
			desc.allowAnim = allowAnim;
			desc.parentPassword = parentPassword == null ? null : parentPassword.clone();

			return desc;
		}

		@JsonDecoder.OnInjected
		public void onInjected() {
			//Temporary value, may need to make a separate isOlderPack function later on
			if (Data.getVer(BCU_VERSION) < Data.getVer("0.6.4.0"))
				names.put(name);
		}
	}

	@JsonClass(read = RType.FILL)
	public static class UserPack extends PackData {

		@JsonField
		@Order(0)
		public final PackDesc desc;

		@JsonField(gen = GenType.FILL)
		@Order(1)
		public PackCasList castles;

		@JsonField(gen = GenType.FILL)
		@Order(2)
		public PackMapColc mc;

		public Source source;

		public boolean editable;
		public boolean useCombos = true;
		public boolean loaded = false;

		private JsonElement elem;

		/**
		 * for old reading method only
		 */
		@Deprecated
		public UserPack(PackDesc desc, Source s) {
			this.desc = desc;
			source = s;
			castles = new PackCasList(this);
		}

		public UserPack(Source s, PackDesc desc, JsonElement elem) {
			this.desc = desc;
			this.elem = elem;
			source = s;
			editable = source instanceof Workspace;
			castles = new PackCasList(this);
			mc = new PackMapColc(this);
		}

		/**
		 * for generating new pack only
		 */
		public UserPack(String id) {
			desc = new PackDesc(id);
			source = new Workspace(id);
			castles = new PackCasList(this);
			mc = new PackMapColc(this);
			editable = true;
			loaded = true;
		}

		public void delete() {
			unregister();

			UserProfile.profile().packmap.remove(getSID());

			source.delete();
		}

		public List<String> foreignList(String id) {
			List<String> list = new ArrayList<>();
			Dependency dep = Dependency.collect(this);
			if (dep.getPacks().contains(id))
				for (Entry<Class<?>, Map<String, Set<Identifier<?>>>> ent : dep.getMap().entrySet()) {
					Map<String, Set<Identifier<?>>> map = ent.getValue();
					if (map.containsKey(id) && map.get(id).size() > 0) {
						list.add(ent.getKey().getSimpleName() + ":");
						for (Identifier<?> identifier : map.get(id))
							list.add("\t" + identifier.get().toString());
					}
				}
			return list;
		}

		public List<Replay> getReplays() {
			List<Replay> ans = new ArrayList<>();
			for (StageMap sm : mc.maps)
				for (Stage st : sm.list)
					ans.addAll(st.recd);
			return ans;
		}

		@Override
		public String getSID() {
			return desc.id;
		}

		public void loadMusics() {
			String[] path = source.listFile("./musics");

			HashMap<Integer, Long> loopMap = new HashMap<>();
			for (Music m : musics) {
				if (m == null || m.id == null)
					continue;

				loopMap.put(m.id.id, m.loop);
			}

			musics.clear();
			if (path != null)
				for (String str : path)
					if (str.length() == 7 && str.endsWith(".ogg")) {
						Integer ind = Data.ignore(() -> Integer.parseInt(str.substring(0, 3)));
						if (ind != null) {
							long loop = loopMap.getOrDefault(ind, (long) 0);
							add(musics, ind, id -> new Music(id, loop, source.getFileData("./musics/" + str)));
						}
					}
			musics.reset();
		}

		public boolean relyOn(String id) {
			Dependency dep = Dependency.collect(this);
			System.out.println("Pack: " + dep.getPacks());// FIXME
			return dep.getPacks().contains(id);
		}

		@Override
		public String toString() {
			return desc.names == null || desc.names.toString().isEmpty() ? desc.id : desc.names.toString();
		}

		public void unregister() {
			UserProfile.unregister(getSID());
		}

		public ArrayList<String> preGetDependencies() {
			ArrayList<String> deps = new ArrayList<>();
			JsonArray jarr = elem.getAsJsonObject().getAsJsonObject("desc").get("dependency").getAsJsonArray();
			for (int i = 0; i < jarr.size(); i++)
				deps.add(jarr.get(i).getAsString());
			return deps;
		}

		public void load() throws Exception {
			UserProfile.setStatic(UserProfile.CURRENT_PACK, source);
			JsonDecoder.inject(elem, UserPack.class, this);
			elem = null;
			loaded = true;
			loadMusics();
			UserProfile.setStatic(UserProfile.CURRENT_PACK, null);

			if(source instanceof Source.ZipSource) {
				if(((Source.ZipSource) source).zip.desc.parentPassword != null) {
					desc.parentPassword = ((Source.ZipSource) source).zip.desc.parentPassword.clone();
				}
			}

			//Since it succeeded to load all data, update Core version of this workspace pack
			if(editable) {
				desc.BCU_VERSION = AssetLoader.CORE_VER;
			}
		}

	}

	@ContGetter
	public static PackData getPack(String str) {
		return UserProfile.getPack(str);
	}

	@Order(0)
	public final FixIndexMap<Trait> traits = new FixIndexMap<>(Trait.class);
	@Order(1)
	public final FixIndexMap<Enemy> enemies = new FixIndexMap<>(Enemy.class);
	@Order(2)
	public final FixIndexMap<EneRand> randEnemies = new FixIndexMap<>(EneRand.class);
	@Order(3)
	public final FixIndexMap<UnitLevel> unitLevels = new FixIndexMap<>(UnitLevel.class);
	@Order(4)
	public final FixIndexMap<Unit> units = new FixIndexMap<>(Unit.class);
	@Order(5)
	public final FixIndexMap<Soul> souls = new FixIndexMap<>(Soul.class);
	@Order(6)
	public final FixIndexMap<DemonSoul> demonSouls = new FixIndexMap<>(DemonSoul.class);
	@Order(7)
	public final FixIndexMap<Background> bgs = new FixIndexMap<>(Background.class);
	@Order(8)
	public final FixIndexMap<CharaGroup> groups = new FixIndexMap<>(CharaGroup.class);
	@Order(9)
	public final FixIndexMap<LvRestrict> lvrs = new FixIndexMap<>(LvRestrict.class);
	@Order(10)
	public final FixIndexMap<Music> musics = new FixIndexMap<>(Music.class);
	@Order(11)
	public final FixIndexMap<Combo> combos = new FixIndexMap<>(Combo.class);

	@Override
	@SuppressWarnings({ "rawtypes" })
	public <R> R getList(Class cls, Reductor<R, FixIndexMap> func, R def) {
		if (cls == Trait.class)
			def = func.reduce(def, traits);
		if (cls == Unit.class)
			def = func.reduce(def, units);
		if (cls == UnitLevel.class)
			def = func.reduce(def, unitLevels);
		if (cls == Enemy.class || cls == AbEnemy.class)
			def = func.reduce(def, enemies);
		if (cls == EneRand.class || cls == AbEnemy.class)
			def = func.reduce(def, randEnemies);
		if (cls == Background.class)
			def = func.reduce(def, bgs);
		if (cls == Soul.class)
			def = func.reduce(def, souls);
		if (cls == Music.class)
			def = func.reduce(def, musics);
		if (cls == CharaGroup.class)
			def = func.reduce(def, groups);
		if (cls == LvRestrict.class)
			def = func.reduce(def, lvrs);
		if (cls == Combo.class)
			def = func.reduce(def, combos);
		return def;
	}

}
