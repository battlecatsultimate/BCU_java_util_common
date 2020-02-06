package common.battle.attack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.battle.entity.AbEntity;
import common.battle.entity.Entity;

public class AttackVolcano extends AttackAb {
	protected final HashMap<Entity,Integer> vcapt;
	
	protected boolean attacked = false;

	public AttackVolcano(AttackSimple a, double p0, double wid, double sta, double end) {
		super(a, p0, wid);
		vcapt = new HashMap<>();
		proc[P_VOLC][0] = 0;
		this.sta = sta;
		this.end = end;
	}
	
	public AttackVolcano(AttackVolcano a, double p0, double wid, double sta, double end) {
		super(a, p0, wid);
		vcapt = a.vcapt;
		proc[P_VOLC][0] = 0;
	}

	@Override
	public void capture() {
		List<AbEntity> le = model.b.inRange(touch, dire, sta, end);

		capt.clear();
		if((abi & AB_ONLY) == 0)
			capt.addAll(le);
		else
			for(AbEntity e : le)
				if(e.targetable(type))
					capt.add(e);
	}

	@Override
	public void excuse() {
		process();
		for (AbEntity e : capt) {
			if(e.isBase())
				continue;
			
			if(e instanceof Entity) {
				if(vcapt.containsKey(e)) {					
					if(vcapt.get(e) <= 0) {
						e.damaged(this);
						attacked = true;
						vcapt.put((Entity)e,30);
					} else
						vcapt.put((Entity)e, vcapt.get(e)-1);
				} else {
					e.damaged(this);
					attacked = true;
					vcapt.put((Entity)e, 30);
				}
			}
		}
	}
}
