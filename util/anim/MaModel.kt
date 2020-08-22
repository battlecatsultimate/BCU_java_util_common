package common.util.anim

import common.CommonStatic
import common.battle.data.DataEntity
import common.io.InStream
import common.io.OutStream
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonEncoder
import common.io.json.Test
import common.io.json.Test.JsonTest_0.JsonD
import common.io.json.Test.JsonTest_2
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.system.files.FileData
import common.util.BattleStatic
import common.util.Data
import common.util.stage.EStage
import common.util.stage.StageMap
import common.util.stage.StageMap.StageMapInfo
import common.util.unit.UnitLevel
import io.BCPlayer
import page.JL
import page.anim.AnimBox
import page.support.ListJtfPolicy
import page.support.SortTable
import page.view.ViewBox
import page.view.ViewBox.Conf
import page.view.ViewBox.Controller
import page.view.ViewBox.VBExporter
import java.io.PrintStream
import java.util.*
import java.util.function.Function

class MaModel : Data, Cloneable, BattleStatic {
    var n: Int
    var m: Int
    var ints = IntArray(3)
    var confs: Array<IntArray?>
    var parts: Array<IntArray?>
    var strs0: Array<String?>
    var strs1: Array<String?>
    var status: MutableMap<IntArray?, Int> = HashMap()

    constructor() {
        n = 1
        m = 1
        parts = arrayOf(intArrayOf(-1, -1, 0, 0, 0, 0, 0, 0, 1000, 1000, 0, 1000, 0, 0))
        ints = intArrayOf(1000, 3600, 1000)
        confs = arrayOf(intArrayOf(0, 0, 0, 0, 0, 0), intArrayOf(0, 0, 0, 0, 0, 0))
        strs0 = arrayOf("default")
        strs1 = arrayOf("default")
    }

    protected constructor(qs: Queue<String>) {
        qs.poll()
        qs.poll()
        n = qs.poll().trim { it <= ' ' }.toInt()
        parts = Array(n) { IntArray(14) }
        strs0 = arrayOfNulls(n)
        for (i in 0 until n) {
            val ss = qs.poll().trim { it <= ' ' }.split(",").toTypedArray()
            for (j in 0..12) parts[i]!![j] = ss[j].trim { it <= ' ' }.toInt()
            if (ss.size == 14) strs0[i] = Data.Companion.restrict(ss[13]) else strs0[i] = ""
        }
        var ss = qs.poll().trim { it <= ' ' }.split(",").toTypedArray()
        for (i in 0..2) ints[i] = ss[i].trim { it <= ' ' }.toInt()
        m = qs.poll().trim { it <= ' ' }.toInt()
        confs = Array(m) { IntArray(6) }
        strs1 = arrayOfNulls(m)
        for (i in 0 until m) {
            ss = qs.poll().trim { it <= ' ' }.split(",").toTypedArray()
            for (j in 0..5) confs[i]!![j] = ss[j].trim { it <= ' ' }.toInt()
            if (ss.size == 7) strs1[i] = Data.Companion.restrict(ss[6]) else strs1[i] = ""
        }
    }

    private constructor(mm: MaModel) {
        n = mm.n
        m = mm.m
        ints = mm.ints.clone()
        parts = arrayOfNulls(n)
        confs = arrayOfNulls(m)
        for (i in 0 until n) parts[i] = mm.parts[i]!!.clone()
        for (i in 0 until m) confs[i] = mm.confs[i]!!.clone()
        strs0 = mm.strs0.clone()
        strs1 = mm.strs1.clone()
    }

    /** regulate check imgcut id and detect parent loop  */
    fun check(anim: AnimD<*, *>) {
        val ics: Int = anim.imgcut!!.n
        for (p in parts) {
            if (p!![2] >= ics) p[2] = 0
            if (p[0] > n) p[0] = 0
        }
        val temp = IntArray(parts.size)
        for (i in parts.indices) check(temp, i)
        for (i in parts.indices) if (temp[i] == 2) parts[i]!![0] = -1
    }

    fun clearAnim(bs: BooleanArray, `as`: Array<MaAnim>) {
        for (ma in `as`) {
            val lp: MutableList<Part?> = ArrayList()
            for (p in ma.parts) if (!bs[p!!.ints[0]]) lp.add(p)
            ma.parts = lp.toTypedArray()
            ma.n = ma.parts.size
        }
    }

    public override fun clone(): MaModel {
        return MaModel(this)
    }

    fun getChild(bs: BooleanArray): Int {
        var total = 0
        var count = 1
        while (count > 0) {
            count = 0
            for (i in 0 until n) if (!bs[i] && parts[i]!![0] >= 0 && bs[parts[i]!![0]]) {
                count++
                total++
                bs[i] = true
            }
        }
        return total
    }

    fun reorder(move: IntArray) {
        val data = parts
        val name = strs0
        parts = arrayOfNulls(move.size)
        strs0 = arrayOfNulls(move.size)
        for (i in 0 until n) if (move[i] < 0 || move[i] >= data.size) {
            parts[i] = intArrayOf(0, -1, 0, 0, 0, 0, 0, 0, 1000, 1000, 0, 1000, 0, 0)
            strs0[i] = "new part"
        } else {
            parts[i] = data[move[i]]
            strs0[i] = name[move[i]]
        }
    }

    fun revert() {
        parts[0]!![8] *= -1
        for (sets in parts) sets!![10] *= -1
    }

    fun write(ps: PrintStream) {
        ps.println("[mamodel]")
        ps.println(3)
        ps.println(n)
        for (i in 0 until n) {
            for (j in 0..12) ps.print(parts[i]!![j].toString() + ",")
            ps.println(strs0[i])
        }
        ps.println(ints[0].toString() + "," + ints[1] + "," + ints[2])
        ps.println(m)
        for (i in 0 until m) {
            for (j in 0 until confs[i].length) ps.print(confs[i]!![j].toString() + ",")
            ps.println(strs1[i])
        }
    }

    fun arrange(e: EAnimI): Array<EPart?> {
        val ents: Array<EPart?> = arrayOfNulls<EPart>(n)
        for (i in 0 until n) ents[i] = EPart(this, e.anim(), parts[i], strs0[i]!!, i, ents)
        return ents
    }

    fun restore(`is`: InStream) {
        n = `is`.nextInt()
        m = `is`.nextInt()
        ints = `is`.nextIntsB()
        parts = `is`.nextIntsBB()
        confs = `is`.nextIntsBB()
        strs0 = arrayOfNulls(n)
        for (i in 0 until n) strs0[i] = `is`.nextString()
        strs1 = arrayOfNulls(m)
        for (i in 0 until m) strs1[i] = `is`.nextString()
    }

    fun write(os: OutStream) {
        os.writeInt(n)
        os.writeInt(m)
        os.writeIntB(ints)
        os.writeIntBB(parts)
        os.writeIntBB(confs)
        for (str in strs0) os.writeString(str)
        for (str in strs1) os.writeString(str)
    }

    /** detect loop  */
    private fun check(temp: IntArray, p: Int): Int {
        if (temp[p] > 0) return temp[p]
        if (parts[p]!![0] == -1) return 1.also { temp[p] = it }
        temp[p] = 2
        if (parts[p]!![0] >= parts.size) parts[p]!![0] = 0
        return check(temp, parts[p]!![0]).also { temp[p] = it }
    }

    companion object {
        fun newIns(f: FileData): MaModel {
            return try {
                MaModel(f.readLine())
            } catch (e: Exception) {
                e.printStackTrace()
                MaModel()
            }
        }

        fun newIns(path: String?): MaModel {
            return CommonStatic.def.readSave<MaModel>(path, Function { f: Queue<String>? ->
                f?.let { MaModel(it) } ?: MaModel()
            })
        }
    }
}
