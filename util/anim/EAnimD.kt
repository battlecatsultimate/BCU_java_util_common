package common.util.anim

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
import common.util.anim.AnimI.AnimType
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

open class EAnimD<T>(ia: AnimI<*, T>, mm: MaModel, anim: MaAnim?) : EAnimI(ia, mm) where T : Enum<T>?, T : AnimType<*, T>? {
    var type: T? = null
    protected var ma: MaAnim?
    protected var f = -1
    fun changeAnim(t: T) {
        f = -1
        ma = (anim() as AnimD<*, T>).getMaAnim(t)
        type = t
    }

    fun done(): Boolean {
        return f > ma!!.max
    }

    override fun draw(g: FakeGraphics, ori: P, siz: Double) {
        if (f == -1) {
            f = 0
            setup()
        }
        ImgCore.Companion.set(g)
        g.translate(ori.x, ori.y)
        for (e in order) {
            val p: P = P.Companion.newP(siz, siz)
            e.drawPart(g, p)
            P.Companion.delete(p)
        }
    }

    override fun ind(): Int {
        return f
    }

    override fun len(): Int {
        return ma!!.max + 1
    }

    override fun setTime(value: Int) {
        setup()
        f = value
        ma!!.update(f, this, true)
    }

    fun setup() {
        ma!!.update(0, this, false)
    }

    override fun update(rotate: Boolean) {
        f++
        ma!!.update(f, this, rotate)
    }

    protected override fun performDeepCopy() {
        super.performDeepCopy()
        (copy as EAnimD<*>).setTime(f)
    }

    init {
        ma = anim
    }
}
