package common.util.stage

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
import common.pack.IndexContainer
import common.pack.IndexContainer.ContGetter
import common.pack.IndexContainer.Reductor
import common.pack.PackData.UserPack
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.pack.UserProfile
import common.system.VImg
import common.system.files.VFile
import common.util.stage.CastleList
import common.util.stage.EStage
import common.util.stage.MapColc.PackMapColc
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
import java.util.*

abstract class CastleList private constructor() : FixIndexMap<CastleImg?>(CastleImg::class.java), IndexContainer {
    class DefCasList(val id: String, val str: String) : CastleList() {
        override fun getID(): String? {
            return id
        }

        override fun toString(): String {
            return str + " (" + size() + ")"
        }

        init {
            map()[id] = this
            defset().add(this)
            for (vf in VFile.Companion.get("./org/img/$str").list()) add(CastleImg(getNextID<CastleImg, CastleImg>(CastleImg::class.java), VImg(vf)))
        }
    }

    class PackCasList(p: UserPack) : CastleList() {
        val pack: UserPack
        override fun getID(): String? {
            return pack.getID()
        }

        override fun toString(): String {
            return pack.desc.name + " (" + size() + ")"
        }

        init {
            pack = p
            map()[pack.desc.id] = this
        }
    }

    override fun <R> getList(cls: Class<*>?, func: Reductor<R, FixIndexMap<*>?>, def: R): R {
        return func.reduce(def, this)
    }

    companion object {
        private const val REG_CASTLE = "castle"
        private const val REG_DEF_CASTLE = "def_castle"
        fun defset(): MutableSet<CastleList> {
            return UserProfile.Companion.getPool<CastleList>(REG_DEF_CASTLE, CastleList::class.java)
        }

        fun from(sta: Stage): Collection<CastleList> {
            val mc: MapColc = sta.map.mc as? PackMapColc ?: return defset()
            val list: MutableList<CastleList> = ArrayList()
            list.addAll(defset())
            val pack: UserPack = (mc as PackMapColc).pack
            list.add(pack.castles)
            for (rel in pack.desc.dependency) list.add(UserProfile.Companion.getUserPack(rel).castles)
            return list
        }

        @ContGetter
        fun getList(str: String): CastleList? {
            return map()[str]
        }

        fun map(): MutableMap<String, CastleList> {
            return UserProfile.Companion.getRegister<CastleList>(REG_CASTLE, CastleList::class.java)
        }
    }
}
