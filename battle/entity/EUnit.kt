package common.battle.entity

import common.battle.StageBasis
import common.battle.attack.AtkModelEnemy
import common.battle.attack.AtkModelUnit
import common.battle.attack.AttackAb
import common.util.BattleObj
import common.util.Data
import common.util.anim.EAnimU
import common.util.unit.Level

class EUnit(b: StageBasis, de: MaskUnit, ea: EAnimU?, d0: Double, level: Level?) : Entity(b, de, ea, d0 * b.b.t().getAtkMulti(), d0 * b.b.t().getDefMulti()) {
    object OrbHandler : BattleObj {
        fun getOrbAtk(atk: AttackAb, en: EEnemy): Int {
            if (atk.matk == null) {
                return 0
            }
            if (atk.origin.model is AtkModelUnit) {
                // Warning : Eunit.e became public now
                val unit = atk.origin.model.e as EUnit
                return unit.getOrbAtk(en.type, atk.matk)
            }
            return 0
        }
    }

    protected val level: Level?
    override fun getAtk(): Int {
        var atk = aam.atk
        if (status[Data.Companion.P_WEAK][0] > 0) atk = atk * status[Data.Companion.P_WEAK][1] / 100
        if (status[Data.Companion.P_STRONG][0] != 0) atk += atk * (status[Data.Companion.P_STRONG][0] + basis.b.getInc(Data.Companion.C_STRONG)) / 100
        return atk
    }

    override fun update() {
        super.update()
        type = if (status[Data.Companion.P_CURSE][0] == 0) data.type else 0
    }

    override fun getDamage(atk: AttackAb, ans: Int): Int {
        var ans = ans
        if (atk.model is AtkModelEnemy) {
            val overlap = type and atk.type
            if (overlap != 0 && abi and Data.Companion.AB_GOOD != 0) ans *= basis.b.t().getGOODDEF(overlap).toInt()
            if (overlap != 0 && abi and Data.Companion.AB_RESIST != 0) ans *= basis.b.t().getRESISTDEF(overlap).toInt()
            if (overlap != 0 && abi and Data.Companion.AB_RESISTS != 0) ans *= basis.b.t().getRESISTSDEF(overlap).toInt()
        }
        if (atk.type and Data.Companion.TB_WITCH > 0 && abi and Data.Companion.AB_WKILL > 0) ans *= basis.b.t().wkDef.toInt()
        if (atk.type and Data.Companion.TB_EVA > 0 && abi and Data.Companion.AB_EKILL > 0) ans *= basis.b.t().ekDef.toInt()
        if (isBase && atk.abi and Data.Companion.AB_BASE > 0) ans *= 4
        ans = critCalc(abi and Data.Companion.AB_METALIC != 0, ans, atk)

        // Perform orb
        ans += getOrbRes(atk.type, atk.matk)
        return ans
    }

    override fun getLim(): Double {
        return basis.st.len - pos
    }

    override fun traitType(): Int {
        return -1
    }

    override fun updateMove(maxl: Double, extmov: Double): Boolean {
        var extmov = extmov
        if (status[Data.Companion.P_SLOW][0] == 0) extmov += data.speed * basis.b.getInc(Data.Companion.C_SPE) / 200.0
        return super.updateMove(maxl, extmov)
    }

    private fun getOrbAtk(trait: Int, matk: MaskAtk): Int {
        val orb: Orb = (data as MaskUnit).getOrb()
        if (orb == null || level!!.orbs == null) {
            return 0
        }
        var ans = 0
        for (line in level.orbs) {
            if (line.size == 0) continue
            if (line[Data.Companion.ORB_TYPE] == Data.Companion.ORB_RES || line[Data.Companion.ORB_TRAIT] and trait == 0) continue
            ans += orb.getAtk(line[Data.Companion.ORB_GRADE], matk)
        }
        return ans
    }

    private fun getOrbRes(trait: Int, matk: MaskAtk): Int {
        val orb: Orb = (data as MaskUnit).getOrb()
        if (orb == null || level!!.orbs == null) return 0
        var ans = 0
        for (line in level.orbs) {
            if (line.size == 0) continue
            if (line[Data.Companion.ORB_TYPE] == Data.Companion.ORB_ATK || line[Data.Companion.ORB_TRAIT] and trait == 0) continue
            ans += orb.getRes(line[Data.Companion.ORB_GRADE], matk)
        }
        return ans
    }

    init {
        layer = de.getFront() + (b.r.nextDouble() * (de.getBack() - de.getFront() + 1)) as Int
        type = de.getType()
        this.level = level
    }
}
