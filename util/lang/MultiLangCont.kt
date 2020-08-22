package common.util.lang

import common.CommonStatic
import common.pack.UserProfile
import common.util.anim.AnimI.AnimType
import common.util.stage.MapColc
import common.util.stage.Stage
import common.util.stage.StageMap
import common.util.unit.Enemy
import common.util.unit.Form
import common.util.unit.Unit.UnitInfo
import java.util.*
import java.util.function.Supplier

class MultiLangCont<I, T> : CommonStatic.Lang() {
    class MultiLangStatics {
        val MCNAME: MultiLangCont<MapColc, String> = MultiLangCont<MapColc, String>()
        val SMNAME: MultiLangCont<StageMap, String> = MultiLangCont<StageMap, String>()
        val STNAME = MultiLangCont<Stage, String>()
        val RWNAME = MultiLangCont<Int, String>()
        val FNAME = MultiLangCont<Form, String>()
        val ENAME: MultiLangCont<Enemy, String> = MultiLangCont<Enemy, String>()
        val COMNAME = MultiLangCont<Int, String>()
        val FEXP = MultiLangCont<Form, Array<String>>()
        val CFEXP: MultiLangCont<UnitInfo, Array<String>> = MultiLangCont<UnitInfo, Array<String>>()
        val EEXP: MultiLangCont<Enemy, Array<String>> = MultiLangCont<Enemy, Array<String>>()
        val ANIMNAME: MultiLangCont<AnimType<*, *>, String> = MultiLangCont<AnimType<*, *>, String>()
    }

    private val map: MutableMap<String, HashMap<I, T>> = TreeMap<String, HashMap<I, T>>()
    fun clear() {
        map.clear()
    }

    fun getCont(x: I): T? {
        for (i in 0 until CommonStatic.Lang.Companion.pref.get(CommonStatic.getConfig().lang).length) {
            val ans = getSub(CommonStatic.Lang.Companion.LOC_CODE.get(CommonStatic.Lang.Companion.pref.get(CommonStatic.getConfig().lang).get(i)))!![x]
            if (ans != null) return ans
        }
        return null
    }

    fun put(loc: String, x: I?, t: T?) {
        if (x == null || t == null) return
        getSub(loc)!![x] = t
    }

    private fun getSub(loc: String): HashMap<I, T>? {
        var ans = map[loc]
        if (ans == null) map[loc] = HashMap<I, T>().also { ans = it }
        return ans
    }

    companion object {
        operator fun get(o: Any): String? {
            if (o is MapColc) return getStatic().MCNAME.getCont(o as MapColc)
            if (o is StageMap) return getStatic().SMNAME.getCont(o as StageMap)
            if (o is Stage) return getStatic().STNAME.getCont(o)
            if (o is Form) return getStatic().FNAME.getCont(o)
            return if (o is Enemy) getStatic().ENAME.getCont(o as Enemy) else null
        }

        fun getStatic(): MultiLangStatics {
            return UserProfile.Companion.getStatic<MultiLangStatics>("MultiLangStatics", Supplier { MultiLangStatics() })
        }
    }
}
