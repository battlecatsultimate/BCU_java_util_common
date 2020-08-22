package common.pack

import common.CommonStatic
import common.io.PackLoader
import common.io.PackLoader.PatchFile
import common.io.PackLoader.ZipDesc
import common.io.PackLoader.ZipDesc.FileDesc
import common.io.assets.Admin.StaticPermitted
import common.pack.Context.ErrType
import common.pack.Context.SupExc
import common.pack.Sourceimport
import common.system.VImg
import common.system.fake.FakeImage
import common.system.fake.ImageBuilder
import common.system.files.FDFile
import common.system.files.FileData
import common.util.Data
import common.util.anim.AnimCE
import common.util.anim.AnimCI
import common.util.anim.ImgCut
import common.util.anim.MaAnim
import common.util.anim.MaModel
import common.util.pack.Background
import common.util.stage.CastleImg
import java.io.*
import java.util.*
import java.util.function.Consumer

com.google.api.client.json.jackson2.JacksonFactory
import kotlin.Throws
import java.io.IOException
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.PrintStream

abstract class Source(val id: String) {
    interface AnimLoader {
        fun getEdi(): VImg?
        fun getIC(): ImgCut
        fun getMA(): Array<MaAnim?>
        fun getMM(): MaModel
        fun getName(): ResourceLocation
        fun getNum(): FakeImage?
        fun getStatus(): Int
        fun getUni(): VImg?
    }

    class ResourceLocation(var pack: String, var id: String) {
        companion object {
            const val LOCAL = "_local"
        }
    }

    @StaticPermitted
    class SourceAnimLoader(private val id: ResourceLocation, loader: Source.SourceAnimLoader.SourceLoader?) : AnimLoader {
        interface SourceLoader {
            fun loadFile(id: ResourceLocation?, str: String?): FileData?
        }

        private val loader: Source.SourceAnimLoader.SourceLoader
        override fun getEdi(): VImg? {
            val edi = loader.loadFile(id, EDI) ?: return null
            return CommonStatic.ctx.noticeErr<VImg>(SupExc<VImg> { VImg(FakeImage.Companion.read(edi)) }, ErrType.ERROR,
                    "failed to read Display Icon$id")
        }

        override fun getIC(): ImgCut {
            return ImgCut.Companion.newIns(loader.loadFile(id, IC))
        }

        override fun getMA(): Array<MaAnim?> {
            val ans: Array<MaAnim?> = arrayOfNulls<MaAnim>(MA.size)
            for (i in MA.indices) ans[i] = MaAnim.Companion.newIns(loader.loadFile(id, MA[i]))
            return ans
        }

        override fun getMM(): MaModel {
            return MaModel.Companion.newIns(loader.loadFile(id, MM))
        }

        override fun getName(): ResourceLocation {
            return id
        }

        override fun getNum(): FakeImage? {
            return CommonStatic.ctx.noticeErr<FakeImage>(SupExc<FakeImage> { FakeImage.Companion.read(loader.loadFile(id, SP)) }, ErrType.ERROR,
                    "failed to read sprite sheet $id")
        }

        override fun getStatus(): Int {
            return if (id.pack == "_local") 0 else 1
        }

        override fun getUni(): VImg? {
            val uni = loader.loadFile(id, UNI) ?: return null
            return CommonStatic.ctx.noticeErr<VImg>(SupExc<VImg> { VImg(FakeImage.Companion.read(uni)) }, ErrType.ERROR,
                    "failed to read deploy icon $id")
        }

        companion object {
            const val IC = "imgcut.txt"
            const val MM = "mamodel.txt"
            val MA = arrayOf("maanim_walk.txt", "maanim_idle.txt", "maanim_attack.txt", "maanim_kb.txt",
                    "maanim_burrow_down.txt", "maanim_burrow_move.txt", "maanim_burrow_up.txt")
            const val SP = "sprite.png"
            const val EDI = "icon_display.png"
            const val UNI = "icon_deploy.png"
        }

        init {
            this.loader = loader
                    ?: Source.SourceAnimLoader.SourceLoader { id: ResourceLocation, str: String -> Workspace.loadAnimFile(id, str) }
        }
    }

    class SourceAnimSaver(private val id: ResourceLocation, animCI: AnimCI) {
        private val anim: AnimCI
        fun delete() {
            CommonStatic.ctx.noticeErr(
                    Context.RunExc { Context.Companion.delete(CommonStatic.ctx.getWorkspaceFile("./" + id.pack + "/animations/" + id.id)) },
                    ErrType.ERROR, "failed to delete animation: $id")
        }

        fun saveAll() {
            saveData()
            saveImgs()
        }

        fun saveData() {
            try {
                write("imgcut.txt", Consumer<PrintStream> { ps: PrintStream? -> anim.imgcut.write(ps) })
                write("mamodel.txt", Consumer<PrintStream> { ps: PrintStream? -> anim.mamodel.write(ps) })
                write("maanim_walk.txt", Consumer<PrintStream> { ps: PrintStream? -> anim.anims.get(0).write(ps) })
                write("maanim_idle.txt", Consumer<PrintStream> { ps: PrintStream? -> anim.anims.get(1).write(ps) })
                write("maanim_attack.txt", Consumer<PrintStream> { ps: PrintStream? -> anim.anims.get(2).write(ps) })
                write("maanim_kb.txt", Consumer<PrintStream> { ps: PrintStream? -> anim.anims.get(3).write(ps) })
                write("maanim_burrow_down.txt", Consumer<PrintStream> { ps: PrintStream? -> anim.anims.get(4).write(ps) })
                write("maanim_burrow_move.txt", Consumer<PrintStream> { ps: PrintStream? -> anim.anims.get(5).write(ps) })
                write("maanim_burrow_up.txt", Consumer<PrintStream> { ps: PrintStream? -> anim.anims.get(6).write(ps) })
            } catch (e: IOException) {
                CommonStatic.ctx.noticeErr(e, ErrType.ERROR, "Error during saving animation data: $anim")
            }
        }

        fun saveIconDeploy() {
            if (anim.getUni() != null) CommonStatic.ctx.noticeErr(Context.RunExc { write("icon_deploy.png", anim.getUni().getImg()) }, ErrType.ERROR,
                    "Error during saving deploy icon: $id")
        }

        fun saveIconDisplay() {
            if (anim.getEdi() != null) CommonStatic.ctx.noticeErr(Context.RunExc { write("icon_display.png", anim.getEdi().getImg()) }, ErrType.ERROR,
                    "Error during saving display icon: $id")
        }

        fun saveImgs() {
            saveSprite()
            saveIconDisplay()
            saveIconDeploy()
        }

        fun saveSprite() {
            CommonStatic.ctx.noticeErr(Context.RunExc { write("sprite.png", anim.getNum()) }, ErrType.ERROR,
                    "Error during saving sprite sheet: $id")
        }

        @Throws(IOException::class)
        private fun write(type: String, con: Consumer<PrintStream>) {
            val f: File = CommonStatic.ctx.getWorkspaceFile("./" + id.pack + "/animations/" + id.id + "/" + type)
            Context.Companion.check(f)
            val ps = PrintStream(f)
            con.accept(ps)
            ps.close()
        }

        @Throws(IOException::class)
        private fun write(type: String, img: FakeImage) {
            val f: File = CommonStatic.ctx.getWorkspaceFile("./" + id.pack + "/animations/" + id.id + "/" + type)
            Context.Companion.check(f)
            Context.Companion.check(FakeImage.Companion.write(img, "PNG", f), "save", f)
        }

        init {
            anim = animCI
        }
    }

    class Workspace(id: String?) : Source(id) {
        fun getBGFile(id: PackData.Identifier<Background?>): File {
            return getFile("./backgrounds/" + Data.Companion.trio(id.id) + ".png")
        }

        fun getCasFile(id: PackData.Identifier<CastleImg?>): File {
            return getFile("./castles/" + Data.Companion.trio(id.id) + ".png")
        }

        override fun loadAnimation(name: String?): AnimCI? {
            return AnimCI(SourceAnimLoader(ResourceLocation(id, name), null))
        }

        @Throws(IOException::class)
        override fun readImage(path: String): FakeImage? {
            return ImageBuilder.Companion.builder.build(getFile(path))
        }

        @Throws(IOException::class)
        override fun streamFile(path: String): InputStream? {
            return FileInputStream(getFile(path))
        }

        @Throws(IOException::class)
        fun writeFile(path: String): OutputStream {
            val f = getFile(path)
            Context.Companion.check(f)
            return FileOutputStream(f)
        }

        private fun getFile(path: String): File {
            return CommonStatic.ctx.getWorkspaceFile("./$id/$path")
        }

        companion object {
            fun loadAnimations(id: String?): List<AnimCE> {
                var id = id
                if (id == null) id = ResourceLocation.Companion.LOCAL
                val folder: File = CommonStatic.ctx.getWorkspaceFile("./$id/animations/")
                val list: MutableList<AnimCE> = ArrayList<AnimCE>()
                if (!folder.exists() || !folder.isDirectory) return list
                for (f in folder.listFiles()) {
                    val path = "./" + id + "/animations/" + f.name + "/sprite.png"
                    if (f.isDirectory && CommonStatic.ctx.getWorkspaceFile(path).exists()) list.add(AnimCE(ResourceLocation(id, f.name)))
                }
                return list
            }

            fun validate(rl: ResourceLocation): ResourceLocation {
                // FIXME find valid path
                return rl
            }

            fun loadAnimFile(id: ResourceLocation, str: String): FileData {
                return FDFile(CommonStatic.ctx.getWorkspaceFile("./" + id.pack + "/animations/" + id.id + "/" + str))
            }
        }
    }

    class ZipSource(desc: ZipDesc) : Source(desc.desc.id) {
        private val zip: ZipDesc
        override fun loadAnimation(name: String?): AnimCI? {
            return AnimCI(SourceAnimLoader(ResourceLocation(id, name)) { id: ResourceLocation, path: String -> loadAnimationFile(id, path) })
        }

        @Throws(Exception::class)
        override fun readImage(path: String): FakeImage? {
            return zip.tree.find(path).getData().getImg()
        }

        @Throws(Exception::class)
        override fun streamFile(path: String): InputStream? {
            return zip.readFile(path)
        }

        @Throws(Exception::class)
        fun unzip(password: String): Workspace? {
            if (!zip.match(PackLoader.getMD5(password.toByteArray(), 16))) return null
            val f: File = CommonStatic.ctx.getWorkspaceFile("./$id/main.pack.json")
            val folder = f.parentFile
            if (folder.exists()) {
                if (!CommonStatic.ctx.confirmDelete()) return null
                Context.Companion.delete(f)
            }
            Context.Companion.check(folder.mkdirs(), "create", folder)
            Context.Companion.check(f.createNewFile(), "create", f)
            val ans = Workspace(id)
            zip.unzip(PatchFile { id: String? ->
                val file = ans.getFile(id)
                Context.Companion.check(file.createNewFile(), "create", file)
                file
            })
            return ans
        }

        private fun loadAnimationFile(id: ResourceLocation, path: String): FileDesc {
            return zip.tree.find("./animations/" + id.id + "/" + path).getData()
        }

        init {
            zip = desc
        }
    }

    abstract fun loadAnimation(name: String?): AnimCI?

    /** read images from file. Use it  */
    @Throws(Exception::class)
    abstract fun readImage(path: String): FakeImage?

    /** used for streaming music. Do not use it for images and small text files  */
    @Throws(Exception::class)
    abstract fun streamFile(path: String): InputStream?
}
