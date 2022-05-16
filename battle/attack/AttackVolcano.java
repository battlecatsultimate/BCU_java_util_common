package common.battle.attack;

import common.battle.entity.AbEntity;
import common.battle.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class AttackVolcano extends AttackAb {

	protected boolean attacked = false;
	private byte volcTime = VOLC_ITV;

	protected final List<Entity> vcapt = new ArrayList<>();
	private final List<Entity> temp = new ArrayList<>();

	public AttackVolcano(Entity e, AttackSimple a, double sta, double end) {
		super(e, a, sta, end, false);
		isCounter = a.isCounter;
		this.sta = sta;
		this.end = end;
		this.waveType = WT_VOLC;
	}

	@Override
	public void capture() {
		List<AbEntity> le = model.b.inRange(touch, dire, sta, end, excludeLastEdge);

		capt.clear();

		for (AbEntity e : le)
			if (e instanceof Entity) {
				if(!vcapt.contains(e)) {
					temp.add((Entity) e);
				}

				capt.add(e);
			}
	}

	@Override
	public void excuse() {
		process();

		if(!temp.isEmpty()) {
			for(Entity e : temp) {
				e.damaged(this);
				attacked = true;
			}

			vcapt.addAll(temp);

			temp.clear();
		}

		if (volcTime == 0) {
			volcTime = VOLC_ITV;
		} else {
			volcTime--;
			return;
		}

		for (AbEntity e : capt) {
			if (e.isBase() && !(e instanceof Entity))
				continue;

			if (e instanceof Entity) {
				e.damaged(this);
				attacked = true;
			}
		}
	}
}
