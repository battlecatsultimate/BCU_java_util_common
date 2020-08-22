package common.util.lang

import common.battle.data.DataEntity
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonClass
import common.io.json.JsonClass.NoTag
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

class LocaleCenter {
    interface Binder {
        fun getNameID(): String?
        fun getNameValue(): String?
        fun getTooltipID(): String?
        fun getToolTipValue(): String?
        fun refresh(): LocaleCenter.Binder
        fun setNameValue(str: String)
        fun setToolTipValue(str: String)
    }

    interface Displayable {
        fun getName(): String?
        fun getTooltip(): String
        fun setName(str: String)
        fun setTooltip(str: String)
    }

    @JsonClass(noTag = NoTag.LOAD)
    class DisplayItem : Displayable {
        var name: String? = null
        var tooltip: String? = null
        override fun getName(): String? {
            return name
        }

        override fun getTooltip(): String {
            return "<html><p width=\"500\">$tooltip</p></html>"
        }

        override fun setName(str: String) {
            name = str
        }

        override fun setTooltip(str: String) {
            tooltip = str
        }
    }

    class ObjBinder(val item: Displayable, val name: String, val func: BinderFunc) : LocaleCenter.Binder {
        interface BinderFunc {
            fun refresh(name: String?): LocaleCenter.Binder
        }

        override fun getNameID(): String? {
            return name
        }

        override fun getNameValue(): String? {
            return item.getName()
        }

        override fun getTooltipID(): String? {
            return name
        }

        override fun getToolTipValue(): String? {
            return item.getTooltip()
        }

        override fun refresh(): LocaleCenter.Binder {
            return func.refresh(name)
        }

        override fun setNameValue(str: String) {
            item.setName(str)
        }

        override fun setToolTipValue(str: String) {
            item.setTooltip(str)
        }
    }
}
