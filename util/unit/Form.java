package common.util.unit;

import common.CommonStatic;
import common.battle.data.*;
import common.io.json.JsonClass;
import common.io.json.JsonClass.JCConstructor;
import common.io.json.JsonClass.JCGeneric;
import common.io.json.JsonClass.JCGetter;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonClass.RType;
import common.io.json.JsonDecoder.OnInjected;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.pack.PackData;
import common.pack.UserProfile;
import common.system.BasedCopable;
import common.util.Animable;
import common.util.Data;
import common.util.anim.AnimU;
import common.util.anim.AnimUD;
import common.util.anim.EAnimU;
import common.util.lang.MultiLangCont;

@JCGeneric(Form.FormJson.class)
@JsonClass(read = RType.FILL)
public class Form extends Animable<AnimU<?>, AnimU.UType> implements BasedCopable<Form, Unit> {

	@JsonClass(noTag = NoTag.LOAD)
	public static class FormJson {

		public Identifier<Unit> uid;
		public int fid;

		@JCConstructor
		public FormJson() {
		}

		@JCConstructor
		public FormJson(Form f) {
			uid = f.uid;
			fid = f.fid;
		}

		@JCGetter
		public Form get() {
			return uid.get().forms[fid];
		}
	}

	public static String lvString(int[] lvs) {
		StringBuilder str = new StringBuilder("Lv." + lvs[0] + ", {");
		for (int i = 1; i < 5; i++)
			str.append(lvs[i]).append(",");
		str.append(lvs[5]).append("}");
		return str.toString();
	}

	@JsonField
	public final MaskUnit du;
	public final Unit unit;
	public final Identifier<Unit> uid;
	@JsonField
	public int fid;
	public Orb orbs = null;
	@JsonField
	public String name = "";
	@JsonField
	public String explanation = "<br><br><br>";

	@JCConstructor
	public Form(Unit u) {
		du = null;
		unit = u;
		uid = unit.id;
		orbs = new Orb(-1);
	}

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
		String nam = trio(uid.id) + "_" + SUFX[fid];
		anim = new AnimUD(str, nam, "edi" + nam + ".png", "uni" + nam + "00.png");
		anim.getUni().setCut(CommonStatic.getBCAssets().unicut);
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
	public EAnimU getEAnim(AnimU.UType t) {
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

	@OnInjected
	public void onInjected() {
		CustomUnit form = (CustomUnit) du;
		form.pack = this;

		if((unit != null || uid != null)) {
			Unit u = unit == null ? uid.get() : unit;

			if(u.getCont() instanceof PackData.UserPack) {
				PackData.UserPack pack = (PackData.UserPack) u.getCont();

				if(UserProfile.isOlderPack(pack, "0.5.1.0")) {
					form.type = Data.reorderTrait(form.type);
				}

				if (UserProfile.isOlderPack(pack, "0.5.2.0") && form.tba != 0) {
					form.tba += form.getPost() + 1;
				}

				if (UserProfile.isOlderPack(pack, "0.5.4.0")) {
					if (form.type != 0) {
						form.traits = Trait.convertType(form.type);
						form.type = 0;
					}
					if (form.getProc().SUMMON.prob > 0) {
						form.getProc().SUMMON.form = 1;
						form.getProc().SUMMON.mult = 1;
					}
				}
			}
		}
	}

	public int[] regulateLv(int[] mod, int[] lv) {
		if (mod != null)
			System.arraycopy(mod, 0, lv, 0, Math.min(mod.length, 6));
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
		String base = (uid == null ? "NULL" : uid.id) + "-" + fid + " ";
		String desp = MultiLangCont.get(this);
		if (desp != null && desp.length() > 0)
			return base + desp;
		if (name.length() > 0)
			return base + name;
		return base;
	}

	public String descriptionGet() {
		String[] desp = MultiLangCont.getDesc(this);
		if (desp != null && desp[fid + 1].length() > 0)
			return desp[fid + 1];
		if (explanation.length() > 0)
			return explanation;
		return "";
	}
}
