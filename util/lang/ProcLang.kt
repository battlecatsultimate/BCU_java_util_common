package common.util.lang

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import common.CommonStatic
import common.io.json.JsonClass
import common.io.json.JsonClass.RType
import common.io.json.JsonClass.WType
import common.io.json.JsonDecoder
import common.io.json.JsonEncoder
import common.io.json.JsonField
import common.io.json.JsonField.IOType
import common.pack.Context
import common.pack.Context.ErrType
import common.pack.UserProfile
import common.util.Data
import common.util.Data.Proc
import common.util.lang.LocaleCenter.DisplayItem
import common.util.lang.LocaleCenter.ObjBinder
import common.util.lang.LocaleCenter.ObjBinder.BinderFunc
import common.util.lang.ProcLang
import java.io.File
import java.io.FileReader
import java.util.*
import java.util.function.Supplier

@JsonClass(read = RType.MANUAL, write = WType.CLASS, generator = "gen", serializer = "ser")
class ProcLang private constructor() {
    @JsonClass(read = RType.FILL)
    class ItemLang(private val name: String, private val cls: Class<*>) {
        @JsonField
        var abbr_name: String? = null

        @JsonField
        var full_name: String? = null

        @JsonField
        var tooltip: String? = null

        @JsonField
        var format: String? = null
        private val map: LinkedHashMap<String, DisplayItem> = LinkedHashMap<String, DisplayItem>()
        operator fun get(proc: String): LocaleCenter.Binder? {
            return try {
                ObjBinder(map[proc], "$name.$proc", BinderFunc { proc: String -> getBinder(proc) })
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun list(): Array<String?> {
            val ans = arrayOfNulls<String>(map.size)
            var i = 0
            for ((key) in map) ans[i++] = key
            return ans
        }

        @JsonField(tag = "class", io = IOType.R)
        fun readClass(elem: JsonElement?) {
            fill("", cls, if (elem == null) null else elem.asJsonObject)
        }

        @JsonField(tag = "class", io = IOType.W)
        fun writeClass(): JsonObject {
            val ans = JsonObject()
            fill(ans, "", cls)
            return ans
        }

        private fun fill(ans: JsonObject, pre: String, c: Class<*>) {
            for (f in c.declaredFields) {
                if (Data.Proc.IntType::class.java.isAssignableFrom(f.type)) fill(ans, f.name + ".", f.type) else ans.add(pre + f.name, JsonEncoder.Companion.encode(map[pre + f.name]))
            }
        }

        private fun fill(pre: String, c: Class<*>, obj: JsonObject?) {
            for (f in c.declaredFields) {
                if (Data.Proc.IntType::class.java.isAssignableFrom(f.type)) {
                    fill(f.name + ".", f.type, obj)
                } else {
                    val elem: JsonElement? = if (obj == null) null else obj.get(pre + f.name)
                    val pf: DisplayItem = if (elem == null) DisplayItem() else JsonDecoder.Companion.decode<DisplayItem>(elem, DisplayItem::class.java)
                    map[pre + f.name] = pf
                }
            }
        }

        private fun getBinder(proc: String): LocaleCenter.Binder? {
            return get()!![name]!![proc]
        }
    }

    class ProcLangStore {
        private val langs = arrayOfNulls<ProcLang>(CommonStatic.Lang.Companion.LOC_CODE.size)
        fun getLang(): ProcLang? {
            val lang: Int = CommonStatic.getConfig().lang
            if (langs[lang] == null) Data.Companion.err(Context.RunExc { read() })
            return langs[lang]
        }

        fun setLang(lang: ProcLang) {
            langs[CommonStatic.getConfig().lang] = lang
        }
    }

    private val map: MutableMap<String, ItemLang> = LinkedHashMap()
    operator fun get(i: Int): ItemLang {
        return get(Proc::class.java.declaredFields.get(i).name)
    }

    operator fun get(str: String): ItemLang? {
        return map[str]
    }

    fun ser(): JsonObject {
        val obj = JsonObject()
        for (f in Proc::class.java.declaredFields) {
            val name = f.name
            obj.add(name, JsonEncoder.Companion.encode(map[name]))
        }
        return obj
    }

    companion object {
        fun clear() {
            UserProfile.Companion.setStatic("ProcLangStore", null)
        }

        @Throws(Exception::class)
        fun gen(elem: JsonElement?): ProcLang {
            val ans = ProcLang()
            val obj: JsonObject? = if (elem == null) null else elem.asJsonObject
            for (f in Proc::class.java.declaredFields) {
                val name = f.name
                val item = ItemLang(name, f.type)
                if (obj != null && obj.has(name)) JsonDecoder.Companion.inject<ItemLang>(obj.get(name), ItemLang::class.java, item) else item.readClass(null)
                ans.map[name] = item
            }
            return ans
        }

        fun get(): ProcLang? {
            return store().getLang()
        }

        @Throws(Exception::class)
        private fun read() {
            val f: File = CommonStatic.ctx.getLangFile("proc.json")
            if (!f.exists()) CommonStatic.ctx.printErr(ErrType.FATAL, "cannot find proc.json")
            val elem: JsonElement = JsonParser.parseReader(FileReader(f))
            val proc: ProcLang = JsonDecoder.Companion.decode<ProcLang>(elem, ProcLang::class.java)
            store().setLang(proc)
        }

        private fun store(): ProcLangStore {
            return UserProfile.Companion.getStatic<ProcLangStore>("ProcLangStore", Supplier { ProcLangStore() })
        }
    }
}
