package common.io

import common.system.P

@Strictfp
abstract class DataIO {
    companion object {
        protected const val BYTE: Byte = 1
        protected const val SHORT: Byte = 2
        protected const val INT: Byte = 3
        protected const val LONG: Byte = 4
        protected const val FLOAT: Byte = 5
        protected const val DOUBLE: Byte = 6
        protected const val BYTESI: Byte = 7
        protected const val INTSB: Byte = 8
        protected const val INTSI: Byte = 9
        protected const val STRING: Byte = 10
        protected const val BYTESB: Byte = 11
        protected const val INTSSBB: Byte = 12
        protected const val DOUBLESB: Byte = 13
        protected const val SUBS: Byte = 14
        fun fromABP(b: ByteArray, index: Int, n: P) {
            var index = index
            fromShort(b, index, (n.x * 10 + 2000) as Short)
            index += 2
            fromShort(b, index, (n.y * 10 + 2000) as Short)
        }

        /** write a number n into a byte[] start from index  */
        fun fromByte(b: ByteArray, index: Int, n: Byte) {
            b[index] = n
        }

        fun fromDouble(b: ByteArray, index: Int, n: Double) {
            val l = java.lang.Double.doubleToLongBits(n)
            fromLong(b, index, l)
        }

        fun fromFloat(b: ByteArray, index: Int, n: Float) {
            val l = java.lang.Float.floatToIntBits(n)
            fromInt(b, index, l)
        }

        fun fromInt(b: ByteArray, index: Int, n: Int) {
            for (i in 0..3) b[index + i] = (n shr i * 8 and 0xff).toByte()
        }

        fun fromLong(b: ByteArray, index: Int, n: Long) {
            for (i in 0..7) b[index + i] = (n shr i * 8 and 0xff).toByte()
        }

        fun fromShort(b: ByteArray, index: Int, n: Short) {
            for (i in 0..1) b[index + i] = (n shr i * 8 and 0xff) as Byte
        }

        fun getSignature(n: Int): ByteArray {
            val ans = ByteArray(4)
            fromInt(ans, 0, n)
            return ans
        }

        fun toABP(datas: IntArray, index: Int): P {
            val x = toShort(datas, index).toDouble()
            val y = toShort(datas, index + 2).toDouble()
            return P(x * 0.1 - 200, y * 0.1 - 200)
        }

        /** read a number from a byte[] start from index  */
        fun toByte(datas: IntArray, index: Int): Int {
            return datas[index]
        }

        fun toDouble(datas: IntArray, index: Int): Double {
            return java.lang.Double.longBitsToDouble(toLong(datas, index))
        }

        fun toFloat(datas: IntArray, index: Int): Float {
            return java.lang.Float.intBitsToFloat(toInt(datas, index))
        }

        fun toInt(datas: IntArray, index: Int): Int {
            var ans = 0
            for (i in 0..3) ans += datas[index + i] shl i * 8
            return ans
        }

        fun toLong(datas: IntArray, index: Int): Long {
            var l: Long = 0
            for (i in 0..7) {
                var temp = datas[index + i].toLong()
                temp = temp shl i * 8
                l += temp
            }
            return l
        }

        fun toShort(datas: IntArray, index: Int): Int {
            var ans: Short = 0
            for (i in 0..1) (ans += datas[index + i] shl (i * 8).toShort().toInt()).toShort()
            return ans.toInt()
        }

        /** translate byte[] to int[] in order to make all the bytes unsigned  */
        fun translate(datas: ByteArray): IntArray {
            val ans = IntArray(datas.size)
            for (i in ans.indices) ans[i] = datas[i] and 0xff
            return ans
        }

        fun translate(datas: IntArray): ByteArray {
            val ans = ByteArray(datas.size)
            for (i in ans.indices) ans[i] = datas[i].toByte()
            return ans
        }
    }
}
