package common.battle.attack;

import common.battle.data.MaskAtk;
import common.battle.entity.AbEntity;
import common.battle.entity.Entity;
import common.util.BattleObj;
import common.util.unit.Trait;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AttackAb extends BattleObj {

	public final int rawAtk, abi;
	public int atk;
	public final ArrayList<Trait> trait;
	public final AtkModelAb model;
	public final AttackAb origin;
	public final MaskAtk matk;
	public final Entity attacker;
	public final int layer;
	public final boolean isLongAtk, SPtr;
	public int duration;
	public boolean excludeLastEdge = false, isCounter = false;

	public int touch = TCH_N, dire, canon = -2, waveType = 0, volcType = 0;

	protected final Proc proc;
	protected final List<AbEntity> capt = new ArrayList<>();
	protected double sta, end;

	protected AttackAb(Entity attacker, AtkModelAb ent, int ATK, ArrayList<Trait> tr, int eab, Proc pro, double p0, double p1, MaskAtk matk, int layer, boolean isLongAtk, int time) {
		this.attacker = attacker;
		dire = ent.getDire();
		origin = this;
		model = ent;
		trait = tr;
		rawAtk = ATK;
		atk = rawAtk;
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

	protected AttackAb(Entity attacker, AttackAb a, double STA, double END, boolean isLongAtk) {
		this.attacker = attacker;
		dire = a.dire;
		origin = a.origin;
		model = a.model;
		rawAtk = a.rawAtk;
		atk = rawAtk;
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
				boolean blocked = false;
				if (proc.KB.dis > 0 && imus.IMUKB.block != 0) {
					if (imus.IMUKB.block > 0)
						blocked = true;
					if (imus.IMUKB.block == 100)
						proc.KB.clear();
					else
						proc.KB.dis *= (100 - imus.IMUKB.block) / 100.0;
				}
				if (proc.SLOW.time > 0 && imus.IMUSLOW.block != 0) {
					if (imus.IMUSLOW.block > 0)
						blocked = true;
					if (imus.IMUSLOW.block == 100)
						proc.SLOW.clear();
					else
						proc.SLOW.time *= (100 - imus.IMUSLOW.block) / 100.0;
				}
				if (proc.STOP.time > 0 && imus.IMUSTOP.block != 0) {
					if (imus.IMUSTOP.block > 0)
						blocked = true;
					if (imus.IMUSTOP.block == 100)
						proc.STOP.clear();
					else
						proc.STOP.time *= (100 - imus.IMUSTOP.block) / 100.0;
				}
				if (proc.WEAK.time > 0 && checkAIImmunity(proc.WEAK.mult - 100,imus.IMUWEAK.smartImu, imus.IMUWEAK.block > 0)) {
					if (imus.IMUWEAK.block > 0)
						blocked = true;
					if (imus.IMUWEAK.block == 100)
						proc.WEAK.clear();
					else
						proc.WEAK.time *= (100 - imus.IMUWEAK.block) / 100.0;
				}
				if (proc.WARP.time > 0 && imus.IMUWARP.block != 0) {
					if (imus.IMUWARP.block > 0)
						blocked = true;
					if (imus.IMUWARP.block == 100)
						proc.WARP.clear();
					else
						proc.WARP.time *= (100 - imus.IMUWARP.block) / 100.0;
				}
				if (proc.CURSE.time > 0 && imus.IMUCURSE.block != 0) {
					if (imus.IMUCURSE.block > 0)
						blocked = true;
					if (imus.IMUCURSE.block == 100)
						proc.CURSE.clear();
					else
						proc.CURSE.time *= (100 - imus.IMUCURSE.block) / 100.0;
				}
				if (proc.POIATK.mult != 0 && imus.IMUPOIATK.block != 0) {
					if (imus.IMUPOIATK.block > 0)
						blocked = true;
					if (imus.IMUPOIATK.block == 100)
						proc.POIATK.clear();
					else
						proc.POIATK.mult *= (100 - imus.IMUPOIATK.block) / 100.0;
				}
				if (proc.SUMMON.mult > 0 && imus.IMUSUMMON.block != 0) {
					if (imus.IMUSUMMON.block > 0)
						blocked = true;
					if (imus.IMUSUMMON.block == 100)
						proc.SUMMON.clear();
					else
						proc.SUMMON.mult *= (100 - imus.IMUSUMMON.block) / 100.0;
				}
				if (proc.CRIT.mult > 0 && imus.CRITI.block != 0) {
					if (imus.CRITI.block > 0)
						blocked = true;
					if (imus.CRITI.block == 100)
						proc.CRIT.clear();
					else
						proc.CRIT.mult *= (100 - imus.CRITI.block) / 100.0;
				}
				if (proc.POISON.damage != 0 && imus.IMUPOI.block != 0 && checkAIImmunity(proc.POISON.damage, imus.IMUPOI.smartImu, imus.IMUPOI.block < 0)) {
					if (imus.IMUPOI.block > 0)
						blocked = true;
					if (imus.IMUPOI.block == 100)
						proc.POISON.clear();
					else
						proc.POISON.damage *= (100 - imus.IMUPOI.block) / 100.0;
				}
				if (proc.SEAL.time > 0 && imus.IMUSEAL.block != 0) {
					if (imus.IMUSEAL.block > 0)
						blocked = true;
					if (imus.IMUSEAL.block == 100)
						proc.SEAL.clear();
					else
						proc.SEAL.time *= (100 - imus.IMUSEAL.block) / 100.0;
				}
				if (proc.ARMOR.time > 0 && imus.IMUARMOR.block != 0 && checkAIImmunity(proc.ARMOR.mult, imus.IMUARMOR.smartImu, imus.IMUARMOR.block < 0)) {
					if (imus.IMUARMOR.block > 0)
						blocked = true;
					if (imus.IMUARMOR.block == 100)
						proc.ARMOR.clear();
					else
						proc.ARMOR.time *= (100 - imus.IMUARMOR.block) / 100.0;
				}
				if (proc.SPEED.time > 0 && imus.IMUSPEED.block != 0) {
					boolean b;
					if (proc.SPEED.type != 2)
						b = imus.IMUSPEED.block < 0;
					else
						b = (e.data.getSpeed() > proc.SPEED.speed && imus.IMUSPEED.block > 0) || (e.data.getSpeed() < proc.SPEED.speed && imus.IMUSPEED.block < 0);

					if (checkAIImmunity(proc.SPEED.speed, imus.IMUSPEED.smartImu, b)) {
						if (imus.IMUSPEED.block > 0)
							blocked = true;
						if (imus.IMUSPEED.block == 100)
							proc.ARMOR.clear();
						else
							proc.ARMOR.time *= (100 - imus.IMUSPEED.block) / 100.0;
					}
				}

				if (blocked)
					e.anim.getEff(STPWAVE);
			}
		}
	}

	/**
	 * Used to obtain whether controlled immunity will have effect or not
	 * @param val The effect of the proc
	 * @param side The side used by the smartImu
	 * @param invert Inverts the >,< signs depending on the proc
	 * @return idk
	 */
	private boolean checkAIImmunity(int val, int side, boolean invert) {
		if (side == 0)
			return true;
		if (invert) {
			return val * side < 0;
		} else {
			return val * side > 0;
		}
	}

	public void notifyEntity(Consumer<Entity> notifier) {
		if (!isCounter && attacker != null) {
			notifier.accept(attacker);
		}
	}

}