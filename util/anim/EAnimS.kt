package common.util.anim

import common.CommonStatic
import common.battle.data.DataEntity
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
import common.util.ImgCore
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

class EAnimS(ia: AnimI<*, *>, mm: MaModel) : EAnimI(ia, mm) {
    override fun draw(g: FakeGraphics, ori: P, siz: Double) {
        ImgCore.Companion.set(g)
        g.translate(ori.x, ori.y)
        if (CommonStatic.getConfig().ref && !CommonStatic.getConfig().battle) {
            val p0: P = P(-200, 0).times(siz)
            val p1: P = P(400, 100).times(siz)
            val p2: P = P(0, -300).times(siz)
            g.drawRect(p0.x as Int, p0.y as Int, p1.x as Int, p1.y as Int)
            g.setColor(FakeGraphics.Companion.RED)
            g.drawLine(0, 0, p2.x as Int, p2.y as Int)
        }
        for (e in order) e.drawPart(g, P(siz, siz))
        if (sele >= 0 && sele < ent!!.size) ent!!.get(sele)!!.drawScale(g, P(siz, siz))
    }

    override fun ind(): Int {
        return 0
    }

    override fun len(): Int {
        return 0
    }

    override fun setTime(value: Int) {}
    override fun update(b: Boolean) {}
}
