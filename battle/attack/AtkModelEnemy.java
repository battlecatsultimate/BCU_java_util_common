package common.battle.attack;

import common.battle.entity.EEnemy;
import common.battle.entity.EntCont;
import common.battle.entity.Entity;
import common.pack.Identifier;
import common.util.Data.Proc.SUMMON;
import common.util.unit.AbEnemy;

public class AtkModelEnemy extends AtkModelEntity {

	private final Proc[] cursed;

	protected AtkModelEnemy(EEnemy ent, double d0) {
		super(ent, d0);
		String[] arr = { "KB", "STOP", "SLOW", "WEAK", "WARP", "CURSE", "SNIPER", "SEAL", "POISON", "BOSS", "IMUATK",
				"POIATK" };
		cursed = new Proc[data.getAtkCount()];
		for (int i = 0; i < cursed.length; i++) {
			cursed[i] = data.getAtkModel(i).getProc().clone();
			for (String s0 : arr)
				cursed[i].get(s0).clear();
		}
	}

	@Override
	public void summon(SUMMON proc, Entity ent, Object acs) {
		AbEnemy ene = (AbEnemy) Identifier.get(proc.id);
		SUMMON.TYPE conf = proc.type;
		if (conf.same_health && ent.health <= 0)
			return;
		int time = proc.time;
		int allow = b.st.data.allow(b, ene);
		if (ene != null && (allow >= 0 || conf.ignore_limit)) {
			double ep = ent.pos + getDire() * proc.dis;
			double mula = proc.mult * 0.01;
			double mult = proc.mult * 0.01;
			if (!conf.fix_buff) {
				mult *= ((EEnemy) e).mult;
				mula *= ((EEnemy) e).mula;
			}
			int l0 = 0, l1 = 9;
			if (!conf.random_layer)
				l0 = l1 = e.layer;
			EEnemy ee = ene.getEntity(b, acs, mult, mula, 0, l0, l1);
			ee.layer = (int) (b.r.nextDouble() * (l1-l0)) + l0;
			ee.group = allow;
			if (ep < ee.data.getWidth())
				ep = ee.data.getWidth();
			if (ep > b.st.len - 800)
				ep = b.st.len - 800;
			ee.added(1, (int) ep);
			b.tempe.add(new EntCont(ee, time));
			if (conf.same_health)
				ee.health = e.health;
			ee.setSummon(conf.anim_type);
		}

	}

	@Override
	protected int getAttack(int ind, Proc proc) {
		int atk = atks[ind];
		extraAtk(ind);
		if (abis[ind] == 1)
			setProc(ind, proc);
		if (e.status[P_WEAK][0] > 0)
			atk = atk * e.status[P_WEAK][1] / 100;
		if (e.status[P_STRONG][0] != 0)
			atk += atk * e.status[P_STRONG][0] / 100;
		return atk;
	}

	@Override
	protected int getBaseAtk(int ind) {
		return atks[ind];
	}

	@Override
	protected Proc getProc(int ind) {
		if (e.status[P_CURSE][0] > 0 && e.status[P_SEAL][0] == 0 && ind < cursed.length)
			return cursed[ind];
		return super.getProc(ind);
	}

}
