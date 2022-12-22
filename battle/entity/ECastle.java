package common.battle.entity;

import common.CommonStatic;
import common.battle.BasisLU;
import common.battle.StageBasis;
import common.battle.attack.AttackAb;
import common.battle.attack.AttackVolcano;
import common.util.anim.EAnimD;
import common.util.pack.EffAnim.DefEff;
import common.util.unit.Trait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ECastle extends AbEntity {

	private final StageBasis sb;
	public int hit = 0;

	public EAnimD<DefEff> smoke;
	public int smokeLayer = -1;
	public int smokeX = -1;

	public ECastle(StageBasis b) {
		super(b.st.trail ? Integer.MAX_VALUE : b.st.health);
		sb = b;
	}

	public ECastle(StageBasis xb, BasisLU b) {
		super(b.t().getBaseHealth());
		sb = xb;
	}

	@Override
	public void damaged(AttackAb atk) {
		hit = 2;

		if(atk.isLongAtk || atk instanceof AttackVolcano)
			smoke = effas().A_WHITE_SMOKE.getEAnim(DefEff.DEF);
		else
			smoke = effas().A_ATK_SMOKE.getEAnim(DefEff.DEF);

		smokeLayer = (int) (atk.layer + 3 - sb.r.nextDouble() * -6);
		smokeX = (int) (pos + 25 - sb.r.nextDouble() * -25);

		int ans = atk.atk;
		System.out.println(atk.atk);
		ans *= 1 + atk.getProc().ATKBASE.mult / 100.0;

		int satk = atk.getProc().SATK.mult;
		if (satk > 0) {
			ans *= (100 + satk) * 0.01;
			sb.lea.add(new EAnimCont(pos, 9, effas().A_SATK.getEAnim(DefEff.DEF), -75.0));
			sb.lea.sort(Comparator.comparingInt(e -> e.layer));
			CommonStatic.setSE(SE_SATK);
		}
		if (atk.getProc().CRIT.mult > 0) {
			ans *= 0.01 * atk.getProc().CRIT.mult;
			sb.lea.add(new EAnimCont(pos, 9, effas().A_CRIT.getEAnim(DefEff.DEF), -75.0));
			sb.lea.sort(Comparator.comparingInt(e -> e.layer));
			CommonStatic.setSE(SE_CRIT);
		}
		CommonStatic.setSE(SE_HIT_BASE);
		health -= ans;

		if (health > maxH)
			health = maxH;

		if (health <= 0)
			health = 0;

		if(dire == -1 && CommonStatic.getConfig().shake && sb.shakeCoolDown[0] == 0 && (sb.shake == null || !Arrays.equals(sb.shake, SHAKE_MODE_BOSS))) {
			sb.shake = SHAKE_MODE_HIT;
			sb.shakeDuration = SHAKE_MODE_HIT[SHAKE_DURATION];
			sb.shakeCoolDown[0] = SHAKE_MODE_HIT[SHAKE_COOL_DOWN];
		}
	}

	@Override
	public int getAbi() {
		return 0;
	}

	@Override
	public boolean isBase() {
		return true;
	}

	@Override
	public void postUpdate() {

	}

	@Override
	public boolean ctargetable(ArrayList<Trait> t, Entity attacker, boolean targetOnly) { return true; }

	@Override
	public int touchable() {
		return TCH_N;
	}

	@Override
	public void update() {
		if(smoke != null) {
			if(smoke.done()) {
				smoke = null;
				smokeLayer = -1;
				smokeX = -1;
			} else {
				smoke.update(false);
			}
		}

		if (hit > 0)
			hit--;
	}

	@Override
	public void preUpdate() {

	}

}
