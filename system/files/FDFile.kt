package common.system.files

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
import common.pack.Context.ErrType
import common.pack.Context.SupExc
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.system.fake.FakeImage
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
import java.io.*

class FDFile(private val file: File) : FileData {
    override fun getBytes(): ByteArray? {
        return try {
            val bs = ByteArray(file.length().toInt())
            val buf = BufferedInputStream(FileInputStream(file))
            buf.read(bs, 0, bs.size)
            buf.close()
            bs
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun getImg(): FakeImage {
        return Data.Companion.err<FakeImage>(SupExc<FakeImage> { FakeImage.Companion.read(file) })
    }

    override fun getStream(): InputStream {
        return CommonStatic.ctx.noticeErr<FileInputStream>(SupExc<FileInputStream> { FileInputStream(file) }, ErrType.ERROR,
                "failed to read bcuzip at $file")
    }

    override fun size(): Int {
        return file.length().toInt()
    }

    override fun toString(): String {
        return file.name
    }
}
