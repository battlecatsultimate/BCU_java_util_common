package common.util.stage

import common.CommonStatic
import common.io.InStream
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonClass
import common.io.json.JsonEncoder
import common.io.json.JsonField
import common.io.json.JsonField.GenType
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
import common.pack.VerFixer.VerFixerException
import common.system.BasedCopable
import common.system.files.VFile
import common.util.BattleStatic
import common.util.Data
import common.util.lang.MultiLangCont
import common.util.pack.Background
import common.util.stage.EStage
import common.util.stage.Limit.PackLimit
import common.util.stage.MapColc.ClipMapColc
import common.util.stage.Recd
import common.util.stage.SCDef
import common.util.stage.Stage
import common.util.stage.StageMap
import common.util.stage.StageMap.StageMapInfo
import common.util.unit.Enemy
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

@JsonClass
class Stage : Data, BasedCopable<Stage?, StageMap?>, BattleStatic {
    class StageInfo(info: StageMapInfo, s: Stage, data: IntArray) {
        val st: Stage
        val map: StageMapInfo
        val energy: Int
        val xp: Int
        val once: Int
        var rand = 0
        val drop: Array<IntArray?>
        val time: Array<IntArray>
        var diff = -1
        fun getHTML(): String {
            var ans = "<html>energy cost: $energy<br> xp: $xp<br> drop rewards: "
            if (drop.size == 0) ans += "none" else if (drop.size == 1) ans += ("{chance: " + drop[0]!![0] + "%, item ID: " + drop[0]!![1] + ", number: " + drop[0]!![2] + "}, once: "
                    + once) else {
                ans += "count: " + drop.size + ", rand mode: " + rand + ", once: " + once + "<br>"
                ans += "<table><tr><th>chance</th><th>item ID</th><th>number</th></tr>"
                for (dp in drop) ans += "<tr><td>" + dp!![0] + "%</td><td>" + dp[1] + "</td><td>" + dp[2] + "</td><tr>"
                ans += "</table>"
            }
            if (time.size > 0) {
                ans += "<br> time scores: count: " + time.size + "<br>"
                ans += "<table><tr><th>score</th><th>item ID</th><th>number</th></tr>"
                for (tm in time) ans += "<tr><td>" + tm[0] + "</td><td>" + tm[1] + "</td><td>" + tm[2] + "</td><tr>"
                ans += "</table>"
            }
            return ans
        }

        init {
            map = info
            st = s
            energy = data[0]
            xp = data[1]
            s.mus0 = PackData.Identifier.Companion.parseInt<Music>(data[2], Music::class.java)
            s.mush = data[3]
            s.mus1 = PackData.Identifier.Companion.parseInt<Music>(data[4], Music::class.java)
            once = data[data.size - 1]
            var isTime = data.size > 15
            if (isTime) for (i in 8..14) if (data[i] != -2) isTime = false
            if (isTime) {
                time = Array((data.size - 17) / 3) { IntArray(3) }
                for (i in time.indices) for (j in 0..2) time[i][j] = data[16 + i * 3 + j]
            } else time = Array(0) { IntArray(3) }
            val isMulti = !isTime && data.size > 9
            if (data.size == 6) {
                drop = arrayOfNulls(0)
                rand = 0
            } else if (!isMulti) {
                drop = arrayOfNulls(1)
                rand = 0
            } else {
                drop = Array((data.size - 7) / 3) { IntArray(3) }
                rand = data[8]
                for (i in 1 until drop.size) for (j in 0..2) drop[i]!![j] = data[6 + i * 3 + j]
            }
            if (drop.size > 0) drop[0] = intArrayOf(data[5], data[6], data[7])
        }
    }

    var info: StageInfo? = null
    var recd: List<Recd> = ArrayList<Recd>()
    val map: StageMap

    @JsonField
    var name = ""

    @JsonField
    var non_con = false

    @JsonField
    var trail = false

    @JsonField
    var len: Int

    @JsonField
    var health: Int

    @JsonField
    var max: Int

    @JsonField
    var mush = 0
    var castle: PackData.Identifier<CastleImg>? = null
    var bg: PackData.Identifier<Background>? = null
    var mus0: PackData.Identifier<Music>? = null
    var mus1: PackData.Identifier<Music>? = null

    @JsonField
    var loop0: Long = 0

    @JsonField
    var loop1: Long = 0

    @JsonField
    var data: SCDef?

    @JsonField(gen = GenType.GEN)
    var lim: Limit? = null

    constructor(sm: StageMap) {
        map = sm
        len = 3000
        health = 60000
        max = 8
        name = "stage " + sm.list.size
        lim = Limit()
        data = SCDef(0)
    }

    @Deprecated("")
    constructor(pack: UserPack?, sm: StageMap, `is`: InStream) : this(sm) {
        val `val`: Int = Data.Companion.getVer(`is`.nextString())
        if (`val` != 409) throw VerFixerException("stage version has to be 409, got$`val`")
        name = `is`.nextString()
        bg = PackData.Identifier.Companion.parseInt<Background>(`is`.nextInt(), Background::class.java)
        castle = PackData.Identifier.Companion.parseInt<CastleImg>(`is`.nextInt(), CastleImg::class.java)
        health = `is`.nextInt()
        len = `is`.nextInt()
        mus0 = PackData.Identifier.Companion.parseInt<Music>(`is`.nextInt(), Music::class.java)
        mush = `is`.nextInt()
        mus1 = PackData.Identifier.Companion.parseInt<Music>(`is`.nextInt(), Music::class.java)
        loop0 = `is`.nextLong()
        loop1 = `is`.nextLong()
        max = `is`.nextByte()
        non_con = `is`.nextByte() == 1
        data = SCDef.Companion.zread(`is`.subStream())
        lim = PackLimit(pack, `is`)
        val t: Int = `is`.nextInt()
        for (i in 0 until t) {
            val name: String = `is`.nextString()
            Recd.Companion.getRecd(this, `is`.subStream(), name)
        }
        validate()
    }

    constructor(sm: StageMap, id: Int, f: VFile<*>, type: Int) {
        map = sm
        if (sm.info != null) sm.info.getData(this)
        val qs: Queue<String> = f.getData().readLine()
        name = "" + id
        var temp: String
        if (type == 0) {
            temp = qs.poll()
            val strs = temp.split(",").toTypedArray()
            var cas: Int = CommonStatic.parseIntN(strs[0])
            if (map.cast != -1) cas += map.cast * 1000
            castle = PackData.Identifier.Companion.parseInt<CastleImg>(cas, CastleImg::class.java)
            non_con = strs[1] == "1"
        } else {
            castle = PackData.Identifier.Companion.parseInt<CastleImg>(0, CastleImg::class.java)
            non_con = false
        }
        val intl = if (type == 2) 9 else 10
        val strs = qs.poll().split(",").toTypedArray()
        len = strs[0].toInt()
        health = strs[1].toInt()
        bg = PackData.Identifier.Companion.parseInt<Background>(strs[4].toInt(), Background::class.java)
        max = strs[5].toInt()
        val isBase = strs[6].toInt() - 2
        val ll: MutableList<IntArray> = ArrayList()
        while (qs.size > 0) if (qs.poll().also { temp = it }.length > 0) {
            if (!Character.isDigit(temp[0])) break
            if (temp.startsWith("0,")) break
            val ss = temp.split(",").toTypedArray()
            val data = IntArray(SCDef.Companion.SIZE)
            for (i in 0 until intl) data[i] = ss[i].toInt()
            data[0] -= 2
            data[2] *= 2
            data[3] *= 2
            data[4] *= 2
            if (intl > 9 && data[5] > 100 && data[9] == 100) {
                data[9] = data[5]
                data[5] = 100
            }
            if (ss.size > 11 && CommonStatic.isInteger(ss[11])) data[SCDef.Companion.M1] = ss[11].toInt() else data[SCDef.Companion.M1] = data[SCDef.Companion.M]
            if (data[0] == isBase) data[SCDef.Companion.C0] = 0
            ll.add(data)
        }
        val scd = SCDef(ll.size)
        for (i in ll.indices) scd.datas.get(i) = SCDef.Line(ll[scd.datas.size - i - 1])
        if (strs.size > 6) {
            val ano: Int = CommonStatic.parseIntN(strs[6])
            if (ano == 317) scd.datas.get(ll.size - 1)!!.castle_0 = 0
        }
        data = scd
        validate()
    }

    operator fun contains(e: Enemy): Boolean {
        return data!!.contains(e)
    }

    override fun copy(sm: StageMap): Stage {
        val ans = Stage(sm)
        ans.len = len
        ans.health = health
        ans.max = max
        ans.bg = bg!!.clone()
        ans.castle = castle!!.clone()
        ans.name = name
        ans.data = data!!.copy()
        if (lim != null) ans.lim = lim!!.clone()
        ans.mus0 = mus0!!.clone()
        ans.mus1 = mus1!!.clone()
        ans.mush = mush
        return ans
    }

    fun getLim(star: Int): Limit {
        val tl = Limit()
        if (lim != null && (lim!!.star == -1 || lim!!.star == star)) tl.combine(lim)
        for (l in map.lim) if (l.star == -1 || l.star == star) if (l.sid == -1 || l.sid == id()) tl.combine(l)
        return tl
    }

    fun id(): Int {
        return map.list.indexOf(this)
    }

    fun isSuitable(pack: String?): Boolean {
        // FIXME suitable
        return false
    }

    fun setName(str: String) {
        // TODO validate str = MainBCU.validate(str);
        var str = str
        while (!checkName(str)) str += "'"
        name = str
    }

    override fun toString(): String {
        val desp: String = MultiLangCont.Companion.get(this)
        if (desp != null && desp.length > 0) return desp
        return if (name.length > 0) name else map.toString() + " - " + id()
    }

    fun validate() {
        trail = data!!.isTrail()
    }

    private fun checkName(str: String): Boolean {
        for (st in map.list) if (st !== this && st.name == str) return false
        return true
    }

    companion object {
        val CLIPMC: MapColc = ClipMapColc()
        val CLIPSM: StageMap = Stage.Companion.CLIPMC.maps.get(0)
    }
}
