package common.battle.attack

import common.battle.entity.EAnimCont
import common.battle.entity.EEnemy
import common.battle.entity.EUnit
import common.battle.entity.Entity
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonEncoder
import common.io.json.Test
import common.io.json.Test.JsonTest_0.JsonD
import common.io.json.Test.JsonTest_2
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.util.BattleObj
import common.util.Data
import common.util.Data.Proc
import common.util.Data.Proc.SUMMON
import common.util.pack.EffAnim.DefEff
import common.util.stage.EStage
import common.util.stage.StageMap
import common.util.stage.StageMap.StageMapInfo
import common.util.unit.UnitLevel
import io.BCPlayer
import page.JL
import page.anim.AnimBox
import page.support.ListJtfPolicy
import page.support.SortTable
import page.view.ViewBox
import page.view.ViewBox.Conf
import page.view.ViewBox.Controller
import page.view.ViewBox.VBExporter

abstract class AtkModelEntity protected constructor(val e: Entity, private val ratk: Double) : AtkModelAb(e.basis) {
    protected val data: MaskEntity
    protected val atks: IntArray
    protected val abis: IntArray
    protected val act: IntArray
    protected val acs: Array<BattleObj?>
    private val sealed: Array<Proc?>
    override fun getAbi(): Int {
        return e.abi
    }

    /** get the attack, for display only  */
    fun getAtk(): Int {
        var ans = 0
        var temp = 0
        var c = 1
        val raw: Array<IntArray> = data.rawAtkData()
        for (i in raw.indices) if (raw[i][1] > 0) {
            ans += temp / c
            temp = if (data.getAtkModel(i).getDire() > 0) atks[i] else 0
            c = 1
        } else {
            temp += if (data.getAtkModel(i).getDire() > 0) atks[i] else 0
            c++
        }
        ans += temp / c
        return ans
    }

    /** generate attack entity  */
    fun getAttack(ind: Int): AttackAb? {
        if (act[ind] == 0) return null
        act[ind]--
        val proc: Proc = Proc.Companion.blank()
        val atk = getAttack(ind, proc)
        val ints = inRange(ind)
        return AttackSimple(this, atk, e.type, getAbi(), proc, ints[0], ints[1], e.data.getAtkModel(ind))
    }

    override fun getDire(): Int {
        return e.dire
    }

    override fun getPos(): Double {
        return e.pos
    }

    /** get the attack box for nth attack  */
    fun inRange(ind: Int): DoubleArray {
        val dire = e.dire
        val d0: Double
        val d1: Double
        d1 = e.pos
        d0 = d1
        if (!data.isLD() && !data.isOmni()) {
            d0 += data.getRange() * dire.toDouble()
            d1 -= data.getWidth() * dire.toDouble()
        } else {
            d0 += data.getAtkModel(ind).getShortPoint() * dire.toDouble()
            d1 += data.getAtkModel(ind).getLongPoint() * dire.toDouble()
        }
        return doubleArrayOf(d0, d1)
    }

    override fun invokeLater(atk: AttackAb, e: Entity) {
        val proc: SUMMON = atk.getProc().SUMMON
        if (proc.exists()) {
            val conf: Data.Proc.SUMMON.TYPE = proc.type
            if (conf.on_hit || conf.on_kill && e.health <= 0) summon(proc, e, e)
        }
    }

    /** get the collide box bound  */
    fun touchRange(): DoubleArray {
        val dire = e.dire
        val d0: Double
        val d1: Double
        d1 = e.pos
        d0 = d1
        d0 += data.getRange() * dire.toDouble()
        d1 -= data.getWidth() * dire.toDouble()
        return doubleArrayOf(d0, d1)
    }

    protected fun extraAtk(ind: Int) {
        if (data.getAtkModel(ind).getMove() != 0) e.pos += data.getAtkModel(ind).getMove() * e.dire.toDouble()
        if (data.getAtkModel(ind).getAltAbi() != 0) e.altAbi(data.getAtkModel(ind).getAltAbi())
        if (abis[ind] == 1) {
            if (b.r.nextDouble() * 100 < getProc(ind).TIME.prob) b.temp_s_stop = Math.max(b.temp_s_stop, getProc(ind).TIME.time)
            if (b.r.nextDouble() * 100 < getProc(ind).THEME.prob) b.changeTheme(getProc(ind).THEME.id, getProc(ind).THEME.time, getProc(ind).THEME.type)
        }
    }

    protected abstract fun getAttack(ind: Int, proc: Proc): Int
    protected abstract fun getBaseAtk(ind: Int): Int
    protected override fun getLayer(): Int {
        return e.layer
    }

    protected open fun getProc(ind: Int): Proc? {
        return if (e.status[Data.Companion.P_SEAL][0] > 0) sealed[ind] else data.getAtkModel(ind).getProc()
    }

    protected fun setProc(ind: Int, proc: Proc) {
        val par = arrayOf("CRIT", "WAVE", "KB", "WARP", "STOP", "SLOW", "WEAK", "POISON", "MOVEWAVE", "CURSE", "SNIPER",
                "BOSS", "SEAL", "BREAK", "SUMMON", "SATK", "POIATK", "VOLC", "ARMOR", "SPEED")
        for (s0 in par) if (getProc(ind).get(s0).perform(b.r)) if (s0 == "SUMMON") {
            val sprc: SUMMON = getProc(ind).SUMMON
            val conf: Data.Proc.SUMMON.TYPE = sprc.type
            if (!conf.on_hit && !conf.on_kill) summon(sprc, e, acs[ind]) else proc.SUMMON.set(sprc)
        } else proc.get(s0).set(getProc(ind).get(s0))
        if (proc.CRIT.exists() && proc.CRIT.mult == 0) proc.CRIT.mult = 200
        if (proc.IMUKB.exists() && proc.KB.dis == 0) proc.KB.dis = Data.Companion.KB_DIS.get(Data.Companion.INT_KB)
        if (proc.IMUKB.exists() && proc.KB.time == 0) proc.KB.time = Data.Companion.KB_TIME.get(Data.Companion.INT_KB)
        proc.POISON.damage *= ratk.toInt()
        if (proc.BOSS.exists()) b.lea.add(EAnimCont(e.pos, e.layer, Data.Companion.effas().A_SHOCKWAVE.getEAnim(DefEff.DEF)))
    }

    protected abstract fun summon(sprc: SUMMON, ent: Entity, acs: Any?)

    companion object {
        fun getIns(e: Entity, d0: Double): AtkModelEntity? {
            if (e is EEnemy) {
                val ee: EEnemy = e as EEnemy
                return AtkModelEnemy(ee, d0)
            }
            if (e is EUnit) {
                val eu: EUnit = e as EUnit
                return AtkModelUnit(eu, d0)
            }
            return null
        }
    }

    init {
        data = e.data
        val raw: Array<IntArray> = data.rawAtkData()
        atks = IntArray(raw.size + 2)
        abis = IntArray(raw.size + 2)
        act = IntArray(raw.size + 2)
        acs = arrayOfNulls<BattleObj>(raw.size + 2)
        for (i in raw.indices) {
            atks[i] = (raw[i][0] * ratk).toInt()
            abis[i] = raw[i][2]
            act[i] = data.getAtkModel(i).loopCount()
            acs[i] = BattleObj()
        }
        if (data.getRevenge() != null) {
            atks[raw.size] = (data.getRevenge().atk * ratk) as Int
            abis[raw.size] = 1
            acs[raw.size] = BattleObj()
            act[raw.size] = data.getRevenge().loopCount()
        }
        if (data.getResurrection() != null) {
            atks[raw.size + 1] = (data.getResurrection().atk * ratk) as Int
            abis[raw.size + 1] = 1
            acs[raw.size + 1] = BattleObj()
            act[raw.size + 1] = data.getResurrection().loopCount()
        }
        sealed = arrayOfNulls<Proc>(data.getAtkCount())
        for (i in sealed.indices) {
            sealed[i] = Proc.Companion.blank()
            sealed[i].MOVEWAVE.set(data.getAtkModel(i).getProc().MOVEWAVE)
        }
    }
}
