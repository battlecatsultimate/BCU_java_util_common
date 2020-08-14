package common.util.unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import common.CommonStatic;
import common.battle.StageBasis;
import common.battle.data.CustomEnemy;
import common.battle.data.DataEnemy;
import common.battle.data.MaskEnemy;
import common.battle.entity.EEnemy;
import common.pack.PackData.Identifier;
import common.system.VImg;
import common.system.files.VFile;
import common.util.Animable;
import common.util.Data;
import common.util.anim.AnimU;
import common.util.anim.AnimUD;
import common.util.anim.EAnimU;
import common.util.lang.MultiLangCont;
import common.util.stage.MapColc;
import common.util.stage.MapColc.PackMapColc;
import common.util.stage.Stage;
import common.util.stage.StageMap;

public class Enemy extends Animable<AnimU<?>> implements AbEnemy {

	public final Identifier<AbEnemy> id;
	public final MaskEnemy de;
	public String name = "";
	public boolean inDic = false;

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

	public Enemy(VFile<?> f) {
		id = new Identifier<>(Identifier.DEF, Enemy.class, CommonStatic.parseIntN(f.getName()));
		String str = "./org/enemy/" + Data.trio(id.id) + "/";
		de = new DataEnemy(this);
		anim = new AnimUD(str, Data.trio(id.id) + "_e", "edi_" + Data.trio(id.id) + ".png", null);
		anim.getEdi().check();

	}

	public List<Stage> findApp() {
		List<Stage> ans = new ArrayList<>();
		for (Stage st : MapColc.getAllStage())
			if (st.contains(this))
				ans.add(st);
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
	public EAnimU getEAnim(int t) {
		if (anim == null)
			return null;
		return anim.getEAnim(t);
	}

	@Override
	public EEnemy getEntity(StageBasis b, Object obj, double mul, double mul2, int d0, int d1, int m) {
		mul *= de.multi(b.b);
		mul2 *= de.multi(b.b);
		return new EEnemy(b, de, getEAnim(0), mul, mul2, d0, d1, m);
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

	@Override
	public String toString() {
		String desp = MultiLangCont.get(this);
		if (desp != null && desp.length() > 0)
			return Data.trio(id.id) + " - " + desp;
		if (name.length() == 0)
			return Data.trio(id.id);
		return Data.trio(id.id) + " - " + name;
	}

}
