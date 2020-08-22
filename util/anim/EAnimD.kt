package common.util.anim

import common.system.P
import common.system.fake.FakeGraphics
import common.util.ImgCore
import common.util.anim.AnimI.AnimType

open class EAnimD<T>(ia: AnimI<*, T>, mm: MaModel, anim: MaAnim?) : EAnimI(ia, mm) where T : Enum<T>?, T : AnimType<*, T>? {
    var type: T? = null
    protected var ma: MaAnim?
    protected var f = -1
    fun changeAnim(t: T) {
        f = -1
        ma = (anim() as AnimD<*, T>).getMaAnim(t)
        type = t
    }

    fun done(): Boolean {
        return f > ma!!.max
    }

    override fun draw(g: FakeGraphics, ori: P, siz: Double) {
        if (f == -1) {
            f = 0
            setup()
        }
        ImgCore.Companion.set(g)
        g.translate(ori.x, ori.y)
        for (e in order) {
            val p: P = P.Companion.newP(siz, siz)
            e.drawPart(g, p)
            P.Companion.delete(p)
        }
    }

    override fun ind(): Int {
        return f
    }

    override fun len(): Int {
        return ma!!.max + 1
    }

    override fun setTime(value: Int) {
        setup()
        f = value
        ma!!.update(f, this, true)
    }

    fun setup() {
        ma!!.update(0, this, false)
    }

    override fun update(rotate: Boolean) {
        f++
        ma!!.update(f, this, rotate)
    }

    protected override fun performDeepCopy() {
        super.performDeepCopy()
        (copy as EAnimD<*>).setTime(f)
    }

    init {
        ma = anim
    }
}
