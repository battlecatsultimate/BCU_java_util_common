package common.util.pack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import common.CommonStatic;
import common.CommonStatic.ImgReader;
import common.io.InStream;
import common.util.Data;
import common.util.stage.Castles;
import common.util.stage.MapColc;

public class Pack extends Data {

	public static final Map<Integer, Pack> map = new TreeMap<>();

	public static Pack def = new Pack();

	public static final int RELY_DEF = 0, RELY_CAS = 1, RELY_BG = 2, RELY_MUS = 3, RELY_ENE = 4, RELY_UNI = 5,
			RELY_CG = 6, RELY_LR = 7, RELY_ABI = 8;
	public static final int M_ES = 0, M_UL = 1, M_US = 2, M_CG = 3, M_LR = 4, M_BG = 5, M_CS = 6, M_MS = 7;

	public static String getAvailable(String str) {
		while (contains(str))
			str += "'";
		return str;
	}

	public static List<Pack> getEditable(int[] alr) {
		List<Pack> ans = new ArrayList<>();
		for (Pack p : map.values())
			if (p.editable) {
				boolean ava = true;
				for (int id : alr)
					ava &= p.id != id;
				if (ava)
					ans.add(p);
			}
		return ans;
	}

	private static boolean contains(String str) {
		for (Pack p : map.values())
			if (p.name.equals(str))
				return true;
		return false;
	}

	public int id;

	public final SoulStore ss = new SoulStore(this);

	public final MusicStore ms = new MusicStore(this);
	public MapColc mc;
	public final List<Integer> rely = new ArrayList<>();
	public boolean editable = true;
	public String name = "custom pack", time = "", author = "";
	public File file;
	public int version;
	private InStream res;
	private ImgReader reader;

	private int ver, bcuver;

	public boolean canDelete() {
		for (Pack p : map.values())
			if (p.rely.contains(id))
				return false;
		return true;
	}

	public void delete() {
		if (!canDelete())
			return;
		CommonStatic.def.delete(CommonStatic.def.route("./res/enemy/" + id + ".bcuenemy"));
		CommonStatic.def.delete(CommonStatic.def.route("./res/img/" + id + "/"));
		map.remove(id);
		Castles.map.remove(id);
	}

}