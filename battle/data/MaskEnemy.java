package common.battle.data;

import common.battle.Basis;
import common.util.unit.Enemy;

public interface MaskEnemy extends MaskEntity {

	int getDrop();

	@Override
	Enemy getPack();

	int getStar();

	float multi(Basis b);

	float getLimit();
}
