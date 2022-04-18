package common.battle.attack;

import common.battle.data.DataEnemy;
import common.battle.entity.EEnemy;
import common.battle.entity.EntCont;
import common.battle.entity.Entity;
import common.pack.Identifier;
import common.util.Data.Proc.SUMMON;
import common.util.unit.AbEnemy;

public class AtkModelEnemy extends AtkModelEntity {

	private final Proc[] cursed;

	protected AtkModelEnemy(EEnemy ent, double d0) {
		super(ent, d0, 1);
		String[] arr = { "KB", "STOP", "SLOW", "WEAK", "WARP", "CURSE", "SNIPER", "SEAL", "POISON", "BOSS",
				"POIATK", "ARMOR", "SPEED", "DMGCUT", "DMGCAP" };
		cursed = new Proc[data.getAtkCount()];
		for (int i = 0; i < cursed.length; i++) {
			cursed[i] = data.getAtkModel(i).getProc().clone();
			for (String s0 : arr)
				cursed[i].get(s0).clear();
		}
	}

@Override
public void summon(SUMMON proc, Entity ent, Object acs, int resist) {
	AbEnemy ene = (AbEnemy) Identifier.get(proc.id);

	if(ene == null)
		return;
		if (resist < 100) {
			SUMMON.TYPE conf = proc.type;
			if (conf.same_health && ent.health <= 0)
				return;
			int time = proc.time;
			int allow = b.st.data.allow(b, ene);
			if (allow >= 0 || conf.ignore_limit) {
				double ep = ent.pos + getDire() * proc.dis;
				double mula = proc.mult * 0.01;
				double mult = proc.mult * 0.01;
				if (!conf.fix_buff) {
					mult *= ((EEnemy) e).mult;
					mula *= ((EEnemy) e).mula;
				}
				mula *= (100.0 - resist) / 100;
				mult *= (100.0 - resist) / 100;

				EEnemy ee = ene.getEntity(b, acs, mult, mula, 0, 9, 0);
				if (conf.random_layer)
					ee.layer = (int) (b.r.nextDouble() * 9);
				else
					ee.layer = e.layer;

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
	} else
		ent.anim.getEff(INV);
}

	@Override
	protected int getAttack(int ind, Proc proc) {
		int atk = atks[ind];
		extraAtk(ind);
		if (abis[ind] == 1)
			setProc(ind, proc);
		if (e.data instanceof DataEnemy)
			for (int j : BCShareable) proc.getArr(j).set(e.getProc().getArr(j));

		if (e.status[P_WEAK][0] > 0)
			atk = atk * e.status[P_WEAK][1] / 100;
		if (e.status[P_STRONG][0] != 0)
			atk += atk * e.status[P_STRONG][0] / 100;
		return atk;
	}

	@Override
	public Proc getProc(int ind) {
		if (e.status[P_CURSE][0] > 0 && e.status[P_SEAL][0] == 0 && ind < cursed.length)
			return cursed[ind];
		return super.getProc(ind);
	}

}
