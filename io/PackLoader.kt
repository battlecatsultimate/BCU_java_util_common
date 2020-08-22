package common.io

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import common.CommonStatic
import common.io.DataIO
import common.io.MultiStream
import common.io.PackLoader.FileLoader.FLStream
import common.io.PackLoader.ZipDesc
import common.io.PackLoader.ZipDesc.FileDesc
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader.AssetHeader
import common.io.json.JsonClass
import common.io.json.JsonClass.JCConstructor
import common.io.json.JsonClass.RType
import common.io.json.JsonDecoder
import common.io.json.JsonDecoder.OnInjected
import common.io.json.JsonEncoder
import common.io.json.JsonField
import common.io.json.JsonField.GenType
import common.pack.Context.ErrType
import common.pack.Context.SupExc
import common.pack.PackData.PackDesc
import common.system.fake.FakeImage
import common.system.files.FDByte
import common.system.files.FileData
import common.system.files.VFileRoot
import common.util.Data
import java.io.*
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object PackLoader {
    private const val HEADER = 16
    private const val PASSWORD = 16
    private const val CHUNK = 1 shl 16
    private const val HEAD_STR = "battlecatsultimate"
    private val HEAD_DATA = getMD5(HEAD_STR.toByteArray(), HEADER)
    private val INIT_VECTOR = getMD5("battlecatsultimate".toByteArray(), 16)
    @Throws(Exception::class)
    fun decrypt(key: ByteArray?): Cipher {
        val iv = IvParameterSpec(INIT_VECTOR)
        val skeySpec = SecretKeySpec(key, "AES")
        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)
        return cipher
    }

    @Throws(Exception::class)
    fun encrypt(key: ByteArray?): Cipher {
        val iv = IvParameterSpec(INIT_VECTOR)
        val skeySpec = SecretKeySpec(key, "AES")
        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv)
        return cipher
    }

    fun getMD5(data: ByteArray?, len: Int): ByteArray {
        val md5: MessageDigest = Data.Companion.err<MessageDigest>(SupExc<MessageDigest> { MessageDigest.getInstance("MD5") })
        val ans: ByteArray = md5.digest(data)
        return if (ans.size == len) ans else Arrays.copyOf(ans, len)
    }

    @Throws(Exception::class)
    fun read(fis: FileInputStream, asset: AssetHeader) {
        val head = ByteArray(HEADER)
        fis.read(head)
        if (!Arrays.equals(head, HEAD_DATA)) throw Exception("Corrupted File: header not match")
        val len = ByteArray(4)
        fis.read(len)
        val size: Int = DataIO.Companion.toInt(DataIO.Companion.translate(len), 0)
        val data = ByteArray(size)
        fis.read(data)
        val desc = String(data)
        val je: JsonElement = JsonParser.parseString(desc)
        JsonDecoder.Companion.inject<AssetHeader>(je, AssetHeader::class.java, asset)
        asset.offset = HEADER + 4 + size
    }

    @Throws(Exception::class)
    fun readAssets(cont: Preloader, f: File): List<ZipDesc> {
        val fis = FileInputStream(f)
        val header = AssetHeader()
        read(fis, header)
        val ans: MutableList<ZipDesc> = ArrayList()
        var off: Int = header.offset
        for (ent in header.list) {
            val zip = FileLoader(cont, fis, off, f, true).pack
            ans.add(zip)
            off += ent.size
        }
        fis.close()
        return ans
    }

    @Throws(Exception::class)
    fun readPack(cont: Preload?, f: File): ZipDesc {
        val fis = FileInputStream(f)
        val ans = FileLoader(Preloader { desc: ZipDesc? -> cont }, fis, 0, f, false).pack
        fis.close()
        return ans
    }

    @Throws(Exception::class)
    fun write(fos: FileOutputStream, asset: AssetHeader?) {
        fos.write(HEAD_DATA)
        val data: ByteArray = JsonEncoder.Companion.encode(asset).toString().toByteArray()
        val len = ByteArray(4)
        DataIO.Companion.fromInt(len, 0, data.size)
        fos.write(len)
        fos.write(data)
    }

    @Throws(Exception::class)
    fun writePack(dst: File, folder: File, pd: PackDesc, password: String) {
        FileSaver(dst, folder, pd, password)
    }

    private fun regulate(size: Int): Int {
        return if (size and 0xF == 0) size else (size or 0xF) + 1
    }

    interface PatchFile {
        @Throws(Exception::class)
        fun getFile(path: String?): File
    }

    interface Preload {
        fun preload(fd: FileDesc?): Boolean
    }

    interface Preloader {
        fun getPreload(desc: ZipDesc?): Preload
    }

    @JsonClass(read = RType.FILL)
    class ZipDesc {
        @JsonClass
        class FileDesc : FileData {
            @JsonField
            var path: String? = null

            @JsonField
            var size = 0

            @JsonField
            var offset = 0
            var file // writing only
                    : File? = null
            private var pack // reading only
                    : ZipDesc? = null
            val data // preload reading only
                    : FDByte? = null

            constructor(parent: FileSaver, path: String?, f: File) {
                this.path = path
                file = f
                size = f.length().toInt()
                offset = parent.len
                parent.len += if (size and 0xF == 0) size else (size or 0xF) + 1
                parent.len += PASSWORD
            }

            @Deprecated("")
            @JCConstructor
            constructor(desc: ZipDesc?) {
                pack = desc
            }

            override fun getImg(): FakeImage {
                return if (data != null) data.getImg() else Data.Companion.err<FakeImage>(SupExc<FakeImage> { FakeImage.Companion.read(this) })
            }

            override fun getStream(): InputStream {
                return pack!!.readFile(path)
            }

            override fun readLine(): Queue<String?>? {
                return if (data != null) data.readLine() else super@FileData.readLine()
            }

            override fun size(): Int {
                return size
            }
        }

        @JsonField
        var desc: PackDesc? = null

        @JsonField(gen = GenType.GEN)
        var files: Array<FileDesc>
        var tree: VFileRoot<FileDesc> = VFileRoot<FileDesc>(".")
        private var offset = 0
        private var loader: FileLoader? = null

        constructor(pd: PackDesc?, fs: Array<FileDesc>) {
            desc = pd
            files = fs
        }

        private constructor(fl: FileLoader, off: Int) {
            loader = fl
            offset = off
        }

        fun match(data: ByteArray?): Boolean {
            return Arrays.equals(data, loader!!.key)
        }

        @OnInjected
        fun onInjected() {
            for (fd in files) tree.build(fd.path, fd)
        }

        fun readFile(path: String?): InputStream {
            val fd: FileDesc = tree.find(path).getData()
            println("stream requested: " + fd.path + ", " + fd.size)
            return CommonStatic.ctx.noticeErr<FLStream>(SupExc<FLStream> { FLStream(loader, offset + fd.offset, fd.size) },
                    ErrType.ERROR, "failed to read bcuzip at $path")
        }

        @Throws(Exception::class)
        fun unzip(func: PatchFile) {
            val fis: InputStream = FileInputStream(loader!!.file)
            fis.skip(offset.toLong())
            for (fd in files) {
                val n = regulate(fd.size) / PASSWORD
                val dest = func.getFile(fd.path)
                val fos: OutputStream = FileOutputStream(dest)
                val bs = ByteArray(PASSWORD)
                val cipher: Cipher = decrypt(loader!!.key)
                fis.read(bs)
                cipher.update(bs)
                for (i in 0 until n) {
                    fis.read(bs)
                    val ans: ByteArray = if (i == n - 1) cipher.doFinal(bs) else cipher.update(bs)
                    fos.write(ans)
                }
                fos.close()
            }
            fis.close()
        }

        @Throws(Exception::class)
        fun load() {
            for (fd in files) if (loader!!.context.getPreload(this).preload(fd)) fd.data = FDByte(loader!!.decode(fd.size)) else loader!!.fis.skip(regulate(fd.size + PASSWORD).toLong())
        }

        @Throws(Exception::class)
        private fun save(saver: FileSaver) {
            for (fd in files) {
                val fis = FileInputStream(fd.file)
                var rem = fd.size
                var data: ByteArray? = null
                val cipher: Cipher = encrypt(saver.key)
                while (rem > 0) {
                    val size = Math.min(rem, CHUNK)
                    if (data == null || data.size != size) data = ByteArray(size)
                    fis.read(data)
                    rem -= size
                    saver.save(cipher, data, rem == 0)
                }
                fis.close()
            }
        }
    }

    private class FileLoader(val context: Preloader, stream: FileInputStream, off: Int, val file: File, useRAF: Boolean) {
        private class FLStream private constructor(ld: FileLoader, offset: Int, size: Int) : InputStream() {
            private val fis: MultiStream.ByteStream
            private val cipher: Cipher
            private var LEN = 0
            private var len: Int
            private var size: Int
            private val cache: ByteArray
            private var index: Int
            @Throws(IOException::class)
            override fun close() {
                fis.close()
            }

            @Throws(IOException::class)
            override fun read(): Int {
                if (size == 0) return -1
                if (index >= LEN) update()
                return readByte() and 0xff
            }

            @Throws(IOException::class)
            override fun read(b: ByteArray, off: Int, len: Int): Int {
                var off = off
                val rlen = Math.min(len, size)
                if (b == null) {
                    throw NullPointerException()
                } else if (off < 0 || rlen < 0 || rlen > b.size - off) {
                    throw IndexOutOfBoundsException()
                } else if (rlen == 0) {
                    return if (rlen < len) -1 else 0
                }
                var i = rlen
                while (i > 0) {
                    if (index >= LEN) update()
                    val avi = Math.min(i, LEN - index)
                    System.arraycopy(cache, index, b, off, avi)
                    i -= avi
                    index += avi
                    off += avi
                    size -= avi
                }
                return rlen
            }

            private fun readByte(): Byte {
                size--
                return cache[index++]
            }

            @Throws(IOException::class)
            private fun update() {
                val rlen = Math.min(len, LEN)
                fis.read(cache, 0, rlen)
                len -= rlen
                index = 0
                try {
                    if (len == 0) cipher.doFinal(cache, 0, rlen, cache, 0) else cipher.update(cache, 0, rlen, cache, 0)
                } catch (e: Exception) {
                    throw IOException(e)
                }
            }

            init {
                len = if (size and 0xF == 0) size else (size or 0xF) + 1
                for (i in 0..6) {
                    if (i == 6 || len < 1 shl i + 13) {
                        LEN = 1 shl i + 10
                        break
                    }
                }
                cache = ByteArray(LEN)
                this.size = size
                cipher = decrypt(ld.key)
                fis = MultiStream.Companion.getStream(ld.file, offset, ld.useRAF)
                val init = ByteArray(PASSWORD)
                fis.read(init, 0, PASSWORD)
                cipher.update(init, 0, PASSWORD, ByteArray(0), 0)
                index = LEN
            }
        }

        val fis: FileInputStream
        val pack: ZipDesc
        val key: ByteArray
        private val useRAF: Boolean

        @Throws(Exception::class)
        fun decode(size: Int): ByteArray {
            val len = regulate(size) + PASSWORD
            var bs = ByteArray(len)
            fis.read(bs)
            bs = decrypt(key).doFinal(bs)
            return if (bs.size != size) Arrays.copyOf(bs, size) else bs
        }

        init {
            fis = stream
            this.useRAF = useRAF
            val head = ByteArray(HEADER)
            fis.read(head)
            if (!Arrays.equals(head, HEAD_DATA)) throw Exception("Corrupted File: header not match")
            key = ByteArray(PASSWORD)
            fis.read(key)
            val len = ByteArray(4)
            fis.read(len)
            val size: Int = DataIO.Companion.toInt(DataIO.Companion.translate(len), 0)
            val desc = String(decode(size))
            val je: JsonElement = JsonParser.parseString(desc)
            val offset = off + HEADER + PASSWORD * 2 + 4 + regulate(size)
            pack = JsonDecoder.Companion.inject<ZipDesc>(je, ZipDesc::class.java, ZipDesc(this, offset))
            pack.load()
        }
    }

    class FileSaver(dst: File, folder: File, pd: PackDesc, password: String) {
        private val fos: FileOutputStream
        val key: ByteArray
        val len = 0
        private fun addFiles(fs: MutableList<FileDesc>, f: File, path: String) {
            for (str in EXCLUDE) if (f.name == str) return
            if (f.isDirectory) for (fi in f.listFiles()) addFiles(fs, fi, path + f.name + "/") else fs.add(FileDesc(this, path + f.name, f))
        }

        @Throws(Exception::class)
        fun save(cipher: Cipher, bs: ByteArray, end: Boolean) {
            var bs = bs
            if (bs.size and 0xF != 0) bs = Arrays.copyOf(bs, (bs.size or 0xF) + 1)
            bs = if (end) cipher.doFinal(bs) else cipher.update(bs)
            fos.write(bs)
        }

        companion object {
            @StaticPermitted
            private val EXCLUDE = arrayOf(".DS_Store", ".desktop.ini", "__MACOSX")
        }

        init {
            val fs: MutableList<FileDesc> = ArrayList<FileDesc>()
            for (fi in folder.listFiles()) addFiles(fs, fi, "./")
            val desc = ZipDesc(pd, fs.toTypedArray())
            val bytedesc: ByteArray = JsonEncoder.Companion.encode(desc).toString().toByteArray()
            fos = FileOutputStream(dst)
            fos.write(HEAD_DATA)
            key = getMD5(password.toByteArray(), PASSWORD)
            fos.write(key)
            val len = ByteArray(4)
            DataIO.Companion.fromInt(len, 0, bytedesc.size)
            fos.write(len)
            save(encrypt(key), bytedesc, true)
            desc.save(this)
            fos.close()
        }
    }
}
