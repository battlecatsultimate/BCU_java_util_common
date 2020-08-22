package common.util.stage

import common.io.InStream
import common.io.assets.Admin.StaticPermitted
import common.io.json.JsonClass
import common.io.json.JsonClass.RType
import common.io.json.JsonField
import common.pack.PackData.UserPack
import common.pack.UserProfile
import common.pack.VerFixer.VerFixerException
import common.system.files.FileData
import common.system.files.VFile
import common.util.Data
import common.util.lang.MultiLangCont
import common.util.stage.CastleList.DefCasList
import common.util.stage.Limit.PackLimit
import common.util.stage.MapColc
import java.util.*

@JsonClass(read = RType.FILL)
open class MapColc : Data() {
    class DefMapColc : MapColc {
        val id: Int
        val name: String

        private constructor() {
            id = 3
            UserProfile.Companion.getRegister<MapColc>(REG_MAPCOLC, MapColc::class.java).put(Data.Companion.trio(id), this)
            name = "CH"
            maps = arrayOfNulls<StageMap>(14)
            val abbr = "./org/stage/CH/stageNormal/stageNormal"
            for (i in 0..2) {
                var vf: FileData = VFile.Companion.get(abbr + "0_" + i + "_Z.csv").getData()
                maps[i] = StageMap(this, i, vf, 1)
                maps[i].name = "EoC " + (i + 1) + " Zombie"
                vf = VFile.Companion.get(abbr + "1_" + i + ".csv").getData()
                maps[3 + i] = StageMap(this, 3 + i, vf, 2)
                maps[i + 3].name = "ItF " + (i + 1)
                vf = VFile.Companion.get(abbr + "2_" + i + ".csv").getData()
                maps[6 + i] = StageMap(this, 6 + i, vf, 3)
                maps[i + 6].name = "CotC " + (i + 1)
            }
            var stn: FileData = VFile.Companion.get(abbr + "0.csv").getData()
            maps[9] = StageMap(this, 9, stn, 1)
            maps[9].name = "EoC 1-3"
            stn = VFile.Companion.get(abbr + "1_0_Z.csv").getData()
            maps[10] = StageMap(this, 10, stn, 2)
            maps[10].name = "ItF 1 Zombie"
            stn = VFile.Companion.get(abbr + "2_2_Invasion.csv").getData()
            maps[11] = StageMap(this, 11, stn, 2)
            maps[11].name = "CotC 3 Invasion"
            stn = VFile.Companion.get(abbr + "1_1_Z.csv").getData()
            maps[12] = StageMap(this, 12, stn, 2)
            maps[12].name = "ItF 2 Zombie"
            maps[13] = StageMap(this, 13, stn, 2)
            maps[13].name = "ItF 3 Zombie"
            val stz: VFile<*> = VFile.Companion.get("./org/stage/CH/stageZ/")
            for (vf in stz.list()) {
                val str: String = vf.getName()
                var id0 = -1
                var id1 = -1
                try {
                    id0 = str.substring(6, 8).toInt()
                    id1 = str.substring(9, 11).toInt()
                } catch (e: Exception) {
                    e.printStackTrace()
                    continue
                }
                if (id0 < 3) maps[id0].add(Stage(maps[id0], id1, vf, 0)) else if (id0 == 4) maps[10].add(Stage(maps[10], id1, vf, 0)) else if (id0 == 5) maps[12].add(Stage(maps[12], id1, vf, 0)) else if (id0 == 6) maps[13].add(Stage(maps[13], id1, vf, 0))
            }
            val stw: VFile<*> = VFile.Companion.get("./org/stage/CH/stageW/")
            for (vf in stw.list()) {
                val str: String = vf.getName()
                var id0 = -1
                var id1 = -1
                try {
                    id0 = str.substring(6, 8).toInt()
                    id1 = str.substring(9, 11).toInt()
                } catch (e: Exception) {
                    e.printStackTrace()
                    continue
                }
                maps[id0 - 1].add(Stage(maps[id0 - 1], id1, vf, 1))
            }
            val sts: VFile<*> = VFile.Companion.get("./org/stage/CH/stageSpace/")
            for (vf in sts.list()) {
                val str: String = vf.getName()
                if (str.length > 20) {
                    maps[11].add(Stage(maps[11], 0, vf, 0))
                    continue
                }
                var id0 = -1
                var id1 = -1
                try {
                    id0 = str.substring(10, 12).toInt()
                    id1 = str.substring(13, 15).toInt()
                } catch (e: Exception) {
                    e.printStackTrace()
                    continue
                }
                maps[id0 - 1].add(Stage(maps[id0 - 1], id1, vf, 1))
            }
            val st: VFile<*> = VFile.Companion.get("./org/stage/CH/stage/")
            for (vf in st.list()) {
                val str: String = vf.getName()
                var id0 = -1
                id0 = try {
                    str.substring(5, 7).toInt()
                } catch (e: Exception) {
                    e.printStackTrace()
                    continue
                }
                maps[9].add(Stage(maps[9], id0, vf, 2))
            }
            maps[9].stars = intArrayOf(100, 200, 400)
        }

        private constructor(st: String, ID: Int, stage: List<VFile<*>>, map: VFile<*>) {
            name = st
            id = ID
            UserProfile.Companion.getRegister<MapColc>(REG_MAPCOLC, MapColc::class.java).put(Data.Companion.trio(id), this)
            val sms: Array<StageMap?> = arrayOfNulls<StageMap>(map.list().size)
            for (m in map.list()) {
                val str: String = m.getName()
                val len = str.length
                var id = -1
                id = try {
                    str.substring(len - 7, len - 4).toInt()
                } catch (e: Exception) {
                    e.printStackTrace()
                    continue
                }
                sms[id] = StageMap(this, id, m.getData())
            }
            maps = sms
            for (s in stage) {
                val str: String = s.getName()
                val len = str.length
                var id0 = -1
                var id1 = -1
                try {
                    id0 = str.substring(len - 10, len - 7).toInt()
                    id1 = str.substring(len - 6, len - 4).toInt()
                } catch (e: Exception) {
                    e.printStackTrace()
                    continue
                }
                sms[id0]!!.add(Stage(sms[id0], id1, s, 0))
            }
        }

        override fun toString(): String {
            val desp: String = MultiLangCont.Companion.get(this)
            return if (desp != null && desp.length > 0) desp + " (" + maps.size + ")" else name + " (" + maps.size + ")"
        }

        companion object {
            private const val REG_IDMAP = "DefMapColc_idmap"

            /** get a BC stage  */
            fun getMap(mid: Int): StageMap? {
                val map: Map<String, MapColc> = UserProfile.Companion.getRegister<MapColc>(REG_MAPCOLC, MapColc::class.java)
                val mc = map[Data.Companion.trio(mid / 1000)] ?: return null
                return mc.maps[mid % 1000]
            }

            fun getMap(id: String?): DefMapColc {
                return UserProfile.Companion.getRegister<MapColc>(REG_MAPCOLC, MapColc::class.java)
                        .get(Data.Companion.trio(UserProfile.Companion.getRegister<Int>(REG_IDMAP, Int::class.java).get(id)))
            }

            fun read() {
                val idmap: MutableMap<String, Int> = UserProfile.Companion.getRegister<Int>(REG_IDMAP, Int::class.java)
                idmap["E"] = 4
                idmap["N"] = 0
                idmap["S"] = 1
                idmap["C"] = 2
                idmap["CH"] = 3
                idmap["T"] = 6
                idmap["V"] = 7
                idmap["R"] = 11
                idmap["M"] = 12
                idmap["A"] = 13
                idmap["B"] = 14
                idmap["RA"] = 24
                idmap["H"] = 25
                idmap["CA"] = 27
                for (i in strs.indices) DefCasList(Data.Companion.hex(i), strs[i])
                val f: VFile<*> = VFile.Companion.get("./org/stage/") ?: return
                for (fi in f.list()) {
                    if (fi.getName() == "CH") continue
                    if (fi.getName() == "D") continue
                    val list: List<VFile<*>> = ArrayList<VFile<*>>(fi.list())
                    val map: VFile<*> = list[0]
                    val stage: MutableList<VFile<*>> = ArrayList<VFile<*>>()
                    for (i in 1 until list.size) if (list[i].list() != null) stage.addAll(list[i].list())
                    DefMapColc(fi.getName(), idmap[fi.getName()]!!, stage, map)
                }
                DefMapColc()
                val qs: Queue<String> = VFile.Companion.readLine("./org/data/Map_option.csv")
                qs.poll()
                for (str in qs) {
                    val strs = str.trim { it <= ' ' }.split(",").toTypedArray()
                    val id = strs[0].toInt()
                    val sm: StageMap = getMap(id) ?: continue
                    val len = strs[1].toInt()
                    sm.stars = IntArray(len)
                    for (i in 0 until len) sm.stars.get(i) = strs[2 + i].toInt()
                    sm.set = strs[6].toInt()
                    sm.retyp = strs[7].toInt()
                    sm.pllim = strs[8].toInt()
                    sm.name += strs[10]
                }
            }
        }
    }

    @JsonClass
    class PackMapColc : MapColc {
        val pack: UserPack

        constructor(pack: UserPack) {
            this.pack = pack
        }

        @Deprecated("")
        constructor(pack: UserPack, `is`: InStream) {
            this.pack = pack
            val `val`: Int = Data.Companion.getVer(`is`.nextString())
            if (`val` != 308) throw VerFixerException("MapColc requires 308, got $`val`")
            `is`.nextString()
            var n: Int = `is`.nextInt()
            for (i in 0 until n) {
                val cg = CharaGroup(pack, `is`)
                pack.groups.set(cg.id!!.id, cg)
            }
            n = `is`.nextInt()
            for (i in 0 until n) {
                val lr = LvRestrict(pack, `is`)
                pack.lvrs.set(lr.id!!.id, lr)
            }
            n = `is`.nextInt()
            maps = arrayOfNulls<StageMap>(n)
            for (i in 0 until n) {
                val sm = StageMap(this)
                maps[i] = sm
                sm.name = `is`.nextString()
                sm.stars = `is`.nextIntsB()
                var m: Int = `is`.nextInt()
                for (j in 0 until m) {
                    val sub: InStream = `is`.subStream()
                    sm.add(Stage(pack, sm, sub))
                }
                m = `is`.nextInt()
                for (j in 0 until m) sm.lim.add(PackLimit(pack, `is`))
            }
        }

        override fun toString(): String {
            return pack.desc.name
        }
    }

    class StItr : MutableIterator<Stage?>, Iterable<Stage?> {
        private var imc: Iterator<MapColc>?
        private var mc: MapColc
        private var ism: Int
        private var `is`: Int
        override fun hasNext(): Boolean {
            return imc != null
        }

        override fun iterator(): MutableIterator<Stage> {
            return this
        }

        override fun next(): Stage {
            val ans: Stage = mc.maps[ism]!!.list.get(`is`)
            `is`++
            validate()
            return ans
        }

        private fun validate() {
            while (`is` >= mc.maps[ism]!!.list.size) {
                `is` = 0
                ism++
                while (ism >= mc.maps.size) {
                    ism = 0
                    if (!imc!!.hasNext()) {
                        imc = null
                        return
                    }
                    mc = imc!!.next()
                }
            }
        }

        init {
            imc = UserProfile.Companion.getRegister<MapColc>(REG_MAPCOLC, MapColc::class.java).values.iterator()
            mc = imc!!.next()
            `is` = 0
            ism = `is`
            validate()
        }
    }

    class ClipMapColc : MapColc() {
        override fun toString(): String {
            return "clipboard"
        }

        init {
            maps = arrayOfNulls<StageMap>(1)
            maps[0] = StageMap(this)
        }
    }

    @JsonField
    var maps: Array<StageMap?> = arrayOfNulls<StageMap>(0)

    companion object {
        private const val REG_MAPCOLC = "MapColc"

        @StaticPermitted
        private val strs = arrayOf("rc", "ec", "sc", "wc")
        fun getAllStage(): Iterable<Stage> {
            return StItr()
        }

        fun values(): Collection<MapColc> {
            return UserProfile.Companion.getRegister<MapColc>(REG_MAPCOLC, MapColc::class.java).values
        }
    }
}
