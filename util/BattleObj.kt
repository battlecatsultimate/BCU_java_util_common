package common.util

import common.CommonStatic
import common.io.BCUException
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.pack.Context.ErrType
import common.util.BattleObj
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.*
import java.util.function.BiConsumer
import kotlin.collections.set

/**
 * this class enables copy of an interconnected system. <br></br>
 * <br></br>
 * capable to copy: <br></br>
 * 1. Primary field <br></br>
 * 2. String field <br></br>
 * 3. Copible field <br></br>
 * 4. Array field of type 1~4 <br></br>
 * 5. Cloneable Collection and Map field with generic type of 1~4<br></br>
 * note: Collections are not Hashed <br></br>
 * exclusion:<br></br>
 * EAnimI (override)<br></br>
 * EneRand (update map reference) <br></br>
 */
@StaticPermitted(Admin.StaticPermitted.Type.TEMP)
@Strictfp
open class BattleObj : ImgCore(), Cloneable {
    protected var copy: BattleObj? = null
    public override fun clone(): BattleObj {
        val c = sysCopy()!!
        terminate()
        ARRMAP.clear()
        UNCHECKED.removeAll(OLD)
        for (cls in UNCHECKED) CommonStatic.ctx.printErr(ErrType.WARN, "Unchecked Class in Battle: $cls")
        OLD.addAll(UNCHECKED)
        UNCHECKED.clear()
        return c
    }

    /** BattleStatic also has this method but different return type  */
    fun conflict(): Int {
        return 0
    }

    /**
     * override this method to make your own copy mechanics if you don't want all
     * your fields copied <br></br>
     * <br></br>
     * this method is called to copy object's references to other objects
     */
    protected open fun performDeepCopy() {
        val lf = getField(javaClass)
        check(lf)
        for (f in lf) {
            if (f.name.startsWith(NONC)) continue
            try {
                f.isAccessible = true
                f[copy] = hardCopy(f[this])
            } catch (e3: IllegalAccessException) {
                e3.printStackTrace()
            }
        }
    }

    /**
     * override this method to flush all objects used<br></br>
     * <br></br>
     * this method is called recursively to flush all resources used
     */
    protected open fun terminate() {
        if (copy == null) return
        val temp = copy
        copy = null
        temp?.terminate()
        val lf = getField(javaClass)
        for (f in lf) {
            f.isAccessible = true
            val tc = f.type
            if (BattleObj::class.java.isAssignableFrom(tc)) {
                var f2: BattleObj? = null
                try {
                    f2 = f[this] as BattleObj
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
                f2?.terminate()
            }
            if (tc.isArray && BattleObj::class.java.isAssignableFrom(tc.componentType)) {
                var f2: Array<BattleObj?>? = null
                try {
                    f2 = f[this] as Array<BattleObj?>
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
                if (f2 != null) for (c in f2) c?.terminate()
            }
            if (MutableCollection::class.java.isAssignableFrom(tc)) {
                if (f.name == NONC) continue
                var f2: Collection<*>? = null
                try {
                    f2 = f[this] as Collection<*>
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
                if (f2 != null) for (c in f2) if (c != null && c is BattleObj) c.terminate()
            }
            if (MutableMap::class.java.isAssignableFrom(tc)) {
                if (f.name == NONC) continue
                var f2: Map<*, *>? = null
                try {
                    f2 = f[this] as Map<*, *>
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
                f2?.forEach(BiConsumer<*, *> { a: Any?, b: Any? ->
                    if (a != null && a is BattleObj) a.terminate()
                    if (b != null && b is BattleObj) b.terminate()
                })
            }
        }
    }

    /** this method is called to check that there isn't any unintended class  */
    private fun check(lf: List<Field>) {
        for (f in lf) {
            var obj: Any? = null
            try {
                obj = f[this]
            } catch (e1: IllegalAccessException) {
                e1.printStackTrace()
            }
            if (obj == null) continue
            val tc: Class<*> = obj.javaClass
            if (checkField(tc)) continue
            if (MutableCollection::class.java.isAssignableFrom(tc)) {
                val f2 = obj as Collection<*>
                for (o in f2) if (!checkField(o.javaClass)) UNCHECKED.add(o.javaClass)
                continue
            }
            if (MutableMap::class.java.isAssignableFrom(tc)) {
                val f2 = obj as Map<*, *>
                f2.forEach(BiConsumer<*, *> { a: Any, b: Any ->
                    if (!checkField(a.javaClass)) UNCHECKED.add(a.javaClass)
                    if (!checkField(b.javaClass)) UNCHECKED.add(b.javaClass)
                })
                continue
            }
            UNCHECKED.add(tc)
        }
    }

    /** make a copy of this object during systematic clone process  */
    private fun sysCopy(): BattleObj? {
        if (copy != null) return copy
        try {
            // copy primary types
            copy = super.clone() as BattleObj
        } catch (e: CloneNotSupportedException) {
            e.printStackTrace()
        }
        copy!!.copy = this
        performDeepCopy()
        return copy
    }

    companion object {
        const val NONC = "NONC_"
        private val EXCLUDE = arrayOf<Class<*>>(Number::class.java, String::class.java, Boolean::class.java, BattleStatic::class.java)
        private val OLD: MutableSet<Class<*>> = HashSet()
        private val UNCHECKED: MutableSet<Class<*>> = HashSet()
        private val ARRMAP: MutableMap<Int, Any> = HashMap()
        protected fun hardCopy(obj: Any?): Any? {
            if (obj == null) return null
            val c: Class<*> = obj.javaClass
            if (c.isPrimitive) return obj
            for (cls in EXCLUDE) if (cls.isAssignableFrom(c)) return obj
            if (obj is BattleObj) return obj.sysCopy()
            if (ARRMAP.containsKey(obj.hashCode())) return ARRMAP[obj.hashCode()]
            if (obj.javaClass.isArray) {
                val ans = java.lang.reflect.Array.newInstance(c.componentType, java.lang.reflect.Array.getLength(obj))
                for (i in 0 until java.lang.reflect.Array.getLength(ans)) java.lang.reflect.Array.set(ans, i, hardCopy(java.lang.reflect.Array.get(obj, i)))
                ARRMAP[obj.hashCode()] = ans
                return ans
            }
            if (MutableCollection::class.java.isAssignableFrom(c)) {
                val f2 = obj as Collection<*>
                var f3: MutableCollection<*>? = null
                try {
                    f3 = f2.javaClass.getConstructor().newInstance()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (f3 != null) for (o in f2) f3.add(hardCopy(o))
                return f3
            }
            if (MutableMap::class.java.isAssignableFrom(c)) {
                val f2 = obj as Map<*, *>
                var f3: MutableMap<*, *>? = null
                try {
                    f3 = f2.javaClass.getConstructor().newInstance()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val f4 = f3
                if (f4 != null) f2.forEach(BiConsumer<*, *> { k: Any?, v: Any? -> f4.put(hardCopy(k), hardCopy(v)) })
                return f3
            }
            throw BCUException("cannot copy class " + obj.javaClass)
        }

        private fun checkField(tc: Class<*>): Boolean {
            if (tc.isPrimitive) return true
            val b0 = BattleObj::class.java.isAssignableFrom(tc)
            val b1: Boolean = BattleStatic::class.java.isAssignableFrom(tc)
            if (b0 && b1) return false
            if (b0 || b1) return true
            for (cls in EXCLUDE) if (cls.isAssignableFrom(tc)) return true
            return if (tc.isArray) checkField(tc.componentType) else false
        }

        private fun getField(cls: Class<out BattleObj>): List<Field> {
            val fl: MutableList<Field> = ArrayList()
            val fs = cls.declaredFields
            for (f in fs) if (!Modifier.isStatic(f.modifiers)) {
                f.isAccessible = true
                fl.add(f)
            }
            var sc: Class<out BattleObj>? = null
            if (BattleObj::class.java.isAssignableFrom(cls) && BattleObj::class.java != cls.superclass) sc = cls.superclass as Class<out BattleObj>
            if (sc != null) fl.addAll(getField(sc))
            return fl
        }
    }
}
