package common.battle.attack;

import common.battle.entity.AbEntity;
import common.battle.entity.Entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AttackWave extends AttackAb {

	protected final Set<Entity> incl;

	public AttackWave(AttackSimple a, double p0, double wid, int wt) {
		super(a, p0 - wid / 2, p0 + wid / 2);
		waveType = wt;
		incl = new HashSet<>();
		if(waveType == WT_MINI)
			proc.MINIWAVE.lv--;
		else
			proc.WAVE.lv--;
	}

	public AttackWave(AttackWave a, double p0, double wid) {
		super(a, p0 - wid / 2, p0 + wid / 2);
		waveType = a.waveType;
		incl = a.incl;
		if(waveType == WT_MINI)
			proc.MINIWAVE.lv--;
		else
			proc.WAVE.lv--;
	}

	@Override
	public void capture() {
		List<AbEntity> le = model.b.inRange(touch, dire, sta, end);
		if (incl != null)
			le.removeIf(e -> incl.contains(e));
		capt.clear();
		if ((abi & AB_ONLY) == 0)
			capt.addAll(le);
		else
			for (AbEntity e : le)
				if (e.targetable(type))
					capt.add(e);
	}

	@Override
	public void excuse() {
		process();
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
