package common.util.unit

import common.CommonStatic
import common.battle.data.CustomUnit
import common.battle.data.PCoin
import common.io.json.JsonClass
import common.io.json.JsonClass.*
import common.io.json.JsonField
import common.pack.IndexContainer.IndexCont
import common.pack.IndexContainer.Indexable
import common.pack.PackData
import common.system.files.VFile
import common.util.Data
import common.util.anim.AnimCE
import common.util.lang.MultiLangCont
import common.util.unit.Unit
import common.util.unit.UnitLevel
import java.util.*
import kotlin.Array
import kotlin.Comparable
import kotlin.Int
import kotlin.IntArray
import kotlin.String
import kotlin.arrayOf
import kotlin.arrayOfNulls

@IndexCont(PackData::class)
@JCGeneric(PackData.Identifier::class)
@JsonClass(noTag = NoTag.LOAD)
class Unit : Data, Comparable<Unit?>, Indexable<PackData?, Unit?> {
    class UnitInfo {
        var evo: Array<IntArray>
        var price = IntArray(10)
        var explanation: Array<Array<String>>
        var type = 0
        fun fillBuy(strs: Array<String>) {
            for (i in 0..9) price[i] = strs[2 + i].toInt()
            type = strs[12].toInt()
            val et = strs[23].toInt()
            if (et >= 15000 && et < 17000) {
                evo = Array(6) { IntArray(2) }
                evo[0][0] = strs[27].toInt()
                for (i in 0..4) {
                    evo[i + 1][0] = strs[28 + i * 2].toInt()
                    evo[i + 1][1] = strs[29 + i * 2].toInt()
                }
            }
        }

        fun getExplanation(): Array<String?> {
            val exp: Array<String?> = MultiLangCont.Companion.getStatic().CFEXP.getCont(this)
            return exp
        }
    }

    @JCIdentifier
    val id: PackData.Identifier<Unit>
    var rarity = 0
    var max = 0
    var maxp = 0
    var forms: Array<Form?>

    @JsonField(alias = [PackData.Identifier::class])
    var lv: UnitLevel? = null
    val info = UnitInfo()

    constructor(identifier: PackData.Identifier<Unit>) {
        id = identifier
    }

    constructor(id: PackData.Identifier<Unit>, ce: AnimCE?, cu: CustomUnit?) {
        this.id = id
        forms = arrayOf(Form(this, 0, "new unit", ce, cu))
        max = 50
        maxp = 0
        rarity = 4
        lv = UnitLevel.Companion.def
        lv!!.units.add(this)
    }

    constructor(p: VFile<*>) {
        id = PackData.Identifier(PackData.Identifier.Companion.DEF, Unit::class.java, CommonStatic.parseIntN(p.getName()))
        val str = "./org/unit/" + Data.Companion.trio(id.id) + "/"
        val qs: Queue<String> = VFile.Companion.readLine(str + "unit" + Data.Companion.trio(id.id) + ".csv")
        forms = arrayOfNulls(p.countSubDire())
        for (i in forms.indices) forms[i] = Form(this, i, str + Data.Companion.SUFX.get(i) + "/", qs.poll())
        for (f in forms) f!!.anim.edi.check()
    }

    protected constructor(id: PackData.Identifier<Unit>, u: Unit) {
        this.id = id
        rarity = u.rarity
        max = u.max
        maxp = u.maxp
        lv = u.lv
        lv.units.add(u)
        forms = arrayOfNulls(u.forms.size)
        for (i in forms.indices) {
            val str: String = AnimCE.Companion.getAvailable("$id-$i")
            val ac = AnimCE(str, u.forms[i].anim)
            val cu = CustomUnit()
            cu.importData(u.forms[i].du)
            forms[i] = Form(this, i, str, ac, cu)
        }
    }

    fun allCombo(): List<Combo> {
        val ans: MutableList<Combo> = ArrayList<Combo>()
        if (id.pack != "_default") return ans
        for (cs in CommonStatic.getBCAssets().combos) for (c in cs) for (`is` in c.units) if (`is`[0] == id.id) {
            ans.add(c)
            break
        }
        return ans
    }

    override operator fun compareTo(u: Unit): Int {
        return id.compareTo(u.id)
    }

    override fun getID(): PackData.Identifier<Unit>? {
        return id
    }

    fun getPrefLv(): Int {
        return max + if (rarity < 2) maxp else 0
    }

    fun getPrefLvs(): IntArray {
        var ans = IntArray(6)
        if (forms.size >= 3) {
            val pc: PCoin? = forms[2]!!.pCoin
            if (pc != null) ans = pc.max.clone()
        }
        ans[0] = getPrefLv()
        return ans
    }

    override fun toString(): String {
        val desp: String = MultiLangCont.Companion.get(forms[0])
        if (desp != null && desp.length > 0) return Data.Companion.trio(id.id) + " " + desp
        return if (forms[0]!!.name.length > 0) Data.Companion.trio(id.id) + " " + forms[0]!!.name else Data.Companion.trio(id.id)
    }
}
