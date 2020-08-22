package common.util.unit

import common.io.json.JsonClass
import common.io.json.JsonClass.NoTag
import common.util.BattleStatic

@JsonClass(noTag = NoTag.LOAD)
class Level : BattleStatic {
    private var lvs = intArrayOf(1, 0, 0, 0, 0, 0)
    private var orbs: Array<IntArray?>? = null

    constructor()
    constructor(lvs: IntArray) {
        if (lvs.size == 6) {
            this.lvs = lvs
        } else {
            this.lvs = intArrayOf(1, 0, 0, 0, 0, 0)
        }
    }

    constructor(lvs: IntArray, orbs: Array<IntArray?>?) {
        if (lvs.size == 6) {
            this.lvs = lvs
        } else {
            this.lvs = intArrayOf(1, 0, 0, 0, 0, 0)
        }
        if (orbs == null) {
            return
        }
        var valid = true
        for (data in orbs) {
            if (data == null) {
                valid = false
                break
            }
            if (data.size == 0) {
                continue
            }
            if (data.size != 3) {
                valid = false
                break
            }
        }
        if (valid) {
            this.orbs = orbs
        }
    }

    override fun clone(): Level {
        return if (orbs != null) Level(lvs.clone(), orbs!!.clone()) else Level(lvs.clone())
    }

    fun getLvs(): IntArray {
        return lvs
    }

    fun getOrbs(): Array<IntArray?>? {
        return orbs
    }

    fun setLvs(lv: IntArray) {
        if (lv.size == 6) {
            lvs = lv
        }
    }

    fun setOrbs(orb: Array<IntArray?>?) {
        if (orb == null) {
            orbs = orb
            return
        }
        var valid = true
        for (data in orb) {
            if (data == null) {
                valid = false
                break
            }
            if (data.size == 0) {
                continue
            }
            if (data.size != 3) {
                valid = false
                break
            }
        }
        if (valid) {
            orbs = orb
        }
    }
}
