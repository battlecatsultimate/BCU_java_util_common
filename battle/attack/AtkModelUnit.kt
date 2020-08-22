package common.battle.attack

import common.battle.BasisLU
import common.battle.entity.EUnit
import common.battle.entity.EntCont
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
import common.util.Data
import common.util.Data.Proc.SUMMON
import common.util.stage.EStage
import common.util.stage.StageMap
import common.util.stage.StageMap.StageMapInfo
import common.util.unit.EForm
import common.util.unit.Unit
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

class AtkModelUnit(ent: Entity, d0: Double) : AtkModelEntity(ent, d0) {
    private val bas: BasisLU
    private val buffed: Array<Proc?>
    override fun summon(proc: SUMMON, ent: Entity, acs: Any?) {
        val u = proc.id.get() as Unit
        val conf: Data.Proc.SUMMON.TYPE = proc.type
        val time: Int = proc.time
        if (u != null && (b.entityCount(-1) < b.max_num || conf.ignore_limit)) {
            val up: Double = ent.pos + getDire() * proc.dis
            val ef = EForm(u.forms[0], u.max)
            val eu: EUnit = ef.getEntity(b)
            if (conf.same_health) eu.health = e.health
            var l0 = 0
            var l1 = 9
            if (!conf.random_layer) {
                l1 = e.layer
                l0 = l1
            }
            eu.layer = (b.r.nextDouble() * (l1 - l0)) as Int + l0
            eu.added(-1, up.toInt())
            b.tempe.add(EntCont(eu, time))
            eu.setSummon(conf.anim_type)
        }
    }

    protected override fun getAttack(ind: Int, proc: Proc): Int {
        var atk: Int = atks.get(ind)
        if (abis.get(ind) == 1) {
            setProc(ind, proc)
            proc.KB.dis = proc.KB.dis * (100 + bas.getInc(Data.Companion.C_KB)) / 100
        }
        extraAtk(ind)
        if (e.status.get(Data.Companion.P_WEAK).get(0) > 0) atk = atk * e.status.get(Data.Companion.P_WEAK).get(1) / 100
        if (e.status.get(Data.Companion.P_STRONG).get(0) != 0) atk += atk * (e.status.get(Data.Companion.P_STRONG).get(0) + bas.getInc(Data.Companion.C_STRONG)) / 100
        return atk
    }

    protected override fun getBaseAtk(ind: Int): Int {
        return atks.get(ind)
    }

    protected override fun getProc(ind: Int): Proc? {
        return if (e.status.get(Data.Companion.P_SEAL).get(0) > 0) super.getProc(ind) else buffed[ind]
    }

    init {
        bas = ent.basis.b
        buffed = arrayOfNulls<Proc>(data.getAtkCount())
        for (i in buffed.indices) {
            buffed[i] = data.getAtkModel(i).getProc().clone()
            buffed[i].STOP.time *= (100 + bas.getInc(Data.Companion.C_STOP)) / 100
            buffed[i].SLOW.time *= (100 + bas.getInc(Data.Companion.C_SLOW)) / 100
            buffed[i].WEAK.time *= (100 + bas.getInc(Data.Companion.C_WEAK)) / 100
            if (buffed[i].CRIT.prob > 0) buffed[i].CRIT.prob += bas.getInc(Data.Companion.C_CRIT)
        }
    }
}
