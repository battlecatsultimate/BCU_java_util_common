package common.util.unit;

import common.CommonStatic;
import common.battle.StageBasis;
import common.battle.data.AtkDataModel;
import common.battle.data.CustomEnemy;
import common.battle.data.DataEnemy;
import common.battle.data.MaskEnemy;
import common.battle.entity.EEnemy;
import common.io.json.JsonClass;
import common.io.json.JsonDecoder.OnInjected;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.pack.PackData;
import common.pack.UserProfile;
import common.system.VImg;
import common.system.files.VFile;
import common.util.Animable;
import common.util.Data;
import common.util.anim.AnimU;
import common.util.anim.AnimU.UType;
import common.util.anim.AnimUD;
import common.util.anim.EAnimU;
import common.util.anim.MaModel;
import common.util.lang.MultiLangCont;
import common.util.lang.MultiLangData;
import common.util.stage.MapColc;
import common.util.stage.MapColc.PackMapColc;
import common.util.stage.Stage;
import common.util.stage.StageMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@JsonClass.JCGeneric(Identifier.class)
@JsonClass
public class Enemy extends Animable<AnimU<?>, UType> implements AbEnemy {

	@JsonClass.JCIdentifier
	@JsonField
	public final Identifier<AbEnemy> id;
	@JsonField
	public final MaskEnemy de;

	@JsonField(io = JsonField.IOType.R)
	public String name = "";
	@JsonField(generic = MultiLangData.class)
	public MultiLangData names = new MultiLangData();

	@JsonField(io = JsonField.IOType.R)
	public String desc = "<br><br><br>";
	@JsonField(generic = MultiLangData.class)
	public MultiLangData description = new MultiLangData("<br><br><br>");

	public boolean inDic = false;

	@JsonClass.JCConstructor
	public Enemy() {
		id = null;
		de = null;
	}

	public Enemy(Identifier<AbEnemy> hash, AnimU<?> ac, CustomEnemy ce) {
		id = hash;
		de = ce;
		ce.pack = this;
		anim = ac;
	}

	public Enemy(VFile f) {
		id = new Identifier<>(Identifier.DEF, Enemy.class, CommonStatic.parseIntN(f.getName()));
		String str = "./org/enemy/" + Data.trio(id.id) + "/";
		de = new DataEnemy(this);
		anim = new AnimUD(str, Data.trio(id.id) + "_e", "edi_" + Data.trio(id.id) + ".png", null);
		anim.getEdi().check();
		MaModel model = anim.loader.getMM();
		((DataEnemy) de).limit = CommonStatic.dataEnemyMinPos(model);
	}

	public List<Stage> findApp() {
		List<Stage> ans = new ArrayList<>();
		for (Stage st : MapColc.getAllStage()) {
			if (st != null && st.contains(this))
				ans.add(st);
		}
		return ans;
	}

	public List<Stage> findApp(MapColc mc) {
		List<Stage> ans = new ArrayList<>();
		for (StageMap sm : mc.maps)
			for (Stage st : sm.list)
				if (st.contains(this))
					ans.add(st);
		return ans;
	}

	public List<MapColc> findMap() {
		List<MapColc> ans = new ArrayList<>();
		for (MapColc mc : MapColc.values()) {
			if (mc instanceof PackMapColc)
				continue;
			boolean col = false;
			for (StageMap sm : mc.maps) {
				for (Stage st : sm.list)
					if (col = st.contains(this)) {
						ans.add(mc);
						break;
					}
				if (col)
					break;
			}
		}
		return ans;
	}

	@Override
	public EAnimU getEAnim(UType t) {
		if (anim == null)
			return null;
		return anim.getEAnim(t);
	}

	@Override
	public EEnemy getEntity(StageBasis b, Object obj, double hpMagnif, double atkMagnif, int d0, int d1, int m) {
		hpMagnif *= de.multi(b.b);
		atkMagnif *= de.multi(b.b);
		EAnimU walkAnim = getEAnim(UType.WALK);
		walkAnim.setTime(0);
		return new EEnemy(b, de, walkAnim, hpMagnif, atkMagnif, d0, d1, m);
	}

	@Override
	public VImg getIcon() {
		if(anim == null)
			return null;

		return anim.getEdi();
	}

	@Override
	public Identifier<AbEnemy> getID() {
		return id;
	}

	@Override
	public Set<Enemy> getPossible() {
		Set<Enemy> te = new TreeSet<>();
		te.add(this);
		return te;
	}

	@OnInjected
	public void onInjected() {
		if(getCont() instanceof PackData.UserPack) {
			CustomEnemy enemy = (CustomEnemy) de;
			enemy.pack = this;
			Proc proc = enemy.getProc();
			PackData.UserPack pack = (PackData.UserPack) getCont();

			if (UserProfile.isOlderPack(pack, "0.5.1.0")) {
				enemy.type = Data.reorderTrait(enemy.type);
			}

			if (UserProfile.isOlderPack(pack, "0.5.2.0") && enemy.tba != 0) {
				enemy.tba += enemy.getPost() + 1;
			}

			if (UserProfile.isOlderPack(pack, "0.5.4.0")) {
				MaModel model = anim.loader.getMM();
				enemy.limit = CommonStatic.customEnemyMinPos(model);
			}

			if (UserProfile.isOlderPack(pack, "0.6.0.0")) {
				proc.BARRIER.health = enemy.shield;
				enemy.traits = Trait.convertType(enemy.type);
				if ((enemy.abi & (1 << 18)) != 0) //Seal Immunity
					proc.IMUSEAL.mult = 100;
				if ((enemy.abi & (1 << 7)) != 0) //Moving atk Immunity
					proc.IMUMOVING.mult = 100;
				if ((enemy.abi & (1 << 12)) != 0) //Poison Immunity
					proc.IMUPOI.mult = 100;
				enemy.abi = Data.reorderAbi(enemy.abi, 0);
			}

			if (UserProfile.isOlderPack(pack, "0.6.1.0")) {
				proc.DMGCUT.reduction = 100;
				proc.DMGCUT.type.traitIgnore = true;
				proc.DMGCAP.type.traitIgnore = true;
				proc.POISON.type.ignoreMetal = true;
			}

			if (UserProfile.isOlderPack(pack, "0.6.4.0")) {
				names.put(name);
				description.put(desc);
			}

			if (UserProfile.isOlderPack(pack, "0.6.5.0")) {
				if ((enemy.abi & 32) > 0) //base destroyer
					proc.ATKBASE.mult = 300;
				enemy.abi = Data.reorderAbi(enemy.abi, 1);
			}

			if (UserProfile.isOlderPack(pack, "0.6.6.0")) {
				if (proc.TIME.prob > 0)
					proc.TIME.intensity = proc.TIME.time;

				if (proc.SUMMON.prob > 0) {
					proc.SUMMON.max_dis = proc.SUMMON.dis;
					proc.SUMMON.min_layer = -1;
					proc.SUMMON.max_layer = -1;
				}
			}

			if (proc.SUMMON.prob > 0 && (proc.SUMMON.id == null || !AbEnemy.class.isAssignableFrom(proc.SUMMON.id.cls)))
				proc.SUMMON.form = 1;

			for (AtkDataModel adm : enemy.atks)
				adm.inject(pack);
			enemy.rep.inject(pack);
			if (enemy.rev != null)
				enemy.rev.inject(pack);
			if (enemy.res != null)
				enemy.res.inject(pack);
			if (enemy.cntr != null)
				enemy.cntr.inject(pack);
			if (enemy.bur != null)
				enemy.bur.inject(pack);
			if (enemy.resu != null)
				enemy.resu.inject(pack);
			if (enemy.revi != null)
				enemy.revi.inject(pack);
		}
	}

	@Override
	public String toString() {
		String desp = MultiLangCont.get(this);
		if (desp != null && desp.length() > 0)
			return Data.trio(id.id) + " - " + desp;

		String nam = names.toString();
		if (nam.length() == 0)
			return Data.trio(id.id);
		return Data.trio(id.id) + " - " + nam;
	}

	public String getExplaination() {
		String[] desp = MultiLangCont.getDesc(this);
		if (desp != null && desp[1].length() > 0)
			return desp[1];
		return description.toString();
	}
}