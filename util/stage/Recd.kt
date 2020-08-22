package common.util.stage

import common.CommonStatic
import common.battle.BasisLU
import common.battle.data.DataEntity
import common.io.InStream
import common.io.OutStream
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonClass.JCConstructor
import common.io.json.JsonEncoder
import common.io.json.Test
import common.io.json.Test.JsonTest_0.JsonD
import common.io.json.Test.JsonTest_2
import common.pack.PackData
import common.pack.PackData.UserPack
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.pack.UserProfile
import common.util.Data
import common.util.stage.EStage
import common.util.stage.MapColc.DefMapColc
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
import java.io.File
import java.util.*

class Recd : Data {
    private class Wait(val st: Stage, input: InStream, name: String) {
        val `is`: InStream
        val str: String

        init {
            `is` = input
            str = name
        }
    }

    var name = "new record"
    var seed: Long = 0
    var conf: IntArray
    var star = 0
    var len = 0
    var lu: BasisLU? = null
    var avail = false
    var marked = false
    var action: OutStream? = null
    var st: Stage? = null

    @JCConstructor
    constructor() {
    }

    constructor(blu: BasisLU?, sta: Stage?, stars: Int, con: IntArray, se: Long) {
        lu = blu
        st = sta
        star = stars
        conf = con
        seed = se
        avail = st != null
    }

    override fun clone(): Recd {
        return Recd(lu.copy(), st, star, conf.clone(), seed)
    }

    fun getLen(): Int {
        if (len > 0) return len
        val `is`: InStream = action.translate()
        val n: Int = `is`.nextInt()
        for (i in 0 until n / 2) {
            `is`.nextInt()
            len += `is`.nextInt()
        }
        return len
    }

    override fun toString(): String {
        return name
    }

    fun write() {
        // TODO Auto-generated method stub
    }

    companion object {
        val map: MutableMap<String?, Recd> = TreeMap<String, Recd>()
        private val WAIT: MutableList<Wait> = ArrayList()
        fun getAvailable(str: String?): String? {
            var str = str
            while (map.containsKey(str)) str += "'"
            return str
        }

        fun getRecd(stage: Stage, `is`: InStream, str: String) {
            WAIT.add(Wait(stage, `is`, str))
        }

        fun read() {
            val f: File = CommonStatic.def.route("./replay/")
            if (!f.exists()) return
            val fs = f.listFiles()
            for (fi in fs) {
                val str = fi.name
                if (str.endsWith(".replay")) {
                    val name = str.substring(0, str.length - 7)
                    val `is`: InStream = CommonStatic.def.readBytes(fi)
                    val rec = getRecd(`is`, name)
                    if (rec != null) map[name] = rec
                }
            }
            for (w in WAIT) w.st.recd.add(getRecd(w.`is`, w.str))
            WAIT.clear()
        }

        private fun getRecd(`is`: InStream, name: String): Recd? {
            val `val`: Int = Data.Companion.getVer(`is`.nextString())
            return if (`val` >= 401) `zread$000401`(`is`, name) else null
        }

        private fun `zread$000401`(`is`: InStream, name: String): Recd? {
            val seed: Long = `is`.nextLong()
            val conf: IntArray = `is`.nextIntsB()
            val star: Int = `is`.nextInt()
            val lu: BasisLU = BasisLU.Companion.zread(`is`.subStream())
            val action: InStream = `is`.subStream()
            val pid: Int = `is`.nextInt()
            var st: Stage? = null
            if (pid == 0) {
                val id: Int = `is`.nextInt()
                val sm: StageMap = DefMapColc.Companion.getMap(id / 1000)
                st = sm.list.get(id % 1000)
                if (st == null) {
                    // TODO Opts.recdErr(name, "stage " + id);
                    return null
                }
            } else {
                st = `zreads$000401`(`is`, pid, name)
            }
            val ans = Recd(lu, st, star, conf, seed)
            ans.action = action.translate()
            ans.name = name
            return ans
        }

        private fun `zreads$000401`(`is`: InStream, pid: Int, name: String): Stage? {
            val mcn: String = `is`.nextString()
            val smid: String = `is`.nextString()
            val stid: String = `is`.nextString()
            val pack: PackData = UserProfile.Companion.getPack(Data.Companion.hex(pid))
            if (pid != 0 && pack == null) {
                // TODO Opts.recdErr(name, "pack " + pid);
                return null
            }
            var mc: MapColc? = null
            mc = if (pid == 0) DefMapColc.Companion.getMap(mcn) else (pack as UserPack).mc
            var sm: StageMap? = null
            for (map in mc.maps) if (map!!.name == smid) sm = map
            if (sm == null) {
                // TODO Opts.recdErr(name, "stage map " + smid);
                return null
            }
            var st: Stage? = null
            for (s in sm.list) if (s.name == stid) st = s
            return st
        }
    }
}
