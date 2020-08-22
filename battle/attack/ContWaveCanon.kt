package common.battle.attack

import common.CommonStatic
import common.system.P
import common.system.fake.FakeGraphics
import common.util.Data
import common.util.pack.NyCastle.NyType

class ContWaveCanon(a: AttackWave, p: Double, private val canid: Int) : ContWaveAb(a, p, CommonStatic.getBCAssets().atks.get(canid).getEAnim(NyType.ATK), 9) {
    override fun draw(gra: FakeGraphics, p: P, psiz: Double) {
        var psiz = psiz
        drawAxis(gra, p, psiz)
        psiz *= if (canid == 0) 1.25 else 0.5 * 1.25
        val pus: P = if (canid == 0) P(9, 40) else P(-72, 0)
        anim.draw(gra, p.plus(pus, -psiz), psiz * 2)
    }

    fun getSize(): Double {
        return 2.5
    }

    override fun nextWave() {
        val np: Double = pos - 405
        ContWaveCanon(AttackWave(atk, np, Data.Companion.NYRAN.get(canid)), np, canid)
        CommonStatic.setSE(Data.Companion.SE_CANNON.get(canid).get(1))
    }
}
