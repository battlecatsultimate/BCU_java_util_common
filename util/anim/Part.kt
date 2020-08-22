package common.util.anim

import common.io.InStream
import common.io.OutStream
import common.util.Data
import java.io.PrintStream
import java.util.*

class Part : Data, Cloneable, Comparable<Part?> {
    var ints = IntArray(5)
    var name: String? = null
    var n: Int
    var max = 0
    var off = 0
    var fir = 0
    var frame = 0
    var vd // for editor only
            = 0
    var moves: Array<IntArray?>

    constructor() {
        ints = intArrayOf(0, 5, -1, 0, 0)
        name = ""
        n = 0
        moves = arrayOfNulls(0)
    }

    constructor(qs: Queue<String>) {
        var ss = qs.poll().trim { it <= ' ' }.split(",").toTypedArray()
        for (i in 0..4) ints[i] = ss[i].trim { it <= ' ' }.toInt()
        name = if (ss.size == 6) Data.Companion.restrict(ss[5]) else ""
        n = qs.poll().trim { it <= ' ' }.toInt()
        moves = Array(n) { IntArray(4) }
        for (i in 0 until n) {
            ss = qs.poll().trim { it <= ' ' }.split(",").toTypedArray()
            for (j in 0..3) moves[i]!![j] = ss[j].trim { it <= ' ' }.toInt()
        }
        validate()
    }

    private constructor(p: Part) {
        ints = p.ints.clone()
        name = p.name
        n = p.n
        moves = arrayOfNulls(n)
        for (i in 0 until n) moves[i] = p.moves[i].clone()
        off = p.off
        validate()
    }

    fun check(anim: AnimD<*, *>) {
        val mms: Int = anim.mamodel!!.n
        val ics: Int = anim.imgcut!!.n
        if (ints[0] >= mms) ints[0] = 0
        if (ints[0] < 0) ints[0] = 0
        if (ints[1] == 2) for (move in moves) if (move!![1] >= ics) move[1] = 0
    }

    public override fun clone(): Part {
        return Part(this)
    }

    override operator fun compareTo(o: Part): Int {
        return Integer.compare(ints[0], o.ints[0])
    }

    fun validate() {
        var doff = 0
        if (n != 0 && (moves[0]!![0] - off < 0 || ints[2] > 1)) doff -= moves[0]!![0]
        for (i in 0 until n) moves[i]!![0] += doff
        off += doff
        fir = if (moves.size == 0) 0 else moves[0]!![0]
        max = if (n > 0) moves[n - 1]!![0] else 0
    }

    fun ensureLast(es: Array<EPart>) {
        if (n == 0) return
        frame = moves[n - 1]!![0]
        es[ints[0]].alter(ints[1], moves[n - 1]!![1].also { vd = it })
    }

    fun getMax(): Int {
        return if (ints[2] > 1) fir + (max - fir) * ints[2] else max
    }

    fun restore(`is`: InStream) {
        n = `is`.nextInt()
        max = `is`.nextInt()
        off = `is`.nextInt()
        ints = `is`.nextIntsB()
        moves = `is`.nextIntsBB()
        name = `is`.nextString()
        validate()
    }

    fun update(f: Int, es: Array<EPart>) {
        frame = f
        for (i in 0 until n) if (frame == moves[i]!![0]) es[ints[0]].alter(ints[1], moves[i]!![1].also { vd = it }) else if (i < n - 1 && frame > moves[i]!![0] && frame < moves[i + 1]!![0]) {
            if (ints[1] > 1) {
                val f0 = moves[i]!![0]
                val v0 = moves[i]!![1]
                val f1 = moves[i + 1]!![0]
                val v1 = moves[i + 1]!![1]
                var ti = 1.0 * (frame - f0) / (f1 - f0)
                if (moves[i]!![2] == 1 || ints[1] == 13 || ints[1] == 14) ti = 0.0 else if (moves[i]!![2] == 0) ; else if (moves[i]!![2] == 2) ti = if (moves[i]!![3] >= 0) 1 - Math.sqrt(1 - Math.pow(ti, moves[i]!![3].toDouble())) else Math.sqrt(1 - Math.pow(1 - ti, (-moves[i]!![3]).toDouble())) else if (moves[i]!![2] == 3) {
                    vd = ease3(i, frame)
                    es[ints[0]].alter(ints[1], vd)
                    break
                } else if (moves[i]!![2] == 4) ti = if (moves[i]!![3] > 0) 1 - Math.cos(ti * Math.PI / 2) else if (moves[i]!![3] < 0) Math.sin(ti * Math.PI / 2) else (1 - Math.cos(ti * Math.PI)) / 2
                vd = if (ints[1] == 2) if (v1 - v0 < 0) Math.ceil((v1 - v0) * ti + v0).toInt() else ((v1 - v0) * ti + v0).toInt() else ((v1 - v0) * ti + v0).toInt()
                es[ints[0]].alter(ints[1], vd)
                break
            }
        }
        if (n > 0 && frame > moves[n - 1]!![0]) ensureLast(es)
    }

    fun write(os: OutStream) {
        os.writeInt(n)
        os.writeInt(max)
        os.writeInt(off)
        os.writeIntB(ints)
        os.writeIntBB(moves)
        os.writeString(name)
    }

    fun write(ps: PrintStream) {
        for (`val` in ints) ps.print("$`val`,")
        ps.println(name)
        ps.println(moves.size)
        for (move in moves) {
            ps.print(move!![0] - off.toString() + ",")
            for (i in 1 until move.size) ps.print(move[i].toString() + ",")
            ps.println()
        }
    }

    private fun ease3(i: Int, frame: Int): Int {
        var low = i
        var high = i
        for (j in i - 1 downTo 0) low = if (moves[j]!![2] == 3) j else break
        for (j in i + 1 until moves.size) if (moves[j.also { high = it }]!![2] != 3) break
        var sum = 0.0
        for (j in low..high) {
            var `val` = moves[j]!![1] * 4096.toDouble()
            for (k in low..high) if (j != k) `val` *= 1.0 * (frame - moves[k]!![0]) / (moves[j]!![0] - moves[k]!![0])
            sum += `val`
        }
        return (sum / 4096).toInt()
    }
}
