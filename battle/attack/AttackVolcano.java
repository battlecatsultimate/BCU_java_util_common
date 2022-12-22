package common.battle.attack;

import common.battle.entity.AbEntity;
import common.battle.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class AttackVolcano extends AttackAb {

	protected boolean attacked = false;
	private byte volcTime = VOLC_ITV;

	protected final List<Entity> vcapt = new ArrayList<>();

	public AttackVolcano(Entity e, AttackSimple a, double sta, double end) {
		super(e, a, sta, end, false);
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
			if (e instanceof Entity && !vcapt.contains((Entity) e)) {
				capt.add(e);
			}
	}

	@Override
	public void excuse() {
		process();

		if (volcTime == 0) {
			volcTime = VOLC_ITV;
			vcapt.clear();
		} else {
			volcTime--;
		}

		if(attacker != null) {
			if(attacker.status[P_WEAK][0] != 0) {
				atk = rawAtk * attacker.status[P_WEAK][1] / 100;
			}

			if(attacker.status[P_STRONG][0] != 0) {
				atk += rawAtk * attacker.status[P_STRONG][0] / 100;
			}
		}

		for (AbEntity e : capt) {
			if (e.isBase() && !(e instanceof Entity))
				continue;

			if (e instanceof Entity) {
				e.damaged(this);
				attacked = true;

				vcapt.add((Entity) e);
			}
		}
	}
}
