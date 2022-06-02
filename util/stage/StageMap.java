package common.util.stage;

import common.CommonStatic;
import common.io.json.JsonClass;
import common.io.json.JsonClass.JCConstructor;
import common.io.json.JsonField;
import common.pack.FixIndexList.FixIndexMap;
import common.pack.Identifier;
import common.pack.IndexContainer;
import common.system.BasedCopable;
import common.system.files.FileData;
import common.system.files.VFile;
import common.util.Data;
import common.util.lang.MultiLangCont;
import common.util.lang.MultiLangData;
import common.util.stage.info.DefStageInfo;

import java.util.ArrayList;
import java.util.Queue;

@IndexContainer.IndexCont(MapColc.class)
@JsonClass
public class StageMap extends Data implements BasedCopable<StageMap, MapColc>,
		IndexContainer.Indexable<MapColc, StageMap>, IndexContainer.SingleIC<Stage> {

	public static class StageMapInfo {

		public final StageMap sm;

		private final Queue<String> qs;

		public int rand, time, lim;

		public int waitTime = -1, clearLimit = -1, resetMode = -1;

		public boolean hiddenUponClear = false;

		private StageMapInfo(StageMap map, FileData ad) {
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
			String line = qs.poll();

			if(line == null)
				return;

			int[] ints = CommonStatic.parseIntsN(line.split("//")[0]);
			if (ints.length <= 4)
				return;
			s.info = new DefStageInfo(this, s, ints);
		}

	}

	@ContGetter
	public static StageMap get(String str) {
		String[] strs = str.split("/");
		if (strs[0].equals(Stage.CLIPMC.getSID()))
			return Stage.CLIPMC.maps.get(Integer.parseInt(strs[1]));
		return new Identifier<>(strs[0], StageMap.class, Integer.parseInt(strs[1])).get();
	}

	@JsonField
	public final Identifier<StageMap> id;
	@JsonField(generic = Limit.class)
	public final ArrayList<Limit> lim = new ArrayList<>();
	public StageMapInfo info;

	@JsonField(generic = Stage.class)
	public final FixIndexMap<Stage> list = new FixIndexMap<>(Stage.class);

	@JsonField(io = JsonField.IOType.R)
	public String name = "";
	@JsonField(generic = MultiLangData.class)
	public MultiLangData names = new MultiLangData();

	@JsonField
	public int price = 1, cast = -1;
	@JsonField
	public int[] stars = new int[] { 100 };

	public int starMask = 0;



	@JCConstructor
	public StageMap() {
		this.id = null;
	}

	public StageMap(Identifier<StageMap> id) {
		this.id = id;
		names.put("new stage map");

	}

	protected StageMap(Identifier<StageMap> id, FileData m) {
		this.id = id;
		info = new StageMapInfo(this, m);
	}

	protected StageMap(Identifier<StageMap> id, String stn, int cas) {
		this(id, VFile.get(stn).getData());
		cast = cas;
	}

	public void add(Stage s) {
		if (s == null)
			return;
		list.add(s);
	}

	@Override
	public StageMap copy(MapColc mc) {
		StageMap sm = new StageMap(mc.getNextID());
		sm.names = names.copy();
		sm.names.put(toString());

		sm.stars = stars.clone();
		for (Stage st : list)
			sm.add(st.copy(sm));
		return sm;
	}

	@Override
	public FixIndexMap<Stage> getFIM() {
		return list;
	}

	@Override
	public Identifier<StageMap> getID() {
		return id;
	}

	@Override
	public String getSID() {
		return id.pack + "/" + id.id;
	}

	@Override
	public String toString() {
		String desp = MultiLangCont.get(this);
		if (desp != null && desp.length() > 0)
			return desp;
		String stName = names.toString();
		if (stName.length() == 0)
			return id + " (" + list.size() + ")";
		return stName;
	}
}
