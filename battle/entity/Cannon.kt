package common.battle.entity

import common.CommonStatic
import common.CommonStatic.BattleConst
import common.battle.StageBasis
import common.battle.attack.*
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonEncoder
import common.io.json.Test
import common.io.json.Test.JsonTest_0.JsonD
import common.io.json.Test.JsonTest_2
import common.pack.PackData
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
import common.util.Data.Proc
import common.util.anim.AnimU.UType
import common.util.anim.EAnimD
import common.util.pack.NyCastle.NyType
import common.util.stage.EStage
import common.util.stage.StageMap
import common.util.stage.StageMap.StageMapInfo
import common.util.unit.Form
import common.util.unit.Unit
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

class Cannon(sb: StageBasis?, val id: Int) : AtkModelAb(sb) {
    private var anim: EAnimD<*>? = null
    private var atka: EAnimD<*>? = null
    private var exta: EAnimD<*>? = null
    private var preTime = 0
    private var wall: EUnit? = null
    var pos = 0.0
    private var tempAtk = false

    /** call when shoot the canon  */
    fun activate() {
        anim = CommonStatic.getBCAssets().atks.get(id).getEAnim(NyType.BASE)
        preTime = Data.Companion.NYPRE.get(id)
        CommonStatic.setSE(Data.Companion.SE_CANNON.get(id).get(0))
    }

    /** attack part of animation  */
    fun drawAtk(g: FakeGraphics, ori: P, siz: Double) {
        var siz = siz
        val at: FakeTransform = g.getTransform()
        if (atka != null) atka.draw(g, ori, siz)
        g.setTransform(at)
        if (exta != null) exta.draw(g, ori, siz)
        g.setTransform(at)
        if (!CommonStatic.getConfig().ref || id == Data.Companion.BASE_H || id == Data.Companion.BASE_SLOW || id == Data.Companion.BASE_GROUND) {
            g.delete(at)
            return
        }

        // after this is the drawing of hit boxes
        siz *= 1.25
        val rat: Double = BattleConst.Companion.ratio
        val h = (640 * rat * siz).toInt()
        g.setColor(FakeGraphics.Companion.MAGENTA)
        var d0 = pos
        val ra: Double = Data.Companion.NYRAN.get(id)
        if (id == Data.Companion.BASE_STOP || id == Data.Companion.BASE_WATER) d0 -= ra / 2
        if (id == Data.Companion.BASE_BARRIER) d0 -= ra
        val x = ((d0 - pos) * rat * siz + ori.x) as Int
        val y = ori.y as Int
        val w = (ra * rat * siz).toInt()
        if (tempAtk) g.fillRect(x, y, w, h) else g.drawRect(x, y, w, h)
        g.delete(at)
    }

    /** base part of animation  */
    fun drawBase(g: FakeGraphics?, ori: P?, siz: Double) {
        if (anim == null) return
        anim.draw(g, ori, siz)
    }

    override fun getAbi(): Int {
        return 0
    }

    override fun getDire(): Int {
        return -1
    }

    override fun getPos(): Double {
        return 0
    }

    fun update() {
        tempAtk = false
        if (anim != null && anim.done()) {
            anim = null
            if (id > 2 && id < 5) {
                atka = CommonStatic.getBCAssets().atks.get(id).getEAnim(NyType.ATK)
                CommonStatic.setSE(Data.Companion.SE_CANNON.get(id).get(1))
            }
        }
        if (atka != null && atka.done()) atka = null
        if (exta != null && exta.done()) exta = null
        if (anim != null) {
            if (id == 7) {
                if (anim.ind() < 32) {
                    anim.update(false)
                } else {
                    anim = null
                }
            } else {
                anim.update(false)
            }
        }
        if (atka != null) atka.update(false)
        if (exta != null) exta.update(false)
        if (anim == null && atka == null && exta == null) {
            if (id > 2 && id < 5) {
                pos = b.ubase.pos
                for (e in b.le) if (e.dire == -1 && e.pos < pos) pos = e.pos
                pos -= Data.Companion.NYRAN.get(id) / 2.toDouble()
            }
            if (id == 2 || id == 6) {
                pos = b.ebase.pos
                for (e in b.le) if (e.dire == 1 && e.pos > pos) pos = e.pos
            }
        }
        if (preTime == -1 && id == 2) {
            // wall canon
            val f: Form = PackData.Identifier.Companion.parseInt<Unit>(339, Unit::class.java).get().forms.get(0)
            val multi: Double = 0.01 * b.b.t().getCanonMulti(id)
            wall = EUnit(b, f.du, f.getEAnim(UType.ENTER), multi, null)
            b.le.add(wall)
            wall.added(-1, pos.toInt())
            preTime = b.b.t().getCanonProcTime(id)
        }
        if (preTime > 0) {
            preTime--
            if (preTime == 0) {
                val proc: Proc = Proc.Companion.blank()
                if (id == 0) {
                    // basic canon
                    proc.WAVE.lv = 12
                    proc.SNIPER.prob = 1
                    val wid: Double = Data.Companion.NYRAN.get(0)
                    val p: Double = b.ubase.pos - wid / 2 + 100
                    val atk: Int = b.b.t().getCanonAtk()
                    val eatk = AttackCanon(this, atk, -1, 0, proc, 0, 0)
                    ContWaveCanon(AttackWave(eatk, p, wid, Data.Companion.WT_CANN or Data.Companion.WT_WAVE), p, 0)
                } else if (id == 1) {
                    // slow canon
                    proc.SLOW.time = b.b.t().getCanonProcTime(id) * (100 + b.b.getInc(Data.Companion.C_SLOW)) / 100
                    val wid: Int = Data.Companion.NYRAN.get(1)
                    val spe = 137
                    val p: Double = b.ubase.pos - wid / 2 + spe
                    val eatk = AttackCanon(this, 0, -1, 0, proc, 0, 0)
                    ContMove(eatk, p, wid, spe, 1, 31, 0, 9)
                } else if (id == 2) {
                    // wall canon
                    if (wall != null) wall.kill()
                    wall = null
                } else if (id == 3) {
                    // freeze canon
                    tempAtk = true
                    proc.STOP.time = b.b.t().getCanonProcTime(id) * (100 + b.b.getInc(Data.Companion.C_STOP)) / 100
                    val atk = (b.b.t().getCanonAtk() * b.b.t().getCanonMulti(id) / 100) as Int
                    val rad: Int = Data.Companion.NYRAN.get(3) / 2
                    b.getAttack(AttackCanon(this, atk, -1, 0, proc, pos - rad, pos + rad))
                } else if (id == 4) {
                    // water canon
                    tempAtk = true
                    proc.CRIT.mult = -(b.b.t().getCanonMulti(id) / 10)
                    val rad: Int = Data.Companion.NYRAN.get(4) / 2
                    b.getAttack(AttackCanon(this, 1, 0, 0, proc, pos - rad, pos + rad))
                } else if (id == 5) {
                    // zombie canon
                    proc.WAVE.lv = 12
                    val wid: Double = Data.Companion.NYRAN.get(5)
                    proc.STOP.time = b.b.t().getCanonProcTime(5) * (100 + b.b.getInc(Data.Companion.C_STOP)) / 100
                    proc.SNIPER.prob = 1
                    val p: Double = b.ubase.pos - wid / 2 + 100
                    val eatk = AttackCanon(this, 0, Data.Companion.TB_ZOMBIE, Data.Companion.AB_ONLY or Data.Companion.AB_ZKILL, proc, 0, 0)
                    ContWaveCanon(AttackWave(eatk, p, wid, Data.Companion.WT_CANN or Data.Companion.WT_WAVE), p, 5)
                } else if (id == 6) {
                    // barrier canon
                    tempAtk = true
                    proc.BREAK.prob = 1
                    proc.KB.dis = Data.Companion.KB_DIS.get(Data.Companion.INT_KB)
                    proc.KB.time = Data.Companion.KB_TIME.get(Data.Companion.INT_KB)
                    val atk = (b.b.t().getCanonAtk() * b.b.t().getCanonMulti(id) / 100) as Int
                    val rad: Int = b.b.t().getCanonProcTime(id)
                    b.getAttack(AttackCanon(this, atk, -1, 0, proc, pos - rad, pos))
                    atka = CommonStatic.getBCAssets().atks.get(id).getEAnim(NyType.ATK)
                    exta = CommonStatic.getBCAssets().atks.get(id).getEAnim(NyType.EXT)
                } else if (id == 7) {
                    // curse cannon
                    tempAtk = true
                    proc.CURSE.time = b.b.t().getCanonProcTime(id)
                    val wid: Int = Data.Companion.NYRAN.get(7)
                    val spe = 137
                    val p: Double = b.ubase.pos - wid / 2 + spe
                    val eatk = AttackCanon(this, 0, -1, 0, proc, 0, 0)
                    ContMove(eatk, p, wid, spe, 1, 31, 0, 9)
                }
            }
        }
    }
}
