package common.util.anim

import common.CommonStatic
import common.CommonStatic.EditLink
import common.battle.data.DataEntity
import common.io.InStream
import common.io.OutStream
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonEncoder
import common.io.json.Test
import common.io.json.Test.JsonTest_0.JsonD
import common.io.json.Test.JsonTest_2
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.pack.UserProfile
import common.system.VImg
import common.system.fake.FakeImage
import common.system.files.VFile
import common.util.anim.AnimCE
import common.util.anim.ImgCut
import common.util.anim.MaAnim
import common.util.anim.MaModel
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
import java.util.*
import java.util.function.BiConsumer

class AnimCE : AnimCI {
    private class AnimCELoader(private val name: String) : AnimLoader {
        private val pre: String
        override fun getEdi(): VImg? {
            return optional(pre + "edi.png")
        }

        override fun getIC(): ImgCut {
            return ImgCut.Companion.newIns("$pre$name.imgcut")
        }

        override fun getMA(): Array<MaAnim?> {
            val anims: Array<MaAnim?> = arrayOfNulls<MaAnim>(7)
            for (i in 0..3) anims[i] = MaAnim.Companion.newIns(pre + name + "0" + i + ".maanim")
            for (i in 0..2) anims[i + 4] = MaAnim.Companion.newIns(pre + name + "_zombie0" + i + ".maanim")
            return anims
        }

        override fun getMM(): MaModel {
            return MaModel.Companion.newIns("$pre$name.mamodel")
        }

        override fun getName(): ResourceLocation {
            return ResourceLocation("_local", name)
        }

        override fun getNum(): FakeImage? {
            return VFile.Companion.getFile("$pre$name.png").getData().getImg()
        }

        override fun getStatus(): Int {
            return 0
        }

        override fun getUni(): VImg? {
            return optional(pre + "uni.png")
        }

        companion object {
            private fun optional(str: String): VImg? {
                val fv: VFile<*> = VFile.Companion.getFile(str) ?: return null
                return VImg(fv)
            }
        }

        init {
            pre = "./res/anim/$name/"
        }
    }

    private inner class History(val name: String, os: OutStream) {
        val data: OutStream
        var mms: OutStream? = null

        init {
            data = os
        }
    }

    private var saved = false
    var link: EditLink? = null
    var history = Stack<common.util.anim.AnimCE.History>()

    constructor(resourceLocation: ResourceLocation) : super(SourceAnimLoader(resourceLocation, null)) {
        id = resourceLocation
        map()[id.id] = this
        history("initial")
    }

    /** for conversion only  */
    @Deprecated("")
    constructor(al: AnimLoader) : super(al) {
    }

    constructor(st: String) : super(AnimCELoader(st)) {
        id = ResourceLocation("_local", st)
        map()[id.id] = this
    }

    constructor(str: String?, ori: AnimD<*, *>) : this(str) {
        loaded = true
        partial = true
        imgcut = ori.imgcut!!.clone()
        mamodel = ori.mamodel!!.clone()
        if (mamodel.confs.size < 1) mamodel.confs = Array<IntArray?>(2) { IntArray(6) }
        anims = arrayOfNulls<MaAnim>(7)
        for (i in 0..6) if (i < ori.anims.size) anims.get(i) = ori.anims.get(i).clone() else anims.get(i) = MaAnim()
        loader.setNum(ori.getNum())
        parts = imgcut.cut(ori.getNum())
        if (ori is AnimU<*>) {
            val au: AnimU<*> = ori
            setEdi(au.getEdi())
            setUni(au.getUni())
        }
        standardize()
        save()
        history("initial")
    }

    fun createNew() {
        loaded = true
        partial = true
        imgcut = ImgCut()
        mamodel = MaModel()
        anims = arrayOfNulls<MaAnim>(7)
        for (i in 0..6) anims.get(i) = MaAnim()
        parts = imgcut.cut(getNum())
        history("initial")
    }

    fun deletable(): Boolean {
        for (p in UserProfile.Companion.getUserPacks()) {
            for (e in p.enemies.getList()) if (e.anim === this) return false
            for (u in p.units.getList()) for (f in u.forms) if (f.anim === this) return false
        }
        return true
    }

    fun delete() {
        map().remove(id.id)
        SourceAnimSaver(id, this).delete()
    }

    fun getUndo(): String {
        return history.peek().name
    }

    fun hardSave(str: String?) {
        if (id == null) {
            id = Workspace.Companion.validate(ResourceLocation("_local", str))
            map()[id.id] = this
        }
        saved = false
        save()
    }

    fun ICedited() {
        check()
        parts = imgcut!!.cut(getNum())
    }

    fun inPool(): Boolean {
        return id.pack != null && id.pack == "_local"
    }

    fun isSaved(): Boolean {
        return saved
    }

    override fun load() {
        try {
            super.load()
            history("initial")
        } catch (e: Exception) {
            e.printStackTrace()
            CommonStatic.def.exit(false)
        }
        validate()
    }

    fun localize(pack: String) {
        if (id.pack == ResourceLocation.Companion.LOCAL) map().remove(id.id)
        val saver = SourceAnimSaver(id, this)
        saver.delete()
        id.pack = pack
        if (id.pack == ResourceLocation.Companion.LOCAL) map()[str] = this
        saver.saveAll()
    }

    fun merge(a: AnimCE, x: Int, y: Int) {
        val ic0: ImgCut = imgcut!!
        val ic1: ImgCut = a.imgcut!!
        val icn: Int = ic0.n
        ic0.n += ic1.n
        ic0.cuts = Arrays.copyOf<IntArray>(ic0.cuts, ic0.n)
        for (i in 0 until icn) ic0.cuts.get(i) = ic0.cuts.get(i)!!.clone()
        ic0.strs = Arrays.copyOf<String>(ic0.strs, ic0.n)
        for (i in 0 until ic1.n) {
            ic0.cuts.get(i + icn) = ic1.cuts.get(i)!!.clone()
            val data: IntArray = ic0.cuts.get(i + icn)
            data[0] += x
            data[1] += y
            ic0.strs.get(i + icn) = ic1.strs.get(i)
        }
        val mm0: MaModel = mamodel!!
        val mm1: MaModel = a.mamodel!!
        val mmn: Int = mm0.n
        mm0.n += mm1.n
        mm0.parts = Arrays.copyOf<IntArray>(mm0.parts, mm0.n)
        for (i in 0 until mmn) mm0.parts.get(i) = mm0.parts.get(i)!!.clone()
        mm0.strs0 = Arrays.copyOf<String>(mm0.strs0, mm0.n)
        val fir: IntArray = mm0.parts.get(0)
        for (i in 0 until mm1.n) {
            mm0.parts.get(i + mmn) = mm1.parts.get(i)!!.clone()
            val data: IntArray = mm0.parts.get(i + mmn)
            if (data[0] != -1) data[0] += mmn else {
                data[0] = 0
                data[8] = data[8] * 1000 / fir[8]
                data[9] = data[9] * 1000 / fir[9]
                data[4] = data[6] * data[8] / 1000 - fir[6]
                data[5] = data[7] * data[9] / 1000 - fir[7]
            }
            data[2] += icn
            mm0.strs0.get(i + mmn) = mm1.strs0.get(i)
        }
        for (i in 0..6) {
            val ma0: MaAnim = anims.get(i)
            val ma1: MaAnim = a.anims.get(i)
            val man: Int = ma0.n
            ma0.n += ma1.n
            ma0.parts = Arrays.copyOf(ma0.parts, ma0.n)
            for (j in 0 until man) ma0.parts.get(j) = ma0.parts.get(j)!!.clone()
            for (j in 0 until ma1.n) {
                ma0.parts.get(j + man) = ma1.parts.get(j)!!.clone()
                val p: Part = ma0.parts.get(j + man)
                p.ints[0] += mmn
                if (p.ints[1] == 2) for (data in p.moves) data[1] += icn
            }
        }
    }

    fun reloImg() {
        setNum(loader.loader.getNum())
        ICedited()
    }

    fun renameTo(str: String) {
        if (id.pack == ResourceLocation.Companion.LOCAL) map().remove(id.id)
        val saver = SourceAnimSaver(id, this)
        saver.delete()
        id.id = str
        if (id.pack == ResourceLocation.Companion.LOCAL) map()[str] = this
        saver.saveAll()
        unSave("rename (not applicapable for undo)")
    }

    fun resize(d: Double) {
        for (l in imgcut!!.cuts) for (i in l!!.indices) (l!![i] *= d).toInt()
        (mamodel!!.parts.get(0)!!.get(8) /= d).toInt()
        (mamodel!!.parts.get(0)!!.get(9) /= d).toInt()
        for (l in mamodel!!.parts) {
            (l!![4] *= d).toInt()
            (l!![5] *= d).toInt()
            (l!![6] *= d).toInt()
            (l!![7] *= d).toInt()
        }
        for (ma in anims) for (p in ma.parts) if (p!!.ints[1] >= 4 && p!!.ints[1] <= 7) for (x in p!!.moves) (x[1] *= d).toInt()
        unSave("resize")
    }

    fun restore() {
        history.pop()
        var `is`: InStream = history.peek().data.translate()
        imgcut!!.restore(`is`)
        ICedited()
        mamodel!!.restore(`is`)
        var n: Int = `is`.nextInt()
        anims = arrayOfNulls<MaAnim>(n)
        for (i in 0 until n) {
            anims.get(i) = MaAnim()
            anims.get(i).restore(`is`)
        }
        `is` = history.peek().mms.translate()
        n = `is`.nextInt()
        for (i in 0 until n) {
            val ind: Int = `is`.nextInt()
            val `val`: Int = `is`.nextInt()
            if (ind >= 0 && ind < mamodel!!.n) mamodel!!.status.put(mamodel!!.parts.get(ind), `val`)
        }
        saved = false
    }

    override fun revert() {
        super.revert()
        unSave("revert")
    }

    fun save() {
        if (!loaded || isSaved()) return
        saved = true
        SourceAnimSaver(id, this).saveAll()
    }

    fun saveIcon() {
        SourceAnimSaver(id, this).saveIconDisplay()
    }

    fun saveImg() {
        SourceAnimSaver(id, this).saveSprite()
    }

    fun saveUni() {
        SourceAnimSaver(id, this).saveIconDeploy()
    }

    fun setEdi(uni: VImg?) {
        loader.setEdi(uni)
    }

    fun setNum(fimg: FakeImage?) {
        loader.setNum(fimg)
        if (loaded) ICedited()
    }

    fun setUni(uni: VImg?) {
        loader.setUni(uni)
    }

    fun unSave(str: String) {
        saved = false
        history(str)
        if (link != null) link.review()
    }

    fun updateStatus() {
        partial()
        val mms: OutStream = OutStream.Companion.getIns()
        mms.writeInt(mamodel!!.status.size)
        mamodel!!.status.forEach(BiConsumer<IntArray, Int> { d: IntArray, s: Int? ->
            var ind = -1
            for (i in 0 until mamodel!!.n) if (mamodel!!.parts.get(i) == d) ind = i
            mms.writeInt(ind)
            mms.writeInt(s)
        })
        mms.terminate()
        history.peek().mms = mms
    }

    protected override fun partial() {
        super.partial()
        standardize()
    }

    private fun history(str: String) {
        partial()
        val os: OutStream = OutStream.Companion.getIns()
        imgcut.write(os)
        mamodel.write(os)
        os.writeInt(anims.size)
        for (ma in anims) ma.write(os)
        os.terminate()
        val h: common.util.anim.AnimCE.History = common.util.anim.AnimCE.History(str, os)
        history.push(h)
        updateStatus()
    }

    private fun standardize() {
        if (mamodel!!.parts.size == 0 || mamodel!!.confs.size == 0) return
        val dat: IntArray = mamodel!!.parts.get(0)
        val con: IntArray = mamodel!!.confs.get(0)
        dat[6] -= con[2]
        dat[7] -= con[3]
        con[3] = 0
        con[2] = con[3]
        val std: IntArray = mamodel!!.ints
        for (data in mamodel!!.parts) {
            data!![8] = data!![8] * 1000 / std[0]
            data!![9] = data!![9] * 1000 / std[0]
            data!![10] = data!![10] * 3600 / std[1]
            data!![11] = data!![11] * 1000 / std[2]
        }
        for (ma in anims) for (p in ma.parts) {
            if (p!!.ints[1] >= 8 && p!!.ints[1] <= 10) for (data in p!!.moves) data[1] = data[1] * 1000 / std[0]
            if (p!!.ints[1] == 11) for (data in p!!.moves) data[1] = data[1] * 3600 / std[1]
            if (p!!.ints[1] == 12) for (data in p!!.moves) data[1] = data[1] * 1000 / std[2]
        }
        std[0] = 1000
        std[1] = 3600
        std[2] = 1000
    }

    companion object {
        private const val REG_LOCAL_ANIM = "local_animation"
        fun getAvailable(string: String): String {
            // FIXME Auto-generated method stub
            return string
        }

        fun map(): MutableMap<String, AnimCE> {
            return UserProfile.Companion.getRegister<AnimCE>(REG_LOCAL_ANIM, AnimCE::class.java)
        }
    }
}
