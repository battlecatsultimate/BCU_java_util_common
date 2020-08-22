package common.battle.attack

import common.battle.entity.EEnemy
import common.battle.entity.EntCont
import common.battle.entity.Entity
import common.util.Data
import common.util.Data.Proc.SUMMON
import common.util.unit.AbEnemy

class AtkModelEnemy(ent: EEnemy, d0: Double) : AtkModelEntity(ent, d0) {
    private val cursed: Array<Proc?>
    override fun summon(proc: SUMMON, ent: Entity, acs: Any?) {
        val ene: AbEnemy = proc.id.get() as AbEnemy
        val conf: Data.Proc.SUMMON.TYPE = proc.type
        val time: Int = proc.time
        val allow: Int = b.st.data.allow(b, ene)
        if (ene != null && (allow >= 0 || conf.ignore_limit)) {
            var ep: Double = ent.pos + getDire() * proc.dis
            var mula: Double = proc.mult * 0.01
            var mult: Double = proc.mult * 0.01
            if (!conf.fix_buff) {
                mult *= (e as EEnemy).mult
                mula *= (e as EEnemy).mula
            }
            var l0 = 0
            var l1 = 9
            if (!conf.random_layer) {
                l1 = e.layer
                l0 = l1
            }
            val ee: EEnemy = ene.getEntity(b, acs, mult, mula, 0, l0, l1)
            ee.group = allow
            if (ep < ee.data.getWidth()) ep = ee.data.getWidth().toDouble()
            if (ep > b.st.len - 800) ep = b.st.len - 800.toDouble()
            ee.added(1, ep.toInt())
            b.tempe.add(EntCont(ee, time))
            if (conf.same_health) ee.health = e.health
            ee.setSummon(conf.anim_type)
        }
    }

    protected override fun getAttack(ind: Int, proc: Proc): Int {
        var atk: Int = atks.get(ind)
        extraAtk(ind)
        if (abis.get(ind) == 1) setProc(ind, proc)
        if (e.status.get(Data.Companion.P_WEAK).get(0) > 0) atk = atk * e.status.get(Data.Companion.P_WEAK).get(1) / 100
        if (e.status.get(Data.Companion.P_STRONG).get(0) != 0) atk += atk * e.status.get(Data.Companion.P_STRONG).get(0) / 100
        return atk
    }

    protected override fun getBaseAtk(ind: Int): Int {
        return atks.get(ind)
    }

    protected override fun getProc(ind: Int): Proc? {
        return if (e.status.get(Data.Companion.P_CURSE).get(0) > 0 && e.status.get(Data.Companion.P_SEAL).get(0) == 0) cursed[ind] else super.getProc(ind)
    }

    init {
        val arr = arrayOf("KB", "STOP", "SLOW", "WEAK", "WARP", "CURSE", "SNIPER", "SEAL", "POISON", "BOSS", "IMUATK",
                "POIATK")
        cursed = arrayOfNulls<Proc>(data.getAtkCount())
        for (i in cursed.indices) {
            cursed[i] = data.getAtkModel(i).getProc().clone()
            for (s0 in arr) cursed[i].get(s0).clear()
        }
    }
}
