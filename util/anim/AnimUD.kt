package common.util.anim

import common.CommonStatic
import common.system.VImg
import common.system.fake.FakeImage
import common.system.fake.FakeImage.Marker
import common.system.files.FileData
import common.system.files.VFile
import common.util.anim.AnimUD.DefImgLoader
import common.util.anim.ImgCut
import common.util.anim.MaAnim
import common.util.anim.MaModel

class AnimUD(path: String, name: String, edi: String?, uni: String?) : AnimU<DefImgLoader?>(path + name, DefImgLoader(path, name, edi, uni)) {
    internal class DefImgLoader(path: String, str: String, sedi: String?, suni: String?) : ImageKeeper {
        private val spath: String
        private val fnum: VFile<*>
        private val fedi: VFile<*>?
        private val funi: VFile<*>?
        private var dnum: FileData? = null
        private var num: FakeImage? = null
        private var edi: VImg? = null
        private var uni: VImg? = null
        override fun getEdi(): VImg? {
            if (edi != null) return edi
            return if (fedi == null) null else VImg(fedi).mark(Marker.EDI).also { edi = it }
        }

        override fun getIC(): ImgCut {
            return ImgCut.Companion.newIns("$spath.imgcut")
        }

        override fun getMA(): Array<MaAnim?> {
            val ma: Array<MaAnim?>
            if (VFile.Companion.getFile(spath + "_zombie00.maanim") != null) ma = arrayOfNulls<MaAnim>(7) else if (VFile.Companion.getFile(spath + "_entry.maanim") != null) ma = arrayOfNulls<MaAnim>(5) else ma = arrayOfNulls<MaAnim>(4)
            for (i in 0..3) ma[i] = MaAnim.Companion.newIns(spath + "0" + i + ".maanim")
            if (ma.size == 5) ma[4] = MaAnim.Companion.newIns(spath + "_entry.maanim")
            if (ma.size == 7) for (i in 0..2) ma[i + 4] = MaAnim.Companion.newIns(spath + "_zombie0" + i + ".maanim")
            return ma
        }

        override fun getMM(): MaModel {
            return MaModel.Companion.newIns("$spath.mamodel")
        }

        override fun getNum(): FakeImage? {
            if (num != null) return num
            val fd = if (dnum == null) fnum.getData().also { dnum = it } else dnum!!
            num = fd.img
            return num
        }

        override fun getUni(): VImg? {
            if (uni != null) return uni
            return if (funi == null) CommonStatic.getBCAssets().slot.get(0) else VImg(funi).mark(Marker.UNI).also { uni = it }
        }

        override fun unload() {
            dnum = null
            if (num != null) {
                num.unload()
                num = null
            }
        }

        init {
            spath = path + str
            fnum = VFile.Companion.get("$spath.png")
            fedi = if (sedi == null) null else VFile.Companion.get(path + sedi)
            funi = if (suni == null) null else VFile.Companion.get(path + suni)
        }
    }
}
