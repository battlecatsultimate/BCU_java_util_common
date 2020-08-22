package common.util.pack

import common.pack.IndexContainer.IndexCont
import common.pack.IndexContainer.Indexable
import common.pack.PackData
import common.system.VImg
import common.system.fake.FakeImage
import common.util.anim.AnimD
import common.util.anim.ImgCut
import common.util.anim.MaAnim
import common.util.anim.MaModel
import common.util.pack.Soul.SoulType

@IndexCont(PackData::class)
class Soul(st: String?, i: Int) : AnimD<Soul?, SoulType?>(st), Indexable<PackData?, Soul?> {
    enum class SoulType : AnimType<Soul?, SoulType?> {
        DEF
    }

    private val id: PackData.Identifier<Soul>
    private val img: VImg
    override fun getID(): PackData.Identifier<Soul>? {
        return id
    }

    override fun getNum(): FakeImage? {
        return img.getImg()
    }

    override fun load() {
        loaded = true
        imgcut = ImgCut.Companion.newIns(str + ".imgcut")
        mamodel = MaModel.Companion.newIns(str + ".mamodel")
        anims = arrayOf<MaAnim>(MaAnim.Companion.newIns(str + ".maanim"))
        types = common.util.pack.Soul.SoulType.values()
        parts = imgcut.cut(img.getImg())
    }

    override fun toString(): String {
        return "soul_" + id.id
    }

    init {
        img = VImg(str + ".png")
        id = PackData.Identifier.Companion.parseInt<Soul>(i, Soul::class.java)
    }
}
