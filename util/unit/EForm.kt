package common.util.unit

import common.battle.StageBasis
import common.battle.data.MaskUnit
import common.battle.data.PCoin
import common.battle.entity.EUnit
import common.util.Data
import common.util.anim.AnimU.UType

class EForm : Data {
    private val f: Form
    private val level: Level
    var du: MaskUnit? = null

    constructor(form: Form, vararg level: Int) {
        f = form
        val lvs = level
        this.level = Level(lvs)
        val pc: PCoin? = f.pCoin
        du = if (pc != null) pc.improve(lvs) else form.du
    }

    constructor(form: Form, level: Level) {
        f = form
        this.level = level
        val pc: PCoin? = f.pCoin
        du = if (pc != null) pc.improve(level.lvs) else form.du
    }

    fun getEntity(b: StageBasis?): EUnit {
        val d = f.unit.lv.getMult(level.lvs[0])
        return EUnit(b, du, f.getEAnim(UType.WALK), d, level)
    }

    fun getPrice(sta: Int): Int {
        return (du.getPrice() * (1 + sta * 0.5))
    }
}
