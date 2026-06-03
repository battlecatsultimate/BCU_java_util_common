package common.battle.attack;

import common.CommonStatic;
import common.battle.data.DataEnemy;
import common.battle.entity.EEnemy;
import common.battle.entity.EUnit;
import common.battle.entity.EntCont;
import common.battle.entity.Entity;
import common.pack.Identifier;
import common.util.Data.Proc.SUMMON;
import common.util.unit.AbEnemy;
import common.util.unit.EForm;
import common.util.unit.Unit;
import org.jcodec.common.tools.MathUtil;

public class AtkModelEnemy extends AtkModelEntity {

	private final Proc[] cursed;

	protected AtkModelEnemy(EEnemy ent, float d0) {
		super(ent, d0, 1);
		String[] arr = { "KB", "STOP", "SLOW", "WEAK", "WARP", "CURSE", "SNIPER", "SEAL", "POISON", "BOSS",
				"POIATK", "ARMOR", "SPEED", "LETHARGY", "DMGCUT", "DMGCAP", "DELAY" };
		cursed = new Proc[data.getAtkCount()];
		for (int i = 0; i < cursed.length; i++) {
			cursed[i] = data.getAtkModel(i).getProc().clone();
			for (String s0 : arr)
				cursed[i].get(s0).clear();
		}
	}

	@Override
	public void summon(SUMMON proc, Entity ent, Object acs, int resist) {
		if (resist < 100) {
			if (proc.id == null || AbEnemy.class.isAssignableFrom(proc.id.cls)) {
				AbEnemy ene = Identifier.getOr(proc.id, AbEnemy.class);
				SUMMON.TYPE conf = proc.type;

				if (conf.same_health && ent.health <= 0)
					return;

				int time = proc.time;
				int allow = b.st.data.allow(b, ene);

				if (allow >= 0 || conf.ignore_limit) {
					int dis = b.getValueBetween(proc.dis, proc.max_dis);
					float ep = ent.pos + getDire() * dis;
					float mula = proc.mult * 0.01f;
					float mult = proc.mult * 0.01f;

					if (!conf.fix_buff) {
						mult *= (float) ((EEnemy) e).mult;
						mula *= (float) ((EEnemy) e).mula;
					}

					mula *= (100.0f - resist) / 100;
					mult *= (100.0f - resist) / 100;

					int layer = e.spawnLayer;

					if (proc.type.layer_type == CommonStatic.LayerType.SET)
						layer = b.getValueBetween(proc.min_layer, proc.max_layer);
					else if (proc.type.layer_type == CommonStatic.LayerType.RELATIVE)
						layer += b.getValueBetween(proc.min_layer, proc.max_layer);

					EEnemy ee = ene.getEntity(b, acs, mult, mula, layer, layer, 0, -1);

					ee.group = allow;
					if (proc.tba == -1)
						ee.setWaitTime(ee.data.getTBA());
					else if (proc.tba > 0)
						ee.setWaitTime(proc.tba);

					if (ep < ee.data.getWidth())
						ep = ee.data.getWidth();
					if (ep > b.st.len - 800)
						ep = b.st.len - 800;

					ee.added(1, (int) ep);

					b.tempe.add(new EntCont(ee, time));

					if (conf.same_health)
						ee.health = e.health;

					ee.setSummon(conf.anim_type, conf.bond_hp ? e : null);
				}
			} else {
				Unit u = Identifier.getOr(proc.id, Unit.class);
				SUMMON.TYPE conf = proc.type;

				if (conf.same_health && ent.health <= 0)
					return;

				int time = proc.time;

				if (u.forms.length == 0)
					return;

				int form = Math.max(0, Math.min(proc.form - 1, u.forms.length - 1));

				if (b.entityCount(-1) < b.maxNum - u.forms[form].du.getWill() || conf.ignore_limit) {
					int lvl = proc.mult;
					lvl = MathUtil.clip(lvl, 1, u.max + u.maxp);
					lvl = (int) (lvl * (100.0 - resist) / 100);

					int dis = b.getValueBetween(proc.dis, proc.max_dis);
					float up = ent.pos + getDire() * dis;
					int layer = e.spawnLayer;

					if (proc.type.layer_type == CommonStatic.LayerType.SET)
						layer = b.getValueBetween(proc.min_layer, proc.max_layer);
					else if (proc.type.layer_type == CommonStatic.LayerType.RELATIVE)
						layer += b.getValueBetween(proc.min_layer, proc.max_layer);

					EForm ef = new EForm(u.forms[form], lvl);
					EUnit eu = ef.invokeEntity(b, lvl, layer, layer);
					if (conf.same_health)
						eu.health = e.health;

					eu.added(-1, (int) up);
					b.tempe.add(new EntCont(eu, time));
					eu.setSummon(conf.anim_type, conf.bond_hp ? e : null);
				}
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

		return atk;
	}

	@Override
	public Proc getProc(int ind) {
		if (e.status[P_CURSE][0] > 0 && e.status[P_SEAL][0] == 0 && ind < cursed.length)
			return cursed[ind];
		return super.getProc(ind);
	}

}
