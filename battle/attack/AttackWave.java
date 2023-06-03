package common.battle.attack;

import common.battle.entity.AbEntity;
import common.battle.entity.Entity;
import common.util.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AttackWave extends AttackAb {

	protected final Set<Entity> incl;

	public AttackWave(Entity e, AttackSimple a, double p0, double wid, int wt) {
		super(e, a, p0 - wid / 2, p0 + wid / 2, false);
		waveType = wt;
		isCounter = a.isCounter;
		incl = new HashSet<>();
		if(waveType == WT_MINI)
			proc.MINIWAVE.lv--;
		else
			proc.WAVE.lv--;
	}

	public AttackWave(Entity e, AttackWave a, double p0, double wid) {
		super(e, a, p0 - wid / 2, p0 + wid / 2, false);
		waveType = a.waveType;
		isCounter = a.isCounter;
		incl = a.incl;
		if(waveType == WT_MINI)
			proc.MINIWAVE.lv--;
		else
			proc.WAVE.lv--;
	}

	public AttackWave(Entity e, AttackWave a, double pos, double start, double end) {
		super(e, a, pos - start, pos + end, false);
		waveType = a.waveType;
		isCounter = a.isCounter;
		incl = a.incl;
		if(waveType == WT_MINI)
			proc.MINIWAVE.lv--;
		else
			proc.WAVE.lv--;
	}

	@Override
	public void capture() {
		List<AbEntity> le = model.b.inRange(touch, dire, sta, end, excludeLastEdge);
		if (incl != null)
			le.removeIf(incl::contains);
		capt.clear();
		if ((abi & AB_ONLY) == 0)
			capt.addAll(le);
		else
			for (AbEntity e : le)
				if (e.ctargetable(trait, attacker, true))
					capt.add(e);
	}

	@Override
	public void excuse() {
		process();

		if(attacker != null) {
			if(attacker.status[P_WEAK][0] != 0) {
				atk = rawAtk * attacker.status[P_WEAK][1] / 100;
			}

			if(attacker.status[P_STRONG][0] != 0) {
				atk += rawAtk * attacker.status[P_STRONG][0] / 100;
			}

			if (attacker.dire == -1 && attacker.basis.canon.deco == DECO_BASE_WALL) {
				atk = (int) (atk * attacker.basis.b.t().getDecorationMagnification(attacker.basis.canon.deco, Data.DECO_WAVE));
			}
		}

		for (AbEntity e : capt) {
			if (e.isBase())
				continue;

			if (e instanceof Entity) {
				e.damaged(this);
				incl.add((Entity) e);
			}
		}
	}

}
