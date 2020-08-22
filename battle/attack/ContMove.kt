package common.battle.attack

import common.CommonStatic
import common.CommonStatic.BattleConst
import common.system.P
import common.system.fake.FakeGraphics
import common.util.Data

class ContMove(`as`: AttackSimple, p: Double, vararg conf: Int) : ContAb(`as`.model.b, p, conf[5]) {
    private val itv: Int
    private val move: Int
    private val ran: Int
    private val rep: Int
    private var t: Int
    private var rem: Int
    private var rept: Int
    private val atk: AttackWave
    private var tempAtk = false
    override fun draw(gra: FakeGraphics, p: P, siz: Double) {
        var siz = siz
        if (!CommonStatic.getConfig().ref) return

        // after this is the drawing of hit boxes
        siz *= 1.25
        val rat: Double = BattleConst.Companion.ratio
        val h = (640 * rat * siz).toInt()
        gra.setColor(FakeGraphics.Companion.MAGENTA)
        val d0 = -ran / 2.toDouble()
        val ra = ran.toDouble()
        val x = (d0 * rat * siz + p.x) as Int
        val y = p.y as Int
        val w = (ra * rat * siz).toInt()
        if (tempAtk) gra.fillRect(x, y, w, h) else gra.drawRect(x, y, w, h)
    }

    override fun update() {
        tempAtk = false
        t--
        if (rept > 0) rept--
        if (t == 0) {
            rem--
            if (rem > 0) t = itv else activate = false
            pos += move * atk.model.getDire().toDouble()
            if (rept == 0) {
                atk.incl!!.clear()
                rept = rep
            }
            sb.getAttack(AttackWave(atk, pos, ran.toDouble()))
            tempAtk = true
        }
    }

    /** conf: range, move, itrv, tot, rept,layer  */
    init {
        move = conf[1]
        itv = conf[2]
        t = itv
        rem = conf[3]
        ran = conf[0]
        rep = conf[4]
        rept = if (rep > 0) rep else -1
        atk = AttackWave(`as`, 0, 0, Data.Companion.WT_MOVE)
    }
}
