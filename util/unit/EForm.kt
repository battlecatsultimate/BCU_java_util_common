package common.util.unit

import common.battle.StageBasis
import common.battle.data.DataEntity
import common.battle.data.MaskUnit
import common.battle.data.PCoin
import common.battle.entity.EUnit
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
import common.util.Data
import common.util.anim.AnimU.UType
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

class EForm : Data {
    private val f: Form
    private val level: Level
    var du: MaskUnit? = null

    constructor(form: Form, vararg level: Int) {
        f = form
        val lvs = level
        this.level = Level(lvs)
        val pc: PCoin? = f.pCoin
        du = if (pc != null) pc.improve(lvs) else form.du
    }

    constructor(form: Form, level: Level) {
        f = form
        this.level = level
        val pc: PCoin? = f.pCoin
        du = if (pc != null) pc.improve(level.lvs) else form.du
    }

    fun getEntity(b: StageBasis?): EUnit {
        val d = f.unit.lv.getMult(level.lvs[0])
        return EUnit(b, du, f.getEAnim(UType.WALK), d, level)
    }

    fun getPrice(sta: Int): Int {
        return (du.getPrice() * (1 + sta * 0.5))
    }
}
