package common.io.json

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.json.JsonClass.*
import common.io.json.JsonDecoder
import common.io.json.JsonException
import common.io.json.JsonField
import common.io.json.JsonField.GenType
import common.io.json.JsonField.IOType
import common.pack.Context.SupExc
import common.util.Data
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.reflect.*
import java.lang.reflect.Array
import java.util.*

class JsonDecoder private constructor(parent: JsonDecoder?, json: JsonObject, cls: Class<*>, pre: Any) {
    interface Decoder {
        @Throws(Exception::class)
        fun decode(elem: JsonElement?): Any?
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
    annotation class OnInjected
    companion object {
        @StaticPermitted
        val REGISTER: MutableMap<Class<*>, common.io.json.JsonDecoder.Decoder> = HashMap()

        @StaticPermitted(Admin.StaticPermitted.Type.TEMP)
        private var current: JsonDecoder
        @Throws(Exception::class)
        fun decode(elem: JsonElement, cls: Class<*>, par: JsonDecoder?): Any? {
            if (elem.isJsonNull) return null
            if (JsonElement::class.java.isAssignableFrom(cls)) return elem
            val dec = REGISTER[cls]
            if (dec != null) return dec.decode(elem)
            if (cls.isArray) return decodeArray(elem, cls, par)
            if (MutableList::class.java.isAssignableFrom(cls)) return decodeList(elem, cls, par)
            if (MutableMap::class.java.isAssignableFrom(cls)) return decodeMap(elem, cls, par)
            if (MutableSet::class.java.isAssignableFrom(cls)) return decodeSet(elem, cls, par)
            // alias
            if (cls.getAnnotation(JCGeneric::class.java) != null && par != null && par.curjfld!!.alias().size > par.index) {
                val jcg: JCGeneric = cls.getAnnotation(JCGeneric::class.java)
                val alias: Class<*> = par.curjfld!!.alias().get(par.index)
                var found = false
                for (ala in jcg.value()) if (ala == alias) {
                    found = true
                    break
                }
                if (!found) throw JsonException(JsonException.Type.TYPE_MISMATCH, null, "class not present in JCGeneric")
                val input = decode(elem, alias, par)
                for (m in alias.declaredMethods) if (m.getAnnotation(JCGetter::class.java) != null) return m.invoke(input)
                throw JsonException(JsonException.Type.TYPE_MISMATCH, null, "no JCGenericRead present")
            }
            // fill existing object
            if (par != null && par.curjfld!!.gen() == GenType.FILL) {
                val `val` = par.curfld!![par.obj]
                return if (cls.getAnnotation(JsonClass::class.java) != null) inject(par, elem.asJsonObject, cls, `val`) else `val`
            }
            // generator
            if (par != null && par.curjfld!!.gen() == GenType.GEN) {
                val ccls: Class<*> = par.obj!!.javaClass
                // default generator
                if (par.curjfld!!.generator().length == 0) {
                    var cst: Constructor<*>? = null
                    for (ci in cls.declaredConstructors) if (ci.parameterCount == 1 && ci.parameters[0].type.isAssignableFrom(ccls)) cst = ci
                    if (cst == null) throw JsonException(JsonException.Type.FUNC, null, "no constructor found: $cls")
                    val `val` = cst.newInstance(par.obj)
                    return inject(par, elem.asJsonObject, cls, `val`)
                }
                // functional generator
                val m = ccls.getMethod(par.curjfld!!.generator(), Class::class.java, JsonElement::class.java)
                return m.invoke(par.obj, cls, elem)
            }
            if (cls.getAnnotation(JsonClass::class.java) != null) return decodeObject(elem, cls, par)
            throw JsonException(JsonException.Type.UNDEFINED, elem, "class not possible to generate")
        }

        fun <T> decode(elem: JsonElement, cls: Class<T>): T {
            return Data.Companion.err<Any>(SupExc<Any> { decode(elem, cls, null) })
        }

        @Throws(JsonException::class)
        fun getBoolean(elem: JsonElement): Boolean {
            if (!elem.isJsonPrimitive || !(elem as JsonPrimitive).isBoolean) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not boolean")
            return elem.getAsBoolean()
        }

        @Throws(JsonException::class)
        fun getByte(elem: JsonElement): Byte {
            if (!elem.isJsonPrimitive || !(elem as JsonPrimitive).isNumber) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not number")
            return elem.getAsByte()
        }

        @Throws(JsonException::class)
        fun getDouble(elem: JsonElement): Double {
            if (!elem.isJsonPrimitive || !(elem as JsonPrimitive).isNumber) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not number")
            return elem.getAsDouble()
        }

        @Throws(JsonException::class)
        fun getFloat(elem: JsonElement): Float {
            if (!elem.isJsonPrimitive || !(elem as JsonPrimitive).isNumber) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not number")
            return elem.getAsFloat()
        }

        fun <T> getGlobal(cls: Class<T>): T? {
            return getGlobal(current, cls) as T?
        }

        @Throws(JsonException::class)
        fun getInt(elem: JsonElement): Int {
            if (!elem.isJsonPrimitive || !(elem as JsonPrimitive).isNumber) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not number")
            return elem.getAsInt()
        }

        @Throws(JsonException::class)
        fun getLong(elem: JsonElement): Long {
            if (!elem.isJsonPrimitive || !(elem as JsonPrimitive).isNumber) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not number")
            return elem.getAsLong()
        }

        @Throws(JsonException::class)
        fun getShort(elem: JsonElement): Short {
            if (!elem.isJsonPrimitive || !(elem as JsonPrimitive).isNumber) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not number")
            return elem.getAsShort()
        }

        @Throws(JsonException::class)
        fun getString(elem: JsonElement): String? {
            if (elem.isJsonNull) return null
            if (elem.isJsonArray) {
                var ans = ""
                val arr: JsonArray = elem.asJsonArray
                for (i in 0 until arr.size()) ans += arr.get(i).asString
                return ans
            }
            if (!elem.isJsonPrimitive || !(elem as JsonPrimitive).isString) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not string")
            return elem.getAsString()
        }

        @Throws(Exception::class)
        fun <T> inject(elem: JsonElement, cls: Class<T>, pre: T): T? {
            return inject(null, elem.asJsonObject, cls, pre) as T?
        }

        @Throws(Exception::class)
        protected fun decodeList(elem: JsonElement, cls: Class<*>, par: JsonDecoder?): List<Any?>? {
            if (par!!.curjfld == null || par.curjfld.generic.size != 1) throw JsonException(JsonException.Type.TAG, null, "generic data structure requires typeProvider tag")
            if (elem.isJsonNull) return null
            val `val` = cls.newInstance() as MutableList<Any?>
            if (elem.isJsonObject && par.curjfld.usePool) {
                val pool: JsonArray = elem.asJsonObject.get("pool").asJsonArray
                val data: JsonArray = elem.asJsonObject.get("data").asJsonArray
                val handler = JsonField.Handler(pool, null, par)
                val n: Int = data.size()
                for (i in 0 until n) `val`.add(handler[data.get(i).asInt])
                return `val`
            }
            if (!elem.isJsonArray) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not array")
            val jarr: JsonArray = elem.asJsonArray
            val n: Int = jarr.size()
            for (i in 0 until n) {
                `val`.add(decode(jarr.get(i), par.curjfld.generic().get(0), par))
            }
            return `val`
        }

        @Throws(Exception::class)
        private fun decodeArray(elem: JsonElement, cls: Class<*>, par: JsonDecoder?): Any {
            val ccls = cls.componentType
            val jf: JsonField? = par?.curjfld
            if (elem.isJsonObject && jf != null && jf.usePool()) {
                val pool: JsonArray = elem.asJsonObject.get("pool").asJsonArray
                val data: JsonArray = elem.asJsonObject.get("data").asJsonArray
                val handler = JsonField.Handler(pool, ccls, par)
                val n: Int = data.size()
                val arr = getArray(ccls, n, par)
                for (i in 0 until n) Array.set(arr, i, handler[data.get(i).asInt])
                return arr
            }
            if (!elem.isJsonArray) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not array")
            val jarr: JsonArray = elem.asJsonArray
            val n: Int = jarr.size()
            val arr = getArray(ccls, n, par)
            for (i in 0 until n) Array.set(arr, i, decode(jarr.get(i), ccls, par))
            return arr
        }

        @Throws(Exception::class)
        private fun decodeMap(elem: JsonElement, cls: Class<*>, par: JsonDecoder?): Map<Any?, Any?>? {
            if (par!!.curjfld == null || par.curjfld.generic().size != 2) throw JsonException(JsonException.Type.TAG, null, "generic data structure requires typeProvider tag")
            if (elem.isJsonNull) return null
            if (!elem.isJsonArray) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not array")
            val jarr: JsonArray = elem.asJsonArray
            val n: Int = jarr.size()
            val `val` = cls.newInstance() as MutableMap<Any?, Any?>
            for (i in 0 until n) {
                val obj: JsonObject = jarr.get(i).asJsonObject
                val key = decode(obj.get("key"), par.curjfld.generic().get(0), par)
                par.index = 1
                val ent = decode(obj.get("val"), par.curjfld.generic().get(1), par)
                par.index = 0
                `val`[key] = ent
            }
            return `val`
        }

        @Throws(Exception::class)
        private fun decodeObject(elem: JsonElement, cls: Class<*>, par: JsonDecoder?): Any? {
            if (elem.isJsonNull) return null
            if (!elem.isJsonObject) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not object for $cls")
            val jobj: JsonObject = elem.asJsonObject
            val jc: JsonClass = cls.getAnnotation(JsonClass::class.java)
            return if (jc.read() == RType.FILL) throw JsonException(JsonException.Type.FUNC, null, "RType FILL requires GenType FILL or GEN") else if (jc.read() == RType.DATA) inject(par, jobj, cls, null) else if (jc.read() == RType.MANUAL) {
                val func: String = jc.generator()
                if (func.length == 0) throw JsonException(JsonException.Type.FUNC, elem, "no generate function")
                val m = cls.getMethod(func, JsonElement::class.java)
                m.invoke(null, jobj)
            } else throw JsonException(JsonException.Type.UNDEFINED, elem, "class not possible to generate")
        }

        @Throws(Exception::class)
        private fun decodeSet(elem: JsonElement, cls: Class<*>, par: JsonDecoder?): Set<Any?>? {
            if (par!!.curjfld == null || par.curjfld.generic().size != 1) throw JsonException(JsonException.Type.TAG, null, "generic data structure requires typeProvider tag")
            if (elem.isJsonNull) return null
            if (!elem.isJsonArray) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not array")
            val jarr: JsonArray = elem.asJsonArray
            val n: Int = jarr.size()
            val `val` = cls.newInstance() as MutableSet<Any?>
            for (i in 0 until n) {
                `val`.add(decode(jarr.get(i), par.curjfld.generic().get(0), par))
            }
            return `val`
        }

        @Throws(Exception::class)
        private fun getArray(cls: Class<*>, n: Int, par: JsonDecoder?): Any {
            return if (par != null && par.curjfld != null && par.curjfld.gen() == GenType.FILL) {
                if (par.curfld == null || par.obj == null) throw JsonException(JsonException.Type.TAG, null, "no enclosing object")
                par.curfld!![par.obj]
            } else Array.newInstance(cls, n)
        }

        private fun getGlobal(par: JsonDecoder, cls: Class<*>): Any? {
            var dec = par
            while (dec != null) {
                if (cls.isInstance(dec.obj)) return dec.obj
                dec = dec.par
            }
            return null
        }

        @Throws(Exception::class)
        private fun inject(par: JsonDecoder?, jobj: JsonObject, cls: Class<*>, pre: Any?): Any? {
            return JsonDecoder(par, jobj, cls, pre ?: cls.newInstance()).obj
        }

        init {
            REGISTER[java.lang.Boolean.TYPE] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getBoolean(common.io.json.elem) }
            REGISTER[Boolean::class.java] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getBoolean(common.io.json.elem) }
            REGISTER[java.lang.Byte.TYPE] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getByte(common.io.json.elem) }
            REGISTER[Byte::class.java] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getByte(common.io.json.elem) }
            REGISTER[java.lang.Short.TYPE] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getShort(common.io.json.elem) }
            REGISTER[Short::class.java] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getShort(common.io.json.elem) }
            REGISTER[Integer.TYPE] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getInt(common.io.json.elem) }
            REGISTER[Int::class.java] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getInt(common.io.json.elem) }
            REGISTER[java.lang.Long.TYPE] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getLong(common.io.json.elem) }
            REGISTER[Long::class.java] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getLong(common.io.json.elem) }
            REGISTER[java.lang.Float.TYPE] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getFloat(common.io.json.elem) }
            REGISTER[Float::class.java] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getFloat(common.io.json.elem) }
            REGISTER[java.lang.Double.TYPE] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getDouble(common.io.json.elem) }
            REGISTER[Double::class.java] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getDouble(common.io.json.elem) }
            REGISTER[String::class.java] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getString(common.io.json.elem) }
            REGISTER[Class::class.java] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> Class.forName(getString(common.io.json.elem)) }
        }
    }

    private val par: JsonDecoder
    private val jobj: JsonObject
    private val obj: Any?
    private val tarcls: Class<*>
    private val tarjcls: JsonClass
    private var curcls: Class<*>? = null
    private var curjcls: JsonClass? = null
    private var curfld: Field? = null
    var curjfld: JsonField? = null
    private var index = 0
    @Throws(Exception::class)
    private fun decode(cls: Class<*>) {
        if (cls.superclass.getAnnotation(JsonClass::class.java) != null) decode(cls.superclass)
        curcls = cls
        curjcls = cls.getAnnotation(JsonClass::class.java)
        if (curjcls == null) throw JsonException(JsonException.Type.TYPE_MISMATCH, jobj, "no annotation for class $curcls")
        val fs = cls.declaredFields
        for (f in fs) {
            if (Modifier.isStatic(f.modifiers)) continue
            curjfld = f.getAnnotation(JsonField::class.java)
            if (curjfld == null && curjcls.noTag() == NoTag.LOAD) curjfld = JsonField.Companion.DEF
            if (curjfld == null || curjfld.block() || curjfld.io() == IOType.W) continue
            var tag: String = curjfld.tag()
            if (tag.length == 0) tag = f.name
            if (!jobj.has(tag)) continue
            val elem: JsonElement = jobj.get(tag)
            f.isAccessible = true
            curfld = f
            f[obj] = decode(elem, f.type, getInvoker())
            curfld = null
        }
        var oni: Method? = null
        for (m in cls.declaredMethods) {
            if (m.getAnnotation(OnInjected::class.java) != null) oni = if (oni == null) m else throw JsonException(JsonException.Type.FUNC, null, "duplicate OnInjected")
            curjfld = m.getAnnotation(JsonField::class.java)
            if (curjfld == null || curjfld.io() == IOType.W) continue
            if (curjfld.io() == IOType.RW) throw JsonException(JsonException.Type.FUNC, null, "functional fields should not have RW type")
            if (m.parameterCount != 1) throw JsonException(JsonException.Type.FUNC, null, "parameter count should be 1")
            val tag: String = curjfld.tag()
            if (tag.length == 0) throw JsonException(JsonException.Type.TAG, null, "function fields must have tag")
            if (!jobj.has(tag)) continue
            val elem: JsonElement = jobj.get(tag)
            val ccls = m.parameters[0].type
            m.invoke(obj, decode(elem, ccls, getInvoker()))
        }
        oni?.invoke(obj)
    }

    private fun getInvoker(): JsonDecoder {
        return if (tarjcls.bypass()) par else this
    }

    init {
        par = parent ?: current
        jobj = json
        obj = pre
        tarcls = cls
        tarjcls = cls.getAnnotation(JsonClass::class.java)
        current = getInvoker()
        decode(tarcls)
        current = par
    }
}
