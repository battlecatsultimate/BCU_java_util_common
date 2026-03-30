package common.battle.entity;

import common.battle.StageBasis;
import common.battle.Treasure;
import common.battle.attack.*;
import common.battle.data.MaskAtk;
import common.battle.data.MaskUnit;
import common.battle.data.OrbInfo;
import common.battle.data.PCoin;
import common.pack.UserProfile;
import common.util.BattleObj;
import common.util.Data;
import common.util.anim.EAnimU;
import common.util.pack.EffAnim;
import common.util.unit.Level;
import common.util.unit.Trait;

import java.util.ArrayList;
import java.util.List;

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

		protected static float getOrbMassive(AttackAb atk, ArrayList<Trait> traits, Treasure t) {
			if(atk.origin.model instanceof AtkModelUnit) {
				return ((EUnit) ((AtkModelUnit) atk.origin.model).e).getOrbMassive(atk.trait, traits, t);
			}

			return ((EUnit) ((AtkModelUnit)atk.model).e).getOrbMassive(atk.trait, traits, t);
		}

		protected static float getOrbGood(AttackAb atk, ArrayList<Trait> traits, Treasure t) {
			if(atk.origin.model instanceof AtkModelUnit) {
				return ((EUnit) ((AtkModelUnit) atk.origin.model).e).getOrbGood(atk.trait, traits, t);
			}

			return ((EUnit) ((AtkModelUnit)atk.model).e).getOrbGood(atk.trait, traits, t);
		}
	}

	public final int lvl;
	public final int[] index;

	protected final Level level;

	public final boolean isSpirit;
	public final boolean isOrbBoosted;
	public int legendGrade = -1, coloGrade = -1, counterGrade = -1;
	public Proc orbProc;

	public EUnit(StageBasis b, MaskUnit de, EAnimU ea, float d0, int layer0, int layer1, Level level, PCoin pc,
				 int[] index, boolean isSpirit, boolean isEveryOther) {
		super(b, de, ea, d0, b.b.t().getAtkMulti(), b.b.t().getDefMulti(), pc, level);
		layer = layer0 == layer1 ? layer0 : layer0 + (int) (b.r.nextFloat() * (layer1 - layer0 + 1));
		traits = de.getTraits();
		lvl = level.getLv() + level.getPlusLv();
		this.isOrbBoosted = isEveryOther;
		this.index = index;
		this.level = level;
		this.isSpirit = isSpirit;

		processAbilityOrbs();
	}

	public EUnit(StageBasis b, MaskUnit de, EAnimU ea, float d0) {
		super(b, de, ea, d0, b.b.t().getAtkMulti(), b.b.t().getDefMulti(), null, null);
		layer = de.getFront() + (int) (b.r.nextFloat() * (de.getBack() - de.getFront() + 1));
		traits = de.getTraits();
		this.index = null;

		lvl = 1;
		health = maxH = (int) (health * b.b.t().getCannonMagnification(BASE_WALL, BASE_WALL_MAGNIFICATION) / 100.0);
		level = null;
		isSpirit = false;
		isOrbBoosted = false;
	}

	private void processAbilityOrbs() {
		int[][] orbs = level.getOrbs();
		if (orbs == null)
			return;
		for (int[] orb : orbs) {
			if (orb.length != ORB_INTS)
				continue;
			int id = orb[0];
			if (id < ORB_DEATH_SURGE)
				continue;
			int grade = orb[2];
			if (id == ORB_SOL_BUFF && basis.est.s.getCont().getCont().getSID().equals("000000") || id == ORB_UL_BUFF && basis.est.s.getCont().getCont().getSID().equals("000013")) {
				legendGrade = Math.max(legendGrade, grade);
				continue;
			}
			if (orbProc == null)
				orbProc = getProc().clone();
			if (id == ORB_WAVE_RESIST) {
				orbProc.IMUWAVE.mult = Math.min(100, orbProc.IMUWAVE.mult + ORB_RESIST_MULT[grade]);
				continue;
			} else if (id == ORB_KB_RESIST) {
				orbProc.IMUKB.mult = Math.min(100, orbProc.IMUKB.mult + ORB_RESIST_MULT[grade]);
				continue;
			} else if (id == ORB_CURSE_RESIST) {
				orbProc.IMUCURSE.mult = Math.min(100, orbProc.IMUCURSE.mult + ORB_RESIST_MULT[grade]);
				continue;
			} else if (id == ORB_SLOW_RESIST) {
				orbProc.IMUSLOW.mult = Math.min(100, orbProc.IMUSLOW.mult + ORB_RESIST_MULT[grade]);
				continue;
			} else if (id == ORB_STOP_RESIST) {
				orbProc.IMUSTOP.mult = Math.min(100, orbProc.IMUSTOP.mult + ORB_RESIST_MULT[grade]);
				continue;
			} else if (id == ORB_WEAK_RESIST) {
				orbProc.IMUWEAK.mult = Math.min(100, orbProc.IMUWEAK.mult + ORB_RESIST_MULT[grade]);
				continue;
			} else if (id == ORB_VOLC_RESIST) {
				orbProc.IMUVOLC.mult = Math.min(100, orbProc.IMUVOLC.mult + ORB_RESIST_MULT[grade]);
				continue;
			} else if (id == ORB_BLAST_RESIST) {
				orbProc.IMUBLAST.mult = Math.min(100, orbProc.IMUBLAST.mult + ORB_RESIST_MULT[grade]);
				continue;
			}
			if (!isOrbBoosted)
				continue;
			if (id == ORB_DEATH_SURGE) {
				Proc.MINIVOLC surge = orbProc.MINIDEATHSURGE;
				if (!surge.exists()) {
					surge.prob = 100;
					orbProc.MINIDEATHSURGE.dis_0 = ORB_DEATH_SURGE_SPAWN_MIN;
					orbProc.MINIDEATHSURGE.dis_1 = ORB_DEATH_SURGE_SPAWN_MAX;
					orbProc.MINIDEATHSURGE.time = 20;
				}
				surge.mult = Math.max(surge.mult, ORB_DEATH_SURGE_MULT[grade]);
			} else if (id == ORB_MONEY_BACK)
				orbProc.MONEYBACK.mult += ORB_MONEY_BACK_MULT[grade];
			else if (id == ORB_CANNON_RECHARGE)
				orbProc.CANONCHARGE.mult = Math.max(orbProc.CANONCHARGE.mult, ORB_CANNON_RECHARGE_MULT[grade]);
			else if (id == ORB_BARON_KILLER)
				coloGrade = Math.max(coloGrade, grade);
			else if (id == ORB_IMUATK)
				orbProc.IMUATKANY.prob = Math.max(orbProc.IMUATKANY.prob, ORB_IMUATK_MULT[grade]);
			else if (id == ORB_SINGLE_COUNTER_SURGE)
				counterGrade = Math.max(counterGrade, grade);
		}
		if (legendGrade != -1)
			maxH = health = health * (100 + ORB_LEGEND_HEATLH[legendGrade]) / 100;
	}

	@Override
	public void kill(KillMode atk) {
		super.kill(atk);

		if (getProc().MONEYBACK.exists())
			basis.money += basis.elu.price[index[0]][index[1]] * getProc().MONEYBACK.mult / 100;
		if (getProc().CANONCHARGE.exists() && basis.cannon < basis.maxCannon - 1)
			basis.cannon = Math.min(basis.maxCannon - 1, basis.cannon + getProc().CANONCHARGE.mult);
	}

	@Override
	public int getAtk() { // visual only
		int atk = aam.getAtk();
		if (status[P_STRONG][0] != 0 && !basis.isBanned(C_STRONG))
			atk += atk * (status[P_STRONG][0] + basis.b.getInc(C_STRONG)) / 100;
		if (status[P_WEAK][0] > 0)
			atk = atk * status[P_WEAK][1] / 100;
		if (legendGrade != -1)
			atk = atk * (100 + ORB_LEGEND_ATTACK[legendGrade]) / 100;
		return atk;
	}

	@Override
	public void update() {
		super.update();

		traits = status[P_CURSE][0] == 0 && status[P_SEAL][0] == 0 ? data.getTraits() : new ArrayList<>();
		if (isSpirit && atkm.atkTime == 0)
			kill(KillMode.SPIRIT);
	}

	@Override
	public void added(int d, float p) {
		super.added(d, p);

		if (isSpirit)
			atkm.startAttack();
	}

	@Override
	public void damaged(AttackAb atk) {
		if (isSpirit) {
			status[P_IMUATK][0] = Integer.MAX_VALUE;
			anim.getEff(P_IMUATK);

			return;
		}

		if (atk instanceof AttackVolcano && counterGrade > -1) {
			AttackVolcano volc = (AttackVolcano) atk;

			if (volc.handler != null && !volc.handler.reflected && !volc.handler.surgeSummoned.contains(this)) {
				basis.lea.add(new SurgeSummoner(pos, layer, (dire == 1 ? effas().A_E_COUNTERSURGE : effas().A_COUNTERSURGE).getEAnim(EffAnim.DefEff.DEF),
						this, volc.handler.time, atk.waveType, volc.handler.startPoint, volc.handler.endPoint,
						ORB_SINGLE_COUNTER_SURGE_MULT[counterGrade]));
				basis.leaSort = true;
				volc.handler.surgeSummoned.add(this);
			}

			counterGrade = -1;
		}

		if (atk.trait.contains(UserProfile.getBCData().traits.get(TRAIT_BEAST))) {
			Proc.BSTHUNT beastDodge = getProc().BSTHUNT;

			if (beastDodge.prob > 0 && (atk.dire != dire)) {
				if (status[P_BSTHUNT][0] == 0 && beastDodge.perform(basis.r)) {
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
	public float getResistValue(AttackAb atk, String procName, int procResist) {
		float ans = 1f - procResist / 100f;

		boolean canBeApplied = false;

		for (int i = 0; i < SUPER_SAGE_RESIST_TYPE.length; i++) {
			if (procName.equals(SUPER_SAGE_RESIST_TYPE[i])) {
				canBeApplied = true;

				break;
			}
		}

		if (atk.trait.contains(UserProfile.getBCData().traits.get(TRAIT_SAGE)) && canBeApplied && (getAbi() & AB_SKILL) != 0) {
			ans *= (1f - SUPER_SAGE_HUNTER_RESIST);
		}

		return ans;
	}

	@Override
	protected int getDamage(AttackAb atk, int ans) {
		if (atk instanceof AttackWave && atk.waveType == WT_MINI)
			ans = (int) ((float) ans * atk.getProc().MINIWAVE.multi / 100.0);
		if (atk instanceof AttackVolcano && (atk.waveType & WT_MIVC) > 0)
			if ((atk.waveType & WT_SOUL) > 0)
				ans = (int) (ans * atk.attacker.getProc().MINIDEATHSURGE.mult / 100f);
			else
				ans = (int) (ans * atk.getProc().MINIVOLC.mult / 100f);

		if (atk.model instanceof AtkModelEnemy && status[P_CURSE][0] == 0) {
			ArrayList<Trait> sharedTraits = new ArrayList<>(atk.trait);
			sharedTraits.retainAll(traits);
			boolean isAntiTraited = targetTraited(atk.trait);
			for (Trait t : traits) {
				if (t.id.pack.equals("000000") || sharedTraits.contains(t))
					continue;
				if ((t.targetType && isAntiTraited) || t.targetForms.contains(((MaskUnit)data).getPack()))
					sharedTraits.add(t);
			}

			if ((getAbi() & AB_GOOD) != 0)
				ans = (int) (ans * basis.b.t().getGOODDEF(atk.trait, sharedTraits, ((MaskUnit)data).getOrb(), level, basis.isBanned(C_GOOD)));
			if ((getAbi() & AB_RESIST) != 0)
				ans = (int) (ans * basis.b.t().getRESISTDEF(atk.trait, sharedTraits, ((MaskUnit)data).getOrb(), level, basis.isBanned(Data.C_RESIST)));
			if (!sharedTraits.isEmpty() && (getAbi() & AB_RESISTS) != 0)
				ans = (int) (ans * basis.b.t().getRESISTSDEF(sharedTraits));
		}

		if (atk.trait.contains(UserProfile.getBCData().traits.get(TRAIT_WITCH)) && (getAbi() & AB_WKILL) > 0)
			ans = (int) (ans * basis.b.t().getWKDef(basis.isBanned(Data.C_WKILL)));
		if (atk.trait.contains(UserProfile.getBCData().traits.get(TRAIT_EVA)) && (getAbi() & AB_EKILL) > 0)
			ans = (int) (ans * basis.b.t().getEKDef(basis.isBanned(Data.C_EKILL)));

		if (isBase)
			ans = (int) (ans * (1 + atk.getProc().ATKBASE.mult / 100.0));

		if (atk.trait.contains(UserProfile.getBCData().traits.get(TRAIT_BARON))) {
			if ((getAbi() & AB_BAKILL) > 0)
				ans = (int) (ans * 0.7);
			else if (coloGrade != -1)
				ans = ans * ORB_BARON_DEFENSE[coloGrade] / 100;
		}
		if (atk.trait.contains(UserProfile.getBCData().traits.get(Data.TRAIT_BEAST)) && getProc().BSTHUNT.active > 0)
			ans = (int) (ans * 0.6);
		if (atk.trait.contains(UserProfile.getBCData().traits.get(Data.TRAIT_SAGE)) && (getAbi() & AB_SKILL) > 0)
			ans = (int) (ans * SUPER_SAGE_HUNTER_HP);

		// Perform orb
		ans = getOrbRes(atk.trait, ans);

		if(basis.canon.base > 0) {
			ans = (int) (ans * basis.b.t().getBaseMagnification(basis.canon.base, atk.trait));
		}

		ans = critCalc((getAbi() & AB_METALIC) != 0, ans, atk);

		return ans;
	}

	@Override
	protected float getLim() {
		return Math.max(0, basis.st.len - pos - ((MaskUnit) data).getLimit());
	}

	@Override
	protected int traitType() {
		return -1;
	}

	@Override
	protected void updateMove(float extmov) {
		int speed = data.getSpeed();
		extmov = (float) ((speed > 0 && basis.getGlobalSpeed(-1, speed) > -1 ? basis.getGlobalSpeed(-1, speed) : data.getSpeed()) * basis.b.getInc(C_SPE) / 50) / 4f;
		super.updateMove(extmov);
	}

	private int getOrbAtk(ArrayList<Trait> trait, MaskAtk matk) {
		OrbInfo orb = ((MaskUnit) data).getOrb();

		if (orb == null || level.getOrbs() == null) {
			return 0;
		}

		int ans = 0;

		for (int[] line : level.getOrbs()) {
			if (line.length == 0)
				continue;
			if (line[ORB_TYPE] != ORB_ATK)
				continue;

			List<Trait> orbType = Trait.convertOrb(line[ORB_TRAIT]);

			boolean orbValid = false;

			for(int i = 0; i < orbType.size(); i++) {
				if (trait.contains(orbType.get(i))) {
					orbValid = true;

					break;
				}
			}

			if (!orbValid)
				continue;

			ans += orb.getAtk(line[ORB_GRADE], matk);
		}

		return ans;
	}

	private int getOrbRes(ArrayList<Trait> trait, int atk) {
		OrbInfo orb = ((MaskUnit) data).getOrb();

		if (orb == null || level == null || level.getOrbs() == null)
			return atk;

		int ans = atk;

		for (int[] line : level.getOrbs()) {
			if (line.length == 0 || line[ORB_TYPE] != Data.ORB_RES)
				continue;

			List<Trait> orbType = Trait.convertOrb(line[ORB_TRAIT]);

			boolean orbValid = false;

			for(int i = 0; i < orbType.size(); i++) {
				if (trait.contains(orbType.get(i))) {
					orbValid = true;

					break;
				}
			}

			if (!orbValid)
				continue;

			ans = orb.getRes(line[ORB_GRADE], ans);
		}

		return ans;
	}

	private float getOrbMassive(ArrayList<Trait> eTraits, ArrayList<Trait> traits, Treasure t) {
		float ini = 1;

		if (!traits.isEmpty())
			ini = 3 + 1f / 3 * t.getFruit(traits);

		OrbInfo orbs = ((MaskUnit)data).getOrb();

		if(orbs != null && level.getOrbs() != null) {
			int[][] levelOrbs = level.getOrbs();

			for(int i = 0; i < levelOrbs.length; i++) {
				if (levelOrbs[i].length < ORB_INTS)
					continue;

				if (levelOrbs[i][ORB_TYPE] == ORB_MASSIVE) {
					List<Trait> orbType = Trait.convertOrb(levelOrbs[i][ORB_TRAIT]);

					for(int j = 0; j < orbType.size(); j++) {
						if (eTraits.contains(orbType.get(j))) {
							ini += ORB_MASSIVE_MULTI[levelOrbs[i][ORB_GRADE]];

							break;
						}
					}
				}
			}
		}

		if (ini == 1)
			return ini;

		float com = 1 + t.b.getInc(C_MASSIVE) * 0.01f;

		return ini * com;
	}

	private float getOrbGood(ArrayList<Trait> eTraits, ArrayList<Trait> traits, Treasure t) {
		float ini = 1;

		if (!traits.isEmpty())
			ini = 1.5f * (1 + 0.2f / 3 * t.getFruit(traits));

		OrbInfo orbs = ((MaskUnit)data).getOrb();

		if(orbs != null && level.getOrbs() != null) {
			int[][] levelOrbs = level.getOrbs();

			for (int i = 0; i < levelOrbs.length; i++) {
				if (levelOrbs[i].length < ORB_INTS)
						continue;

				if (levelOrbs[i][ORB_TYPE] == ORB_STRONG) {
					List<Trait> orbType = Trait.convertOrb(levelOrbs[i][ORB_TRAIT]);

					for(int j = 0; j < orbType.size(); j++) {
						if (eTraits.contains(orbType.get(j))) {
							ini += ORB_STR_ATK_MULTI[levelOrbs[i][ORB_GRADE]];

							break;
						}
					}
				}
			}
		}

		if (ini == 1)
			return ini;

		float com = 1 + t.b.getInc(C_GOOD) * 0.01f;
		return ini * com;
	}

	@Override
	protected void onLastBreathe() {
		basis.notifyUnitDeath();
	}

	@Override
	public Proc getProc() {
		return orbProc != null ? orbProc : super.getProc();
	}
}
