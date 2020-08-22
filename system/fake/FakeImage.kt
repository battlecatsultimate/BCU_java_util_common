package common.system.fake

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
import common.system.fake.ImageBuilder
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
import java.io.IOException

interface FakeImage {
    enum class Marker {
        BG, EDI, UNI, RECOLOR, RECOLORED
    }

    fun bimg(): Any?
    fun getHeight(): Int
    fun getRGB(i: Int, j: Int): Int
    fun getSubimage(i: Int, j: Int, k: Int, l: Int): FakeImage?
    fun getWidth(): Int
    fun gl(): Any?
    fun isValid(): Boolean
    fun mark(m: Marker) {}
    fun setRGB(i: Int, j: Int, p: Int)
    fun unload()

    companion object {
        @Throws(IOException::class)
        fun read(o: Any?): FakeImage? {
            return ImageBuilder.Companion.builder.build(o)
        }

        @Throws(IOException::class)
        fun write(img: FakeImage?, str: String?, o: Any?): Boolean {
            return ImageBuilder.Companion.builder.write(img, str, o)
        }
    }
}
