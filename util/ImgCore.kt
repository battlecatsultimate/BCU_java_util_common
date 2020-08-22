package common.util

import common.CommonStatic
import common.io.assets.Admin.StaticPermitted
import common.system.P
import common.system.fake.FakeGraphics
import common.system.fake.FakeImage
import common.util.Dataimport
import java.util.*

open class ImgCore : Data() {
    companion object {
        @StaticPermitted
        val NAME = arrayOf("opacity", "color", "accuracy", "scale")

        @StaticPermitted
        val VAL = arrayOf("fast", "default", "quality")
        protected var randSeries: MutableList<Int> = ArrayList()
        fun set(g: FakeGraphics) {
            if (CommonStatic.getConfig().battle) return
            for (i in 0..3) g.setRenderingHint(i, CommonStatic.getConfig().ints.get(i))
        }

        protected fun drawImg(g: FakeGraphics, bimg: FakeImage, piv: P, sc: P, opa: Double, glow: Boolean,
                              extend: Double) {
            var extend = extend
            if (opa < CommonStatic.getConfig().fullOpa * 0.01 - 1e-5) if (!glow) g.setComposite(FakeGraphics.Companion.TRANS, (opa * 256).toInt(), 0) else g.setComposite(FakeGraphics.Companion.BLEND, (opa * 256).toInt(), 1) else if (glow) g.setComposite(FakeGraphics.Companion.BLEND, 256, 1) else g.setComposite(FakeGraphics.Companion.DEF, 0, 0)
            if (extend == 0.0) drawImage(g, bimg, -piv.x, -piv.y, sc.x, sc.y) else {
                var x: Double = -piv.x
                while (extend > 1) {
                    drawImage(g, bimg, x, -piv.y, sc.x, sc.y)
                    x += sc.x
                    extend--
                }
                val w = (bimg.getWidth() * extend) as Int
                val h: Int = bimg.getHeight()
                if (w > 0) {
                    val par: FakeImage = bimg.getSubimage(0, 0, w, h)
                    drawImage(g, par, x, -piv.y, sc.x * extend, sc.y)
                }
            }
            g.setComposite(FakeGraphics.Companion.DEF, 0, 0)
        }

        protected fun drawRandom(g: FakeGraphics, bimg: Array<FakeImage>, piv: P, sc: P, opa: Double, glow: Boolean,
                                 extend: Double) {
            var extend = extend
            if (opa < CommonStatic.getConfig().fullOpa * 0.01 - 1e-5) if (!glow) g.setComposite(FakeGraphics.Companion.TRANS, (opa * 256).toInt(), 0) else g.setComposite(FakeGraphics.Companion.BLEND, (opa * 256).toInt(), 1) else if (glow) g.setComposite(FakeGraphics.Companion.BLEND, 256, 1) else g.setComposite(FakeGraphics.Companion.DEF, 0, 0)
            if (extend == 0.0) drawImage(g, bimg[0], -piv.x, -piv.y, sc.x, sc.y) else {
                var x: Double = -piv.x
                var i = 0
                while (extend > 1) {
                    var data: Int
                    if (i >= randSeries.size) {
                        data = (Math.random() * 3).toInt()
                        randSeries.add(data)
                    } else {
                        data = randSeries[i]
                    }
                    val ranImage: FakeImage = bimg[data]
                    drawImage(g, ranImage, x, -piv.y, sc.x, sc.y)
                    x += sc.x
                    extend--
                    i++
                }
                val w = (bimg[0].getWidth() * extend) as Int
                val h: Int = bimg[0].getHeight()
                if (w > 0) {
                    val par: FakeImage
                    par = bimg[0].getSubimage(0, 0, w, h)
                    drawImage(g, par, x, -piv.y, sc.x * extend, sc.y)
                }
            }
            g.setComposite(FakeGraphics.Companion.DEF, 0, 0)
        }

        protected fun drawSca(g: FakeGraphics, piv: P, sc: P) {
            g.setColor(FakeGraphics.Companion.RED)
            g.fillOval(-10, -10, 20, 20)
            g.drawOval(-40, -40, 80, 80)
            var x = -piv.x as Int
            var y = -piv.y as Int
            if (sc.x < 0) x += sc.x.toInt()
            if (sc.y < 0) y += sc.y.toInt()
            val sx = Math.abs(sc.x) as Int
            val sy = Math.abs(sc.y) as Int
            g.drawRect(x, y, sx, sy)
            g.setColor(FakeGraphics.Companion.YELLOW)
            g.drawRect(x - 40, y - 40, sx + 80, sy + 80)
        }

        private fun drawImage(g: FakeGraphics, bimg: FakeImage, x: Double, y: Double, w: Double, h: Double) {
            val ix = Math.round(x).toInt()
            val iy = Math.round(y).toInt()
            val iw = Math.round(w).toInt()
            val ih = Math.round(h).toInt()
            g.drawImage(bimg, ix.toDouble(), iy.toDouble(), iw.toDouble(), ih.toDouble())
        }
    }
}
