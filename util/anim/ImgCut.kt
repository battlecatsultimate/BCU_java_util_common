package common.util.anim

import common.CommonStatic
import common.io.InStream
import common.io.OutStream
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
import common.system.files.FileData
import common.util.Data
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
import java.io.PrintStream
import java.util.*
import java.util.function.Function

class ImgCut : Data, Cloneable {
    var name: String? = null
    var n: Int
    var cuts: Array<IntArray?>
    var strs: Array<String?>

    constructor() {
        n = 1
        cuts = arrayOf(intArrayOf(0, 0, 1, 1))
        strs = arrayOf("default")
    }

    protected constructor(qs: Queue<String>) {
        qs.poll()
        qs.poll()
        name = Data.Companion.restrict(qs.poll())
        n = qs.poll().trim { it <= ' ' }.toInt()
        cuts = Array(n) { IntArray(4) }
        strs = arrayOfNulls(n)
        for (i in 0 until n) {
            val ss = qs.poll().trim { it <= ' ' }.split(",").toTypedArray()
            for (j in 0..3) cuts[i]!![j] = ss[j].trim { it <= ' ' }.toInt()
            if (ss.size == 5) strs[i] = Data.Companion.restrict(ss[4]) else strs[i] = ""
        }
    }

    private constructor(ic: ImgCut) {
        name = ic.name
        n = ic.n
        cuts = arrayOfNulls(n)
        for (i in 0 until n) cuts[i] = ic.cuts[i]!!.clone()
        strs = ic.strs.clone()
    }

    public override fun clone(): ImgCut {
        return ImgCut(this)
    }

    fun cut(bimg: FakeImage?): Array<FakeImage?> {
        val w: Int = bimg.getWidth()
        val h: Int = bimg.getHeight()
        val parts: Array<FakeImage?> = arrayOfNulls<FakeImage>(n)
        for (i in 0 until n) {
            val cut = cuts[i]!!.clone()
            if (cut[0] < 0) cut[0] = 0
            if (cut[1] < 0) cut[1] = 0
            if (cut[0] > w - 2) cut[0] = w - 2
            if (cut[1] > h - 2) cut[1] = h - 2
            if (cut[2] <= 0) cut[2] = 1
            if (cut[3] <= 0) cut[3] = 1
            if (cut[2] + cut[0] > w - 1) cut[2] = w - 1 - cut[0]
            if (cut[3] + cut[1] > h - 1) cut[3] = h - 1 - cut[1]
            parts[i] = bimg.getSubimage(cut[0], cut[1], cut[2], cut[3])
        }
        return parts
    }

    fun write(ps: PrintStream) {
        ps.println("[imgcut]")
        ps.println("0")
        ps.println(name)
        ps.println(n)
        for (i in 0 until n) {
            for (j in 0..3) ps.print(cuts[i]!![j].toString() + ",")
            ps.println(strs[i])
        }
    }

    fun restore(`is`: InStream) {
        n = `is`.nextInt()
        cuts = `is`.nextIntsBB()
        strs = arrayOfNulls(n)
        for (i in 0 until n) strs[i] = `is`.nextString()
    }

    fun write(os: OutStream) {
        os.writeInt(n)
        os.writeIntBB(cuts)
        for (str in strs) os.writeString(str)
    }

    companion object {
        fun newIns(f: FileData): ImgCut {
            return try {
                ImgCut(f.readLine())
            } catch (e: Exception) {
                e.printStackTrace()
                ImgCut()
            }
        }

        fun newIns(path: String?): ImgCut {
            return CommonStatic.def.readSave<ImgCut>(path, Function { f: Queue<String>? ->
                f?.let { ImgCut(it) } ?: ImgCut()
            })
        }
    }
}
