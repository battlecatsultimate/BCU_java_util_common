package common.io.json

import com.google.gson.JsonArray
import common.io.assets.Admin.StaticPermitted
import common.io.json.JsonDecoder
import common.io.json.JsonField
import java.lang.annotation.Documentedimport
import java.util.*
import kotlin.reflect.KClass

/**
 * ways to read from JSON: <br></br>
 * 1. default setting, use on fields, put `@JsonField`, ignore fields not
 * in JSON <br></br>
 * 2. customize IOType, to be used only when reading or writing, add parameter
 * `IOType` <br></br>
 * 3. use on methods, must have `IOType` `R` or `W` <br></br>
 * 4. `GenType` `FILL` mode, on `RType` `SET` or
 * `FILL` object field only, allows injection on pre-existing objects. Can
 * use on primitive arrays. Not applicable to Collections. <br></br>
 * 5. {@Code GenType} `GEN` mode, use parameter `generator` to
 * specify function name, must be static function declared in this class<br></br>
 * <hr></hr>
 * Forbidden pairs:
 *  * Primitive - GenType.FILL
 *  * Primitive - GenType.GEN
 *  * Collection - GenType.FILL
 *  * Method - GenType.FILL
 *  * Method - IOType.RW
 *  * Type.FILL - GenType.SET
 * <hr></hr>
 * Suggestions:
 *  * GenType.GEN with object field without using JsonObject can be replaced
 * with GenType.FILL to avoid unnecessary functions
 *  * use functional read and field write to pre-process inputs
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class JsonField(val alias: Array<KClass<*>> = [], val block: Boolean = false,
                           /**
                            * Generation Type for this Field. Default is SET, which means to set the value.
                            * FILL requires a default value and must be used on object fields. GEN uses
                            * generator function. Functional Fields must use SET.
                            */
                           val gen: GenType = GenType.SET,
                           /**
                            * ignored when GenType is not GEN, must refer to a static method declared in
                            * this class with parameter of this type and `JsonObject`. second
                            * parameter can be unused, as it will also be injected
                            */
                           val generator: String = "",
                           /**
                            * 1. used for generic data structures. Currently supports List, Set, and Map.
                            * Note: the field declaration must be instantiatable
                            */
                           val generic: Array<KClass<*>> = [], val io: IOType = IOType.RW, val ser: SerType = SerType.DEF, val serializer: String = "",
                           /**
                            * tag name for this field, use the field name if not specified. Must be
                            * specified for functions
                            */
                           val tag: String = "", val usePool: Boolean = false) {
    enum class GenType {
        SET, FILL, GEN
    }

    @JsonClass
    class Handler {
        val list: MutableList<Any?> = ArrayList()

        constructor() {}
        constructor(jarr: JsonArray, cls: Class<*>, dec: JsonDecoder) {
            var cls = cls
            val n: Int = jarr.size()
            if (dec.curjfld!!.generic().size == 1) cls = dec.curjfld!!.generic().get(0)
            for (i in 0 until n) list.add(JsonDecoder.Companion.decode(jarr.get(i), cls, dec))
        }

        fun add(o: Any?): Int {
            if (o == null) return -1
            for (i in list.indices) if (list[i] === o) // hard comparison
                return i
            list.add(o)
            return list.size - 1
        }

        operator fun get(i: Int): Any? {
            return if (i == -1) null else list[i]
        }
    }

    enum class IOType {
        R, W, RW
    }

    enum class SerType {
        DEF, FUNC, CLASS
    }

    companion object {
        @StaticPermitted
        var DEF: JsonField = object : JsonField() {
            override fun alias(): Array<Class<*>> {
                return arrayOfNulls(0)
            }

            override fun annotationType(): Class<out Annotation?> {
                return JsonField::class.java
            }

            override fun block(): Boolean {
                return false
            }

            override fun gen(): GenType {
                return GenType.SET
            }

            override fun generator(): String {
                return ""
            }

            override fun generic(): Array<Class<*>> {
                return arrayOfNulls(0)
            }

            override fun io(): IOType {
                return IOType.RW
            }

            override fun ser(): SerType {
                return SerType.DEF
            }

            override fun serializer(): String {
                return ""
            }

            override fun tag(): String {
                return ""
            }

            override fun usePool(): Boolean {
                return false
            }
        }
    }
}
