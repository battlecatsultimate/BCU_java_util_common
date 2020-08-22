package common.util.unit

import common.io.InStream
import common.pack.IndexContainer.IndexCont
import common.pack.IndexContainer.Indexable
import common.pack.PackData
import common.util.unit.Unitimport
import java.util.*

com.google.api.client.json.jackson2.JacksonFactory
@IndexCont(PackData::class)
class UnitLevel : Indexable<PackData?, UnitLevel?> {
    val lvs = IntArray(3)
    val units: MutableList<Unit> = ArrayList()
    var id: PackData.Identifier<UnitLevel>? = null

    constructor(ID: PackData.Identifier<UnitLevel>?, `is`: InStream) {
        id = ID
        zread(`is`)
    }

    constructor(ID: PackData.Identifier<UnitLevel>?, ul: UnitLevel) {
        id = ID
        for (i in 0..2) lvs[i] = ul.lvs[i]
    }

    constructor(inp: IntArray) {
        var `val` = -1
        for (i in inp.indices) {
            if (`val` != inp[i]) {
                `val` = inp[i]
                if (`val` == 10) lvs[0] = i
                if (`val` == 5) lvs[1] = i
                if (`val` == 0) lvs[2] = i
            }
        }
        if (lvs[1] == 0) lvs[1] = inp.size
        if (lvs[2] == 0) lvs[2] = inp.size
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is UnitLevel) return false
        val ul = o
        if (lvs.size != ul.lvs.size) return false
        for (i in lvs.indices) if (lvs[i] != ul.lvs[i]) return false
        return id == null || ul.id == null || id!!.equals(ul.id)
    }

    override fun getID(): PackData.Identifier<UnitLevel>? {
        return id
    }

    fun getMult(lv: Int): Double {
        var dec = lv
        var pre = 0
        var mul = 20
        var d = 0.8
        for (i in lvs.indices) {
            val dur = lvs[i] - pre
            if (dec > dur * 10) {
                d += mul * dur * 0.1
                dec -= dur * 10
            } else {
                d += mul * dec * 0.01
                break
            }
            mul /= 2
            pre = lvs[i]
        }
        return d
    }

    override fun toString(): String {
        var ans = "{"
        for (set in lvs) {
            if (ans.length > 1) ans += ", "
            ans += set
        }
        ans += "}"
        return ans
    }

    private fun zread(`is`: InStream) {
        val ver: Int = `is`.nextInt()
        if (ver == 1) {
            val vs: IntArray = `is`.nextIntsB()
            lvs[0] = vs[0]
            lvs[1] = vs[1]
            lvs[2] = vs[2]
        } else {
            val vs: Array<IntArray> = `is`.nextIntsBB()
            lvs[0] = vs[1][0]
            lvs[1] = vs[2][0]
            lvs[2] = vs[3][0]
        }
    }

    companion object {
        var def: UnitLevel? = null
    }
}
