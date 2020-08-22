package common.pack

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
import common.pack.FixIndexList.FixIndexMap
import common.pack.PackData
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
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import kotlin.reflect.KClass

interface IndexContainer {
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
    annotation class ContGetter
    interface Indexable<R : IndexContainer?, T : Indexable<R, T>?> {
        fun getCont(): R {
            return getID()!!.getCont() as R
        }

        fun getID(): PackData.Identifier<T>?
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
    annotation class IndexCont(val value: KClass<out IndexContainer?>)
    interface Reductor<R, T> {
        fun reduce(r: R, t: T): R
    }

    fun getID(): String?
    fun <R> getList(cls: Class<*>?, func: Reductor<R, FixIndexMap<*>?>?, def: R): R
    fun <T : R?, R : Indexable<*, R>?> getNextID(cls: Class<T>?): PackData.Identifier<R>? {
        val id = getList(cls, Reductor<Int, FixIndexMap<*>> { r: Int?, l: FixIndexMap<*> -> l.nextInd() }, 0)
        return PackData.Identifier(getID(), cls, id)
    }
}
