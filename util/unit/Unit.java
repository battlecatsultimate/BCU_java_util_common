package common.util.unit;

import common.CommonStatic;
import common.battle.data.CustomUnit;
import common.io.json.FieldOrder;
import common.io.json.JsonClass;
import common.io.json.JsonClass.JCGeneric;
import common.io.json.JsonClass.JCIdentifier;
import common.io.json.JsonField;
import common.io.json.JsonField.GenType;
import common.pack.Identifier;
import common.pack.IndexContainer.IndexCont;
import common.pack.IndexContainer.Indexable;
import common.pack.PackData;
import common.pack.Source;
import common.pack.Source.ResourceLocation;
import common.pack.Source.Workspace;
import common.pack.UserProfile;
import common.system.files.VFile;
import common.util.Data;
import common.util.anim.AnimCE;
import common.util.lang.MultiLangCont;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

@IndexCont(PackData.class)
@JCGeneric(Identifier.class)
@JsonClass
public class Unit extends Data implements Comparable<Unit>, Indexable<PackData, Unit> {

	public static class UnitInfo {

		public int[][] evo;
		public int[] price = new int[10];
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

		public String[] getCatfruitExplanation() {
			String[] exp = MultiLangCont.getStatic().CFEXP.getCont(this);
			if (exp != null)
				return exp;
			return new String[0];
		}

	}

	@JsonField
	@JCIdentifier
	@FieldOrder.Order(0)
	public final Identifier<Unit> id;
	@JsonField
	@FieldOrder.Order(1)
	public int rarity, max, maxp;
	@JsonField(gen = GenType.GEN)
	@FieldOrder.Order(2)
	public Form[] forms;
	@JsonField(alias = Identifier.class)
	@FieldOrder.Order(3)
	public UnitLevel lv;

	public final UnitInfo info = new UnitInfo();

	@JsonClass.JCConstructor
	public Unit() {
		id = null;
	}

	public Unit(Identifier<Unit> identifier) {
		id = identifier;
	}

	public Unit(Identifier<Unit> id, AnimCE ce, CustomUnit cu) {
		this.id = id;
		forms = new Form[] { new Form(this, 0, "new unit", ce, cu) };
		max = 50;
		maxp = 0;
		rarity = 4;
		lv = CommonStatic.getBCAssets().defLv;
		lv.units.add(this);
	}

	public Unit(VFile p, int[] m) {
		id = new Identifier<>(Identifier.DEF, Unit.class, CommonStatic.parseIntN(p.getName()));
		String str = "./org/unit/" + Data.trio(id.id) + "/";
		Queue<String> qs = VFile.readLine(str + "unit" + Data.trio(id.id) + ".csv");

		if (p.countSubDire() != 0 && m[0] + m[1] >= 0)
			forms = new Form[p.countSubDire() + m.length];
		else if (p.countSubDire() != 0)
			forms = new Form[p.countSubDire()];
		else
			forms = new Form[m.length];

		for (int i = 0; i < forms.length; i++) {
			if (p.containsSubDire(SUFX[i])) {
				forms[i] = new Form(this, i, str + SUFX[i] + "/", qs.poll());
			} else
				forms[i] = new Form(this, i, m[i], "./org/img/m/" + Data.trio(m[i]) + "/", qs.poll());
		} for (Form f : forms)
			f.anim.getEdi().check();
	}

	protected Unit(Identifier<Unit> id, Unit u) {
		this.id = id;
		rarity = u.rarity;
		max = u.max;
		maxp = u.maxp;
		lv = u.lv;
		lv.units.add(u);
		forms = new Form[u.forms.length];
		for (int i = 0; i < forms.length; i++) {
			String str = id + "-" + i;
			ResourceLocation rl = new ResourceLocation(ResourceLocation.LOCAL, str, Source.BasePath.ANIM);
			Workspace.validate(rl);
			AnimCE ac = new AnimCE(rl, u.forms[i].anim);
			CustomUnit cu = new CustomUnit();
			cu.importData(u.forms[i].du);
			forms[i] = new Form(this, i, str, ac, cu);
		}
	}

	public List<Combo> allCombo() {
		List<Combo> ans = new ArrayList<>();
		List<Combo> comboList = UserProfile.getBCData().combos.getList();
		UserProfile.getUserPacks().forEach(p -> comboList.addAll(p.combos.getList()));
		for (Combo c : comboList)
			for (Form f : id.get().forms)
				if (Arrays.stream(c.forms).anyMatch(form -> form.uid.compareTo(f.uid) == 0)) {
					ans.add(c);
					break;
				}
		return ans;
	}

	@Override
	public int compareTo(Unit u) {
		return id.compareTo(u.id);
	}

	@Override
	public Identifier<Unit> getID() {
		return id;
	}

	public int getPrefLv() {
		return Math.min(CommonStatic.getConfig().prefLevel, max) + Math.min((rarity < 2 && maxp > 0 ? (int)((CommonStatic.getConfig().prefLevel - 1) / 49.0 * maxp) : 0),maxp);
	}

	@Override
	public String toString() {
		String desp = MultiLangCont.get(forms == null ? null : forms[0]);
		if (desp != null && desp.length() > 0)
			return Data.trio(id.id) + " " + desp;
		String name = forms[0].names.toString();
		if (name.length() > 0)
			return Data.trio(id.id) + " " + name;
		return Data.trio(id.id);
	}
}