package common.battle.attack;

import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeTransform;
import common.util.anim.EAnimD;
import common.util.pack.EffAnim;

public class ContVolcano extends ContAb {
	protected final EAnimD anim;
	protected final AttackVolcano v;

	private int t = 0;
	private int aliveTime;

	protected ContVolcano(AttackVolcano v, double p, int lay, int alive) {
		super(v.model.b, p, lay);
		anim = EffAnim.effas[v.dire == 1 ? A_E_VOLC : A_VOLC].getEAnim(1);
		this.v = v;
		aliveTime = alive;
	}

	@Override
	public void draw(FakeGraphics gra, P p, double psiz) {
		FakeTransform at = gra.getTransform();

		gra.setTransform(at);
	}

	@Override
	public void update() {
		if (aliveTime <= 0) {
			activate = false;
		} else {
			aliveTime--;
			t++;
		}
	}

}
