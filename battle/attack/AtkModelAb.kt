package common.battle.attack

import common.battle.StageBasis
import common.battle.entity.Entity
import common.util.BattleObj

abstract class AtkModelAb(bas: StageBasis) : BattleObj() {
    val b: StageBasis

    /** get the ability bitmask of this attack  */
    abstract fun getAbi(): Int

    /** get the direction of the entity  */
    abstract fun getDire(): Int

    /** get the position of the entity  */
    abstract fun getPos(): Double

    /** invoke when damage calculation is finished  */
    open fun invokeLater(atk: AttackAb, e: Entity) {}
    open fun getLayer(): Int {
        return 10
    }

    init {
        b = bas
    }
}
