package common.battle.attack;

import common.CommonStatic;
import common.util.pack.EffAnim;

public class ContWaveDef extends ContWaveAb {

	protected ContWaveDef(AttackWave a, double p, int layer) {	
		super(a, p, EffAnim.effas[a.dire == 1 ? A_E_WAVE : A_WAVE].getEAnim(0), layer);

		CommonStatic.def.setSE(SE_WAVE);
	}

	@Override
	protected void nextWave() {
		int dire = atk.model.getDire();
		double np = pos + W_PROG * dire;
		int wid = dire == 1 ? W_E_WID : W_U_WID;
		new ContWaveDef(new AttackWave(atk, np, wid), np, layer);
	}

}
