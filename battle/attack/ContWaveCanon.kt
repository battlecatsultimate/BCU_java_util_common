package common.battle.attack

import common.CommonStatic
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
import common.util.pack.NyCastle.NyType
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

class ContWaveCanon(a: AttackWave, p: Double, private val canid: Int) : ContWaveAb(a, p, CommonStatic.getBCAssets().atks.get(canid).getEAnim(NyType.ATK), 9) {
    override fun draw(gra: FakeGraphics, p: P, psiz: Double) {
        var psiz = psiz
        drawAxis(gra, p, psiz)
        psiz *= if (canid == 0) 1.25 else 0.5 * 1.25
        val pus: P = if (canid == 0) P(9, 40) else P(-72, 0)
        anim.draw(gra, p.plus(pus, -psiz), psiz * 2)
    }

    fun getSize(): Double {
        return 2.5
    }

    protected override fun nextWave() {
        val np: Double = pos - 405
        ContWaveCanon(AttackWave(atk, np, Data.Companion.NYRAN.get(canid)), np, canid)
        CommonStatic.setSE(Data.Companion.SE_CANNON.get(canid).get(1))
    }
}
