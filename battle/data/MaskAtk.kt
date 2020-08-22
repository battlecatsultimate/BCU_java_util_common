package common.battle.data

import common.util.BattleStatic
import common.util.Data
import common.util.Data.Proc

interface MaskAtk : BattleStatic {
    fun getAltAbi(): Int {
        return 0
    }

    fun getAtk(): Int
    fun getDire(): Int {
        return 1
    }

    fun getLongPoint(): Int
    fun getMove(): Int {
        return 0
    }

    fun getProc(): Proc?
    fun getShortPoint(): Int
    fun getTarget(): Int {
        return Data.Companion.TCH_N
    }

    fun isRange(): Boolean
    fun loopCount(): Int {
        return -1
    }
}
