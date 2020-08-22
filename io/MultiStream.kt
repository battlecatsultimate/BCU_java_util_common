package common.io

import common.CommonStatic
import common.pack.Context.ErrType
import java.io.*
import java.util.*

class MultiStream private constructor(private val file: File) {
    interface ByteStream {
        @Throws(IOException::class)
        fun close()

        @Throws(IOException::class)
        fun read(bs: ByteArray, i: Int, rlen: Int)
    }

    class TrueStream(f: File?, pos: Int) : common.io.MultiStream.ByteStream {
        private val fis: FileInputStream
        @Throws(IOException::class)
        override fun close() {
            fis.close()
        }

        @Throws(IOException::class)
        override fun read(bs: ByteArray, off: Int, len: Int) {
            fis.read(bs, off, len)
        }

        init {
            fis = FileInputStream(f)
            val skip = fis.skip(pos.toLong()) as Int
            if (skip != pos) throw IOException("failed to skip bytes")
        }
    }

    private interface RunExc {
        @Throws(IOException::class)
        fun run()
    }

    private inner class SubStream(private var pos: Int) : common.io.MultiStream.ByteStream {
        @Throws(IOException::class)
        override fun close() {
        }

        @Throws(IOException::class)
        override fun read(bs: ByteArray, off: Int, len: Int) {
            attempt { readBytes(pos, bs, off, len) }
            pos += bs.size
        }
    }

    private var raf: RandomAccessFile? = null
    private var poscache: Long = -1
    @Throws(IOException::class)
    fun close() {
        if (raf == null) return
        poscache = -1
        raf.close()
        raf = null
    }

    @Throws(FileNotFoundException::class)
    private fun access(): RandomAccessFile? {
        if (raf != null) return raf
        poscache = -1
        raf = RandomAccessFile(file, "r")
        return raf
    }

    @Throws(IOException::class)
    private fun attempt(r: common.io.MultiStream.RunExc) {
        try {
            r.run()
            return
        } catch (e: IOException) {
            CommonStatic.ctx.printErr(ErrType.INFO, "failed to read, attempted again")
            close()
        }
        r.run()
        CommonStatic.ctx.printErr(ErrType.INFO, "attempt succeed")
    }

    @Throws(IOException::class)
    private fun readBytes(pos: Int, arr: ByteArray, off: Int, len: Int) {
        access()
        if (poscache == -1L) poscache = raf.getFilePointer()
        if (pos.toLong() != poscache) {
            raf.seek(pos.toLong())
            poscache = pos.toLong()
        }
        raf.read(arr, off, len)
        poscache += len.toLong()
    }

    companion object {
        private val MAP: MutableMap<File, MultiStream> = HashMap()
        @Throws(IOException::class)
        fun getStream(f: File, pos: Int, useRAF: Boolean): common.io.MultiStream.ByteStream {
            if (!useRAF) {
                return TrueStream(f, pos)
            }
            var ms = MAP[f]
            if (ms == null) MAP[f] = MultiStream(f).also { ms = it }
            return ms!!.SubStream(pos)
        }
    }
}
