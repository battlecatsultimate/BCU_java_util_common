package common.battle.attack;

import common.battle.data.MaskEntity;
import common.battle.entity.EAnimCont;
import common.battle.entity.EEnemy;
import common.battle.entity.EUnit;
import common.battle.entity.Entity;
import common.util.BattleObj;
import common.util.Data.Proc.SUMMON;

public abstract class AtkModelEntity extends AtkModelAb {

	public static AtkModelEntity getIns(Entity e, double d0) {
		if (e instanceof EEnemy) {
			EEnemy ee = (EEnemy) e;
			return new AtkModelEnemy(ee, d0);
		}
		if (e instanceof EUnit) {
			EUnit eu = (EUnit) e;
			return new AtkModelUnit(eu, d0);
		}
		return null;

	}

	protected final MaskEntity data;
	public final Entity e;
	protected final int[] atks, abis, act;
	protected final BattleObj[] acs;
	private final double ratk;
	private final Proc[] sealed;

	protected AtkModelEntity(Entity ent, double d0) {
		super(ent.basis);
		ratk = d0;
		e = ent;
		data = e.data;
		int[][] raw = data.rawAtkData();
		atks = new int[raw.length + 2];
		abis = new int[raw.length + 2];
		act = new int[raw.length + 2];
		acs = new BattleObj[raw.length + 2];
		for (int i = 0; i < raw.length; i++) {
			atks[i] = (int) (raw[i][0] * d0);
			abis[i] = raw[i][2];
			act[i] = data.getAtkModel(i).loopCount();
			acs[i] = new BattleObj();
		}
		if (data.getRevenge() != null) {
			atks[raw.length] = (int) (data.getRevenge().atk * d0);
			abis[raw.length] = 1;
			acs[raw.length] = new BattleObj();
			act[raw.length] = data.getRevenge().loopCount();
		}
		if (data.getResurrection() != null) {
			atks[raw.length + 1] = (int) (data.getResurrection().atk * d0);
			abis[raw.length + 1] = 1;
			acs[raw.length + 1] = new BattleObj();
			act[raw.length + 1] = data.getResurrection().loopCount();
		}
		sealed = new Proc[data.getAtkCount()];
		for (int i = 0; i < sealed.length; i++) {
			sealed[i] = Proc.blank();
			sealed[i].MOVEWAVE.set(data.getAtkModel(i).getProc().MOVEWAVE);
		}
	}

	@Override
	public int getAbi() {
		return e.getAbi();
	}

	/** get the attack, for display only */
	public int getAtk() {
		int ans = 0, temp = 0, c = 1;
		int[][] raw = data.rawAtkData();
		for (int i = 0; i < raw.length; i++)
			if (raw[i][1] > 0) {
				ans += temp / c;
				temp = data.getAtkModel(i).getDire() > 0 ? atks[i] : 0;
				c = 1;
			} else {
				temp += data.getAtkModel(i).getDire() > 0 ? atks[i] : 0;
				c++;
			}
		ans += temp / c;
		return ans;
	}

	/** generate attack entity */
	public final AttackAb getAttack(int ind) {
		if (act[ind] == 0)
			return null;
		act[ind]--;
		Proc proc = Proc.blank();
		int atk = getAttack(ind, proc);
		double[] ints = inRange(ind);
		return new AttackSimple(this, atk, e.type, getAbi(), proc, ints[0], ints[1], e.data.getAtkModel(ind));
	}

	@Override
	public int getDire() {
		return e.dire;
	}

	@Override
	public double getPos() {
		return e.pos;
	}

	/** get the attack box for nth attack */
	public double[] inRange(int ind) {
		int dire = e.dire;
		double d0, d1;
		d0 = d1 = e.pos;
		if (!data.isLD() && !data.isOmni()) {
			d0 += data.getRange() * dire;
			d1 -= data.getWidth() * dire;
		} else {
			d0 += data.getAtkModel(ind).getShortPoint() * dire;
			d1 += data.getAtkModel(ind).getLongPoint() * dire;
		}
		return new double[] { d0, d1 };
	}

	@Override
	public void invokeLater(AttackAb atk, Entity e) {
		SUMMON proc = atk.getProc().SUMMON;
		if (proc.exists()) {
			SUMMON.TYPE conf = proc.type;
			if (conf.on_hit || conf.on_kill && e.health <= 0)
				summon(proc, e, e);
		}
	}

	/** get the collide box bound */
	public double[] touchRange() {
		int dire = e.dire;
		double d0, d1;
		d0 = d1 = e.pos;
		d0 += data.getRange() * dire;
		d1 -= data.getWidth() * dire;
		return new double[] { d0, d1 };
	}

	protected void extraAtk(int ind) {
		if (data.getAtkModel(ind).getMove() != 0)
			e.pos += data.getAtkModel(ind).getMove() * e.dire;
		if (data.getAtkModel(ind).getAltAbi() != 0)
			e.altAbi(data.getAtkModel(ind).getAltAbi());
		if (abis[ind] == 1) {
			if (b.r.nextDouble() * 100 < getProc(ind).TIME.prob)
				b.temp_s_stop = Math.max(b.temp_s_stop, getProc(ind).TIME.time);
			if (b.r.nextDouble() * 100 < getProc(ind).THEME.prob)
				b.changeTheme(getProc(ind).THEME.id, getProc(ind).THEME.time, getProc(ind).THEME.type);
		}
	}

	protected abstract int getAttack(int ind, Proc proc);

	protected abstract int getBaseAtk(int ind);

	@Override
	protected int getLayer() {
		return e.layer;
	}

	protected Proc getProc(int ind) {
		if (e.status[P_SEAL][0] > 0)
			return sealed[ind];
		return data.getAtkModel(ind).getProc();
	}

	protected void setProc(int ind, Proc proc) {
		String[] par = { "CRIT", "WAVE", "KB", "WARP", "STOP", "SLOW", "WEAK", "POISON", "MOVEWAVE", "CURSE", "SNIPER",
				"BOSS", "SEAL", "BREAK", "SUMMON", "SATK", "POIATK", "VOLC", "ARMOR", "SPEED" };
		for (String s0 : par)
			if (getProc(ind).get(s0).perform(b.r))
				if (s0.equals("SUMMON")) {
					SUMMON sprc = getProc(ind).SUMMON;
					SUMMON.TYPE conf = sprc.type;
					if (!conf.on_hit && !conf.on_kill)
						summon(sprc, e, acs[ind]);
					else
						proc.SUMMON.set(sprc);
				} else
					proc.get(s0).set(getProc(ind).get(s0));

		if (proc.CRIT.exists() && proc.CRIT.mult == 0)
			proc.CRIT.mult = 200;
		if (proc.IMUKB.exists() && proc.KB.dis == 0)
			proc.KB.dis = KB_DIS[INT_KB];
		if (proc.IMUKB.exists() && proc.KB.time == 0)
			proc.KB.time = KB_TIME[INT_KB];
		proc.POISON.damage *= ratk;
		if (proc.BOSS.exists())
			b.lea.add(new EAnimCont(e.pos, e.layer, effas()[A_SHOCKWAVE].getEAnim(0)));
	}

	protected abstract void summon(SUMMON sprc, Entity ent, Object acs);

}
