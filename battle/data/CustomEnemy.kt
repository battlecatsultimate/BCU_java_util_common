package common.battle.data

import common.battle.Basis
import common.io.InStream
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonClass
import common.io.json.JsonEncoder
import common.io.json.JsonField
import common.io.json.Test
import common.io.json.Test.JsonTest_0.JsonD
import common.io.json.Test.JsonTest_2
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.util.Data
import common.util.stage.EStage
import common.util.stage.StageMap
import common.util.stage.StageMap.StageMapInfo
import common.util.unit.AbEnemy
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

@JsonClass
class CustomEnemy : CustomEntity(), MaskEnemy {
    var pack: Enemy? = null

    @JsonField
    var star = 0

    @JsonField
    var drop = 0
    fun copy(e: Enemy?): CustomEnemy {
        val ce = CustomEnemy()
        ce.importData(this)
        ce.pack = e
        return ce
    }

    fun fillData(ver: Int, `is`: InStream) {
        zread(ver, `is`)
    }

    override fun getDrop(): Double {
        return drop.toDouble()
    }

    override fun getPack(): Enemy? {
        return pack
    }

    override fun getStar(): Int {
        return star
    }

    override fun getSummon(): Set<AbEnemy> {
        val ans: MutableSet<AbEnemy> = TreeSet<AbEnemy>()
        for (adm in atks) if (adm.proc!!.SUMMON.prob > 0) ans.add(adm.proc!!.SUMMON.id.get() as AbEnemy)
        return ans
    }

    override fun importData(de: MaskEntity) {
        super.importData(de)
        if (de is MaskEnemy) {
            val me: MaskEnemy = de
            star = me.getStar()
            drop = me.getDrop() as Int
        }
    }

    override fun multi(b: Basis): Double {
        if (star > 0) return b.t().getStarMulti(star)
        return if (type and Data.Companion.TB_ALIEN > 0) b.t().getAlienMulti() else 1
    }

    private fun zread(`val`: Int, `is`: InStream) {
        var `val` = `val`
        `val` = Data.Companion.getVer(`is`.nextString())
        if (`val` >= 400) `zread$000400`(`is`)
    }

    private fun `zread$000400`(`is`: InStream) {
        zreada(`is`)
        star = `is`.nextByte()
        drop = `is`.nextInt()
    }

    init {
        rep = AtkDataModel(this)
        atks = arrayOfNulls<AtkDataModel>(1)
        atks.get(0) = AtkDataModel(this)
        width = 320
        speed = 8
        hp = 10000
        hb = 1
        type = 1
    }
}
