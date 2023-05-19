package common.util.lang;

import common.CommonStatic;
import common.CommonStatic.Lang;
import common.pack.UserProfile;
import common.util.anim.AnimI;
import common.util.stage.MapColc;
import common.util.stage.Stage;
import common.util.stage.StageMap;
import common.util.unit.Combo;
import common.util.unit.Enemy;
import common.util.unit.Form;
import common.util.unit.Unit;

import java.util.*;

public class MultiLangCont<I, T> extends Lang {

	public static class MultiLangStatics {

		public final MultiLangCont<MapColc, String> MCNAME = new MultiLangCont<>();
		public final MultiLangCont<StageMap, String> SMNAME = new MultiLangCont<>();
		public final MultiLangCont<Stage, String> STNAME = new MultiLangCont<>();
		public final MultiLangCont<Integer, String> RWNAME = new MultiLangCont<>();
		public final MultiLangCont<Integer, String> RWSTNAME = new MultiLangCont<>();
		public final MultiLangCont<Integer, String> RWSVNAME = new MultiLangCont<>();
		public final MultiLangCont<Form, String> FNAME = new MultiLangCont<>();
		public final MultiLangCont<Enemy, String> ENAME = new MultiLangCont<>();
		public final MultiLangCont<Combo, String> COMNAME = new MultiLangCont<>();
		public final MultiLangCont<Form, String[]> FEXP = new MultiLangCont<>();
		public final MultiLangCont<Unit.UnitInfo, String> CFEXP = new MultiLangCont<>();
		public final MultiLangCont<Enemy, String[]> EEXP = new MultiLangCont<>();

		protected final MultiLangCont<AnimI.AnimType<?, ?>, String> ANIMNAME = new MultiLangCont<>();
		protected final boolean[] loaded_anim = new boolean[LOC_CODE.length];

		public void clear() {
			Arrays.fill(loaded_anim, false);
			ProcLang.clear();
		}

		public String getAnimName(AnimI.AnimType<?, ?> type) {
			int lang = Math.max(0, Math.min(pref.length, CommonStatic.getConfig().lang));

			if (!loaded_anim[lang]) {
				loaded_anim[lang] = true;
				AnimTypeLocale.read();
			}

			return ANIMNAME.getCont(type);
		}

	}

	public static String get(Object o) {
		if (o == null)
			return null;

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
		if (o instanceof Combo)
			return getStatic().COMNAME.getCont((Combo) o);

		return null;
	}

	public static String get(Object o, int lang) {
		if(o == null)
			return null;

		if (o instanceof MapColc)
			return getStatic().MCNAME.getCont((MapColc) o, lang);
		if (o instanceof StageMap)
			return getStatic().SMNAME.getCont((StageMap) o, lang);
		if (o instanceof Stage)
			return getStatic().STNAME.getCont((Stage) o, lang);
		if (o instanceof Form)
			return getStatic().FNAME.getCont((Form) o, lang);
		if (o instanceof Enemy)
			return getStatic().ENAME.getCont((Enemy) o, lang);
		if (o instanceof Combo)
			return getStatic().COMNAME.getCont((Combo) o, lang);
		
		return null;
	}

	public static int getGrabbedLocale(Object o) {
		if (o == null)
			return -1;

		if (o instanceof MapColc)
			return getStatic().MCNAME.getSelectedLocale((MapColc) o);
		if (o instanceof StageMap)
			return getStatic().SMNAME.getSelectedLocale((StageMap) o);
		if (o instanceof Stage)
			return getStatic().STNAME.getSelectedLocale((Stage) o);
		if (o instanceof Form)
			return getStatic().FNAME.getSelectedLocale((Form) o);
		if (o instanceof Enemy)
			return getStatic().ENAME.getSelectedLocale((Enemy) o);
		if (o instanceof Combo)
			return getStatic().COMNAME.getSelectedLocale((Combo) o);

		return -1;
	}

	public static String[] getDesc(Object o) {
		if (o == null)
			return null;

		if (o instanceof Form)
			return getStatic().FEXP.getCont((Form) o);
		if (o instanceof Enemy)
			return getStatic().EEXP.getCont((Enemy) o);

		return null;
	}

	public static MultiLangStatics getStatic() {
		return UserProfile.getStatic("MultiLangStatics", MultiLangStatics::new);
	}

	public static String getStageDrop(int id) {
		String trial = getStatic().RWSTNAME.getCont(id);

		if(trial == null || trial.isEmpty())
			trial = getStatic().RWNAME.getCont(id);

		return trial;
	}

	public static String getStageDrop(int id, int lang) {
		String trial = getStatic().RWSTNAME.getCont(id, lang);

		if(trial == null || trial.isEmpty())
			trial = getStatic().RWNAME.getCont(id, lang);

		return trial;
	}

	public static String getServerDrop(int id) {
		String trial = getStatic().RWSVNAME.getCont(id);

		if(trial == null || trial.isEmpty())
			trial = getStatic().RWNAME.getCont(id);

		return trial;
	}

	public static String getServerDrop(int id, int lang) {
		String trial = getStatic().RWSVNAME.getCont(id, lang);

		if(trial == null || trial.isEmpty())
			trial = getStatic().RWNAME.getCont(id, lang);

		return trial;
	}

	private final Map<String, HashMap<I, T>> map = new TreeMap<>();

	public void clear() {
		map.clear();
	}

	public T getCont(I x) {
		int lang = Math.max(0, Math.min(pref.length, CommonStatic.getConfig().lang));

		for (int i = 0; i < pref[lang].length; i++) {
			T ans = getSub(LOC_CODE[pref[lang][i]]).get(x);
			if (ans != null)
				return ans;
		}
		
		return null;
	}

	public T getCont(I x, int lang) {
		int l = Math.max(0, Math.min(pref.length, lang));

		for (int i = 0; i < pref[l].length; i++) {
			T ans = getSub(LOC_CODE[pref[l][i]]).get(x);

			if (ans != null)
				return ans;
		}

		return null;
	}

	public int getSelectedLocale(I x) {
		int lang = Math.max(0, Math.min(pref.length, CommonStatic.getConfig().lang));

		for(int i = 0; i < pref[lang].length; i++) {
			T ans =  getSub(LOC_CODE[pref[lang][i]]).get(x);

			if(ans != null)
				return pref[lang][i];
		}

		return -1;
	}

	public void put(String loc, I x, T t) {
		if (x == null || t == null)
			return;
		getSub(loc).put(x, t);
	}

	private HashMap<I, T> getSub(String loc) {
		return map.computeIfAbsent(loc, k -> new HashMap<>());
	}

	public Map<I, T> getMap(String loc) {
		return map.get(loc);
	}
}
