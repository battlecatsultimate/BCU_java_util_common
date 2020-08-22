package common.io

import common.CommonStatic
import common.io.DataIO
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
import java.io.UnsupportedEncodingException

@Strictfp
interface InStream {
    fun end(): Boolean
    fun nextByte(): Int
    fun nextBytesB(): ByteArray
    fun nextBytesI(): ByteArray
    fun nextDouble(): Double
    fun nextDoubles(): DoubleArray
    fun nextFloat(): Float
    fun nextInt(): Int
    fun nextIntsB(): IntArray
    fun nextIntsBB(): Array<IntArray?>
    fun nextLong(): Long
    fun nextShort(): Int
    fun nextString(): String
    fun subStream(): InStream
    fun translate(): OutStream

    companion object {
        fun getIns(bs: ByteArray): InStream? {
            val `is`: IntArray = DataIO.Companion.translate(bs)
            val sig: Int = DataIO.Companion.toInt(`is`, 0)
            if (sig == bs.size - 4) {
                return InStreamDef(`is`, 4, `is`.size)
            }
            throw BCUException("Unsupported version")
        }
    }
}

@Strictfp
internal class InStreamDef : DataIO, InStream {
    private val bs: IntArray
    private val off: Int
    private val max: Int
    private var index: Int

    protected constructor(isd: InStreamDef) {
        bs = isd.bs
        off = isd.index
        index = off
        max = isd.max
    }

    constructor(data: IntArray, ofs: Int, m: Int) {
        bs = data
        off = ofs
        max = m
        index = off
    }

    override fun end(): Boolean {
        return index == max
    }

    override fun nextByte(): Int {
        check(1)
        val ans: Int = DataIO.Companion.toByte(bs, index)
        index++
        return ans
    }

    override fun nextBytesB(): ByteArray {
        val len = nextByte()
        val ints = ByteArray(len)
        for (i in 0 until len) ints[i] = nextByte().toByte()
        return ints
    }

    override fun nextBytesI(): ByteArray {
        val len = nextInt()
        val ints = ByteArray(len)
        for (i in 0 until len) ints[i] = nextByte().toByte()
        return ints
    }

    override fun nextDouble(): Double {
        check(8)
        val ans: Double = DataIO.Companion.toDouble(bs, index)
        index += 8
        return ans
    }

    override fun nextDoubles(): DoubleArray {
        val len = nextByte()
        val ints = DoubleArray(len)
        for (i in 0 until len) ints[i] = nextDouble()
        return ints
    }

    override fun nextFloat(): Float {
        check(4)
        val ans: Float = DataIO.Companion.toFloat(bs, index)
        index += 4
        return ans
    }

    override fun nextInt(): Int {
        check(4)
        val ans: Int = DataIO.Companion.toInt(bs, index)
        index += 4
        return ans
    }

    override fun nextIntsB(): IntArray {
        val len = nextByte()
        val ints = IntArray(len)
        for (i in 0 until len) ints[i] = nextInt()
        return ints
    }

    override fun nextIntsBB(): Array<IntArray?> {
        val len = nextByte()
        val ints = arrayOfNulls<IntArray>(len)
        for (i in 0 until len) ints[i] = nextIntsB()
        return ints
    }

    override fun nextLong(): Long {
        check(8)
        val ans: Long = DataIO.Companion.toLong(bs, index)
        index += 8
        return ans
    }

    override fun nextShort(): Int {
        check(2)
        val ans: Int = DataIO.Companion.toShort(bs, index)
        index += 2
        return ans
    }

    override fun nextString(): String {
        val bts = nextBytesB()
        return try {
            String(bts, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            String(bts)
        }
    }

    fun pos(): Int {
        return index - off
    }

    fun reread() {
        index = off
    }

    fun size(): Int {
        return max - off
    }

    fun skip(n: Int) {
        index += n
    }

    override fun subStream(): InStreamDef {
        val n = nextInt()
        if (n > size()) {
            Exception("error in getting subStream").printStackTrace()
            CommonStatic.def.exit(false)
        }
        val `is` = InStreamDef(bs, index, index + n)
        index += n
        return `is`
    }

    override fun toString(): String {
        return "InStreamDef " + size()
    }

    override fun translate(): OutStreamDef {
        val data = ByteArray(max - index)
        for (i in 0 until max - index) data[i] = bs[index + i].toByte()
        return OutStreamDef(data)
    }

    protected fun getBytes(): IntArray {
        return bs
    }

    private fun check(i: Int) {
        if (max - index < i) {
            val str = ("out of bound: " + (index - off) + "/" + (max - off) + ", " + index + "/" + max + "/" + off
                    + "/" + bs.size)
            throw BCUException(str)
        }
    }
}
