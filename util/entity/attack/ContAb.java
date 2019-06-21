package common.util.entity.attack;

import common.util.BattleObj;
import common.util.basis.StageBasis;
import common.util.system.P;
import common.util.system.fake.FakeGraphics;

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
	}

	public abstract void draw(FakeGraphics gra, P p, double psiz);

	public abstract void update();

}
