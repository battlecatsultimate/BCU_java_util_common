package common.battle.attack;

import common.CommonStatic;
import common.CommonStatic.BattleConst;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeTransform;
import common.util.anim.EAnimD;
import common.util.pack.EffAnim.VolcEff;

public class ContVolcano extends ContAb {
	protected final EAnimD<VolcEff> anim;
	protected final AttackVolcano v;

	private final int aliveTime;

	private int t = 0;

	protected ContVolcano(AttackVolcano v, double p, int lay, int alive) {
		super(v.model.b, p, lay);
		anim = (v.dire == 1 ? effas().A_E_VOLC : effas().A_VOLC).getEAnim(VolcEff.START);
		this.v = v;
		aliveTime = alive;
		CommonStatic.setSE(SE_VOLC_START);

		update();
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
		if (t >= VOLC_PRE && t <= VOLC_PRE + aliveTime && anim.type != VolcEff.DURING) {
			anim.changeAnim(VolcEff.DURING, false);
			CommonStatic.setSE(SE_VOLC_LOOP);
		} else if (t > VOLC_PRE + aliveTime && anim.type != VolcEff.END)
			anim.changeAnim(VolcEff.END, false);

		if (t >= VOLC_PRE && t < VOLC_PRE + aliveTime && (t - VOLC_PRE) % VOLC_SE == 0) {
			CommonStatic.setSE(SE_VOLC_LOOP);
		}

		if (t >= aliveTime + VOLC_POST + VOLC_PRE) {
			activate = false;
		} else {
			if (t >= VOLC_PRE && t < VOLC_PRE + aliveTime)
				sb.getAttack(v);
			anim.update(false);
			t++;
		}
	}

	protected void drawAxis(FakeGraphics gra, P p, double siz) {
		if (!CommonStatic.getConfig().ref)
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
