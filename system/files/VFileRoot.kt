package common.system.files

class VFileRoot<T : FileData?>(str: String) : VFile<T>(str) {
    fun build(str: String, fd: T?): VFile<T>? {
        val strs = str.split("/|\\\\").toTypedArray()
        var par: VFile<T> = this
        for (i in 1 until strs.size) {
            var next: VFile<T>? = null
            for (ch in par.list()) if (ch.name == strs[i]) next = ch
            if (next == null) if (i == strs.size - 1) return if (fd != null) VFile<T>(par, strs[i], fd) else VFile<T>(par, strs[i]) else next = VFile<T>(par, strs[i])
            if (i == strs.size - 1) {
                if (fd == null) return next
                next.setData(fd)
                return next
            }
            par = next
        }
        return null
    }

    fun find(str: String): VFile<T>? {
        val strs = str.split("/|\\\\").toTypedArray()
        var par: VFile<T> = this
        for (i in 1 until strs.size) {
            var next: VFile<T>? = null
            for (ch in par.list()) if (ch.name == strs[i]) next = ch
            if (next == null) return null
            if (i == strs.size - 1) return next
            par = next
        }
        return this
    }
}
