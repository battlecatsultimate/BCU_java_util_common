package common.system.files

import common.CommonStatic
import common.pack.Context.ErrType
import common.pack.Context.SupExc
import common.system.fake.FakeImage
import common.util.Data
import java.io.*

class FDFile(private val file: File) : FileData {
    override fun getBytes(): ByteArray? {
        return try {
            val bs = ByteArray(file.length().toInt())
            val buf = BufferedInputStream(FileInputStream(file))
            buf.read(bs, 0, bs.size)
            buf.close()
            bs
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun getImg(): FakeImage {
        return Data.Companion.err<FakeImage>(SupExc<FakeImage> { FakeImage.Companion.read(file) })
    }

    override fun getStream(): InputStream {
        return CommonStatic.ctx.noticeErr<FileInputStream>(SupExc<FileInputStream> { FileInputStream(file) }, ErrType.ERROR,
                "failed to read bcuzip at $file")
    }

    override fun size(): Int {
        return file.length().toInt()
    }

    override fun toString(): String {
        return file.name
    }
}
