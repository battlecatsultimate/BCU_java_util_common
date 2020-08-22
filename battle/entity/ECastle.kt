package common.battle.entity

import common.CommonStatic
import common.battle.BasisLU
import common.battle.StageBasis
import common.battle.attack.AttackAb
import common.util.Data
import common.util.pack.EffAnim.DefEff

class ECastle : AbEntity {
    private val sb: StageBasis

    constructor(b: StageBasis) : super(b.st.health) {
        sb = b
    }

    constructor(xb: StageBasis, b: BasisLU) : super(b.t().getBaseHealth()) {
        sb = xb
    }

    override fun damaged(atk: AttackAb) {
        var ans: Int = atk.atk
        if (atk.abi and Data.Companion.AB_BASE > 0) ans *= 4
        val satk: Int = atk.getProc().SATK.mult
        if (satk > 0) ans *= (100 + satk) * 0.01.toInt()
        if (atk.getProc().CRIT.mult > 0) {
            ans *= 0.01 * atk.getProc().CRIT.mult.toInt()
            sb.lea.add(EAnimCont(pos, 9, Data.Companion.effas().A_CRIT.getEAnim(DefEff.DEF)))
            CommonStatic.setSE(Data.Companion.SE_CRIT)
        } else CommonStatic.setSE(Data.Companion.SE_HIT_BASE)
        health -= ans.toLong()
        if (health > maxH) health = maxH
        if (health <= 0) health = 0
    }

    override fun getAbi(): Int {
        return 0
    }

    override fun isBase(): Boolean {
        return true
    }

    override fun postUpdate() {}
    override fun targetable(type: Int): Boolean {
        return true
    }

    override fun touchable(): Int {
        return Data.Companion.TCH_N
    }

    override fun update() {}
}
