package common.battle.attack;

import common.battle.BasisLU;
import common.battle.data.PCoin;
import common.battle.entity.EUnit;
import common.battle.entity.EntCont;
import common.battle.entity.Entity;
import common.pack.Identifier;
import common.util.Data.Proc.SUMMON;
import common.util.unit.EForm;
import common.util.unit.Level;
import common.util.unit.Unit;
import org.jcodec.common.tools.MathUtil;

public class AtkModelUnit extends AtkModelEntity {

	private final BasisLU bas;
	private final Proc[] buffed;

	protected AtkModelUnit(Entity ent, double d0, double d1, PCoin pcoin, Level lv) {
		super(ent, d0, d1, pcoin, lv);
		bas = ent.basis.b;
		buffed = new Proc[data.getAtkCount()];
		for (int i = 0; i < buffed.length; i++) {
			if(data.getAtkModel(i).getProc() == null)
				buffed[i] = Proc.blank();
			else
				buffed[i] = data.getAtkModel(i).getProc().clone();
			buffed[i].STOP.time = (buffed[i].STOP.time * (100 + bas.getInc(C_STOP))) / 100;
			buffed[i].SLOW.time = (buffed[i].SLOW.time * (100 + bas.getInc(C_SLOW))) / 100;
			buffed[i].WEAK.time = (buffed[i].WEAK.time * (100 + bas.getInc(C_WEAK))) / 100;
			if (buffed[i].CRIT.prob > 0)
				buffed[i].CRIT.prob += bas.getInc(C_CRIT);
		}
	}

	@Override
	public void summon(SUMMON proc, Entity ent, Object acs, int resist) {
		if (resist < 100) {
			Unit u = Identifier.getOr(proc.id, Unit.class);
			SUMMON.TYPE conf = proc.type;
			if (conf.same_health && ent.health <= 0)
				return;
			int time = proc.time;
			if (b.entityCount(-1) < b.max_num || conf.ignore_limit) {
				int[] lvl = {proc.mult + ((EUnit)e).lvl,0,0,0,0,0};
				if (conf.fix_buff)
					lvl[0] = proc.mult;
				lvl[0] = MathUtil.clip(lvl[0], 1, u.max + u.maxp);
				lvl[0] *= (100.0 - resist)/100;
				double up = ent.pos + getDire() * proc.dis;
				EForm ef = new EForm(u.forms[proc.form - 1], lvl);
				EUnit eu = ef.invokeEntity(b, lvl[0]);
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
		} else
			ent.anim.getEff(INV);
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
	protected Proc getProc(int ind) {
		if (e.status[P_SEAL][0] > 0 || ind >= buffed.length)
			return super.getProc(ind);
		return buffed[ind];
	}

}
