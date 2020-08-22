package common.util.unit

import common.CommonStatic
import common.battle.StageBasis
import common.battle.entity.EEnemy
import common.io.InStream
import common.pack.PackData
import common.system.VImg
import common.util.Data
import common.util.EREnt
import common.util.EntRand
import java.util.*

class EneRand(ID: PackData.Identifier<AbEnemy>) : EntRand<PackData.Identifier<AbEnemy?>?>(), AbEnemy {
    val id: PackData.Identifier<AbEnemy>
    var name = ""
    fun fillPossible(se: MutableSet<Enemy>, sr: MutableSet<EneRand?>) {
        sr.add(this)
        for (e in list) {
            val ae: AbEnemy = e.ent.get()
            if (ae is Enemy) se.add(ae)
            if (ae is EneRand) {
                val er = ae
                if (!sr.contains(er)) er.fillPossible(se, sr)
            }
        }
    }

    override fun getEntity(sb: StageBasis, obj: Any?, mul: Double, mul2: Double, d0: Int, d1: Int, m: Int): EEnemy {
        sb.rege.add(this)
        return get(getSelection(sb, obj), sb, obj, mul, mul2, d0, d1, m)
    }

    override fun getIcon(): VImg {
        return CommonStatic.getBCAssets().ico.get(0).get(0)
    }

    override fun getID(): PackData.Identifier<AbEnemy>? {
        return id
    }

    override fun getPossible(): Set<Enemy> {
        val te: MutableSet<Enemy> = TreeSet<Enemy>()
        fillPossible(te, TreeSet<EneRand>())
        return te
    }

    override fun toString(): String {
        return id.id.toString() + " - " + name + " (" + id.pack + ")"
    }

    fun zread(`is`: InStream) {
        val ver: Int = Data.Companion.getVer(`is`.nextString())
        if (ver >= 400) `zread$000400`(`is`)
    }

    private operator fun get(x: EREnt<PackData.Identifier<AbEnemy>>, sb: StageBasis, obj: Any?, mul: Double, mul2: Double, d0: Int, d1: Int,
                             m: Int): EEnemy {
        return x.ent.get().getEntity(sb, obj, x.multi * mul / 100, x.multi * mul2 / 100, d0, d1, m)
    }

    private fun `zread$000400`(`is`: InStream) {
        name = `is`.nextString()
        type = `is`.nextInt()
        val n: Int = `is`.nextInt()
        for (i in 0 until n) {
            val ere: EREnt<PackData.Identifier<AbEnemy>> = EREnt<PackData.Identifier<AbEnemy>>()
            list.add(ere)
            ere.ent = PackData.Identifier.Companion.parseInt<AbEnemy>(`is`.nextInt(), EneRand::class.java)
            ere.multi = `is`.nextInt()
            ere.share = `is`.nextInt()
        }
    }

    init {
        id = ID
    }
}
