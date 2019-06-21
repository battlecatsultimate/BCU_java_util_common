package common.util.entity.data;

import java.util.Set;
import java.util.TreeSet;

import common.util.basis.Basis;
import common.util.unit.AbEnemy;
import common.util.unit.Enemy;

public interface MaskEnemy extends MaskEntity {

	public double getDrop();

	@Override
	public Enemy getPack();

	public int getStar();

	public default Set<AbEnemy> getSummon() {
		return new TreeSet<AbEnemy>();
	}

	public double multi(Basis b);

}
