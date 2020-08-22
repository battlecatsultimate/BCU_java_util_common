package common.io.assets

import common.CommonStatic
import common.io.PackLoader
import common.io.PackLoader.Preload
import common.io.PackLoader.ZipDesc
import common.io.PackLoader.ZipDesc.FileDesc
import common.io.json.JsonClass
import common.io.json.JsonField
import common.pack.Context
import common.pack.Context.ErrType
import common.pack.PackData.PackDesc
import common.system.files.VFile
import common.util.Data
import java.io.*
import java.io.Fileimport
import java.util.*

com.google.api.client.json.jackson2.JacksonFactory
import kotlin.Throws
import java.io.IOException
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.TreeMap
import java.util.TreeSet

object AssetLoader {
    const val CORE_VER = "0.5.0.0"
    private val ANIMFL = arrayOf(".imgcut", ".mamodel", ".maanim")
    private val NONPRE = arrayOf("\\./org/img/../.....\\.png", "\\./org/enemy/.../..._.\\.png",
            "\\./org/unit/..././..._.\\.png", "\\./org/unit/..././udi..._.\\.png")
    private const val LEN = 1024
    fun load() {
        try {
            val folder: File = CommonStatic.ctx.getAssetFile("./assets/")
            for (f in folder.listFiles()) {
                if (f.name.endsWith(".assets.bcuzips")) {
                    val list: List<ZipDesc> = PackLoader.readAssets(PackLoader.Preloader { obj: ZipDesc? -> getPreload() }, f)
                    for (zip in list) if (Data.Companion.getVer(zip.desc.BCU_VERSION) <= Data.Companion.getVer(CORE_VER)) {
                        VFile.Companion.getBCFileTree().merge(zip.tree)
                    }
                }
            }
        } catch (e: Exception) {
            CommonStatic.ctx.noticeErr(e, ErrType.FATAL, "failed to read asset")
        }
    }

    @Throws(Exception::class)
    fun merge() {
        try {
            val folder: File = CommonStatic.ctx.getAssetFile("./assets/")
            val map: MutableMap<String, MutableMap<String, File>> = TreeMap<String, Map<String, File>>()
            for (f in folder.listFiles()) {
                if (f.name.endsWith(".asset.bcuzip")) {
                    val pre = f.name.substring(0, 2)
                    val name = f.name.substring(0, 6)
                    var sub = map[pre]
                    if (sub == null) map[pre] = TreeMap<String, File>().also { sub = it }
                    sub!![name] = f
                }
            }
            for ((key, value) in map) {
                val target: File = CommonStatic.ctx.getAssetFile("./assets/" + key + "xxxx.assets.bcuzips")
                val dst: File = CommonStatic.ctx.getAssetFile("./assets/.temp.assets.bcuzips")
                Context.Companion.check(dst)
                val fos = FileOutputStream(dst)
                val header = AssetHeader()
                var fis: FileInputStream? = null
                if (target.exists()) {
                    fis = FileInputStream(target)
                    PackLoader.read(fis, header)
                }
                val queue: MutableList<File> = ArrayList()
                for ((_, value1) in value) if (header.add(value1)) queue.add(value1) else Context.Companion.delete(value1)
                PackLoader.write(fos, header)
                if (fis != null) {
                    stream(fos, fis)
                    fis.close()
                }
                for (efile in queue) {
                    fis = FileInputStream(efile)
                    stream(fos, fis)
                    fis.close()
                    Context.Companion.delete(efile)
                }
                fos.close()
                Context.Companion.delete(target)
                dst.renameTo(target)
            }
        } catch (e: Exception) {
            // TODO corruption prevention
            CommonStatic.ctx.noticeErr(e, ErrType.FATAL, "failed to merge asset")
        }
    }

    fun previewAssets(): Set<String>? {
        return try {
            val folder: File = CommonStatic.ctx.getAssetFile("./assets/")
            val ans: MutableSet<String> = TreeSet<String>()
            for (f in folder.listFiles()) {
                if (f.name.endsWith(".assets.bcuzips")) {
                    val header = AssetHeader()
                    val fis = FileInputStream(f)
                    PackLoader.read(fis, header)
                    fis.close()
                    for (ent in header.list) ans.add(ent.desc.id)
                }
            }
            ans
        } catch (e: Exception) {
            CommonStatic.ctx.noticeErr(e, ErrType.FATAL, "failed to preview asset")
            null
        }
    }

    private fun getPreload(desc: ZipDesc): Preload {
        return if (Data.Companion.getVer(desc.desc.BCU_VERSION) > Data.Companion.getVer(CORE_VER)) Preload { fd: FileDesc? -> false } else Preload { obj: FileDesc? -> preload() }
    }

    private fun preload(fd: FileDesc): Boolean {
        if (fd.size < 1024) return true
        for (str in ANIMFL) if (fd.path.endsWith(str)) return false
        for (str in NONPRE) if (fd.path.length == str.length - 2 && fd.path.matches(str)) return false
        return true
    }

    @Throws(IOException::class)
    private fun stream(fos: OutputStream, fis: InputStream?) {
        val data = ByteArray(LEN)
        var len: Int
        do {
            len = fis!!.read(data)
            fos.write(data, 0, len)
        } while (len == LEN)
    }

    @JsonClass
    class AssetHeader {
        @JsonClass
        class AssetEntry {
            @JsonField
            val desc: PackDesc?

            @JsonField
            val size: Int

            constructor() {
                desc = null
                size = 0
            }

            constructor(f: File) {
                desc = PackLoader.readPack(Preload { fd: FileDesc? -> false }, f).desc
                size = f.length().toInt()
            }

            override fun equals(o: Any?): Boolean {
                return o is AssetEntry && o.desc.id == desc.id
            }
        }

        @JsonField(generic = [AssetEntry::class])
        val list = ArrayList<AssetEntry>()
        var offset = 0

        @Throws(Exception::class)
        fun add(file: File): Boolean {
            val ent = AssetEntry(file)
            if (list.contains(ent)) return false
            list.add(ent)
            return true
        }
    }
}
