package common.battle.attack;

import common.battle.data.MaskEntity;
import common.battle.entity.AbEntity;
import common.battle.entity.Entity;

import java.util.HashMap;
import java.util.List;

public class AttackVolcano extends AttackAb {

	private final MaskEntity ent;
	protected boolean attacked = false;

	protected final HashMap<Entity, Integer> vcapt;

	public AttackVolcano(AttackSimple a, MaskEntity entdata, double sta, double end) {
		super(a, sta, end, false);
		vcapt = new HashMap<>();
		this.sta = sta;
		this.end = end;
		this.waveType = WT_VOLC;
		this.ent = entdata;
	}

	@Override
	public void capture() {

		List<AbEntity> le = model.b.inRange(touch, dire, sta, end);

		capt.clear();
		for (AbEntity e : le)
			if (((abi & AB_ONLY) == 0 || ((e.targetable(type)) || e.ctargetable(type, ent))) && e instanceof Entity && !vcapt.containsKey(e))
				capt.add(e);
	}

	@Override
	public void excuse() {
		process();
		for (AbEntity e : capt) {
			if (e.isBase() && !(e instanceof Entity))
				continue;
			if (e instanceof Entity) {
				e.damaged(this, ent);
				vcapt.put((Entity) e, VOLC_ITV);
				attacked = true;
			}
		}

		vcapt.entrySet().removeIf(ent -> {
			int n = ent.getValue() - 1;
			if (n > 0)
				ent.setValue(n);
			return n == 0;
		});
	}
}
