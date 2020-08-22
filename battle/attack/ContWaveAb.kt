package common.battle.attack

import common.CommonStatic
import common.CommonStatic.BattleConst
import common.battle.entity.Entity
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

abstract class ContWaveAb protected constructor(a: AttackWave, p: Double, ead: EAnimD<*>, layer: Int) : ContAb(a.model.b, p, layer) {
    protected val atk: AttackWave
    protected val anim: EAnimD<*>
    private var t = 0
    private val maxt: Int
    private var tempAtk = false
    override fun draw(gra: FakeGraphics, p: P, siz: Double) {
        val at: FakeTransform = gra.getTransform()
        anim.draw(gra, p, siz)
        gra.setTransform(at)
        drawAxis(gra, p, siz)
        gra.delete(at)
    }

    override fun update() {
        tempAtk = false
        if (t == Data.Companion.W_TIME) {
            atk.capture()
            for (e in atk.capt) if (e.getAbi() and Data.Companion.AB_WAVES > 0) {
                if (e is Entity) (e as Entity).anim.getEff(Data.Companion.STPWAVE)
                activate = false
                return
            }
            sb.getAttack(atk)
            tempAtk = true
            if (atk.getProc().WAVE.exists()) nextWave()
        }
        if (maxt == t) activate = false
        anim.update(false)
        t++
    }

    protected fun drawAxis(gra: FakeGraphics, p: P, siz: Double) {
        var siz = siz
        if (!CommonStatic.getConfig().ref) return

        // after this is the drawing of hit boxes
        siz *= 1.25
        val rat: Double = BattleConst.Companion.ratio
        val h = (640 * rat * siz).toInt()
        gra.setColor(FakeGraphics.Companion.MAGENTA)
        val d0: Double = Math.min(atk.sta, atk.end)
        val ra: Double = Math.abs(atk.sta - atk.end)
        val x = ((d0 - pos) * rat * siz + p.x) as Int
        val y = p.y as Int
        val w = (ra * rat * siz).toInt()
        if (tempAtk) gra.fillRect(x, y, w, h) else gra.drawRect(x, y, w, h)
    }

    /** generate the next wave container  */
    protected abstract fun nextWave()

    init {
        atk = a
        anim = ead
        maxt = anim.len()
    }
}
