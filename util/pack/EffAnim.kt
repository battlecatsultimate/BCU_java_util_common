package common.util.pack

import common.CommonStatic
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonEncoder
import common.io.json.Test
import common.io.json.Test.JsonTest_0.JsonD
import common.io.json.Test.JsonTest_2
import common.pack.Context
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.system.VImg
import common.system.fake.FakeImage
import common.system.fake.FakeImage.Marker
import common.util.Data
import common.util.anim.AnimD
import common.util.anim.ImgCut
import common.util.anim.MaAnim
import common.util.anim.MaModel
import common.util.pack.EffAnim
import common.util.pack.EffAnim.EffType
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
import java.util.function.Function

class EffAnim<T>(st: String?, vi: VImg?, ic: ImgCut, anims: Array<T>) : AnimD<EffAnim<T>?, T>(st) where T : Enum<T>?, T : EffType<T>? {
    enum class ArmorEff(private val path: String) : EffType<ArmorEff?> {
        BUFF("buff"), DEBUFF("debuff");

        override fun path(): String {
            return path
        }
    }

    enum class BarEneEff(private val path: String) : EffType<BarEneEff?> {
        BREAK("_breaker"), DESTR("_destruction");

        override fun path(): String {
            return path
        }
    }

    enum class BarrierEff(private val path: String) : EffType<BarrierEff?> {
        BREAK("_breaker"), DESTR("_destruction"), DURING("_during"), START("_start"), END("_end");

        override fun path(): String {
            return path
        }
    }

    enum class DefEff(private val path: String) : EffType<DefEff?> {
        DEF("");

        override fun path(): String {
            return path
        }
    }

    class EffAnimStore {
        var A_DOWN: EffAnim<DefEff>? = null
        var A_E_DOWN: EffAnim<DefEff>? = null
        var A_UP: EffAnim<DefEff>? = null
        var A_E_UP: EffAnim<DefEff>? = null
        var A_SLOW: EffAnim<DefEff>? = null
        var A_E_SLOW: EffAnim<DefEff>? = null
        var A_STOP: EffAnim<DefEff>? = null
        var A_E_STOP: EffAnim<DefEff>? = null
        var A_SHIELD: EffAnim<DefEff>? = null
        var A_E_SHIELD: EffAnim<DefEff>? = null
        var A_FARATTACK: EffAnim<DefEff>? = null
        var A_E_FARATTACK: EffAnim<DefEff>? = null
        var A_WAVE_INVALID: EffAnim<DefEff>? = null
        var A_E_WAVE_INVALID: EffAnim<DefEff>? = null
        var A_WAVE_STOP: EffAnim<DefEff>? = null
        var A_E_WAVE_STOP: EffAnim<DefEff>? = null
        var A_WAVEGUARD // unused
                : EffAnim<DefEff>? = null
        var A_E_WAVEGUARD // unused
                : EffAnim<DefEff>? = null
        var A_EFF_INV: EffAnim<DefEff>? = null
        var A_EFF_DEF // unused
                : EffAnim<DefEff>? = null
        var A_Z_STRONG: EffAnim<DefEff>? = null
        var A_B: EffAnim<BarrierEff>? = null
        var A_E_B: EffAnim<BarEneEff>? = null
        var A_W: EffAnim<WarpEff>? = null
        var A_W_C: EffAnim<WarpEff>? = null
        var A_CURSE: EffAnim<DefEff>? = null
        var A_ZOMBIE: EffAnim<ZombieEff>? = null
        var A_SHOCKWAVE: EffAnim<DefEff>? = null
        var A_CRIT: EffAnim<DefEff>? = null
        var A_KB: EffAnim<KBEff>? = null
        var A_SNIPER: EffAnim<SniperEff>? = null
        var A_U_ZOMBIE: EffAnim<ZombieEff>? = null
        var A_U_B: EffAnim<BarrierEff>? = null
        var A_U_E_B: EffAnim<BarEneEff>? = null
        var A_SEAL: EffAnim<DefEff>? = null
        var A_POI0: EffAnim<DefEff>? = null
        var A_POI1: EffAnim<DefEff>? = null
        var A_POI2: EffAnim<DefEff>? = null
        var A_POI3: EffAnim<DefEff>? = null
        var A_POI4: EffAnim<DefEff>? = null
        var A_POI5: EffAnim<DefEff>? = null
        var A_POI6: EffAnim<DefEff>? = null
        var A_POI7: EffAnim<DefEff>? = null
        var A_SATK: EffAnim<DefEff>? = null
        var A_IMUATK: EffAnim<DefEff>? = null
        var A_POISON: EffAnim<DefEff>? = null
        var A_VOLC: EffAnim<VolcEff>? = null
        var A_E_VOLC: EffAnim<VolcEff>? = null
        var A_E_CURSE: EffAnim<DefEff>? = null
        var A_WAVE: EffAnim<DefEff>? = null
        var A_E_WAVE: EffAnim<DefEff>? = null
        var A_ARMOR: EffAnim<ArmorEff>? = null
        var A_E_ARMOR: EffAnim<ArmorEff>? = null
        var A_SPEED: EffAnim<SpeedEff>? = null
        var A_E_SPEED: EffAnim<SpeedEff>? = null
        var A_WEAK_UP: EffAnim<WeakUpEff>? = null
        var A_E_WEAK_UP: EffAnim<WeakUpEff>? = null
        fun values(): Array<EffAnim<*>> {
            val fld = EffAnimStore::class.java.declaredFields
            val ans: Array<EffAnim<*>> = arrayOfNulls(fld.size)
            Data.Companion.err(Context.RunExc { for (i in ans.indices) ans[i] = fld[i][this] as EffAnim<*> })
            return ans
        }

        operator fun set(i: Int, eff: EffAnim<DefEff>) {
            Data.Companion.err(Context.RunExc { EffAnimStore::class.java.declaredFields[i][this] = eff })
        }
    }

    interface EffType<T> : AnimType<EffAnim<T>?, T> where T : Enum<T>?, T : EffType<T>? {
        fun path(): String
    }

    enum class KBEff(private val path: String) : EffType<KBEff?> {
        KB("_hb"), SW("_sw"), ASS("_ass");

        override fun path(): String {
            return path
        }
    }

    enum class SniperEff(private val path: String) : EffType<SniperEff?> {
        IDLE("00"), ATK("01");

        override fun path(): String {
            return path
        }
    }

    enum class SpeedEff(private val path: String) : EffType<SpeedEff?> {
        UP("up"), DOWN("down");

        override fun path(): String {
            return path
        }
    }

    enum class VolcEff(private val path: String) : EffType<VolcEff?> {
        START("00"), DURING("01"), END("02");

        override fun path(): String {
            return path
        }
    }

    enum class WarpEff(private val path: String) : EffType<WarpEff?> {
        ENTER("_entrance"), EXIT("_exit");

        override fun path(): String {
            return path
        }
    }

    enum class WeakUpEff(private val path: String) : EffType<WeakUpEff?> {
        UP("up");

        override fun path(): String {
            return path
        }
    }

    enum class ZombieEff(private val path: String) : EffType<ZombieEff?> {
        REVIVE("revive"), DOWN("_down");

        override fun path(): String {
            return path
        }
    }

    private val vimg: VImg?
    private var rev = false
    private var name = ""
    override fun getNum(): FakeImage? {
        return vimg.getImg()
    }

    override fun load() {
        loaded = true
        parts = imgcut.cut(vimg.getImg())
        mamodel = MaModel.Companion.newIns(str + ".mamodel")
        anims = arrayOfNulls<MaAnim>(types.size)
        for (i in types.indices) anims.get(i) = MaAnim.Companion.newIns(str + types.get(i).path() + ".maanim")
        if (rev) revert()
    }

    override fun toString(): String {
        if (name.length > 0) return name
        val ss: Array<String> = str.split("/").toTypedArray()
        return ss[ss.size - 1]
    }

    companion object {
        fun read() {
            val effas: EffAnimStore = CommonStatic.getBCAssets().effas
            val stre = "./org/battle/e1/set_enemy001_zombie"
            val ve = VImg("$stre.png")
            val ice: ImgCut = ImgCut.Companion.newIns("$stre.imgcut")
            val stra = "./org/battle/a/"
            val va = VImg(stra + "000_a.png")
            val ica: ImgCut = ImgCut.Companion.newIns(stra + "000_a.imgcut")
            var ski = "skill00"
            val stfs = arrayOfNulls<String>(4)
            val vfs: Array<VImg?> = arrayOfNulls<VImg>(4)
            val icfs: Array<ImgCut?> = arrayOfNulls<ImgCut>(4)
            for (i in 0..3) {
                stfs[i] = "./org/battle/s$i/"
                vfs[i] = VImg(stfs[i].toString() + ski + i + ".png")
                icfs[i] = ImgCut.Companion.newIns(stfs[i].toString() + ski + i + ".imgcut")
            }
            effas.A_SHOCKWAVE = EffAnim<DefEff>(stra + "boss_welcome", va, ica, common.util.pack.EffAnim.DefEff.values())
            effas.A_CRIT = EffAnim<DefEff>(stra + "critical", va, ica, common.util.pack.EffAnim.DefEff.values())
            effas.A_KB = EffAnim<KBEff>(stra + "kb", va, ica, common.util.pack.EffAnim.KBEff.values())
            effas.A_ZOMBIE = EffAnim<ZombieEff>(stre, ve, ice, common.util.pack.EffAnim.ZombieEff.values())
            effas.A_U_ZOMBIE = EffAnim<ZombieEff>(stre, ve, ice, common.util.pack.EffAnim.ZombieEff.values())
            effas.A_U_ZOMBIE!!.rev = true
            ski = "skill_"
            for (i in Data.Companion.A_PATH.indices) {
                val path = stfs[0] + Data.Companion.A_PATH.get(i) + "/" + ski + Data.Companion.A_PATH.get(i)
                effas[i * 2] = EffAnim<DefEff>(path, vfs[0], icfs[0], common.util.pack.EffAnim.DefEff.values())
                effas[i * 2 + 1] = EffAnim<DefEff>(path + "_e", vfs[0], icfs[0], common.util.pack.EffAnim.DefEff.values())
            }
            effas.A_EFF_INV = EffAnim<DefEff>(stfs[0].toString() + ski + "effect_invalid", vfs[0], icfs[0], common.util.pack.EffAnim.DefEff.values())
            effas.A_EFF_DEF = EffAnim<DefEff>(stfs[0].toString() + ski + "effectdef", vfs[0], icfs[0], common.util.pack.EffAnim.DefEff.values())
            effas.A_Z_STRONG = EffAnim<DefEff>(stfs[1].toString() + ski + "zombie_strong", vfs[1], icfs[1], common.util.pack.EffAnim.DefEff.values())
            effas.A_B = EffAnim<BarrierEff>(stfs[2].toString() + ski + "barrier", vfs[2], icfs[2], common.util.pack.EffAnim.BarrierEff.values())
            effas.A_U_B = EffAnim<BarrierEff>(stfs[2].toString() + ski + "barrier", vfs[2], icfs[2], common.util.pack.EffAnim.BarrierEff.values())
            effas.A_U_B!!.rev = true
            effas.A_E_B = EffAnim<BarEneEff>(stfs[2].toString() + ski + "barrier_e", vfs[2], icfs[2], common.util.pack.EffAnim.BarEneEff.values())
            effas.A_U_E_B = EffAnim<BarEneEff>(stfs[2].toString() + ski + "barrier_e", vfs[2], icfs[2], common.util.pack.EffAnim.BarEneEff.values())
            effas.A_U_E_B!!.rev = true
            effas.A_W = EffAnim<WarpEff>(stfs[2].toString() + ski + "warp", vfs[2], icfs[2], common.util.pack.EffAnim.WarpEff.values())
            effas.A_W_C = EffAnim<WarpEff>(stfs[2].toString() + ski + "warp_chara", vfs[2], icfs[2], common.util.pack.EffAnim.WarpEff.values())
            val strs = "./org/battle/sniper/"
            val strm = "img043"
            val vis = VImg("$strs$strm.png")
            val ics: ImgCut = ImgCut.Companion.newIns("$strs$strm.imgcut")
            effas.A_SNIPER = EffAnim<SniperEff>(strs + "000_snyaipa", vis, ics, common.util.pack.EffAnim.SniperEff.values())
            effas.A_CURSE = EffAnim<DefEff>(stfs[3].toString() + ski + "curse", vfs[3], icfs[3], common.util.pack.EffAnim.DefEff.values())
            readCustom(stfs, icfs)
            val vuw = VImg("./org/battle/s4/skill004.png")
            val icsvuw: ImgCut = ImgCut.Companion.newIns("./org/battle/s4/skill004.imgcut")
            effas.A_WAVE = EffAnim<DefEff>("./org/battle/s4/skill_wave_attack", vuw, icsvuw, common.util.pack.EffAnim.DefEff.values())
            val vew = VImg("./org/battle/s5/skill005.png")
            val icsvew: ImgCut = ImgCut.Companion.newIns("./org/battle/s5/skill005.imgcut")
            effas.A_E_WAVE = EffAnim<DefEff>("./org/battle/s5/skill_wave_attack_e", vew, icsvew, common.util.pack.EffAnim.DefEff.values())
            val vsatk = VImg("./org/battle/s6/skill006.png")
            val icsatk: ImgCut = ImgCut.Companion.newIns("./org/battle/s6/skill006.imgcut")
            effas.A_SATK = EffAnim<DefEff>("./org/battle/s6/strong_attack", vsatk, icsatk, common.util.pack.EffAnim.DefEff.values())
            val viatk = VImg("./org/battle/s7/skill007.png")
            val iciatk: ImgCut = ImgCut.Companion.newIns("./org/battle/s7/skill007.imgcut")
            effas.A_IMUATK = EffAnim<DefEff>("./org/battle/s7/skill_attack_invalid", viatk, iciatk, common.util.pack.EffAnim.DefEff.values())
            val vip = VImg("./org/battle/s8/skill008.png")
            val icp: ImgCut = ImgCut.Companion.newIns("./org/battle/s8/skill008.imgcut")
            effas.A_POISON = EffAnim<DefEff>("./org/battle/s8/skill_percentage_attack", vip, icp, common.util.pack.EffAnim.DefEff.values())
            var vic: VImg? = VImg("./org/battle/s9/skill009.png")
            var icc: ImgCut = ImgCut.Companion.newIns("./org/battle/s9/skill009.imgcut")
            effas.A_VOLC = EffAnim<VolcEff>("./org/battle/s9/skill_volcano", vic, icc, common.util.pack.EffAnim.VolcEff.values())
            vic = VImg("./org/battle/s10/skill010.png")
            icc = ImgCut.Companion.newIns("./org/battle/s10/skill010.imgcut")
            effas.A_E_VOLC = EffAnim<VolcEff>("./org/battle/s10/skill_volcano", vic, icc, common.util.pack.EffAnim.VolcEff.values())
            val vcu = VImg("./org/battle/s11/skill011.png")
            val iccu: ImgCut = ImgCut.Companion.newIns("./org/battle/s11/skill011.imgcut")
            effas.A_E_CURSE = EffAnim<DefEff>("./org/battle/s11/skill_curse_e", vcu, iccu, common.util.pack.EffAnim.DefEff.values())
        }

        private fun excColor(fimg: FakeImage, f: Function<IntArray, Int>) {
            fimg.mark(Marker.RECOLOR)
            val w: Int = fimg.getWidth()
            val h: Int = fimg.getHeight()
            for (i in 0 until w) for (j in 0 until h) {
                var p: Int = fimg.getRGB(i, j)
                val b = p and 255
                val g = p shr 8 and 255
                val r = p shr 16 and 255
                val a = p shr 24
                p = f.apply(intArrayOf(a, r, g, b))
                fimg.setRGB(i, j, p)
            }
            fimg.mark(Marker.RECOLORED)
        }

        private fun readCustom(stfs: Array<String?>, icfs: Array<ImgCut>) {
            val ski = "skill_"
            val effas: EffAnimStore = CommonStatic.getBCAssets().effas
            val vseal = VImg(stfs[3].toString() + "skill003.png")
            excColor(vseal.getImg()) { `is`: IntArray -> `is`[0] shl 24 or (`is`[1] shl 16) or (`is`[3] shl 8) or `is`[2] }
            effas.A_SEAL = EffAnim<DefEff>(stfs[3].toString() + ski + "curse", vseal, icfs[3], common.util.pack.EffAnim.DefEff.values())
            var vpois = VImg(stfs[3].toString() + "skill003.png")
            excColor(vpois.getImg()) { `is`: IntArray -> `is`[0] shl 24 or (`is`[2] shl 16) or (`is`[3] shl 8) or `is`[1] }
            effas.A_POI0 = EffAnim<DefEff>(stfs[3].toString() + ski + "curse", vpois, icfs[3], common.util.pack.EffAnim.DefEff.values())
            effas.A_POI0!!.name = "poison_DF"
            vpois = VImg(stfs[3].toString() + "poison.png")
            effas.A_POI1 = EffAnim<DefEff>(stfs[3].toString() + ski + "curse", vpois, icfs[3], common.util.pack.EffAnim.DefEff.values())
            effas.A_POI1!!.name = "poison_DT0"
            val strpb = stfs[3].toString() + "poisbub/poisbub"
            vpois = VImg("$strpb.png")
            val icpois: ImgCut = ImgCut.Companion.newIns("$strpb.imgcut")
            effas.A_POI2 = EffAnim<DefEff>(strpb, vpois, icpois, common.util.pack.EffAnim.DefEff.values())
            effas.A_POI2!!.name = "poison_purple"
            vpois = VImg("$strpb.png")
            excColor(vpois.getImg()) { `is`: IntArray -> `is`[0] shl 24 or (`is`[1] shl 16) or (`is`[3] shl 8) or `is`[2] }
            effas.A_POI3 = EffAnim<DefEff>(strpb, vpois, icpois, common.util.pack.EffAnim.DefEff.values())
            effas.A_POI3!!.name = "poison_green"
            vpois = VImg("$strpb.png")
            excColor(vpois.getImg()) { `is`: IntArray -> `is`[0] shl 24 or (`is`[2] shl 16) or (`is`[1] shl 8) or `is`[3] }
            effas.A_POI4 = EffAnim<DefEff>(strpb, vpois, icpois, common.util.pack.EffAnim.DefEff.values())
            effas.A_POI4!!.name = "poison_blue"
            vpois = VImg("$strpb.png")
            excColor(vpois.getImg()) { `is`: IntArray -> `is`[0] shl 24 or (`is`[2] shl 16) or (`is`[3] shl 8) or `is`[1] }
            effas.A_POI5 = EffAnim<DefEff>(strpb, vpois, icpois, common.util.pack.EffAnim.DefEff.values())
            effas.A_POI5!!.name = "poison_cyan"
            vpois = VImg("$strpb.png")
            excColor(vpois.getImg()) { `is`: IntArray -> `is`[0] shl 24 or (`is`[3] shl 16) or (`is`[1] shl 8) or `is`[2] }
            effas.A_POI6 = EffAnim<DefEff>(strpb, vpois, icpois, common.util.pack.EffAnim.DefEff.values())
            effas.A_POI6!!.name = "poison_orange"
            vpois = VImg("$strpb.png")
            excColor(vpois.getImg()) { `is`: IntArray -> `is`[0] shl 24 or (`is`[3] shl 16) or (`is`[2] shl 8) or `is`[1] }
            effas.A_POI7 = EffAnim<DefEff>(strpb, vpois, icpois, common.util.pack.EffAnim.DefEff.values())
            effas.A_POI7!!.name = "poison_pink"
            var breaker = stfs[3].toString() + "armor_break/armor_break"
            var vbreak: VImg? = VImg("$breaker.png")
            var icbreak: ImgCut = ImgCut.Companion.newIns("$breaker.imgcut")
            effas.A_ARMOR = EffAnim<ArmorEff>(breaker, vbreak, icbreak, common.util.pack.EffAnim.ArmorEff.values())
            breaker = stfs[3].toString() + "armor_break_e/armor_break_e"
            icbreak = ImgCut.Companion.newIns("$breaker.imgcut")
            vbreak = VImg("$breaker.png")
            effas.A_E_ARMOR = EffAnim<ArmorEff>(breaker, vbreak, icbreak, common.util.pack.EffAnim.ArmorEff.values())
            var speed = stfs[3].toString() + "speed/speed"
            var vspeed: VImg? = VImg("$speed.png")
            var icspeed: ImgCut = ImgCut.Companion.newIns("$speed.imgcut")
            effas.A_SPEED = EffAnim<SpeedEff>(speed, vspeed, icspeed, common.util.pack.EffAnim.SpeedEff.values())
            speed = stfs[3].toString() + "speed_e/speed_e"
            vspeed = VImg("$speed.png")
            icspeed = ImgCut.Companion.newIns("$speed.imgcut")
            effas.A_E_SPEED = EffAnim<SpeedEff>(speed, vspeed, icspeed, common.util.pack.EffAnim.SpeedEff.values())
            val wea = "./org/battle/"
            var weakup = wea + "weaken_up/weaken_up"
            var vwea: VImg? = VImg("$weakup.png")
            var icwea: ImgCut = ImgCut.Companion.newIns("$weakup.imgcut")
            effas.A_WEAK_UP = EffAnim<WeakUpEff>(weakup, vwea, icwea, common.util.pack.EffAnim.WeakUpEff.values())
            weakup = wea + "weaken_up_e/weaken_up_e"
            vwea = VImg("$weakup.png")
            icwea = ImgCut.Companion.newIns("$weakup.imgcut")
            effas.A_E_WEAK_UP = EffAnim<WeakUpEff>(weakup, vwea, icwea, common.util.pack.EffAnim.WeakUpEff.values())
        }
    }

    init {
        vimg = vi
        imgcut = ic
        types = anims
    }
}
