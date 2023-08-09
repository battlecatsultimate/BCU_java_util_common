package common.battle.attack;

import common.battle.entity.AbEntity;
import common.battle.entity.Entity;
import common.util.Data;

import java.util.ArrayList;
import java.util.List;

public class AttackVolcano extends AttackAb {
	public ContVolcano handler;
	protected boolean attacked = false;
	protected final List<Entity> vcapt = new ArrayList<>();

	private byte volcTime = VOLC_ITV;

	public AttackVolcano(Entity e, AttackAb a, double sta, double end, int vt) {
		super(e, a, sta, end, false);
		isCounter = a.isCounter;
		this.sta = sta;
		this.end = end;
		this.waveType = vt;
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

		if (attacker != null) {
			if (attacker.status[P_STRONG][0] != 0)
				atk += atk * attacker.status[P_STRONG][0] / 100;
			if (attacker.status[P_WEAK][0] != 0)
				atk = atk * attacker.status[P_WEAK][1] / 100;
			if (attacker.dire == 1 && attacker.basis.canon.deco == DECO_BASE_WATER)
				atk *= attacker.basis.b.t().getDecorationMagnification(attacker.basis.canon.deco, Data.DECO_SURGE);
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
