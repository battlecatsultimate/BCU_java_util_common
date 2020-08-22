package common.system

import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.Adminimport
import common.system.P
import common.util.BattleObj
import java.util.*

@Strictfp
open class P(var x: Double, var y: Double) : BattleObj() {
    fun abs(): Double {
        return dis(P(0, 0))
    }

    fun atan2(): Double {
        return Math.atan2(y, x)
    }

    fun atan2(p: P): Double {
        return sf(p).atan2()
    }

    open fun copy(): P {
        return P(x, y)
    }

    fun crossP(p: P): Double {
        return x * p.y - y * p.x
    }

    fun dis(p: P): Double {
        return Math.sqrt(Math.pow(p.x - x, 2.0) + Math.pow(p.y - y, 2.0))
    }

    open fun divide(p: P): P {
        x /= p.x
        y /= p.y
        return this
    }

    fun dotP(p: P): Double {
        return x * p.x + y * p.y
    }

    override fun equals(obj: Any?): Boolean {
        if (P::class.java.isAssignableFrom(obj!!.javaClass)) {
            val i = obj as P?
            if (Math.abs(i!!.x - x) + Math.abs(i.y - y) < 1e-10) return true
        }
        return false
    }

    fun limit(b2: P): Boolean {
        return limit(P(0, 0), b2)
    }

    fun limit(b1: P, b2: P): Boolean {
        val ans = out(b1, b2, 0.0)
        if (x < b1.x) x = b1.x
        if (x > b2.x) x = b2.x
        if (y < b1.y) y = b1.y
        if (y > b2.y) y = b2.y
        return ans
    }

    fun middle(p: P, per: Double): P {
        return copy().plus(sf(p), per)
    }

    fun middleC(p: P, per: Double): P {
        return copy().plus(sf(p), (1 - Math.cos(Math.PI * per)) / 2)
    }

    fun moveOut(v: P, b2: P, r: Double): Boolean {
        return moveOut(v, P(0, 0), b2, r)
    }

    fun moveOut(v: P, b1: P, b2: P, r: Double): Boolean {
        return x + r < b1.x && v.x <= 0 || y + r < b1.y && v.y <= 0 || x - r > b2.x && v.x >= 0 || y - r > b2.y && v.y >= 0
    }

    fun out(rect: IntArray, sca: Double, r: Double): Boolean {
        val p0 = P(rect[0].toDouble(), rect[1].toDouble())
        val p1 = P(rect[2].toDouble(), rect[3].toDouble()).plus(p0)
        p0.times(sca)
        p1.times(sca)
        return out(p0, p1, r)
    }

    fun out(b2: P, r: Double): Boolean {
        return out(P(0, 0), b2, r)
    }

    fun out(b1: P, b2: P, r: Double): Boolean {
        return x + r < b1.x || y + r < b1.y || x - r > b2.x || y - r > b2.y
    }

    fun plus(px: Double, py: Double): P {
        x += px
        y += py
        return this
    }

    operator fun plus(p: P): P {
        x += p.x
        y += p.y
        return this
    }

    fun plus(p: P, n: Double): P {
        x += p.x * n
        y += p.y * n
        return this
    }

    fun positivize(): P {
        if (x < 0) x = -x
        if (y < 0) y = -y
        return this
    }

    fun rotate(t: Double): P {
        return setTo(x * Math.cos(t) - y * Math.sin(t), y * Math.cos(t) + x * Math.sin(t))
    }

    fun setTo(tx: Double, ty: Double): P {
        x = tx
        y = ty
        return this
    }

    /* return this */
    fun setTo(p: P): P {
        x = p.x
        y = p.y
        return this
    }

    open fun sf(p: P): P {
        return substractFrom(p)
    }

    fun substractFrom(p: P): P {
        return P(p.x - x, p.y - y)
    }

    open operator fun times(d: Double): P {
        x *= d
        y *= d
        return this
    }

    open fun times(hf: Double, vf: Double): P {
        x *= hf
        y *= vf
        return this
    }

    open operator fun times(p: P): P {
        x *= p.x
        y *= p.y
        return this
    }

    override fun toString(): String {
        return "$x,$y"
    }

    companion object {
        @StaticPermitted(Admin.StaticPermitted.Type.TEMP)
        var stack: Deque<P> = ArrayDeque<P>()
        fun delete(p: P?) {
            stack.add(p)
        }

        @Synchronized
        fun newP(x: Double, y: Double): P {
            if (!stack.isEmpty()) {
                val p: P = stack.pollFirst() ?: return P(x, y)
                return p.setTo(x, y)
            }
            return P(x, y)
        }

        @Synchronized
        fun newP(p: P): P {
            if (!stack.isEmpty()) {
                val p1: P = stack.pollFirst() ?: return P(p.x, p.y)
                return p1.setTo(p.x, p.y)
            }
            return P(p.x, p.y)
        }

        fun polar(r: Double, t: Double): P {
            return P(r * Math.cos(t), r * Math.sin(t))
        }

        fun reg(cx: Float): Float {
            if (cx < 0) return 0
            return if (cx > 1) 1 else cx
        }
    }
}
