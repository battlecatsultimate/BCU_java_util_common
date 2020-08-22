package common.battle.data

import common.pack.PackData
import common.util.Animable
import common.util.BattleStatic
import common.util.Data
import common.util.Data.Proc
import common.util.anim.AnimU
import common.util.anim.AnimU.UType
import common.util.pack.Soul

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
