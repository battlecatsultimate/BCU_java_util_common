package common.util.stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import common.CommonStatic;
import common.battle.BasisLU;
import common.io.InStream;
import common.io.OutStream;
import common.pack.PackData;
import common.pack.PackData.UserPack;
import common.pack.UserProfile;
import common.util.Data;
import common.util.stage.MapColc.DefMapColc;

public class Recd extends Data {

	private static class Wait {

		private final Stage.PackStage st;
		private final InStream is;
		private final String str;

		private Wait(Stage.PackStage stage, InStream input, String name) {
			st = stage;
			is = input;
			str = name;
		}

	}

	public static final Map<String, Recd> map = new TreeMap<>();

	private static final List<Wait> WAIT = new ArrayList<>();

	public static String getAvailable(String str) {
		while (map.containsKey(str))
			str += "'";
		return str;
	}

	public static void getRecd(Stage.PackStage stage, InStream is, String str) {
		WAIT.add(new Wait(stage, is, str));
	}

	public static void read() {
		File f = CommonStatic.def.route("./replay/");
		if (!f.exists())
			return;
		File[] fs = f.listFiles();
		for (File fi : fs) {
			String str = fi.getName();
			if (str.endsWith(".replay")) {
				String name = str.substring(0, str.length() - 7);
				InStream is = CommonStatic.def.readBytes(fi);
				Recd rec = getRecd(is, name);
				if (rec != null)
					map.put(name, rec);
			}
		}
		for (Wait w : WAIT)
			w.st.recd.add(getRecd(w.is, w.str));
		WAIT.clear();
	}

	private static Recd getRecd(InStream is, String name) {
		int val = getVer(is.nextString());
		if (val >= 401)
			return zread$000401(is, name);
		return null;
	}

	private static Recd zread$000401(InStream is, String name) {
		long seed = is.nextLong();
		int[] conf = is.nextIntsB();
		int star = is.nextInt();
		BasisLU lu = BasisLU.zread(is.subStream());
		InStream action = is.subStream();
		int pid = is.nextInt();
		Stage st = null;
		if (pid == 0) {
			int id = is.nextInt();
			StageMap sm = DefMapColc.getMap(id / 1000);
			st = sm.list.get(id % 1000);
			if (st == null) {
				// TODO Opts.recdErr(name, "stage " + id);
				return null;
			}
		} else {
			st = zreads$000401(is, pid, name);
		}

		Recd ans = new Recd(lu, st, star, conf, seed);
		ans.action = action.translate();
		ans.name = name;
		return ans;
	}

	private static Stage zreads$000401(InStream is, int pid, String name) {
		String mcn = is.nextString();
		String smid = is.nextString();
		String stid = is.nextString();
		PackData pack = UserProfile.getPack(Data.hex(pid));
		if (pid != 0 && pack == null) {
			// TODO Opts.recdErr(name, "pack " + pid);
			return null;
		}
		MapColc mc = null;
		if (pid == 0)
			mc = DefMapColc.getMap(mcn);
		else
			mc = ((UserPack) pack).mc;
		StageMap sm = null;
		for (StageMap map : mc.maps)
			if (map.name.equals(smid))
				sm = map;
		if (sm == null) {
			// TODO Opts.recdErr(name, "stage map " + smid);
			return null;
		}
		Stage st = null;
		for (Stage s : sm.list)
			if (s.name.equals(stid))
				st = s;
		if (st == null) {
			// TODO Opts.recdErr(name, "stage " + stid);
			return null;
		}
		return st;
	}

	public String name = "new record";
	public long seed;
	public int[] conf;
	public int star, len;
	public BasisLU lu;
	public boolean avail;

	public boolean marked;

	public OutStream action;

	public Stage st;

	public Recd(BasisLU blu, Stage sta, int stars, int[] con, long se) {
		lu = blu;
		st = sta;
		star = stars;
		conf = con;
		seed = se;
		avail = st != null;
	}

	@Override
	public Recd clone() {
		return new Recd(lu.copy(), st, star, conf.clone(), seed);
	}

	public int getLen() {
		if (len > 0)
			return len;
		InStream is = action.translate();
		int n = is.nextInt();
		for (int i = 0; i < n / 2; i++) {
			is.nextInt();
			len += is.nextInt();
		}
		return len;
	}

	@Override
	public String toString() {
		return name;
	}

}
