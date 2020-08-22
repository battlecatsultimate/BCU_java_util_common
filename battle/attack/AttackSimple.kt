package common.battle.attack

import common.battle.entity.AbEntity
import common.util.Data
import common.util.Data.Proc.MoveWave
import common.util.Data.Proc.Volc
import java.util.*

open class AttackSimple(ent: AtkModelAb, ATK: Int, t: Int, eab: Int, pro: Proc, p0: Double, p1: Double, private val range: Boolean,
                        matk: MaskAtk?) : AttackAb(ent, ATK, t, eab, pro, p0, p1, matk) {
    constructor(ent: AtkModelAb, ATK: Int, t: Int, eab: Int, proc: Proc, p0: Double, p1: Double, mask: MaskAtk) : this(ent, ATK, t, eab, proc, p0, p1, mask.isRange(), mask) {
        touch = mask.getTarget()
        dire *= mask.getDire()
    }

    override fun capture() {
        val pos: Double = model.getPos()
        val le: MutableList<AbEntity> = model.b.inRange(touch, dire, sta, end)
        capt.clear()
        if (canon > -2) le.remove(model.b.ebase)
        if (abi and Data.Companion.AB_ONLY == 0) capt.addAll(le) else for (e in le) if (e.targetable(type)) capt.add(e)
        if (!range) {
            if (capt.size == 0) return
            val ents: MutableList<AbEntity> = ArrayList<AbEntity>()
            ents.add(capt.get(0))
            var dis: Double = Math.abs(pos - ents[0].pos)
            for (e in capt) if (Math.abs(pos - e.pos) < dis - 0.1) {
                ents.clear()
                ents.add(e)
                dis = Math.abs(pos - e.pos)
            } else if (Math.abs(pos - e.pos) < dis + 0.1) ents.add(e)
            capt.clear()
            val r = (model.b.r.nextDouble() * ents.size) as Int
            capt.add(ents[r])
        }
    }

    override fun excuse() {
        process()
        val layer: Int = model.getLayer()
        if (proc.MOVEWAVE.exists()) {
            val mw: MoveWave = proc.MOVEWAVE
            val dire: Int = model.getDire()
            val p0: Double = model.getPos() + dire * mw.dis
            ContMove(this, p0, mw.width, mw.speed, 1, mw.time, mw.itv, layer)
            return
        }
        for (e in capt) e.damaged(this)
        if (capt.size > 0 && proc.WAVE.exists()) {
            val dire: Int = model.getDire()
            val wid: Int = if (dire == 1) Data.Companion.W_E_WID else Data.Companion.W_U_WID
            val addp: Int = (if (dire == 1) Data.Companion.W_E_INI else Data.Companion.W_U_INI) + wid / 2
            val p0: Double = model.getPos() + dire * addp
            // generate a wave when hits somebody
            ContWaveDef(AttackWave(this, p0, wid.toDouble(), Data.Companion.WT_WAVE), p0, layer)
        }
        if (capt.size > 0 && proc.VOLC.exists()) {
            val dire: Int = model.getDire()
            val volc: Volc = proc.VOLC
            val addp: Int = volc.dis_0 + (model.b.r.nextDouble() * (volc.dis_1 - volc.dis_0)) as Int
            val p0: Double = model.getPos() + dire * addp
            val sta: Double = p0 + if (dire == 1) Data.Companion.W_VOLC_PIERCE else Data.Companion.W_VOLC_INNER
            val end: Double = p0 - if (dire == 1) Data.Companion.W_VOLC_INNER else Data.Companion.W_VOLC_PIERCE
            ContVolcano(AttackVolcano(this, sta, end), p0, layer, volc.time)
        }
    }
}
