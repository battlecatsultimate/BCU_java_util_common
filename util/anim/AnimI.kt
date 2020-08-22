package common.util.anim

import common.system.fake.FakeImage
import common.util.Animable
import common.util.BattleStatic
import common.util.anim.AnimI.AnimType
import common.util.lang.MultiLangCont

abstract class AnimI<A : AnimI<A, T>?, T> : Animable<A, T>(), BattleStatic where T : Enum<T>?, T : AnimType<A, T>? {
    interface AnimType<A : AnimI<A, T>?, T> where T : Enum<T>?, T : AnimType<A, T>?

    abstract fun check()
    abstract fun load()
    abstract fun names(): Array<String?>
    abstract fun parts(img: Int): FakeImage?
    abstract fun types(): Array<T>

    companion object {
        protected fun translate(vararg anim: AnimType<*, *>?): Array<String?> {
            val ans = arrayOfNulls<String>(anim.size)
            for (i in ans.indices) ans[i] = MultiLangCont.Companion.getStatic().ANIMNAME.getCont(anim[i])
            return ans
        }
    }

    init {
        anim = this as A
    }
}
