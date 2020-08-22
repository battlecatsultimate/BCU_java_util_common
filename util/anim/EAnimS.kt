package common.util.anim

import common.CommonStatic
import common.system.P
import common.system.fake.FakeGraphics
import common.util.ImgCore

class EAnimS(ia: AnimI<*, *>, mm: MaModel) : EAnimI(ia, mm) {
    override fun draw(g: FakeGraphics, ori: P, siz: Double) {
        ImgCore.Companion.set(g)
        g.translate(ori.x, ori.y)
        if (CommonStatic.getConfig().ref && !CommonStatic.getConfig().battle) {
            val p0: P = P(-200, 0).times(siz)
            val p1: P = P(400, 100).times(siz)
            val p2: P = P(0, -300).times(siz)
            g.drawRect(p0.x as Int, p0.y as Int, p1.x as Int, p1.y as Int)
            g.setColor(FakeGraphics.Companion.RED)
            g.drawLine(0, 0, p2.x as Int, p2.y as Int)
        }
        for (e in order) e.drawPart(g, P(siz, siz))
        if (sele >= 0 && sele < ent!!.size) ent!!.get(sele)!!.drawScale(g, P(siz, siz))
    }

    override fun ind(): Int {
        return 0
    }

    override fun len(): Int {
        return 0
    }

    override fun setTime(value: Int) {}
    override fun update(b: Boolean) {}
}
