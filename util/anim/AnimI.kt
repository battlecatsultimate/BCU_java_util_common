package common.util.anim

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
import common.system.fake.FakeImage
import common.util.Animable
import common.util.BattleStatic
import common.util.anim.AnimI.AnimType
import common.util.lang.MultiLangCont
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

abstract class AnimI<A : AnimI<A, T>?, T> : Animable<A, T>(), BattleStatic where T : Enum<T>?, T : AnimType<A, T>? {
    interface AnimType<A : AnimI<A, T>?, T> where T : Enum<T>?, T : AnimType<A, T>?

    abstract fun check()
    abstract fun load()
    abstract fun names(): Array<String?>
    abstract fun parts(img: Int): FakeImage?
    abstract fun types(): Array<T>

    companion object {
        protected fun translate(vararg anim: AnimType<*, *>?): Array<String?> {
            val ans = arrayOfNulls<String>(anim.size)
            for (i in ans.indices) ans[i] = MultiLangCont.Companion.getStatic().ANIMNAME.getCont(anim[i])
            return ans
        }
    }

    init {
        anim = this as A
    }
}
