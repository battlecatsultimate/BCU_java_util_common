package common.system.fake

interface FakeGraphics {
    fun colRect(x: Int, y: Int, w: Int, h: Int, r: Int, g: Int, b: Int, a: Int)
    fun delete(at: FakeTransform?) {}
    fun drawImage(bimg: FakeImage, x: Double, y: Double)
    fun drawImage(bimg: FakeImage, x: Double, y: Double, d: Double, e: Double)
    fun drawLine(i: Int, j: Int, x: Int, y: Int)
    fun drawOval(i: Int, j: Int, k: Int, l: Int)
    fun drawRect(x: Int, y: Int, x2: Int, y2: Int)
    fun fillOval(i: Int, j: Int, k: Int, l: Int)
    fun fillRect(x: Int, y: Int, w: Int, h: Int)
    fun getTransform(): FakeTransform
    fun gradRect(x: Int, y: Int, w: Int, h: Int, a: Int, b: Int, c: IntArray, d: Int, e: Int, f: IntArray)
    fun rotate(d: Double)
    fun scale(hf: Int, vf: Int)
    fun setColor(c: Int)
    fun setComposite(mode: Int, p0: Int, p1: Int)
    fun setRenderingHint(key: Int, `object`: Int)
    fun setTransform(at: FakeTransform)
    fun translate(x: Double, y: Double)

    companion object {
        const val RED = 0
        const val YELLOW = 1
        const val BLACK = 2
        const val MAGENTA = 3
        const val BLUE = 4
        const val CYAN = 5
        const val WHITE = 6
        const val DEF = 0
        const val TRANS = 1
        const val BLEND = 2
        const val GRAY = 3
    }
}
