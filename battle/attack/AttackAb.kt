package common.battle.attack

import common.battle.data.MaskAtk
import common.battle.entity.AbEntity
import common.battle.entity.Entityimport
import common.util.BattleObj
import common.util.Data
import java.util.*

abstract class AttackAb : BattleObj {
    val atk: Int
    val type: Int
    val abi: Int
    val model: AtkModelAb
    val origin: AttackAb
    val matk: MaskAtk?
    var touch: Int = Data.Companion.TCH_N
    var dire: Int
    var canon = -2
    var waveType = 0
    protected val proc: Proc
    val capt: List<AbEntity> = ArrayList<AbEntity>()
    var sta: Double
    var end: Double

    protected constructor(ent: AtkModelAb, ATK: Int, t: Int, eab: Int, pro: Proc, p0: Double, p1: Double, matk: MaskAtk?) {
        dire = ent.getDire()
        origin = this
        model = ent
        type = t
        atk = ATK
        proc = pro
        abi = eab
        sta = p0
        end = p1
        this.matk = matk
    }

    protected constructor(a: AttackAb, STA: Double, END: Double) {
        dire = a.dire
        origin = a.origin
        model = a.model
        atk = a.atk
        abi = a.abi
        type = a.type
        proc = a.proc
        touch = a.touch
        canon = a.canon
        sta = STA
        end = END
        matk = a.matk
    }

    /** capture the entities  */
    abstract fun capture()

    /** apply this attack to the entities captured  */
    abstract fun excuse()
    fun getProc(): Proc {
        return proc
    }

    protected fun process() {
        for (ae in capt) {
            if (ae is Entity) {
                val e = ae as Entity
                if (e.proc.CRITI.type == 2) proc.CRIT.clear()
            }
        }
    }
}
