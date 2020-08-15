package common.util.lang;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import common.CommonStatic;
import common.CommonStatic.Lang;
import common.pack.UserProfile;
import common.util.anim.AnimI;
import common.util.stage.MapColc;
import common.util.stage.Stage;
import common.util.stage.StageMap;
import common.util.unit.Enemy;
import common.util.unit.Form;
import common.util.unit.Unit;

public class MultiLangCont<I, T> extends Lang {

	public static class MultiLangStatics {

		public final MultiLangCont<MapColc, String> MCNAME = new MultiLangCont<>();
		public final MultiLangCont<StageMap, String> SMNAME = new MultiLangCont<>();
		public final MultiLangCont<Stage, String> STNAME = new MultiLangCont<>();
		public final MultiLangCont<Integer, String> RWNAME = new MultiLangCont<>();
		public final MultiLangCont<Form, String> FNAME = new MultiLangCont<>();
		public final MultiLangCont<Enemy, String> ENAME = new MultiLangCont<>();
		public final MultiLangCont<Integer, String> COMNAME = new MultiLangCont<>();
		public final MultiLangCont<Form, String[]> FEXP = new MultiLangCont<>();
		public final MultiLangCont<Unit.UnitInfo, String[]> CFEXP = new MultiLangCont<>();
		public final MultiLangCont<Enemy, String[]> EEXP = new MultiLangCont<>();
		public final MultiLangCont<AnimI.AnimType<?, ?>, String> ANIMNAME = new MultiLangCont<>();

	}

	public static String get(Object o) {
		if (o instanceof MapColc)
			return getStatic().MCNAME.getCont((MapColc) o);
		if (o instanceof StageMap)
			return getStatic().SMNAME.getCont((StageMap) o);
		if (o instanceof Stage)
			return getStatic().STNAME.getCont((Stage) o);
		if (o instanceof Form)
			return getStatic().FNAME.getCont((Form) o);
		if (o instanceof Enemy)
			return getStatic().ENAME.getCont((Enemy) o);
		return null;
	}

	public static MultiLangStatics getStatic() {
		return UserProfile.getStatic("MultiLangStatics", MultiLangStatics::new);
	}

	private final Map<String, HashMap<I, T>> map = new TreeMap<>();

	public void clear() {
		map.clear();
	}

	public T getCont(I x) {
		for (int i = 0; i < pref[CommonStatic.getConfig().lang].length; i++) {
			T ans = getSub(LOC_CODE[pref[CommonStatic.getConfig().lang][i]]).get(x);
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
