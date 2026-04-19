package common.battle.entity;

import common.battle.StageBasis;
import common.battle.attack.*;
import common.battle.data.MaskEnemy;
import common.battle.data.MaskUnit;
import common.pack.UserProfile;
import common.util.Data;
import common.util.anim.EAnimU;
import common.util.pack.EffAnim;
import common.util.stage.SCDef;
import common.util.stage.StageLimit;
import common.util.unit.Form;
import common.util.unit.Trait;
import common.util.unit.Unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EEnemy extends Entity {

	public final int mark;
	public final double mult, mula;
	public final int line;

	public byte hit;

	public EEnemy(StageBasis b, MaskEnemy de, EAnimU ea, float magnif, float atkMagnif, int d0, int d1, int m, int l) {
		super(b, de, ea, atkMagnif, magnif);
		mult = magnif;
		mula = atkMagnif;
		mark = m;
		line = l;
		isBase = mark <= -1;
		currentLayer = spawnLayer = d0 == d1 ? d0 : d0 + (int) (b.r.nextFloat() * (d1 - d0 + 1));
		traits = de.getTraits();

		skipSpawnBurrow = mark >= 1;
	}

	@Override
	public int getAtk() {
		int atk = aam.getAtk();
		if (status[P_STRONG][0] != 0)
			atk += atk * status[P_STRONG][0] / 100;
		if (status[P_STRONG][1] != 0)
			atk += atk * status[P_STRONG][1] / 100;
		if (status[P_WEAK][0] > 0)
			atk = atk * status[P_WEAK][1] / 100;
		return atk;
	}

	@Override
	public void kill(KillMode atk) {
		super.kill(atk);

		if (basis.st.drop && atk == KillMode.NORMAL) {
			List<Unit> unitsHit = new ArrayList<>();
			for (AttackAb attack : lastKilledBy) {
				if (!(attack.attacker instanceof EUnit))
					continue;
				EUnit u = (EUnit) attack.attacker;
				unitsHit.add(((Form) u.data.getPack()).unit);

				if (!(attack instanceof AttackSimple))
					continue;
				if (u.bountyGrade != -1) { // todo: verify what happens if two bounty orb cats kill one enemy at the same time in BC
					status[P_BOUNTY][0] += ORB_SINGLE_BOUNTY_MULT[u.bountyGrade];
					u.bountyOrbCheck = true;
				}
			}
			float mul = basis.b.t().getDropMulti()
					* (1 + (StageLimit.isComboBanned(basis.est.lim, Data.C_MEAR) ? 0 : basis.b.getInc(Data.C_MEAR, unitsHit)) * 0.01f)
					* (1 + (status[P_BOUNTY][0] / 100f));
			basis.money = (int) (basis.money + mul * ((MaskEnemy) data).getDrop());
		}
		if (basis.st.trail && !basis.isDojoOvertime() && basis.isActive() && atk == KillMode.NORMAL) {
			SCDef.Line d = basis.st.data.getSimple(line);
			int time = basis.st.timeLimit * 1800;
			int score = (int) (((MaskEnemy) data).getDrop() / 100f + (d.score * (2f * time - basis.time)) / time);
			basis.score += score;
		}
	}

	@Override
	protected int getDamage(AttackAb atk, int ans) {
		if (atk instanceof AttackWave && atk.waveType == WT_MINI)
			ans = (int) (ans * atk.getProc().MINIWAVE.multi / 100f);

		if (atk instanceof AttackVolcano && (atk.waveType & WT_MIVC) > 0)
			if ((atk.waveType & WT_SOUL) > 0)
				ans = (int) (ans * atk.attacker.getProc().MINIDEATHSURGE.mult / 100f);
			else
				ans = (int) (ans * atk.getProc().MINIVOLC.mult / 100f);

		if (atk.model instanceof AtkModelUnit && atk.attacker.status[P_CURSE][0] == 0) {
			ArrayList<Trait> sharedTraits = new ArrayList<>(atk.trait);

			sharedTraits.retainAll(traits);

			boolean isAntiTraited = Trait.isTargetTraited(atk.trait);

			for (Trait t : traits) {
				if (t.id.pack.equals("000000") || sharedTraits.contains(t))
					continue;

				if ((t.targetType && isAntiTraited) || t.targetForms.contains(((MaskUnit)atk.attacker.data).getPack()))
					sharedTraits.add(t);
			}

			if (!sharedTraits.isEmpty() && (atk.abi & AB_GOOD) != 0) {
				ans = (int) (ans * EUnit.OrbHandler.getOrbGood(atk, sharedTraits, basis.b.t()));
				basis.scoreActivated(SCORE_GOOD, 1, atk.trait.size());
			}

			if (!sharedTraits.isEmpty() && (atk.abi & AB_MASSIVE) != 0) {
				ans = (int) (ans * EUnit.OrbHandler.getOrbMassive(atk, sharedTraits, basis.b.t()));
				basis.scoreActivated(SCORE_MASSIVE, 1, atk.trait.size());
			}

			if (!sharedTraits.isEmpty() && (atk.abi & AB_MASSIVES) != 0) {
				ans = (int) (ans * basis.b.t().getMASSIVESATK(sharedTraits));
				basis.scoreActivated(SCORE_MASSIVES, 1, atk.trait.size());
			}
		}

		if (isBase)
			ans = (int) (ans * (1 + atk.getProc().ATKBASE.mult / 100.0));

		if (traits.contains(UserProfile.getBCData().traits.get(TRAIT_WITCH)) && (atk.abi & AB_WKILL) > 0)
			ans = (int) (ans * basis.b.t().getWKAtk(StageLimit.isComboBanned(basis.est.lim, Data.C_WKILL) ? 0 : basis.b.getInc(Data.C_WKILL, ((Form) atk.attacker.data.getPack()).unit)));
		if (traits.contains(UserProfile.getBCData().traits.get(TRAIT_EVA)) && (atk.abi & AB_EKILL) > 0)
			ans = (int) (ans * basis.b.t().getEKAtk(StageLimit.isComboBanned(basis.est.lim, Data.C_EKILL) ? 0 : basis.b.getInc(Data.C_EKILL, ((Form) atk.attacker.data.getPack()).unit)));

		if (traits.contains(UserProfile.getBCData().traits.get(TRAIT_BARON))) {
			if ((atk.abi & AB_BAKILL) > 0)
				ans = (int) (ans * 1.6);
			if (atk.attacker instanceof EUnit && ((EUnit) atk.attacker).coloGrade != -1)
				ans = ans * ORB_BARON_DAMAGE[((EUnit) atk.attacker).coloGrade] / 100;
		}
		if (traits.contains(UserProfile.getBCData().traits.get(TRAIT_BEAST)) && atk.getProc().BSTHUNT.active == 1)
			ans = (int) (ans * 2.5);
		if (traits.contains(UserProfile.getBCData().traits.get(TRAIT_SAGE)) && (atk.abi & AB_SKILL) > 0)
			ans = (int) (ans * SUPER_SAGE_HUNTER_ATTACK);
		if (traits.contains(UserProfile.getBCData().traits.get(TRAIT_VILLAIN)) && (atk.abi & AB_VKILL) > 0)
			ans = (int) (ans * VILLAIN_KILLER_ATTACK);
		if (atk.canon == 16)
			if ((touchable() & TCH_UG) > 0)
				ans = (int) (maxH * basis.b.t().getCannonMagnification(5, BASE_HOLY_ATK_UNDERGROUND));
			else
				ans = (int) (maxH * basis.b.t().getCannonMagnification(5, BASE_HOLY_ATK_SURFACE));

		ans = critCalc(data.getTraits().contains(UserProfile.getBCData().traits.get(TRAIT_METAL)), ans, atk);

		// Perform Orb
		ans += EUnit.OrbHandler.getOrbAtk(atk, this);

		return ans;
	}

	@Override
	protected float getLim() {
		float ans;
		float minPos = ((MaskEnemy) data).getLimit();

		if (mark >= 1)
			ans = pos - (minPos + basis.boss_spawn); // guessed value compared to BC
		else
			ans = pos - minPos;
		return Math.max(0, ans);
	}

	@Override
	protected int traitType() {
		return 1;
	}

	@Override
	public boolean damaged(AttackAb atk) {
		hit = 2;
		return super.damaged(atk);
	}

	@Override
	public float getResistValue(AttackAb atk, String procName, int procResist) {
		float ans = 1f - procResist / 100f;
		int resistMultiplier = 0;

		if (atk.canon > 0 && getProc().IMUCANNON.exists() && (atk.canon & getProc().IMUCANNON.type) > 0)
			resistMultiplier += getProc().IMUCANNON.mult;
		if ((atk.abi & AB_SKILL) == 0 && traits.contains(UserProfile.getBCData().traits.get(TRAIT_SAGE)) && Arrays.asList(SUPER_SAGE_RESIST_TYPE).contains(procName))
			resistMultiplier += SUPER_SAGE_RESIST;

		return ans * (100 - Math.min(100, resistMultiplier)) / 100;
	}

	@Override
	public void update() {
		if(hit > 0) {
			hit--;
		}

		super.update();
	}

	@Override
	public boolean processProcs(AttackAb atk) {
		boolean doCheck = super.processProcs(atk);
		if (!doCheck)
			return false;
		Proc atkProc = atk.getProc();

		if (atkProc.DELAY.exists() && line != -1 && basis.est.num[line] >= 0 && basis.est.rem[line] > 0) {
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
				basis.scoreActivated(P_DELAY, 1, atk.trait.size());
			} else {
				anim.getEff(INV);
			}
		}

		return true;
	}

	@Override
	public void postUpdate() {
		if (Arrays.stream(status[P_DELAY]).anyMatch(v -> v != 0)) {
			for (int i = 0; i < 3; i++) {
				basis.lineDelay[line][i] = status[P_DELAY][i];
				status[P_DELAY][i] = 0;
			}
		}

		super.postUpdate();

		if (health > 0)
			status[P_BOUNTY][0] = 0;
	}

	@Override
	protected void onLastBreathe() {
	}
}
