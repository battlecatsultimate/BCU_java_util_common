package common.util

interface BattleStatic {
    /**
     * designed to prevent a class from extending BattleObj and implementing
     * BattleStatic
     */
    fun conflict() {}
}
