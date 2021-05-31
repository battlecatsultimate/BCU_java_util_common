package common.battle.attack;

import common.battle.entity.Cannon;

import java.util.ArrayList;

public class AttackCanon extends AttackSimple {

	public AttackCanon(Cannon c, int ATK, ArrayList<Trait> tr, int eab, Proc pro, double p0, double p1, int duration) {
		super(c, ATK, tr, eab, pro, p0, p1, true, null, 9, false, duration);
		canon = c.id;
		waveType |= WT_CANN;
		if (canon == 5)
			touch = TCH_UG | TCH_N;
	}

}
