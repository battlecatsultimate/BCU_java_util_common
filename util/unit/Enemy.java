package common.util.unit;

import common.CommonStatic;
import common.battle.StageBasis;
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
import common.util.lang.MultiLangCont;
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
	@JsonField
	public String name = "";
	@JsonField
	public String desc = "<br><br><br>";
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

	public Enemy(Identifier<AbEnemy> ID, Enemy old) {
		id = ID;
		de = ((CustomEnemy) old.de).copy(this);
		name = old.name;
		anim = old.anim;
	}

	public Enemy(VFile f) {
		id = new Identifier<>(Identifier.DEF, Enemy.class, CommonStatic.parseIntN(f.getName()));
		String str = "./org/enemy/" + Data.trio(id.id) + "/";
		de = new DataEnemy(this);
		anim = new AnimUD(str, Data.trio(id.id) + "_e", "edi_" + Data.trio(id.id) + ".png", null);
		anim.getEdi().check();
		anim.partial();
		((DataEnemy) de).limit = (int) (Math.abs(anim.mamodel.confs[0][2]) * Math.abs(anim.mamodel.parts[0][8]) * 3.0 / anim.mamodel.ints[0]);
		anim.unload();
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
		CustomEnemy enemy = (CustomEnemy) de;
		enemy.pack = this;

		if(getCont() instanceof PackData.UserPack) {
			PackData.UserPack pack = (PackData.UserPack) getCont();

			if (UserProfile.isOlderPack(pack, "0.5.1.0")) {
				enemy.type = Data.reorderTrait(enemy.type);
			}

			if (UserProfile.isOlderPack(pack, "0.5.2.0") && enemy.tba != 0) {
				enemy.tba += enemy.getPost() + 1;
			}

			if (UserProfile.isOlderPack(pack, "0.5.4.0")) {
				anim.load();
				anim.partial();
				enemy.limit = (int) (Math.abs(anim.mamodel.parts[0][6]) * 3.0 * Math.abs(anim.mamodel.parts[0][8]) / 1000.0);
				anim.unload();
			}

			if (UserProfile.isOlderPack(pack, "0.5.6.3")) {
				enemy.getProc().BARRIER.health = enemy.shield;
				enemy.traits = Trait.convertType(enemy.type);
				Proc proc = enemy.getProc();
				if ((enemy.abi & (1 << 18)) != 0) //Seal Immunity
					proc.IMUSEAL.mult = 100;
				if ((enemy.abi & (1 << 7)) != 0) //Moving atk Immunity
					proc.IMUMOVING.mult = 100;
				if ((enemy.abi & (1 << 12)) != 0) //Poison Immunity
					proc.IMUPOI.mult = 100;
				enemy.abi = Data.reorderAbi(enemy.abi);
			}

		}
	}

	@Override
	public String toString() {
		String desp = MultiLangCont.get(this);
		if (desp != null && desp.length() > 0)
			return Data.trio(id.id) + " - " + desp;
		if (name.length() == 0)
			return Data.trio(id.id);
		return Data.trio(id.id) + " - " + name;
	}

	public String descriptionGet() {
		String[] desp = MultiLangCont.getDesc(this);
		if (desp != null && desp[1].length() > 0)
			return desp[1];
		if (desc.length() == 0)
			return "";
		return desc;
	}
}