package common.system.files

import common.CommonStatic
import common.pack.Context.ErrType
import common.pack.Context.SupExc
import common.system.fake.FakeImage
import common.util.Data
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

interface FileData {
    fun getBytes(): ByteArray? {
        return try {
            val ans = ByteArray(size())
            val `is` = getStream()
            val r = `is`.read(ans)
            `is`.close()
            if (r != size()) CommonStatic.ctx.printErr(ErrType.FATAL, "failed to read data")
            ans
        } catch (e: Exception) {
            CommonStatic.ctx.noticeErr(e, ErrType.FATAL, "failed to read data")
            null
        }
    }

    fun getImg(): FakeImage
    fun getStream(): InputStream
    fun readLine(): Queue<String?>? {
        val `is` = getStream()
        return try {
            val ans: Queue<String?> = ArrayDeque()
            val isr = InputStreamReader(`is`, "UTF-8")
            val reader = BufferedReader(isr)
            var temp: String? = null
            while (reader.readLine().also { temp = it } != null) ans.add(temp)
            reader.close()
            ans
        } catch (e: Exception) {
            CommonStatic.ctx.noticeErr(e, ErrType.FATAL, "failed to read lines")
            null
        }
    }

    fun size(): Int
}

internal interface ByteData : FileData {
    override fun getBytes(): ByteArray?
    override fun getImg(): FakeImage {
        return Data.Companion.err<FakeImage>(SupExc<FakeImage> { FakeImage.Companion.read(bytes) })
    }

    override fun getStream(): InputStream {
        return ByteArrayInputStream(bytes)
    }

    override fun size(): Int {
        return bytes!!.size
    }
}
