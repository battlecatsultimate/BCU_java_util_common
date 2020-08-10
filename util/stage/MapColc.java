package common.util.stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import common.io.InStream;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.io.json.JsonClass.RType;
import common.io.json.JsonField.GenType;
import common.pack.FixIndexList;
import common.pack.UserProfile;
import common.pack.VerFixer.VerFixerException;
import common.pack.PackData.UserPack;
import common.system.MultiLangCont;
import common.system.files.AssetData;
import common.system.files.VFile;
import common.util.Data;

@JsonClass(read = RType.FILL)
public class MapColc extends Data {

	public static class DefMapColc extends MapColc {

		private static final String REG_IDMAP = "DefMapColc_idmap";

		/** get a BC stage */
		public static StageMap getMap(int mid) {
			return UserProfile.getRegister(REG_MAPCOLC, MapColc.class).get(Data.trio(mid / 1000)).maps[mid % 1000];
		}

		public static DefMapColc getMap(String id) {
			return (DefMapColc) UserProfile.getRegister(REG_MAPCOLC, MapColc.class)
					.get(Data.trio(UserProfile.getRegister(REG_IDMAP, Integer.class).get(id)));
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
				new Castles(i, strs[i]);
			VFile<AssetData> f = VFile.get("./org/stage/");
			if (f == null)
				return;
			for (VFile<AssetData> fi : f.list()) {
				if (fi.getName().equals("CH"))
					continue;
				if (fi.getName().equals("D"))
					continue;
				VFile<AssetData> map = fi.list().get(0);
				List<VFile<AssetData>> stage = new ArrayList<>();
				for (int i = 1; i < fi.list().size(); i++)
					if (fi.list().get(i).list() != null)
						stage.addAll(fi.list().get(i).list());
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
			}
		}

		public final int id;
		public final String name;

		private DefMapColc() {
			id = 3;
			name = "CH";
			maps = new StageMap[14];
			String abbr = "./org/stage/CH/stageNormal/stageNormal";
			for (int i = 0; i < 3; i++) {
				AssetData vf = VFile.get(abbr + "0_" + i + "_Z.csv").getData();
				maps[i] = new StageMap(this, i, vf, 1);
				maps[i].name = "EoC " + (i + 1) + " Zombie";
				vf = VFile.get(abbr + "1_" + i + ".csv").getData();
				maps[3 + i] = new StageMap(this, 3 + i, vf, 2);
				maps[i + 3].name = "ItF " + (i + 1);
				vf = VFile.get(abbr + "2_" + i + ".csv").getData();
				maps[6 + i] = new StageMap(this, 6 + i, vf, 3);
				maps[i + 6].name = "CotC " + (i + 1);
			}
			AssetData stn = VFile.get(abbr + "0.csv").getData();
			maps[9] = new StageMap(this, 9, stn, 1);
			maps[9].name = "EoC 1-3";
			stn = VFile.get(abbr + "1_0_Z.csv").getData();
			maps[10] = new StageMap(this, 10, stn, 2);
			maps[10].name = "ItF 1 Zombie";
			stn = VFile.get(abbr + "2_2_Invasion.csv").getData();
			maps[11] = new StageMap(this, 11, stn, 2);
			maps[11].name = "CotC 3 Invasion";
			stn = VFile.get(abbr + "1_1_Z.csv").getData();
			maps[12] = new StageMap(this, 12, stn, 2);
			maps[12].name = "ItF 2 Zombie";
			maps[13] = new StageMap(this, 13, stn, 2);
			maps[13].name = "ItF 3 Zombie";
			VFile<AssetData> stz = VFile.get("./org/stage/CH/stageZ/");
			for (VFile<AssetData> vf : stz.list()) {
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
					maps[id0].add(new Stage.DefStage(maps[id0], id1, vf, 0));
				else if (id0 == 4)
					maps[10].add(new Stage.DefStage(maps[10], id1, vf, 0));
				else if (id0 == 5)
					maps[12].add(new Stage.DefStage(maps[12], id1, vf, 0));
				else if (id0 == 6)
					maps[13].add(new Stage.DefStage(maps[13], id1, vf, 0));
			}
			VFile<AssetData> stw = VFile.get("./org/stage/CH/stageW/");
			for (VFile<AssetData> vf : stw.list()) {
				String str = vf.getName();
				int id0 = -1, id1 = -1;
				try {
					id0 = Integer.parseInt(str.substring(6, 8));
					id1 = Integer.parseInt(str.substring(9, 11));
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				maps[id0 - 1].add(new Stage.DefStage(maps[id0 - 1], id1, vf, 1));
			}
			VFile<AssetData> sts = VFile.get("./org/stage/CH/stageSpace/");
			for (VFile<AssetData> vf : sts.list()) {
				String str = vf.getName();
				if (str.length() > 20) {
					maps[11].add(new Stage.DefStage(maps[11], 0, vf, 0));
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
				maps[id0 - 1].add(new Stage.DefStage(maps[id0 - 1], id1, vf, 1));
			}

			VFile<AssetData> st = VFile.get("./org/stage/CH/stage/");
			for (VFile<AssetData> vf : st.list()) {
				String str = vf.getName();
				int id0 = -1;
				try {
					id0 = Integer.parseInt(str.substring(5, 7));
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				maps[9].add(new Stage.DefStage(maps[9], id0, vf, 2));
			}
			maps[9].stars = new int[] { 100, 200, 400 };
		}

		private DefMapColc(String st, int ID, List<VFile<AssetData>> stage, VFile<AssetData> map) {
			name = st;
			id = ID;
			StageMap[] sms = new StageMap[map.list().size()];
			for (VFile<AssetData> m : map.list()) {
				String str = m.getName();
				int len = str.length();
				int id = -1;
				try {
					id = Integer.parseInt(str.substring(len - 7, len - 4));
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				sms[id] = new StageMap(this, id, m.getData());
			}
			maps = sms;

			for (VFile<AssetData> s : stage) {
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
				sms[id0].add(new Stage.DefStage(sms[id0], id1, s, 0));
			}
		}

		@Override
		public String toString() {
			String desp = MultiLangCont.get(this);
			if (desp != null && desp.length() > 0)
				return desp + " (" + maps.length + ")";
			return name + " (" + maps.length + ")";
		}

	}

	@JsonClass
	public static class PackMapColc extends MapColc {

		public final UserPack pack;

		public PackMapColc(UserPack pack) {
			this.pack = pack;
		}

		@Deprecated
		public PackMapColc(UserPack pack, InStream is) throws VerFixerException {
			this.pack = pack;
			int val = getVer(is.nextString());
			if (val != 308)
				throw new VerFixerException("MapColc requires 308, got " + val);
			is.nextString();

			int n = is.nextInt();
			for (int i = 0; i < n; i++) {
				CharaGroup cg = new CharaGroup.PackCG(this, is);
				groups.set(cg.id, cg);
			}

			n = is.nextInt();
			for (int i = 0; i < n; i++) {
				LvRestrict lr = new LvRestrict.PackLR(this, is);
				lvrs.set(lr.id, lr);
			}

			n = is.nextInt();
			maps = new StageMap[n];
			for (int i = 0; i < n; i++) {
				StageMap sm = new StageMap(this);
				maps[i] = sm;
				sm.name = is.nextString();
				sm.stars = is.nextIntsB();
				int m = is.nextInt();
				for (int j = 0; j < m; j++) {
					InStream sub = is.subStream();
					sm.add(new Stage.PackStage(sm, sub));
				}
				m = is.nextInt();
				for (int j = 0; j < m; j++)
					sm.lim.add(new Limit.PackLimit(this, is));
			}
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
			Stage ans = mc.maps[ism].list.get(is);
			is++;
			validate();
			return ans;
		}

		private void validate() {
			while (is >= mc.maps[ism].list.size()) {
				is = 0;
				ism++;
				while (ism >= mc.maps.length) {
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

	public static final String REG_MAPCOLC = "MapColc";

	private static String[] strs = new String[] { "rc", "ec", "sc", "wc" };

	public static Iterable<Stage> getAllStage() {
		return new StItr();
	}

	@JsonField(gen = GenType.FILL)
	public FixIndexList<CharaGroup> groups = new FixIndexList<>(CharaGroup.class);
	@JsonField(gen = GenType.FILL)
	public FixIndexList<LvRestrict> lvrs = new FixIndexList<>(LvRestrict.class);
	@JsonField
	public StageMap[] maps = new StageMap[0];

}