package common.battle.data

import common.util.Data.Proc

class DataAtk(data: DefaultData, val index: Int) : MaskAtk {
    val data: DefaultData
    override fun getAtk(): Int {
        return when (index) {
            0 -> data.atk
            1 -> data.atk1
            2 -> data.atk2
            else -> 0
        }
    }

    override fun getLongPoint(): Int {
        return data.lds + data.ldr
    }

    override fun getProc(): Proc? {
        return data.proc
    }

    override fun getShortPoint(): Int {
        return data.lds
    }

    override fun isRange(): Boolean {
        return data.isrange
    }

    init {
        this.data = data
    }
}
