package common.battle.attack;

import common.CommonStatic;
import common.CommonStatic.BattleConst;
import common.battle.entity.AbEntity;
import common.battle.entity.Entity;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeTransform;
import common.util.anim.EAnimD;

public abstract class ContWaveAb extends ContAb {

	protected final AttackWave atk;
	protected final EAnimD<?> anim;
	protected ContWaveDef nextWave;
	protected int soundEffect;
	private int t = 0;
	private final int maxt;
	private boolean tempAtk;

	protected ContWaveAb(AttackWave a, double p, EAnimD<?> ead, int layer, boolean delay) {
		super(a.model.b, p, layer);
		atk = a;
		anim = ead;
		maxt = anim.len() - 1;
		anim.setTime(1);
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

	@Override
	public void update() {
		tempAtk = false;
		boolean isMini = atk.waveType == WT_MINI;
		// guessed attack point compared from BC
		int attack = (isMini ? 4 : 6);
		// guessed wave block time compared from BC
		if (t == 0)
			CommonStatic.setSE(soundEffect);
		if (t >= (isMini ? 1 : 2) && t <= attack) {
			atk.capture();
			for (AbEntity e : atk.capt)
				if ((e.getAbi() & AB_WAVES) > 0) {
					if (e instanceof Entity)
						((Entity) e).anim.getEff(STPWAVE);
					deactivate();
					return;
				}
		}
		if (!activate)
			return;
		if (t == (isMini ? W_MINI_TIME - 1 : W_TIME)) {
			if (isMini && atk.proc.MINIWAVE.lv > 0)
				nextWave();
			else if (!isMini && atk.getProc().WAVE.lv > 0)
				nextWave();
		}
		if (t == attack) {
			sb.getAttack(atk);
			tempAtk = true;
		}
		if (maxt == t)
			activate = false;
		if (t >= 0)
			anim.update(false);
		t++;
	}

	/**
	 * kill all consecutive waves
	 */
	protected void deactivate() {
		activate = false;
		if (nextWave != null)
			nextWave.deactivate();
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
