package common.util.stage

import common.CommonStatic
import common.CommonStatic.BCAuxAssets
import common.battle.BasisLU
import common.battle.BasisSet
import common.system.files.VFile
import common.util.stage.MapColc.DefMapColc
import java.util.*

object RandStage {
    fun getLU(att: Int): BasisLU {
        val aux: BCAuxAssets = CommonStatic.getBCAssets()
        var r = Math.random() * 100
        for (i in 0..9) r -= if (r < aux.randRep.get(att).get(i)) return BasisSet.Companion.current().sele.randomize(10 - i) else aux.randRep.get(att).get(i)
        return BasisSet.Companion.current().sele
    }

    fun getStage(sta: Int): Stage {
        val mc: DefMapColc = DefMapColc.Companion.getMap("N")
        if (sta == 47) return mc.maps.get(48).list.get(0)
        val l: MutableList<Stage> = ArrayList()
        l.addAll(mc.maps.get(sta).list)
        l.addAll(mc.maps.get(sta + 1).list)
        return l[(Math.random() * l.size).toInt()]
    }

    fun read() {
        val qs: Queue<String> = VFile.Companion.readLine("./org/stage/D/RandomDungeon_000.csv")
        for (i in 0..4) CommonStatic.getBCAssets().randRep.get(i) = CommonStatic.parseIntsN(qs.poll())
    }
}
