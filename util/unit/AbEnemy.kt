package common.util.unit

import common.battle.StageBasis
import common.battle.data.DataEntity
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
import common.pack.IndexContainer.IndexCont
import common.pack.IndexContainer.Indexable
import common.pack.PackData
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.system.VImg
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

@IndexCont(PackData::class)
interface AbEnemy : Comparable<AbEnemy?>, Indexable<PackData?, AbEnemy?> {
    override operator fun compareTo(e: AbEnemy): Int {
        return getID()!!.compareTo(e.getID())
    }

    fun getEntity(sb: StageBasis, obj: Any?, mul: Double, mul1: Double, d0: Int, d1: Int, m: Int): EEnemy
    fun getIcon(): VImg
    override fun getID(): PackData.Identifier<AbEnemy>?
    fun getPossible(): Set<Enemy>
}
