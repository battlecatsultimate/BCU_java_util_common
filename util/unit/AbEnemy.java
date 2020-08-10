package common.util.unit;

import java.util.Set;

import common.battle.StageBasis;
import common.battle.entity.EEnemy;
import common.pack.PackData.Identifier;
import common.pack.PackData.Indexable;
import common.system.VImg;

public interface AbEnemy extends Comparable<AbEnemy>, Indexable {

	@Override
	public default int compareTo(AbEnemy e) {
		return getID().compareTo(e.getID());
	}

	public EEnemy getEntity(StageBasis sb, Object obj, double mul, double mul1, int d0, int d1, int m);

	public VImg getIcon();

	@Override
	public Identifier getID();

	public Set<Enemy> getPossible();

}
