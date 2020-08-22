package common.util.stage

import common.io.InStream
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonClass
import common.io.json.JsonClass.*
import common.io.json.JsonEncoder
import common.io.json.JsonField
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
import common.util.Data
import common.util.stage.CharaGroup
import common.util.stage.EStage
import common.util.stage.StageMap
import common.util.stage.StageMap.StageMapInfo
import common.util.unit.Unit
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
@JsonClass(noTag = NoTag.LOAD)
@JCGeneric(PackData.Identifier::class)
class CharaGroup : Data, Indexable<PackData?, CharaGroup?>, Comparable<CharaGroup?> {
    var name = ""

    @JCIdentifier
    var id: PackData.Identifier<CharaGroup>? = null
    var type = 0

    @JsonField(generic = [Unit::class], alias = [PackData.Identifier::class])
    val set: TreeSet<Unit> = TreeSet<Unit>()

    constructor(cg: CharaGroup) {
        type = cg.type
        set.addAll(cg.set)
    }

    constructor(id: PackData.Identifier<CharaGroup>?) {
        this.id = id
    }

    constructor(ID: Int, t: Int, units: Array<PackData.Identifier<Unit?>?>) : this(t, *units) {
        id = PackData.Identifier.Companion.parseInt<CharaGroup>(ID, CharaGroup::class.java)
    }

    @Deprecated("")
    constructor(mc: UserPack, `is`: InStream) {
        val ver: Int = Data.Companion.getVer(`is`.nextString())
        if (ver == 308) {
            name = `is`.nextString()
            id = mc.getID<CharaGroup>(CharaGroup::class.java, `is`.nextInt())
            type = `is`.nextInt()
            val m: Int = `is`.nextInt()
            for (j in 0 until m) {
                val u: Unit = PackData.Identifier.Companion.parseInt<Unit>(`is`.nextInt(), Unit::class.java).get()
                if (u != null) set.add(u)
            }
        }
    }

    private constructor(t: Int, vararg units: PackData.Identifier<Unit>) {
        type = t
        for (uid in units) {
            val u = uid.get()
            if (u != null) set.add(u)
        }
    }

    fun allow(u: Unit?): Boolean {
        return if (type == 0 && !set.contains(u) || type == 2 && set.contains(u)) false else true
    }

    fun combine(cg: CharaGroup): CharaGroup {
        val ans = CharaGroup(this)
        if (type == 0 && cg.type == 0) ans.set.retainAll(cg.set) else if (type == 0 && cg.type == 2) ans.set.removeAll(cg.set) else if (type == 2 && cg.type == 0) {
            ans.type = 0
            ans.set.addAll(cg.set)
            ans.set.removeAll(set)
        } else if (type == 2 && cg.type == 2) ans.set.addAll(cg.set)
        return ans
    }

    override operator fun compareTo(cg: CharaGroup): Int {
        return id!!.compareTo(cg.id)
    }

    override fun getID(): PackData.Identifier<CharaGroup>? {
        return id
    }

    override fun toString(): String {
        return id.toString() + " - " + name
    }

    fun used(): Boolean {
        val mc: UserPack = getCont() as UserPack
        for (lr in mc.lvrs.getList()) if (lr.res.containsKey(this)) return true
        for (sm in mc.mc.maps) for (st in sm.list) if (st.lim != null && st.lim.group === this) return true
        return false
    }
}
