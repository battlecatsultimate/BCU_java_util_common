package common.battle.entity

import common.battle.attack.AttackAb
import common.util.BattleObj

abstract class AbEntity protected constructor(h: Int) : BattleObj() {
    var health: Long
    var maxH: Long
    var dire = 0
    var pos = 0.0
    fun added(d: Int, p: Int) {
        pos = p.toDouble()
        dire = d
    }

    abstract fun damaged(atk: AttackAb)
    abstract fun getAbi(): Int
    abstract fun isBase(): Boolean
    abstract fun postUpdate()
    abstract fun targetable(type: Int): Boolean
    abstract fun touchable(): Int
    abstract fun update()

    init {
        var h = h
        if (h <= 0) h = 1
        maxH = h.toLong()
        health = maxH
    }
}
