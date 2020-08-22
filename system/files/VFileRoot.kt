package common.system.files

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

class VFileRoot<T : FileData?>(str: String) : VFile<T>(str) {
    fun build(str: String, fd: T?): VFile<T>? {
        val strs = str.split("/|\\\\").toTypedArray()
        var par: VFile<T> = this
        for (i in 1 until strs.size) {
            var next: VFile<T>? = null
            for (ch in par.list()) if (ch.name == strs[i]) next = ch
            if (next == null) if (i == strs.size - 1) return if (fd != null) VFile<T>(par, strs[i], fd) else VFile<T>(par, strs[i]) else next = VFile<T>(par, strs[i])
            if (i == strs.size - 1) {
                if (fd == null) return next
                next.setData(fd)
                return next
            }
            par = next
        }
        return null
    }

    fun find(str: String): VFile<T>? {
        val strs = str.split("/|\\\\").toTypedArray()
        var par: VFile<T> = this
        for (i in 1 until strs.size) {
            var next: VFile<T>? = null
            for (ch in par.list()) if (ch.name == strs[i]) next = ch
            if (next == null) return null
            if (i == strs.size - 1) return next
            par = next
        }
        return this
    }
}
