package common.battle

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

abstract class BattleField {
    var sb: StageBasis

    protected constructor(stage: EStage, bas: BasisLU, ints: IntArray, seed: Long) {
        sb = StageBasis(stage, bas, ints, seed)
    }

    protected constructor(bas: StageBasis) {
        sb = bas
    }

    open fun update() {
        sb.time++
        actions()
        sb.update()
    }

    protected fun act_can(): Boolean {
        return sb.act_can()
    }

    protected fun act_lock(i: Int, j: Int) {
        sb.act_lock(i, j)
    }

    protected fun act_mon(): Boolean {
        return sb.act_mon()
    }

    protected fun act_sniper(): Boolean {
        return sb.act_sniper()
    }

    protected fun act_spawn(i: Int, j: Int, boo: Boolean): Boolean {
        return sb.act_spawn(i, j, boo)
    }

    protected abstract fun actions()
}
