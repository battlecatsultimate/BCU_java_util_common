package common.io

import common.io.DataIO
import java.io.IOException
import java.io.OutputStream
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

@Strictfp
interface OutStream {
    fun accept(os: OutStream)

    @Throws(IOException::class)
    fun flush(fos: OutputStream)
    fun MD5(): ByteArray?
    fun terminate()
    fun translate(): InStream
    fun writeByte(n: Byte)
    fun writeBytesB(s: ByteArray)
    fun writeBytesI(s: ByteArray)
    fun writeDouble(n: Double)
    fun writeDoubles(ints: DoubleArray?)
    fun writeFloat(n: Double)
    fun writeFloat(n: Float)
    fun writeInt(n: Int)
    fun writeIntB(ints: IntArray?)
    fun writeIntBB(ints: Array<IntArray?>?)
    fun writeIntsN(vararg ns: Int) {
        for (i in ns) writeInt(i)
    }

    fun writeLong(n: Long)
    fun writeShort(n: Short)
    fun writeString(str: String)

    companion object {
        fun getIns(): OutStream? {
            return OutStreamDef()
        }
    }
}

@Strictfp
internal class OutStreamDef : DataIO, OutStream {
    private var bs: ByteArray
    private var index = 0

    constructor() {
        bs = ByteArray(1024)
    }

    constructor(size: Int) {
        bs = ByteArray(size)
        index = 0
    }

    constructor(data: ByteArray) {
        bs = data
        index = bs.size
    }

    override fun accept(os: OutStream) {
        if (os !is OutStreamDef) throw BCUException("OutStream type mismatch")
        os.terminate()
        val obs = os.bs
        if (obs.size == 0) throw BCUException("zero stream")
        writeInt(obs.size)
        check(obs.size)
        for (i in obs.indices) bs[index++] = obs[i]
    }

    fun concat(s: ByteArray) {
        check(s.size)
        for (b in s) DataIO.Companion.fromByte(bs, index++, b)
    }

    @Throws(IOException::class)
    override fun flush(fos: OutputStream) {
        terminate()
        fos.write(DataIO.Companion.getSignature(bs.size)) // signature
        fos.write(getBytes())
    }

    fun getBytes(): ByteArray {
        return bs
    }

    override fun MD5(): ByteArray? {
        try {
            val mdi: MessageDigest = MessageDigest.getInstance("MD5")
            mdi.update(DataIO.Companion.getSignature(bs.size))
            return mdi.digest(bs)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return null
    }

    fun pos(): Int {
        return index
    }

    override fun terminate() {
        if (index == bs.size) return
        bs = Arrays.copyOf(bs, index)
    }

    override fun toString(): String {
        return "OutStreamDef " + size()
    }

    override fun translate(): InStreamDef {
        return InStreamDef(DataIO.Companion.translate(bs), 0, bs.size)
    }

    override fun writeByte(n: Byte) {
        check(1)
        DataIO.Companion.fromByte(bs, index, n)
        index++
    }

    override fun writeBytesB(s: ByteArray) {
        check(s.size + 1)
        writeByte(s.size.toByte())
        for (b in s) writeByte(b)
    }

    override fun writeBytesI(s: ByteArray) {
        writeInt(s.size)
        check(s.size)
        for (b in s) writeByte(b)
    }

    override fun writeDouble(n: Double) {
        check(8)
        DataIO.Companion.fromDouble(bs, index, n)
        index += 8
    }

    override fun writeDoubles(ints: DoubleArray?) {
        if (ints == null) {
            writeByte(0.toByte())
            return
        }
        writeByte(ints.size.toByte())
        for (i in ints) writeDouble(i)
    }

    override fun writeFloat(n: Double) {
        writeFloat(n.toFloat())
    }

    override fun writeFloat(n: Float) {
        check(4)
        DataIO.Companion.fromFloat(bs, index, n)
        index += 4
    }

    override fun writeInt(n: Int) {
        check(4)
        DataIO.Companion.fromInt(bs, index, n)
        index += 4
    }

    override fun writeIntB(ints: IntArray?) {
        if (ints == null) {
            writeByte(0.toByte())
            return
        }
        writeByte(ints.size.toByte())
        for (i in ints) writeInt(i)
    }

    override fun writeIntBB(ints: Array<IntArray?>?) {
        if (ints == null) {
            writeByte(0.toByte())
            return
        }
        writeByte(ints.size.toByte())
        for (i in ints) writeIntB(i)
    }

    override fun writeLong(n: Long) {
        check(8)
        DataIO.Companion.fromLong(bs, index, n)
        index += 8
    }

    override fun writeShort(n: Short) {
        check(2)
        DataIO.Companion.fromShort(bs, index, n)
        index += 2
    }

    override fun writeString(str: String) {
        var bts: ByteArray
        try {
            bts = str.toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            bts = str.toByteArray()
            e.printStackTrace()
        }
        writeBytesB(bts)
    }

    protected fun size(): Int {
        return bs.size
    }

    private fun check(i: Int) {
        if (index + i > bs.size * 2) bs = Arrays.copyOf(bs, index + i) else if (index + i > bs.size) bs = Arrays.copyOf(bs, bs.size * 2)
    }
}
