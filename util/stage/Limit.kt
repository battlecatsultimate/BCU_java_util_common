package common.util.stage

import common.io.InStream
import common.io.json.JsonClass
import common.io.json.JsonField
import common.pack.PackData
import common.pack.PackData.UserPack
import common.pack.UserProfile
import common.pack.VerFixer.VerFixerException
import common.util.BattleStatic
import common.util.Data
import common.util.stage.MapColc.DefMapColc

@JsonClass
open class Limit
/** for copy or combine only  */
    : Data(), BattleStatic {
    class DefLimit(strs: Array<String>) : Limit() {
        init {
            val mid = strs[0].toInt()
            DefMapColc.Companion.getMap(mid).lim.add(this)
            star = strs[1].toInt()
            sid = strs[2].toInt()
            rare = strs[3].toInt()
            num = strs[4].toInt()
            line = strs[5].toInt()
            min = strs[6].toInt()
            max = strs[7].toInt()
            group = UserProfile.Companion.getBCData().groups.get(strs[8].toInt())
        }
    }

    class PackLimit : Limit {
        @JsonField
        var name = ""

        constructor()

        @Deprecated("")
        constructor(mc: UserPack, `is`: InStream) {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 308) throw VerFixerException("Limit requires ver 308, got $ver")
            val g: Int = `is`.nextInt()
            name = `is`.nextString()
            sid = `is`.nextInt()
            star = `is`.nextInt()
            if (g >= 0) group = mc.groups.get(g)
            val l: Int = `is`.nextInt()
            if (l >= 0) lvr = mc.lvrs.get(l)
            rare = `is`.nextInt()
            num = `is`.nextByte()
            line = `is`.nextByte()
            min = `is`.nextInt()
            max = `is`.nextInt()
        }
    }

    @JsonField
    var star = -1

    @JsonField
    var sid = -1

    @JsonField
    var rare = 0

    @JsonField
    var num = 0

    @JsonField
    var line = 0

    @JsonField
    var min = 0

    @JsonField
    var max = 0

    @JsonField(alias = [PackData.Identifier::class])
    var group: CharaGroup? = null

    @JsonField(alias = [PackData.Identifier::class])
    var lvr: LvRestrict? = null
    override fun clone(): Limit {
        val l = Limit()
        l.star = star
        l.sid = sid
        l.rare = rare
        l.num = num
        l.line = line
        l.min = min
        l.max = max
        l.group = group
        l.lvr = lvr
        return l
    }

    fun combine(l: Limit) {
        if (rare == 0) rare = l.rare else if (l.rare != 0) rare = rare and l.rare
        num = if (num * l.num > 0) Math.min(num, l.num) else Math.max(num, l.num)
        line = line or l.line
        min = Math.max(min, l.min)
        max = if (max > 0 && l.max > 0) Math.min(max, l.max) else max + l.max
        if (l.group != null) group = if (group != null) group.combine(l.group) else l.group
        if (l.lvr != null) if (lvr != null) lvr.combine(l.lvr) else lvr = l.lvr
    }

    override fun toString(): String {
        return (if (sid == -1) "all stages" else "" + sid) + " - " + if (star == -1) "all stars" else (star + 1).toString() + " star"
    }
}
