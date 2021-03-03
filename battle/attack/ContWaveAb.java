package common.battle.attack;

import common.CommonStatic;
import common.CommonStatic.BattleConst;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeTransform;
import common.util.anim.EAnimD;

import java.util.Set;

public abstract class ContWaveAb extends ContAb {

	protected final AttackWave atk;
	protected final EAnimD<?> anim;
	protected Set<ContWaveAb> waves;
	protected int soundEffect;
	protected int t = 0;
	protected int maxt;
	protected boolean tempAtk;

	protected ContWaveAb(AttackWave a, double p, EAnimD<?> ead, int layer, boolean delay) {
		super(a.model.b, p, layer);
		atk = a;
		anim = ead;
		maxt = anim.len();
		if (delay)
			t = -2;
	}

	@Override
	public void draw(FakeGraphics gra, P p, double siz) {
		if (t < 0)
			return;
		FakeTransform at = gra.getTransform();
		anim.draw(gra, p, siz);
		gra.setTransform(at);
		drawAxis(gra, p, siz);
		gra.delete(at);
	}

	/**
	 * kill every related wave
	 */
	protected void deactivate() {
		waves.forEach(w -> w.activate = false);
	}

	protected void drawAxis(FakeGraphics gra, P p, double siz) {
		if (!CommonStatic.getConfig().ref)
			return;

		// after this is the drawing of hit boxes
		siz *= 1.25;
		double rat = BattleConst.ratio;
		int h = (int) (640 * rat * siz);
		gra.setColor(FakeGraphics.MAGENTA);
		double d0 = Math.min(atk.sta, atk.end);
		double ra = Math.abs(atk.sta - atk.end);
		int x = (int) ((d0 - pos) * rat * siz + p.x);
		int y = (int) p.y;
		int w = (int) (ra * rat * siz);
		if (tempAtk)
			gra.fillRect(x, y, w, h);
		else
			gra.drawRect(x, y, w, h);
	}

	/**
	 * generate the next wave container
	 */
	protected abstract void nextWave();

}
