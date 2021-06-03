package common.battle.entity;

import common.battle.StageBasis;
import common.battle.attack.AtkModelUnit;
import common.battle.attack.AttackAb;
import common.battle.attack.AttackWave;
import common.battle.data.MaskEnemy;
import common.pack.UserProfile;
import common.util.anim.EAnimU;
import common.util.unit.Trait;

import java.util.ArrayList;

public class EEnemy extends Entity {

	public final int mark;
	public final double mult, mula;

	public EEnemy(StageBasis b, MaskEnemy de, EAnimU ea, double magnif, double atkMagnif, int d0, int d1, int m) {
		super(b, de, ea, atkMagnif, magnif);
		mult = magnif;
		mula = atkMagnif;
		mark = m;
		isBase = mark == -1;
		layer = d0 + (int) (b.r.nextDouble() * (d1 - d0 + 1));
		traits = de.getTraits();

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
	public void kill(boolean atk) {
		super.kill(atk);
		if (!basis.st.trail && !atk) {
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
			int overlap = ctargetable(atk.trait,false) ? 1 : 0;
			ArrayList<Trait> sharedTraits = new ArrayList<>(atk.trait);
			sharedTraits.retainAll(traits);
			if (overlap != 0 && (atk.abi & AB_GOOD) != 0)
				ans *= basis.b.t().getGOODATK(sharedTraits);
			if (overlap != 0 && (atk.abi & AB_MASSIVE) != 0)
				ans *= basis.b.t().getMASSIVEATK(sharedTraits);
			if (overlap != 0 && (atk.abi & AB_MASSIVES) != 0)
				ans *= basis.b.t().getMASSIVESATK(sharedTraits);
		}
		if (isBase && (atk.abi & AB_BASE) > 0)
			ans *= 4;
		if (traits.contains(UserProfile.getBCData().traits.get(TRAIT_WITCH)) && (atk.abi & AB_WKILL) > 0)
			ans *= basis.b.t().getWKAtk();
		if (traits.contains(UserProfile.getBCData().traits.get(TRAIT_EVA)) && (atk.abi & AB_EKILL) > 0)
			ans *= basis.b.t().getEKAtk();
		if (atk.canon == 5)
			if ((touchable() & TCH_UG) > 0)
				ans = (int) (maxH * basis.b.t().getCanonMulti(-5) / 1000);
			else
				ans = (int) (maxH * basis.b.t().getCanonMulti(5) / 1000);
		ans = critCalc(data.getTraits().contains(UserProfile.getBCData().traits.get(TRAIT_METAL)), ans, atk);

		// Perform Orb
		ans += EUnit.OrbHandler.getOrbAtk(atk, this);

		return ans;
	}

	@Override
	protected double getLim() {
		double ans;
		int width = ((MaskEnemy) data).getLim();

		if (mark == 1)
			ans = pos - 700 - width; // guessed value compared to BC
		else
			ans = pos - data.getWidth();
		return Math.max(0, ans);
	}

	@Override
	protected int traitType() {
		return 1;
	}
}
