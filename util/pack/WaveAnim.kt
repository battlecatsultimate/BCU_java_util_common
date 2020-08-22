package common.util.pack

import common.battle.data.DataEntity
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
import common.util.anim.AnimI
import common.util.anim.EAnimD
import common.util.anim.MaAnim
import common.util.anim.MaModel
import common.util.pack.WaveAnim.WaveType
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

class WaveAnim(private val bg: Background, model: MaModel, anim: MaAnim) : AnimI<WaveAnim?, WaveType?>() {
    enum class WaveType : AnimType<WaveAnim?, WaveType?> {
        DEF
    }

    private val mamodel: MaModel
    private val maanim: MaAnim
    private var parts: Array<FakeImage>?
    override fun check() {
        if (parts == null) load()
    }

    override fun getEAnim(t: WaveType): EAnimD<WaveType>? {
        return EAnimD<WaveType>(this, mamodel, maanim)
    }

    override fun load() {
        bg.check()
        parts = bg.parts
    }

    override fun names(): Array<String?> {
        return AnimI.Companion.translate(WaveType.DEF)
    }

    override fun parts(i: Int): FakeImage? {
        check()
        return parts!![i]
    }

    override fun types(): Array<WaveType> {
        return common.util.pack.WaveAnim.WaveType.values()
    }

    init {
        mamodel = model
        maanim = anim
    }
}
