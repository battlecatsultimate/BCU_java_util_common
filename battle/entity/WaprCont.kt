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
import common.system.fake.FakeTransform
import common.util.Data
import common.util.anim.EAnimD
import common.util.anim.EAnimU
import common.util.pack.EffAnim.WarpEff
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

class WaprCont(p: Double, pa: WarpEff?, layer: Int, a: EAnimU, dire: Int) : EAnimCont(p, layer, Data.Companion.effas().A_W.getEAnim(pa)) {
    private val ent: EAnimU
    private val chara: EAnimD<*>
    val dire: Int
    override fun draw(gra: FakeGraphics, p: P, psiz: Double) {
        val at: FakeTransform = gra.getTransform()
        p.y -= 275 * psiz
        super.draw(gra, p, psiz)
        gra.setTransform(at)
        p.y += 275 * psiz
        ent.paraTo(chara)
        ent.draw(gra, p, psiz)
        ent.paraTo(null)
        gra.delete(at)
    }

    override fun update() {
        super.update()
        chara.update(false)
    }

    init {
        ent = a
        chara = Data.Companion.effas().A_W_C.getEAnim(pa)
        this.dire = dire
    }
}
