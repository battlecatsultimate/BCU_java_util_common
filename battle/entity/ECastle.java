package common.battle.entity;

import common.CommonStatic;
import common.battle.BasisLU;
import common.battle.StageBasis;
import common.battle.attack.AttackAb;
import common.util.pack.EffAnim.DefEff;

public class ECastle extends AbEntity {

	private final StageBasis sb;

	public ECastle(StageBasis b) {
		super(b.st.health);
		sb = b;
	}

	public ECastle(StageBasis xb, BasisLU b) {
		super(b.t().getBaseHealth());
		sb = xb;
	}

	@Override
	public void damaged(AttackAb atk) {
		int ans = atk.atk;
		if ((atk.abi & AB_BASE) > 0)
			ans *= 4;
		int satk = atk.getProc().SATK.mult;
		if (satk > 0)
			ans *= (100 + satk) * 0.01;
		if (atk.getProc().CRIT.mult > 0) {
			ans *= 0.01 * atk.getProc().CRIT.mult;
			sb.lea.add(new EAnimCont(pos, 9, effas().A_CRIT.getEAnim(DefEff.DEF)));
			CommonStatic.setSE(SE_CRIT);
		} else
			CommonStatic.setSE(SE_HIT_BASE);
		health -= ans;
		if (health > maxH)
			health = maxH;

		if (health <= 0)
			health = 0;
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
	public boolean targetable(int type) {
		return true;
	}

	@Override
	public int touchable() {
		return TCH_N;
	}

	@Override
	public void update() {

	}

}
