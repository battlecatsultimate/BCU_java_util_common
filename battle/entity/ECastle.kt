package common.battle.entity

import common.CommonStatic
import common.battle.BasisLU
import common.battle.StageBasis
import common.battle.attack.AttackAb
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

class ECastle : AbEntity {
    private val sb: StageBasis

    constructor(b: StageBasis) : super(b.st.health) {
        sb = b
    }

    constructor(xb: StageBasis, b: BasisLU) : super(b.t().getBaseHealth()) {
        sb = xb
    }

    override fun damaged(atk: AttackAb) {
        var ans: Int = atk.atk
        if (atk.abi and Data.Companion.AB_BASE > 0) ans *= 4
        val satk: Int = atk.getProc().SATK.mult
        if (satk > 0) ans *= (100 + satk) * 0.01.toInt()
        if (atk.getProc().CRIT.mult > 0) {
            ans *= 0.01 * atk.getProc().CRIT.mult.toInt()
            sb.lea.add(EAnimCont(pos, 9, Data.Companion.effas().A_CRIT.getEAnim(DefEff.DEF)))
            CommonStatic.setSE(Data.Companion.SE_CRIT)
        } else CommonStatic.setSE(Data.Companion.SE_HIT_BASE)
        health -= ans.toLong()
        if (health > maxH) health = maxH
        if (health <= 0) health = 0
    }

    override fun getAbi(): Int {
        return 0
    }

    override fun isBase(): Boolean {
        return true
    }

    override fun postUpdate() {}
    override fun targetable(type: Int): Boolean {
        return true
    }

    override fun touchable(): Int {
        return Data.Companion.TCH_N
    }

    override fun update() {}
}
