package common.util.unit

import common.CommonStatic
import common.CommonStatic.BCAuxAssets
import common.system.files.VFile
import common.util.Data
import java.util.*

class Combo private constructor(val id: Int, str: String) : Data() {
    val name: Int
    val show: Int
    val type: Int
    val lv: Int
    val units: Array<IntArray>
    override fun toString(): String {
        return "$name,$show"
    }

    companion object {
        fun readFile() {
            val aux: BCAuxAssets = CommonStatic.getBCAssets()
            var qs: Queue<String> = VFile.Companion.readLine("./org/data/NyancomboData.csv")
            val list: MutableList<Combo> = ArrayList()
            var i = 0
            var ns = IntArray(Data.Companion.C_TOT)
            for (str in qs) {
                if (str.length < 20) continue
                val c = Combo(i++, str.trim { it <= ' ' })
                if (c.show > 0) {
                    list.add(c)
                    ns[c.type]++
                }
            }
            i = 0
            while (i < Data.Companion.C_TOT) {
                aux.combos.get(i) = arrayOfNulls<Combo>(ns[i])
                i++
            }
            ns = IntArray(Data.Companion.C_TOT)
            for (c in list) aux.combos.get(c.type).get(ns[c.type]++) = c
            qs = VFile.Companion.readLine("./org/data/NyancomboParam.tsv")
            i = 0
            while (i < Data.Companion.C_TOT) {
                val strs = qs.poll().trim { it <= ' ' }.split("\t").toTypedArray()
                if (strs.size < 5) {
                    i++
                    continue
                }
                for (j in 0..4) {
                    aux.values.get(i).get(j) = strs[j].toInt()
                    if (i == Data.Companion.C_RESP) aux.values.get(i).get(j) *= 2.6
                    if (i == Data.Companion.C_C_SPE) aux.values.get(i).get(j) = (aux.values.get(i).get(j) - 10) * 15
                }
                i++
            }
            qs = VFile.Companion.readLine("./org/data/NyancomboFilter.tsv")
            aux.filter = arrayOfNulls<IntArray>(qs.size)
            i = 0
            while (i < aux.filter.size) {
                val strs = qs.poll().trim { it <= ' ' }.split("\t").toTypedArray()
                aux.filter.get(i) = IntArray(strs.size)
                for (j in strs.indices) aux.filter.get(i).get(j) = strs[j].toInt()
                i++
            }
        }
    }

    init {
        val strs = str.split(",").toTypedArray()
        name = strs[0].toInt()
        show = strs[1].toInt()
        var n = 0
        n = 0
        while (n < 5) {
            if (strs[2 + n * 2].toInt() == -1) break
            n++
        }
        units = Array(n) { IntArray(2) }
        for (i in 0 until n) {
            units[i][0] = strs[2 + i * 2].toInt()
            units[i][1] = strs[3 + i * 2].toInt()
        }
        type = strs[12].toInt()
        lv = strs[13].toInt()
    }
}
