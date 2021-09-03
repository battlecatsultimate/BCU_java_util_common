package common.battle.attack;

import common.CommonStatic;
import common.battle.entity.AbEntity;
import common.battle.entity.Entity;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.util.pack.NyCastle.NyType;

import java.util.HashSet;
import java.util.Set;

public class ContWaveCanon extends ContWaveAb {

	private final int canid;

	public ContWaveCanon(AttackWave a, double p, int id) {
		super(a, p, CommonStatic.getBCAssets().atks[id].getEAnim(NyType.ATK), 9, true);
		canid = id;
		soundEffect = SE_CANNON[canid][1];

		waves = new HashSet<>();
		waves.add(this);
		maxt = (W_TIME + 1) * (a.proc.WAVE.lv + 1) + (anim.len() - (W_TIME + 3));

		if (id != 0) {
			anim.setTime(1);
			maxt -= 1;
		}
	}

	public ContWaveCanon(AttackWave a, double p, int id, int maxTime, Set<ContWaveAb> waves) {
		super(a, p, CommonStatic.getBCAssets().atks[id].getEAnim(NyType.ATK), 9, false);
		canid = id;
		soundEffect = SE_CANNON[canid][1];

		this.waves = waves;
		this.waves.add(this);
		maxt = maxTime;

		if (id != 0) {
			anim.setTime(1);
			maxt -= 1;
		}
	}

	@Override
	public void draw(FakeGraphics gra, P p, double psiz) {
		if (t < 0)
			return;
		drawAxis(gra, p, psiz);
		if (canid == 0)
			psiz *= 1.25;
		else
			psiz *= 0.5 * 1.25;
		P pus = canid == 0 ? new P(9, 40) : new P(-72, 0);
		anim.draw(gra, p.plus(pus, -psiz), psiz * 2);
	}

	public double getSize() {
		return 2.5;
	}

	@Override
	public void updatePartial() {
		tempAtk = false;
		// guessed attack point compared from BC
		int attack = 2;
		// guessed wave block time compared from BC
		if (t == 0)
			CommonStatic.setSE(soundEffect);
		if (t >= 1 && t <= attack) {
			atk.capture();
			for (AbEntity e : atk.capt)
				if ((e.getAbi() & AB_WAVES) > 0) {
					if (e instanceof Entity)
						((Entity) e).anim.getEff(STPWAVE);
					deactivate();
					return;
				}
		}
		if (!activate)
			return;
		if (t == W_TIME && atk.getProc().WAVE.lv > 0)
			nextWave();
		if (t >= attack) {
			sb.getAttack(atk);
			tempAtk = true;
		}
		if (maxt == t)
			deactivate();
		if (t >= 0 && !anim.done())
			anim.update(false);
		t++;
	}

	@Override
	protected void nextWave() {
		double np = pos - 405;
		new ContWaveCanon(new AttackWave(atk.attacker, atk, np, NYRAN[canid]), np, canid, maxt - t, waves);
	}

}
