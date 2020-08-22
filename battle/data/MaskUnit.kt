package common.battle.data

import common.util.unit.Form

interface MaskUnit : MaskEntity {
    fun getBack(): Int
    fun getFront(): Int
    fun getOrb(): Orb?
    override fun getPack(): Form?
    fun getPrice(): Int
    fun getRespawn(): Int
}
