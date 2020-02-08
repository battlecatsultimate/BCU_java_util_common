package common.battle.attack;

import common.CommonStatic;
import common.CommonStatic.BattleConst;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeTransform;
import common.util.ImgCore;
import common.util.anim.EAnimD;
import common.util.pack.EffAnim;

public class ContVolcano extends ContAb {
	protected final EAnimD anim;
	protected final AttackVolcano v;

	private final int aliveTime;

	private int t = 0;

	protected ContVolcano(AttackVolcano v, double p, int lay, int alive) {
		super(v.model.b, p, lay);
		anim = EffAnim.effas[v.dire == 1 ? A_E_VOLC : A_VOLC].getEAnim(1);
		anim.changeAnim(0);
		this.v = v;
		aliveTime = alive;
		CommonStatic.def.setSE(SE_VOLC_START);
	}

	@Override
	public void draw(FakeGraphics gra, P p, double psiz) {
		FakeTransform at = gra.getTransform();
		anim.draw(gra, p, psiz);
		gra.setTransform(at);
		drawAxis(gra, p, psiz);
	}

	@Override
	public void update() {
		if (t > VOLC_PRE && t <= VOLC_PRE + aliveTime && anim.type != 1) {
			anim.changeAnim(1);
			CommonStatic.def.setSE(SE_VOLC_LOOP);
		} else if (t > VOLC_PRE + aliveTime && anim.type != 2)
			anim.changeAnim(2);

		if (t > VOLC_PRE && t <= VOLC_PRE + aliveTime && (t-VOLC_PRE) % VOLC_SE == 0) {
			CommonStatic.def.setSE(SE_VOLC_LOOP);
		}

		if (t >= aliveTime + VOLC_POST + VOLC_PRE) {
			activate = false;
		} else {
			if (t > VOLC_PRE && t <= VOLC_PRE + aliveTime)
				sb.getAttack(v);
			anim.update(false);
			t++;
		}
	}

	protected void drawAxis(FakeGraphics gra, P p, double siz) {
		if (!ImgCore.ref)
			return;

		// after this is the drawing of hit boxes
		siz *= 1.25;
		double rat = BattleConst.ratio;
		int h = (int) (640 * rat * siz);
		gra.setColor(FakeGraphics.MAGENTA);
		double d0 = Math.min(v.sta, v.end);
		double ra = Math.abs(v.sta - v.end);
		int x = (int) ((d0 - pos) * rat * siz + p.x);
		int y = (int) p.y;
		int w = (int) (ra * rat * siz);

		if (v.attacked) {
			gra.fillRect(x, y, w, h);
			v.attacked = !v.attacked;
		} else {
			gra.drawRect(x, y, w, h);
		}
	}

}
