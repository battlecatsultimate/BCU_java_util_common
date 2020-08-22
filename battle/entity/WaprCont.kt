package common.battle.entity

import common.system.P
import common.system.fake.FakeGraphics
import common.system.fake.FakeTransform
import common.util.Data
import common.util.anim.EAnimD
import common.util.anim.EAnimU
import common.util.pack.EffAnim.WarpEff

class WaprCont(p: Double, pa: WarpEff?, layer: Int, a: EAnimU, dire: Int) : EAnimCont(p, layer, Data.Companion.effas().A_W.getEAnim(pa)) {
    private val ent: EAnimU
    private val chara: EAnimD<*>
    val dire: Int
    override fun draw(gra: FakeGraphics, p: P, psiz: Double) {
        val at: FakeTransform = gra.getTransform()
        p.y -= 275 * psiz
        super.draw(gra, p, psiz)
        gra.setTransform(at)
        p.y += 275 * psiz
        ent.paraTo(chara)
        ent.draw(gra, p, psiz)
        ent.paraTo(null)
        gra.delete(at)
    }

    override fun update() {
        super.update()
        chara.update(false)
    }

    init {
        ent = a
        chara = Data.Companion.effas().A_W_C.getEAnim(pa)
        this.dire = dire
    }
}
