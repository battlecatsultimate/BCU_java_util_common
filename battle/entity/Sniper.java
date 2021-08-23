package common.battle.entity;

import common.battle.StageBasis;
import common.battle.attack.AtkModelAb;
import common.battle.attack.AttackAb;
import common.battle.attack.AttackSimple;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.util.anim.EAnimD;
import common.util.pack.EffAnim.SniperEff;

public class Sniper extends AtkModelAb {

	private final EAnimD<?> anim = effas().A_SNIPER.getEAnim(SniperEff.IDLE);
	private final EAnimD<?> atka = effas().A_SNIPER.getEAnim(SniperEff.ATK);
	private int coolTime = SNIPER_CD, preTime = 0, atkTime = 0, angle = 0;
	public boolean enabled = true, canDo = true;
	public double pos, height, updown;

	public Sniper(StageBasis sb) {
		super(sb);
	}

	/**
	 * attack part of animation
	 */
	public void drawAtk(FakeGraphics g, P ori, double siz) {
		// TODO
	}

	/**
	 * base part of animation
	 */
	public void drawBase(FakeGraphics gra, P ori, double siz) {
		// TODO
		// double angle = Math.atan2(getPos() - pos, height);

		height = ori.y;
		if (atkTime == 0)
			anim.draw(gra, ori, siz);
		else
			atka.draw(gra, ori, siz);

	}

	@Override
	public int getAbi() {
		return 0;
	}

	@Override
	public int getDire() {
		return -1;
	}

	@Override
	public double getPos() {
		return b.st.len + SNIPER_POS;
	}

	public void update() {
		if (canDo && b.ubase.health <= 0) {
			canDo = false;
		}

		if (enabled && coolTime > 0)
			coolTime--;
		if (coolTime == 0 && enabled && pos > 0 && canDo) {
			coolTime = SNIPER_CD;
			preTime = SNIPER_PRE;
			atkTime = atka.len();
			atka.setup();
		}
		if (preTime > 0) {
			preTime--;
			if (preTime == 0) {
				// attack
				int atk = b.b.t().getBaseHealth() / 20;
				Proc proc = Proc.blank();
				proc.SNIPER.prob = 1;
				AttackAb a = new AttackSimple(null, this, atk, -1, 0, proc, 0, getPos(), false, null, -1, true, 1);
				a.canon = -1;
				b.getAttack(a);
			}
		}
		if (atkTime > 0) {
			atkTime--;
			atka.update(false);
		} else {
			anim.update(true);
		}

		// find enemy pos
		pos = -1;
		for (Entity e : b.le)
			if (e.dire == 1 && e.pos > pos && !e.isBase && (e.touchable() & TCH_N) > 0)
				pos = e.pos;

		// Get angle of cannon and bullet
		angle = -(int) (Math.toDegrees(Math.atan(height / (getPos() - pos)))) * 10;

		anim.ent[5].alter(11, angle);
		atka.ent[5].alter(11, angle);

		// Get distance which bullet will fly
		// path = new P(-(getPos()-pos+300),height);
	}
}
