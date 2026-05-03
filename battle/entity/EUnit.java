package common.battle.entity;

import common.battle.StageBasis;
import common.battle.Treasure;
import common.battle.attack.*;
import common.battle.data.MaskAtk;
import common.battle.data.MaskUnit;
import common.battle.data.PCoin;
import common.pack.UserProfile;
import common.util.BattleObj;
import common.util.Data;
import common.util.anim.EAnimU;
import common.util.pack.EffAnim;
import common.util.stage.StageLimit;
import common.util.unit.Level;
import common.util.unit.Trait;
import common.util.unit.Unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class EUnit extends Entity {

	public static class OrbHandler extends BattleObj {
		private static EUnit getEUnit(AttackAb atk) {
			return atk.origin.model instanceof AtkModelUnit ? (EUnit) ((AtkModelUnit) atk.origin.model).e : (EUnit) ((AtkModelUnit) atk.model).e;
		}

		protected static int getOrbAtk(AttackAb atk, EEnemy en) {
			if (atk.matk == null) {
				return 0;
			}

			if (atk.origin.model instanceof AtkModelUnit) {
				// Warning : Eunit.e became public now
				return getEUnit(atk).getOrbAtk(en.traits, atk.matk);
			}

			return 0;
		}

		protected static float getOrbMassive(AttackAb atk, ArrayList<Trait> traits, Treasure t) {
			EUnit eu = getEUnit(atk);
			return eu.getOrbMassive(atk.trait, traits, t, eu.basis.b.getInc(Data.C_MASSIVE, ((MaskUnit) eu.data).getPack().unit));
		}

		protected static float getOrbGood(AttackAb atk, ArrayList<Trait> traits, Treasure t) {
			EUnit eu = getEUnit(atk);
			return eu.getOrbGood(atk.trait, traits, t, eu.basis.b.getInc(Data.C_GOOD, ((MaskUnit) eu.data).getPack().unit));
		}
	}

	public final int lvl;
	public final int[] index;
	public final int[] inc = new int[C_TOT];

	protected final Level level;

	public final boolean isSpirit;
	public final boolean isOrbBoosted;
	public boolean bountyOrbCheck = false;
	public int legendGrade = -1, coloGrade = -1, counterGrade = -1, bountyGrade = -1;

	public EUnit(StageBasis b, MaskUnit de, EAnimU ea, float d0, int layer0, int layer1, Level level, PCoin pc,
				 int[] index, boolean isSpirit, boolean isEveryOther) {
		super(b, de, ea, d0, b.b.t().getAtkMulti(), b.b.t().getDefMulti(), pc, level);
		currentLayer = spawnLayer = layer0 == layer1 ? layer0 : layer0 + (int) (b.r.nextFloat() * (layer1 - layer0 + 1));
		traits = de.getTraits();
		lvl = level.getLv() + level.getPlusLv();
		this.isOrbBoosted = isEveryOther;
		this.index = index;
		this.level = level;
		this.isSpirit = isSpirit;

		processAbilityOrbs();
		processComboAbilities();
	}

	public EUnit(StageBasis b, MaskUnit de, EAnimU ea, float d0) {
		super(b, de, ea, d0, b.b.t().getAtkMulti(), b.b.t().getDefMulti(), null, null);
		currentLayer = spawnLayer = de.getFront() + (int) (b.r.nextFloat() * (de.getBack() - de.getFront() + 1));
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
			if (id == ORB_WAVE_RESIST) {
				proc.IMUWAVE.mult = Math.min(100, proc.IMUWAVE.mult + ORB_RESIST_MULT[grade]);
				continue;
			} else if (id == ORB_KB_RESIST) {
				proc.IMUKB.mult = Math.min(100, proc.IMUKB.mult + ORB_RESIST_MULT[grade]);
				continue;
			} else if (id == ORB_CURSE_RESIST) {
				proc.IMUCURSE.mult = Math.min(100, proc.IMUCURSE.mult + ORB_RESIST_MULT[grade]);
				continue;
			} else if (id == ORB_SLOW_RESIST) {
				proc.IMUSLOW.mult = Math.min(100, proc.IMUSLOW.mult + ORB_RESIST_MULT[grade]);
				continue;
			} else if (id == ORB_STOP_RESIST) {
				proc.IMUSTOP.mult = Math.min(100, proc.IMUSTOP.mult + ORB_RESIST_MULT[grade]);
				continue;
			} else if (id == ORB_WEAK_RESIST) {
				proc.IMUWEAK.mult = Math.min(100, proc.IMUWEAK.mult + ORB_RESIST_MULT[grade]);
				continue;
			} else if (id == ORB_VOLC_RESIST) {
				proc.IMUVOLC.mult = Math.min(100, proc.IMUVOLC.mult + ORB_RESIST_MULT[grade]);
				continue;
			} else if (id == ORB_BLAST_RESIST) {
				proc.IMUBLAST.mult = Math.min(100, proc.IMUBLAST.mult + ORB_RESIST_MULT[grade]);
				continue;
			} else if (id == ORB_BERSERKER) {
				proc.BERSERK.killCount = 10;
				proc.BERSERK.mult = Math.max(proc.BERSERK.mult, ORB_BERSERKER_MULT[grade]);
				continue;
			}

			if (!isOrbBoosted)
				continue;
			if (id == ORB_DEATH_SURGE) {
				Proc.MINIVOLC surge = proc.MINIDEATHSURGE;
				if (!surge.exists()) {
					surge.prob = 100;
					proc.MINIDEATHSURGE.dis_0 = ORB_DEATH_SURGE_SPAWN_MIN;
					proc.MINIDEATHSURGE.dis_1 = ORB_DEATH_SURGE_SPAWN_MAX;
					proc.MINIDEATHSURGE.time = 20;
				}
				surge.mult = Math.max(surge.mult, ORB_DEATH_SURGE_MULT[grade]);
			} else if (id == ORB_MONEY_BACK)
				proc.MONEYBACK.mult += ORB_MONEY_BACK_MULT[grade];
			else if (id == ORB_CANNON_RECHARGE)
				proc.CANONCHARGE.mult = Math.max(proc.CANONCHARGE.mult, ORB_CANNON_RECHARGE_MULT[grade]);
			else if (id == ORB_BARON_KILLER)
				coloGrade = Math.max(coloGrade, grade);
			else if (id == ORB_IMUATK)
				proc.IMUATKANY.prob = Math.max(proc.IMUATKANY.prob, ORB_IMUATK_MULT[grade]);
			else if (id == ORB_SINGLE_COUNTER_SURGE && (getAbi() & AB_CSUR) == 0)
				counterGrade = Math.max(counterGrade, grade);
			else if (id == ORB_BOUNTY)
				bountyGrade = Math.max(bountyGrade, grade);
		}
		if (legendGrade != -1)
			maxH = health = health * (100 + ORB_LEGEND_HEATLH[legendGrade]) / 100;
	}

	private void processComboAbilities() {
		Unit u = ((MaskUnit) data).getPack().unit;
		if (!StageLimit.isComboBanned(basis.est.lim, C_IMUWAVE) && basis.b.getInc(C_IMUWAVE, u) > 0)
			proc.IMUWAVE.mult = 100;
		if (!StageLimit.isComboBanned(basis.est.lim, C_IMUVOLC) && basis.b.getInc(C_IMUVOLC, u) > 0)
			proc.IMUVOLC.mult = 100;
	}

	@Override
	public int getAbi() {
		int abi = super.getAbi();
		Unit u = ((MaskUnit) data).getPack().unit;
		if (!StageLimit.isComboBanned(basis.est.lim, C_VKILL) && basis.b.getInc(C_VKILL, u) > 0)
			abi |= AB_VKILL;
		return abi;
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
		if (status[P_STRONG][0] != 0 && !StageLimit.isComboBanned(basis.est.lim, C_STRONG))
			atk += atk * (status[P_STRONG][0] + basis.b.getInc(C_STRONG, ((MaskUnit) data).getPack().unit)) / 100;
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
	public boolean damaged(AttackAb atk) {
		if (isSpirit) {
			status[P_IMUATK][0] = Integer.MAX_VALUE;
			anim.getEff(P_IMUATK);

			return false;
		}

		if (atk instanceof AttackVolcano && counterGrade > -1) {
			AttackVolcano volc = (AttackVolcano) atk;

			if (volc.handler != null && !volc.handler.reflected && !volc.handler.surgeSummoned.contains(this)) {
				basis.lea.add(new SurgeSummoner(pos, currentLayer, (dire == 1 ? effas().A_E_COUNTERSURGE : effas().A_COUNTERSURGE).getEAnim(EffAnim.DefEff.DEF),
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

					return false;
				}
			}
		}

		boolean damaged = super.damaged(atk);

		if (index != null)
			basis.totalDamageTaken[index[0]][index[1]] += atk.atk;

		return damaged;
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
		MaskUnit mu = ((MaskUnit) data);
		if (atk instanceof AttackWave && atk.waveType == WT_MINI)
			ans = (int) ((float) ans * atk.getProc().MINIWAVE.multi / 100.0);
		if (atk instanceof AttackVolcano && (atk.waveType & WT_MIVC) > 0)
			if ((atk.waveType & WT_SOUL) > 0)
				ans = (int) (ans * atk.attacker.getProc().MINIDEATHSURGE.mult / 100f);
			else
				ans = (int) (ans * atk.getProc().MINIVOLC.mult / 100f);

		if (atk.model instanceof AtkModelEnemy && status[P_CURSE][0] == 0) {
			List<Trait> sharedTraits = new ArrayList<>(atk.trait); // get traits of enemy
			sharedTraits.retainAll(traits); // keep
			boolean isAntiTraited = Trait.isTargetTraited(atk.trait);
			for (Trait t : traits) {
				if (t.id.pack.equals("000000") || sharedTraits.contains(t))
					continue;
				if (t.targetType && isAntiTraited)
					sharedTraits.add(t);
			}

			if ((getAbi() & AB_GOOD) != 0) {
				ans = (int) (ans * basis.b.t().getGOODDEF(atk.trait, sharedTraits, level,
						StageLimit.isComboBanned(basis.est.lim, C_GOOD) ? 0 : basis.b.getInc(C_GOOD, mu.getPack().unit)));
				if (!sharedTraits.isEmpty())
					basis.scoreActivated(SCORE_GOOD, -1, traits.size());
			}
			if ((getAbi() & AB_RESIST) != 0) {
				ans = (int) (ans * basis.b.t().getRESISTDEF(atk.trait, sharedTraits, level,
						StageLimit.isComboBanned(basis.est.lim, Data.C_RESIST) ? 0 : basis.b.getInc(Data.C_RESIST, mu.getPack().unit)));
				if (!sharedTraits.isEmpty())
					basis.scoreActivated(SCORE_RESIST, -1, traits.size());
			}
			if (!sharedTraits.isEmpty() && (getAbi() & AB_RESISTS) != 0) {
				ans = (int) (ans * basis.b.t().getRESISTSDEF(sharedTraits));
				basis.scoreActivated(SCORE_RESISTS, -1, traits.size());
			}
		}

		if (atk.trait.contains(UserProfile.getBCData().traits.get(TRAIT_WITCH)) && (getAbi() & AB_WKILL) > 0)
			ans = (int) (ans * basis.b.t().getWKDef(StageLimit.isComboBanned(basis.est.lim, Data.C_WKILL) ? 0 : basis.b.getInc(Data.C_WKILL, mu.getPack().unit)));
		if (atk.trait.contains(UserProfile.getBCData().traits.get(TRAIT_EVA)) && (getAbi() & AB_EKILL) > 0)
			ans = (int) (ans * basis.b.t().getEKDef(StageLimit.isComboBanned(basis.est.lim, Data.C_EKILL) ? 0 : basis.b.getInc(Data.C_EKILL, mu.getPack().unit)));

		if (isBase)
			ans = (int) (ans * (1 + atk.getProc().ATKBASE.mult / 100.0));

		if (atk.trait.contains(UserProfile.getBCData().traits.get(TRAIT_BARON))) {
			if ((getAbi() & AB_BAKILL) > 0)
				ans = (int) (ans * 0.7);
			else if (coloGrade != -1)
				ans = ans * ORB_BARON_DEFENSE[coloGrade] / 100;
		}
		if (atk.trait.contains(UserProfile.getBCData().traits.get(TRAIT_BEAST)) && getProc().BSTHUNT.active > 0)
			ans = (int) (ans * 0.6);
		if (atk.trait.contains(UserProfile.getBCData().traits.get(TRAIT_SAGE)) && (getAbi() & AB_SKILL) > 0)
			ans = (int) (ans * SUPER_SAGE_HUNTER_HP);
		if (atk.trait.contains(UserProfile.getBCData().traits.get(TRAIT_VILLAIN)) && (getAbi() & AB_VKILL) > 0)
			ans = (int) (ans * VILLAIN_KILLER_RESIST);

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
		extmov += (float) ((speed > 0 && basis.getGlobalSpeed(-1, speed) > -1 ? basis.getGlobalSpeed(-1, speed) : data.getSpeed())
				* basis.b.getInc(C_SPE, ((MaskUnit) data).getPack().unit) / 50);
		super.updateMove(extmov / 4f);
	}

	private int getOrbAtk(ArrayList<Trait> trait, MaskAtk matk) {
		if (level.getOrbs() == null)
			return 0;

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

			ans += ORB_ATK_MULTI[line[ORB_GRADE]] * matk.getAtk() / 100;
		}

		return ans;
	}

	private int getOrbRes(List<Trait> trait, int atk) {
		if (level == null || level.getOrbs() == null)
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
			ans = (100 - ORB_RES_MULTI[line[ORB_GRADE]]) * ans / 100;
		}

		return ans;
	}

	private float getOrbMassive(List<Trait> eTraits, List<Trait> traits, Treasure t, int comboInc) {
		float ini = 1;

		if (!traits.isEmpty())
			ini = 3 + 1f / 3 * t.getFruit(traits);

		if(level.getOrbs() != null) {
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

		float com = 1 + comboInc * 0.01f;

		return ini * com;
	}

	private float getOrbGood(List<Trait> eTraits, List<Trait> traits, Treasure t, int comboInc) {
		float ini = 1;

		if (!traits.isEmpty())
			ini = 1.5f * (1 + 0.2f / 3 * t.getFruit(traits));

		if(level.getOrbs() != null) {
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

		float com = 1 + comboInc * 0.01f;
		return ini * com;
	}

	@Override
	protected void onLastBreathe() {
		basis.notifyUnitDeath();
	}

	@Override
	public void postUpdate() {
		if (Arrays.stream(status[P_DELAY]).anyMatch(v -> v != 0)) {
			for (int i = 0; i < 3; i++) {
				basis.cdDelay[index[0]][index[1]][i] += status[P_DELAY][i];
				status[P_DELAY][i] = 0;
			}
		}

		super.postUpdate();

		if (bountyGrade > -1 && bountyOrbCheck)
			bountyGrade = -1;
	}

	@Override
	public boolean processProcs(AttackAb atk) {
		boolean doCheck = super.processProcs(atk);
		if (!doCheck)
			return false;
		Proc atkProc = atk.getProc();

		if (atkProc.DELAY.exists() && index != null && basis.elu.cool[index[0]][index[1]] > 0) {
			Proc.DELAY d = atkProc.DELAY;
			Proc.IMUAD imu = getProc().IMUDELAY;
			float res;
			if (Proc.checkSmartImu(d.strength, imu.smartImu, imu.mult < 0))
				res = getResistValue(atk, "IMUDELAY", imu.mult);
			else
				res = 0;
			if (res < 100) {
				int strength = (int) (d.strength * res);
				if (strength != 0) {
					status[P_DELAY][d.type] += strength;
					basis.lea.add(new EAnimCont(pos, currentLayer, effas().A_E_DELAY.getEAnim(EffAnim.DefEff.DEF), -50f));
					basis.leaSort = true;
				}
			} else {
				anim.getEff(INV);
			}
		}

		return true;
	}
}
