package common.battle.attack;

import common.battle.StageBasis;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.util.BattleObj;

import java.util.Comparator;

public abstract class ContAb extends BattleObj {

	protected final StageBasis sb;

	public double pos;
	public boolean activate = true;
	public int layer;

	protected ContAb(StageBasis b, double p, int lay) {
		sb = b;
		pos = p;
		layer = lay;
		sb.tlw.add(this);
		sb.tlw.sort(Comparator.comparingInt(e -> e.layer));
	}

	public abstract void draw(FakeGraphics gra, P p, double psiz);

	public abstract void update();

	public abstract void updateAnimation();

	public abstract boolean IMUTime();
}
