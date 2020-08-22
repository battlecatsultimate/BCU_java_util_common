package common.util.stage

import common.io.InStream
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonClass
import common.io.json.JsonEncoder
import common.io.json.JsonField
import common.io.json.Test
import common.io.json.Test.JsonTest_0.JsonD
import common.io.json.Test.JsonTest_2
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.system.BasedCopable
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

@JsonClass
class SCGroup(@field:JsonField val id: Int, vararg ns: Int) : Data(), BasedCopable<SCGroup?, Int?> {
    @JsonField
    private val max = intArrayOf(-1, -1, -1, -1)
    override fun copy(id: Int): SCGroup {
        return SCGroup(id, *max)
    }

    fun getMax(star: Int): Int {
        return if (max[star] == -1 && star > 0) getMax(star - 1) else max[star]
    }

    fun setMax(`val`: Int, star: Int) {
        if (star == -1) {
            for (i in max.indices) max[i] = `val`
            return
        }
        max[star] = `val`
        if (star > 0 && max[star - 1] < 0) setMax(`val`, star - 1)
    }

    override fun toString(): String {
        var str: String = Data.Companion.trio(id) + " - " + max[0]
        var temp = ""
        for (i in 1..3) if (max[i] == -1) return str else if (max[i] == max[i - 1]) temp += "," + max[i] else {
            str += temp + "," + max[i]
            temp = ""
        }
        return str
    }

    companion object {
        fun zread(`is`: InStream): SCGroup? {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver == 404) {
                val id: Int = `is`.nextInt()
                val n: Int = `is`.nextInt()
                val max = IntArray(n)
                for (i in 0 until n) max[i] = `is`.nextInt()
                `is`.nextInt()
                return SCGroup(id, *max)
            }
            return null
        }
    }

    init {
        for (i in 0 until ns.size) max[i] = ns[i]
    }
}
