package common.util.entity;

import common.util.BattleObj;
import common.util.anim.EAnimD;
import common.util.system.P;
import common.util.system.fake.FakeGraphics;

public class EAnimCont extends BattleObj {

	public final double pos;
	public final int layer;
	private final EAnimD anim;

	public EAnimCont(double p, int lay, EAnimD ead) {
		pos = p;
		layer = lay;
		anim = ead;
	}

	/** return whether this animation is finished */
	public boolean done() {
		return anim.done();
	}

	public void draw(FakeGraphics gra, P p, double psiz) {
		anim.draw(gra, p, psiz);
	}

	public void update() {
		anim.update(false);

	}

}
