package common.util.unit;

import common.battle.data.CustomUnit;
import common.battle.data.DataUnit;
import common.battle.data.MaskUnit;
import common.battle.data.Orb;
import common.battle.data.PCoin;
import common.pack.PackData.Identifier;
import common.system.BasedCopable;
import common.util.Animable;
import common.util.anim.AnimU;
import common.util.anim.AnimUD;
import common.util.anim.EAnimU;
import common.util.anim.ImgCut;
import common.util.lang.MultiLangCont;

public class Form extends Animable<AnimU<?>> implements BasedCopable<Form, Unit> {

	public static ImgCut unicut, udicut;

	public static String lvString(int[] lvs) {
		String str = "Lv." + lvs[0] + ", {";
		for (int i = 1; i < 5; i++)
			str += lvs[i] + ",";
		str += lvs[5] + "}";
		return str;
	}

	public final MaskUnit du;
	public final Unit unit;
	public final Identifier<Unit> uid;
	public int fid;

	public Orb orbs = null;

	public String name = "";

	public Form(Unit u, int f, String str, AnimU<?> ac, CustomUnit cu) {
		unit = u;
		uid = u.id;
		fid = f;
		name = str;
		anim = ac;
		du = cu;
		cu.pack = this;
		orbs = new Orb(-1);
	}

	protected Form(Unit u, int f, String str, String data) {
		unit = u;
		uid = u.id;
		fid = f;
		String nam = uid.id + "_" + SUFX[fid];
		anim = new AnimUD(str, nam, "edi" + nam + ".png", "uni" + nam + "00.png");
		anim.getUni().setCut(unicut);
		String[] strs = data.split("//")[0].trim().split(",");
		du = new DataUnit(this, unit, strs);
	}

	@Override
	public Form copy(Unit b) {
		CustomUnit cu = new CustomUnit();
		cu.importData(du);
		return new Form(b, fid, name, anim, cu);
	}

	public int getDefaultPrice(int sta) {
		PCoin pc = getPCoin();
		int price = pc == null ? du.getPrice() : pc.full.getPrice();
		return (int) (price * (1 + sta * 0.5));
	}

	@Override
	public EAnimU getEAnim(int t) {
		return anim.getEAnim(t);
	}

	public String[] getExplanation() {
		String[] exp = MultiLangCont.getStatic().FEXP.getCont(this);
		if (exp != null)
			return exp;
		return new String[0];
	}

	public PCoin getPCoin() {
		if (du instanceof DataUnit)
			return ((DataUnit) du).pcoin;
		return null;
	}

	public MaskUnit maxu() {
		PCoin pc = getPCoin();
		if (pc != null)
			return pc.full;
		return du;
	}

	public int[] regulateLv(int[] mod, int[] lv) {
		if (mod != null)
			for (int i = 0; i < Math.min(mod.length, 6); i++)
				lv[i] = mod[i];
		int[] maxs = new int[6];
		maxs[0] = unit.max + unit.maxp;
		PCoin pc = null;
		if (unit.forms.length >= 3)
			pc = unit.forms[2].getPCoin();
		if (pc != null)
			for (int i = 0; i < 5; i++)
				maxs[i + 1] = Math.max(1, pc.info[i][1]);
		for (int i = 0; i < 6; i++) {
			if (lv[i] < 0)
				lv[i] = 0;
			if (lv[i] > maxs[i])
				lv[i] = maxs[i];
		}
		if (lv[0] == 0)
			lv[0] = 1;
		return lv;
	}

	@Override
	public String toString() {
		String base = uid.id + "-" + fid + " ";
		String desp = MultiLangCont.get(this);
		if (desp != null && desp.length() > 0)
			return base + desp;
		if (name.length() > 0)
			return base + name;
		return base;
	}

}
