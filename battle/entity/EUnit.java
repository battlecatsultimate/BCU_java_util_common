package common.battle.entity;

import common.battle.StageBasis;
import common.battle.attack.AtkModelEnemy;
import common.battle.attack.AtkModelUnit;
import common.battle.attack.AttackAb;
import common.battle.attack.AttackWave;
import common.battle.data.MaskAtk;
import common.battle.data.MaskUnit;
import common.battle.data.Orb;
import common.battle.data.PCoin;
import common.util.BattleObj;
import common.util.Data;
import common.util.anim.EAnimU;
import common.util.unit.Level;

public class EUnit extends Entity {

	public static class OrbHandler extends BattleObj {
		protected static int getOrbAtk(AttackAb atk, EEnemy en) {
			if (atk.matk == null) {
				return 0;
			}

			if (atk.origin.model instanceof AtkModelUnit) {
				// Warning : Eunit.e became public now
				EUnit unit = (EUnit) ((AtkModelUnit) atk.origin.model).e;

				return unit.getOrbAtk(en.type, atk.matk);
			}

			return 0;
		}
	}

	protected final Level level;

	public EUnit(StageBasis b, MaskUnit de, EAnimU ea, double d0, Level level, PCoin pc) {
		super(b, de, ea, d0, b.b.t().getAtkMulti(), b.b.t().getDefMulti(), pc, level);
		layer = de.getFront() + (int) (b.r.nextDouble() * (de.getBack() - de.getFront() + 1));
		type = de.getType();
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
		type = status[P_CURSE][0] == 0 ? data.getType() : 0;
	}

	@Override
	protected int getDamage(AttackAb atk, int ans) {
		if (atk instanceof AttackWave && atk.waveType == WT_MINI) {
			ans = (int) ((double) ans * atk.getProc().MINIWAVE.multi / 100.0);
		}
		if (atk.model instanceof AtkModelEnemy) {
			int overlap = type & atk.type;
			if (overlap != 0 && (getAbi() & AB_GOOD) != 0)
				ans *= basis.b.t().getGOODDEF(overlap);
			if (overlap != 0 && (getAbi() & AB_RESIST) != 0)
				ans *= basis.b.t().getRESISTDEF(overlap);
			if (overlap != 0 && (getAbi() & AB_RESISTS) != 0)
				ans *= basis.b.t().getRESISTSDEF(overlap);
		}
		if ((atk.type & TB_WITCH) > 0 && (getAbi() & AB_WKILL) > 0)
			ans *= basis.b.t().getWKDef();
		if ((atk.type & TB_EVA) > 0 && (getAbi() & AB_EKILL) > 0)
			ans *= basis.b.t().getEKDef();
		if (isBase && (atk.abi & AB_BASE) > 0)
			ans *= 4;
		ans = critCalc((getAbi() & AB_METALIC) != 0, ans, atk);

		// Perform orb
		ans = getOrbRes(atk.type, ans);

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

	private int getOrbAtk(int trait, MaskAtk matk) {
		Orb orb = ((MaskUnit) data).getOrb();

		if (orb == null || level.getOrbs() == null) {
			return 0;
		}

		int ans = 0;

		for (int[] line : level.getOrbs()) {
			if (line.length == 0)
				continue;

			if (line[ORB_TYPE] == Data.ORB_RES || (line[ORB_TRAIT] & trait) == 0)
				continue;

			ans += orb.getAtk(line[ORB_GRADE], matk);
		}

		return ans;
	}

	private int getOrbRes(int trait, int atk) {
		Orb orb = ((MaskUnit) data).getOrb();

		if (orb == null || level.getOrbs() == null)
			return atk;

		int ans = atk;

		for (int[] line : level.getOrbs()) {
			if (line.length == 0)
				continue;

			if (line[ORB_TYPE] == Data.ORB_ATK || (line[ORB_TRAIT] & trait) == 0)
				continue;

			ans = orb.getRes(line[ORB_GRADE], ans);
		}

		return ans;
	}
}
