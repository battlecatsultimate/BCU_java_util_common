package common.battle.entity;

import common.battle.attack.AttackAb;
import common.util.BattleObj;

public abstract class AbEntity extends BattleObj {

	public long health, maxH;
	public int dire;
	public double pos;

	protected AbEntity(int h) {
		if (h <= 0)
			h = 1;
		health = maxH = h;
	}

	public void added(int d, double p) {
		pos = p;
		dire = d;
	}

	public abstract void damaged(AttackAb atk);

	public abstract int getAbi();

	public abstract boolean isBase();

	public abstract void postUpdate();

	public abstract boolean targetable(int type);

	public abstract int touchable();

	public abstract void update();

	public abstract void preUpdate();
}
