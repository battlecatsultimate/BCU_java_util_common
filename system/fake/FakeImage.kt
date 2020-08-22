package common.system.fake

import common.system.fake.ImageBuilder
import java.io.IOException

interface FakeImage {
    enum class Marker {
        BG, EDI, UNI, RECOLOR, RECOLORED
    }

    fun bimg(): Any?
    fun getHeight(): Int
    fun getRGB(i: Int, j: Int): Int
    fun getSubimage(i: Int, j: Int, k: Int, l: Int): FakeImage?
    fun getWidth(): Int
    fun gl(): Any?
    fun isValid(): Boolean
    fun mark(m: Marker) {}
    fun setRGB(i: Int, j: Int, p: Int)
    fun unload()

    companion object {
        @Throws(IOException::class)
        fun read(o: Any?): FakeImage? {
            return ImageBuilder.Companion.builder!!.build(o)
        }

        @Throws(IOException::class)
        fun write(img: FakeImage, str: String, o: Any): Boolean {
            return ImageBuilder.Companion.builder!!.write(img, str, o)
        }
    }
}
