package common.battle.data

import common.battle.Basis
import common.io.InStream
import common.io.json.JsonClass
import common.io.json.JsonField
import common.util.Data
import common.util.unit.AbEnemy
import common.util.unit.Enemy
import java.util.*

@JsonClass
class CustomEnemy : CustomEntity(), MaskEnemy {
    var pack: Enemy? = null

    @JsonField
    var star = 0

    @JsonField
    var drop = 0
    fun copy(e: Enemy?): CustomEnemy {
        val ce = CustomEnemy()
        ce.importData(this)
        ce.pack = e
        return ce
    }

    fun fillData(ver: Int, `is`: InStream) {
        zread(ver, `is`)
    }

    override fun getDrop(): Double {
        return drop.toDouble()
    }

    override fun getPack(): Enemy? {
        return pack
    }

    override fun getStar(): Int {
        return star
    }

    override fun getSummon(): Set<AbEnemy> {
        val ans: MutableSet<AbEnemy> = TreeSet<AbEnemy>()
        for (adm in atks) if (adm.proc!!.SUMMON.prob > 0) ans.add(adm.proc!!.SUMMON.id.get() as AbEnemy)
        return ans
    }

    override fun importData(de: MaskEntity) {
        super.importData(de)
        if (de is MaskEnemy) {
            val me: MaskEnemy = de
            star = me.getStar()
            drop = me.getDrop() as Int
        }
    }

    override fun multi(b: Basis): Double {
        if (star > 0) return b.t().getStarMulti(star)
        return if (type and Data.Companion.TB_ALIEN > 0) b.t().getAlienMulti() else 1
    }

    private fun zread(`val`: Int, `is`: InStream) {
        var `val` = `val`
        `val` = Data.Companion.getVer(`is`.nextString())
        if (`val` >= 400) `zread$000400`(`is`)
    }

    private fun `zread$000400`(`is`: InStream) {
        zreada(`is`)
        star = `is`.nextByte()
        drop = `is`.nextInt()
    }

    init {
        rep = AtkDataModel(this)
        atks = arrayOfNulls<AtkDataModel>(1)
        atks.get(0) = AtkDataModel(this)
        width = 320
        speed = 8
        hp = 10000
        hb = 1
        type = 1
    }
}
