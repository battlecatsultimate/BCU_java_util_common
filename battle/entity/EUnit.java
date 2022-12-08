package common.battle.entity;

import common.battle.StageBasis;
import common.battle.Treasure;
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

@SuppressWarnings("ForLoopReplaceableByForEach")
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

		protected static double getOrbMassive(AttackAb atk, ArrayList<Trait> traits, Treasure t) {
			if(atk.origin.model instanceof AtkModelUnit) {
				return ((EUnit) ((AtkModelUnit) atk.origin.model).e).getOrbMassive(atk.trait, traits, t);
			}

			return ((EUnit) ((AtkModelUnit)atk.model).e).getOrbMassive(atk.trait, traits, t);
		}

		protected static double getOrbGood(AttackAb atk, ArrayList<Trait> traits, Treasure t) {
			if(atk.origin.model instanceof AtkModelUnit) {
				return ((EUnit) ((AtkModelUnit) atk.origin.model).e).getOrbGood(atk.trait, traits, t);
			}

			return ((EUnit) ((AtkModelUnit)atk.model).e).getOrbGood(atk.trait, traits, t);
		}
	}

	public final int lvl;
	public final int[] index;

	protected final Level level;

	public EUnit(StageBasis b, MaskUnit de, EAnimU ea, double d0, int layer0, int layer1, Level level, PCoin pc, int[] index) {
		super(b, de, ea, d0, b.b.t().getAtkMulti(), b.b.t().getDefMulti(), pc, level);
		layer = layer0 == layer1 ? layer0 : layer0 + (int) (b.r.nextDouble() * (layer1 - layer0 + 1));
		traits = de.getTraits();
		lvl = level.getLv();
		this.index = index;

		this.level = level;
	}

	//used for waterblast
	public EUnit(StageBasis b, MaskUnit de, EAnimU ea, double d0) {
		super(b, de, ea, d0, b.b.t().getAtkMulti(), b.b.t().getDefMulti(), null, null);
		layer = de.getFront() + (int) (b.r.nextDouble() * (de.getBack() - de.getFront() + 1));
		traits = de.getTraits();
		this.index = null;

		lvl = 1;
		health = maxH = (int) (health * b.b.t().getCannonMagnification(BASE_WALL, BASE_WALL_MAGNIFICATION) / 100.0);
		level = null;
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
		traits = status[P_CURSE][0] == 0 && status[P_SEAL][0] == 0 ? data.getTraits() : new ArrayList<>();
	}

	@Override
	public void damaged(AttackAb atk) {
		if (atk.trait.contains(BCTraits.get(TRAIT_BEAST))) {
			Proc.BSTHUNT beastDodge = getProc().BSTHUNT;
			if (beastDodge.prob > 0 && (atk.dire != dire)) {
				if (status[P_BSTHUNT][0] == 0 && (beastDodge.prob == 100 || basis.r.nextDouble() * 100 < beastDodge.prob)) {
					status[P_BSTHUNT][0] = beastDodge.time;
					anim.getEff(P_IMUATK);
				}

				if (status[P_BSTHUNT][0] > 0) {
					damageTaken += atk.atk;

					if(index != null) {
						basis.totalDamageTaken[index[0]][index[1]] += atk.atk;
					}

					return;
				}
			}
		}

		super.damaged(atk);

		if(index != null) {
			basis.totalDamageTaken[index[0]][index[1]] += atk.atk;
		}
	}

	@Override
	protected int getDamage(AttackAb atk, int ans) {
		if (atk instanceof AttackWave && atk.waveType == WT_MINI) {
			ans = (int) ((double) ans * atk.getProc().MINIWAVE.multi / 100.0);
		}
		if (atk.model instanceof AtkModelEnemy && status[P_CURSE][0] == 0) {
			ArrayList<Trait> sharedTraits = new ArrayList<>(atk.trait);
			sharedTraits.retainAll(traits);
			boolean isAntiTraited = targetTraited(atk.trait);
			for (Trait t : traits) {
				if (t.BCTrait || sharedTraits.contains(t))
					continue;
				if ((t.targetType && isAntiTraited) || t.others.contains(((MaskUnit)data).getPack()))
					sharedTraits.add(t);
			}
			if ((getAbi() & AB_GOOD) != 0)
				ans *= basis.b.t().getGOODDEF(atk.trait, sharedTraits, ((MaskUnit)data).getOrb(), level);
			if ((getAbi() & AB_RESIST) != 0)
				ans *= basis.b.t().getRESISTDEF(atk.trait, sharedTraits, ((MaskUnit)data).getOrb(), level);
			if (!sharedTraits.isEmpty() && (getAbi() & AB_RESISTS) != 0)
				ans *= basis.b.t().getRESISTSDEF(sharedTraits);
		}
		if (atk.trait.contains(UserProfile.getBCData().traits.get(TRAIT_WITCH)) && (getAbi() & AB_WKILL) > 0)
			ans *= basis.b.t().getWKDef();
		if (atk.trait.contains(UserProfile.getBCData().traits.get(TRAIT_EVA)) && (getAbi() & AB_EKILL) > 0)
			ans *= basis.b.t().getEKDef();
		if (isBase)
			ans *= 1 + atk.getProc().ATKBASE.mult / 100.0;
		if (atk.trait.contains(UserProfile.getBCData().traits.get(TRAIT_BARON)) && (getAbi() & AB_BAKILL) > 0)
			ans *= 0.7;
		if (atk.trait.contains(UserProfile.getBCData().traits.get(Data.TRAIT_BEAST)) && getProc().BSTHUNT.type.active)
			ans *= 0.6; //Not sure
		ans = critCalc((getAbi() & AB_METALIC) != 0, ans, atk);

		// Perform orb
		ans = getOrbRes(atk.trait, ans);

		return ans;
	}

	@Override
	protected double getLim() {
		return Math.max(0, basis.st.len - pos - ((MaskUnit) data).getLimit());
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

			if (line[ORB_TYPE] != Data.ORB_ATK || !trait.contains(orbType))
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

			if (line[ORB_TYPE] != Data.ORB_RES || !trait.contains(orbType))
				continue;

			ans = orb.getRes(line[ORB_GRADE], ans);
		}

		return ans;
	}

	private double getOrbMassive(ArrayList<Trait> eTraits, ArrayList<Trait> traits, Treasure t) {
		double ini = 1;

		if (!traits.isEmpty())
			ini = 3 + 1.0 / 3 * t.getFruit(traits);

		Orb orbs = ((MaskUnit)data).getOrb();

		if(orbs != null && level.getOrbs() != null) {
			int[][] levelOrbs = level.getOrbs();

			for(int i = 0; i < levelOrbs.length; i++) {
				if (levelOrbs[i].length < ORB_TOT)
					continue;

				if (levelOrbs[i][ORB_TYPE] == ORB_MASSIVE) {
					Trait orbType = Trait.convertType(levelOrbs[i][ORB_TRAIT]).get(0);

					if (eTraits.contains(orbType))
						ini += ORB_MASSIVE_MULTI[levelOrbs[i][ORB_GRADE]];
				}
			}
		}

		if (ini == 1)
			return ini;

		double com = 1 + t.b.getInc(C_MASSIVE) * 0.01;

		return ini * com;
	}

	private double getOrbGood(ArrayList<Trait> eTraits, ArrayList<Trait> traits, Treasure t) {
		double ini = 1;

		if (!traits.isEmpty())
			ini = 1.5 * (1 + 0.2 / 3 * t.getFruit(traits));

		Orb orbs = ((MaskUnit)data).getOrb();

		if(orbs != null && level.getOrbs() != null) {
			int[][] levelOrbs = level.getOrbs();

			for (int i = 0; i < levelOrbs.length; i++) {
				if (levelOrbs[i].length < ORB_TOT)
						continue;

				if (levelOrbs[i][ORB_TYPE] == ORB_STRONG) {
					Trait orbType = Trait.convertType(levelOrbs[i][ORB_TRAIT]).get(0);
					if (eTraits.contains(orbType)) {
						ini += ORB_STR_ATK_MULTI[levelOrbs[i][ORB_GRADE]];
					}
				}
			}
		}

		if (ini == 1)
			return ini;

		double com = 1 + t.b.getInc(C_GOOD) * 0.01;
		return ini * com;
	}

	@Override
	protected void onLastBreathe() {
		basis.notifyUnitDeath();
	}
}
