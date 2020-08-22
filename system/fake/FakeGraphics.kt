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

interface FakeGraphics {
    fun colRect(x: Int, y: Int, w: Int, h: Int, r: Int, g: Int, b: Int, a: Int)
    fun delete(at: FakeTransform?) {}
    fun drawImage(bimg: FakeImage, x: Double, y: Double)
    fun drawImage(bimg: FakeImage, x: Double, y: Double, d: Double, e: Double)
    fun drawLine(i: Int, j: Int, x: Int, y: Int)
    fun drawOval(i: Int, j: Int, k: Int, l: Int)
    fun drawRect(x: Int, y: Int, x2: Int, y2: Int)
    fun fillOval(i: Int, j: Int, k: Int, l: Int)
    fun fillRect(x: Int, y: Int, w: Int, h: Int)
    fun getTransform(): FakeTransform
    fun gradRect(x: Int, y: Int, w: Int, h: Int, a: Int, b: Int, c: IntArray, d: Int, e: Int, f: IntArray)
    fun rotate(d: Double)
    fun scale(hf: Int, vf: Int)
    fun setColor(c: Int)
    fun setComposite(mode: Int, p0: Int, p1: Int)
    fun setRenderingHint(key: Int, `object`: Int)
    fun setTransform(at: FakeTransform)
    fun translate(x: Double, y: Double)

    companion object {
        const val RED = 0
        const val YELLOW = 1
        const val BLACK = 2
        const val MAGENTA = 3
        const val BLUE = 4
        const val CYAN = 5
        const val WHITE = 6
        const val DEF = 0
        const val TRANS = 1
        const val BLEND = 2
        const val GRAY = 3
    }
}
