package common.system

import common.system.fake.FakeGraphics
import common.system.fake.FakeImage

class SymCoord(fg: FakeGraphics, R: Double, X: Double, Y: Double, t: Int) {
    var g: FakeGraphics
    var r: Double
    var x: Double
    var y: Double
    var type: Int
    private val size: P = P(0, 0)
    private val pos: P = P(0, 0)
    fun draw(vararg fis: FakeImage): P {
        setSize(0.0, 0.0)
        for (f in fis) {
            size.x += f.getWidth().toDouble()
            size.y = Math.max(size.y, f.getHeight().toDouble())
        }
        size.times(r)
        setPos(x, y)
        if (type and 1 > 0) pos.x -= size.x
        if (type and 2 > 0) pos.y -= size.y
        for (f in fis) {
            if (r == 1.0) g.drawImage(f, pos.x, pos.y) else g.drawImage(f, pos.x, pos.y, f.getWidth() * r, f.getHeight() * r)
            pos.x += f.getWidth() * r
        }
        return size
    }

    private fun setPos(x: Double, y: Double) {
        pos.x = x
        pos.y = y
    }

    private fun setSize(x: Double, y: Double) {
        size.x = x
        size.y = y
    }

    init {
        g = fg
        r = R
        x = X
        y = Y
        type = t
    }
}
