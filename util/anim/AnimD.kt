package common.util.anim

import common.system.fake.FakeImage
import common.util.anim.AnimI
import common.util.anim.AnimI.AnimType

abstract class AnimD<A : AnimD<A, T>?, T>(protected val str: String) : AnimI<A, T>() where T : Enum<T>?, T : AnimType<A, T>? {
    var imgcut: ImgCut? = null
    var mamodel: MaModel? = null
    var types: Array<T>
    var anims: Array<MaAnim>
    var parts: Array<FakeImage?>?
    var mismatch = false
    protected var loaded = false
    override fun check() {
        if (!loaded) load()
    }

    override fun getEAnim(t: T): EAnimD<T>? {
        check()
        if (mamodel == null) return null
        val anim: MaAnim? = getMaAnim(t)
        return if (anim == null) null else EAnimD<T>(this, mamodel, anim)
    }

    fun getMaAnim(t: T): MaAnim? {
        for (i in types.indices) if (types[i] === t) return anims[i]
        return null
    }

    abstract fun getNum(): FakeImage?
    fun len(t: T): Int {
        check()
        return getMaAnim(t)!!.max + 1
    }

    abstract override fun load()
    override fun names(): Array<String?> {
        check()
        return AnimI.Companion.translate(*types)
    }

    override fun parts(i: Int): FakeImage? {
        return if (i < 0 || i >= parts!!.size) null else parts!![i]
    }

    fun reorderModel(inds: IntArray) {
        for (ints in mamodel!!.parts) if (ints != null && ints[0] >= 0) ints[0] = inds[ints[0]]
        for (ma in anims) for (part in ma.parts) part!!.ints[0] = inds[part!!.ints[0]]
    }

    open fun revert() {
        mamodel!!.revert()
        for (ma in anims) if (ma != null) ma.revert()
    }

    override fun types(): Array<T> {
        check()
        return types
    }

    open fun unload() {
        if (parts != null) {
            for (i in parts.indices) {
                if (parts!![i] != null) {
                    parts!![i].unload()
                    parts!![i] = null
                }
            }
        }
        parts = null
        loaded = false
    }

    fun validate() {
        check()
        mamodel!!.check(this)
        for (ma in anims) {
            for (p in ma.parts) {
                p!!.check(this)
                p!!.validate()
            }
            ma.validate()
        }
    }
}
