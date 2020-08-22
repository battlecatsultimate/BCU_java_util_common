package common.util

import common.CommonStatic
import common.CommonStatic.BCAuxAssets
import common.battle.entity.AbEntity
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
import common.system.P
import common.system.SymCoord
import common.system.VImg
import common.system.fake.FakeImage
import common.util.Data
import common.util.anim.ImgCut
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

object Res : ImgCore {
    fun getBase(ae: AbEntity, coor: SymCoord): P {
        val aux: BCAuxAssets = CommonStatic.getBCAssets()
        var h: Long = ae.health
        if (h < 0) h = 0
        val val0 = Res.getLab(h)
        val val1 = Res.getLab(ae.maxH)
        val input: Array<FakeImage?> = arrayOfNulls<FakeImage>(val0.size + val1.size + 1)
        for (i in val0.indices) input[i] = aux.num.get(5).get(val0[i]).getImg()
        input[val0.size] = aux.num.get(5).get(10).getImg()
        for (i in val1.indices) input[val0.size + i + 1] = aux.num.get(5).get(val1[i]).getImg()
        return coor.draw(*input)
    }

    fun getCost(cost: Int, enable: Boolean, coor: SymCoord): P {
        val aux: BCAuxAssets = CommonStatic.getBCAssets()
        if (cost == -1) return coor.draw(aux.battle.get(0).get(3).getImg())
        val `val` = Res.getLab(cost.toLong())
        val input: Array<FakeImage?> = arrayOfNulls<FakeImage>(`val`.size)
        for (i in `val`.indices) input[i] = aux.num.get(if (enable) 3 else 4).get(`val`[i]).getImg()
        return coor.draw(*input)
    }

    fun getMoney(mon: Int, max: Int, coor: SymCoord): P {
        val aux: BCAuxAssets = CommonStatic.getBCAssets()
        val val0 = Res.getLab(mon.toLong())
        val val1 = Res.getLab(max.toLong())
        val input: Array<FakeImage?> = arrayOfNulls<FakeImage>(val0.size + val1.size + 1)
        for (i in val0.indices) input[i] = aux.num.get(0).get(val0[i]).getImg()
        input[val0.size] = aux.num.get(0).get(10).getImg()
        for (i in val1.indices) input[val0.size + i + 1] = aux.num.get(0).get(val1[i]).getImg()
        return coor.draw(*input)
    }

    fun getWorkerLv(lv: Int, enable: Boolean, coor: SymCoord): P {
        val aux: BCAuxAssets = CommonStatic.getBCAssets()
        return coor.draw(aux.num.get(if (enable) 1 else 2).get(10).getImg(), aux.num.get(if (enable) 1 else 2).get(lv).getImg())
    }

    fun readData() {
        val aux: BCAuxAssets = CommonStatic.getBCAssets()
        aux.unicut = ImgCut.Companion.newIns("./org/data/uni.imgcut")
        aux.udicut = ImgCut.Companion.newIns("./org/data/udi.imgcut")
        val uni = VImg("./org/page/uni.png")
        uni.setCut(aux.unicut)
        aux.slot.get(0) = uni
        aux.ico.get(0) = arrayOfNulls<VImg>(6)
        aux.ico.get(1) = arrayOfNulls<VImg>(4)
        aux.ico.get(0).get(0) = VImg("./org/page/foreground.png")
        aux.ico.get(0).get(1) = VImg("./org/page/starFG.png")
        aux.ico.get(0).get(2) = VImg("./org/page/EFBG.png")
        aux.ico.get(0).get(3) = VImg("./org/page/TFBG.png")
        aux.ico.get(0).get(4) = VImg("./org/page/glow.png")
        aux.ico.get(0).get(5) = VImg("./org/page/EFFG.png")
        aux.ico.get(1).get(0) = VImg("./org/page/uni_f.png")
        aux.ico.get(1).get(1) = VImg("./org/page/uni_c.png")
        aux.ico.get(1).get(2) = VImg("./org/page/uni_s.png")
        aux.ico.get(1).get(3) = VImg("./org/page/uni_box.png")
        for (vs in aux.ico.get(1)) vs.setCut(aux.unicut)
        val ic029: ImgCut = ImgCut.Companion.newIns("./org/page/img029.imgcut")
        val img029 = VImg("./org/page/img029.png")
        val parts: Array<FakeImage> = ic029.cut(img029.getImg())
        aux.slot.get(1) = VImg(parts[9])
        aux.slot.get(2) = VImg(parts[10])
        Res.readAbiIcon()
        Res.readBattle()
    }

    private fun getLab(cost: Long): IntArray {
        var cost = cost
        if (cost < 0) cost = 0
        var len = Math.log10(if (cost == 0L) 1 else cost.toDouble()).toInt() + 1
        if (len < 0) len = 0
        val input = IntArray(len)
        for (i in 0 until len) {
            input[len - i - 1] = (cost % 10).toInt()
            cost /= 10
        }
        return input
    }

    private fun readAbiIcon() {
        val aux: BCAuxAssets = CommonStatic.getBCAssets()
        CommonStatic.getConfig().icon = true
        val ic015: ImgCut = ImgCut.Companion.newIns("./org/page/img015.imgcut")
        val img015 = VImg("./org/page/img015.png")
        val parts: Array<FakeImage> = ic015.cut(img015.getImg())
        aux.icon.get(0) = arrayOfNulls<VImg>(Data.Companion.ABI_TOT)
        aux.icon.get(1) = arrayOfNulls<VImg>(Data.Companion.PROC_TOT)
        aux.icon.get(2) = arrayOfNulls<VImg>(Data.Companion.ATK_TOT)
        aux.icon.get(3) = arrayOfNulls<VImg>(Data.Companion.TRAIT_TOT)
        aux.icon.get(3).get(Data.Companion.TRAIT_RED) = VImg(parts[77])
        aux.icon.get(3).get(Data.Companion.TRAIT_FLOAT) = VImg(parts[78])
        aux.icon.get(3).get(Data.Companion.TRAIT_BLACK) = VImg(parts[79])
        aux.icon.get(3).get(Data.Companion.TRAIT_METAL) = VImg(parts[80])
        aux.icon.get(3).get(Data.Companion.TRAIT_ANGEL) = VImg(parts[81])
        aux.icon.get(3).get(Data.Companion.TRAIT_ALIEN) = VImg(parts[82])
        aux.icon.get(3).get(Data.Companion.TRAIT_ZOMBIE) = VImg(parts[83])
        aux.icon.get(3).get(Data.Companion.TRAIT_RELIC) = VImg(parts[84])
        aux.icon.get(0).get(Data.Companion.ABI_EKILL) = VImg(parts[110])
        aux.icon.get(2).get(Data.Companion.ATK_OMNI) = VImg(parts[112])
        aux.icon.get(1).get(Data.Companion.P_IMUCURSE) = VImg(parts[116])
        aux.icon.get(1).get(Data.Companion.P_WEAK) = VImg(parts[195])
        aux.icon.get(1).get(Data.Companion.P_STRONG) = VImg(parts[196])
        aux.icon.get(1).get(Data.Companion.P_STOP) = VImg(parts[197])
        aux.icon.get(1).get(Data.Companion.P_SLOW) = VImg(parts[198])
        aux.icon.get(1).get(Data.Companion.P_LETHAL) = VImg(parts[199])
        aux.icon.get(0).get(Data.Companion.ABI_BASE) = VImg(parts[200])
        aux.icon.get(1).get(Data.Companion.P_CRIT) = VImg(parts[201])
        aux.icon.get(0).get(Data.Companion.ABI_ONLY) = VImg(parts[202])
        aux.icon.get(0).get(Data.Companion.ABI_GOOD) = VImg(parts[203])
        aux.icon.get(0).get(Data.Companion.ABI_RESIST) = VImg(parts[204])
        aux.icon.get(0).get(Data.Companion.ABI_EARN) = VImg(parts[205])
        aux.icon.get(0).get(Data.Companion.ABI_MASSIVE) = VImg(parts[206])
        aux.icon.get(1).get(Data.Companion.P_KB) = VImg(parts[207])
        aux.icon.get(1).get(Data.Companion.P_WAVE) = VImg(parts[208])
        aux.icon.get(0).get(Data.Companion.ABI_METALIC) = VImg(parts[209])
        aux.icon.get(1).get(Data.Companion.P_IMUWAVE) = VImg(parts[210])
        aux.icon.get(2).get(Data.Companion.ATK_AREA) = VImg(parts[211])
        aux.icon.get(2).get(Data.Companion.ATK_LD) = VImg(parts[212])
        aux.icon.get(1).get(Data.Companion.P_IMUWEAK) = VImg(parts[213])
        aux.icon.get(1).get(Data.Companion.P_IMUSTOP) = VImg(parts[214])
        aux.icon.get(1).get(Data.Companion.P_IMUSLOW) = VImg(parts[215])
        aux.icon.get(1).get(Data.Companion.P_IMUKB) = VImg(parts[216])
        aux.icon.get(2).get(Data.Companion.ATK_SINGLE) = VImg(parts[217])
        aux.icon.get(0).get(Data.Companion.ABI_WAVES) = VImg(parts[218])
        aux.icon.get(0).get(Data.Companion.ABI_WKILL) = VImg(parts[258])
        aux.icon.get(0).get(Data.Companion.ABI_RESISTS) = VImg(parts[122])
        aux.icon.get(0).get(Data.Companion.ABI_MASSIVES) = VImg(parts[114])
        aux.icon.get(0).get(Data.Companion.ABI_ZKILL) = VImg(parts[260])
        aux.icon.get(1).get(Data.Companion.P_IMUWARP) = VImg(parts[262])
        aux.icon.get(1).get(Data.Companion.P_BREAK) = VImg(parts[264])
        aux.icon.get(1).get(Data.Companion.P_WARP) = VImg(parts[266])
        aux.icon.get(1).get(Data.Companion.P_SATK) = VImg(parts[229])
        aux.icon.get(1).get(Data.Companion.P_IMUATK) = VImg(parts[231])
        aux.icon.get(1).get(Data.Companion.P_VOLC) = VImg(parts[239])
        aux.icon.get(1).get(Data.Companion.P_IMUPOIATK) = VImg(parts[237])
        aux.icon.get(1).get(Data.Companion.P_IMUVOLC) = VImg(parts[243])
        aux.icon.get(1).get(Data.Companion.P_CURSE) = VImg(parts[289])
        aux.icon.get(0).get(Data.Companion.ABI_THEMEI) = VImg("./org/page/icons/ThemeX.png")
        aux.icon.get(0).get(Data.Companion.ABI_TIMEI) = VImg("./org/page/icons/TimeX.png")
        aux.icon.get(0).get(Data.Companion.ABI_IMUSW) = VImg("./org/page/icons/BossWaveX.png")
        aux.icon.get(0).get(Data.Companion.ABI_SNIPERI) = VImg("./org/page/icons/SnipeX.png")
        aux.icon.get(0).get(Data.Companion.ABI_POII) = VImg("./org/page/icons/PoisonX.png")
        aux.icon.get(0).get(Data.Companion.ABI_SEALI) = VImg("./org/page/icons/SealX.png")
        aux.icon.get(0).get(Data.Companion.ABI_GHOST) = VImg("./org/page/icons/Ghost.png")
        aux.icon.get(1).get(Data.Companion.P_THEME) = VImg("./org/page/icons/Theme.png")
        aux.icon.get(1).get(Data.Companion.P_TIME) = VImg("./org/page/icons/Time.png")
        aux.icon.get(1).get(Data.Companion.P_BOSS) = VImg("./org/page/icons/BossWave.png")
        aux.icon.get(1).get(Data.Companion.P_SNIPER) = VImg("./org/page/icons/Snipe.png")
        aux.icon.get(1).get(Data.Companion.P_POISON) = VImg("./org/page/icons/Poison.png")
        aux.icon.get(1).get(Data.Companion.P_SEAL) = VImg("./org/page/icons/Seal.png")
        aux.icon.get(1).get(Data.Companion.P_MOVEWAVE) = VImg("./org/page/icons/Moving.png")
        aux.icon.get(1).get(Data.Companion.P_SUMMON) = VImg("./org/page/icons/Summon.png")
        aux.icon.get(0).get(Data.Companion.ABI_MOVEI) = VImg("./org/page/icons/MovingX.png")
        aux.icon.get(0).get(Data.Companion.ABI_GLASS) = VImg("./org/page/icons/Suicide.png")
        aux.icon.get(1).get(Data.Companion.P_BURROW) = VImg("./org/page/icons/Burrow.png")
        aux.icon.get(1).get(Data.Companion.P_REVIVE) = VImg("./org/page/icons/Revive.png")
        aux.icon.get(1).get(Data.Companion.P_CRITI) = VImg("./org/page/icons/CritX.png")
        aux.icon.get(3).get(Data.Companion.TRAIT_WHITE) = VImg("./org/page/icons/White.png")
        aux.icon.get(1).get(Data.Companion.P_POIATK) = VImg("./org/page/icons/BCPoison.png")
        aux.icon.get(1).get(Data.Companion.P_ARMOR) = VImg("./org/page/icons/ArmorBreak.png")
        aux.icon.get(1).get(Data.Companion.P_SPEED) = VImg("./org/page/icons/Speed.png")
        CommonStatic.getConfig().icon = false
    }

    private fun readBattle() {
        val aux: BCAuxAssets = CommonStatic.getBCAssets()
        aux.battle.get(0) = arrayOfNulls<VImg>(4)
        aux.battle.get(1) = arrayOfNulls<VImg>(12)
        aux.battle.get(2) = arrayOfNulls<VImg>(5)
        val ic001: ImgCut = ImgCut.Companion.newIns("./org/page/img001.imgcut")
        val img001 = VImg("./org/page/img001.png")
        var parts: Array<FakeImage?> = ic001.cut(img001.getImg())
        val vals = intArrayOf(5, 19, 30, 40, 51, 62, 73, 88, 115)
        val adds = intArrayOf(1, 2, 2, 0, 0, 1, 1, 1, 0)
        for (i in 0..8) {
            for (j in 0..9) aux.num.get(i).get(j) = VImg(parts[vals[i] - 5 + j])
            if (adds[i] == 1) aux.num.get(i).get(10) = VImg(parts[vals[i] + 5])
            if (adds[i] == 2) aux.num.get(i).get(10) = VImg(parts[vals[i] - 6])
        }
        aux.battle.get(0).get(3) = VImg(parts[81])
        val ic002: ImgCut = ImgCut.Companion.newIns("./org/page/img002.imgcut")
        val img002 = VImg("./org/page/img002.png")
        parts = ic002.cut(img002.getImg())
        aux.battle.get(0).get(0) = VImg(parts[5])
        aux.battle.get(0).get(1) = VImg(parts[24])
        aux.battle.get(0).get(2) = VImg(parts[6])
        aux.battle.get(1).get(0) = VImg(parts[8])
        aux.battle.get(1).get(1) = VImg(parts[7])
        for (i in 0..9) aux.battle.get(1).get(2 + i) = VImg(parts[11 + i])
        aux.battle.get(2).get(0) = VImg(parts[27])
        aux.battle.get(2).get(1) = VImg(parts[29])
        aux.battle.get(2).get(2) = VImg(parts[32])
        aux.battle.get(2).get(3) = VImg(parts[33])
        aux.battle.get(2).get(4) = VImg(parts[38])
        // money, lv, lv dark,cost,cost dark,hp, money light,time,point
    }
}
