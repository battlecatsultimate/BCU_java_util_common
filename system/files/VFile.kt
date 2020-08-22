package common.system.files

import common.CommonStatic
import common.io.PackLoader.ZipDesc.FileDesc
import common.pack.UserProfile
import java.io.File
import java.util.*
import java.util.function.Predicate

open class VFile<T : FileData?> : Comparable<VFile<T>?> {
    var name: String
    protected var parent: VFile<T>?
    private val subs: MutableMap<String, VFile<T>>?
    private var data: T?
    var mark = 0

    /** constructor for root directory  */
    protected constructor(str: String) : this(null, str)

    /** constructor for directory  */
    constructor(par: VFile<T>?, str: String) {
        parent = par
        name = str
        subs = TreeMap<String, VFile<T>>()
        data = null
        if (parent != null) parent.subs!![name] = this
    }

    /** constructor for data file  */
    constructor(par: VFile<T>?, str: String, fd: T) {
        parent = par
        name = str
        subs = null
        data = fd
        if (parent != null) parent.subs!![name] = this
    }

    override operator fun compareTo(o: VFile<T>): Int {
        return name.compareTo(o.name)
    }

    fun countSubDire(): Int {
        var ans = 0
        for (f in subs!!.values) if (f.subs != null) ans++
        return ans
    }

    fun delete() {
        parent!!.subs!!.remove(name)
    }

    fun getData(): T? {
        return data
    }

    fun getIf(p: Predicate<VFile<T>?>): List<VFile<T>> {
        val ans: MutableList<VFile<T>> = ArrayList()
        for (v in list()!!) {
            if (p.test(v)) ans.add(v)
            if (v.subs != null) ans.addAll(v.getIf(p))
        }
        return ans
    }

    fun getName(): String {
        return name
    }

    fun getParent(): VFile<T>? {
        return parent
    }

    fun getPath(): String {
        return if (parent != null) parent.getPath() + "/" + name else name
    }

    fun list(): Collection<VFile<T>>? {
        return subs?.values
    }

    @Throws(Exception::class)
    fun merge(f: VFile<T>) {
        if (subs == null || f.subs == null) throw Exception("merge can only happen for folders")
        for (fi in f.subs.values) {
            fi.parent = this
            if (fi.subs == null || !subs.containsKey(fi.name)) subs[fi.name] = fi else subs[fi.name]!!.merge(fi)
        }
    }

    fun replace(t: T) {
        delete()
        VFile(parent, name, t)
    }

    fun setData(fd: T) {
        data = fd
    }

    override fun toString(): String {
        return name
    }

    companion object {
        operator fun get(str: String?): VFile<FileDesc>? {
            return getBCFileTree().find(str!!)
        }

        fun getBCFileTree(): VFileRoot<FileDesc> {
            return UserProfile.Companion.getBCData().root
        }

        fun getFile(path: String): VFile<out FileData>? {
            if (path.startsWith("./org/") || path.startsWith("./lang/")) return getBCFileTree().find(path)
            if (path.startsWith("./res/")) {
                val f: File = CommonStatic.def.route(path)
                return if (!f.exists()) null else VFile(null, f.name, FDFile(f))
            }
            return null
        }

        fun readLine(str: String): Queue<String> {
            return getFile(str)!!.getData()!!.readLine()
        }
    }
}
