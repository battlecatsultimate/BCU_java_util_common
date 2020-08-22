package common.battle.attack

import common.CommonStatic
import common.CommonStatic.BattleConst
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
import common.util.pack.EffAnim.VolcEff
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

class ContVolcano(v: AttackVolcano, p: Double, lay: Int, alive: Int) : ContAb(v.model.b, p, lay) {
    protected val anim: EAnimD<VolcEff>
    protected val v: AttackVolcano
    private val aliveTime: Int
    private var t = 0
    override fun draw(gra: FakeGraphics, p: P, psiz: Double) {
        val at: FakeTransform = gra.getTransform()
        anim.draw(gra, p, psiz)
        gra.setTransform(at)
        drawAxis(gra, p, psiz)
    }

    override fun update() {
        if (t > Data.Companion.VOLC_PRE && t <= Data.Companion.VOLC_PRE + aliveTime && anim.type != VolcEff.DURING) {
            anim.changeAnim(VolcEff.DURING)
            CommonStatic.setSE(Data.Companion.SE_VOLC_LOOP)
        } else if (t > Data.Companion.VOLC_PRE + aliveTime && anim.type != VolcEff.END) anim.changeAnim(VolcEff.END)
        if (t > Data.Companion.VOLC_PRE && t < Data.Companion.VOLC_PRE + aliveTime && (t - Data.Companion.VOLC_PRE) % Data.Companion.VOLC_SE == 0) {
            CommonStatic.setSE(Data.Companion.SE_VOLC_LOOP)
        }
        if (t >= aliveTime + Data.Companion.VOLC_POST + Data.Companion.VOLC_PRE) {
            activate = false
        } else {
            if (t > Data.Companion.VOLC_PRE && t <= Data.Companion.VOLC_PRE + aliveTime) sb.getAttack(v)
            anim.update(false)
            t++
        }
    }

    protected fun drawAxis(gra: FakeGraphics, p: P, siz: Double) {
        var siz = siz
        if (!CommonStatic.getConfig().ref) return

        // after this is the drawing of hit boxes
        siz *= 1.25
        val rat: Double = BattleConst.Companion.ratio
        val h = (640 * rat * siz).toInt()
        gra.setColor(FakeGraphics.Companion.MAGENTA)
        val d0: Double = Math.min(v.sta, v.end)
        val ra: Double = Math.abs(v.sta - v.end)
        val x = ((d0 - pos) * rat * siz + p.x) as Int
        val y = p.y as Int
        val w = (ra * rat * siz).toInt()
        if (v.attacked) {
            gra.fillRect(x, y, w, h)
            v.attacked = !v.attacked
        } else {
            gra.drawRect(x, y, w, h)
        }
    }

    init {
        anim = (if (v.dire == 1) Data.Companion.effas().A_E_VOLC else Data.Companion.effas().A_VOLC).getEAnim(VolcEff.START)
        this.v = v
        aliveTime = alive
        CommonStatic.setSE(Data.Companion.SE_VOLC_START)
    }
}
