package common.battle.data

abstract class DefaultData : DataEntity() {
    var proc: Proc? = null
    var lds = 0
    var ldr = 0
    var atk = 0
    var atk1 = 0
    var atk2 = 0
    protected var pre = 0
    protected var pre1 = 0
    protected var pre2 = 0
    protected var abi0 = 1
    protected var abi1 = 0
    protected var abi2 = 0
    protected var tba = 0
    protected var datks: Array<DataAtk>?
    var isrange = false
    override fun allAtk(): Int {
        return ((atk + atk1 + atk2) * (1 + proc!!.CRIT.prob * 0.01)) as Int
    }

    override fun getAllProc(): Proc? {
        return proc
    }

    override fun getAtkCount(): Int {
        return if (atk1 == 0) 1 else if (atk2 == 0) 2 else 3
    }

    override fun getAtkModel(ind: Int): MaskAtk? {
        return if (ind >= getAtkCount() || datks == null || ind >= datks!!.size) null else datks!![ind]
    }

    override fun getItv(): Int {
        val len: Int = getAnimLen()
        val post = len - getLongPre()
        return getLongPre() + Math.max(tba * 2 - 1, post)
    }

    override fun getPost(): Int {
        return getAnimLen() - getLongPre()
    }

    override fun getProc(): Proc? {
        return proc
    }

    override fun getRepAtk(): MaskAtk? {
        return datks!![0]
    }

    override fun getTBA(): Int {
        return getItv() - getAnimLen()
    }

    override fun isLD(): Boolean {
        return lds > 0 && ldr * lds >= 0
    }

    override fun isOmni(): Boolean {
        return lds > 0 && -ldr > lds
    }

    override fun isRange(): Boolean {
        return isrange
    }

    override fun rawAtkData(): Array<IntArray?> {
        val data = Array<IntArray?>(getAtkCount()) { IntArray(3) }
        data[0]!![0] = atk
        data[0]!![1] = pre
        data[0]!![2] = abi0
        if (atk1 == 0) return data
        data[1]!![0] = atk1
        data[1]!![1] = pre1 - pre
        data[1]!![2] = abi1
        if (atk2 == 0) return data
        data[2]!![0] = atk2
        data[2]!![1] = pre2 - pre1
        data[2]!![2] = abi2
        return data
    }

    override fun touchBase(): Int {
        return if (lds > 0) lds else range
    }

    protected fun getLongPre(): Int {
        if (pre2 > 0) return pre2
        return if (pre1 > 0) pre1 else pre
    }
}
