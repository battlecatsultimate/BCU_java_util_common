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
import common.pack.IndexContainer.IndexCont
import common.pack.IndexContainer.Indexable
import common.pack.PackData
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.system.VImg
import common.system.fake.FakeImage
import common.util.anim.AnimD
import common.util.anim.ImgCut
import common.util.anim.MaAnim
import common.util.anim.MaModel
import common.util.pack.Soul.SoulType
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

@IndexCont(PackData::class)
class Soul(st: String?, i: Int) : AnimD<Soul?, SoulType?>(st), Indexable<PackData?, Soul?> {
    enum class SoulType : AnimType<Soul?, SoulType?> {
        DEF
    }

    private val id: PackData.Identifier<Soul>
    private val img: VImg
    override fun getID(): PackData.Identifier<Soul>? {
        return id
    }

    override fun getNum(): FakeImage? {
        return img.getImg()
    }

    override fun load() {
        loaded = true
        imgcut = ImgCut.Companion.newIns(str + ".imgcut")
        mamodel = MaModel.Companion.newIns(str + ".mamodel")
        anims = arrayOf<MaAnim>(MaAnim.Companion.newIns(str + ".maanim"))
        types = common.util.pack.Soul.SoulType.values()
        parts = imgcut.cut(img.getImg())
    }

    override fun toString(): String {
        return "soul_" + id.id
    }

    init {
        img = VImg(str + ".png")
        id = PackData.Identifier.Companion.parseInt<Soul>(i, Soul::class.java)
    }
}
