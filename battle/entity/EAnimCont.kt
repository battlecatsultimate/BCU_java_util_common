package common.battle.entity

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
import common.system.P
import common.system.fake.FakeGraphics
import common.util.BattleObj
import common.util.anim.EAnimD
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

open class EAnimCont(val pos: Double, val layer: Int, ead: EAnimD<*>) : BattleObj() {
    private val anim: EAnimD<*>

    /** return whether this animation is finished  */
    fun done(): Boolean {
        return anim.done()
    }

    open fun draw(gra: FakeGraphics, p: P, psiz: Double) {
        anim.draw(gra, p, psiz)
    }

    open fun update() {
        anim.update(false)
    }

    init {
        anim = ead
    }
}
