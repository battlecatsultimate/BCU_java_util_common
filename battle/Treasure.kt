package common.battle

import common.io.InStream
import common.io.OutStream
import common.io.json.JsonClass
import common.io.json.JsonClass.RType
import common.io.json.JsonField
import common.io.json.JsonField.GenType
import common.util.Data

@JsonClass(read = RType.FILL)
class Treasure : Data {
    val b: Basis

    @JsonField(gen = GenType.FILL)
    var tech = IntArray(Data.Companion.LV_TOT)

    @JsonField(gen = GenType.FILL)
    var trea = IntArray(Data.Companion.T_TOT)

    @JsonField(gen = GenType.FILL)
    var bslv = IntArray(Data.Companion.BASE_TOT)

    @JsonField(gen = GenType.FILL)
    var fruit = IntArray(7)

    @JsonField(gen = GenType.FILL)
    var gods = IntArray(3)

    @JsonField
    var alien = 0

    @JsonField
    var star = 0

    /** new Treasure object  */
    constructor(bas: Basis) {
        b = bas
        `zread$000000`()
    }

    /** read Treasure from data  */
    constructor(bas: Basis, ver: Int, `is`: InStream) {
        b = bas
        zread(ver, `is`)
    }

    /** copy Treasure object  */
    constructor(bas: Basis, t: Treasure) {
        b = bas
        tech = t.tech.clone()
        trea = t.trea.clone()
        fruit = t.fruit.clone()
        gods = t.gods.clone()
        alien = t.alien
        star = t.star
        bslv = t.bslv.clone()
    }

    /** get multiplication of non-starred alien  */
    fun getAlienMulti(): Double {
        return 7 - alien * 0.01
    }

    /** get cat attack multiplication  */
    fun getAtkMulti(): Double {
        val ini = 1 + trea[Data.Companion.T_ATK] * 0.005
        val com: Double = 1 + b.getInc(Data.Companion.C_ATK) * 0.01
        return ini * com
    }

    /** get base health  */
    fun getBaseHealth(): Int {
        val t = tech[Data.Companion.LV_BASE]
        var base = if (t < 6) t * 1000 else if (t < 8) 5000 + (t - 5) * 2000 else 9000 + (t - 7) * 3000
        base += trea[Data.Companion.T_BASE] * 70
        base += if (bslv[0] > 10) 36000 + 4000 * (bslv[0] - 10) else 3600 * bslv[0]
        return base * (100 + b.getInc(Data.Companion.C_BASE)) / 100
    }

    /** get normal canon attack  */
    fun getCanonAtk(): Int {
        val base = 50 + tech[Data.Companion.LV_CATK] * 50 + trea[Data.Companion.T_CATK] * 5
        return base * (100 + b.getInc(Data.Companion.C_C_ATK)) / 100
    }

    /** get special canon data 1  */
    fun getCanonMulti(type: Int): Double {
        if (type == 2) return if (bslv[2] > 10) 298 + 3.2 * (bslv[2] - 10) else 100 + 19.8 * bslv[2] else if (type == 3) return if (bslv[3] > 10) 39 + 0.9 * (bslv[3] - 10) else 30 + 0.9 * bslv[3] else if (type == 4) return if (bslv[4] > 10) (200 + 15 * (bslv[4] - 10)).toDouble() else (110 + 9 * bslv[4]).toDouble() else if (type == 5) return if (bslv[5] > 10) 25 + 7.5 * (bslv[5] - 10) else (5 + 2 * bslv[5]).toDouble() else if (type == -5) return if (bslv[5] > 10) (150 + 15 * (bslv[5] - 10)).toDouble() else (100 + 5 * bslv[5]).toDouble() else if (type == 6) return if (bslv[6] > 10) (50 + 5 * (bslv[6] - 10)).toDouble() else (30 + 2 * bslv[6]).toDouble()
        return 0
    }

    /** get special canon data 2, usually proc time  */
    fun getCanonProcTime(type: Int): Int {
        if (type == 1) return if (bslv[1] > 10) 50 + 5 * (bslv[1] - 10) else 30 + 2 * bslv[1] else if (type == 2) return if (bslv[2] > 10) 90 + 9 * (bslv[2] - 10) / 2 else 60 + 3 * bslv[2] else if (type == 3) return if (bslv[3] > 10) 30 + 3 * (bslv[3] - 10) else 15 + 3 * bslv[3] / 2 else if (type == 5) return if (bslv[5] > 10) 45 + 3 * (bslv[3] - 10) / 2 else 30 + 3 * bslv[3] / 2 else if (type == 6) return if (bslv[6] > 10) 250 + 15 * (bslv[6] - 10) else 200 + 5 * bslv[6] else if (type == 7) return if (bslv[7] > 10) 60 + 6 * (bslv[7] - 10) else 33 + 3 * bslv[7]
        return 0
    }

    /** get cat health multiplication  */
    fun getDefMulti(): Double {
        val ini = 1 + trea[Data.Companion.T_DEF] * 0.005
        val com: Double = 1 + b.getInc(Data.Companion.C_DEF) * 0.01
        return ini * com
    }

    /** get accounting multiplication  */
    fun getDropMulti(): Double {
        return (0.95 + 0.05 * tech[Data.Companion.LV_ACC] + 0.005 * trea[Data.Companion.T_ACC]) * (1 + b.getInc(Data.Companion.C_MEAR) * 0.01)
    }

    /** get EVA kill ability attack multiplication  */
    fun getEKAtk(): Double {
        return 0.05 * (100 + b.getInc(Data.Companion.C_EKILL))
    }

    /** get EVA kill ability reduce damage multiplication  */
    fun getEKDef(): Double {
        return 20.0 / (100 + b.getInc(Data.Companion.C_EKILL))
    }

    /** get processed cat cool down time  */
    fun getFinRes(ori: Int): Int {
        val dec: Double = 6 - tech[Data.Companion.LV_RES] * 6 - trea[Data.Companion.T_RES] * 0.3 - b.getInc(Data.Companion.C_RESP)
        return Math.max(60.0, ori + 10 + dec).toInt()
    }

    /** get maximum fruit of certain trait bitmask  */
    fun getFruit(type: Int): Double {
        var ans = 0.0
        if (type and Data.Companion.TB_RED != 0) ans = Math.max(ans, fruit[Data.Companion.T_RED])
        if (type and Data.Companion.TB_BLACK != 0) ans = Math.max(ans, fruit[Data.Companion.T_BLACK])
        if (type and Data.Companion.TB_ANGEL != 0) ans = Math.max(ans, fruit[Data.Companion.T_ANGEL])
        if (type and Data.Companion.TB_FLOAT != 0) ans = Math.max(ans, fruit[Data.Companion.T_FLOAT])
        if (type and Data.Companion.TB_ALIEN != 0) ans = Math.max(ans, fruit[Data.Companion.T_ALIEN])
        if (type and Data.Companion.TB_METAL != 0) ans = Math.max(ans, fruit[Data.Companion.T_METAL])
        if (type and Data.Companion.TB_ZOMBIE != 0) ans = Math.max(ans, fruit[Data.Companion.T_ZOMBIE])
        return ans * 0.01
    }

    /** get attack multiplication from strong against ability  */
    fun getGOODATK(type: Int): Double {
        val ini = 1.5 * (1 + 0.2 / 3 * getFruit(type))
        val com: Double = 1 + b.getInc(Data.Companion.C_GOOD) * 0.01
        return ini * com
    }

    /** get damage reduce multiplication from strong against ability  */
    fun getGOODDEF(type: Int): Double {
        val ini = 0.5 - 0.1 / 3 * getFruit(type)
        val com: Double = 1 - b.getInc(Data.Companion.C_GOOD) * 0.01
        return ini * com
    }

    /** get attack multiplication from massive damage ability  */
    fun getMASSIVEATK(type: Int): Double {
        val ini = 3 + 1.0 / 3 * getFruit(type)
        val com: Double = 1 + b.getInc(Data.Companion.C_MASSIVE) * 0.01
        return ini * com
    }

    /** get attack multiplication from super massive damage ability  */
    fun getMASSIVESATK(type: Int): Double {
        return 5 + 1.0 / 3 * getFruit(type)
    }

    /** get damage reduce multiplication from resistant ability  */
    fun getRESISTDEF(type: Int): Double {
        val ini = 0.25 - 0.05 / 3 * getFruit(type)
        val com: Double = 1 - b.getInc(Data.Companion.C_RESIST) * 0.01
        return ini * com
    }

    /** get damage reduce multiplication from super resistant ability  */
    fun getRESISTSDEF(type: Int): Double {
        return 1.0 / 6 - 1.0 / 126 * getFruit(type)
    }

    /** get reverse cat cool down time  */
    fun getRevRes(res: Int): Int {
        var res = res
        if (res < 60) res = 60
        val dec: Double = 6 - tech[Data.Companion.LV_RES] * 6 - trea[Data.Companion.T_RES] * 0.3 - b.getInc(Data.Companion.C_RESP)
        return (res - 10 - dec).toInt()
    }

    /** get multiplication of starred enemy  */
    fun getStarMulti(st: Int): Double {
        return if (st == 1) 16 - star * 0.01 else 11 - 0.1 * gods[st - 2]
    }

    /** get witch kill ability attack multiplication  */
    fun getWKAtk(): Double {
        return 0.05 * (100 + b.getInc(Data.Companion.C_WKILL))
    }

    /** get witch kill ability reduce damage multiplication  */
    fun getWKDef(): Double {
        return 10.0 / (100 + b.getInc(Data.Companion.C_WKILL))
    }

    fun getXPMult(): Double {
        val txp1 = trea[Data.Companion.T_XP1]
        val txp2 = trea[Data.Companion.T_XP2]
        val tm = txp1 * 0.005 + txp2 * 0.0025
        return 0.95 + tech[Data.Companion.LV_XP] * 0.05 + tm
    }

    /** get canon recharge time  */
    fun CanonTime(map: Int): Int {
        var base = 1503 + 50 * (tech[Data.Companion.LV_CATK] - tech[Data.Companion.LV_RECH])
        base -= if (trea[Data.Companion.T_RECH] <= 300) (1.5 * trea[Data.Companion.T_RECH]).toInt() else 3 * trea[Data.Companion.T_RECH] - 450
        base -= b.getInc(Data.Companion.C_C_SPE)
        base = Math.max(950, base + map * 450)
        return base
    }

    /** get the cost to upgrade worker cat  */
    fun getLvCost(lv: Int): Int {
        val t = tech[Data.Companion.LV_WORK]
        val base = if (t < 8) 30 + 10 * t else 20 * t - 40
        return base * lv
    }

    /** get wallet capacity  */
    fun getMaxMon(lv: Int): Int {
        var base = Math.max(25, 50 * tech[Data.Companion.LV_WALT])
        base = base * (1 + lv)
        base += trea[Data.Companion.T_WALT] * 10
        return base * (100 + b.getInc(Data.Companion.C_M_MAX)) / 100
    }

    /** get money increase rate  */
    fun getMonInc(lv: Int): Double {
        return (0.15 + 0.1 * tech[Data.Companion.LV_WORK]) * (1 + (lv - 1) * 0.1) + trea[Data.Companion.T_WORK] * 0.01
    }

    /** save data to file  */
    protected fun write(os: OutStream) {
        os.writeString("0.4.0")
        os.writeIntB(tech)
        os.writeIntB(trea)
        os.writeInt(alien)
        os.writeInt(star)
        os.writeIntB(fruit)
        os.writeIntB(gods)
        os.writeIntB(bslv)
    }

    /** read date from file, support multiple versions  */
    private fun zread(`val`: Int, `is`: InStream) {
        var `val` = `val`
        `zread$000000`()
        if (`val` >= 305) `val` = Data.Companion.getVer(`is`.nextString())
        if (`val` >= 400) `zread$000400`(`is`) else if (`val` >= 305) `zread$000305`(`is`) else if (`val` >= 304) `zread$000304`(`is`) else if (`val` >= 301) `zread$000301`(`is`) else if (`val` >= 203) `zread$000203`(`is`)
    }

    private fun `zread$000000`() {
        for (i in 0 until Data.Companion.LV_TOT) tech[i] = Data.Companion.MLV.get(i)
        for (i in 0 until Data.Companion.T_TOT) trea[i] = Data.Companion.MT.get(i)
        fruit[Data.Companion.T_ANGEL] = 300
        fruit[Data.Companion.T_FLOAT] = fruit[Data.Companion.T_ANGEL]
        fruit[Data.Companion.T_BLACK] = fruit[Data.Companion.T_FLOAT]
        fruit[Data.Companion.T_RED] = fruit[Data.Companion.T_BLACK]
        fruit[Data.Companion.T_ALIEN] = 300
        fruit[Data.Companion.T_ZOMBIE] = fruit[Data.Companion.T_ALIEN]
        fruit[Data.Companion.T_METAL] = fruit[Data.Companion.T_ZOMBIE]
        for (i in 0 until Data.Companion.BASE_TOT) bslv[i] = 20
        gods[2] = 100
        gods[1] = gods[2]
        gods[0] = gods[1]
        alien = 600
        star = 1500
    }

    private fun `zread$000203`(`is`: InStream) {
        for (i in 0..7) tech[i] = `is`.nextByte()
        for (i in 0..8) trea[i] = `is`.nextShort()
        alien = `is`.nextInt()
        star = `is`.nextInt()
        fruit = `is`.nextIntsB()
        gods = `is`.nextIntsB()
    }

    private fun `zread$000301`(`is`: InStream) {
        `zread$000203`(`is`)
        for (i in 0..4) bslv[i] = `is`.nextByte()
    }

    private fun `zread$000304`(`is`: InStream) {
        `zread$000203`(`is`)
        for (i in 0..5) bslv[i] = `is`.nextByte()
    }

    private fun `zread$000305`(`is`: InStream) {
        `zread$000203`(`is`)
        val temp: IntArray = `is`.nextIntsB()
        for (i in temp.indices) bslv[i] = temp[i]
    }

    private fun `zread$000400`(`is`: InStream) {
        val lv: IntArray = `is`.nextIntsB()
        val tr: IntArray = `is`.nextIntsB()
        for (i in 0 until Math.min(Data.Companion.LV_TOT, lv.size)) tech[i] = lv[i]
        for (i in 0 until Math.min(Data.Companion.T_TOT, tr.size)) trea[i] = tr[i]
        alien = `is`.nextInt()
        star = `is`.nextInt()
        fruit = `is`.nextIntsB()
        gods = `is`.nextIntsB()
        val bs: IntArray = `is`.nextIntsB()
        for (i in bs.indices) bslv[i] = bs[i]
    }
}
