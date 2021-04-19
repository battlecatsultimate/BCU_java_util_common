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
	public final boolean isLongAtk;
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
				if (e.getProc().CRITI.type == 2)
					proc.CRIT.clear();
			}
		}
	}

}
