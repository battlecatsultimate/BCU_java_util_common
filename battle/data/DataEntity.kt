package common.battle.data

import common.io.json.JsonClass
import common.io.json.JsonClass.NoTag
import common.pack.PackData
import common.util.Data
import common.util.pack.Soul

@JsonClass(noTag = NoTag.LOAD)
abstract class DataEntity : Data(), MaskEntity {
    var hp = 0
    var hb = 0
    var speed = 0
    var range = 0
    var abi = 0
    var type = 0
    var width = 0
    var loop = -1
    var shield = 0
    var death: PackData.Identifier<Soul>? = null
    override fun getAbi(): Int {
        return abi
    }

    override fun getAtkLoop(): Int {
        return loop
    }

    override fun getDeathAnim(): PackData.Identifier<Soul>? {
        return death
    }

    override fun getHb(): Int {
        return hb
    }

    override fun getHp(): Int {
        return hp
    }

    override fun getRange(): Int {
        return range
    }

    override fun getShield(): Int {
        return shield
    }

    override fun getSpeed(): Int {
        return speed
    }

    override fun getType(): Int {
        return type
    }

    override fun getWidth(): Int {
        return width
    }
}
