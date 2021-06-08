package common.battle.entity;

import common.battle.StageBasis;
import common.battle.attack.AtkModelEnemy;
import common.battle.attack.AtkModelUnit;
import common.battle.attack.AttackAb;
import common.battle.attack.AttackWave;
import common.battle.data.*;
import common.pack.UserProfile;
import common.util.BattleObj;
import common.util.Data;
import common.util.anim.EAnimU;
import common.util.unit.Level;
import common.util.unit.Trait;

import java.util.ArrayList;

public class EUnit extends Entity {

	public static class OrbHandler extends BattleObj {
		protected static int getOrbAtk(AttackAb atk, EEnemy en) {
			if (atk.matk == null) {
				return 0;
			}

			if (atk.origin.model instanceof AtkModelUnit) {
				// Warning : Eunit.e became public now
				EUnit unit = (EUnit) ((AtkModelUnit) atk.origin.model).e;

				return unit.getOrbAtk(en.traits, atk.matk);
			}

			return 0;
		}
	}

	public final int lvl;

	protected final Level level;

	public EUnit(StageBasis b, MaskUnit de, EAnimU ea, double d0, Level level, PCoin pc) {
		super(b, de, ea, d0, b.b.t().getAtkMulti(), b.b.t().getDefMulti(), pc, level);
		layer = de.getFront() + (int) (b.r.nextDouble() * (de.getBack() - de.getFront() + 1));
		traits = de.getTraits();
		// if level is null, update HP to match level
		if (level == null) {
			lvl = 1;
			health = maxH = (int) (health * b.b.t().getCannonMagnification(BASE_WALL, BASE_WALL_MAGNIFICATION) / 100.0);
		} else {
			lvl = level.getLvs()[0];
		}
		this.level = level;
	}

	@Override
	public int getAtk() {
		int atk = aam.getAtk();
		if (status[P_WEAK][0] > 0)
			atk = atk * status[P_WEAK][1] / 100;
		if (status[P_STRONG][0] != 0)
			atk += atk * (status[P_STRONG][0] + basis.b.getInc(C_STRONG)) / 100;

		return atk;
	}

	@Override
	public void update() {
		super.update();
		traits = status[P_CURSE][0] == 0 ? data.getTraits() : new ArrayList<>();
	}

	@Override
	protected int getDamage(AttackAb atk, int ans) {
		if (atk instanceof AttackWave && atk.waveType == WT_MINI) {
			ans = (int) ((double) ans * atk.getProc().MINIWAVE.multi / 100.0);
		}
		if (atk.model instanceof AtkModelEnemy) {
			int overlap = ctargetable(atk.trait,false) ? 1 : 0;
			ArrayList<Trait> sharedTraits = new ArrayList<>(atk.trait);
			sharedTraits.retainAll(traits);
			if (overlap != 0 && (getAbi() & AB_GOOD) != 0)
				ans *= basis.b.t().getGOODDEF(sharedTraits);
			if (overlap != 0 && (getAbi() & AB_RESIST) != 0)
				ans *= basis.b.t().getRESISTDEF(sharedTraits);
			if (overlap != 0 && (getAbi() & AB_RESISTS) != 0)
				ans *= basis.b.t().getRESISTSDEF(sharedTraits);
		}
		if (traits.contains(UserProfile.getBCData().traits.get(TRAIT_WITCH)) && (getAbi() & AB_WKILL) > 0)
			ans *= basis.b.t().getWKDef();
		if (traits.contains(UserProfile.getBCData().traits.get(TRAIT_EVA)) && (getAbi() & AB_EKILL) > 0)
			ans *= basis.b.t().getEKDef();
		if (isBase && (atk.abi & AB_BASE) > 0)
			ans *= 4;
		ans = critCalc((getAbi() & AB_METALIC) != 0, ans, atk);

		// Perform orb
		ans = getOrbRes(atk.trait, ans);

		return ans;
	}

	@Override
	protected double getLim() {
		return basis.st.len - pos;
	}

	@Override
	protected int traitType() {
		return -1;
	}

	@Override
	protected boolean updateMove(double maxl, double extmov) {
		if (status[P_SLOW][0] == 0)
			extmov += data.getSpeed() * basis.b.getInc(C_SPE) / 200.0;
		return super.updateMove(maxl, extmov);
	}

	private int getOrbAtk(ArrayList<Trait> trait, MaskAtk matk) {
		Orb orb = ((MaskUnit) data).getOrb();

		if (orb == null || level.getOrbs() == null) {
			return 0;
		}

		int ans = 0;

		for (int[] line : level.getOrbs()) {
			if (line.length == 0)
				continue;
			Trait orbType = Trait.convertType(line[ORB_TRAIT]).get(0);

			if (line[ORB_TYPE] == Data.ORB_RES || !trait.contains(orbType))
				continue;

			ans += orb.getAtk(line[ORB_GRADE], matk);
		}

		return ans;
	}

	private int getOrbRes(ArrayList<Trait> trait, int atk) {
		Orb orb = ((MaskUnit) data).getOrb();

		if (orb == null || level.getOrbs() == null)
			return atk;

		int ans = atk;

		for (int[] line : level.getOrbs()) {
			if (line.length == 0)
				continue;
			Trait orbType = Trait.convertType(line[ORB_TRAIT]).get(0);

			if (line[ORB_TYPE] == Data.ORB_ATK || !trait.contains(orbType))
				continue;

			ans = orb.getRes(line[ORB_GRADE], ans);
		}

		return ans;
	}
}
