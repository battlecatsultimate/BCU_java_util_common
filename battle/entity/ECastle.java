package common.battle.entity;

import common.CommonStatic;
import common.battle.BasisLU;
import common.battle.StageBasis;
import common.battle.attack.AttackAb;
import common.util.pack.EffAnim.DefEff;
import common.util.unit.Trait;

import java.util.ArrayList;
import java.util.Comparator;

public class ECastle extends AbEntity {

	private final StageBasis sb;
	public int hit = 0;

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
		hit = 2;
		if(atk.isLongAtk)
			sb.lea.add(new EAnimCont(pos, atk.layer, effas().A_WHITE_SMOKE.getEAnim(DefEff.DEF), -75.0));
		else
			sb.lea.add(new EAnimCont(pos, atk.layer, effas().A_ATK_SMOKE.getEAnim(DefEff.DEF), -75.0));

		sb.lea.sort(Comparator.comparingInt(e -> e.layer));

		int ans = atk.atk;
		if ((atk.abi & AB_BASE) > 0)
			ans *= 4;
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
	public boolean ctargetable(ArrayList<Trait> t, boolean targetOnly) { return true; }

	@Override
	public int touchable() {
		return TCH_N;
	}

	@Override
	public void update() {
		if (hit > 0)
			hit--;
	}

}
