package common.battle.data

import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonEncoder
import common.io.json.Test
import common.io.json.Test.JsonTest_0.JsonD
import common.io.json.Test.JsonTest_2
import common.pack.PackData
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.util.Animable
import common.util.BattleStatic
import common.util.Data
import common.util.Data.Proc
import common.util.anim.AnimU
import common.util.anim.AnimU.UType
import common.util.pack.Soul
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

interface MaskEntity : BattleStatic {
    fun allAtk(): Int
    fun getAbi(): Int
    fun getAllProc(): Proc?

    /** get the attack animation length  */
    fun getAnimLen(): Int {
        return getPack().anim.getAtkLen()
    }

    fun getAtkCount(): Int
    fun getAtkLoop(): Int
    fun getAtkModel(ind: Int): MaskAtk?
    fun getDeathAnim(): PackData.Identifier<Soul>?
    fun getHb(): Int
    fun getHp(): Int

    /** get the attack period  */
    fun getItv(): Int

    /** get the Enemy/Form this data represents  */
    fun getPack(): Animable<AnimU<*>?, UType?>?
    fun getPost(): Int
    fun getProc(): Proc?
    fun getRange(): Int
    fun getRepAtk(): MaskAtk?
    fun getResurrection(): AtkDataModel? {
        return null
    }

    fun getRevenge(): AtkDataModel? {
        return null
    }

    fun getShield(): Int
    fun getSpeed(): Int

    /** get waiting time  */
    fun getTBA(): Int
    fun getTouch(): Int {
        return Data.Companion.TCH_N
    }

    fun getType(): Int
    fun getWidth(): Int
    fun isLD(): Boolean
    fun isOmni(): Boolean
    fun isRange(): Boolean
    fun rawAtkData(): Array<IntArray?>
    fun touchBase(): Int
}
