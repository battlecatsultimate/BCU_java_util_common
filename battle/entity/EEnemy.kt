package common.battle.entity

import common.battle.StageBasis
import common.battle.attack.AtkModelUnit
import common.battle.attack.AttackAb
import common.battle.entity.EUnit.OrbHandler
import common.util.Data
import common.util.anim.EAnimU

class EEnemy(b: StageBasis, de: MaskEnemy, ea: EAnimU?, val mult: Double, val mula: Double, d0: Int, d1: Int, val mark: Int) : Entity(b, de, ea, mula, if (mark == -1) -1 else mult) {
    override fun getAtk(): Int {
        var atk = aam.atk
        if (status[Data.Companion.P_WEAK][0] > 0) atk = atk * status[Data.Companion.P_WEAK][1] / 100
        if (status[Data.Companion.P_STRONG][0] != 0) atk += atk * status[Data.Companion.P_STRONG][0] / 100
        return atk
    }

    override fun kill() {
        super.kill()
        if (!basis.st.trail) {
            var mul = basis.b.t().dropMulti
            if (tempearn) mul *= 2.0
            basis.mon += mul * (data as MaskEnemy).getDrop()
        }
    }

    override fun getDamage(atk: AttackAb, ans: Int): Int {
        var ans = ans
        if (atk.model is AtkModelUnit) {
            val overlap = type and atk.type
            if (overlap != 0 && atk.abi and Data.Companion.AB_GOOD != 0) ans *= basis.b.t().getGOODATK(overlap).toInt()
            if (overlap != 0 && atk.abi and Data.Companion.AB_MASSIVE != 0) ans *= basis.b.t().getMASSIVEATK(overlap).toInt()
            if (overlap != 0 && atk.abi and Data.Companion.AB_MASSIVES != 0) ans *= basis.b.t().getMASSIVESATK(overlap).toInt()
        }
        if (isBase && atk.abi and Data.Companion.AB_BASE > 0) ans *= 4
        if (type and Data.Companion.TB_WITCH > 0 && atk.abi and Data.Companion.AB_WKILL > 0) ans *= basis.b.t().wkAtk.toInt()
        if (type and Data.Companion.TB_EVA > 0 && atk.abi and Data.Companion.AB_EKILL > 0) ans *= basis.b.t().ekAtk.toInt()
        if (atk.canon == 5) ans = if (touchable() and Data.Companion.TCH_UG > 0) (maxH * basis.b.t().getCanonMulti(-5) / 1000).toInt() else (maxH * basis.b.t().getCanonMulti(5) / 1000).toInt()
        ans = critCalc(data.type and Data.Companion.TB_METAL != 0, ans, atk)

        // Perform Orb
        ans += OrbHandler.getOrbAtk(atk, this)
        return ans
    }

    override fun getLim(): Double {
        var ans = 0.0
        ans = if (mark == 1) pos - 800 - data.width else pos - data.width
        return if (ans < 0) 0 else ans
    }

    override fun traitType(): Int {
        return 1
    }

    init {
        isBase = mark == -1
        layer = d0 + (b.r.nextDouble() * (d1 - d0 + 1)) as Int
        type = de.getType()
    }
}
