package common.battle

import common.CommonStatic
import common.io.InStream
import common.io.json.JsonClass
import common.io.json.JsonDecoder.OnInjected
import common.io.json.JsonField
import common.pack.PackData
import common.util.Data
import common.util.unit.Combo
import common.util.unit.EForm
import common.util.unit.Form
import common.util.unit.Form.FormJson
import common.util.unit.Level
import common.util.unit.Unit
import java.util.*
import kotlin.Array
import kotlin.Boolean
import kotlin.BooleanArray
import kotlin.Int
import kotlin.IntArray
import kotlin.arrayOfNulls
import kotlin.run

@JsonClass
class LineUp : Data {
    @JsonField(generic = [Unit::class, Level::class], alias = [PackData.Identifier::class])
    val map: TreeMap<Unit, Level> = TreeMap<Unit, Level>()

    @JsonField(alias = [FormJson::class])
    val fs = Array(2) { arrayOfNulls<Form>(5) }
    val efs: Array<Array<EForm?>> = Array<Array<EForm?>>(2) { arrayOfNulls<EForm>(5) }
    var inc = IntArray(Data.Companion.C_TOT)
    var loc = IntArray(5)
    var coms: MutableList<Combo> = ArrayList<Combo>()
    private var updating = false

    /** new LineUp object  */
    constructor() {
        renew()
    }

    /** read a LineUp object from data  */
    constructor(ver: Int, `is`: InStream) {
        zread(ver, `is`)
        renew()
    }

    /** clone a LineUp object  */
    constructor(ref: LineUp) {
        for (i in 0..1) for (j in 0..4) fs[i][j] = ref.fs[i][j]
        for ((key, value) in ref.map.entries) {
            map.put(key, value.clone())
        }
        renew()
    }

    /** shift all cats to lowest index possible  */
    fun arrange() {
        for (i in 0..9) if (getFS(i) == null) for (j in i + 1..9) if (getFS(j) != null) {
            setFS(getFS(j), i)
            setFS(null, j)
            break
        } else if (j == 9) return
    }

    /** test whether contains certain combo  */
    operator fun contains(c: Combo): Boolean {
        for (com in coms) if (com === c) return true
        return false
    }

    /** get level of an Unit, if no date recorded, record default one  */
    @Synchronized
    fun getLv(u: Unit): Level {
        if (!map.containsKey(u)) setLv(u, u.prefLvs)
        return map.get(u)
    }

    /**
     * return how much space from 1st row a combo will need to put in this lineup
     */
    fun occupance(c: Combo): Int {
        val com: Array<IntArray> = c.units
        var rem = com.size
        for (i in com.indices) for (j in 0..4) {
            val f = fs[0][j] ?: continue
            if (eq(f.uid, com[i][0])) rem--
        }
        return rem
    }

    @OnInjected
    fun renew() {
        validate()
        renewEForm()
        renewCombo()
    }

    /** apply a combo  */
    fun set(com: Array<IntArray>) {
        // if a unit in the lineup is present in the combo
        val rep = BooleanArray(5)
        // if a unit in the combo is already present in the lineup
        val exi = BooleanArray(com.size)
        // the number of units required to inject
        var rem = com.size
        for (i in com.indices) for (j in 0..4) {
            val f = fs[0][j] ?: continue
            if (eq(f.uid, com[i][0])) {
                rep[j] = true
                exi[i] = true
                if (f.fid < com[i][1]) fs[0][j] = f.unit.forms[com[i][1]]
                loc[j]++
                rem--
            }
        }
        // number of units not present in any combo
        var free = 0
        for (i in 0..4) if (loc[i] == 0) free++
        if (free < rem) {
            // required to remove some combo
            var del = rem - free
            while (del > 0) {
                val c: Combo = coms.removeAt(0)
                for (i in c.units.indices) {
                    if (c.units.get(i).get(0) == -1) break
                    for (j in 0..4) {
                        val f = fs[0][j] ?: break
                        if (!eq(f.uid, c.units.get(i).get(0))) continue
                        loc[j]--
                        if (loc[j] == 0) del--
                        break
                    }
                }
            }
        }
        for (i in 0..4) for (`is` in com) if (fs[1][i] != null && eq(fs[1][i]!!.uid, `is`[0])) fs[1][i] = null
        arrange()
        var emp = 0
        for (i in 0..9) if (getFS(i) == null) emp++
        if (emp < rem) {
            for (i in 10 - rem until 10 - emp) setFS(null, i)
            emp = rem
        }
        var p = 0
        var r = 0
        var i = 0
        var j = 10 - emp
        while (r < rem) {
            while (loc[i] != 0) i++
            while (exi[p]) p++
            setFS(getFS(i), j++)
            val c = com[p++]
            setFS(PackData.Identifier.Companion.parseInt<Unit>(c[0], Unit::class.java).get().forms.get(c[1]), i++)
            r++
        }
        renew()
    }

    /** set level record of an Unit  */
    @Synchronized
    fun setLv(u: Unit?, lv: IntArray?) {
        val sub = updating
        updating = true
        var l: Level = map.get(u)
        if (l != null) {
            l.lvs = lv
        } else {
            l = Level(lv)
            map.put(u, l)
        }
        if (!sub) renewEForm()
        updating = updating and sub
    }

    /** set orb data of an Unit  */
    @Synchronized
    fun setOrb(u: Unit?, lvs: IntArray?, orbs: Array<IntArray?>?) {
        // lvs must be generated before doing something with orbs
        val sub = updating
        updating = true
        var l: Level = map.get(u)
        if (l != null) {
            l.lvs = lvs
            l.orbs = orbs
        } else {
            l = Level(lvs, orbs)
            map.put(u, l)
        }
        if (!sub) renewEForm()
        updating = updating and sub
    }

    /** return whether implementing this combo will replace other combo  */
    fun willRem(c: Combo): Boolean {
        var free = 0
        for (i in 0..4) if (fs[0][i] == null) free++ else if (loc[i] == 0) {
            var b = true
            for (`is` in c.units) if (eq(fs[0][i]!!.uid, `is`[0])) {
                b = false
                break
            }
            if (b) free++
        }
        return free < occupance(c)
    }

    /** set slot using 1 dim index  */
    fun setFS(f: Form?, i: Int) {
        fs[i / 5][i % 5] = f
    }

    /** get Form from 1 dim index  */
    private fun getFS(i: Int): Form? {
        return fs[i / 5][i % 5]
    }

    /** check combo information  */
    private fun renewCombo() {
        val tcom: MutableList<Combo> = ArrayList<Combo>()
        inc = IntArray(Data.Companion.C_TOT)
        loc = IntArray(5)
        for (cs in CommonStatic.getBCAssets().combos) for (c in cs) {
            var b = true
            for (i in c.units.indices) {
                if (c.units.get(i).get(0) == -1) break
                var b0 = false
                for (j in 0..4) {
                    val f = fs[0][j] ?: break
                    if (!eq(f.uid, c.units.get(i).get(0)) || f.fid < c.units.get(i).get(1)) continue
                    b0 = true
                    break
                }
                if (b0) continue
                b = false
                break
            }
            if (b) {
                tcom.add(c)
                inc[c.type] += CommonStatic.getBCAssets().values.get(c.type).get(c.lv)
                for (i in c.units.indices) for (j in 0..4) {
                    val f = fs[0][j] ?: continue
                    if (eq(f.uid, c.units.get(i).get(0)) && f.fid >= c.units.get(i).get(1)) loc[j]++
                }
            }
        }
        run {
            var i = 0
            while (i < coms.size) {
                if (!tcom.contains(coms[i])) {
                    coms.removeAt(i)
                    i--
                }
                i++
            }
        }
        for (i in tcom.indices) if (!coms.contains(tcom[i])) coms.add(tcom[i])
    }

    private fun renewEForm() {
        for (i in 0..1) for (j in 0..4) if (fs[i][j] == null) efs[i][j] = null else efs[i][j] = EForm(fs[i][j], getLv(fs[i][j]!!.unit))
    }

    private fun validate() {
        for (i in 0..9) if (getFS(i) != null) {
            val id = getFS(i)!!.uid
            val f = getFS(i)!!.fid
            val u = id.get()
            if (u == null || u.forms[f] == null) setFS(null, i)
        }
        arrange()
    }

    /** read data from file, support multiple version  */
    private fun zread(ver: Int, `is`: InStream) {
        val `val`: Int = Data.Companion.getVer(`is`.nextString())
        if (`val` >= 400) `zread$000400`(`is`)
    }

    private fun `zread$000400`(`is`: InStream) {
        val n: Int = `is`.nextInt()
        for (i in 0 until n) {
            val uid: Int = `is`.nextInt()
            val fid: Int = `is`.nextInt()
            setFS(PackData.Identifier.Companion.parseInt<Unit>(uid, Unit::class.java).get().forms.get(fid), i)
        }
        val m: Int = `is`.nextInt()
        for (i in 0 until m) {
            val uid: Int = `is`.nextInt()
            val lv: IntArray = `is`.nextIntsB()
            val u: Unit = PackData.Identifier.Companion.parseInt<Unit>(uid, Unit::class.java).get()
            var orbs: Array<IntArray?>? = null
            val existing: Int = `is`.nextInt()
            if (existing == 1) {
                orbs = `is`.nextIntsBB()
            }
            if (u != null) map.put(u, Level(lv, orbs))
        }
        arrange()
    }

    companion object {
        fun eq(id: PackData.Identifier<Unit?>, com: Int): Boolean {
            return id.pack == PackData.Identifier.Companion.DEF && id.id == com
        }
    }
}
