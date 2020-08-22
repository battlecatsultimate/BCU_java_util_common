package common.battle.entity

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
import common.util.BattleObj
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

abstract class AbEntity protected constructor(h: Int) : BattleObj() {
    var health: Long
    var maxH: Long
    var dire = 0
    var pos = 0.0
    fun added(d: Int, p: Int) {
        pos = p.toDouble()
        dire = d
    }

    abstract fun damaged(atk: AttackAb)
    abstract fun getAbi(): Int
    abstract fun isBase(): Boolean
    abstract fun postUpdate()
    abstract fun targetable(type: Int): Boolean
    abstract fun touchable(): Int
    abstract fun update()

    init {
        var h = h
        if (h <= 0) h = 1
        maxH = h.toLong()
        health = maxH
    }
}
