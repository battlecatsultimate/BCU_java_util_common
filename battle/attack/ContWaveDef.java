package common.battle.attack;

import common.CommonStatic;
import common.util.anim.EAnimD;
import common.util.pack.EffAnim.DefEff;

public class ContWaveDef extends ContWaveAb {

	protected ContWaveDef(AttackWave a, double p, int layer) {
		super(a, p, (a.dire == 1 ? a.waveType == WT_MINI ? effas().A_E_MINIWAVE : effas().A_E_WAVE : a.waveType == WT_MINI ? effas().A_MINIWAVE : effas().A_WAVE).getEAnim(DefEff.DEF), layer);

		CommonStatic.setSE(SE_WAVE);
	}

	@Override
	protected void nextWave() {
		int dire = atk.model.getDire();
		double np = pos + W_PROG * dire;
		int wid = dire == 1 ? W_E_WID : W_U_WID;
		new ContWaveDef(new AttackWave(atk, np, wid), np, layer);
	}
}
