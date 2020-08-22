package common.util.stage

import common.battle.StageBasis
import common.battle.entity.EEnemy
import common.util.BattleObj
import common.util.unit.AbEnemy

class EStage(val s: Stage, val star: Int) : BattleObj() {
    val lim: Limit
    val num: IntArray
    val rem: IntArray
    val mul: Double
    private var b: StageBasis? = null

    /** add n new enemies to StageBasis  */
    fun allow(): EEnemy? {
        for (i in rem.indices) {
            val data = s.data.getSimple(i)
            if (inHealth(data) && s.data.allow(b, data.group) && rem[i] == 0 && num[i] != -1) {
                rem[i] = data.respawn_0 + (b.r.nextDouble() * (data.respawn_1 - data.respawn_0)) as Int
                if (num[i] > 0) {
                    num[i]--
                    if (num[i] == 0) num[i] = -1
                }
                if (data.boss == 1) b.shock = true
                val multi = (if (data.multiple == 0) 100 else data.multiple) * mul * 0.01
                val mulatk = (if (data.mult_atk == 0) 100 else data.mult_atk) * mul * 0.01
                val e: AbEnemy = data.enemy.get()
                val ee: EEnemy = e.getEntity(b, data, multi, mulatk, data.layer_0, data.layer_1, data.boss)
                ee.group = data.group
                return ee
            }
        }
        return null
    }

    fun assign(sb: StageBasis?) {
        b = sb
        val datas = s.data.simple
        for (i in rem.indices) {
            rem[i] = datas[i].spawn_0
            if (Math.abs(datas[i].spawn_0) < Math.abs(datas[i].spawn_1)) rem[i] += ((datas[i].spawn_1 - datas[i].spawn_0) * b.r.nextDouble()) as Int
        }
    }

    /** get the Entity representing enemy base, return null if none  */
    fun base(sb: StageBasis?): EEnemy? {
        val ind = num.size - 1
        if (ind < 0) return null
        val data = s.data.getSimple(ind)
        if (data.castle_0 == 0) {
            num[ind] = -1
            val multi = data.multiple * mul * 0.01
            val mulatk = if (data.mult_atk == 0) multi else data.mult_atk * mul * 0.01
            val e: AbEnemy = data.enemy.get()
            return e.getEntity(sb, this, multi, mulatk, data.layer_0, data.layer_1, -1)
        }
        return null
    }

    /** return true if there is still boss in the base  */
    fun hasBoss(): Boolean {
        for (i in rem.indices) {
            val data = s.data.getSimple(i)
            if (data.boss == 1 && num[i] > 0) return true
        }
        return false
    }

    fun update() {
        for (i in rem.indices) {
            val data = s.data.getSimple(i)
            if (inHealth(data) && rem[i] < 0) rem[i] *= -1
            if (rem[i] > 0) rem[i]--
        }
    }

    private fun inHealth(line: SCDef.Line): Boolean {
        val c0 = line.castle_0
        val c1 = line.castle_1
        val d: Double = if (!s.trail) b.getEBHP() * 100 else b.ebase.maxH - b.ebase.health
        return if (c0 >= c1) if (s.trail) d >= c0 else d <= c0 else d > c0 && d <= c1
    }

    init {
        s.validate()
        val datas = s.data.simple
        rem = IntArray(datas.size)
        num = IntArray(datas.size)
        for (i in rem.indices) num[i] = datas[i].number
        lim = s.getLim(star)
        mul = s.map.stars[star] * 0.01
    }
}
