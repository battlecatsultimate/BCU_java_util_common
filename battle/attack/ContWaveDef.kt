package common.battle.attack

import common.CommonStatic
import common.util.Data
import common.util.pack.EffAnim.DefEff

class ContWaveDef(a: AttackWave, p: Double, layer: Int) : ContWaveAb(a, p, (if (a.dire == 1) Data.Companion.effas().A_E_WAVE else Data.Companion.effas().A_WAVE).getEAnim(DefEff.DEF), layer) {
    protected override fun nextWave() {
        val dire: Int = atk.model.getDire()
        val np: Double = pos + Data.Companion.W_PROG * dire
        val wid: Int = if (dire == 1) Data.Companion.W_E_WID else Data.Companion.W_U_WID
        ContWaveDef(AttackWave(atk, np, wid.toDouble()), np, layer)
    }

    init {
        CommonStatic.setSE(Data.Companion.SE_WAVE)
    }
}
