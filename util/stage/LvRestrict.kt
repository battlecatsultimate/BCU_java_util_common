package common.util.stage

import common.battle.LineUp
import common.io.InStream
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonClass
import common.io.json.JsonClass.JCIdentifier
import common.io.json.JsonEncoder
import common.io.json.JsonField
import common.io.json.JsonField.GenType
import common.io.json.Test
import common.io.json.Test.JsonTest_0.JsonD
import common.io.json.Test.JsonTest_2
import common.pack.IndexContainer.IndexCont
import common.pack.IndexContainer.Indexable
import common.pack.PackData
import common.pack.PackData.UserPack
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.pack.VerFixer.VerFixerException
import common.util.Data
import common.util.stage.CharaGroup
import common.util.stage.EStage
import common.util.stage.LvRestrict
import common.util.stage.StageMap
import common.util.stage.StageMap.StageMapInfo
import common.util.unit.Form
import common.util.unit.Level
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

@IndexCont(PackData::class)
@JsonClass
class LvRestrict : Data, Indexable<PackData?, LvRestrict?> {
    @JsonField(generic = [CharaGroup::class, IntArray::class], alias = [PackData.Identifier::class])
    val res: TreeMap<CharaGroup, IntArray> = TreeMap<CharaGroup, IntArray>()

    @JsonField(gen = GenType.FILL)
    var rares = Array(Data.Companion.RARITY_TOT) { IntArray(6) }
    var all = IntArray(6)

    @JCIdentifier
    var id: PackData.Identifier<LvRestrict>? = null
    var name = ""

    constructor(ID: PackData.Identifier<LvRestrict>?) {
        all = MAX.clone()
        for (i in 0 until Data.Companion.RARITY_TOT) rares[i] = MAX.clone()
        id = ID
    }

    constructor(ID: PackData.Identifier<LvRestrict>?, lvr: LvRestrict) {
        id = ID
        all = lvr.all.clone()
        for (i in 0 until Data.Companion.RARITY_TOT) rares[i] = lvr.rares[i].clone()
        for (cg in lvr.res.keys) res.put(cg, lvr.res.get(cg).clone())
    }

    @Deprecated("")
    constructor(mc: UserPack, `is`: InStream) {
        val ver: Int = Data.Companion.getVer(`is`.nextString())
        if (ver != 308) throw VerFixerException("LvRestrict requires 308, got $ver")
        name = `is`.nextString()
        id = PackData.Identifier.Companion.parseInt<LvRestrict>(`is`.nextInt(), LvRestrict::class.java)
        var tb: IntArray = `is`.nextIntsB()
        for (i in tb.indices) all[i] = tb[i]
        val tbb: Array<IntArray> = `is`.nextIntsBB()
        for (i in tbb.indices) for (j in 0 until tbb[i].length) rares[i][j] = tbb[i][j]
        val n: Int = `is`.nextInt()
        for (i in 0 until n) {
            val cg: Int = `is`.nextInt()
            val vals = IntArray(6)
            tb = `is`.nextIntsB()
            for (j in tb.indices) vals[j] = tb[j]
            val cgs: CharaGroup = mc.groups.get(cg)
            if (cgs != null) res.put(cgs, vals)
        }
    }

    private constructor(lvr: LvRestrict) {
        for (cg in lvr.res.keys) res.put(cg, lvr.res.get(cg).clone())
    }

    fun combine(lvr: LvRestrict): LvRestrict {
        val ans = LvRestrict(this)
        for (i in 0..5) ans.all[i] = Math.min(lvr.all[i], all[i])
        for (i in 0 until Data.Companion.RARITY_TOT) for (j in 0..5) ans.rares[i][j] = Math.min(lvr.rares[i][j], rares[i][j])
        for (cg in lvr.res.keys) if (res.containsKey(cg)) {
            val lv0: IntArray = res.get(cg)
            val lv1: IntArray = lvr.res.get(cg)
            val lv = IntArray(6)
            for (i in 0..5) lv[i] = Math.min(lv0[i], lv1[i])
            ans.res.put(cg, lv)
        } else ans.res.put(cg, lvr.res.get(cg).clone())
        return ans
    }

    override fun getID(): PackData.Identifier<LvRestrict>? {
        return id
    }

    fun isValid(lu: LineUp): Boolean {
        for (fs in lu.fs) for (f in fs) if (f != null) {
            val mlv = valid(f).lvs
            val flv: IntArray = lu.map.get(f.unit).getLvs()
            for (i in 0..5) if (mlv[i] < flv[i]) return false
        }
        return true
    }

    override fun toString(): String {
        return id.toString() + "-" + name
    }

    fun used(): Boolean {
        for (sm in (getCont() as UserPack).mc.maps) for (st in sm.list) if (st.lim != null && st.lim.lvr === this) return true
        return false
    }

    fun valid(f: Form): Level {
        val lv = MAX.clone()
        var mod = false
        for (cg in res.keys) if (cg.set.contains(f.unit)) {
            val rst: IntArray = res.get(cg)
            for (i in 0..5) lv[i] = Math.min(lv[i], rst[i])
            mod = true
        }
        if (mod) return Level(f.regulateLv(null, lv))
        for (i in 0..5) lv[i] = Math.min(lv[i], rares[f.unit.rarity][i])
        for (i in 0..5) lv[i] = Math.min(lv[i], all[i])
        return Level(f.regulateLv(null, lv))
    }

    fun validate(lu: LineUp) {
        for (fs in lu.fs) for (f in fs) if (f != null) lu.map.put(f.unit, valid(f))
        lu.renew()
    }

    companion object {
        @StaticPermitted
        val MAX = intArrayOf(120, 10, 10, 10, 10, 10)
    }
}
