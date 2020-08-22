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

class AttackVolcano(a: AttackSimple, sta: Double, end: Double) : AttackAb(a, sta, end) {
    var attacked = false
    protected val vcapt: HashMap<Entity, Int>
    override fun capture() {
        val le: List<AbEntity> = model.b.inRange(touch, dire, sta, end)
        capt.clear()
        for (e in le) if ((abi and Data.Companion.AB_ONLY == 0 || e.targetable(type)) && e is Entity && !vcapt.containsKey(e)) capt.add(e)
    }

    override fun excuse() {
        process()
        for (e in capt) {
            if (e.isBase()) continue
            if (e is Entity) {
                e.damaged(this)
                vcapt[e] = Data.Companion.VOLC_ITV
                attacked = true
            }
        }
        vcapt.entries.removeIf { ent: MutableMap.MutableEntry<Entity, Int> ->
            val n = ent.value - 1
            if (n > 0) ent.setValue(n)
            n == 0
        }
    }

    init {
        vcapt = HashMap()
        sta = sta
        end = end
        this.waveType = Data.Companion.WT_VOLC
    }
}
