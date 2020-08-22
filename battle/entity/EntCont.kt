package common.battle.entity

import common.util.BattleObj

class EntCont(var ent: Entity, var t: Int) : BattleObj() {
    fun update() {
        if (t > 0) t--
    }
}
