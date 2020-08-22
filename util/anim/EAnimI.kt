package common.util.anim

import common.system.P
import common.system.fake.FakeGraphics
import common.util.BattleObj

abstract class EAnimI(ia: AnimI<*, *>, mm: MaModel) : BattleObj() {
    var sele = -1
    var ent: Array<EPart?>? = null
    protected val a: AnimI<*, *>
    protected val mamodel: MaModel
    protected var order: Array<EPart?>
    open fun anim(): AnimI<*, *> {
        return a
    }

    abstract fun draw(g: FakeGraphics, ori: P, siz: Double)
    abstract fun ind(): Int
    abstract fun len(): Int
    fun organize() {
        ent = mamodel.arrange(this)
        order = arrayOfNulls<EPart>(ent!!.size)
        for (i in ent!!.indices) {
            ent!![i]!!.ea = this
            order[i] = ent!![i]
        }
        sort()
    }

    abstract fun setTime(value: Int)
    abstract fun update(b: Boolean)
    override fun performDeepCopy() {
        (copy as EAnimI).organize()
    }

    fun sort() {
        sort(order, 0, order.size - 1)
    }

    override fun terminate() {
        copy = null
    }

    companion object {
        private fun sort(arr: Array<EPart?>, low: Int, high: Int) {
            if (low < high) {
                val pivot: EPart = arr[(low + high) / 2]!!
                var i = low - 1
                var j = high + 1
                while (i < j) {
                    while (arr[++i]!!.compareTo(pivot) < 0);
                    while (arr[--j]!!.compareTo(pivot) > 0);
                    if (i >= j) break
                    val tmp: EPart? = arr[i]
                    arr[i] = arr[j]
                    arr[j] = tmp
                }
                sort(arr, low, j)
                sort(arr, j + 1, high)
            }
        }
    }

    init {
        a = ia
        mamodel = mm
        organize()
    }
}
