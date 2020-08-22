package common.util

import common.system.Copable

class EREnt<X> : BattleStatic, Copable<EREnt<X>?> {
    var ent: X? = null
    var multi = 100
    var mula = 100
    var share = 1
    override fun copy(): EREnt<X?> {
        val ans = EREnt<X?>()
        ans.ent = ent
        ans.multi = multi
        ans.mula = mula
        ans.share = share
        return ans
    }
}
