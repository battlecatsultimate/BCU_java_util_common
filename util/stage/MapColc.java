package common.util.stage;

import common.io.InStream;
import common.io.assets.Admin.StaticPermitted;
import common.io.json.JsonClass;
import common.io.json.JsonClass.RType;
import common.io.json.JsonField;
import common.pack.FixIndexList.FixIndexMap;
import common.pack.IndexContainer;
import common.pack.PackData.UserPack;
import common.pack.UserProfile;
import common.pack.VerFixer.VerFixerException;
import common.system.files.VFile;
import common.util.Data;
import common.util.lang.MultiLangCont;

import java.util.*;

@JsonClass(read = RType.FILL)
public abstract class MapColc extends Data implements IndexContainer.SingleIC<StageMap> {

	public static class DefMapColc extends MapColc {

		private static final String REG_IDMAP = "DefMapColc_idmap";

		/**
		 * get a BC stage
		 */
		public static StageMap getMap(int mid) {
			Map<String, MapColc> map = UserProfile.getRegister(REG_MAPCOLC, MapColc.class);
			MapColc mc = map.get(Data.hex(mid / 1000));
			if (mc == null)
				return null;
			return mc.maps.get(mid % 1000);
		}

		public static DefMapColc getMap(String id) {
			return (DefMapColc) UserProfile.getRegister(REG_MAPCOLC, MapColc.class)
					.get(Data.hex(UserProfile.getRegister(REG_IDMAP, Integer.class).get(id)));
		}

		public static void read() {
			Map<String, Integer> idmap = UserProfile.getRegister(REG_IDMAP, Integer.class);
			idmap.put("E", 4);
			idmap.put("N", 0);
			idmap.put("S", 1);
			idmap.put("C", 2);
			idmap.put("CH", 3);
			idmap.put("T", 6);
			idmap.put("V", 7);
			idmap.put("R", 11);
			idmap.put("M", 12);
			idmap.put("A", 13);
			idmap.put("B", 14);
			idmap.put("RA", 24);
			idmap.put("H", 25);
			idmap.put("CA", 27);
			for (int i = 0; i < strs.length; i++)
				new CastleList.DefCasList(Data.hex(i), strs[i]);
			VFile f = VFile.get("./org/stage/");
			if (f == null)
				return;
			for (VFile fi : f.list()) {
				if (fi.getName().equals("CH"))
					continue;
				if (fi.getName().equals("D"))
					continue;
				if (fi.getName().equals("DM"))
					continue;
				List<VFile> list = new ArrayList<>(fi.list());
				VFile map = list.get(0);
				List<VFile> stage = new ArrayList<>();
				for (int i = 1; i < list.size(); i++) {
					if(fi.getName().equals("N") && list.get(i).getName().contains("stageRN-1"))
						continue;

					if (list.get(i).list() != null)
						stage.addAll(list.get(i).list());
				}
				new DefMapColc(fi.getName(), idmap.get(fi.getName()), stage, map);
			}
			new DefMapColc();
			Queue<String> qs = VFile.readLine("./org/data/Map_option.csv");
			qs.poll();
			for (String str : qs) {
				String[] strs = str.trim().split(",");
				int id = Integer.parseInt(strs[0]);
				StageMap sm = getMap(id);
				if (sm == null)
					continue;
				int len = Integer.parseInt(strs[1]);
				sm.stars = new int[len];
				for (int i = 0; i < len; i++)
					sm.stars[i] = Integer.parseInt(strs[2 + i]);
				sm.set = Integer.parseInt(strs[6]);
				sm.retyp = Integer.parseInt(strs[7]);
				sm.pllim = Integer.parseInt(strs[8]);
				sm.name += strs[10];
				sm.starMask = Integer.parseInt(strs[12]);
			}
		}

		public final int id;
		public final String name;

		private DefMapColc() {
			id = 3;
			UserProfile.getRegister(REG_MAPCOLC, MapColc.class).put(Data.hex(id), this);
			name = "CH";
			String abbr = "./org/stage/CH/stageNormal/stageNormal";
			for(int j = 0; j < 3; j++) {
				if(j == 0) {
					for (int i = 0; i < 3; i++) {
						int I = i;
						add(i, id -> new StageMap(id, abbr + "0_" + I + "_Z.csv", 1)).name = "EoC " + (i + 1) + " Zombie";
					}
				} else if(j == 1) {
					for (int i = 0; i < 3; i++) {
						int I = i;
						add(3 + i, id -> new StageMap(id, abbr + "1_" + I + ".csv", 2)).name = "ItF " + (i + 1);
					}
				} else {
					for (int i = 0; i < 3; i++) {
						int I = i;
						add(6 + i, id -> new StageMap(id, abbr + "2_" + I + ".csv", 3)).name = "CotC " + (i + 1);
					}
				}
			}

			add(9, id -> new StageMap(id, abbr + "0.csv", 1)).name = "EoC 1-3";
			add(10, id -> new StageMap(id, abbr + "1_0_Z.csv", 2)).name = "ItF 1 Zombie";
			add(11, id -> new StageMap(id, abbr + "2_2_Invasion.csv", 2)).name = "CotC 3 Invasion";
			add(12, id -> new StageMap(id, abbr + "1_1_Z.csv", 2)).name = "ItF 2 Zombie";
			add(13, id -> new StageMap(id, abbr + "1_2_Z.csv", 2)).name = "ItF 3 Zombie";

			String akuOutbreak = "./org/stage/DM/";

			add(14, id -> new StageMap(id, akuOutbreak+"MSDDM/MapStageDataDM_000.csv", 0));

			VFile stz = VFile.get("./org/stage/CH/stageZ/");
			for (VFile vf : stz.list()) {
				String str = vf.getName();
				int id0 = -1, id1 = -1;
				try {
					id0 = Integer.parseInt(str.substring(6, 8));
					id1 = Integer.parseInt(str.substring(9, 11));
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				if (id0 < 3)
					maps.get(id0).add(id1, id -> new Stage(id, vf, 0));
				else if (id0 == 4)
					maps.get(10).add(id1, id -> new Stage(id, vf, 0));
				else if (id0 == 5)
					maps.get(12).add(id1, id -> new Stage(id, vf, 0));
				else if (id0 == 6)
					maps.get(13).add(id1, id -> new Stage(id, vf, 0));
			}
			VFile stw = VFile.get("./org/stage/CH/stageW/");
			for (VFile vf : stw.list()) {
				String str = vf.getName();
				int id0 = -1, id1 = -1;
				try {
					id0 = Integer.parseInt(str.substring(6, 8));
					id1 = Integer.parseInt(str.substring(9, 11));
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				maps.get(id0 - 1).add(id1, id -> new Stage(id, vf, 1));
			}
			VFile sts = VFile.get("./org/stage/CH/stageSpace/");
			for (VFile vf : sts.list()) {
				String str = vf.getName();
				if (str.length() > 20) {
					maps.get(11).add(0, id -> new Stage(id, vf, 0));
					continue;
				}
				int id0 = -1, id1 = -1;
				try {
					id0 = Integer.parseInt(str.substring(10, 12));
					id1 = Integer.parseInt(str.substring(13, 15));
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				maps.get(id0 - 1).add(id1, id -> new Stage(id, vf, 1));
			}

			VFile st = VFile.get("./org/stage/CH/stage/");
			for (VFile vf : st.list()) {
				String str = vf.getName();
				int id0 = -1;
				try {
					id0 = Integer.parseInt(str.substring(5, 7));
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				maps.get(9).add(id0, id -> new Stage(id, vf, 2));
			}
			maps.get(9).stars = new int[] { 100, 150, 400 };

			VFile sta = VFile.get(akuOutbreak+"StageDM/");
			for(VFile vf : sta.list()) {
				String str = vf.getName();
				int id0;
				try {
					id0 = Integer.parseInt(str.substring(11, 13));

					maps.get(14).add(id0, id -> new Stage(id, vf, 0));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		private DefMapColc(String st, int ID, List<VFile> stage, VFile map) {
			name = st;
			id = ID;
			UserProfile.getRegister(REG_MAPCOLC, MapColc.class).put(Data.hex(id), this);
			for (VFile m : map.list()) {
				String str = m.getName();
				int len = str.length();
				int id = -1;
				try {
					id = Integer.parseInt(str.substring(len - 7, len - 4));
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				add(id, ind -> new StageMap(ind, m.getData()));
			}

			for (VFile s : stage) {
				String str = s.getName();
				int len = str.length();
				int id0 = -1, id1 = -1;
				try {
					id0 = Integer.parseInt(str.substring(len - 10, len - 7));
					id1 = Integer.parseInt(str.substring(len - 6, len - 4));
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				StageMap sm = maps.get(id0);
				sm.add(id1, id -> new Stage(id, s, 0));
			}
		}

		@Override
		public String getSID() {
			return Data.hex(id);
		}

		@Override
		public String toString() {
			String desp = MultiLangCont.get(this);
			if (desp != null && desp.length() > 0)
				return desp + " (" + maps.size() + ")";
			return name + " (" + maps.size() + ")";
		}

	}

	@JsonClass
	public static class PackMapColc extends MapColc {

		public final UserPack pack;

		public PackMapColc(UserPack pack) {
			this.pack = pack;
			UserProfile.getRegister(REG_MAPCOLC, MapColc.class).put(pack.getSID(), this);
		}

		@SuppressWarnings("deprecation")
		@Deprecated
		public PackMapColc(UserPack pack, InStream is) throws VerFixerException {
			this.pack = pack;
			UserProfile.getRegister(REG_MAPCOLC, MapColc.class).put(pack.getSID(), this);
			int val = getVer(is.nextString());
			if (val != 308)
				throw new VerFixerException("MapColc requires 308, got " + val);
			is.nextString();

			int n = is.nextInt();
			for (int i = 0; i < n; i++) {
				CharaGroup cg = new CharaGroup(pack, is);
				pack.groups.set(cg.id.id, cg);
			}

			n = is.nextInt();
			for (int i = 0; i < n; i++) {
				LvRestrict lr = new LvRestrict(pack, is);
				pack.lvrs.set(lr.id.id, lr);
			}

			n = is.nextInt();
			for (int i = 0; i < n; i++) {
				StageMap sm = add(i, StageMap::new);
				sm.name = is.nextString();
				sm.stars = is.nextIntsB();
				int m = is.nextInt();
				for (int j = 0; j < m; j++) {
					InStream sub = is.subStream();
					sm.add(id -> Data.err(() -> new Stage(pack, id, sub)));
				}
				m = is.nextInt();
				for (int j = 0; j < m; j++)
					sm.lim.add(new Limit.PackLimit(pack, is));
			}
		}

		@Override
		public String getSID() {
			return pack.getSID();
		}

		@Override
		public String toString() {
			return pack.desc.name;
		}

	}

	public static class StItr implements Iterator<Stage>, Iterable<Stage> {

		private Iterator<MapColc> imc;
		private MapColc mc;
		private int ism, is;

		protected StItr() {
			imc = UserProfile.getRegister(REG_MAPCOLC, MapColc.class).values().iterator();
			mc = imc.next();
			ism = is = 0;
			validate();
		}

		@Override
		public boolean hasNext() {
			return imc != null;
		}

		@Override
		public Iterator<Stage> iterator() {
			return this;
		}

		@Override
		public Stage next() {
			Stage ans = mc.maps.get(ism).list.get(is);
			is++;
			validate();
			return ans;
		}

		private void validate() {
			while (is >= mc.maps.get(ism).list.size()) {
				is = 0;
				ism++;
				while (ism >= mc.maps.size()) {
					ism = 0;

					if (!imc.hasNext()) {
						imc = null;
						return;
					}
					mc = imc.next();
				}
			}
		}

	}

	public static class ClipMapColc extends MapColc {

		protected ClipMapColc() {
			add(0, StageMap::new);
		}

		@Override
		public String getSID() {
			return "clipboard";
		}

		@Override
		public String toString() {
			return getSID();
		}

	}

	private static final String REG_MAPCOLC = "MapColc";

	@StaticPermitted
	private static final String[] strs = new String[] { "rc", "ec", "wc", "sc" };

	@ContGetter
	public static MapColc get(String id) {
		if(id.equals("clipboard"))
			return Stage.CLIPMC;

		return UserProfile.getRegister(REG_MAPCOLC, MapColc.class).get(id);
	}

	public static Iterable<Stage> getAllStage() {
		return new StItr();
	}

	public static Collection<MapColc> values() {
		return UserProfile.getRegister(REG_MAPCOLC, MapColc.class).values();
	}

	@JsonField
	public FixIndexMap<StageMap> maps = new FixIndexMap<>(StageMap.class);

	@Override
	public FixIndexMap<StageMap> getFIM() {
		return maps;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <R> R getList(Class cls, Reductor<R, FixIndexMap> func, R def) {
		return func.reduce(def, maps);
	}

}