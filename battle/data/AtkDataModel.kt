package common.battle.data

import common.io.InStream
import common.io.json.JsonClass
import common.io.json.JsonClass.NoTag
import common.io.json.JsonClass.RType
import common.io.json.JsonField
import common.system.BasedCopable
import common.util.Data
import common.util.Data.Proc

@JsonClass(read = RType.FILL, noTag = NoTag.LOAD)
class AtkDataModel : Data, MaskAtk, BasedCopable<AtkDataModel?, CustomEntity?> {
    @JsonField(block = true)
    val ce: CustomEntity
    var str = ""
    var atk = 0
    var pre = 1
    var ld0 = 0
    var ld1 = 0
    var targ: Int = Data.Companion.TCH_N
    var count = -1
    var dire = 1
    var alt = 0
    var move = 0
    var range = true

    @JsonField
    var proc: Proc? = null

    constructor(ent: CustomEntity) {
        ce = ent
        str = ce.getAvailable(str)
        proc = Proc.Companion.blank()
    }

    constructor(ene: CustomEntity, adm: AtkDataModel) {
        ce = ene
        str = ce.getAvailable(adm.str)
        atk = adm.atk
        pre = adm.pre
        ld0 = adm.ld0
        ld1 = adm.ld1
        range = adm.range
        dire = adm.dire
        count = adm.count
        targ = adm.targ
        alt = adm.alt
        move = adm.move
        proc = proc!!.clone()
    }

    constructor(ent: CustomEntity, `is`: InStream) {
        ce = ent
        proc = Proc.Companion.blank()
        zread("0.3.7", `is`)
    }

    constructor(ene: CustomEntity, me: MaskEntity, i: Int) {
        ce = ene
        str = ce.getAvailable("copied")
        val dat: Array<IntArray?> = me.rawAtkData()
        val am: MaskAtk = me.getAtkModel(i)
        if (dat[i]!![2] == 1) proc = am!!.getProc().clone()
        ld0 = am!!.getShortPoint()
        ld1 = am!!.getLongPoint()
        pre = dat[i]!![1]
        atk = dat[i]!![0]
        range = am!!.isRange()
        dire = am!!.getDire()
        count = am!!.loopCount()
        targ = am!!.getTarget()
        alt = am!!.getAltAbi()
        move = am!!.getMove()
    }

    override fun clone(): AtkDataModel {
        return AtkDataModel(ce, this)
    }

    override fun copy(nce: CustomEntity): AtkDataModel {
        return AtkDataModel(nce, this)
    }

    override fun getAltAbi(): Int {
        return alt
    }

    override fun getAtk(): Int {
        return atk
    }

    override fun getDire(): Int {
        return dire
    }

    override fun getLongPoint(): Int {
        return if (isLD()) ld1 else ce.range
    }

    override fun getMove(): Int {
        return move
    }

    override fun getProc(): Proc? {
        return if (ce.rep !== this && ce.common) ce.rep!!.getProc() else proc
    }

    override fun getShortPoint(): Int {
        return if (isLD()) ld0 else -ce.width
    }

    override fun getTarget(): Int {
        return targ
    }

    override fun isRange(): Boolean {
        return range
    }

    override fun loopCount(): Int {
        return count
    }

    override fun toString(): String {
        return str
    }

    fun getAtkData(): IntArray {
        return intArrayOf(atk, pre, 1)
    }

    fun isLD(): Boolean {
        return ld0 != 0 || ld1 != 0
    }

    fun isOmni(): Boolean {
        return ld0 * ld1 < 0
    }

    private fun zread(ver: String, `is`: InStream) {
        val `val`: Int = Data.Companion.getVer(`is`.nextString())
        if (`val` >= 404) `zread$000404`(`is`)
    }

    private fun `zread$000404`(`is`: InStream) {
        str = `is`.nextString()
        atk = `is`.nextInt()
        pre = `is`.nextInt()
        ld0 = `is`.nextInt()
        ld1 = `is`.nextInt()
        targ = `is`.nextInt()
        count = `is`.nextInt()
        dire = `is`.nextInt()
        alt = `is`.nextInt()
        move = `is`.nextInt()
        val bm: Int = `is`.nextInt()
        range = bm and 1 > 0
        proc = Proc.Companion.load(`is`.nextIntsBB())
    }
}
