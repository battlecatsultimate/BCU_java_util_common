package common.battle.entity;

import common.CommonStatic;
import common.CommonStatic.BattleConst;
import common.battle.StageBasis;
import common.battle.attack.*;
import common.pack.Identifier;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeTransform;
import common.util.anim.AnimU.UType;
import common.util.anim.EAnimD;
import common.util.pack.NyCastle.NyType;
import common.util.unit.Form;
import common.util.unit.Unit;

public class Cannon extends AtkModelAb {

	public final int id;
	private EAnimD<?> anim, atka, exta;
	private int preTime = 0;
	private EUnit wall = null;
	public double pos;
	private boolean tempAtk;

	public Cannon(StageBasis sb, int type) {
		super(sb);
		id = type;
	}

	/**
	 * call when shoot the canon
	 */
	public void activate() {
		anim = CommonStatic.getBCAssets().atks[id].getEAnim(NyType.BASE);
		preTime = NYPRE[id];
		CommonStatic.setSE(SE_CANNON[id][0]);
	}

	/**
	 * attack part of animation
	 */
	public void drawAtk(FakeGraphics g, P ori, double siz) {
		FakeTransform at = g.getTransform();
		if (atka != null)
			atka.draw(g, ori, siz);
		g.setTransform(at);
		if (exta != null)
			exta.draw(g, ori, siz);
		g.setTransform(at);
		if (!CommonStatic.getConfig().ref || id == BASE_H || id == BASE_SLOW || id == BASE_GROUND) {
			g.delete(at);
			return;
		}

		// after this is the drawing of hit boxes
		siz *= 1.25;
		double rat = BattleConst.ratio;
		int h = (int) (640 * rat * siz);
		g.setColor(FakeGraphics.MAGENTA);
		double d0 = pos, ra = id == BASE_BARRIER ? b.b.t().getCanonProcTime(id) : NYRAN[id];
		if (id == BASE_STOP || id == BASE_WATER)
			d0 -= ra / 2;
		if (id == BASE_BARRIER)
			d0 -= ra;
		int x = (int) ((d0 - pos) * rat * siz + ori.x);
		int y = (int) ori.y;
		int w = (int) (ra * rat * siz);
		if (tempAtk)
			g.fillRect(x, y, w, h);
		else
			g.drawRect(x, y, w, h);

		g.delete(at);
	}

	/**
	 * base part of animation
	 */
	public void drawBase(FakeGraphics g, P ori, double siz) {
		if (anim == null)
			return;
		anim.draw(g, ori, siz);

	}

	@Override
	public int getAbi() {
		return 0;
	}

	@Override
	public int getDire() {
		return -1;
	}

	@Override
	public double getPos() {
		return 0;
	}

	public void update() {
		tempAtk = false;
		if (anim != null && anim.done()) {
			anim = null;
			if (id > 2 && id < 5) {
				atka = CommonStatic.getBCAssets().atks[id].getEAnim(NyType.ATK);
				CommonStatic.setSE(SE_CANNON[id][1]);
			}
		}
		if (atka != null && atka.done())
			atka = null;
		if (exta != null && exta.done())
			exta = null;
		if (anim != null) {
			if (id == 7) {
				if (anim.ind() < 32) {
					anim.update(false);
				} else {
					anim = null;
				}
			} else {
				anim.update(false);
			}
		}
		if (atka != null)
			atka.update(false);
		if (exta != null)
			exta.update(false);

		if (anim == null && atka == null && exta == null) {
			if (id > 2 && id < 5) {
				pos = b.ubase.pos;
				for (Entity e : b.le)
					if (e.dire == -1 && e.pos < pos && (e.touchable() & (TCH_N | TCH_KB)) != 0)
						pos = e.pos;
				pos -= NYRAN[id] / 2;
			}
			if (id == 2 || id == 6) {
				pos = b.ebase.pos;
				for (Entity e : b.le)
					if (e.dire == 1 && e.pos > pos && (e.touchable() & (TCH_N | TCH_KB)) != 0)
						pos = e.pos;
			}
		}

		if (preTime == -1 && id == 2) {
			// wall canon
			Form f = Identifier.parseInt(339, Unit.class).get().forms[0];
			double multi = 0.01 * b.b.t().getCanonMulti(id);
			wall = new EUnit(b, f.du, f.getEAnim(UType.ENTER), multi, null);
			b.le.add(wall);
			wall.added(-1, (int) pos);
			preTime = b.b.t().getCanonProcTime(id);
		}
		if (preTime > 0) {
			preTime--;
			if (preTime == 0) {
				Proc proc = Proc.blank();
				if (id == 0) {
					// basic canon
					proc.WAVE.lv = 12;
					proc.SNIPER.prob = 1;
					double wid = NYRAN[0];
					double p = b.ubase.pos - wid / 2 + 100;
					int atk = b.b.t().getCanonAtk();
					AttackCanon eatk = new AttackCanon(this, atk, -1, 0, proc, 0, 0);
					new ContWaveCanon(new AttackWave(eatk, p, wid, WT_CANN | WT_WAVE), p, 0);
				} else if (id == 1) {
					// slow canon
					proc.SLOW.time = b.b.t().getCanonProcTime(id) * (100 + b.b.getInc(C_SLOW)) / 100;
					int wid = NYRAN[1];
					int spe = 137;
					double p = b.ubase.pos - wid / 2 + spe;
					AttackCanon eatk = new AttackCanon(this, 0, -1, 0, proc, 0, 0);
					new ContMove(eatk, p, wid, spe, 1, 31, 0, 9);
				} else if (id == 2) {
					// wall canon
					if (wall != null)
						wall.kill();
					wall = null;
				} else if (id == 3) {
					// freeze canon
					tempAtk = true;
					proc.STOP.time = b.b.t().getCanonProcTime(id) * (100 + b.b.getInc(C_STOP)) / 100;
					int atk = (int) (b.b.t().getCanonAtk() * b.b.t().getCanonMulti(id) / 100);
					int rad = NYRAN[3] / 2;
					b.getAttack(new AttackCanon(this, atk, -1, 0, proc, pos - rad, pos + rad));
				} else if (id == 4) {
					// water canon
					tempAtk = true;
					proc.CRIT.mult = -(int) (b.b.t().getCanonMulti(id) / 10);
					int rad = NYRAN[4] / 2;
					b.getAttack(new AttackCanon(this, 1, 0, 0, proc, pos - rad, pos + rad));
				} else if (id == 5) {
					// zombie canon
					proc.WAVE.lv = 12;
					double wid = NYRAN[5];
					proc.STOP.time = b.b.t().getCanonProcTime(5) * (100 + b.b.getInc(C_STOP)) / 100;
					proc.SNIPER.prob = 1;
					double p = b.ubase.pos - wid / 2 + 100;
					AttackCanon eatk = new AttackCanon(this, 0, TB_ZOMBIE, AB_ONLY | AB_ZKILL, proc, 0, 0);
					new ContWaveCanon(new AttackWave(eatk, p, wid, WT_CANN | WT_WAVE), p, 5);
				} else if (id == 6) {
					// barrier canon
					tempAtk = true;
					proc.BREAK.prob = 1;
					proc.KB.dis = KB_DIS[INT_KB];
					proc.KB.time = KB_TIME[INT_KB];
					int atk = (int) (b.b.t().getCanonAtk() * b.b.t().getCanonMulti(id) / 100);
					int rad = b.b.t().getCanonProcTime(id);
					b.getAttack(new AttackCanon(this, atk, -1, 0, proc, pos - rad, pos));

					atka = CommonStatic.getBCAssets().atks[id].getEAnim(NyType.ATK);
					exta = CommonStatic.getBCAssets().atks[id].getEAnim(NyType.EXT);
				} else if (id == 7) {
					// curse cannon
					tempAtk = true;
					proc.CURSE.time = (b.b.t().getCanonProcTime(id));
					int wid = NYRAN[7];
					int spe = 137;
					double p = b.ubase.pos - wid / 2 + spe;
					AttackCanon eatk = new AttackCanon(this, 0, -1, 0, proc, 0, 0);
					new ContMove(eatk, p, wid, spe, 1, 31, 0, 9);
				}
			}
		}

	}

}
