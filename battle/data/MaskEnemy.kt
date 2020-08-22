package common.battle.data

import common.battle.Basis
import common.util.unit.AbEnemy
import common.util.unit.Enemy
import java.util.*

interface MaskEnemy : MaskEntity {
    fun getDrop(): Double
    override fun getPack(): Enemy?
    fun getStar(): Int
    fun getSummon(): Set<AbEnemy> {
        return TreeSet<AbEnemy>()
    }

    fun multi(b: Basis): Double
}
