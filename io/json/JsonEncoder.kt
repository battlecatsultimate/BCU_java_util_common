package common.io.json

import common.io.json.JsonField.IOType
import common.io.json.JsonField.SerType
import common.pack.Context.SupExc
import common.util.Data
import java.lang.Exceptionimport
import java.lang.reflect.Array
import java.lang.reflect.Modifier

com.google.api.client.json.jackson2.JacksonFactory
import kotlin.Throws
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import common.io.json.JsonClass.NoTag
import common.io.json.JsonClass.WType
import com.google.gson.JsonArray
import common.io.json.JsonClass.JCGeneric
import com.google.gson.JsonPrimitive
import com.google.gson.JsonNull
import common.io.json.JsonClass.JCIdentifier

class JsonEncoder private constructor(private val par: JsonEncoder?, private val obj: Any) {
    private val ans: JsonObject = JsonObject()
    private var curjcls: JsonClass? = null
    private var curjfld: JsonField? = null
    private var index = 0
    @Throws(Exception::class)
    private fun encode(cls: Class<*>) {
        if (cls.superclass.getAnnotation(JsonClass::class.java) != null) encode(cls.superclass)
        curjcls = cls.getAnnotation(JsonClass::class.java)
        for (f in cls.declaredFields) if (curjcls.noTag() == NoTag.LOAD || f.getAnnotation(JsonField::class.java) != null) {
            if (Modifier.isStatic(f.modifiers)) continue
            var jf: JsonField = f.getAnnotation(JsonField::class.java)
            if (jf == null) jf = JsonField.Companion.DEF
            if (jf.block() || jf.io() == IOType.R) continue
            val tag = if (jf.tag().length == 0) f.name else jf.tag()
            f.isAccessible = true
            curjfld = jf
            ans.add(tag, encode(f[obj], getInvoker()))
            curjfld = null
        }
        for (m in cls.declaredMethods) if (m.getAnnotation(JsonField::class.java) != null) {
            val jf: JsonField = m.getAnnotation(JsonField::class.java)
            if (jf.io() == IOType.R) continue
            if (jf.io() == IOType.RW) throw JsonException(JsonException.Type.FUNC, null, "functional fields should not have RW type")
            val tag: String = jf.tag()
            if (tag.length == 0) throw JsonException(JsonException.Type.TAG, null, "function fields must have tag")
            curjfld = jf
            ans.add(tag, encode(m.invoke(obj), getInvoker()))
            curjfld = null
        }
    }

    private fun getInvoker(): JsonEncoder? {
        return if (curjcls!!.bypass()) par else this
    }

    companion object {
        fun encode(obj: Any?): JsonElement {
            return Data.Companion.err<JsonElement>(SupExc<JsonElement> { encode(obj, null) })
        }

        @Throws(Exception::class)
        private fun encode(obj: Any?, par: JsonEncoder?): JsonElement {
            if (obj == null) return JsonNull.INSTANCE
            if (obj is JsonElement) return obj
            if (obj is Number) return JsonPrimitive(obj as Number?)
            if (obj is Boolean) return JsonPrimitive(obj as Boolean?)
            if (obj is String) return JsonPrimitive(obj as String?)
            if (obj is Class<*>) return JsonPrimitive(obj.name)
            val cls: Class<*> = obj.javaClass
            if (cls.isArray) {
                if (par != null && par.curjfld != null && par.curjfld.usePool()) {
                    val handler = JsonField.Handler()
                    val n = Array.getLength(obj)
                    val jarr = JsonArray()
                    for (i in 0 until n) jarr.add(handler.add(Array.get(obj, i)))
                    val jobj = JsonObject()
                    jobj.add("pool", encode(handler.list))
                    jobj.add("data", jarr)
                    return jobj
                }
                val n = Array.getLength(obj)
                val arr = JsonArray(n)
                for (i in 0 until n) arr.add(encode(Array.get(obj, i), par))
                return arr
            }
            if (cls.getAnnotation(JCGeneric::class.java) != null && par != null && par.curjfld!!.alias().size > par.index) {
                val jcg: JCGeneric = cls.getAnnotation(JCGeneric::class.java)
                val alias: Class<*> = par.curjfld!!.alias().get(par.index)
                var found = false
                for (ala in jcg.value()) if (ala == alias) {
                    found = true
                    break
                }
                if (!found) throw JsonException(JsonException.Type.TYPE_MISMATCH, null, "class not present in JCGeneric")
                for (f in cls.declaredFields) {
                    val jcgw: JCIdentifier = f.getAnnotation(JCIdentifier::class.java)
                    if (jcgw != null && f.type == alias) return encode(f[obj], par)
                }
                val con = alias.getConstructor(cls)
                return encode(con.newInstance(obj), par)
            }
            if (par != null && par.curjfld != null) {
                val jfield: JsonField? = par.curjfld
                if (jfield!!.ser() == SerType.FUNC) {
                    if (jfield.serializer().length == 0) throw JsonException(JsonException.Type.FUNC, null, "no serializer function")
                    val m = par.obj.javaClass.getMethod(jfield.serializer(), cls)
                    return encode(m.invoke(par.obj, obj))
                } else if (jfield.ser() == SerType.CLASS) {
                    val cjc: JsonClass = cls.getAnnotation(JsonClass::class.java)
                    if (cjc == null || cjc.serializer().length == 0) throw JsonException(JsonException.Type.FUNC, null, "no serializer function")
                    val func: String = cjc.serializer()
                    val m = cls.getMethod(func)
                    return encode(m.invoke(obj), null)
                }
            }
            val jc: JsonClass = cls.getAnnotation(JsonClass::class.java)
            if (jc != null) if (jc.write() == WType.DEF) return JsonEncoder(par, obj).ans else if (jc.write() == WType.CLASS) {
                if (jc.serializer().length == 0) throw JsonException(JsonException.Type.FUNC, null, "no serializer function")
                val func: String = jc.serializer()
                val m = cls.getMethod(func)
                return encode(m.invoke(obj), null)
            }
            if (obj is List<*>) return encodeList(obj, par)
            if (obj is Set<*>) return encodeSet(obj, par)
            if (obj is Map<*, *>) return encodeMap(obj, par)
            throw JsonException(JsonException.Type.UNDEFINED, null, "object $obj not defined")
        }

        @Throws(Exception::class)
        private fun encodeList(list: List<*>, par: JsonEncoder?): JsonElement {
            if (par != null && par.curjfld != null && par.curjfld.usePool()) {
                val handler = JsonField.Handler()
                val n = list.size
                val jarr = JsonArray()
                for (i in 0 until n) jarr.add(handler.add(list[i]))
                val jobj = JsonObject()
                jobj.add("pool", encode(handler.list))
                jobj.add("data", jarr)
                return jobj
            }
            val ans = JsonArray(list.size)
            for (obj in list) ans.add(encode(obj, par))
            return ans
        }

        @Throws(Exception::class)
        private fun encodeMap(map: Map<*, *>, par: JsonEncoder?): JsonArray {
            val ans = JsonArray(map.size)
            for ((key, value) in map) {
                val ent = JsonObject()
                ent.add("key", encode(key, par))
                par!!.index = 1
                ent.add("val", encode(value, par))
                par.index = 0
                ans.add(ent)
            }
            return ans
        }

        @Throws(Exception::class)
        private fun encodeSet(set: Set<*>, par: JsonEncoder?): JsonArray {
            val ans = JsonArray(set.size)
            for (obj in set) ans.add(encode(obj, par))
            return ans
        }
    }

    init {
        encode(obj.javaClass)
    }
}
