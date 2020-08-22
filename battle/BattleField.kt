package common.battle

import common.util.stage.EStage

abstract class BattleField {
    var sb: StageBasis

    protected constructor(stage: EStage, bas: BasisLU, ints: IntArray, seed: Long) {
        sb = StageBasis(stage, bas, ints, seed)
    }

    protected constructor(bas: StageBasis) {
        sb = bas
    }

    open fun update() {
        sb.time++
        actions()
        sb.update()
    }

    protected fun act_can(): Boolean {
        return sb.act_can()
    }

    protected fun act_lock(i: Int, j: Int) {
        sb.act_lock(i, j)
    }

    protected fun act_mon(): Boolean {
        return sb.act_mon()
    }

    protected fun act_sniper(): Boolean {
        return sb.act_sniper()
    }

    protected fun act_spawn(i: Int, j: Int, boo: Boolean): Boolean {
        return sb.act_spawn(i, j, boo)
    }

    protected abstract fun actions()
}
