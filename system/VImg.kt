package common.system

import common.pack.Context.SupExc
import common.system.fake.FakeImage
import common.system.fake.FakeImage.Marker
import common.system.files.FileData
import common.system.files.VFile
import common.util.Data
import common.util.ImgCore
import common.util.anim.ImgCut

class VImg(o: Any?) : ImgCore() {
    private var file: VFile<out FileData>? = null
    var name = ""
    var bimg: FakeImage? = null
    private var loaded = false
    private var ic: ImgCut? = null
    private var marker: Marker? = null
    @Synchronized
    fun check() {
        if (!loaded) load()
    }

    fun getImg(): FakeImage? {
        check()
        return bimg
    }

    fun mark(string: Marker?): VImg {
        marker = string
        if (bimg != null) bimg.mark(string)
        return this
    }

    fun setCut(cut: ImgCut?) {
        ic = cut
    }

    fun setImg(img: Any?) {
        bimg = Data.Companion.err<FakeImage>(SupExc<FakeImage> { FakeImage.Companion.read(img) })
        if (ic != null) bimg = ic.cut(bimg).get(0)
        loaded = true
    }

    override fun toString(): String {
        return if (file == null) if (name.length == 0) "img" else name else file.getName()
    }

    fun unload() {
        // FIXME Auto-generated method stub
    }

    private fun load() {
        loaded = true
        if (file == null) return
        bimg = file.getData().getImg()
        if (bimg == null) return
        if (marker != null) bimg.mark(marker)
        if (ic != null) bimg = ic.cut(bimg).get(0)
        try {
            bimg.getWidth()
        } catch (e: Exception) {
            bimg = null
        }
    }

    init {
        file = if (o is String) VFile.Companion.getFile(o as String?) else if (o is VFile<*>) o as VFile<*>? else null
        if (file == null) bimg = Data.Companion.err<FakeImage>(SupExc<FakeImage> { FakeImage.Companion.read(o) })
        loaded = bimg != null
    }
}
