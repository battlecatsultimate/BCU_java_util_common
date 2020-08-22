package common.battle.entity

import common.CommonStatic
import common.CommonStatic.BattleConst
import common.battle.StageBasis
import common.battle.attack.AtkModelEntity
import common.battle.attack.AttackAb
import common.system.P
import common.system.fake.FakeGraphics
import common.system.fake.FakeTransform
import common.util.BattleObj
import common.util.Data
import common.util.Data.Proc
import common.util.Data.Proc.POISON
import common.util.Data.Proc.PT
import common.util.anim.AnimU.UType
import common.util.anim.EAnimD
import common.util.anim.EAnimU
import common.util.pack.EffAnim
import common.util.pack.EffAnim.*
import common.util.pack.Soul
import common.util.pack.Soul.SoulType
import java.util.*
import java.util.function.Predicate

/** Entity class for units and enemies  */
abstract class Entity protected constructor(b: StageBasis, de: MaskEntity, ea: EAnimU, d0: Double, d1: Double) : AbEntity(if (d1 < 0) b.st.health else (de.getHp() * d1)) {
    class AnimManager(private val e: Entity, ea: EAnimU) : BattleObj() {
        private val status: Array<IntArray>

        /**
         * dead FSM time <br></br>
         * -1 means not dead<br></br>
         * positive value means time remain for death anim to play
         */
        var dead = -1

        /** KB anim, null means not being KBed, can have various value during battle  */
        private var back: EAnimD<KBEff>? = null

        /** entity anim  */
        private val anim: EAnimU

        /** corpse anim  */
        val corpse: EAnimD<ZombieEff>? = null

        /** soul anim, null means not dead yet  */
        private var soul: EAnimD<SoulType>? = null

        /** responsive effect FSM time  */
        private var efft = 0

        /** responsive effect FSM type  */
        private var eftp = 0

        /**
         * on-entity effect icons<br></br>
         * index defined by Data.A_()
         */
        private val effs: Array<EAnimD<*>?> = arrayOfNulls<EAnimD<*>?>(Data.Companion.A_TOT)

        /** draw this entity  */
        fun draw(gra: FakeGraphics, p: P?, siz: Double) {
            if (dead > 0) {
                soul.draw(gra, p, siz)
                return
            }
            val at: FakeTransform = gra.getTransform()
            if (corpse != null) corpse.draw(gra, p, siz)
            if (corpse == null || status[Data.Companion.P_REVIVE][1] < Data.Companion.REVIVE_SHOW_TIME) {
                if (corpse != null) {
                    gra.setTransform(at)
                    anim.changeAnim(UType.WALK)
                }
            } else {
                gra.delete(at)
                return
            }
            anim.paraTo(back)
            if (e.kbTime == 0 || e.kb.kbType != Data.Companion.INT_WARP) anim.draw(gra, p, siz)
            anim.paraTo(null)
            gra.setTransform(at)
            if (CommonStatic.getConfig().ref) e.drawAxis(gra, p, siz)
            gra.delete(at)
        }

        /** draw the effect icons  */
        fun drawEff(g: FakeGraphics, p: P, siz: Double) {
            if (dead != -1) return
            if (status[Data.Companion.P_WARP][2] != 0) return
            val at: FakeTransform = g.getTransform()
            val EWID = 48
            var x: Double = p.x
            if (effs[eftp] != null) {
                effs[eftp].draw(g, p, siz)
            }
            for (eae in effs) {
                if (eae == null) continue
                g.setTransform(at)
                eae.draw(g, P(x, p.y), siz)
                x -= EWID * e.dire * siz
            }
            g.delete(at)
        }

        /** get a effect icon  */
        fun getEff(t: Int) {
            val dire = e.dire
            if (t == Data.Companion.INV) {
                effs[eftp] = null
                eftp = Data.Companion.A_EFF_INV
                effs[eftp] = Data.Companion.effas().A_EFF_INV.getEAnim(DefEff.DEF)
                efft = Data.Companion.effas().A_EFF_INV.len(DefEff.DEF)
            }
            if (t == Data.Companion.P_WAVE) {
                val id: Int = if (dire == -1) Data.Companion.A_WAVE_INVALID else Data.Companion.A_E_WAVE_INVALID
                val eff: EffAnim<DefEff> = if (dire == -1) Data.Companion.effas().A_WAVE_INVALID else Data.Companion.effas().A_E_WAVE_INVALID
                effs[id] = eff.getEAnim(DefEff.DEF)
                status[Data.Companion.P_WAVE][0] = eff.len(DefEff.DEF)
            }
            if (t == Data.Companion.STPWAVE) {
                effs[eftp] = null
                eftp = if (dire == -1) Data.Companion.A_WAVE_STOP else Data.Companion.A_E_WAVE_STOP
                val eff: EffAnim<DefEff> = if (dire == -1) Data.Companion.effas().A_WAVE_STOP else Data.Companion.effas().A_E_WAVE_STOP
                effs[eftp] = eff.getEAnim(DefEff.DEF)
                efft = eff.len(DefEff.DEF)
            }
            if (t == Data.Companion.INVWARP) {
                effs[eftp] = null
                eftp = if (dire == -1) Data.Companion.A_FARATTACK else Data.Companion.A_E_FARATTACK
                val eff: EffAnim<DefEff> = if (dire == -1) Data.Companion.effas().A_FARATTACK else Data.Companion.effas().A_E_FARATTACK
                effs[eftp] = eff.getEAnim(DefEff.DEF)
                efft = eff.len(DefEff.DEF)
            }
            if (t == Data.Companion.P_STOP) {
                val id: Int = if (dire == -1) Data.Companion.A_STOP else Data.Companion.A_E_STOP
                effs[id] = (if (dire == -1) Data.Companion.effas().A_STOP else Data.Companion.effas().A_E_STOP).getEAnim(DefEff.DEF)
            }
            if (t == Data.Companion.P_IMUATK) {
                effs[Data.Companion.A_IMUATK] = Data.Companion.effas().A_IMUATK.getEAnim(DefEff.DEF)
            }
            if (t == Data.Companion.P_SLOW) {
                val id: Int = if (dire == -1) Data.Companion.A_SLOW else Data.Companion.A_E_SLOW
                effs[id] = (if (dire == -1) Data.Companion.effas().A_SLOW else Data.Companion.effas().A_E_SLOW).getEAnim(DefEff.DEF)
            }
            if (t == Data.Companion.P_WEAK) {
                if (status[Data.Companion.P_WEAK][1] <= 100) {
                    val id: Int = if (dire == -1) Data.Companion.A_DOWN else Data.Companion.A_E_DOWN
                    effs[id] = (if (dire == -1) Data.Companion.effas().A_DOWN else Data.Companion.effas().A_E_DOWN).getEAnim(DefEff.DEF)
                } else {
                    val id: Int = if (dire == -1) Data.Companion.A_WEAK_UP else Data.Companion.A_E_WEAK_UP
                    effs[id] = (if (dire == -1) Data.Companion.effas().A_WEAK_UP else Data.Companion.effas().A_E_WEAK_UP).getEAnim(WeakUpEff.UP)
                }
            }
            if (t == Data.Companion.P_CURSE) {
                val id: Int = if (dire == -1) Data.Companion.A_CURSE else Data.Companion.A_E_CURSE
                effs[id] = (if (dire == -1) Data.Companion.effas().A_CURSE else Data.Companion.effas().A_E_CURSE).getEAnim(DefEff.DEF)
            }
            if (t == Data.Companion.P_POISON) {
                val mask = status[Data.Companion.P_POISON][0]
                val arr: Array<EffAnim<*>> = arrayOf<EffAnim<*>>(Data.Companion.effas().A_POI0, Data.Companion.effas().A_POI1, Data.Companion.effas().A_POI2, Data.Companion.effas().A_POI3, Data.Companion.effas().A_POI4,
                        Data.Companion.effas().A_POI5, Data.Companion.effas().A_POI6, Data.Companion.effas().A_POI7)
                for (i in Data.Companion.A_POIS.indices) if (mask and (1 shl i) > 0) {
                    val id: Int = Data.Companion.A_POIS.get(i)
                    effs[id] = (arr[id] as EffAnim<DefEff?>).getEAnim(DefEff.DEF)
                }
            }
            if (t == Data.Companion.P_SEAL) {
                val id: Int = Data.Companion.A_SEAL
                effs[id] = Data.Companion.effas().A_SEAL.getEAnim(DefEff.DEF)
            }
            if (t == Data.Companion.P_STRONG) {
                val id: Int = if (dire == -1) Data.Companion.A_UP else Data.Companion.A_E_UP
                effs[id] = (if (dire == -1) Data.Companion.effas().A_UP else Data.Companion.effas().A_E_UP).getEAnim(DefEff.DEF)
            }
            if (t == Data.Companion.P_LETHAL) {
                val id: Int = if (dire == -1) Data.Companion.A_SHIELD else Data.Companion.A_E_SHIELD
                val ea: EffAnim<DefEff> = if (dire == -1) Data.Companion.effas().A_SHIELD else Data.Companion.effas().A_E_SHIELD
                status[Data.Companion.P_LETHAL][1] = ea.len(DefEff.DEF)
                effs[id] = ea.getEAnim(DefEff.DEF)
                CommonStatic.setSE(Data.Companion.SE_LETHAL)
            }
            if (t == Data.Companion.P_WARP) {
                val ea: EffAnim<WarpEff> = Data.Companion.effas().A_W
                val ind = status[Data.Companion.P_WARP][2]
                val pa: WarpEff = if (ind == 0) WarpEff.ENTER else WarpEff.EXIT
                e.basis.lea.add(WaprCont(e.pos, pa, e.layer, anim, e.dire))
                CommonStatic.setSE(if (ind == 0) Data.Companion.SE_WARP_ENTER else Data.Companion.SE_WARP_EXIT)
                status[Data.Companion.P_WARP][ind] = ea.len(pa)
            }
            if (t == Data.Companion.BREAK_ABI) {
                val id: Int = if (dire == -1) Data.Companion.A_U_E_B else Data.Companion.A_E_B
                effs[id] = (if (dire == -1) Data.Companion.effas().A_U_E_B else Data.Companion.effas().A_E_B).getEAnim(BarEneEff.BREAK)
                status[Data.Companion.P_BREAK][0] = effs[id].len()
                CommonStatic.setSE(Data.Companion.SE_BARRIER_ABI)
            }
            if (t == Data.Companion.BREAK_ATK) {
                val id: Int = if (dire == -1) Data.Companion.A_U_E_B else Data.Companion.A_E_B
                effs[id] = (if (dire == -1) Data.Companion.effas().A_U_E_B else Data.Companion.effas().A_E_B).getEAnim(BarEneEff.DESTR)
                status[Data.Companion.P_BREAK][0] = effs[id].len()
                CommonStatic.setSE(Data.Companion.SE_BARRIER_ATK)
            }
            if (t == Data.Companion.BREAK_NON) {
                val id: Int = if (dire == -1) Data.Companion.A_U_B else Data.Companion.A_B
                effs[id] = (if (dire == -1) Data.Companion.effas().A_U_B else Data.Companion.effas().A_B).getEAnim(BarrierEff.END)
                status[Data.Companion.P_BREAK][0] = effs[id].len()
                CommonStatic.setSE(Data.Companion.SE_BARRIER_NON)
            }
            if (t == Data.Companion.P_ARMOR) {
                val id: Int = if (dire == -1) Data.Companion.A_ARMOR else Data.Companion.A_E_ARMOR
                val eff: EffAnim<ArmorEff> = if (dire == -1) Data.Companion.effas().A_ARMOR else Data.Companion.effas().A_E_ARMOR
                val index: ArmorEff = if (status[Data.Companion.P_ARMOR][1] >= 0) ArmorEff.DEBUFF else ArmorEff.BUFF
                effs[id] = eff.getEAnim(index)
            }
            if (t == Data.Companion.P_SPEED) {
                val id: Int = if (dire == -1) Data.Companion.A_SPEED else Data.Companion.A_E_SPEED
                val eff: EffAnim<SpeedEff> = if (dire == -1) Data.Companion.effas().A_SPEED else Data.Companion.effas().A_E_SPEED
                val index: SpeedEff
                index = if (status[Data.Companion.P_SPEED][2] <= 1) {
                    if (status[Data.Companion.P_SPEED][1] >= 0) SpeedEff.UP else SpeedEff.DOWN
                } else {
                    if (status[Data.Companion.P_SPEED][1] >= e.data.speed) SpeedEff.UP else SpeedEff.DOWN
                }
                effs[id] = eff.getEAnim(index)
            }
        }

        /** update effect icons animation  */
        private fun checkEff() {
            val dire = e.dire
            if (efft == 0) effs[eftp] = null
            if (status[Data.Companion.P_STOP][0] == 0) {
                val id: Int = if (dire == -1) Data.Companion.A_STOP else Data.Companion.A_E_STOP
                effs[id] = null
            }
            if (status[Data.Companion.P_SLOW][0] == 0) {
                val id: Int = if (dire == -1) Data.Companion.A_SLOW else Data.Companion.A_E_SLOW
                effs[id] = null
            }
            if (status[Data.Companion.P_WEAK][0] == 0) {
                val id: Int
                id = if (status[Data.Companion.P_WEAK][1] <= 100) {
                    if (dire == -1) Data.Companion.A_DOWN else Data.Companion.A_E_DOWN
                } else {
                    if (dire == -1) Data.Companion.A_WEAK_UP else Data.Companion.A_E_WEAK_UP
                }
                effs[id] = null
            }
            if (status[Data.Companion.P_CURSE][0] == 0) {
                val id: Int = if (dire == -1) Data.Companion.A_CURSE else Data.Companion.A_E_CURSE
                effs[id] = null
            }
            if (status[Data.Companion.P_IMUATK][0] == 0) {
                val id: Int = Data.Companion.A_IMUATK
                effs[id] = null
            }
            if (status[Data.Companion.P_POISON][0] == 0) {
                val id: Int = Data.Companion.A_POI0
                effs[id] = null
            }
            if (status[Data.Companion.P_SEAL][0] == 0) {
                val id: Int = Data.Companion.A_SEAL
                effs[id] = null
            }
            if (status[Data.Companion.P_LETHAL][1] == 0) {
                val id: Int = if (dire == -1) Data.Companion.A_SHIELD else Data.Companion.A_E_SHIELD
                effs[id] = null
            } else status[Data.Companion.P_LETHAL][1]--
            if (status[Data.Companion.P_WAVE][0] == 0) {
                val id: Int = if (dire == -1) Data.Companion.A_WAVE_INVALID else Data.Companion.A_E_WAVE_INVALID
                effs[id] = null
            } else status[Data.Companion.P_WAVE][0]--
            if (status[Data.Companion.P_STRONG][0] == 0) {
                val id: Int = if (dire == -1) Data.Companion.A_UP else Data.Companion.A_E_UP
                effs[id] = null
            }
            if (status[Data.Companion.P_BREAK][0] == 0) {
                val id: Int = if (dire == -1) Data.Companion.A_U_B else Data.Companion.A_B
                effs[id] = null
            } else status[Data.Companion.P_BREAK][0]--
            if (status[Data.Companion.P_ARMOR][0] == 0) {
                val id: Int = if (dire == -1) Data.Companion.A_ARMOR else Data.Companion.A_E_ARMOR
                effs[id] = null
            }
            if (status[Data.Companion.P_SPEED][0] == 0) {
                val id: Int = if (dire == -1) Data.Companion.A_SPEED else Data.Companion.A_E_SPEED
                effs[id] = null
            }
            efft--
        }

        /**
         * process kb animation <br></br>
         * called when kb is applied
         */
        private fun kbAnim() {
            val t = e.kb.kbType
            if (t != Data.Companion.INT_SW && t != Data.Companion.INT_WARP) setAnim(UType.HB) else {
                setAnim(UType.WALK)
                anim.update(false)
            }
            if (t == Data.Companion.INT_WARP) {
                e.kbTime = status[Data.Companion.P_WARP][0]
                getEff(Data.Companion.P_WARP)
                status[Data.Companion.P_WARP][2] = 1
            }
            if (t == Data.Companion.INT_KB) e.kbTime = status[Data.Companion.P_KB][0]
            if (t == Data.Companion.INT_HB) back = Data.Companion.effas().A_KB.getEAnim(KBEff.KB)
            if (t == Data.Companion.INT_SW) back = Data.Companion.effas().A_KB.getEAnim(KBEff.SW)
            if (t == Data.Companion.INT_ASS) back = Data.Companion.effas().A_KB.getEAnim(KBEff.ASS)

            // Z-kill icon
            if (e.health <= 0 && e.zx.tempZK && e.zx.canRevive() > 0) {
                val eae: EAnimD<DefEff> = Data.Companion.effas().A_Z_STRONG.getEAnim(DefEff.DEF)
                e.basis.lea.add(EAnimCont(e.pos, e.layer, eae))
                CommonStatic.setSE(Data.Companion.SE_ZKILL)
            }
        }

        /** set kill anim  */
        fun kill() {
            if (e.abi and Data.Companion.AB_GLASS != 0) {
                dead = 0
                return
            }
            val s: Soul? = e.data.deathAnim.get()
            dead = if (s == null) 0 else s.getEAnim(SoulType.DEF).also { soul = it }.len()
        }

        fun setAnim(t: UType): Int {
            if (anim.type != t) anim.changeAnim(t)
            return anim.len()
        }

        fun update() {
            checkEff()
            for (i in effs.indices) if (effs[i] != null) effs[i].update(false)
            if (status[Data.Companion.P_STOP][0] == 0 && (e.kbTime == 0 || e.kb.kbType != Data.Companion.INT_SW)) anim.update(false)
            if (back != null) back.update(false)
            if (dead > 0) {
                soul.update(false)
                dead--
            }
            if (e.data.resurrection != null && dead >= 0) {
                val adm: AtkDataModel = e.data.resurrection
                if (soul == null || adm.pre == soul.len() - dead) e.basis.getAttack(e.aam.getAttack(e.data.atkCount + 1))
            }
        }

        init {
            anim = ea
            status = e.status
        }
    }

    private class AtkManager(private val e: Entity) : BattleObj() {
        /** atk FSM time  */
        var atkTime = 0

        /** attack times remain  */
        var loop: Int

        /** atk id primarily for display  */
        var tempAtk = -1

        /** const field, attack count  */
        private var multi = 0

        /** atk loop FSM type  */
        private var preID = 0

        /** pre-atk time const field  */
        private val pres: IntArray

        /** atk loop FSM time  */
        private var preTime = 0
        fun setUp() {
            atkTime = e.data.animLen
            loop--
            preID = 0
            preTime = pres[0]
            e.anim.setAnim(UType.ATK)
        }

        fun stopAtk() {
            if (atkTime > 0) {
                atkTime = 0
                preTime = 0
                if (preID == multi) e.waitTime = e.data.tba
            }
        }

        /** update attack state  */
        fun updateAttack() {
            atkTime--
            if (preTime >= 0) {
                if (preTime == 0) {
                    val atk0 = preID
                    while (++preID < multi && pres[preID] == 0);
                    tempAtk = (atk0 + e.basis.r.nextDouble() * (preID - atk0)).toInt()
                    e.basis.getAttack(e.aam.getAttack(tempAtk))
                    if (preID < multi) preTime = pres[preID]
                }
                preTime--
            }
            if (atkTime == 0) {
                e.waitTime = e.data.tba
                e.anim.setAnim(UType.ATK)
            }
        }

        init {
            val raw = e.data.rawAtkData()
            pres = IntArray(raw.size.also { multi = it })
            for (i in 0 until multi) pres[i] = raw[i][1]
            loop = e.data.atkLoop
        }
    }

    private class KBManager(private val e: Entity) : BattleObj() {
        /** KB FSM type  */
        var kbType = 0

        /** remaining distance to KB  */
        private var kbDis = 0.0

        /** temp field to store wanted KB length  */
        private var tempKBdist = 0.0

        /** temp field to store wanted KB type  */
        private var tempKBtype = -1
        private var initPos = 0.0
        private var kbDuration = 0.0
        private var time = 1.0

        /** process the interruption received  */
        fun doInterrupt() {
            val t = tempKBtype
            if (t == -1) return
            val d = tempKBdist
            tempKBtype = -1
            e.clearState()
            kbType = t
            e.kbTime = Data.Companion.KB_TIME.get(t)
            kbDis = d
            initPos = e.pos
            kbDuration = e.kbTime.toDouble()
            time = 1.0
            e.anim.kbAnim()
        }

        private fun easeOut(time: Double, start: Double, end: Double, duration: Double, dire: Double): Double {
            var time = time
            time /= duration
            return -end * time * (time - 2) * dire + start
        }

        fun interrupt(t: Int, d: Double) {
            if (t == Data.Companion.INT_ASS && e.abi and Data.Companion.AB_SNIPERI > 0) {
                e.anim.getEff(Data.Companion.INV)
                return
            }
            if (t == Data.Companion.INT_SW && e.abi and Data.Companion.AB_IMUSW > 0) {
                e.anim.getEff(Data.Companion.INV)
                return
            }
            val prev = tempKBtype
            if (prev == -1 || Data.Companion.KB_PRI.get(t) >= Data.Companion.KB_PRI.get(prev)) {
                tempKBtype = t
                tempKBdist = d
            }
        }

        private fun kbmove(mov: Double) {
            if (mov < 0) e.updateMove(-mov, -mov) else {
                val lim = e.getLim()
                e.pos -= (if (mov < lim) mov else lim) * e.dire
            }
        }

        /**
         * update KB state <br></br>
         * in KB state: deal with warp, KB go back, and anim change <br></br>
         * end of KB: check whether it's killed, deal with revive
         */
        fun updateKB() {
            if (kbType != Data.Companion.INT_WARP && kbType != Data.Companion.INT_KB) {
                val mov = kbDis / e.kbTime
                kbDis -= mov
                kbmove(mov)
            } else if (kbType == Data.Companion.INT_KB) {
                if (time == 1.0) {
                    kbDuration = e.kbTime.toDouble()
                }
                var mov = easeOut(time, initPos, kbDis, kbDuration, -e.dire.toDouble()) - e.pos
                mov *= -e.dire.toDouble()
                kbmove(mov)
                time++
            } else {
                e.anim.setAnim(UType.WALK)
                if (e.status[Data.Companion.P_WARP][0] > 0) e.status[Data.Companion.P_WARP][0]--
                if (e.status[Data.Companion.P_WARP][1] > 0) e.status[Data.Companion.P_WARP][1]--
                val ea: EffAnim<WarpEff> = Data.Companion.effas().A_W
                if (e.kbTime == ea.len(WarpEff.EXIT)) {
                    kbmove(kbDis)
                    kbDis = 0.0
                    e.anim.getEff(Data.Companion.P_WARP)
                    e.status[Data.Companion.P_WARP][2] = 0
                }
            }
            if (kbType == Data.Companion.INT_HB && e.data.revenge != null) if (Data.Companion.KB_TIME.get(Data.Companion.INT_HB) - e.kbTime == e.data.revenge.pre) e.basis.getAttack(e.aam.getAttack(e.data.atkCount))
            e.kbTime--
            if (e.kbTime == 0) {
                e.anim.back = null
                e.anim.setAnim(UType.WALK)
                kbDuration = 0.0
                initPos = 0.0
                time = 1.0
                if (e.health <= 0) e.preKill()
            }
        }
    }

    private class PoisonToken(private val e: Entity) : BattleObj() {
        private val list: MutableList<POISON> = ArrayList<POISON>()
        fun add(ws: POISON) {
            if (ws.type.unstackable) list.removeIf(Predicate<POISON> { e: POISON -> e.type.unstackable && type(e) == type(ws) })
            ws.prob = 0 // used as counter
            list.add(ws)
            getMax()
        }

        private fun damage(dmg: Int, type: Int) {
            var type = type
            type = type and 7
            val mul = if (type == 0) 100 else if (type == 1) e.maxH else if (type == 2) e.health else e.maxH - e.health
            e.damage += mul * dmg / 100
        }

        private fun getMax() {
            var max = 0
            for (i in list.indices) max = max or (1 shl type(list[i]))
            e.status[Data.Companion.P_POISON][0] = max
        }

        private fun type(ws: POISON): Int {
            return ws.type.damage_type + if (ws.damage < 0) 4 else 0
        }

        fun update() {
            for (i in list.indices) {
                val ws: POISON = list[i]
                if (ws.time > 0) {
                    ws.time--
                    ws.prob-- // used as counter for itv
                    if (e.health > 0 && ws.prob <= 0) {
                        damage(ws.damage, type(ws))
                        ws.prob += ws.itv
                    }
                }
            }
            list.removeIf(Predicate<POISON> { w: POISON -> w.time <= 0 })
            getMax()
        }
    }

    private class WeakToken(private val e: Entity) : BattleObj() {
        private val list: MutableList<IntArray> = ArrayList()
        fun add(`is`: IntArray) {
            list.add(`is`)
            getMax()
        }

        private fun getMax() {
            var max = 0
            var `val` = if (list.isEmpty()) 100 else list[0][1]
            for (i in list.indices) {
                val ws = list[i]
                max = Math.max(max, ws[0])
                `val` = Math.min(`val`, ws[1])
            }
            e.status[Data.Companion.P_WEAK][0] = max
            val ov = e.status[Data.Companion.P_WEAK][1].toDouble()
            e.status[Data.Companion.P_WEAK][1] = `val`
            if (ov > 100 && `val` <= 100) {
                if (e.dire == -1) {
                    e.anim.effs[Data.Companion.A_WEAK_UP] = null
                } else {
                    e.anim.effs[Data.Companion.A_E_WEAK_UP] = null
                }
            }
        }

        fun update() {
            for (i in list.indices) list[i][0]--
            list.removeIf { w: IntArray -> w[0] <= 0 }
            getMax()
        }
    }

    private class ZombX(private val e: Entity) : BattleObj() {
        private val list: MutableSet<Entity> = HashSet()

        /** temp field: marker for zombie killer  */
        private var tempZK = false
        private var extraRev = 0
        private fun canRevive(): Int {
            if (e.status[Data.Companion.P_REVIVE][0] != 0) return 1
            val tot = totExRev()
            return if (tot == -1 || tot > extraRev) 2 else 0
        }

        private fun canZK(): Boolean {
            if (e.proc.REVIVE.type.imu_zkill) return false
            for (zx in list) if (zx.proc.REVIVE.type.imu_zkill) return false
            return true
        }

        fun damaged(atk: AttackAb) {
            tempZK = tempZK or (atk.abi and Data.Companion.AB_ZKILL > 0 && canZK())
        }

        private fun doRevive(c: Int) {
            if (c == 1) e.status[Data.Companion.P_REVIVE][0]-- else if (c == 2) extraRev++
            var deadAnim = minRevTime()
            val ea: EffAnim<ZombieEff> = Data.Companion.effas().A_ZOMBIE
            deadAnim += ea.getEAnim(ZombieEff.REVIVE).len()
            e.status[Data.Companion.P_REVIVE][1] = deadAnim
            e.health = e.maxH * maxRevHealth() / 100
        }

        private fun maxRevHealth(): Int {
            var max = e.proc.REVIVE.health
            if (e.status[Data.Companion.P_REVIVE][0] == 0) max = 0
            for (zx in list) {
                val `val` = zx.proc.REVIVE.health
                max = Math.max(max, `val`)
            }
            return max
        }

        private fun minRevTime(): Int {
            var min = e.proc.REVIVE.time
            if (e.status[Data.Companion.P_REVIVE][0] == 0) min = Int.MAX_VALUE
            for (zx in list) {
                val `val` = zx.proc.REVIVE.time
                min = Math.min(min, `val`)
            }
            return min
        }

        fun postUpdate() {
            if (e.health > 0) tempZK = false
        }

        fun prekill(): Boolean {
            val c = canRevive()
            if (!tempZK && c > 0) {
                val status = e.status
                doRevive(c)
                // clear state
                e.waitTime = 0
                e.bdist = 0.0
                status[Data.Companion.P_BURROW][2] = 0
                status[Data.Companion.P_STOP] = IntArray(Data.Companion.PROC_WIDTH)
                status[Data.Companion.P_SLOW] = IntArray(Data.Companion.PROC_WIDTH)
                status[Data.Companion.P_WEAK] = IntArray(Data.Companion.PROC_WIDTH)
                status[Data.Companion.P_CURSE] = IntArray(Data.Companion.PROC_WIDTH)
                status[Data.Companion.P_SEAL] = IntArray(Data.Companion.PROC_WIDTH)
                status[Data.Companion.P_STRONG] = IntArray(Data.Companion.PROC_WIDTH)
                status[Data.Companion.P_LETHAL] = IntArray(Data.Companion.PROC_WIDTH)
                status[Data.Companion.P_POISON] = IntArray(Data.Companion.PROC_WIDTH)
                return true
            }
            return false
        }

        private fun totExRev(): Int {
            var sum = 0
            for (zx in list) {
                val `val` = zx.proc.REVIVE.count
                if (`val` == -1) return -1
                sum += `val`
            }
            return sum
        }

        /** update revive status  */
        fun updateRevive() {
            val status = e.status
            val anim: AnimManager = e.anim
            list.removeIf { em: Entity ->
                val conf = em.proc.REVIVE.type.range_type
                if (conf == 3) return@removeIf false
                if (conf == 2 || em.kbTime == -1) return@removeIf em.kbTime == -1
                true
            }
            val lm: List<AbEntity> = e.basis.inRange(Data.Companion.TCH_ZOMBX, -e.dire, 0.0, e.basis.st.len.toDouble())
            for (i in lm.indices) {
                if (lm[i] === e) continue
                val em = lm[i] as Entity
                val d0 = em.pos + em.proc.REVIVE.dis_0
                val d1 = em.pos + em.proc.REVIVE.dis_1
                if ((d0 - e.pos) * (d1 - e.pos) > 0) continue
                val conf = em.proc.REVIVE.type
                if (!conf.revive_non_zombie && e.type and Data.Companion.TB_ZOMBIE == 0) continue
                val type = conf.range_type
                if (type == 0 && em.touchable() and (Data.Companion.TCH_N or Data.Companion.TCH_EX) == 0) continue
                list.add(em)
            }
            if (status[Data.Companion.P_REVIVE][1] > 0) {
                e.acted = true
                val ea: EffAnim<ZombieEff> = if (e.dire == -1) Data.Companion.effas().A_U_ZOMBIE else Data.Companion.effas().A_ZOMBIE
                if (anim.corpse == null) anim.corpse = ea.getEAnim(ZombieEff.DOWN)
                if (status[Data.Companion.P_REVIVE][1] == ea.getEAnim(ZombieEff.REVIVE).len()) anim.corpse = ea.getEAnim(ZombieEff.REVIVE)
                status[Data.Companion.P_REVIVE][1]--
                if (anim.corpse != null) anim.corpse.update(false)
                if (status[Data.Companion.P_REVIVE][1] == 0) anim.corpse = null
            }
        }
    }

    val anim: AnimManager
    private val atkm: AtkManager
    private val zx = ZombX(this)

    /** game engine, contains environment configuration  */
    val basis: StageBasis

    /** entity data, read only  */
    val data: MaskEntity

    /** group, used for search  */
    var group = 0
    private val kb = KBManager(this)

    /** layer of display, constant field  */
    var layer = 0

    /** proc status, contains ability-specific status data  */
    val status = Array(Data.Companion.PROC_TOT) { IntArray(Data.Companion.PROC_WIDTH) }

    /** trait of enemy, also target trait of unit, use bitmask  */
    var type = 0

    /** attack model  */
    protected val aam: AtkModelEntity

    /** temp field: damage accumulation  */
    private var damage: Long = 0

    /** const field  */
    var isBase = false

    /**
     * KB FSM time, values: <br></br>
     * 0: not KB <br></br>
     * -1: dead <br></br>
     * positive: KB time count-down <br></br>
     * negative: burrow FSM type
     */
    private var kbTime = 0

    /** temp field: marker for double income  */
    protected var tempearn = false

    /** wait FSM time  */
    private var waitTime = 0

    /** acted: temp field, for update sync  */
    private var acted = false

    /** barrier value, 0 means no barrier or broken  */
    private var barrier: Int

    /** remaining burrow distance  */
    private var bdist = 0.0

    /** poison proc processor  */
    private val pois = PoisonToken(this)

    /** abilities that are activated after it's attacked  */
    private val tokens: MutableList<AttackAb> = ArrayList<AttackAb>()

    /**
     * temp field within an update loop <br></br>
     * used for moving determination
     */
    private var touch = false

    /** temp field: whether it can attack  */
    private var touchEnemy = false

    /** weak proc processor  */
    private val weaks = WeakToken(this)
    private var altAbi = 0
    private val sealed: Proc = Proc.Companion.blank()
    fun altAbi(alt: Int) {
        altAbi = altAbi xor alt
    }

    /** accept attack  */
    override fun damaged(atk: AttackAb) {
        var dmg = getDamage(atk, atk.atk)
        // if immune to wave and the attack is wave, jump out
        if (atk.waveType and Data.Companion.WT_WAVE > 0) {
            if (getProc().IMUWAVE.mult > 0) anim.getEff(Data.Companion.P_WAVE)
            dmg = if (getProc().IMUWAVE.mult == 100) return else dmg * (100 - getProc().IMUWAVE.mult) / 100
        }
        if (atk.waveType and Data.Companion.WT_MOVE > 0) if (getAbi() and Data.Companion.AB_MOVEI > 0) {
            anim.getEff(Data.Companion.P_WAVE)
            return
        }
        if (atk.waveType and Data.Companion.WT_VOLC > 0) {
            if (getProc().IMUVOLC.mult > 0) {
                anim.getEff(Data.Companion.INV)
                return
            }
        }
        tokens.add(atk)
        val imuatk: PT = data.getProc().IMUATK
        if ((atk.dire == -1 || atk.type == -1 || receive(atk.type, -1)) && imuatk.prob > 0) {
            if (status[Data.Companion.P_IMUATK][0] == 0 && basis.r.nextDouble() * 100 < imuatk.prob) {
                status[Data.Companion.P_IMUATK][0] = (imuatk.time * (1 + 0.2 / 3 * getFruit(atk.type, -1))) as Int
                anim.getEff(Data.Companion.P_IMUATK)
            }
            if (status[Data.Companion.P_IMUATK][0] > 0) return
        }
        if (barrier > 0) {
            if (atk.getProc().BREAK.prob > 0) {
                barrier = 0
                anim.getEff(Data.Companion.BREAK_ABI)
            } else if (getDamage(atk, atk.atk) >= barrier) {
                barrier = 0
                anim.getEff(Data.Companion.BREAK_ATK)
                return
            } else {
                anim.getEff(Data.Companion.BREAK_NON)
                return
            }
        }
        CommonStatic.setSE(if (isBase) Data.Companion.SE_HIT_BASE else if (basis.r.irDouble() < 0.5) Data.Companion.SE_HIT_0 else Data.Companion.SE_HIT_1)
        damage += dmg.toLong()
        zx.damaged(atk)
        tempearn = tempearn or (atk.abi and Data.Companion.AB_EARN > 0)
        if (atk.getProc().CRIT.mult > 0) {
            basis.lea.add(EAnimCont(pos, layer, Data.Companion.effas().A_CRIT.getEAnim(DefEff.DEF)))
            CommonStatic.setSE(Data.Companion.SE_CRIT)
        }
        if (atk.getProc().SATK.mult > 0) {
            basis.lea.add(EAnimCont(pos, layer, Data.Companion.effas().A_SATK.getEAnim(DefEff.DEF)))
            CommonStatic.setSE(Data.Companion.SE_SATK)
        }

        // process proc part
        if (atk.type != -1 && !receive(atk.type, 1)) return
        if (atk.getProc().POIATK.mult > 0) {
            val rst: Int = getProc().IMUPOIATK.mult
            if (rst == 100) {
                anim.getEff(Data.Companion.INV)
            } else {
                damage += maxH * atk.getProc().POIATK.mult / 100
                basis.lea.add(EAnimCont(pos, layer, Data.Companion.effas().A_POISON.getEAnim(DefEff.DEF)))
                CommonStatic.setSE(Data.Companion.SE_POISON)
            }
        }
        if (!isBase && atk.getProc().ARMOR.time > 0) {
            status[Data.Companion.P_ARMOR][0] = atk.getProc().ARMOR.time
            status[Data.Companion.P_ARMOR][1] = atk.getProc().ARMOR.mult
            anim.getEff(Data.Companion.P_ARMOR)
        }
        val f = getFruit(atk.type, 1)
        var time = 1 + f * 0.2 / 3
        var dist = 1 + f * 0.1
        if (atk.type < 0 || atk.canon != -2) {
            time = 1.0
            dist = time
        }
        if (atk.getProc().STOP.time > 0) {
            var `val` = (atk.getProc().STOP.time * time) as Int
            val rst: Int = getProc().IMUSTOP.mult
            `val` = `val` * (100 - rst) / 100
            status[Data.Companion.P_STOP][0] = Math.max(status[Data.Companion.P_STOP][0], `val`)
            if (rst < 100) anim.getEff(Data.Companion.P_STOP) else anim.getEff(Data.Companion.INV)
        }
        if (atk.getProc().SLOW.time > 0) {
            var `val` = (atk.getProc().SLOW.time * time) as Int
            val rst: Int = getProc().IMUSLOW.mult
            `val` = `val` * (100 - rst) / 100
            status[Data.Companion.P_SLOW][0] = Math.max(status[Data.Companion.P_SLOW][0], `val`)
            if (rst < 100) anim.getEff(Data.Companion.P_SLOW) else anim.getEff(Data.Companion.INV)
        }
        if (atk.getProc().WEAK.time > 0) {
            var `val` = (atk.getProc().WEAK.time * time) as Int
            val rst: Int = getProc().IMUWEAK.mult
            `val` = `val` * (100 - rst) / 100
            if (rst < 100) {
                weaks.add(intArrayOf(`val`, atk.getProc().WEAK.mult))
                anim.getEff(Data.Companion.P_WEAK)
            } else anim.getEff(Data.Companion.INV)
        }
        if (atk.getProc().CURSE.time > 0) {
            var `val`: Int = atk.getProc().CURSE.time
            val rst: Int = getProc().IMUCURSE.mult
            `val` = `val` * (100 - rst) / 100
            status[Data.Companion.P_CURSE][0] = Math.max(status[Data.Companion.P_CURSE][0], `val`)
            if (rst < 100) anim.getEff(Data.Companion.P_CURSE) else anim.getEff(Data.Companion.INV)
        }
        if (atk.getProc().KB.dis != 0) {
            val rst: Int = getProc().IMUKB.mult
            if (rst < 100) {
                status[Data.Companion.P_KB][0] = atk.getProc().KB.time
                interrupt(Data.Companion.P_KB, atk.getProc().KB.dis * dist * (100 - rst) / 100)
            } else anim.getEff(Data.Companion.INV)
        }
        if (atk.getProc().SNIPER.prob > 0) interrupt(Data.Companion.INT_ASS, Data.Companion.KB_DIS.get(Data.Companion.INT_ASS))
        if (atk.getProc().BOSS.prob > 0) interrupt(Data.Companion.INT_SW, Data.Companion.KB_DIS.get(Data.Companion.INT_SW))
        if (atk.getProc().WARP.exists()) if (getProc().IMUWARP.mult < 100) {
            interrupt(Data.Companion.INT_WARP, atk.getProc().WARP.dis.toDouble())
            val e: EffAnim<WarpEff> = Data.Companion.effas().A_W
            val len: Int = e.len(WarpEff.ENTER) + e.len(WarpEff.EXIT)
            var `val`: Int = atk.getProc().WARP.time
            val rst: Int = getProc().IMUWARP.mult
            `val` = `val` * (100 - rst) / 100
            status[Data.Companion.P_WARP][0] = `val` + len
        } else anim.getEff(Data.Companion.INVWARP)
        if (atk.getProc().SEAL.time > 0) if (getAbi() and Data.Companion.AB_SEALI == 0) {
            status[Data.Companion.P_SEAL][0] = atk.getProc().SEAL.time
            anim.getEff(Data.Companion.P_SEAL)
        } else anim.getEff(Data.Companion.INV)
        if (atk.getProc().POISON.time > 0) if (getAbi() and Data.Companion.AB_POII == 0 || atk.getProc().POISON.damage < 0) {
            val ws: POISON = atk.getProc().POISON.clone() as POISON
            if (ws.type.damage_type == 1) ws.damage = getDamage(atk, ws.damage)
            pois.add(ws)
            anim.getEff(Data.Companion.P_POISON)
        } else anim.getEff(Data.Companion.INV)
        if (atk.getProc().SPEED.time > 0) {
            status[Data.Companion.P_SPEED][0] = atk.getProc().SPEED.time
            status[Data.Companion.P_SPEED][1] = atk.getProc().SPEED.speed
            status[Data.Companion.P_SPEED][2] = atk.getProc().SPEED.type
            anim.getEff(Data.Companion.P_SPEED)
        }
    }

    /** get the current ability bitmask  */
    override fun getAbi(): Int {
        return if (status[Data.Companion.P_SEAL][0] > 0) data.getAbi() xor altAbi and (Data.Companion.AB_ONLY or Data.Companion.AB_METALIC or Data.Companion.AB_GLASS) else data.getAbi() xor altAbi
    }

    /** get the currently attack, only used in display  */
    open fun getAtk(): Int {
        return aam.getAtk()
    }

    /** get the current proc array  */
    fun getProc(): Proc {
        return if (status[Data.Companion.P_SEAL][0] > 0) sealed else data.getProc()
    }

    /** receive an interrupt  */
    fun interrupt(t: Int, d: Double) {
        kb.interrupt(t, d)
    }

    override fun isBase(): Boolean {
        return isBase
    }

    /** mark it dead, proceed death animation  */
    open fun kill() {
        if (kbTime == -1) return
        kbTime = -1
        anim.kill()
    }

    /** update the entity after receiving attacks  */
    override fun postUpdate() {
        val hb: Int = data.getHb()
        var ext: Long = health * hb % maxH
        if (ext == 0L) ext = maxH
        if (status[Data.Companion.P_ARMOR][0] > 0) {
            damage *= (100 + status[Data.Companion.P_ARMOR][1]) / 100.toLong()
        }
        if (!isBase && damage > 0 && kbTime <= 0 && kbTime != -1 && (ext <= damage * hb || health < damage)) interrupt(Data.Companion.INT_HB, Data.Companion.KB_DIS.get(Data.Companion.INT_HB))
        health -= damage
        if (health > maxH) health = maxH
        damage = 0

        // increase damage
        val strong: Int = getProc().STRONG.health
        if (status[Data.Companion.P_STRONG][0] == 0 && strong > 0 && health * 100 <= maxH * strong) {
            status[Data.Companion.P_STRONG][0] = getProc().STRONG.mult
            anim.getEff(Data.Companion.P_STRONG)
        }
        // lethal strike
        if (getProc().LETHAL.prob > 0 && health <= 0) {
            val b: Boolean = basis.r.nextDouble() * 100 < getProc().LETHAL.prob
            if (status[Data.Companion.P_LETHAL][0] == 0 && b) {
                health = 1
                anim.getEff(Data.Companion.P_LETHAL)
            }
            status[Data.Companion.P_LETHAL][0]++
        }
        for (i in tokens.indices) tokens[i].model.invokeLater(tokens[i], this)
        tokens.clear()
        kb.doInterrupt()
        if (getAbi() and Data.Companion.AB_GLASS > 0 && atkm.atkTime == 0 && kbTime == 0 && atkm.loop == 0) kill()

        // update animations
        anim.update()
        zx.postUpdate()
        if (health > 0) tempearn = false
        if (isBase && health < 0) health = 0
        acted = false
    }

    fun setSummon(conf: Int) {
        // conf 1
        if (conf == 1) {
            kb.kbType = Data.Companion.INT_WARP
            kbTime = Data.Companion.effas().A_W.len(WarpEff.EXIT)
            status[Data.Companion.P_WARP][2] = 1
        }
        // conf 2
        if (conf == 2 && data.getPack().anim.anims.size >= 7) kbTime = -3
        if (conf == 3 && data.getPack().anim.anims.size >= 7) {
            kbTime = -3
            status[Data.Companion.P_BURROW] = IntArray(Data.Companion.PROC_WIDTH)
        }
    }

    /** can be targeted by the cat with Targer ability of trait t  */
    override fun targetable(t: Int): Boolean {
        return type and t > 0 || isBase
    }

    /** get touch mode bitmask  */
    override fun touchable(): Int {
        val n: Int = if (getAbi() and Data.Companion.AB_GHOST > 0) Data.Companion.TCH_EX else Data.Companion.TCH_N
        val ex = if (getProc().REVIVE.type.revive_others) Data.Companion.TCH_ZOMBX else 0
        if (kbTime == -1) return Data.Companion.TCH_SOUL or ex
        if (status[Data.Companion.P_REVIVE][1] > 0) return Data.Companion.TCH_CORPSE or ex
        if (status[Data.Companion.P_BURROW][2] > 0) return n or Data.Companion.TCH_UG or ex
        return if (kbTime < -1) Data.Companion.TCH_UG or ex else (if (kbTime == 0) n else Data.Companion.TCH_KB) or ex
    }

    /**
     * update the entity. order of update: <br></br>
     * KB -> revive -> move -> burrow -> attack -> wait
     */
    override fun update() {
        // if this entity is in kb state, do kbmove()
        // eneity can move right after kbmove, no need to mark acted
        if (kbTime > 0) kb.updateKB()

        // update revive status, mark acted
        zx.updateRevive()

        // do move check if available, move if possible
        if (kbTime == 0 && !acted && atkm.atkTime == 0) updateTouch()
        val nstop = status[Data.Companion.P_STOP][0] == 0

        // update burrow state if not stopped
        if (nstop) updateBurrow()

        // update wait and attack state
        if (kbTime == 0) {
            var binatk = kbTime >= 0 && waitTime + kbTime + atkm.atkTime == 0
            binatk = binatk && touchEnemy && atkm.loop != 0 && nstop

            // if it can attack, setup attack state
            if (!acted && binatk) atkm.setUp()

            // update waiting state
            if ((waitTime > 0 || !touchEnemy) && touch && atkm.atkTime == 0) anim.setAnim(UType.IDLE)
            if (waitTime > 0) waitTime--

            // update attack status when in attack state
            if (status[Data.Companion.P_STOP][0] == 0 && atkm.atkTime > 0) atkm.updateAttack()
        }

        // update proc effects
        updateProc()
    }

    protected fun critCalc(isMetal: Boolean, ans: Int, atk: AttackAb): Int {
        var ans = ans
        val satk: Int = atk.getProc().SATK.mult
        if (satk > 0) ans *= (100 + satk) * 0.01.toInt()
        var crit: Int = atk.getProc().CRIT.mult
        if (getProc().CRITI.type == 1) crit = 0
        if (isMetal) if (crit > 0) (ans *= 0.01 * crit).toInt() else if (crit < 0) ans = Math.ceil(health * crit / -100.0).toInt() else ans = if (ans > 0) 1 else 0 else if (crit > 0) (ans *= 0.01 * crit).toInt() else if (crit < 0) ans = Math.ceil(maxH * 0.0001).toInt()
        return ans
    }

    /** determine the amount of damage received from this attack  */
    protected abstract fun getDamage(atk: AttackAb, ans: Int): Int

    /** get max distance to go back  */
    protected abstract fun getLim(): Double
    protected abstract fun traitType(): Int

    /**
     * move forward <br></br>
     * maxl: max distance to move <br></br>
     * extmov: distance try to add to this movement return false when movement reach
     * endpoint
     */
    protected open fun updateMove(maxl: Double, extmov: Double): Boolean {
        acted = true
        var max: Double = (basis.getBase(dire).pos - pos) * dire - data.touchBase()
        if (maxl >= 0) max = Math.min(max, maxl)
        var mov: Double = if (isBase) 0 else if (status[Data.Companion.P_SLOW][0] > 0) 0.5 else data.getSpeed() * 0.5
        if (status[Data.Companion.P_SPEED][0] > 0 && status[Data.Companion.P_SLOW][0] <= 0) {
            if (status[Data.Companion.P_SPEED][2] == 0) {
                mov += status[Data.Companion.P_SPEED][1] * 0.5
            } else if (status[Data.Companion.P_SPEED][2] == 1) {
                mov = mov * (100 + status[Data.Companion.P_SPEED][1]) / 100
            } else if (status[Data.Companion.P_SPEED][2] == 2) {
                mov = status[Data.Companion.P_SPEED][1] * 0.5
            }
        }
        if (cantGoMore()) {
            mov = 0.0
        }
        mov += extmov
        pos += Math.min(mov, max) * dire
        return max > mov
    }

    private fun cantGoMore(): Boolean {
        if (status[Data.Companion.P_SPEED][0] == 0) return false
        return if (dire == 1) {
            pos <= 0
        } else {
            pos >= basis.st.len
        }
    }

    /** interrupt whatever this entity is doing  */
    private fun clearState() {
        atkm.stopAtk()
        if (kbTime < -1 || status[Data.Companion.P_BURROW][2] > 0) {
            status[Data.Companion.P_BURROW][2] = 0
            bdist = 0.0
            kbTime = 0
        }
        if (status[Data.Companion.P_REVIVE][1] > 0) {
            status[Data.Companion.P_REVIVE][1] = 0
            anim.corpse = null
        }
    }

    private fun drawAxis(gra: FakeGraphics, p: P, siz: Double) {
        // after this is the drawing of hit boxes
        var siz = siz
        siz *= 1.25
        val rat: Double = BattleConst.Companion.ratio
        val poa: Double = p.x - pos * rat * siz
        val py = p.y as Int
        val h = (640 * rat * siz).toInt()
        gra.setColor(FakeGraphics.Companion.RED)
        for (i in 0 until data.getAtkCount()) {
            val ds: DoubleArray = aam.inRange(i)
            val d0 = Math.min(ds[0], ds[1])
            val ra = Math.abs(ds[0] - ds[1])
            val x = (d0 * rat * siz + poa).toInt()
            val y = (p.y + 100 * i * rat * siz) as Int
            val w = (ra * rat * siz).toInt()
            if (atkm.tempAtk == i) gra.fillRect(x, y, w, h) else gra.drawRect(x, y, w, h)
        }
        gra.setColor(FakeGraphics.Companion.YELLOW)
        val x = ((pos + data.getRange() * dire) * rat * siz + poa) as Int
        gra.drawLine(x, py, x, py + h)
        gra.setColor(FakeGraphics.Companion.BLUE)
        val bx = ((if (dire == -1) pos else pos - data.getWidth()) * rat * siz + poa) as Int
        val bw = (data.getWidth() * rat * siz) as Int
        gra.drawRect(bx, p.y as Int, bw, h)
        gra.setColor(FakeGraphics.Companion.CYAN)
        gra.drawLine((pos * rat * siz + poa) as Int, py, (pos * rat * siz + poa) as Int, py + h)
        atkm.tempAtk = -1
    }

    /** get the extra proc time due to fruits, for EEnemy only  */
    private fun getFruit(atktype: Int, dire: Int): Double {
        return if (traitType() != dire) 0 else basis.b.t().getFruit(atktype and type)
    }

    /** called when last KB reached  */
    private fun preKill() {
        CommonStatic.setSE(if (basis.r.irDouble() < 0.5) Data.Companion.SE_DEATH_0 else Data.Companion.SE_DEATH_1)
        if (zx.prekill()) return
        kill()
    }

    /** can be effected by the ability targeting trait t  */
    private fun receive(t: Int, dire: Int): Boolean {
        return if (traitType() != dire) true else type and t > 0
    }

    /** update burrow state  */
    private fun updateBurrow() {
        if (!acted && kbTime == 0 && touch && status[Data.Companion.P_BURROW][0] != 0) {
            val bpos: Double = basis.getBase(dire).pos
            val ntbs: Boolean = (bpos - pos) * dire > data.touchBase()
            if (ntbs) {
                // setup burrow state
                status[Data.Companion.P_BURROW][0]--
                status[Data.Companion.P_BURROW][2] = anim.setAnim(UType.BURROW_DOWN)
                kbTime = -2
            }
        }
        if (!acted && kbTime == -2) {
            acted = true
            // burrow down
            status[Data.Companion.P_BURROW][2]--
            if (status[Data.Companion.P_BURROW][2] == 0) {
                kbTime = -3
                anim.setAnim(UType.BURROW_MOVE)
                bdist = data.getRepAtk().getProc().BURROW.dis.toDouble()
            }
        }
        if (!acted && kbTime == -3) {
            // move underground
            val oripos: Double = pos
            val b = updateMove(bdist, 0.0)
            bdist -= (pos - oripos) * dire
            if (!b) {
                bdist = 0.0
                kbTime = -4
                status[Data.Companion.P_BURROW][2] = anim.setAnim(UType.BURROW_UP)
            }
        }
        if (!acted && kbTime == -4) {
            // burrow up
            acted = true
            status[Data.Companion.P_BURROW][2]--
            if (status[Data.Companion.P_BURROW][2] == 0) kbTime = 0
        }
    }

    /** update proc status  */
    private fun updateProc() {
        if (status[Data.Companion.P_STOP][0] > 0) status[Data.Companion.P_STOP][0]--
        if (status[Data.Companion.P_SLOW][0] > 0) status[Data.Companion.P_SLOW][0]--
        if (status[Data.Companion.P_CURSE][0] > 0) status[Data.Companion.P_CURSE][0]--
        if (status[Data.Companion.P_SEAL][0] > 0) status[Data.Companion.P_SEAL][0]--
        if (status[Data.Companion.P_IMUATK][0] > 0) status[Data.Companion.P_IMUATK][0]--
        if (status[Data.Companion.P_ARMOR][0] > 0) status[Data.Companion.P_ARMOR][0]--
        if (status[Data.Companion.P_SPEED][0] > 0) status[Data.Companion.P_SPEED][0]--
        // update tokens
        weaks.update()
        pois.update()
    }

    /** update touch state, move if possible  */
    private fun updateTouch() {
        touch = true
        val ds: DoubleArray = aam.touchRange()
        val le: MutableList<AbEntity> = basis.inRange(data.getTouch(), dire, ds[0], ds[1])
        var blds = false
        if (data.isLD() || data.isOmni()) {
            val bpos: Double = basis.getBase(dire).pos
            blds = (bpos - pos) * dire > data.touchBase()
            if (blds) le.remove(basis.getBase(dire))
            blds = blds and (le.size == 0)
        } else blds = le.size == 0
        if (status[Data.Companion.P_STOP][0] == 0 && blds) {
            touch = false
            anim.setAnim(UType.WALK)
            updateMove(-1.0, 0.0)
        }
        touchEnemy = touch
        if (getAbi() and Data.Companion.AB_ONLY > 0) {
            touchEnemy = false
            for (i in le.indices) if (le[i].targetable(type)) touchEnemy = true
        }
    }

    init {
        basis = b
        data = de
        aam = AtkModelEntity.Companion.getIns(this, d0)
        anim = AnimManager(this, ea)
        atkm = AtkManager(this)
        barrier = de.getShield()
        status[Data.Companion.P_BURROW][0] = getProc().BURROW.count
        status[Data.Companion.P_REVIVE][0] = getProc().REVIVE.count
        sealed.BURROW.set(data.getProc().BURROW)
        sealed.REVIVE.count = data.getProc().REVIVE.count
        sealed.REVIVE.time = data.getProc().REVIVE.time
        sealed.REVIVE.health = data.getProc().REVIVE.health
    }
}
