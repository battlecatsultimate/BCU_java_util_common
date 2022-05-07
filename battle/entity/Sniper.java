package common.battle.entity;

import common.CommonStatic;
import common.battle.StageBasis;
import common.battle.attack.AtkModelAb;
import common.battle.attack.AttackAb;
import common.battle.attack.AttackSimple;
import common.pack.UserProfile;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.util.anim.EAnimD;
import common.util.pack.EffAnim.SniperEff;
import common.util.unit.Trait;

import java.util.ArrayList;

public class Sniper extends AtkModelAb {

	private final EAnimD<?> anim = effas().A_SNIPER.getEAnim(SniperEff.IDLE);
	private final EAnimD<?> atka = effas().A_SNIPER.getEAnim(SniperEff.ATK);
	private int coolTime = SNIPER_CD, preTime = 0, atkTime = 0;
	public boolean enabled = true, canDo = true;
	public double pos, layer, height, bulletX,cannonAngle = 0, bulletAngle = 0;

	public Sniper(StageBasis sb) {
		super(sb);
	}

	/**
	 * base part of animation
	 */
	public void drawBase(FakeGraphics gra, P ori, double siz) {
		height = ori.y;

		if (atkTime == 0)
			anim.draw(gra, ori, siz);
		else {
			atka.draw(gra, ori, siz);
		}
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
		return b.ubase.pos + SNIPER_POS;
	}

	private void getAngle() {
		double Cx = b.st.len * 4 - 3200;
		double Uy = 4480;
		double Cy = 4400;
		double r = b.pos / CommonStatic.BattleConst.ratio * 4 / b.siz;

		double theta = Math.toDegrees(Math.atan2((10 * Math.sin(Math.PI / 30 * b.time) + (int) (Cy / 10) - 369) - (int) (Uy / 10) + 58 - 4 * layer, (int) ((Cx - r) / 10) + 203 - (int) ((pos * 4 - r) / 10)));

		if(bulletX == 0) {
			bulletAngle = theta;
		}

		//Formula is different, only for visual
		cannonAngle = theta;
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
			anim.setup();
		}

		// find enemy pos
		pos = -1;
		for (Entity e : b.le)
			if (e.dire == 1 && e.pos > pos && !e.isBase && (e.touchable() & TCH_N) > 0) {
				pos = e.pos;
				layer = e.layer;
			}

		getAngle();

		if (preTime > 0) {
			preTime--;
			if (preTime == 0) {
				//fire bullet
				bulletX = b.ubase.pos + SNIPER_POS + 375 * Math.cos(Math.toRadians((int) bulletAngle));

				atka.ent[6].alter(12, 1000);
				anim.ent[6].alter(12, 1000);
			}
		}

		if (bulletX != 0 && bulletX > pos) {
			bulletX = (int) (bulletX * 4 - 1500 * Math.cos(Math.toRadians((int) bulletAngle))) / 4.0;

			atka.ent[6].alter(4, (int) ((bulletX - b.ubase.pos - SNIPER_POS) / Math.cos(Math.toRadians((int) bulletAngle)) * CommonStatic.BattleConst.ratio * 1.13));
			anim.ent[6].alter(4, (int) ((bulletX - b.ubase.pos - SNIPER_POS) / Math.cos(Math.toRadians((int) bulletAngle)) * CommonStatic.BattleConst.ratio * 1.13));

			if (bulletX <= pos) {
				int atk = b.b.t().getBaseHealth() / 20;
				Proc proc = Proc.blank();
				proc.SNIPER.prob = 1;
				ArrayList<Trait> CTrait = new ArrayList<>();
				CTrait.add(UserProfile.getBCData().traits.get(TRAIT_TOT));
				AttackAb a = new AttackSimple(null, this, atk, CTrait, 0, proc, 0, getPos(), false, null, -1, true, 1);
				a.canon = -1;
				b.getAttack(a);

				bulletX = 0;

				atka.ent[6].alter(12, 0);
				anim.ent[6].alter(12, 0);
			}
		}

		if (atkTime > 0) {
			atkTime--;
			atka.update(false);
		} else {
			anim.update(true);
		}

		if(bulletX > 0) {
			anim.ent[6].alter(12, 1000);
			anim.ent[5].alter(11, (int) Math.round(bulletAngle * 10));
		} else {
			anim.ent[5].alter(11, (int) Math.round(cannonAngle * 10));
		}

		atka.ent[5].alter(11, (int) Math.round(bulletAngle * 10));

		anim.ent[1].alter(5, - (int) Math.round((989.5 + 25 * Math.sin(Math.PI * b.time / 30) - height) * CommonStatic.BattleConst.ratio));
		atka.ent[1].alter(5, - (int) Math.round((989.5 + 25 * Math.sin(Math.PI * b.time / 30) - height) * CommonStatic.BattleConst.ratio));
	}
}
