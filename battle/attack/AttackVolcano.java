package common.battle.attack;

import common.battle.entity.AbEntity;
import common.battle.entity.Entity;

import java.util.HashMap;
import java.util.List;

public class AttackVolcano extends AttackAb {

	protected boolean attacked = false;
	protected int volcTime = VOLC_ITV;

	protected final HashMap<Entity, Integer> vcapt;

	public AttackVolcano(Entity e, AttackSimple a, double sta, double end) {
		super(e, a, sta, end, false);
		vcapt = new HashMap<>();
		isCounter = a.isCounter;
		this.sta = sta;
		this.end = end;
		this.waveType = WT_VOLC;
	}

	@Override
	public void capture() {

		List<AbEntity> le = model.b.inRange(touch, dire, sta, end, excludeLastEdge);

		capt.clear();
		for (AbEntity e : le)
			if (e instanceof Entity && !vcapt.containsKey(e))
				capt.add(e);
	}

	@Override
	public void excuse() {
		process();
		if (volcTime == 0) {
			vcapt.clear();
			volcTime = VOLC_ITV;
		} else
			volcTime--;

		for (AbEntity e : capt) {
			if (e.isBase() && !(e instanceof Entity))
				continue;
			if (e instanceof Entity) {
				e.damaged(this);
				vcapt.put((Entity) e, VOLC_ITV);
				attacked = true;
			}
		}
	}
}
