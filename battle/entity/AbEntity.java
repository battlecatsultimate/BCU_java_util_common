package common.battle.entity;

import common.battle.attack.AttackAb;
import common.util.BattleObj;
import common.util.unit.Trait;

import java.util.ArrayList;

public abstract class AbEntity extends BattleObj {

	/**
	 * health = Unit's current health.
	 * maxH = Unit's maximum HP. Used to limit healing and any effects that require % of Entity's HP.
	 */
	public long health, maxH;
	/**
	 * Direction/Faction of entity. -1 is Cat unit, 1 is Enemy Unit
	 */
	public int dire;
	/**
	 * Current Position of this Entity
	 */
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

	public abstract boolean targetable(Entity ent);

	public abstract boolean ctargetable(ArrayList<Trait> t, Entity attacker);

	public abstract int touchable();

	public abstract void preUpdate();

	public abstract void update();
}
