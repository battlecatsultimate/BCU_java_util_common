package common.util.pack

import common.CommonStatic
import common.CommonStatic.BCAuxAssets
import common.system.VImg
import common.system.fake.FakeImage
import common.util.anim.*
import common.util.pack.NyCastle
import common.util.pack.NyCastle.NyType

class NyCastle private constructor(str: String, t: Int) : AnimI<NyCastle?, NyType?>() {
    enum class NyType : AnimType<NyCastle?, NyType?> {
        BASE, ATK, EXT
    }

    private val id: Int
    private val sprite: VImg
    private val ic: ImgCut
    private val model: MaModel
    private var atkm: MaModel? = null
    private var extm: MaModel? = null
    private val manim: MaAnim
    private var atka: MaAnim? = null
    private var exta: MaAnim? = null
    private var parts: Array<FakeImage>?
    override fun check() {
        if (parts == null) load()
    }

    override fun getEAnim(t: NyType): EAnimD<NyType>? {
        check()
        if (t == NyType.BASE) return EAnimD<NyType>(this, model, manim)
        if (t == NyType.ATK) return EAnimD<NyType>(this, atkm, atka)
        return if (t == NyType.EXT) EAnimD<NyType>(this, extm, exta) else null
    }

    override fun load() {
        parts = ic.cut(sprite.getImg())
    }

    override fun names(): Array<String?> {
        if (atkm == null) return arrayOf("castle")
        return if (extm == null) arrayOf("castle", "atk") else arrayOf("castle", "atk", "ext")
    }

    override fun parts(i: Int): FakeImage? {
        return parts!![i]
    }

    override fun toString(): String {
        return "castle $id"
    }

    override fun types(): Array<NyType> {
        return common.util.pack.NyCastle.NyType.values()
    }

    companion object {
        const val TOT = 8
        fun read() {
            val aux: BCAuxAssets = CommonStatic.getBCAssets()
            val pre = "./org/castle/00"
            val mid = "/nyankoCastle_00"
            val type = intArrayOf(0, 2, 3)
            for (t in 0..2) for (i in 0 until TOT) {
                val str = pre + type[t] + mid + type[t] + "_0" + i
                aux.main.get(t).get(i) = VImg("$str.png")
            }
            for (i in 0 until TOT) {
                val str = pre + 1 + mid + 1 + "_0"
                aux.atks.get(i) = NyCastle(str, i)
            }
        }
    }

    init {
        anim = this
        id = t
        sprite = VImg(str + t + "_00.png")
        ic = ImgCut.Companion.newIns(str + t + "_00.imgcut")
        model = MaModel.Companion.newIns(str + t + "_01.mamodel")
        manim = MaAnim.Companion.newIns(str + t + "_01.maanim")
        if (t != 1 && t != 2 && t != 7) {
            atkm = MaModel.Companion.newIns(str + t + "_00.mamodel")
            atka = MaAnim.Companion.newIns(str + t + "_00.maanim")
        } else {
            atkm = null
            atka = null
        }
        if (t == 6) {
            extm = MaModel.Companion.newIns(str + t + "_02.mamodel")
            exta = MaAnim.Companion.newIns(str + t + "_02.maanim")
        } else {
            extm = null
            exta = null
        }
    }
}
