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
import common.util.Data
import common.util.pack.EffAnim.DefEff
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

class ContWaveDef(a: AttackWave, p: Double, layer: Int) : ContWaveAb(a, p, (if (a.dire == 1) Data.Companion.effas().A_E_WAVE else Data.Companion.effas().A_WAVE).getEAnim(DefEff.DEF), layer) {
    protected override fun nextWave() {
        val dire: Int = atk.model.getDire()
        val np: Double = pos + Data.Companion.W_PROG * dire
        val wid: Int = if (dire == 1) Data.Companion.W_E_WID else Data.Companion.W_U_WID
        ContWaveDef(AttackWave(atk, np, wid.toDouble()), np, layer)
    }

    init {
        CommonStatic.setSE(Data.Companion.SE_WAVE)
    }
}
