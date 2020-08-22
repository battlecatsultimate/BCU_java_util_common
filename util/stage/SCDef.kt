package common.util.stage

import common.battle.StageBasis
import common.io.InStream
import common.io.json.JsonClass
import common.io.json.JsonClass.JCConstructor
import common.io.json.JsonClass.NoTag
import common.io.json.JsonField
import common.io.json.JsonField.GenType
import common.pack.FixIndexList
import common.pack.PackData
import common.pack.PackData.PackDesc
import common.system.Copable
import common.util.Data
import common.util.stage.SCDef
import common.util.stage.SCGroup
import common.util.unit.AbEnemy
import common.util.unit.Enemy
import java.util.*
import java.util.function.BiConsumer

@JsonClass
class SCDef : Copable<SCDef?> {
    @JsonClass(noTag = NoTag.LOAD)
    class Line : Cloneable {
        var enemy: PackData.Identifier<AbEnemy>? = null
        var number = 0
        var boss = 0
        var multiple = 0
        var group = 0
        var spawn_0 = 0
        var spawn_1 = 0
        var respawn_0 = 0
        var respawn_1 = 0
        var castle_0 = 0
        var castle_1 = 0
        var layer_0 = 0
        var layer_1 = 0
        var mult_atk = 0

        @JCConstructor
        constructor() {
        }

        constructor(arr: IntArray) {
            enemy = PackData.Identifier.Companion.parseInt<AbEnemy>(arr[E], Enemy::class.java)
            number = arr[N]
            boss = arr[B]
            multiple = arr[M]
            group = arr[G]
            spawn_0 = arr[S0]
            respawn_0 = arr[R0]
            castle_0 = arr[C0]
            layer_0 = arr[L0]
            spawn_1 = arr[S1]
            respawn_1 = arr[R1]
            castle_1 = arr[C1]
            layer_1 = arr[L1]
            mult_atk = arr[M1]
        }

        public override fun clone(): common.util.stage.SCDef.Line {
            return try {
                super.clone() as common.util.stage.SCDef.Line
            } catch (e: CloneNotSupportedException) {
                e.printStackTrace()
                null
            }
        }
    }

    @JsonField
    var datas: Array<common.util.stage.SCDef.Line?>

    @JsonField(gen = GenType.FILL)
    val sub: FixIndexList<SCGroup> = FixIndexList<SCGroup>(SCGroup::class.java)

    @JsonField(generic = [PackData.Identifier::class, Int::class])
    val smap: MutableMap<PackData.Identifier<AbEnemy>, Int> = TreeMap<PackData.Identifier<AbEnemy>, Int>()

    @JsonField
    var sdef = 0

    protected constructor() {}
    protected constructor(`is`: InStream, ver: Int) {
        if (ver >= 305) {
            val n: Int = `is`.nextByte()
            datas = arrayOfNulls(n)
            val tmp = IntArray(SIZE)
            for (i in 0 until n) {
                Arrays.fill(tmp, 0)
                for (j in 0..9) tmp[j] = `is`.nextInt()
                tmp[M1] = tmp[M]
                datas[i] = common.util.stage.SCDef.Line(tmp)
            }
        } else if (ver >= 203) {
            val n: Int = `is`.nextByte()
            datas = arrayOfNulls(n)
            val tmp = IntArray(SIZE)
            for (i in 0 until n) {
                Arrays.fill(tmp, 0)
                for (j in 0..9) tmp[j] = `is`.nextInt()
                if (tmp[5] < 100) tmp[2] *= -1
                tmp[M1] = tmp[M]
                datas[i] = common.util.stage.SCDef.Line(tmp)
            }
        } else datas = arrayOfNulls(0)
    }

    constructor(s: Int) {
        datas = arrayOfNulls(s)
    }

    fun allow(sb: StageBasis?, e: AbEnemy): Int {
        var o = smap[e.getID()]
        o = o ?: sdef
        return if (allow(sb, o)) o else -1
    }

    fun allow(sb: StageBasis, `val`: Int): Boolean {
        if (sb.entityCount(1) >= sb.st.max) return false
        if (`val` < 0 || `val` > 1000 || sub.get(`val`) == null) return true
        val g: SCGroup = sub.get(`val`)
        return sb.entityCount(1, `val`) < g.getMax(sb.est.star)
    }

    operator fun contains(e: Enemy): Boolean {
        for (dat in datas) if (dat!!.enemy === e.id) return true
        return false
    }

    override fun copy(): SCDef {
        val ans = SCDef(datas.size)
        for (i in datas.indices) ans.datas[i] = datas[i]!!.clone()
        ans.sdef = sdef
        smap.forEach(BiConsumer<PackData.Identifier<AbEnemy>, Int> { e: PackData.Identifier<AbEnemy>, i: Int -> ans.smap.put(e, i) })
        sub.forEach(BiConsumer<Int, SCGroup> { i: Int, e: SCGroup -> ans.sub.set(i, e.copy(i)) })
        return ans
    }

    fun getAllEnemy(): Set<Enemy> {
        val l: MutableSet<Enemy> = TreeSet<Enemy>()
        for (dat in datas) l.addAll(dat!!.enemy.get().possible)
        for (e in getSummon()) l.addAll(e.getPossible())
        return l
    }

    fun getSimple(): Array<common.util.stage.SCDef.Line?> {
        return datas
    }

    fun getSimple(i: Int): common.util.stage.SCDef.Line? {
        return datas[i]
    }

    fun getSMap(): Array<Map.Entry<PackData.Identifier<AbEnemy>, Int>> {
        return smap.entries.toTypedArray<Map.Entry<*, *>>()
    }

    fun getSummon(): Set<AbEnemy> {
        val ans: MutableSet<AbEnemy> = TreeSet<AbEnemy>()
        val temp: MutableSet<AbEnemy> = TreeSet<AbEnemy>()
        val pre: MutableSet<Enemy> = TreeSet<Enemy>()
        val post: MutableSet<Enemy> = TreeSet<Enemy>()
        for (line in datas) {
            val e: AbEnemy? = line!!.enemy.get()
            if (e != null) pre.addAll(e.getPossible())
        }
        while (pre.size > 0) {
            for (e in pre) temp.addAll(e.de.getSummon())
            ans.addAll(temp)
            post.addAll(pre)
            pre.clear()
            for (e in temp) pre.addAll(e.getPossible())
            pre.removeAll(post)
            temp.clear()
        }
        return ans
    }

    fun isSuitable(pack: PackDesc): Boolean {
        for (ints in datas) {
            if (ints!!.enemy.pack == PackData.Identifier.Companion.DEF) continue
            var b = ints.enemy.pack == pack.id
            for (rel in pack.dependency) b = b or (ints.enemy.pack == rel)
            if (!b) return false
        }
        return true
    }

    fun isTrail(): Boolean {
        for (data in datas) if (data!!.castle_0 > 100) return true
        return false
    }

    companion object {
        @Deprecated("")
        val SIZE = 14

        @Deprecated("")
        val E = 0

        @Deprecated("")
        val N = 1

        @Deprecated("")
        val S0 = 2

        @Deprecated("")
        val R0 = 3

        @Deprecated("")
        val R1 = 4

        @Deprecated("")
        val C0 = 5

        @Deprecated("")
        val L0 = 6

        @Deprecated("")
        val L1 = 7

        @Deprecated("")
        val B = 8

        @Deprecated("")
        val M = 9

        @Deprecated("")
        val S1 = 10

        @Deprecated("")
        val C1 = 11

        @Deprecated("")
        val G = 12

        @Deprecated("")
        val M1 = 13
        fun zread(`is`: InStream): SCDef? {
            val t: Int = `is`.nextInt()
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (t == 0) {
                if (ver >= 402) {
                    var n: Int = `is`.nextInt()
                    val m: Int = `is`.nextInt()
                    val scd = SCDef(n)
                    val tmp = IntArray(SIZE)
                    for (i in 0 until n) {
                        Arrays.fill(tmp, 0)
                        for (j in 0 until m) tmp[j] = `is`.nextInt()
                        if (m < 14) tmp[M1] = tmp[M]
                        scd.datas[i] = common.util.stage.SCDef.Line(tmp)
                    }
                    scd.sdef = `is`.nextInt()
                    n = `is`.nextInt()
                    for (i in 0 until n) scd.smap[PackData.Identifier.Companion.parseInt<AbEnemy>(`is`.nextInt(), Enemy::class.java)] = `is`.nextInt()
                    n = `is`.nextInt()
                    for (i in 0 until n) {
                        val scg: SCGroup = SCGroup.Companion.zread(`is`)
                        scd.sub.set(scg!!.id, scg)
                    }
                    return scd
                }
            }
            return null
        }
    }
}
