package common.battle.attack;

import common.battle.entity.Cannon;

public class AttackCanon extends AttackSimple {

	public AttackCanon(Cannon c, int ATK, int t, int eab, Proc pro, double p0, double p1, int duration) {
		super(null, c, ATK, t, eab, pro, p0, p1, true, null, 9, false, duration);
		canon = c.id;
		excludeLastEdge = c.id == 6;
		waveType |= WT_CANN;
		if (canon == 5)
			touch = TCH_UG | TCH_N;
	}

}
