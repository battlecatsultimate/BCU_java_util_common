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
import common.system.fake.FakeTransform
import common.util.ImgCore
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

class EAnimU(ani: AnimU<*>, i: UType) : EAnimD<UType?>(ani, ani.mamodel!!, ani.getMaAnim(i)) {
    override fun anim(): AnimU<*> {
        return a as AnimU<*>
    }

    override fun draw(g: FakeGraphics, ori: P, siz: Double) {
        if (f == -1) {
            f = 0
            setup()
        }
        ImgCore.Companion.set(g)
        val at: FakeTransform = g.getTransform()
        g.translate(ori.x, ori.y)
        if (CommonStatic.getConfig().ref && !CommonStatic.getConfig().battle) {
            val p0: P = P.Companion.newP(-200.0, 0.0).times(siz)
            val p1: P = P.Companion.newP(400.0, 100.0).times(siz)
            val p2: P = P.Companion.newP(0.0, -300.0).times(siz)
            g.drawRect(p0.x as Int, p0.y as Int, p1.x as Int, p1.y as Int)
            g.setColor(FakeGraphics.Companion.RED)
            g.drawLine(0, 0, p2.x as Int, p2.y as Int)
            P.Companion.delete(p0)
            P.Companion.delete(p1)
            P.Companion.delete(p2)
        }
        for (e in order) {
            val p: P = P.Companion.newP(siz, siz)
            e.drawPart(g, p)
            P.Companion.delete(p)
        }
        if (sele >= 0 && sele < ent!!.size) {
            val p: P = P.Companion.newP(siz, siz)
            ent!!.get(sele)!!.drawScale(g, p)
            P.Companion.delete(p)
        }
        g.setTransform(at)
        g.delete(at)
    }

    /** make this animation a component of another, used in warp and kb  */
    fun paraTo(base: EAnimD<*>?) {
        if (base == null) ent!!.get(0)!!.setPara(null) else ent!!.get(0)!!.setPara(base.ent!!.get(1))
    }

    init {
        type = i
    }
}
