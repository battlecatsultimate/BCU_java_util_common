package common.battle.data

import common.io.InStream
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonClass
import common.io.json.JsonClass.NoTag
import common.io.json.JsonEncoder
import common.io.json.JsonField
import common.io.json.JsonField.GenType
import common.io.json.Test
import common.io.json.Test.JsonTest_0.JsonD
import common.io.json.Test.JsonTest_2
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.util.Data.Proc
import common.util.pack.Soul
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
import java.util.*

@JsonClass(noTag = NoTag.LOAD)
abstract class CustomEntity : DataEntity() {
    @JsonField(gen = GenType.GEN)
    var rep: AtkDataModel? = null

    @JsonField(gen = GenType.GEN)
    var rev: AtkDataModel? = null

    @JsonField(gen = GenType.GEN)
    var res: AtkDataModel? = null

    @JsonField(gen = GenType.GEN, usePool = true)
    var atks: Array<AtkDataModel?>?
    var tba = 0
    var base = 0
    var touch: Int = common.util.Data.Companion.TCH_N
    var common = true
    private var all: Proc? = null
    override fun allAtk(): Int {
        var ans = 0
        var temp = 0
        var c = 1
        for (adm in atks!!) if (adm!!.pre > 0) {
            ans += temp / c
            temp = if (adm!!.getDire() > 0) adm!!.atk else 0
            c = 1
        } else {
            temp += if (adm!!.getDire() > 0) adm!!.atk else 0
            c++
        }
        ans += temp / c
        return ans
    }

    override fun getAllProc(): Proc? {
        if (all != null) return all
        all = rep!!.getProc()!!.clone()
        for (adm in atks!!) {
            for (i in 0 until common.util.Data.Companion.PROC_TOT) if (!all.getArr(i).exists()) all.getArr(i).set(adm!!.proc!!.getArr(i))
        }
        return all
    }

    fun getAllProcs(): Array<Proc?> {
        val n = atks!!.size + 1
        val ans: Array<Proc?> = arrayOfNulls<Proc>(n)
        ans[0] = rep!!.proc
        for (i in atks!!.indices) ans[i + 1] = atks!![i]!!.proc
        return ans
    }

    override fun getAtkCount(): Int {
        return atks!!.size
    }

    override fun getAtkModel(ind: Int): MaskAtk? {
        if (ind < atks!!.size) return atks!![ind]
        if (ind == atks!!.size) return rev
        return if (ind == atks!!.size + 1) res else null
    }

    fun getAvailable(str: String): String {
        var str = str
        while (contains(str)) str += "'"
        return str
    }

    override fun getItv(): Int {
        return getAnimLen() + tba
    }

    override fun getPost(): Int {
        var ans: Int = getAnimLen()
        for (adm in atks!!) ans -= adm!!.pre
        return ans
    }

    override fun getProc(): Proc? {
        return rep!!.getProc()
    }

    override fun getRepAtk(): MaskAtk? {
        return rep
    }

    override fun getResurrection(): AtkDataModel? {
        return res
    }

    override fun getRevenge(): AtkDataModel? {
        return rev
    }

    override fun getTBA(): Int {
        return tba
    }

    override fun getTouch(): Int {
        return touch
    }

    open fun importData(de: MaskEntity) {
        hp = de.getHp()
        hb = de.getHb()
        speed = de.getSpeed()
        range = de.getRange()
        abi = de.getAbi()
        loop = de.getAtkLoop()
        type = de.getType()
        width = de.getWidth()
        shield = de.getShield()
        tba = de.getTBA()
        touch = de.getTouch()
        death = de.getDeathAnim()
        if (de is CustomEntity) {
            `importData$1`(de)
            return
        }
        base = de.touchBase()
        common = false
        rep = AtkDataModel(this)
        rep.proc = de.getRepAtk()!!.getProc().clone()
        val m: Int = de.getAtkCount()
        atks = arrayOfNulls<AtkDataModel>(m)
        for (i in 0 until m) atks!![i] = AtkDataModel(this, de, i)
    }

    override fun isLD(): Boolean {
        var ans = false
        for (adm in atks!!) ans = ans or adm!!.isLD()
        return ans
    }

    override fun isOmni(): Boolean {
        var ans = false
        for (adm in atks!!) ans = ans or adm!!.isOmni()
        return ans
    }

    override fun isRange(): Boolean {
        for (adm in atks!!) if (adm!!.range) return true
        return false
    }

    override fun rawAtkData(): Array<IntArray?> {
        val ans = arrayOfNulls<IntArray>(atks!!.size)
        for (i in atks!!.indices) ans[i] = atks!![i]!!.getAtkData()
        return ans
    }

    override fun touchBase(): Int {
        return if (base == 0) range else base
    }

    protected fun zreada(`is`: InStream) {
        val ver: Int = common.util.Data.Companion.getVer(`is`.nextString())
        if (ver >= 404) `zreada$000404`(`is`)
    }

    private operator fun contains(str: String): Boolean {
        if (atks == null || atks!!.size == 0) return false
        for (adm in atks!!) if (adm != null && adm.str == str) return true
        return false
    }

    private fun `importData$1`(ce: CustomEntity) {
        base = ce.base
        common = ce.common
        rep = AtkDataModel(this, ce.rep)
        val temp: MutableList<AtkDataModel?> = ArrayList<AtkDataModel?>()
        val tnew: MutableList<AtkDataModel> = ArrayList<AtkDataModel>()
        val inds = IntArray(ce.atks!!.size)
        for (i in ce.atks!!.indices) {
            if (!temp.contains(ce.atks!![i])) {
                temp.add(ce.atks!![i])
                tnew.add(AtkDataModel(this, ce.atks!![i]))
            }
            inds[i] = temp.indexOf(ce.atks!![i])
        }
        atks = arrayOfNulls<AtkDataModel>(ce.atks!!.size)
        for (i in atks!!.indices) atks!![i] = tnew[inds[i]]
    }

    private fun `zreada$000404`(`is`: InStream) {
        hp = `is`.nextInt()
        hb = `is`.nextInt()
        speed = `is`.nextInt()
        range = `is`.nextInt()
        abi = `is`.nextInt()
        type = `is`.nextInt()
        width = `is`.nextInt()
        shield = `is`.nextInt()
        tba = `is`.nextInt()
        base = `is`.nextInt()
        touch = `is`.nextInt()
        loop = `is`.nextInt()
        death = common.pack.PackData.Identifier.Companion.parseInt<Soul>(`is`.nextInt(), Soul::class.java)
        common = `is`.nextInt() > 0
        rep = AtkDataModel(this, `is`)
        val m: Int = `is`.nextInt()
        val set: Array<AtkDataModel?> = arrayOfNulls<AtkDataModel>(m)
        for (i in 0 until m) set[i] = AtkDataModel(this, `is`)
        val n: Int = `is`.nextInt()
        atks = arrayOfNulls<AtkDataModel>(n)
        for (i in 0 until n) atks!![i] = set[`is`.nextInt()]
        val adi: Int = `is`.nextInt()
        if (adi and 1 > 0) rev = AtkDataModel(this, `is`)
        if (adi and 2 > 0) res = AtkDataModel(this, `is`)
    }
}
