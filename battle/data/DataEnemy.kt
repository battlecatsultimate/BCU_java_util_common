package common.battle.data

import common.battle.Basis
import common.pack.PackData
import common.util.Data
import common.util.Data.Proc
import common.util.pack.Soul
import common.util.unit.Enemy

class DataEnemy(e: Enemy) : DefaultData(), MaskEnemy {
    private val enemy: Enemy
    private var earn = 0
    private var star = 0
    fun fillData(strs: Array<String>) {
        val ints = IntArray(strs.size)
        for (i in strs.indices) ints[i] = strs[i].toInt()
        hp = ints[0]
        hb = ints[1]
        speed = ints[2]
        atk = ints[3]
        tba = ints[4]
        range = ints[5]
        earn = ints[6]
        width = ints[8]
        var t = 0
        if (ints[10] == 1) t = t or Data.Companion.TB_RED
        isrange = ints[11] == 1
        pre = ints[12]
        if (ints[13] == 1) t = t or Data.Companion.TB_FLOAT
        if (ints[14] == 1) t = t or Data.Companion.TB_BLACK
        if (ints[15] == 1) t = t or Data.Companion.TB_METAL
        if (ints[16] == 1) t = t or Data.Companion.TB_WHITE
        if (ints[17] == 1) t = t or Data.Companion.TB_ANGEL
        if (ints[18] == 1) t = t or Data.Companion.TB_ALIEN
        if (ints[19] == 1) t = t or Data.Companion.TB_ZOMBIE
        proc!!.KB.prob = ints[20]
        proc!!.STOP.prob = ints[21]
        proc!!.STOP.time = ints[22]
        proc!!.SLOW.prob = ints[23]
        proc!!.SLOW.time = ints[24]
        proc!!.CRIT.prob = ints[25]
        var a = 0
        if (ints[26] == 1) a = a or Data.Companion.AB_BASE
        proc!!.WAVE.prob = ints[27]
        proc!!.WAVE.lv = ints[28]
        proc!!.WEAK.prob = ints[29]
        proc!!.WEAK.time = ints[30]
        proc!!.WEAK.mult = ints[31]
        proc!!.STRONG.health = ints[32]
        proc!!.STRONG.mult = ints[33]
        proc!!.LETHAL.prob = ints[34]
        lds = ints[35]
        ldr = ints[36]
        if (ints[37] == 1) proc!!.IMUWAVE.mult = 100
        if (ints[38] == 1) a = a or Data.Companion.AB_WAVES
        if (ints[39] == 1) proc!!.IMUKB.mult = 100
        if (ints[40] == 1) proc!!.IMUSTOP.mult = 100
        if (ints[41] == 1) proc!!.IMUSLOW.mult = 100
        if (ints[42] == 1) proc!!.IMUWEAK.mult = 100
        proc!!.BURROW.count = ints[43]
        proc!!.BURROW.dis = ints[44] / 4
        proc!!.REVIVE.count = ints[45]
        proc!!.REVIVE.time = ints[46]
        proc!!.REVIVE.health = ints[47]
        if (ints[48] == 1) t = t or Data.Companion.TB_WITCH
        if (ints[49] == 1) t = t or Data.Companion.TB_INFH
        death = PackData.Identifier.Companion.parseInt<Soul>(ints[54], Soul::class.java)
        atk1 = ints[55]
        atk2 = ints[56]
        pre1 = ints[57]
        pre2 = ints[58]
        abi0 = ints[59]
        abi1 = ints[60]
        abi2 = ints[61]
        shield = ints[64]
        proc!!.WARP.prob = ints[65]
        proc!!.WARP.time = ints[66]
        proc!!.WARP.dis = ints[67] / 4
        star = ints[69]
        if (ints[71] == 1) t = t or Data.Companion.TB_EVA
        if (ints[72] == 1) t = t or Data.Companion.TB_RELIC
        proc!!.CURSE.prob = ints[73]
        proc!!.CURSE.time = ints[74]
        proc!!.SATK.prob = ints[75]
        proc!!.SATK.mult = ints[76]
        proc!!.IMUATK.prob = ints[77]
        proc!!.IMUATK.time = ints[78]
        proc!!.POIATK.prob = ints[79]
        proc!!.POIATK.mult = ints[80]
        proc!!.VOLC.prob = ints[81]
        proc!!.VOLC.dis_0 = ints[82] / 4
        proc!!.VOLC.dis_1 = ints[83] / 4 + proc!!.VOLC.dis_0
        proc!!.VOLC.time = ints[84] * Data.Companion.VOLC_ITV
        abi = a
        type = t
        datks = arrayOfNulls<DataAtk>(getAtkCount())
        for (i in datks.indices) {
            datks.get(i) = DataAtk(this, i)
        }
    }

    override fun getDrop(): Double {
        return earn.toDouble()
    }

    override fun getPack(): Enemy? {
        return enemy
    }

    override fun getStar(): Int {
        return star
    }

    override fun multi(b: Basis): Double {
        if (star > 0) return b.t().getStarMulti(star)
        return if (type and Data.Companion.TB_ALIEN > 0) b.t().getAlienMulti() else 1
    }

    init {
        enemy = e
        proc = Proc.Companion.blank()
    }
}
