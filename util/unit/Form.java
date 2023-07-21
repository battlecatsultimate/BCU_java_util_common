package common.util.unit;

import common.CommonStatic;
import common.battle.data.*;
import common.io.json.JsonClass;
import common.io.json.JsonClass.*;
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
import common.util.anim.MaModel;
import common.util.lang.MultiLangCont;
import common.util.lang.MultiLangData;

import javax.annotation.Nullable;

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
			try {
				return uid.get().forms[fid];
			} catch (Exception e) {
				return null;
			}
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

	@JsonField(io = JsonField.IOType.R)
	public String name = "";
	@JsonField(generic = MultiLangData.class)
	public MultiLangData names = new MultiLangData();

	@JsonField(io = JsonField.IOType.R)
	public String explanation = "<br><br><br>";
	@JsonField(generic = MultiLangData.class)
	public MultiLangData description = new MultiLangData("<br><br><br>");

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
		names.put(str);
		anim = ac;
		du = cu;
		cu.pack = this;
		orbs = new Orb(-1);
	}

	//Used for BC units
	protected Form(Unit u, int f, String str, String data) {
		unit = u;
		uid = u.id;
		fid = f;
		String nam = trio(uid.id) + "_" + SUFX[fid];
		anim = new AnimUD(str, nam, "edi" + nam + ".png", "uni" + nam + "00.png");
		anim.getUni().setCut(CommonStatic.getBCAssets().unicut);
		String[] strs = data.split("//")[0].trim().split(",");
		du = new DataUnit(this, unit, strs);
		MaModel model = anim.loader.getMM();
		((DataUnit) du).limit = CommonStatic.dataFormMinPos(model);
	}
	//Used for BC eggs
	protected Form(Unit u, int f, int m, String str, String data) {
		unit = u;
		uid = u.id;
		fid = f;
		String nam = trio(m) + "_m";
		anim = new AnimUD(str, nam, "edi" + nam + duo(fid) + ".png", "uni" + nam + duo(fid) + ".png");
		anim.getUni().setCut(CommonStatic.getBCAssets().unicut);
		String[] strs = data.split("//")[0].trim().split(",");
		du = new DataUnit(this, unit, strs);
		MaModel model = anim.loader.getMM();
		((DataUnit) du).limit = CommonStatic.dataFormMinPos(model);
	}

	@Override
	public Form copy(Unit b) {
		CustomUnit cu = new CustomUnit();
		cu.importData(du);
		return new Form(b, fid, names.toString(), anim, cu);
	}

	public int getDefaultPrice(int sta) {
		MaskUnit upc = maxu();
		return (int) (upc.getPrice() * (1 + sta * 0.5));
	}

	@Override
	public EAnimU getEAnim(AnimU.UType t) {
		return anim.getEAnim(t);
	}

	public MaskUnit maxu() {
		PCoin pc = du.getPCoin();
		if (pc != null) {
			return pc.full;
		}
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

				if (UserProfile.isOlderPack(pack, "0.5.1.0")) {
					form.type = Data.reorderTrait(form.type);
				}

				if (UserProfile.isOlderPack(pack, "0.5.2.0") && form.tba != 0) {
					form.tba += form.getPost() + 1;
				}

				if (UserProfile.isOlderPack(pack, "0.6.0.0")) {
					MaModel model = anim.loader.getMM();
					form.limit = CommonStatic.customFormMinPos(model);
					form.getProc().BARRIER.health = form.shield;
					form.traits = Trait.convertType(form.type);
					Proc proc = form.getProc();
					if ((form.abi & (1 << 18)) != 0) //Seal Immunity
						proc.IMUSEAL.mult = 100;
					if ((form.abi & (1 << 7)) != 0) //Moving atk Immunity
						proc.IMUMOVING.mult = 100;
					if ((form.abi & (1 << 12)) != 0) //Poison Immunity
						proc.IMUPOI.mult = 100;
					form.abi = Data.reorderAbi(form.abi, 0);
				}

				if (UserProfile.isOlderPack(pack, "0.6.1.0")) {
					form.getProc().DMGCUT.reduction = 100;
					form.getProc().POISON.type.ignoreMetal = true;
				}

				if (UserProfile.isOlderPack(pack, "0.6.4.0")) {
					names.put(name);
					description.put(explanation);
				}

				if (UserProfile.isOlderPack(pack, "0.6.5.0")) {
					Proc proc = form.getProc();

					if ((form.abi & 16) > 0) //2x money
						proc.BOUNTY.mult = 100;
					if ((form.abi & 32) > 0) //base destroyer
						proc.ATKBASE.mult = 300;
					form.abi = Data.reorderAbi(form.abi, 1);
				}
				if (UserProfile.isOlderPack(pack, "0.6.6.0")) {
					if (form.getProc().TIME.prob > 0)
						form.getProc().TIME.intensity = form.getProc().TIME.time;

					if (form.getProc().SUMMON.prob > 0) {
						form.getProc().SUMMON.max_dis = form.getProc().SUMMON.dis;
						form.getProc().SUMMON.min_layer = -1;
						form.getProc().SUMMON.max_layer = -1;
					}
				}
				if (UserProfile.isOlderPack(pack, "0.7.4.1") && form.pcoin != null) {
					form.pcoin.info.forEach(i -> i[12] = -1);
				}

				if (form.getProc().SUMMON.prob > 0 && form.getProc().SUMMON.form <= 0) {
					form.getProc().SUMMON.form = 1;
					form.getProc().SUMMON.mult = 1;
					form.getProc().SUMMON.type.fix_buff = true;
				}

				for (AtkDataModel adm : form.atks)
					adm.inject(pack);
				form.rep.inject(pack);
				if (form.rev != null)
					form.rev.inject(pack);
				if (form.res != null)
					form.res.inject(pack);
				if (form.cntr != null)
					form.cntr.inject(pack);
				if (form.bur != null)
					form.bur.inject(pack);
				if (form.resu != null)
					form.resu.inject(pack);
				if (form.revi != null)
					form.revi.inject(pack);
			}
		}
		if (form.getPCoin() != null)
			form.pcoin.update();
	}

	/**
	 * Validate level values in {@code target} {@link common.util.unit.Level}
	 * @param src {@code Level} that will be put into {@code target} {@code Level}. Can be null
	 * @param target {@code Level} that will be validated
	 * @return Validated {@code target} {@code Level} will be returned
	 */
	public Level regulateLv(@Nullable Level src, Level target) {
		if(src != null) {
			target.setLevel(Math.max(1, Math.min(src.getLv(), unit.max)));
			target.setPlusLevel(Math.max(0, Math.min(src.getPlusLv(), unit.maxp)));

			PCoin pc = du.getPCoin();

			if (pc != null) {
				int[] maxTalents = new int[pc.info.size()];

				for (int i = 0; i < pc.info.size(); i++)
					maxTalents[i] = Math.max(1, pc.info.get(i)[1]);

				int[] t = new int[maxTalents.length];

				for (int i = 0; i < Math.min(maxTalents.length, src.getTalents().length); i++) {
					t[i] = Math.min(maxTalents[i], Math.max(0, src.getTalents()[i]));
				}

				if (src.getTalents().length < target.getTalents().length) {
					for (int i = src.getTalents().length; i < Math.min(maxTalents.length, target.getTalents().length); i++) {
						t[i] = Math.min(maxTalents[i], Math.min(0, target.getTalents()[i]));
					}
				}

				target.setTalents(t);
			}
		} else {
			target.setLevel(Math.max(1, Math.min(unit.max, target.getLv())));
			target.setPlusLevel(Math.max(0, Math.min(unit.maxp, target.getPlusLv())));

			PCoin pc = du.getPCoin();

			if (pc != null) {
				int[] maxTalents = new int[pc.info.size()];
				int[] t = new int[pc.info.size()];

				for (int i = 0; i < pc.info.size(); i++)
					maxTalents[i] = Math.max(1, pc.info.get(i)[1]);

				for (int i = 0; i < Math.min(maxTalents.length, target.getTalents().length); i++) {
					t[i] = Math.min(maxTalents[i], Math.max(0, target.getTalents()[i]));
				}

				target.setTalents(t);
			}
		}

		return target;
	}

	@Override
	public String toString() {
		String base = (uid == null ? "NULL" : uid.id) + "-" + fid + " ";
		String desp = MultiLangCont.get(this);
		if (desp != null && desp.length() > 0)
			return base + desp;

		String nam = names.toString();
		if (nam.length() > 0)
			return base + nam;
		return base;
	}

	public String getExplaination() {
		String[] desp = MultiLangCont.getDesc(this);
		if (desp != null && desp[fid + 1].length() > 0)
			return desp[fid + 1];
		return description.toString();
	}

	public boolean hasEvolveCost() {
		return unit.info.hasEvolveCost() && fid == 2;
	}
}