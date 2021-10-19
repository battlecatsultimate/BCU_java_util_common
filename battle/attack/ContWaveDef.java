package common.battle.attack;

import common.CommonStatic;
import common.battle.entity.AbEntity;
import common.battle.entity.Entity;
import common.util.pack.EffAnim.DefEff;

import java.util.HashSet;
import java.util.Set;

public class ContWaveDef extends ContWaveAb {

	protected ContWaveDef(AttackWave a, double p, int layer, boolean delay) {
		super(a, p, (a.dire == 1 ? a.waveType == WT_MINI ? effas().A_E_MINIWAVE : effas().A_E_WAVE : a.waveType == WT_MINI ? effas().A_MINIWAVE : effas().A_WAVE).getEAnim(DefEff.DEF), layer, delay);
		soundEffect = SE_WAVE;

		maxt -= 1;
		anim.setTime(1);
		waves = new HashSet<>();
		waves.add(this);
	}

	protected ContWaveDef(AttackWave a, double p, int layer, boolean delay, Set<ContWaveAb> waves) {
		super(a, p, (a.dire == 1 ? a.waveType == WT_MINI ? effas().A_E_MINIWAVE : effas().A_E_WAVE : a.waveType == WT_MINI ? effas().A_MINIWAVE : effas().A_WAVE).getEAnim(DefEff.DEF), layer, delay);
		soundEffect = SE_WAVE;

		maxt -= 1;
		anim.setTime(1);
		this.waves = waves;
		this.waves.add(this);
	}

	@Override
	public void update() {
		tempAtk = false;
		boolean isMini = atk.waveType == WT_MINI;
		// guessed attack point compared from BC
		int attack = (isMini ? 4 : 6);
		// guessed wave block time compared from BC
		if (t == 0)
			CommonStatic.setSE(soundEffect);
		if (t >= (isMini ? 1 : 2) && t <= attack) {
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
		if (t == (isMini ? W_MINI_TIME - 1 : W_TIME)) {
			if (isMini && atk.proc.MINIWAVE.lv > 0)
				nextWave();
			else if (!isMini && atk.getProc().WAVE.lv > 0)
				nextWave();
		}
		if (t == attack) {
			sb.getAttack(atk);
			tempAtk = true;
		}
		if (maxt == t)
			activate = false;
		if (t >= 0)
			anim.update(false);
		t++;
	}

	@Override
	protected void nextWave() {
		int dire = atk.model.getDire();
		double np = pos + W_PROG * dire;
		int wid = dire == 1 ? W_E_WID : W_U_WID;
		new ContWaveDef(new AttackWave(atk.attacker, atk, np, wid), np, layer, false, waves);
	}
}
