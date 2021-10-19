package common.battle.data;

import common.battle.Basis;
import common.util.unit.AbEnemy;
import common.util.unit.Enemy;

import java.util.Set;
import java.util.TreeSet;

public interface MaskEnemy extends MaskEntity {

	int getDrop();

	@Override
	Enemy getPack();

	int getStar();

	default Set<AbEnemy> getSummon() {
		return new TreeSet<AbEnemy>();
	}

	double multi(Basis b);

	double getLimit();
}
