package common.battle.entity

import common.battle.StageBasis
import common.battle.attack.AtkModelAb
import common.battle.attack.AttackAb
import common.battle.attack.AttackSimple
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
import common.util.Data
import common.util.Data.Proc
import common.util.anim.EAnimD
import common.util.pack.EffAnim.SniperEff
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

class Sniper(sb: StageBasis?) : AtkModelAb(sb) {
    private val anim: EAnimD<*> = Data.Companion.effas().A_SNIPER.getEAnim(SniperEff.IDLE)
    private val atka: EAnimD<*> = Data.Companion.effas().A_SNIPER.getEAnim(SniperEff.ATK)
    private var coolTime: Int = Data.Companion.SNIPER_CD
    private var preTime = 0
    private var atkTime = 0
    private var angle = 0
    var enabled = true
    var canDo = true
    var pos = 0.0
    var height = 0.0
    var updown = 0.0

    /** attack part of animation  */
    fun drawAtk(g: FakeGraphics?, ori: P?, siz: Double) {
        // TODO
    }

    /** base part of animation  */
    fun drawBase(gra: FakeGraphics?, ori: P, siz: Double) {
        // TODO
        // double angle = Math.atan2(getPos() - pos, height);
        height = ori.y
        if (atkTime == 0) anim.draw(gra, ori, siz) else atka.draw(gra, ori, siz)
    }

    override fun getAbi(): Int {
        return 0
    }

    override fun getDire(): Int {
        return -1
    }

    override fun getPos(): Double {
        return b.st.len + Data.Companion.SNIPER_POS
    }

    fun update() {
        if (canDo && b.ubase.health <= 0) {
            canDo = false
        }
        if (enabled && coolTime > 0) coolTime--
        if (coolTime == 0 && enabled && pos > 0 && canDo) {
            coolTime = Data.Companion.SNIPER_CD
            preTime = Data.Companion.SNIPER_PRE
            atkTime = atka.len()
            atka.setup()
        }
        if (preTime > 0) {
            preTime--
            if (preTime == 0) {
                // attack
                val atk: Int = b.b.t().getBaseHealth() / 20
                val proc: Proc = Proc.Companion.blank()
                proc.SNIPER.prob = 1
                val a: AttackAb = AttackSimple(this, atk, -1, 0, proc, 0, getPos(), false, null)
                a.canon = -1
                b.getAttack(a)
            }
        }
        if (atkTime > 0) {
            atkTime--
            atka.update(false)
        } else {
            anim.update(true)
        }

        // find enemy pos
        pos = -1.0
        for (e in b.le) if (e.dire == 1 && e.pos > pos && !e.isBase && e.touchable() and Data.Companion.TCH_N > 0) pos = e.pos

        // Get angle of cannon and bullet
        angle = (-Math.toDegrees(Math.atan(height / (getPos() - pos)))).toInt() * 10
        anim.ent.get(5).alter(11, angle)
        atka.ent.get(5).alter(11, angle)

        // Get distance which bullet will fly
        // path = new P(-(getPos()-pos+300),height);
    }
}
