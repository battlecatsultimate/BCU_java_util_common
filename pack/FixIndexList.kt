package common.pack

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import common.io.json.JsonClass
import common.io.json.JsonDecoder
import common.io.json.JsonEncoder
import common.io.json.JsonField
import common.io.json.JsonField.IOType
import common.pack.IndexContainer.Indexable
import common.util.Dataimport
import java.util.*
import java.util.function.BiConsumer

@JsonClass
open class FixIndexList<T>(cls: Class<T>?) : Data() {
    open class FixIndexMap<T : Indexable<*, *>?>(cls: Class<T>?) : FixIndexList<T>(cls)

    protected var arr: Array<T?>
    private var size = 0
    fun add(t: T?) {
        val ind = nextInd()
        arr[ind] = t
        if (t != null) {
            size++
        }
    }

    fun clear() {
        for (i in arr.indices) arr[i] = null
        size = 0
    }

    operator fun contains(t: T?): Boolean {
        if (t == null) return false
        for (a in arr) if (t == a) return true
        return false
    }

    fun forEach(c: BiConsumer<Int?, T>) {
        for (i in arr.indices) if (arr[i] != null) c.accept(i, arr[i])
    }

    operator fun get(ind: Int): T? {
        return if (ind < 0 || ind >= arr.size) null else arr[ind]
    }

    fun getFirstInd(): Int {
        if (size == 0) return -1
        for (i in arr.indices) if (arr[i] != null) return i
        return -1
    }

    fun getList(): List<T> {
        val ans: MutableList<T> = ArrayList(size)
        for (t in arr) if (t != null) ans.add(t)
        return ans
    }

    fun getMap(): Map<Int, T?> {
        val map: MutableMap<Int, T?> = TreeMap<Int, T>()
        for (i in arr.indices) if (arr[i] != null) map[i] = arr[i]
        return map
    }

    fun indexOf(tar: T): Int {
        for (i in arr.indices) if (arr[i] != null && arr[i] == tar) return i
        return -1
    }

    fun nextInd(): Int {
        for (i in arr.indices) if (arr[i] == null) return i
        arr = Arrays.copyOf(arr, arr.size * 2)
        return arr.size / 2
    }

    fun remove(t: T?) {
        if (t == null) return
        for (i in arr.indices) if (arr[i] === t) {
            arr[i] = null
            size--
        }
    }

    operator fun set(ind: Int, t: T?) {
        while (arr.size <= ind) arr = Arrays.copyOf(arr, arr.size * 2)
        if (arr[ind] != null) size--
        if (t != null) size++
        arr[ind] = t
    }

    fun size(): Int {
        return size
    }

    @JsonField(tag = "data", io = IOType.R)
    @Throws(Exception::class)
    fun zgen(e: JsonElement) {
        val cls = arr.javaClass.componentType as Class<T>
        val jarr: JsonArray = e.getAsJsonArray()
        for (i in 0 until jarr.size()) {
            val ji: JsonObject = jarr.get(i).getAsJsonObject()
            val ind: Int = ji.get("ind").getAsInt()
            val `val`: T = JsonDecoder.Companion.decode(ji.get("val"), cls)
            this[ind] = `val`
        }
    }

    @JsonField(tag = "data", io = IOType.W)
    @Throws(Exception::class)
    fun zser(): JsonElement {
        val data = JsonArray()
        for (i in arr.indices) if (arr[i] != null) {
            val ent = JsonObject()
            ent.addProperty("ind", i)
            ent.add("val", JsonEncoder.Companion.encode(arr[i]))
        }
        return data
    }

    init {
        arr = java.lang.reflect.Array.newInstance(cls, 16) as Array<T?>
    }
}
