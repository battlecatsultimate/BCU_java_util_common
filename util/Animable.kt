package common.util

import common.util.anim.AnimI
import common.util.anim.AnimI.AnimType
import common.util.anim.EAnimI

abstract class Animable<A : AnimI<A, T>?, T> : ImgCore() where T : Enum<T>?, T : AnimType<A, T>? {
    var anim: A? = null
    abstract fun getEAnim(t: T): EAnimI?
}
