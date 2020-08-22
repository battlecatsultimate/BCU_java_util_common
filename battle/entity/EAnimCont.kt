package common.battle.entity

import common.system.P
import common.system.fake.FakeGraphics
import common.util.BattleObj
import common.util.anim.EAnimD

open class EAnimCont(val pos: Double, val layer: Int, ead: EAnimD<*>) : BattleObj() {
    private val anim: EAnimD<*>

    /** return whether this animation is finished  */
    fun done(): Boolean {
        return anim.done()
    }

    open fun draw(gra: FakeGraphics, p: P, psiz: Double) {
        anim.draw(gra, p, psiz)
    }

    open fun update() {
        anim.update(false)
    }

    init {
        anim = ead
    }
}
