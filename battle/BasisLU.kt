package common.battle

import common.battle.BasisSet
import common.io.InStream
import common.io.json.JsonClass
import common.io.json.JsonClass.RType
import common.io.json.JsonField
import common.io.json.JsonField.GenType
import common.pack.PackData
import common.pack.UserProfile
import common.system.Copable
import common.util.BattleStatic
import common.util.Data
import common.util.unit.Unit

@JsonClass(read = RType.FILL)
class BasisLU : Basis, Copable<BasisLU?>, BattleStatic {
    private val t: Treasure

    @JsonField(gen = GenType.FILL)
    val lu: LineUp

    @JsonField(gen = GenType.FILL)
    var nyc = IntArray(3)

    constructor(bs: BasisSet) {
        t = Treasure(this, bs.t())
        lu = LineUp()
        name = "lineup " + bs.lb.size
    }

    constructor(bs: BasisSet, line: LineUp, str: String) {
        t = Treasure(this, bs.t())
        name = str
        lu = line
    }

    constructor(bs: BasisSet, line: LineUp, str: String, ints: IntArray) : this(bs, line, str) {
        nyc = ints
    }

    constructor(bs: BasisSet, bl: BasisLU?) {
        t = Treasure(this, bs.t())
        lu = LineUp(bl!!.lu)
        name = "lineup " + bs.lb.size
        nyc = bl!!.nyc.clone()
    }

    private constructor(ver: Int, `is`: InStream) {
        lu = LineUp(ver, if (ver >= 308) `is`.subStream() else `is`)
        t = Treasure(this, ver, `is`)
    }

    override fun copy(): BasisLU {
        return BasisLU(BasisSet.Companion.current(), this)
    }

    override fun getInc(type: Int): Int {
        return lu.inc.get(type)
    }

    fun randomize(n: Int): BasisLU {
        val ans = copy()
        val rad = getRandom(n)
        val list: MutableList<Unit> = UserProfile.Companion.getBCData().units.getList()
        list.remove(PackData.Identifier.Companion.parseInt<Unit>(339, Unit::class.java).get())
        for (fs in ans.lu.fs) for (f in fs) if (f != null) list.remove(f.unit)
        for (i in 0 until n) {
            val u = list[(Math.random() * list.size).toInt()]
            list.remove(u)
            ans.lu.setFS(u.forms[u.forms.size - 1], rad[i])
        }
        ans.lu.arrange()
        return ans
    }

    /**
     * although the Treasure information is the same, this includes the effects of
     * combo, so need to be an independent Treasure Object
     */
    override fun t(): Treasure {
        return t
    }

    companion object {
        fun zread(`is`: InStream): BasisLU {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            return if (ver >= 308) `zread$000308`(`is`) else `zread$000307`(`is`)
        }

        private fun getRandom(n: Int): IntArray {
            val ans = IntArray(n)
            var a = 0
            for (i in 0 until n) {
                var x = (Math.random() * 10).toInt()
                while (a and (1 shl x) > 0) x = (Math.random() * 10).toInt()
                a = a or (1 shl x)
                ans[i] = x
            }
            return ans
        }

        private fun `zread$000307`(`is`: InStream): BasisLU {
            val name: String = `is`.nextString()
            val ans = BasisLU(307, `is`)
            ans.nyc = `is`.nextIntsB()
            ans.name = name
            return ans
        }

        private fun `zread$000308`(`is`: InStream): BasisLU {
            val name: String = `is`.nextString()
            val ans = BasisLU(308, `is`)
            ans.nyc = `is`.nextIntsB()
            ans.name = name
            return ans
        }
    }
}
