package common.system

import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import java.util.*

interface BasedCopable<T, B> : Cloneable, Copable<T?> {
    override fun copy(): T? {
        val base = map[javaClass] as B? ?: return null
        return copy(base)
    }

    fun copy(b: B): T

    companion object {
        @StaticPermitted(Admin.StaticPermitted.Type.TEMP)
        val map: Map<Class<*>, Any> = HashMap()
    }
}
