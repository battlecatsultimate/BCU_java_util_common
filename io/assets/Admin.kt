package common.io.assets

import common.CommonStatic
import common.battle.BasisSet
import common.io.PackLoader
import common.io.PackLoader.Preload
import common.io.PackLoader.ZipDesc
import common.io.PackLoader.ZipDesc.FileDesc
import common.pack.Context
import common.pack.Context.ErrType
import common.pack.PackData.PackDesc
import common.pack.UserProfile
import common.util.Data
import java.io.Fileimport
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.*
import java.util.function.Consumer

object Admin {
    private val ANIMFL = arrayOf(".imgcut", ".mamodel", ".maanim")
    private val NONPRE = arrayOf("\\./org/img/../.....\\.png", "\\./org/enemy/.../..._.\\.png",
            "\\./org/unit/..././..._.\\.png", "\\./org/unit/..././udi..._.\\.png")

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        CommonStatic.ctx = AdminContext()
        // AssetLoader.merge();
        searchForStaticFields()
    }

    fun preload(fd: FileDesc): Boolean {
        if (fd.size < 1024) return true
        for (str in ANIMFL) if (fd.path.endsWith(str)) return false
        for (str in NONPRE) if (fd.path.length == str.length - 2 && fd.path.matches(str)) return false
        return true
    }

    @Throws(Exception::class)
    fun read(): List<ZipDesc> {
        val folder = File("./assets/assets/")
        val list: MutableList<ZipDesc> = ArrayList<ZipDesc>()
        for (f in folder.listFiles()) {
            if (f.name.endsWith(".asset.bcuzip")) {
                val zd: ZipDesc = PackLoader.readPack(Preload { obj: FileDesc? -> preload() }, f)
                list.add(zd)
            }
        }
        return list
    }

    @Throws(ClassNotFoundException::class)
    fun searchForStaticFields() {
        val f = File("./src/common")
        val qfile: Queue<File> = ArrayDeque()
        val qcls: Queue<Class<*>> = ArrayDeque()
        qfile.add(f)
        while (qfile.size > 0) {
            val fi = qfile.poll()
            if (fi.isDirectory) for (fx in fi.listFiles()) qfile.add(fx) else if (fi.name.endsWith(".java")) {
                var str = fi.toString()
                str = str.substring(6, str.length - 5)
                str = str.replace("/".toRegex(), ".")
                qcls.add(Class.forName(str))
            }
        }
        val clist: MutableList<Class<*>> = ArrayList()
        while (qcls.size > 0) {
            val cls = qcls.poll()
            clist.add(cls)
            for (ci in cls.declaredClasses) qcls.add(ci)
        }
        val flist: MutableList<Field> = ArrayList()
        for (cls in clist) {
            if (cls.isEnum) continue
            if (cls.getAnnotation(StaticPermitted::class.java) != null) continue
            for (field in cls.declaredFields) {
                if (field.modifiers and Modifier.STATIC == 0) continue
                if (field.modifiers and Modifier.FINAL != 0) {
                    if (field.type.isPrimitive) continue
                    if (field.type == String::class.java) continue
                }
                if (field.getAnnotation(StaticPermitted::class.java) != null) continue
                flist.add(field)
            }
        }
        for (field in flist) {
            println(field)
        }
    }

    @Throws(Exception::class)
    fun write() {
        for (i in 1..9) {
            val f = File("./assets/" + Data.Companion.hex(i))
            val dst = File("./assets/assets/" + Data.Companion.hex(i) + ".asset.bcuzip")
            Context.Companion.check(dst)
            val pd = PackDesc("asset_" + Data.Companion.hex(i))
            pd.author = "PONOS"
            pd.BCU_VERSION = "0.5.0.0"
            pd.desc = "default required asset " + Data.Companion.hex(i)
            PackLoader.writePack(dst, f, pd, "battlecatsultimate")
        }
    }

    class AdminContext : Context {
        override fun confirmDelete(): Boolean {
            println("skip delete confirmation")
            return true
        }

        override fun getAssetFile(string: String): File {
            return File("./assets/$string")
        }

        override fun getLangFile(file: String): File {
            return File("./assets/lang/en/$file")
        }

        override fun getPackFolder(): File {
            return File("./packs")
        }

        override fun getUserFile(string: String): File {
            return File("./user/$string")
        }

        override fun getWorkspaceFile(relativePath: String): File {
            return File("./workspace/$relativePath")
        }

        override fun initProfile() {
            AssetLoader.load()
            UserProfile.Companion.getBCData().load(Consumer { str: String? -> })
            BasisSet.Companion.read()
            // UserProfile.loadPacks(); TODO
        }

        override fun noticeErr(e: Exception, t: ErrType, str: String?) {
            printErr(t, str)
            e.printStackTrace(if (t == ErrType.INFO) System.out else System.err)
        }

        override fun preload(desc: FileDesc): Boolean {
            return Admin.preload(desc)
        }

        override fun printErr(t: ErrType, str: String?) {
            (if (t == ErrType.INFO) System.out else System.err).println(str)
        }
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS, AnnotationTarget.FIELD)
    annotation class StaticPermitted(val value: common.io.assets.Admin.StaticPermitted.Type = common.io.assets.Admin.StaticPermitted.Type.FINAL) {
        enum class Type {
            FINAL, ENV, TEMP
        }
    }
}
