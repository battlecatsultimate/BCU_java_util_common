package common.util.pack

import common.CommonStatic
import common.CommonStatic.BCAuxAssets
import common.pack.IndexContainer.IndexCont
import common.pack.IndexContainer.Indexable
import common.pack.PackData
import common.pack.UserProfile
import common.system.P
import common.system.VImg
import common.system.fake.FakeGraphics
import common.system.fake.FakeImage
import common.system.fake.FakeImage.Marker
import common.system.files.VFile
import common.util.anim.*
import common.util.pack.Background
import common.util.pack.Background.BGWvType
import common.util.pack.WaveAnim.WaveType
import java.util.*

@IndexCont(PackData::class)
class Background : AnimI<Background?, BGWvType?>, Indexable<PackData?, Background?> {
    enum class BGWvType : AnimType<Background?, BGWvType?> {
        ENEMY, UNIT
    }

    val id: PackData.Identifier<Background>
    val img: VImg
    val cs = Array(4) { IntArray(3) }
    private val uwav: WaveAnim
    private val ewav: WaveAnim
    var ic: Int
    var top: Boolean
    var parts: Array<FakeImage>? = null

    constructor(id: PackData.Identifier<Background>, vimg: VImg) {
        this.id = id
        img = vimg
        ic = 1
        top = true
        val aux: BCAuxAssets = CommonStatic.getBCAssets()
        uwav = aux.defu
        ewav = aux.defe
    }

    private constructor(vimg: VImg, ints: IntArray) {
        val id: Int = UserProfile.Companion.getBCData().bgs.size()
        this.id = PackData.Identifier.Companion.parseInt<Background>(id, Background::class.java)
        img = vimg
        top = ints[14] == 1 || ints[13] == 8
        ic = if (ints[13] == 8) 1 else ints[13]
        for (i in 0..3) cs[i] = intArrayOf(ints[i * 3 + 1], ints[i * 3 + 2], ints[i * 3 + 3])
        UserProfile.Companion.getBCData().bgs.add(this)
        val aux: BCAuxAssets = CommonStatic.getBCAssets()
        if (id <= 107) {
            uwav = WaveAnim(this, aux.uwavm, aux.uwava)
            ewav = WaveAnim(this, aux.ewavm, aux.ewava)
        } else {
            uwav = aux.defu
            ewav = aux.defe
        }
        if (id == 0) {
            aux.defu = uwav
            aux.defe = ewav
        }
    }

    override fun check() {
        if (parts != null) return
        load()
    }

    fun copy(id: PackData.Identifier<Background?>?): Background {
        val bg = Background(id, VImg(img.getImg()))
        for (i in 0..3) bg.cs[i] = cs[i]
        bg.top = top
        bg.ic = ic
        return bg
    }

    fun draw(g: FakeGraphics, rect: P, pos: Int, h: Int, siz: Double) {
        check()
        val off = (pos - Background.Companion.shift * siz) as Int
        val fw = (768 * siz).toInt()
        val fh = (510 * siz).toInt()
        g.gradRect(0, h, rect.x as Int, rect.y as Int - h, 0, h, cs[2], 0, h + fh, cs[3])
        if (h > fh) {
            val y = h - fh * 2
            if (top && parts!!.size > Background.Companion.TOP) {
                var x = off
                while (x < rect.x) {
                    if (x + fw > 0) g.drawImage(parts!![Background.Companion.TOP], x.toDouble(), y.toDouble(), fw.toDouble(), fh.toDouble())
                    x += fw
                }
            } else {
                g.gradRect(0, 0, rect.x as Int, fh + y, 0, y, cs[0], 0, y + fh, cs[1])
            }
        }
        var x = off
        while (x < rect.x) {
            if (x + fw > 0) g.drawImage(parts!![Background.Companion.BG], x.toDouble(), h - fh.toDouble(), fw.toDouble(), fh.toDouble())
            x += fw
        }
    }

    override fun getEAnim(t: BGWvType): EAnimD<WaveType>? {
        return if (t == BGWvType.ENEMY) ewav.getEAnim(WaveType.DEF) else if (t == BGWvType.UNIT) uwav.getEAnim(WaveType.DEF) else null
    }

    override fun getID(): PackData.Identifier<Background>? {
        return id
    }

    override fun load() {
        img.mark(Marker.BG)
        val aux: BCAuxAssets = CommonStatic.getBCAssets()
        parts = aux.iclist.get(ic).cut(img.getImg())
    }

    override fun names(): Array<String?> {
        return arrayOf(toString(), "enemy wave", "unit wave")
    }

    override fun parts(i: Int): FakeImage? {
        return parts!![i]
    }

    override fun toString(): String {
        return id.toString()
    }

    override fun types(): Array<BGWvType> {
        return Background.BGWvType.values()
    }

    companion object {
        const val BG = 0
        const val TOP = 20
        const val shift = 65 // in pix
        fun read() {
            val aux: BCAuxAssets = CommonStatic.getBCAssets()
            val path = "./org/battle/bg/"
            for (vf in VFile.Companion.get("./org/battle/bg").list()) {
                val name: String = vf.getName()
                if (name.length != 11 || !name.endsWith(".imgcut")) continue
                aux.iclist.add(ImgCut.Companion.newIns(path + name))
            }
            aux.uwavm = MaModel.Companion.newIns("./org/battle/bg/bg_01.mamodel")
            aux.ewavm = MaModel.Companion.newIns("./org/battle/bg/bg_02.mamodel")
            aux.uwava = MaAnim.Companion.newIns("./org/battle/bg/bg_01.maanim")
            aux.ewava = MaAnim.Companion.newIns("./org/battle/bg/bg_02.maanim")
            val qs: Queue<String> = VFile.Companion.readLine("./org/battle/bg/bg.csv")
            qs.poll()
            for (vf in VFile.Companion.get("./org/img/bg/").list()) if (vf.getName().length == 9) {
                val ints = IntArray(15)
                try {
                    val strs = qs.poll().split(",").toTypedArray()
                    for (i in 0..14) ints[i] = strs[i].toInt()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Background(VImg(vf), ints)
            }
        }
    }
}
