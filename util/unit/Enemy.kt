package common.util.unit

import common.CommonStatic
import common.battle.StageBasis
import common.battle.data.CustomEnemy
import common.battle.data.DataEnemy
import common.battle.data.DataEntity
import common.battle.data.MaskEnemy
import common.battle.entity.EEnemy
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonEncoder
import common.io.json.Test
import common.io.json.Test.JsonTest_0.JsonD
import common.io.json.Test.JsonTest_2
import common.pack.PackData
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.system.VImg
import common.system.files.VFile
import common.util.Animable
import common.util.Data
import common.util.anim.AnimU
import common.util.anim.AnimU.UType
import common.util.anim.AnimUD
import common.util.anim.EAnimU
import common.util.lang.MultiLangCont
import common.util.stage.EStage
import common.util.stage.MapColc
import common.util.stage.MapColc.PackMapColc
import common.util.stage.Stage
import common.util.stage.StageMap
import common.util.stage.StageMap.StageMapInfo
import common.util.unit.Enemy
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

class Enemy : Animable<AnimU<*>?, UType?>, AbEnemy {
    val id: PackData.Identifier<AbEnemy>
    val de: MaskEnemy
    var name = ""
    var inDic = false

    constructor(hash: PackData.Identifier<AbEnemy>, ac: AnimU<*>?, ce: CustomEnemy) {
        id = hash
        de = ce
        ce.pack = this
        anim = ac
    }

    constructor(ID: PackData.Identifier<AbEnemy>, old: Enemy) {
        id = ID
        de = (old.de as CustomEnemy).copy(this)
        name = old.name
        anim = old.anim
    }

    constructor(f: VFile<*>) {
        id = PackData.Identifier(PackData.Identifier.Companion.DEF, Enemy::class.java, CommonStatic.parseIntN(f.getName()))
        val str = "./org/enemy/" + Data.Companion.trio(id.id) + "/"
        de = DataEnemy(this)
        anim = AnimUD(str, Data.Companion.trio(id.id) + "_e", "edi_" + Data.Companion.trio(id.id) + ".png", null)
        anim.getEdi().check()
    }

    fun findApp(): List<Stage> {
        val ans: MutableList<Stage> = ArrayList()
        for (st in MapColc.Companion.getAllStage()) if (st.contains(this)) ans.add(st)
        return ans
    }

    fun findApp(mc: MapColc): List<Stage> {
        val ans: MutableList<Stage> = ArrayList()
        for (sm in mc.maps) for (st in sm.list) if (st.contains(this)) ans.add(st)
        return ans
    }

    fun findMap(): List<MapColc> {
        val ans: MutableList<MapColc> = ArrayList<MapColc>()
        for (mc in MapColc.Companion.values()) {
            if (mc is PackMapColc) continue
            var col = false
            for (sm in mc.maps) {
                for (st in sm.list) if (st.contains(this).also { col = it }) {
                    ans.add(mc)
                    break
                }
                if (col) break
            }
        }
        return ans
    }

    override fun getEAnim(t: UType): EAnimU? {
        return if (anim == null) null else anim.getEAnim(t)
    }

    override fun getEntity(b: StageBasis, obj: Any?, mul: Double, mul2: Double, d0: Int, d1: Int, m: Int): EEnemy {
        var mul = mul
        var mul2 = mul2
        mul *= de.multi(b.b)
        mul2 *= de.multi(b.b)
        return EEnemy(b, de, getEAnim(UType.WALK), mul, mul2, d0, d1, m)
    }

    override fun getIcon(): VImg {
        return anim.getEdi()
    }

    override fun getID(): PackData.Identifier<AbEnemy>? {
        return id
    }

    override fun getPossible(): Set<Enemy> {
        val te: MutableSet<Enemy> = TreeSet<Enemy>()
        te.add(this)
        return te
    }

    override fun toString(): String {
        val desp: String = MultiLangCont.Companion.get(this)
        if (desp != null && desp.length > 0) return Data.Companion.trio(id.id) + " - " + desp
        return if (name.length == 0) Data.Companion.trio(id.id) else Data.Companion.trio(id.id) + " - " + name
    }
}
