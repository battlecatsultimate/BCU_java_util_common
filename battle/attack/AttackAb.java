package common.battle.attack;

import common.battle.data.MaskAtk;
import common.battle.entity.AbEntity;
import common.battle.entity.Entity;
import common.util.BattleObj;
import common.util.unit.Trait;

import java.util.ArrayList;
import java.util.List;

public abstract class AttackAb extends BattleObj {

	public final int atk, abi;
	public final ArrayList<Trait> trait;
	public final AtkModelAb model;
	public final AttackAb origin;
	public final MaskAtk matk;
	public final int layer;
	public final boolean isLongAtk, SPtr;
	public int duration;

	public int touch = TCH_N, dire, canon = -2, waveType = 0;

	protected final Proc proc;
	protected final List<AbEntity> capt = new ArrayList<>();
	protected double sta, end;

	protected AttackAb(AtkModelAb ent, int ATK, ArrayList<Trait> tr, int eab, Proc pro, double p0, double p1, MaskAtk matk, int layer, boolean isLongAtk, int time) {
		dire = ent.getDire();
		origin = this;
		model = ent;
		trait = tr;
		atk = ATK;
		proc = pro;
		abi = eab;
		sta = p0;
		end = p1;
		this.duration = time;
		this.matk = matk;
		this.layer = layer;
		this.isLongAtk = isLongAtk;
		SPtr = matk != null && matk.getSPtrait();
	}

	protected AttackAb(AttackAb a, double STA, double END, boolean isLongAtk) {
		dire = a.dire;
		origin = a.origin;
		model = a.model;
		atk = a.atk;
		abi = a.abi;
		trait = a.trait;
		proc = a.proc;
		touch = a.touch;
		canon = a.canon;
		sta = STA;
		end = END;
		this.duration = 1;
		this.matk = a.matk;
		this.layer = a.layer;
		this.isLongAtk = isLongAtk;
		SPtr = a.SPtr;
	}

	/**
	 * capture the entities
	 */
	public abstract void capture();

	/**
	 * apply this attack to the entities captured
	 */
	public abstract void excuse();

	public Proc getProc() {
		return proc;
	}

	protected void process() {
		duration--;
		for (AbEntity ae : capt) {
			if (ae instanceof Entity) {
				Entity e = (Entity) ae;
				Proc imus = e.getProc();
				if (proc.KB.dis > 0) {
					if (imus.IMUKB.block > 0)
						e.anim.getEff(STPWAVE);
					if (imus.IMUKB.block == 100)
						proc.KB.clear();
					else
						proc.KB.dis *= (100 - imus.IMUKB.block) / 100.0;
				}
				if (proc.SLOW.time > 0) {
					if (imus.IMUSLOW.block > 0)
						e.anim.getEff(STPWAVE);
					if (imus.IMUSLOW.block == 100)
						proc.SLOW.clear();
					else
						proc.SLOW.time *= (100 - imus.IMUSLOW.block) / 100.0;
				}
				if (proc.STOP.time > 0) {
					if (imus.IMUSTOP.block > 0)
						e.anim.getEff(STPWAVE);
					if (imus.IMUSTOP.block == 100)
						proc.STOP.clear();
					else
						proc.STOP.time *= (100 - imus.IMUSTOP.block) / 100.0;
				}
				if (proc.WEAK.time > 0 && (proc.WEAK.mult - 100) * imus.IMUWEAK.smartImu <= 0) {
					if (imus.IMUWEAK.block > 0)
						e.anim.getEff(STPWAVE);
					if (imus.IMUWEAK.block == 100)
						proc.WEAK.clear();
					else
						proc.WEAK.time *= (100 - imus.IMUWEAK.block) / 100.0;
				}
				if (proc.WARP.time > 0) {
					if (imus.IMUWARP.block > 0)
						e.anim.getEff(STPWAVE);
					if (imus.IMUWARP.block == 100)
						proc.WARP.clear();
					else
						proc.WARP.time *= (100 - imus.IMUWARP.block) / 100.0;
				}
				if (proc.CURSE.time > 0) {
					if (imus.IMUCURSE.block > 0)
						e.anim.getEff(STPWAVE);
					if (imus.IMUCURSE.block == 100)
						proc.CURSE.clear();
					else
						proc.CURSE.time *= (100 - imus.IMUCURSE.block) / 100.0;
				}
				if (proc.POIATK.mult > 0 && proc.POISON.damage * imus.IMUPOI.smartImu >= 0) {
					if (imus.IMUPOIATK.block > 0)
						e.anim.getEff(STPWAVE);
					if (imus.IMUPOIATK.block == 100)
						proc.POIATK.clear();
					else
						proc.POIATK.mult *= (100 - imus.IMUPOIATK.block) / 100.0;
				}
				if (proc.SUMMON.mult > 0) {
					if (imus.IMUSUMMON.block > 0)
						e.anim.getEff(STPWAVE);
					if (imus.IMUSUMMON.block == 100)
						proc.SUMMON.clear();
					else
						proc.SUMMON.mult *= (100 - imus.IMUSUMMON.block) / 100.0;
				}
				if (proc.CRIT.mult > 0) {
					if (imus.CRITI.block > 0)
						e.anim.getEff(STPWAVE);
					if (imus.CRITI.block == 100)
						proc.CRIT.clear();
					else
						proc.CRIT.mult *= (100 - imus.CRITI.block) / 100.0;
				}
				if (proc.POISON.damage != 0 && proc.POISON.damage * imus.IMUPOI.smartImu >= 0) {
					if (imus.IMUPOI.block > 0)
						e.anim.getEff(STPWAVE);
					if (imus.IMUPOI.block == 100)
						proc.POISON.clear();
					else
						proc.POISON.damage *= (100 - imus.IMUPOI.block) / 100.0;
				}
				if (proc.SEAL.time > 0) {
					if (imus.IMUSEAL.block > 0)
						e.anim.getEff(STPWAVE);
					if (imus.IMUSEAL.block == 100)
						proc.SEAL.clear();
					else
						proc.SEAL.time *= (100 - imus.IMUSEAL.block) / 100.0;
				}

				if (proc.ARMOR.time > 0 && proc.ARMOR.mult * imus.IMUARMOR.smartImu >= 0) {
					if (imus.IMUARMOR.block > 0)
						e.anim.getEff(STPWAVE);
					if (imus.IMUARMOR.block == 100)
						proc.ARMOR.clear();
					else
						proc.ARMOR.time *= (100 - imus.IMUARMOR.block) / 100.0;
				}

				if (proc.SPEED.time > 0 && proc.ARMOR.mult * imus.IMUARMOR.smartImu <= 0) {
					if (imus.IMUSPEED.block > 0)
						e.anim.getEff(STPWAVE);
					if (imus.IMUSPEED.block == 100)
						proc.ARMOR.clear();
					else
						proc.ARMOR.time *= (100 - imus.IMUSPEED.block) / 100.0;
				}
			}
		}
	}

}
