package common.util.unit;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import common.CommonStatic;
import common.CommonStatic.ImgReader;
import common.io.InStream;
import common.io.OutStream;
import common.util.Animable;
import common.util.anim.AnimCE;
import common.util.anim.AnimU;
import common.util.anim.EAnimI;
import common.util.pack.Pack;
import main.Opts;
import main.Printer;

public class DIYAnim extends Animable<AnimCE> {

	public static final Map<String, DIYAnim> map = new TreeMap<>();

	public static AnimCE getAnim(String str, boolean ene) {
		DIYAnim ai = DIYAnim.map.get(str);
		if (ai == null) {
			Collection<DIYAnim> cd = DIYAnim.map.values();
			if (cd.size() > 0)
				ai = cd.iterator().next();
			Printer.e("DIYAnim", 26, "Animation Missing: " + str);
			Opts.loadErr("Animation Missing: " + str);
		}
		if (ai == null) {
			AnimU<?> au;
			if (ene)
				au = EnemyStore.getEnemy(0).anim;
			else
				au = Pack.def.us.ulist.get(0).forms[0].anim;
			return new AnimCE("error", au);
		}
		return ai.getAnimC();
	}

	public static List<AnimCE> getAnims() {
		List<AnimCE> ans = new ArrayList<>();
		for (DIYAnim da : map.values())
			ans.add(da.anim);
		return ans;
	}

	public static void read() {
		map.clear();
		File f = CommonStatic.def.route("./res/anim/");
		if (f.exists())
			for (File fi : f.listFiles()) {
				if (!fi.isDirectory())
					continue;
				File check = CommonStatic.def.route("./res/anim/" + fi.getName() + "/" + fi.getName() + ".png");
				if (!check.exists())
					continue;
				new DIYAnim(fi.getName());
			}
	}

	public static OutStream writeAnim(AnimCE anim) {
		OutStream os = OutStream.getIns();
		os.writeString("0.4.1");
		os.writeInt(anim.inPool);
		if (anim.inPool == 0)
			os.writeString(anim.toString());
		else if (anim.inPool == 1)
			os.accept(anim.write());
		else if (anim.inPool == 2) {
			// TODO
		}
		os.terminate();
		return os;
	}

	public static AnimCE zread(InStream nam, ImgReader r, boolean ene) {
		int ver = getVer(nam.nextString());
		if (ver >= 000401)
			return zread$000401(nam, r, ene);
		return null;
	}

	private static AnimCE zread$000401(InStream is, ImgReader r, boolean ene) {
		int type = is.nextInt();
		if (type == 0)
			return getAnim(is.nextString(), ene);
		else if (type == 1)
			return new AnimCE(is.subStream(), r);
		else if (type == 2)
			return null; // TODO
		else
			return null;
	}

	public DIYAnim(AnimCE ac) {
		anim = ac;
	}

	public DIYAnim(String str) {
		map.put(str, this);
		anim = new AnimCE(str);
		anim.load();
	}

	public DIYAnim(String str, AnimCE ac) {
		map.put(str, this);
		anim = ac;
	}

	public boolean deletable() {
		for (Pack pack : Pack.map.values()) {
			for (Enemy e : pack.es.getList())
				if (e.anim == anim)
					return false;
			for (Unit u : pack.us.ulist.getList())
				for (Form f : u.forms)
					if (f.anim == anim)
						return false;
		}
		return true;
	}

	public AnimCE getAnimC() {
		return anim;
	}

	@Override
	public EAnimI getEAnim(int t) {
		if (anim == null)
			return null;
		return anim.getEAnim(t);
	}

	@Override
	public String toString() {
		return anim.toString();
	}

}
