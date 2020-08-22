package common.util

import common.battle.StageBasis
import common.util.Dataimport
import common.util.LockLL.X
import common.utilimport.LockLL.X
import java.util.*

abstract class EntRand<X> : Data() {
    val list: List<EREnt<X>> = ArrayList<EREnt<X>>()
    val map: MutableMap<StageBasis, Lock<X>> = HashMap<StageBasis, Lock<X>>()
    var type = 0
    fun updateCopy(sb: StageBasis, o: Any?) {
        if (o != null) map[sb] = o as Lock<X>
    }

    protected fun getSelection(sb: StageBasis, obj: Any): EREnt<X>? {
        if (type != T_NL) {
            var l = map[sb]
            if (l == null) map[sb] = if (type == T_LL) LockLL<X>() else LockGL<X>().also { l = it }
            var ae: EREnt<X>? = l!!.get(obj)
            if (ae == null) l!!.put(obj, selector(sb, obj).also { ae = it })
            return ae
        }
        return selector(sb, obj)
    }

    private fun selector(sb: StageBasis, obj: Any): EREnt<X>? {
        var tot = 0
        for (e in list) tot += e.share
        if (tot > 0) {
            var r = (sb.r.nextDouble() * tot) as Int
            for (ent in list) {
                r -= ent.share
                if (r < 0) return ent
            }
        }
        return null
    }

    companion object {
        const val T_NL = 0
        const val T_LL = 1
        const val T_GL = 2
    }
}

internal interface Lock<X> {
    operator fun get(obj: Any?): EREnt<X>?
    fun put(obj: Any?, ae: EREnt<X>?): EREnt<X>?
}

internal class LockGL<X> : BattleObj(), Lock<X> {
    private var ae: EREnt<X>? = null
    override operator fun get(obj: Any?): EREnt<X>? {
        return ae
    }

    override fun put(obj: Any?, e: EREnt<X>?): EREnt<X>? {
        val pre: EREnt<X>? = ae
        ae = e
        return pre
    }
}

internal object LockLL<X> : HashMap<Any?, EREnt<X>?>, Lock<X> {
    private const val serialVersionUID = 1L
}
