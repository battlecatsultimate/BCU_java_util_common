package common.util.entity.attack;

import common.util.BattleObj;
import common.util.basis.StageBasis;
import common.util.entity.Entity;

public abstract class AtkModelAb extends BattleObj {

	public final StageBasis b;

	public AtkModelAb(StageBasis bas) {
		b = bas;
	}

	/** get the ability bitmask of this attack */
	public abstract int getAbi();

	/** get the direction of the entity */
	public abstract int getDire();

	/** get the position of the entity */
	public abstract double getPos();

	/** invoke when damage calculation is finished */
	public void invokeLater(AttackAb atk, Entity e) {
	}

	protected int getLayer() {
		return 10;
	}

}
