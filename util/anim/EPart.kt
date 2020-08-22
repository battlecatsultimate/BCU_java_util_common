package common.util.anim

import common.CommonStatic
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonEncoder
import common.io.json.Test
import common.io.json.Test.JsonTest_0.JsonD
import common.io.json.Test.JsonTest_2
import common.pack.Context.ErrType
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.system.P
import common.system.fake.FakeGraphics
import common.system.fake.FakeImage
import common.system.fake.FakeTransform
import common.util.ImgCore
import common.util.stage.EStage
import common.util.stage.StageMap
import common.util.stage.StageMap.StageMapInfo
import common.util.unit.UnitLevel
import io.BCPlayer
import page.JL
import page.anim.AnimBox
import page.support.ListJtfPolicy
import page.support.SortTable
import page.view.ViewBox
import page.view.ViewBox.Conf
import page.view.ViewBox.Controller
import page.view.ViewBox.VBExporter

class EPart(mm: MaModel, aa: AnimI<*, *>, part: IntArray?, str: String, i: Int, ents: Array<EPart?>) : ImgCore(), Comparable<EPart?> {
    private val name: String
    private val model: MaModel
    private val a: AnimI<*, *>
    private val args: IntArray?
    private val ent: Array<EPart?>
    private val ind: Int
    private var fa: EPart? = null
    private var para: EPart? = null
    private var id = 0
    private var img = 0
    private var gsca = 0
    private var pos: P = P(0, 0)
    private var piv: P = P(0, 0)
    private var sca: P = P(0, 0)
    private var z = 0
    private var angle = 0
    private var opacity = 0
    private var glow = 0
    private var extend = 0
    private var extType // extType - 0 : Slow, 1 : Curse
            = 0
    private var hf = 0
    private var vf = 0
    var ea: EAnimI? = null
    var par // temp
            = 0

    fun alter(m: Int, v: Int) {
        if (m == 0) if (v < ent.size) fa = ent[v.also { par = it }] else fa = ent[0.also { par = it }] else if (m == 1) id = v else if (m == 2) {
            if (extType == 1 && img != v) for (i in ImgCore.Companion.randSeries.indices) {
                var r: Int = ImgCore.Companion.randSeries.get(i)
                r += 1
                r = if (r > 3) 0 else r
                ImgCore.Companion.randSeries.set(i, r)
            }
            img = v
        } else if (m == 3) z = v * ent.size + ind else if (m == 4) pos.x = args!![4] + v.toDouble() else if (m == 5) pos.y = args!![5] + v.toDouble() else if (m == 6) piv.x = args!![6] + v.toDouble() else if (m == 7) piv.y = args!![7] + v.toDouble() else if (m == 8) gsca = v else if (m == 9) sca.x = args!![8] * v / model.ints.get(0).toDouble() else if (m == 10) sca.y = args!![9] * v / model.ints.get(0).toDouble() else if (m == 11) angle = args!![10] + v else if (m == 12) opacity = v * args!![11] / model.ints.get(2) else if (m == 13) hf = if (v == 0) 1 else -1 else if (m == 14) vf = if (v == 0) 1 else -1 else if (m == 50) {
            extend = v
            extType = 0
        } else if (m == 51) {
            extend = v
            extType = 1
        } else CommonStatic.ctx.printErr(ErrType.NEW, "unhandled modification $m")
    }

    override operator fun compareTo(o: EPart): Int {
        return if (z > o.z) 1 else if (z == o.z) 0 else -1
    }

    fun getVal(m: Int): Int {
        if (m == 0) return par else if (m == 1) return id else if (m == 2) return img else if (m == 3) return z else if (m == 4) return pos.x else if (m == 5) return pos.y else if (m == 6) return piv.x else if (m == 7) return piv.y else if (m == 8) return gsca else if (m == 9) return sca.x else if (m == 10) return sca.y else if (m == 11) return angle else if (m == 12) return opacity else if (m == 13) return hf else if (m == 14) return vf
        return -1
    }

    override fun toString(): String {
        return name
    }

    fun drawPart(g: FakeGraphics, base: P?) {
        if (img < 0 || id < 0 || opa() < CommonStatic.getConfig().deadOpa * 0.01 + 1e-5 || a.parts(img) == null) return
        val at: FakeTransform = g.getTransform()
        transform(g, base)
        val bimg: FakeImage = a.parts(img)
        val w: Int = bimg.getWidth()
        val h: Int = bimg.getHeight()
        val p0: P = getSize()
        val tpiv: P = P.Companion.newP(piv).times(p0).times(base)
        val sc: P = P.Companion.newP(w.toDouble(), h.toDouble()).times(p0).times(base)
        P.Companion.delete(p0)
        if (extType == 0) ImgCore.Companion.drawImg(g, bimg, tpiv, sc, opa(), glow == 1, 1.0 * extend / model.ints.get(0)) else if (extType == 1) ImgCore.Companion.drawRandom(g, arrayOf<FakeImage?>(a.parts(3), a.parts(4), a.parts(5), a.parts(6)), tpiv, sc, opa(),
                glow == 1, 1.0 * extend / model.ints.get(0))
        P.Companion.delete(tpiv)
        P.Companion.delete(sc)
        g.setTransform(at)
        g.delete(at)
    }

    fun drawScale(g: FakeGraphics, base: P?) {
        val bimg: FakeImage = a.parts(img) ?: return
        val at: FakeTransform = g.getTransform()
        transform(g, base)
        val w: Int = bimg.getWidth()
        val h: Int = bimg.getHeight()
        val p0: P = getSize()
        val tpiv: P = P.Companion.newP(piv).times(p0).times(base)
        val sc: P = P.Companion.newP(w.toDouble(), h.toDouble()).times(p0).times(base)
        P.Companion.delete(p0)
        ImgCore.Companion.drawSca(g, tpiv, sc)
        P.Companion.delete(tpiv)
        P.Companion.delete(sc)
        g.setTransform(at)
        g.delete(at)
    }

    fun setPara(p: EPart?) {
        if (p == null) {
            fa = para
            para = null
        } else {
            para = fa
            fa = p
        }
    }

    fun setValue() {
        if (args!![0] >= ent.size) args[0] = 0
        fa = if (args[0] <= -1) null else ent[args[0]]
        id = args[1]
        img = args[2]
        z = args[3] * ent.size + ind
        pos = pos.setTo(args[4], args[5])
        piv = piv.setTo(args[6], args[7])
        sca = sca.setTo(args[8], args[9])
        angle = args[10]
        opacity = args[11]
        glow = args[12]
        extend = args[13]
        gsca = model.ints.get(0)
        vf = 1
        hf = vf
    }

    private fun getSize(): P {
        val mi: Double = 1.0 / model.ints.get(0)
        return if (fa == null) P.Companion.newP(sca).times(gsca * mi * mi) else fa!!.getSize().times(sca).times(gsca * mi * mi)
    }

    private fun opa(): Double {
        if (opacity == 0) return 0
        return if (fa != null) fa!!.opa() * opacity / model.ints.get(2) else 1.0 * opacity / model.ints.get(2)
    }

    private fun transform(g: FakeGraphics, sizer: P) {
        var siz: P = sizer
        if (fa != null) {
            fa!!.transform(g, sizer)
            siz = fa!!.getSize().times(sizer)
        }
        val tpos: P = P.Companion.newP(pos).times(siz)
        if (ent[0] !== this) {
            g.translate(tpos.x, tpos.y)
            g.scale(hf, vf)
        } else {
            if (model.confs.size > 0) {
                val data: IntArray = model.confs.get(0)
                val p0: P = getSize()
                val shi: P = P.Companion.newP(data[2], data[3]).times(p0)
                P.Companion.delete(p0)
                val p3: P = shi.times(sizer)
                g.translate(-p3.x, -p3.y)
                P.Companion.delete(shi)
            }
            val p0: P = getSize()
            val p: P = P.Companion.newP(piv).times(p0).times(sizer)
            P.Companion.delete(p0)
            g.translate(p.x, p.y)
            P.Companion.delete(p)
        }
        if (angle != 0) g.rotate(Math.PI * 2 * angle / model.ints.get(1))
        P.Companion.delete(tpos)
        if (fa != null) P.Companion.delete(siz)
    }

    init {
        model = mm
        a = aa
        args = part
        ent = ents
        name = str
        ind = i
        setValue()
    }
}
