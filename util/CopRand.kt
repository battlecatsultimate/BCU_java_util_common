package common.util

import java.util.*

class CopRand(private var seed: Long) : BattleObj() {
    fun irDouble(): Double {
        return Math.random()
    }

    fun nextDouble(): Double {
        val r = Random(seed)
        seed = r.nextLong()
        return r.nextDouble()
    }
}
