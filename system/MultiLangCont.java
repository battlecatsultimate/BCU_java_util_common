package common.system;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import common.CommonStatic.Lang;
import common.system.files.AssetData;
import common.util.stage.MapColc;
import common.util.stage.Stage;
import common.util.stage.StageMap;
import common.util.unit.Enemy;
import common.util.unit.Form;
import common.util.unit.Unit;

public class MultiLangCont<I, T> extends Lang {

	public static final MultiLangCont<MapColc, String> MCNAME = new MultiLangCont<>();
	public static final MultiLangCont<StageMap, String> SMNAME = new MultiLangCont<>();
	public static final MultiLangCont<Stage, String> STNAME = new MultiLangCont<>();
	public static final MultiLangCont<Integer, String> RWNAME = new MultiLangCont<>();
	public static final MultiLangCont<Form, String> FNAME = new MultiLangCont<>();
	public static final MultiLangCont<Enemy, String> ENAME = new MultiLangCont<>();

	public static final MultiLangCont<Form, String[]> FEXP = new MultiLangCont<>();
	public static final MultiLangCont<Unit.UnitInfo, String[]> CFEXP = new MultiLangCont<>();
	public static final MultiLangCont<Enemy, String[]> EEXP = new MultiLangCont<>();

	public static final Map<MultiLangFile, AssetData> VFILE = new HashMap<>();

	public static String get(Object o) {
		if (o instanceof MapColc)
			return MCNAME.getCont((MapColc) o);
		if (o instanceof StageMap)
			return SMNAME.getCont((StageMap) o);
		if (o instanceof Stage)
			return STNAME.getCont((Stage) o);
		if (o instanceof Form)
			return FNAME.getCont((Form) o);
		if (o instanceof Enemy)
			return ENAME.getCont((Enemy) o);
		return null;
	}

	public static void redefine() {
		VFILE.forEach((mlf, f) -> mlf.reload(f));
	}

	private final Map<String, HashMap<I, T>> map = new TreeMap<>();

	public void clear() {
		map.clear();
	}

	public T getCont(I x) {
		for (int i = 0; i < pref[lang].length; i++) {
			T ans = getSub(LOC_CODE[pref[lang][i]]).get(x);
			if (ans != null)
				return ans;
		}
		return null;
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
