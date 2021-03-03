package common.battle.entity;

import common.battle.StageBasis;
import common.battle.attack.AtkModelUnit;
import common.battle.attack.AttackAb;
import common.battle.attack.AttackWave;
import common.battle.data.MaskEnemy;
import common.util.anim.EAnimU;

public class EEnemy extends Entity {

	public final int mark;
	public final double mult, mula;

	public EEnemy(StageBasis b, MaskEnemy de, EAnimU ea, double d, double datk, int d0, int d1, int m) {
		super(b, de, ea, datk, 1, d);
		mult = d;
		mula = datk;
		mark = m;
		isBase = mark == -1;
		layer = d0 + (int) (b.r.nextDouble() * (d1 - d0 + 1));
		type = de.getType();

		canBurrow = mark != 1;
	}

	@Override
	public int getAtk() {
		int atk = aam.getAtk();
		if (status[P_WEAK][0] > 0)
			atk = atk * status[P_WEAK][1] / 100;
		if (status[P_STRONG][0] != 0)
			atk += atk * status[P_STRONG][0] / 100;
		return atk;
	}

	@Override
	public void kill() {
		super.kill();
		if (!basis.st.trail) {
			double mul = basis.b.t().getDropMulti();
			if (tempearn)
				mul *= 2;
			basis.mon += mul * ((MaskEnemy) data).getDrop();
		}
	}

	@Override
	protected int getDamage(AttackAb atk, int ans) {
		if (atk instanceof AttackWave && atk.waveType == WT_MINI) {
			ans = (int) ((double) ans * atk.getProc().MINIWAVE.multi / 100.0);
		}
		if (atk.model instanceof AtkModelUnit) {
			int overlap = type & atk.type;
			if (overlap != 0 && (atk.abi & AB_GOOD) != 0)
				ans *= basis.b.t().getGOODATK(overlap);
			if (overlap != 0 && (atk.abi & AB_MASSIVE) != 0)
				ans *= basis.b.t().getMASSIVEATK(overlap);
			if (overlap != 0 && (atk.abi & AB_MASSIVES) != 0)
				ans *= basis.b.t().getMASSIVESATK(overlap);
		}
		if (isBase && (atk.abi & AB_BASE) > 0)
			ans *= 4;
		if ((type & TB_WITCH) > 0 && (atk.abi & AB_WKILL) > 0)
			ans *= basis.b.t().getWKAtk();
		if ((type & TB_EVA) > 0 && (atk.abi & AB_EKILL) > 0)
			ans *= basis.b.t().getEKAtk();
		if (atk.canon == 5)
			if ((touchable() & TCH_UG) > 0)
				ans = (int) (maxH * basis.b.t().getCanonMulti(-5) / 1000);
			else
				ans = (int) (maxH * basis.b.t().getCanonMulti(5) / 1000);
		ans = critCalc((data.getType() & TB_METAL) != 0, ans, atk);

		// Perform Orb
		ans += EUnit.OrbHandler.getOrbAtk(atk, this);

		return ans;
	}

	@Override
	protected double getLim() {
		double ans = 0;
		// third number of last ma_model line, or if it's 0 (custom unit), use pivot-x of part 0
		int width = Math.abs(anim.getMaModel().confs[0][2]);
		if (width == 0)
			width = Math.abs(anim.getMaModel().parts[0][6]);

		if (mark == 1)
			ans = pos - 700 - width * 6; // guessed value compared to BC
		else
			ans = pos - data.getWidth();
		return Math.max(0, ans);
	}

	@Override
	protected int traitType() {
		return 1;
	}

}
