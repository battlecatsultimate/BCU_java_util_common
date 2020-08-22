package common.util.unit

import common.battle.StageBasis
import common.battle.entity.EEnemy
import common.pack.IndexContainer.IndexCont
import common.pack.IndexContainer.Indexable
import common.pack.PackData
import common.system.VImg

@IndexCont(PackData::class)
interface AbEnemy : Comparable<AbEnemy?>, Indexable<PackData?, AbEnemy?> {
    override operator fun compareTo(e: AbEnemy): Int {
        return getID()!!.compareTo(e.getID())
    }

    fun getEntity(sb: StageBasis, obj: Any?, mul: Double, mul1: Double, d0: Int, d1: Int, m: Int): EEnemy
    fun getIcon(): VImg
    override fun getID(): PackData.Identifier<AbEnemy>?
    fun getPossible(): Set<Enemy>
}
