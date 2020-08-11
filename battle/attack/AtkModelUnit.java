package common.battle.attack;

import common.battle.BasisLU;
import common.battle.entity.EUnit;
import common.battle.entity.EntCont;
import common.battle.entity.Entity;
import common.util.Data.Proc.SUMMON;
import common.util.unit.EForm;
import common.util.unit.Unit;

public class AtkModelUnit extends AtkModelEntity {

	private final BasisLU bas;
	private final Proc[] buffed;

	protected AtkModelUnit(Entity ent, double d0) {
		super(ent, d0);
		bas = ent.basis.b;
		buffed = new Proc[data.getAtkCount()];
		for (int i = 0; i < buffed.length; i++) {
			buffed[i] = data.getAtkModel(i).getProc().clone();
			buffed[i].STOP.time *= (100 + bas.getInc(C_STOP)) / 100;
			buffed[i].SLOW.time *= (100 + bas.getInc(C_SLOW)) / 100;
			buffed[i].WEAK.time *= (100 + bas.getInc(C_WEAK)) / 100;
			if (buffed[i].CRIT.prob > 0)
				buffed[i].CRIT.prob += bas.getInc(C_CRIT);
		}
	}

	@Override
	public void summon(SUMMON proc, Entity ent, Object acs) {

		Unit u = (Unit) proc.id.get();
		SUMMON.TYPE conf = proc.type;
		int time = proc.time;
		if (u != null && (b.entityCount(-1) < b.max_num || conf.ignore_limit)) {
			double up = ent.pos + getDire() * proc.dis;
			EForm ef = new EForm(u.forms[0], u.max);
			EUnit eu = ef.getEntity(b);
			if (conf.same_health)
				eu.health = e.health;
			int l0 = 0, l1 = 9;
			if (!conf.random_layer)
				l0 = l1 = e.layer;
			eu.layer = (int) (b.r.nextDouble() * (l1 - l0)) + l0;
			eu.added(-1, (int) up);
			b.tempe.add(new EntCont(eu, time));
			eu.setSummon(conf.anim_type);
		}

	}

	@Override
	protected int getAttack(int ind, Proc proc) {

		int atk = atks[ind];
		if (abis[ind] == 1) {
			setProc(ind, proc);
			proc.KB.dis = proc.KB.dis * (100 + bas.getInc(C_KB)) / 100;
		}
		extraAtk(ind);
		if (e.status[P_WEAK][0] > 0)
			atk = atk * e.status[P_WEAK][1] / 100;
		if (e.status[P_STRONG][0] != 0)
			atk += atk * (e.status[P_STRONG][0] + bas.getInc(C_STRONG)) / 100;
		return atk;
	}

	@Override
	protected int getBaseAtk(int ind) {
		return atks[ind];
	}

	@Override
	protected Proc getProc(int ind) {
		if (e.status[P_SEAL][0] > 0)
			return super.getProc(ind);
		return buffed[ind];
	}

}
