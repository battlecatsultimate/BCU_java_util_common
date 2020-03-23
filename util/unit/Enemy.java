package common.util.unit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import common.CommonStatic;
import common.battle.StageBasis;
import common.battle.data.CustomEnemy;
import common.battle.data.DataEnemy;
import common.battle.data.MaskEnemy;
import common.battle.entity.EEnemy;
import common.system.MultiLangCont;
import common.system.VImg;
import common.system.files.AssetData;
import common.system.files.VFile;
import common.util.Animable;
import common.util.anim.AnimU;
import common.util.anim.AnimUD;
import common.util.anim.EAnimU;
import common.util.pack.Pack;
import common.util.stage.MapColc;
import common.util.stage.Stage;
import common.util.stage.StageMap;

public class Enemy extends Animable<AnimU<?>> implements AbEnemy {

	public static void readData() throws IOException {
		VFile.get("./org/enemy/").list().forEach(p -> new Enemy(p));
		Queue<String> qs = VFile.readLine("./org/data/t_unit.csv");
		qs.poll();
		qs.poll();
		for (Enemy e : Pack.def.es.getList())
			((DataEnemy) e.de).fillData(qs.poll().split("//")[0].trim().split(","));
		qs = VFile.readLine("./org/data/enemy_dictionary_list.csv");
		for (String str : qs)
			Pack.def.es.get(Integer.parseInt(str.split(",")[0])).inDic = true;
	}

	public final int id;
	public final MaskEnemy de;
	public final Pack pac;
	public String name = "";
	public boolean inDic = false;

	public Enemy(int hash, AnimU<?> ac, CustomEnemy ce) {
		id = hash;
		de = ce;
		ce.pack = this;
		anim = ac;
		pac = Pack.map.get(hash / 1000);
	}

	public Enemy(int ID, Enemy old, Pack np) {
		id = ID;
		pac = np;
		de = ((CustomEnemy) old.de).copy(this);
		name = old.name;
		anim = old.anim;
	}

	public Enemy(VFile<AssetData> f) {
		id = CommonStatic.parseIntN(f.getName());
		Pack.def.es.add(this);
		pac = Pack.def;
		String str = "./org/enemy/" + trio(id) + "/";
		de = new DataEnemy(this);
		anim = new AnimUD(str, trio(id) + "_e", "edi_" + trio(id) + ".png", null);
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
		for (MapColc mc : MapColc.MAPS.values()) {
			if (mc.pack != Pack.def)
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
	public EEnemy getEntity(StageBasis b, Object obj, double mul, int d0, int d1, int m) {
		mul *= de.multi(b.b);
		return new EEnemy(b, de, getEAnim(0), mul, d0, d1, m);
	}

	@Override
	public VImg getIcon() {
		return anim.getEdi();
	}

	@Override
	public int getID() {
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
			return trio(id % 1000) + " - " + desp;
		if (name.length() == 0)
			return trio(id % 1000);
		return trio(id % 1000) + " - " + name;
	}

}
