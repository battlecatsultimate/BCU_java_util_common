package common.util.unit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import common.CommonStatic;
import common.battle.data.CustomUnit;
import common.battle.data.PCoin;
import common.io.json.JsonClass.JCGeneric;
import common.io.json.JsonClass.JCGenericRead;
import common.io.json.JsonClass.JCGenericWrite;
import common.pack.PackData.Identifier;
import common.system.FixIndexList;
import common.system.MultiLangCont;
import common.system.files.AssetData;
import common.system.files.VFile;
import common.util.Data;
import common.util.anim.AnimCE;
import common.util.pack.Pack;

public class Unit extends Data implements Comparable<Unit> {

	public static class UnitInfo {

		public int[][] evo;
		public int[] price = new int[10];
		public String[][] explanation;
		public int type;

		public void fillBuy(String[] strs) {
			for (int i = 0; i < 10; i++)
				price[i] = Integer.parseInt(strs[2 + i]);
			type = Integer.parseInt(strs[12]);
			int et = Integer.parseInt(strs[23]);
			if (et >= 15000 && et < 17000) {
				evo = new int[6][2];
				evo[0][0] = Integer.parseInt(strs[27]);
				for (int i = 0; i < 5; i++) {
					evo[i + 1][0] = Integer.parseInt(strs[28 + i * 2]);
					evo[i + 1][1] = Integer.parseInt(strs[29 + i * 2]);
				}
			}
		}

		public String[] getExplanation() {
			String[] exp = MultiLangCont.CFEXP.getCont(this);
			if (exp != null)
				return exp;
			return new String[0];
		}

	}

	public final Identifier id;
	public int rarity, max, maxp;
	public Form[] forms;
	public UnitLevel lv;

	public final UnitInfo info = new UnitInfo();

	public Unit(Identifier identifier) {
		id = identifier;
	}

	public Unit(VFile<AssetData> p) {
		id = new Identifier("_default", Data.trio(CommonStatic.parseIntN(p.getName())));
		String str = "./org/unit/" + id.id + "/";
		Queue<String> qs = VFile.readLine(str + "unit" + id.id + ".csv");
		forms = new Form[p.countSubDire()];
		for (int i = 0; i < forms.length; i++)
			forms[i] = new Form(this, i, str + SUFX[i] + "/", qs.poll());
		for (Form f : forms)
			f.anim.getEdi().check();
	}

	protected Unit(Identifier id, DIYAnim da, CustomUnit cu) {
		this.id = id;
		forms = new Form[] { new Form(this, 0, "new unit", da.getAnimC(), cu) };
		max = 50;
		maxp = 0;
		rarity = 4;
		lv = UnitLevel.def;
		lv.units.add(this);
	}

	protected Unit(Identifier id, Unit u) {
		this.id = id;
		rarity = u.rarity;
		max = u.max;
		maxp = u.maxp;
		lv = u.lv;
		lv.units.add(u);
		forms = new Form[u.forms.length];
		for (int i = 0; i < forms.length; i++) {
			String str = AnimCE.getAvailable(id + "-" + i);
			AnimCE ac = new AnimCE(str, u.forms[i].anim);
			DIYAnim da = new DIYAnim(str, ac);
			DIYAnim.map.put(str, da);
			CustomUnit cu = new CustomUnit();
			cu.importData(u.forms[i].du);
			forms[i] = new Form(this, i, str, ac, cu);
		}
	}

	public List<Combo> allCombo() {
		List<Combo> ans = new ArrayList<>();
		if (!id.pack.equals("_default"))
			return ans;
		for (Combo[] cs : Combo.combos)
			for (Combo c : cs)
				for (int[] is : c.units)
					if (trio(is[0]).equals(id.id)) {
						ans.add(c);
						break;
					}
		return ans;
	}

	@Override
	public int compareTo(Unit u) {
		return id.compareTo(u.id);
	}

	public int getPrefLv() {
		return max + (rarity < 2 ? maxp : 0);
	}

	public int[] getPrefLvs() {
		int[] ans = new int[6];
		if (forms.length >= 3) {
			PCoin pc = forms[2].getPCoin();
			if (pc != null)
				ans = pc.max.clone();
		}
		ans[0] = getPrefLv();
		return ans;
	}

	@Override
	public String toString() {
		String desp = MultiLangCont.get(forms[0]);
		if (desp != null && desp.length() > 0)
			return id.id + " " + desp;
		if (forms[0].name.length() > 0)
			return id.id + " " + forms[0].name;
		return id.id;
	}

}