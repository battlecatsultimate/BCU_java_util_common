package common.util.stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import common.CommonStatic;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.system.BasedCopable;
import common.system.MultiLangCont;
import common.system.files.AssetData;
import common.util.Data;

@JsonClass
public class StageMap extends Data implements BasedCopable<StageMap, MapColc> {

	public static class StageMapInfo {

		public final StageMap sm;

		private Queue<String> qs;

		public int rand, time, lim;

		private StageMapInfo(StageMap map, AssetData ad) {
			sm = map;
			qs = ad.readLine();
			int[] ints = CommonStatic.parseIntsN(qs.poll().split("//")[0]);
			if (ints.length > 3) {
				rand = ints[1];
				time = ints[2];
				lim = ints[3];
			}
			qs.poll();
		}

		protected void getData(Stage s) {
			int[] ints = CommonStatic.parseIntsN(qs.poll().split("//")[0]);
			if (ints.length <= 4)
				return;
			s.info = new Stage.StageInfo(this, s, ints);
		}

	}

	public final MapColc mc;
	public final List<Limit> lim = new ArrayList<>();
	public StageMapInfo info;
	
	@JsonField(generic = Stage.class)
	public final List<Stage> list = new ArrayList<>();
	@JsonField
	public String name = "";
	@JsonField
	public int id,price = 1, retyp, pllim, set,cast = -1;
	@JsonField
	public int[] stars = new int[] { 100 };

	public StageMap(MapColc map) {
		mc = map;
		name = "new stage map";
	}

	protected StageMap(MapColc map, int ID, AssetData m) {
		info = new StageMapInfo(this, m);
		mc = map;
		id = ID;

	}

	protected StageMap(MapColc map, int ID, AssetData m, int cas) {
		this(map, ID, m);
		cast = cas;
	}

	public void add(Stage s) {
		if (s == null)
			return;
		list.add(s);
	}

	@Override
	public StageMap copy(MapColc mc) {
		StageMap sm = new StageMap(mc);
		sm.name = name;
		if (name.length() == 0)
			sm.name = toString();
		sm.stars = stars.clone();
		for (Stage st : list)
			sm.add(st.copy(sm));
		return sm;
	}

	@Override
	public String toString() {
		String desp = MultiLangCont.get(this);
		if (desp != null && desp.length() > 0)
			return desp;
		if (name.length() == 0)
			return mc + " - " + id + " (" + list.size() + ")";
		return name;
	}

}
