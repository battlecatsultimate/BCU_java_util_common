package common.battle.attack

import common.battle.entity.AbEntity
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
import common.util.Data
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
import java.util.*
import java.util.function.Predicate

class AttackWave : AttackAb {
    val incl: MutableSet<Entity>?

    constructor(a: AttackSimple, p0: Double, wid: Double, wt: Int) : super(a, p0 - wid / 2, p0 + wid / 2) {
        waveType = wt
        incl = HashSet()
        proc.WAVE.lv--
    }

    constructor(a: AttackWave, p0: Double, wid: Double) : super(a, p0 - wid / 2, p0 + wid / 2) {
        waveType = a.waveType
        incl = a.incl
        proc.WAVE.lv--
    }

    override fun capture() {
        val le: MutableList<AbEntity> = model.b.inRange(touch, dire, sta, end)
        if (incl != null) le.removeIf(Predicate<AbEntity> { e: AbEntity -> incl.contains(e) })
        capt.clear()
        if (abi and Data.Companion.AB_ONLY == 0) capt.addAll(le) else for (e in le) if (e.targetable(type)) capt.add(e)
    }

    override fun excuse() {
        process()
        for (e in capt) {
            if (e.isBase()) continue
            if (e is Entity) {
                e.damaged(this)
                incl!!.add(e as Entity)
            }
        }
    }
}
