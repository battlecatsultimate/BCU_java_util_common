package common.battle.attack

import common.battle.entity.AbEntity
import common.battle.entity.Entity
import common.util.Data
import java.util.*

class AttackVolcano(a: AttackSimple, sta: Double, end: Double) : AttackAb(a, sta, end) {
    var attacked = false
    protected val vcapt: HashMap<Entity, Int>
    override fun capture() {
        val le: List<AbEntity> = model.b.inRange(touch, dire, sta, end)
        capt.clear()
        for (e in le) if ((abi and Data.Companion.AB_ONLY == 0 || e.targetable(type)) && e is Entity && !vcapt.containsKey(e)) capt.add(e)
    }

    override fun excuse() {
        process()
        for (e in capt) {
            if (e.isBase()) continue
            if (e is Entity) {
                e.damaged(this)
                vcapt[e] = Data.Companion.VOLC_ITV
                attacked = true
            }
        }
        vcapt.entries.removeIf { ent: MutableMap.MutableEntry<Entity, Int> ->
            val n = ent.value - 1
            if (n > 0) ent.setValue(n)
            n == 0
        }
    }

    init {
        vcapt = HashMap()
        sta = sta
        end = end
        this.waveType = Data.Companion.WT_VOLC
    }
}
