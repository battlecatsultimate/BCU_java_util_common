package common.battle

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import common.CommonStatic
import common.battle.BasisLU
import common.io.InStream
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonClass
import common.io.json.JsonDecoder
import common.io.json.JsonEncoder
import common.io.json.JsonField
import common.io.json.JsonField.GenType
import common.io.json.JsonField.IOType
import common.io.json.Test
import common.io.json.Test.JsonTest_0.JsonD
import common.io.json.Test.JsonTest_2
import common.pack.Context
import common.pack.Context.ErrType
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.pack.UserProfile
import common.pack.VerFixer.VerFixerException
import common.system.Copable
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
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import java.util.function.Supplier

@JsonClass
class BasisSet : Basis, Copable<BasisSet?> {
    @JsonField(gen = GenType.FILL)
    private val t: Treasure

    @JsonField(generic = [BasisLU::class], gen = GenType.GEN)
    val lb: ArrayList<BasisLU?> = ArrayList<BasisLU?>()
    var sele: BasisLU? = null

    constructor() {
        if (listRaw().size == 0) name = "temporary" else name = "set " + listRaw().size
        t = Treasure(this)
        setCurrent(this)
        lb.add(BasisLU(this).also { sele = it })
        listRaw().add(this)
    }

    constructor(ref: BasisSet) {
        name = "set " + list().size
        list().add(this)
        t = Treasure(this, ref.t)
        setCurrent(this)
        for (blu in ref.lb) lb.add(BasisLU(this, blu).also { sele = it })
    }

    @Deprecated("")
    private constructor(ver: Int, `is`: InStream) {
        name = `is`.nextString()
        t = Treasure(this, ver, `is`)
        zread(ver, `is`)
    }

    fun add(): BasisLU? {
        lb.add(BasisLU(this).also { sele = it })
        return sele
    }

    override fun copy(): BasisSet {
        return BasisSet(this)
    }

    fun copyCurrent(): BasisLU? {
        lb.add(BasisLU(this, sele).also { sele = it })
        return sele
    }

    /** BasisSet are used in data display, so cannot be effected by combo  */
    override fun getInc(type: Int): Int {
        return 0
    }

    fun remove(): BasisLU {
        lb.remove(sele)
        return lb[0].also { sele = it }!!
    }

    override fun t(): Treasure {
        return t
    }

    @JsonField(tag = "sele", io = IOType.R)
    fun zgen(ind: Int) {
        sele = lb[ind]
    }

    @JsonField(tag = "sele", io = IOType.W)
    fun zser(): Int {
        return lb.indexOf(sele)
    }

    @Deprecated("")
    @Throws(VerFixerException::class)
    private fun zread(`val`: Int, `is`: InStream) {
        var `val` = `val`
        `val` = Data.Companion.getVer(`is`.nextString())
        if (`val` != 308) throw VerFixerException("basis set has to have version 308, got $`val`")
        val n: Int = `is`.nextInt()
        for (i in 0 until n) {
            val str: String = `is`.nextString()
            val ints: IntArray = `is`.nextIntsB()
            val sub: InStream = `is`.subStream()
            val bl = BasisLU(this, LineUp(308, sub), str, ints)
            lb.add(bl)
        }
        val ind: Int = `is`.nextInt()
        sele = lb[ind]
    }

    companion object {
        fun current(): BasisSet {
            return UserProfile.Companion.getStatic<BasisSet>("BasisSet_current", Supplier { def() })
        }

        fun def(): BasisSet {
            listRaw()
            return UserProfile.Companion.getStatic<BasisSet>("BasisSet_default", Supplier { BasisSet() })
        }

        fun list(): MutableList<BasisSet> {
            def()
            return listRaw()
        }

        fun read() {
            def()
            val f: File = CommonStatic.ctx.getUserFile("./basis.json")
            if (f.exists()) try {
                FileReader(f).use { r ->
                    val je: JsonElement = JsonParser.parseReader(r)
                    r.close()
                    val jel: JsonElement = je.getAsJsonObject().get("list")
                    JsonDecoder.Companion.decode<Array<BasisSet>>(jel, Array<BasisSet>::class.java)
                    val cur: Int = je.getAsJsonObject().get("current").getAsInt()
                    setCurrent(list()[cur])
                }
            } catch (e: Exception) {
                CommonStatic.ctx.noticeErr(e, ErrType.WARN, "failed to read basis data")
            }
        }

        @Deprecated("")
        @Throws(VerFixerException::class)
        fun read(`is`: InStream) {
            zreads(`is`, false)
        }

        fun setCurrent(cur: BasisSet?) {
            UserProfile.Companion.setStatic("BasisSet_current", cur)
        }

        fun write() {
            val target: File = CommonStatic.ctx.getUserFile("./basis.json")
            val temp: File = CommonStatic.ctx.getUserFile("./.temp.basis.json")
            try {
                FileWriter(temp).use { w ->
                    Context.Companion.check(temp)
                    val list: List<BasisSet> = list()
                    val cur = list.indexOf(current())
                    val arr = arrayOfNulls<BasisSet>(list.size - 1)
                    for (i in arr.indices) arr[i] = list[i + 1]
                    val ans = JsonObject()
                    ans.add("list", JsonEncoder.Companion.encode(arr))
                    ans.addProperty("current", cur)
                    w.write(ans.toString())
                    w.close()
                    Context.Companion.delete(target)
                    temp.renameTo(target)
                }
            } catch (e: Exception) {
                CommonStatic.ctx.noticeErr(e, ErrType.ERROR, "failed to save basis data")
            }
        }

        private fun listRaw(): MutableList<BasisSet> {
            return UserProfile.Companion.getStatic<ArrayList<BasisSet>>("BasisSet_list", Supplier { ArrayList<BasisSet?>() })
        }

        @Throws(VerFixerException::class)
        private fun zreads(`is`: InStream, bac: Boolean): List<BasisSet> {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 308) throw VerFixerException("basis set has to have version 308, got $ver")
            val ans = if (bac) ArrayList() else list()
            val n: Int = `is`.nextInt()
            for (i in 1 until n) {
                val bs = BasisSet(ver, `is`.subStream())
                ans.add(bs)
            }
            val ind = Math.max(`is`.nextInt(), ans.size - 1)
            if (!bac) setCurrent(list()[ind])
            return ans
        }
    }
}
