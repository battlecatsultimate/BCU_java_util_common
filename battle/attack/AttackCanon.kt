package common.battle.attack

import common.battle.entity.Cannon
import common.util.Data

class AttackCanon(c: Cannon, ATK: Int, t: Int, eab: Int, pro: Proc, p0: Double, p1: Double) : AttackSimple(c, ATK, t, eab, pro, p0, p1, true, null) {
    init {
        canon = c.id
        waveType = waveType or Data.Companion.WT_CANN
        if (canon == 5) touch = Data.Companion.TCH_UG or Data.Companion.TCH_N
    }
}
