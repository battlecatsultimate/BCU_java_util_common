package common.battle.data

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
import common.pack.Context.ErrType
import common.pack.PackData
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.system.files.VFile
import common.util.Data
import common.util.Data.Proc.ProcItem
import common.util.stage.EStage
import common.util.stage.StageMap
import common.util.stage.StageMap.StageMapInfo
import common.util.unit.Unit
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

class PCoin private constructor(strs: Array<String>) : Data() {
    private val id: Int
    private val du: DataUnit
    val full: DataUnit
    val max: IntArray
    val info = Array(5) { IntArray(12) }
    fun improve(lvs: IntArray): DataUnit {
        val ans: DataUnit = du.clone()
        for (i in 0..4) {
            if (lvs[i + 1] == 0) continue
            if (info[i][0] >= Data.Companion.PC_CORRES.size) {
                CommonStatic.ctx.printErr(ErrType.NEW, "new PCoin ability not yet handled by BCU: " + info[i][0])
                continue
            }
            val type: IntArray = Data.Companion.PC_CORRES.get(info[i][0])
            if (type.size > 2 || type[0] == -1) {
                CommonStatic.ctx.printErr(ErrType.NEW, "new PCoin ability not yet handled by BCU: " + info[i][0])
                continue
            }
            val maxlv = info[i][1]
            val modifs = IntArray(4)
            if (maxlv > 1) {
                for (j in 0..3) {
                    val v0 = info[i][2 + j * 2]
                    val v1 = info[i][3 + j * 2]
                    modifs[j] = (v1 - v0) * (lvs[i + 1] - 1) / (maxlv - 1) + v0
                }
            }
            if (maxlv == 0) for (j in 0..3) modifs[j] = info[i][3 + j * 2]
            if (type[0] == Data.Companion.PC_P) {
                val tar: ProcItem = ans.proc!!.getArr(type[1])
                for (j in 0..3) if (modifs[j] > 0) tar.set(j, tar.get(j) + modifs[j])
                if (type[1] == Data.Companion.P_STRONG) tar.set(0, 100 - tar.get(0))
                if (type[1] == Data.Companion.P_WEAK) tar.set(2, 100 - tar.get(2))
            } else if (type[0] == Data.Companion.PC_AB) ans.abi = ans.abi or type[1] else if (type[0] == Data.Companion.PC_BASE) if (type[1] == Data.Companion.PC2_HP) ans.hp *= 1 + modifs[0] * 0.01.toInt() else if (type[1] == Data.Companion.PC2_ATK) {
                val atk = 1 + modifs[0] * 0.01
                ans.atk *= atk.toInt()
                ans.atk1 *= atk.toInt()
                ans.atk2 *= atk.toInt()
            } else if (type[1] == Data.Companion.PC2_SPEED) ans.speed += modifs[0] else if (type[1] == Data.Companion.PC2_CD) ans.respawn -= modifs[0] else if (type[1] == Data.Companion.PC2_COST) ans.price -= modifs[0] else if (type[0] == Data.Companion.PC_IMU) ans.proc!!.getArr(type[1]).set(0, 100) else if (type[0] == Data.Companion.PC_TRAIT) ans.type = ans.type or type[1]
        }
        return ans
    }

    companion object {
        fun read() {
            val qs: Queue<String> = VFile.Companion.readLine("./org/data/SkillAcquisition.csv")
            qs.poll()
            for (str in qs) {
                val strs = str.trim { it <= ' ' }.split(",").toTypedArray()
                if (strs.size == 62) PCoin(strs)
            }
        }
    }

    init {
        id = CommonStatic.parseIntN(strs[0])
        max = IntArray(6)
        for (i in 0..4) {
            for (j in 0..11) info[i][j] = CommonStatic.parseIntN(strs[2 + i * 12 + j])
            max[i + 1] = info[i][1]
            if (max[i + 1] == 0) max[i + 1] = 1
        }
        du = PackData.Identifier.Companion.parseInt<Unit>(id, Unit::class.java).get().forms.get(2).du
        du.pcoin = this
        full = improve(max)
    }
}
