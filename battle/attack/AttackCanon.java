package common.battle.attack;

import common.battle.entity.Cannon;
import common.util.unit.Trait;

import java.util.ArrayList;

public class AttackCanon extends AttackSimple {

	public AttackCanon(Cannon c, int ATK, ArrayList<Trait> tr, int eab, Proc pro, double p0, double p1, int duration) {
		super(null, c, ATK, tr, eab, pro, p0, p1, true, null, 9, false, duration);
		canon = c.id > 2 ? 1 << (c.id - 1) : 1 << c.id;
		excludeLastEdge = c.id == 6;
		waveType |= WT_CANN;
		if (canon == 16)
			touch = TCH_UG | TCH_N;
		if (canon == 16 || canon == 32)
			touch |= TCH_CORPSE;
	}

}
