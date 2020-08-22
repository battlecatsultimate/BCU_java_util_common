package common.battle

import common.CommonStatic.FakeKey
import common.io.InStream
import common.util.stage.EStage
import common.util.stage.Recd

class SBRply(re: Recd) : Mirror(re) {
    private val r: Recd
    private val mir: MirrorSet
    fun back(t: Int) {
        val m = mir.getReal(sb.time - t)
        sb = m!!.sb
        rl = m.rl
    }

    fun prog(): Int {
        return if (r.len == 0) 0 else Math.min(mir.size - 1, sb.time * mir.size / r.len)
    }

    fun restoreTo(perc: Int) {
        val m = mir.getRaw(perc) ?: return
        sb = m.sb
        rl = m.rl
        while (prog() < perc) update()
    }

    fun size(): Int {
        return mir.size - 1
    }

    fun transform(kh: FakeKey): SBCtrl {
        return SBCtrl(kh, sb.clone() as StageBasis, r)
    }

    override fun update() {
        super.update()
        mir.add(this)
    }

    init {
        r = re
        mir = MirrorSet(r)
    }
}

internal open class Mirror : BattleField {
    var rl: Release

    constructor(sr: Mirror) : super(sr.sb.clone() as StageBasis) {
        rl = sr.rl.clone()
    }

    protected constructor(r: Recd) : super(EStage(r.st, r.star), r.lu, r.conf, r.seed) {
        rl = Release(r.action.translate())
    }

    /** process the user action  */
    override fun actions() {
        val rec = rl.get()
        if (rec and 1 > 0) act_mon()
        if (rec and 2 > 0) act_can()
        if (rec and 4 > 0) act_sniper()
        for (i in 0..1) for (j in 0..4) {
            if (rec and (1 shl i * 5 + j + 3) > 0) act_lock(i, j)
            act_spawn(i, j, rec and (1 shl i * 5 + j + 13) > 0)
        }
        sb.rx.add(rec)
    }
}

internal class MirrorSet(r: Recd) {
    private val mis: Array<Mirror?>
    private val len: Int
    val size: Int
    fun add(sb: SBRply) {
        val t = sb.sb.time
        if (t * size / len >= size) return
        if (mis[t * size / len] == null) mis[t * size / len] = common.battle.Mirror(sb)
    }

    fun getRaw(t: Int): Mirror? {
        val mr = mis[t]
        if (mr == null) {
            for (i in t - 1 downTo 0) if (mis[i] != null) return common.battle.Mirror(mis[i])
            return null
        }
        return common.battle.Mirror(mr)
    }

    fun getReal(t: Int): Mirror? {
        val m = getRaw(t * size / len)
        while (m!!.sb.time < t) m.update()
        return m
    }

    init {
        len = r.len + 1
        size = Math.sqrt(len.toDouble()).toInt()
        mis = arrayOfNulls(size)
    }
}

internal class Release {
    protected val recd: IntArray
    private var ind = 0
    private var rec = 0
    private var rex = 0

    constructor(`in`: InStream) {
        val n: Int = `in`.nextInt()
        recd = IntArray(n)
        for (i in 0 until n) recd[i] = `in`.nextInt()
    }

    private constructor(r: Release) {
        recd = r.recd
        ind = r.ind
        rec = r.rec
        rex = r.rex
    }

    override fun clone(): Release {
        return Release(this)
    }

    fun get(): Int {
        if (rex == 0) if (recd.size <= ind) rec = 0 else {
            rec = recd[ind++]
            rex = recd[ind++]
        }
        rex--
        return rec
    }
}
