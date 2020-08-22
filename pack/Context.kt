package common.pack

import common.battle.data.DataEntity
import common.io.PackLoader.ZipDesc.FileDesc
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonEncoder
import common.io.json.Test
import common.io.json.Test.JsonTest_0.JsonD
import common.io.json.Test.JsonTest_2
import common.pack.Context
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
import java.io.File
import java.io.IOException

interface Context {
    enum class ErrType {
        FATAL, ERROR, WARN, INFO, NEW, CORRUPT
    }

    interface RunExc {
        @Throws(Exception::class)
        fun run()
    }

    interface SupExc<T> {
        @Throws(Exception::class)
        fun get(): T
    }

    fun confirmDelete(): Boolean
    fun getAssetFile(string: String): File
    fun getLangFile(file: String): File
    fun getPackFolder(): File
    fun getUserFile(string: String): File
    fun getWorkspaceFile(relativePath: String): File
    fun initProfile()
    fun noticeErr(r: Context.RunExc, t: ErrType, str: String?): Boolean {
        return try {
            r.run()
            true
        } catch (e: Exception) {
            noticeErr(e, t, str)
            false
        }
    }

    fun <T> noticeErr(r: SupExc<T>, t: ErrType, str: String?): T? {
        return try {
            r.get()
        } catch (e: Exception) {
            noticeErr(e, t, str)
            null
        }
    }

    fun noticeErr(e: Exception, t: ErrType, str: String?)
    fun preload(desc: FileDesc): Boolean
    fun printErr(t: ErrType, str: String?)

    companion object {
        @Throws(IOException::class)
        fun check(bool: Boolean, str: String, f: File) {
            if (!bool) throw IOException("failed to $str file $f")
        }

        @Throws(IOException::class)
        fun check(f: File) {
            if (!f.parentFile.exists()) Context.Companion.check(f.parentFile.mkdirs(), "create", f)
            if (!f.exists()) Context.Companion.check(f.createNewFile(), "create", f)
        }

        @Throws(IOException::class)
        fun delete(f: File?) {
            if (f == null || !f.exists()) return
            if (f.isDirectory) for (i in f.listFiles()) Context.Companion.delete(i)
            Context.Companion.check(f.delete(), "delete", f)
        }
    }
}
