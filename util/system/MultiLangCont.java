package common.util.system;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import common.util.stage.MapColc;
import common.util.stage.Stage;
import common.util.stage.StageMap;
import common.util.system.fake.CommonStatic;
import common.util.system.files.AssetData;
import common.util.unit.Enemy;
import common.util.unit.Form;

public class MultiLangCont<I, T> {

	public static final MultiLangCont<MapColc, String> MCNAME = new MultiLangCont<>();
	public static final MultiLangCont<StageMap, String> SMNAME = new MultiLangCont<>();
	public static final MultiLangCont<Stage, String> STNAME = new MultiLangCont<>();
	public static final MultiLangCont<Form, String> FNAME = new MultiLangCont<>();
	public static final MultiLangCont<Enemy, String> ENAME = new MultiLangCont<>();

	public static final Map<MultiLangFile, AssetData> VFILE = new HashMap<>();

	public static String get(Object o) {
		String loc = CommonStatic.LOC_CODE[CommonStatic.lang];
		if (o instanceof MapColc)
			return MCNAME.get(loc, (MapColc) o);
		if (o instanceof StageMap)
			return SMNAME.get(loc, (StageMap) o);
		if (o instanceof Stage)
			return STNAME.get(loc, (Stage) o);
		if (o instanceof Form)
			return FNAME.get(loc, (Form) o);
		if (o instanceof Enemy)
			return ENAME.get(loc, (Enemy) o);
		return null;
	}

	public static void redefine() {
		VFILE.forEach((mlf, f) -> mlf.reload(f));
	}

	private final Map<String, HashMap<I, T>> map = new TreeMap<>();

	public T get(String loc, I x) {
		T ans = getSub(loc).get(x);
		int lang = 0;
		while (ans == null && lang < 4)
			ans = getSub(CommonStatic.LOC_CODE[lang++]).get(x);
		return ans;
	}

	public void put(String loc, I x, T t) {
		if (x == null || t == null)
			return;
		getSub(loc).put(x, t);
	}

	private HashMap<I, T> getSub(String loc) {
		HashMap<I, T> ans = map.get(loc);
		if (ans == null)
			map.put(loc, ans = new HashMap<>());
		return ans;
	}

}
