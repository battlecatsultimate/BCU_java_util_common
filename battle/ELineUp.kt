package common.battle

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
import common.util.stage.Limit
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

class ELineUp(lu: LineUp, sb: StageBasis) : BattleObj() {
    private val b: StageBasis
    val price: Array<IntArray>
    val cool: Array<IntArray>
    val maxC: Array<IntArray>

    /** reset cool down time of an unit  */
    operator fun get(i: Int, j: Int) {
        cool[i][j] = maxC[i][j]
    }

    /** count down the cool down time  */
    fun update() {
        for (i in 0..1) for (j in 0..4) if (cool[i][j] > 0) cool[i][j]--
    }

    init {
        b = sb
        price = Array(2) { IntArray(5) }
        cool = Array(2) { IntArray(5) }
        maxC = Array(2) { IntArray(5) }
        val lim: Limit = b.est.lim
        for (i in 0..1) for (j in 0..4) {
            if (lu.fs.get(i).get(j) == null) {
                price[i][j] = -1
                continue
            }
            price[i][j] = lu.efs.get(i).get(j).getPrice(b.st.map.price)
            maxC[i][j] = sb.b.t().getFinRes(lu.fs.get(i).get(j).du.getRespawn())
            if (lim == null) continue
            val overl = lim.rare shr lu.fs.get(i).get(j).unit.rarity
            if (lim.rare != 0 && overl and 1 == 0) price[i][j] = -1
            if (lim.line == 1 && i == 1) price[i][j] = -1
            if (lim.min > 0 && price[i][j] < lim.min) price[i][j] = -1
            if (lim.max > 0 && price[i][j] > lim.max) price[i][j] = -1
            if (lim.group != null && !lim.group.allow(lu.fs.get(i).get(j).unit)) price[i][j] = -1
        }
    }
}
