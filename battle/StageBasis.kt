package common.battle

import common.CommonStatic
import common.battle.attack.AttackAb
import common.battle.attack.ContAb
import common.battle.entity.*
import common.pack.PackData
import common.util.BattleObj
import common.util.CopRand
import common.util.Data
import common.util.pack.Background
import common.util.pack.EffAnim.DefEff
import common.util.stage.EStage
import common.util.stage.MapColc.DefMapColc
import common.util.stage.Stage
import common.util.unit.EForm
import common.util.unit.EneRand
import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate

class StageBasis(stage: EStage, bas: BasisLU, ints: IntArray, seed: Long) : BattleObj() {
    val b: BasisLU
    val st: Stage
    val est: EStage
    val elu: ELineUp
    val nyc: IntArray
    val locks = Array(2) { BooleanArray(5) }
    var ebase: AbEntity? = null
    val ubase: AbEntity
    val canon: Cannon
    var sniper: Sniper? = null
    val le: MutableList<Entity> = ArrayList()
    val tempe: MutableList<EntCont> = ArrayList<EntCont>()
    val lw: MutableList<ContAb> = ArrayList<ContAb>()
    val tlw: MutableList<ContAb> = ArrayList<ContAb>()
    val lea: MutableList<EAnimCont> = ArrayList<EAnimCont>()
    val rege: Set<EneRand> = HashSet<EneRand>()
    val conf: IntArray
    val r: CopRand
    val rx = Recorder()
    var work_lv: Int
    var max_mon = 0
    var can: Int
    var max_can: Int
    var next_lv: Int
    var max_num: Int
    var mon: Double
    var shock = false
    var time = 0
    var s_stop = 0
    var temp_s_stop = 0
    var bg: Background
    private val la: MutableList<AttackAb> = ArrayList<AttackAb>()
    private var lethal = false
    private var themeTime = 0
    private var theme: PackData.Identifier<Background>? = null
    private var themeType: Data.Proc.THEME.TYPE? = null
    fun changeTheme(id: PackData.Identifier<Background>?, time: Int, type: Data.Proc.THEME.TYPE?) {
        theme = id
        themeTime = time
        themeType = type
    }

    fun entityCount(d: Int): Int {
        var ans = 0
        for (i in le.indices) if (le[i].dire == d) ans++
        return ans
    }

    fun entityCount(d: Int, g: Int): Int {
        var ans = 0
        for (i in le.indices) if (le[i].dire == d && le[i].group == g) ans++
        return ans
    }

    /** receive attacks and excuse together, capture targets first  */
    fun getAttack(a: AttackAb?) {
        if (a == null) return
        a.capture()
        la.add(a)
    }

    /** the base that entity with this direction will attack  */
    fun getBase(dire: Int): AbEntity {
        return if (dire == 1) ubase else ebase
    }

    fun getEBHP(): Double {
        return 1.0 * ebase.health / ebase.maxH
    }

    /**
     * list of entities in the range of d0~d1 that can be touched by entity with
     * this direction and touch mode
     */
    fun inRange(touch: Int, dire: Int, d0: Double, d1: Double): List<AbEntity> {
        val ans: MutableList<AbEntity> = ArrayList<AbEntity>()
        if (dire == 0) return ans
        for (i in le.indices) if (le[i].dire * dire == -1 && le[i].touchable() and touch > 0 && (le[i].pos - d0) * (le[i].pos - d1) <= 0) ans.add(le[i])
        val b: AbEntity = if (dire == 1) ubase else ebase
        if (b.touchable() and touch > 0 && (b.pos - d0) * (b.pos - d1) <= 0) ans.add(b)
        return ans
    }

    fun act_can(): Boolean {
        if (can == max_can) {
            canon.activate()
            can = 0
            return true
        }
        return false
    }

    fun act_lock(i: Int, j: Int) {
        locks[i][j] = !locks[i][j]
    }

    fun act_mon(): Boolean {
        if (work_lv < 8 && mon > next_lv) {
            mon -= next_lv.toDouble()
            work_lv++
            next_lv = b.t().getLvCost(work_lv)
            max_mon = b.t().getMaxMon(work_lv)
            if (work_lv == 8) next_lv = -1
            return true
        }
        return false
    }

    fun act_sniper(): Boolean {
        if (sniper != null) {
            sniper.enabled = !sniper.enabled
            return true
        }
        return false
    }

    fun act_spawn(i: Int, j: Int, boo: Boolean): Boolean {
        if (ubase.health == 0L) return false
        if (elu.cool.get(i).get(j) > 0) return false
        if (elu.price.get(i).get(j) == -1) return false
        if (elu.price.get(i).get(j) > mon) return false
        if (locks[i][j] || boo) {
            if (entityCount(-1) >= max_num) return false
            val f: EForm = b.lu.efs.get(i).get(j) ?: return false
            elu.get(i, j)
            val eu: EUnit = f.getEntity(this)
            eu.added(-1, st.len - 700)
            le.add(eu)
            mon -= elu.price.get(i).get(j)
            return true
        }
        return false
    }

    protected override fun performDeepCopy() {
        super.performDeepCopy()
        for (er in rege) er.updateCopy(BattleObj.Companion.hardCopy(this) as StageBasis, BattleObj.Companion.hardCopy(er.map.get(this)))
    }

    /**
     * process actions and add enemies from stage first then update each entities
     * and receive attacks then excuse attacks and do post update then delete dead
     * entities
     */
    fun update() {
        tempe.removeIf(Predicate<EntCont> { e: EntCont ->
            if (e.t == 0) le.add(e.ent)
            e.t == 0
        })
        if (s_stop == 0) {
            val allow = st.max - entityCount(1)
            if (time % 2 == 1 && ebase.health > 0 && allow > 0) {
                val e: EEnemy = est.allow()
                if (e != null) {
                    e.added(1, if (e.mark == 1) 801 else 700)
                    le.add(e)
                }
            }
            elu.update()
            can++
            max_mon = b.t().getMaxMon(work_lv)
            mon += b.t().getMonInc(work_lv)
            est.update()
            if (shock) {
                for (i in le.indices) if (le[i].dire == -1 && le[i].touchable() and Data.Companion.TCH_N > 0) le[i].interrupt(Data.Companion.INT_SW, Data.Companion.KB_DIS.get(Data.Companion.INT_SW))
                lea.add(EAnimCont(700, 9, Data.Companion.effas().A_SHOCKWAVE.getEAnim(DefEff.DEF)))
                CommonStatic.setSE(Data.Companion.SE_BOSS)
                shock = false
            }
            ebase.update()
            canon.update()
            if (sniper != null) sniper.update()
            tempe.forEach(Consumer<EntCont> { ec: EntCont -> ec.update() })
        }
        for (i in le.indices) if (s_stop == 0 || le[i].abi and Data.Companion.AB_TIMEI != 0) le[i].update()
        if (s_stop == 0) {
            lw.forEach(Consumer<ContAb> { wc: ContAb -> wc.update() })
            lea.forEach(Consumer<EAnimCont> { e: EAnimCont -> e.update() })
            lw.addAll(tlw)
            tlw.clear()
        }
        la.forEach(Consumer<AttackAb> { a: AttackAb -> a.excuse() })
        la.clear()
        if (s_stop == 0) {
            ebase.postUpdate()
            if (!lethal && ebase.health <= 0 && est.hasBoss()) {
                lethal = true
                ebase.health = 1
            }
            if (ebase.health <= 0) for (i in le.indices) if (le[i].dire == 1) le[i].kill()
            if (ubase.health <= 0) for (i in le.indices) if (le[i].dire == -1) le[i].kill()
        }
        for (i in le.indices) if (s_stop == 0 || le[i].abi and Data.Companion.AB_TIMEI != 0) le[i].postUpdate()
        if (s_stop == 0) {
            le.removeIf { e: Entity -> e.anim.dead == 0 }
            lw.removeIf(Predicate<ContAb> { w: ContAb -> !w.activate })
            lea.removeIf(Predicate<EAnimCont> { a: EAnimCont -> a.done() })
        }
        updateTheme()
        if (s_stop > 0) s_stop--
        s_stop = Math.max(s_stop, temp_s_stop)
        temp_s_stop = 0
        can = Math.min(max_can, Math.max(0, can))
        mon = Math.min(max_mon.toDouble(), Math.max(0.0, mon))
    }

    private fun updateTheme() {
        if (theme != null) {
            bg = theme!!.get()
            if (themeType!!.kill) {
                le.removeIf { e: Entity -> e.abi and Data.Companion.AB_THEMEI == 0 }
                lw.clear()
                la.clear()
                tlw.clear()
                lea.clear()
                tempe.removeIf(Predicate<EntCont> { e: EntCont -> e.ent.getAbi() and Data.Companion.AB_THEMEI == 0 })
            }
            theme = null
        }
        if (s_stop == 0 && themeTime > 0) {
            themeTime--
            if (themeTime == 0) theme = st.bg
        }
    }

    companion object {
        var testing = true
    }

    init {
        b = bas
        r = CopRand(seed)
        nyc = bas.nyc
        est = stage
        st = est.s
        elu = ELineUp(bas.lu, this)
        est.assign(this)
        bg = st.bg.get()
        val ee: EEnemy = est.base(this)
        if (ee != null) ebase = ee else ebase = ECastle(this)
        ebase.added(1, 800)
        ubase = ECastle(this, bas)
        ubase.added(-1, st.len - 800)
        var sttime = 3
        if (st.map.mc === DefMapColc.Companion.getMap("CH")) {
            if (st.map.id == 9) sttime = Math.round(Math.log(est.mul) / Math.log(2.0)).toInt()
            if (st.map.id < 3) sttime = st.map.id
        }
        val max = if (est.lim != null) est.lim.num else 50
        max_num = if (max <= 0) 50 else max
        max_can = bas.t().CanonTime(sttime)
        work_lv = 1 + bas.getInc(Data.Companion.C_M_LV)
        mon = bas.getInc(Data.Companion.C_M_INI).toDouble()
        can = max_can * bas.getInc(Data.Companion.C_C_INI) / 100
        canon = Cannon(this, nyc[0])
        conf = ints
        if (conf[0] and 1 > 0) work_lv = 8
        if (conf[0] and 2 > 0) sniper = Sniper(this) else sniper = null
        next_lv = bas.t().getLvCost(work_lv)
    }
}
