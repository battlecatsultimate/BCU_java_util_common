package common.util.stage;

import common.CommonStatic;
import common.io.json.JsonClass;
import common.io.json.JsonClass.JCConstructor;
import common.io.json.JsonDecoder;
import common.io.json.JsonEncoder;
import common.io.json.JsonField;
import common.pack.FixIndexList.FixIndexMap;
import common.pack.Identifier;
import common.pack.IndexContainer;
import common.system.BasedCopable;
import common.system.files.FileData;
import common.system.files.VFile;
import common.util.Data;
import common.util.lang.MultiLangCont;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.google.gson.JsonParser;

@IndexContainer.IndexCont(MapColc.class)
@JsonClass
public class StageMap extends Data implements BasedCopable<StageMap, MapColc>,
		IndexContainer.Indexable<MapColc, StageMap>, IndexContainer.SingleIC<Stage> {

	public static class StageMapInfo {

		public final StageMap sm;

		private final Queue<String> qs;

		public int rand, time, lim;

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
			int[] ints = CommonStatic.parseIntsN(qs.poll().split("//")[0]);
			if (ints.length <= 4)
				return;
			s.info = new Stage.StageInfo(this, s, ints);
		}

	}

	@SuppressWarnings("unchecked")
	@ContGetter
	public static StageMap get(String str) {
		return Identifier.get(JsonDecoder.decode(JsonParser.parseString(str), Identifier.class));
	}

	@JsonField
	public final Identifier<StageMap> id;
	public final List<Limit> lim = new ArrayList<>();
	public StageMapInfo info;

	@JsonField(generic = Stage.class)
	public final FixIndexMap<Stage> list = new FixIndexMap<>(Stage.class);
	@JsonField
	public String name = "";
	@JsonField
	public int price = 1, retyp, pllim, set, cast = -1;
	@JsonField
	public int[] stars = new int[] { 100 };

	@JCConstructor
	public StageMap() {
		this.id = null;
	}

	public StageMap(Identifier<StageMap> id) {
		this.id = id;
		name = "new stage map";

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
		sm.name = name;
		if (name.length() == 0)
			sm.name = toString();
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
		return JsonEncoder.encode(id).toString();
	}

	@Override
	public String toString() {
		String desp = MultiLangCont.get(this);
		if (desp != null && desp.length() > 0)
			return desp;
		if (name.length() == 0)
			return id + " (" + list.size() + ")";
		return name;
	}

}
