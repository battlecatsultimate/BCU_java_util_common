package common.battle.attack

import common.battle.entity.AbEntity
import common.battle.entity.Entity
import common.util.Data
import java.util.*
import java.util.function.Predicate

class AttackWave : AttackAb {
    val incl: MutableSet<Entity>?

    constructor(a: AttackSimple, p0: Double, wid: Double, wt: Int) : super(a, p0 - wid / 2, p0 + wid / 2) {
        waveType = wt
        incl = HashSet()
        proc.WAVE.lv--
    }

    constructor(a: AttackWave, p0: Double, wid: Double) : super(a, p0 - wid / 2, p0 + wid / 2) {
        waveType = a.waveType
        incl = a.incl
        proc.WAVE.lv--
    }

    override fun capture() {
        val le: MutableList<AbEntity> = model.b.inRange(touch, dire, sta, end)
        if (incl != null) le.removeIf(Predicate<AbEntity> { e: AbEntity -> incl.contains(e) })
        capt.clear()
        if (abi and Data.Companion.AB_ONLY == 0) capt.addAll(le) else for (e in le) if (e.targetable(type)) capt.add(e)
    }

    override fun excuse() {
        process()
        for (e in capt) {
            if (e.isBase()) continue
            if (e is Entity) {
                e.damaged(this)
                incl!!.add(e)
            }
        }
    }
}
