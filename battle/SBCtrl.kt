package common.battle

import common.CommonStatic.FakeKey
import common.io.OutStream
import common.util.BattleObj
import common.util.stage.EStage
import common.util.stage.Recd
import common.util.stage.Stageimport
import java.util.*

class SBCtrl : BattleField {
    private val keys: FakeKey
    val action: MutableList<Int> = ArrayList()
    val re: Recd

    constructor(kh: FakeKey, stage: Stage?, star: Int, bas: BasisLU, ints: IntArray, seed: Long) : super(EStage(stage, star), bas, ints, seed) {
        re = Recd(bas, stage, star, ints, seed)
        keys = kh
    }

    constructor(kh: FakeKey, sb: StageBasis, r: Recd) : super(sb) {
        keys = kh
        re = r.clone()
    }

    fun getData(): Recd {
        re.name = ""
        re.action = sb.rx.write()
        re.len = sb.time
        return re
    }

    /** process the user action  */
    override fun actions() {
        if (sb.ebase.health <= 0) return
        var rec = 0
        if ((keys.pressed(-1, 0) || action.contains(-1)) && act_mon()) {
            rec = rec or 1
            keys.remove(-1, 0)
        }
        if ((keys.pressed(-1, 1) || action.contains(-2)) && act_can()) {
            rec = rec or 2
            keys.remove(-1, 1)
        }
        if ((keys.pressed(-1, 2) || action.contains(-3)) && act_sniper()) {
            rec = rec or 4
            keys.remove(-1, 2)
        }
        for (i in 0..1) for (j in 0..4) {
            val b0: Boolean = keys.pressed(i, j)
            val b1 = action.contains(i * 5 + j)
            if (keys.pressed(-2, 0) || action.contains(10)) if (b0) {
                act_lock(i, j)
                rec = rec or (1 shl i * 5 + j + 3)
                keys.remove(i, j)
            } else if (b1) {
                act_lock(i, j)
                rec = rec or (1 shl i * 5 + j + 3)
                action.remove((i * 5 + j) as Any)
            }
            if (act_spawn(i, j, b0 || b1) && (b0 || b1)) rec = rec or (1 shl i * 5 + j + 13)
        }
        action.clear()
        sb.rx.add(rec)
    }
}

internal class Recorder : BattleObj() {
    private val recd: MutableList<Int> = ArrayList()
    private var num = 0
    private var rep = 0
    fun add(rec: Int) {
        if (rec == num) rep++ else {
            if (rep > 0) {
                recd.add(num)
                recd.add(rep)
            }
            num = rec
            rep = 1
        }
    }

    fun write(): OutStream {
        val os: OutStream = OutStream.Companion.getIns()
        if (rep > 0) {
            recd.add(num)
            recd.add(rep)
        }
        num = 0
        rep = 0
        os.writeInt(recd.size)
        for (i in recd) os.writeInt(i)
        os.terminate()
        return os
    }
}
