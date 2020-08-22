package common.battle.data

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
import common.util.Data.Proc
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

class DataAtk(data: DefaultData, val index: Int) : MaskAtk {
    val data: DefaultData
    override fun getAtk(): Int {
        return when (index) {
            0 -> data.atk
            1 -> data.atk1
            2 -> data.atk2
            else -> 0
        }
    }

    override fun getLongPoint(): Int {
        return data.lds + data.ldr
    }

    override fun getProc(): Proc? {
        return data.proc
    }

    override fun getShortPoint(): Int {
        return data.lds
    }

    override fun isRange(): Boolean {
        return data.isrange
    }

    init {
        this.data = data
    }
}
