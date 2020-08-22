package common.util.unit

import common.CommonStatic
import common.battle.data.*
import common.io.json.JsonClass
import common.io.json.JsonClass.*
import common.io.json.JsonField
import common.pack.PackData
import common.system.BasedCopable
import common.util.Animable
import common.util.Data
import common.util.anim.AnimU
import common.util.anim.AnimU.UType
import common.util.anim.AnimUD
import common.util.anim.EAnimU
import common.util.lang.MultiLangCont
import common.util.unit.Form.FormJson

@JCGeneric(FormJson::class)
@JsonClass
class Form : Animable<AnimU<*>?, UType?>, BasedCopable<Form?, Unit?> {
    @JsonClass(noTag = NoTag.LOAD)
    class FormJson {
        var uid: PackData.Identifier<Unit>? = null
        var fid = 0

        @JCConstructor
        constructor() {
        }

        @JCConstructor
        constructor(f: Form) {
            uid = f.uid
            fid = f.fid
        }

        @JCGetter
        fun get(): Form {
            return uid!!.get().forms[fid]
        }
    }

    @JsonField
    val du: MaskUnit?
    val unit: Unit
    val uid: PackData.Identifier<Unit>

    @JsonField
    var fid = 0
    var orbs: Orb? = null

    @JsonField
    var name = ""

    @JCConstructor
    constructor(u: Unit) {
        du = null
        unit = u
        uid = unit.id
    }

    constructor(u: Unit, f: Int, str: String, ac: AnimU<*>, cu: CustomUnit) {
        unit = u
        uid = u.id
        fid = f
        name = str
        anim = ac
        du = cu
        cu.pack = this
        orbs = Orb(-1)
    }

    constructor(u: Unit, f: Int, str: String?, data: String) {
        unit = u
        uid = u.id
        fid = f
        val nam: String = Data.Companion.trio(uid.id) + "_" + Data.Companion.SUFX.get(fid)
        anim = AnimUD(str, nam, "edi$nam.png", "uni" + nam + "00.png")
        anim.getUni().setCut(CommonStatic.getBCAssets().unicut)
        val strs = data.split("//").toTypedArray()[0].trim { it <= ' ' }.split(",").toTypedArray()
        du = DataUnit(this, unit, strs)
    }

    override fun copy(b: Unit): Form {
        val cu = CustomUnit()
        cu.importData(du)
        return Form(b, fid, name, anim, cu)
    }

    fun getDefaultPrice(sta: Int): Int {
        val pc: PCoin? = getPCoin()
        val price: Int = if (pc == null) du.getPrice() else pc.full.getPrice()
        return (price * (1 + sta * 0.5)).toInt()
    }

    override fun getEAnim(t: UType): EAnimU? {
        return anim.getEAnim(t)
    }

    fun getExplanation(): Array<String?> {
        val exp: Array<String?> = MultiLangCont.Companion.getStatic().FEXP.getCont(this)
        return exp ?: arrayOfNulls(0)
    }

    fun getPCoin(): PCoin? {
        return if (du is DataUnit) (du as DataUnit?).pcoin else null
    }

    fun maxu(): MaskUnit? {
        val pc: PCoin? = getPCoin()
        return if (pc != null) pc.full else du
    }

    fun regulateLv(mod: IntArray?, lv: IntArray): IntArray {
        if (mod != null) for (i in 0 until Math.min(mod.size, 6)) lv[i] = mod[i]
        val maxs = IntArray(6)
        maxs[0] = unit.max + unit.maxp
        var pc: PCoin? = null
        if (unit.forms.size >= 3) pc = unit.forms[2].pCoin
        if (pc != null) for (i in 0..4) maxs[i + 1] = Math.max(1, pc.info.get(i).get(1))
        for (i in 0..5) {
            if (lv[i] < 0) lv[i] = 0
            if (lv[i] > maxs[i]) lv[i] = maxs[i]
        }
        if (lv[0] == 0) lv[0] = 1
        return lv
    }

    override fun toString(): String {
        val base = uid.id.toString() + "-" + fid + " "
        val desp: String = MultiLangCont.Companion.get(this)
        if (desp != null && desp.length > 0) return base + desp
        return if (name.length > 0) base + name else base
    }

    companion object {
        fun lvString(lvs: IntArray): String {
            var str = "Lv." + lvs[0] + ", {"
            for (i in 1..4) str += lvs[i].toString() + ","
            str += lvs[5].toString() + "}"
            return str
        }
    }
}
