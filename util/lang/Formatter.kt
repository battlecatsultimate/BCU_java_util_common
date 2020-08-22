package common.util.lang

import common.CommonStatic
import common.io.assets.Admin.StaticPermitted
import common.io.json.JsonClass
import common.io.json.JsonEncoder
import common.io.json.JsonField
import common.pack.Context.ErrType
import common.pack.PackData
import common.util.lang.Formatter
import common.util.pack.Background
import java.util.*

class Formatter private constructor(private val str: String, private val obj: Any, private val ctx: Any) {
    @JsonClass
    class Context {
        val left = "("
        val right = ")"
        val sqleft = "["
        val sqright = "]"
        val crleft = "{"
        val crright = "}"

        @JsonField
        var isEnemy = false

        @JsonField
        var useSecond = false

        constructor() {}
        constructor(ene: Boolean, sec: Boolean) {
            isEnemy = ene
            useSecond = sec
        }

        fun abs(v: Int): String {
            return "" + Math.abs(v)
        }

        fun bg(id: PackData.Identifier<Background?>?): String {
            return PackData.Identifier.Companion.get<Background>(id).toString() + ""
        }

        fun dispTime(time: Int): String {
            return if (useSecond) toSecond(time) + "s" else time.toString() + "f"
        }

        fun entity(id: PackData.Identifier<*>?): String {
            return PackData.Identifier.Companion.get(id).toString() + ""
        }

        fun toSecond(time: Int): String {
            return "" + time * 100 / 30 / 100.0
        }
    }

    private inner class BoolElem(start: Int, end: Int) : Formatter.Comp(start, end) {
        @Throws(Exception::class)
        fun eval(): Boolean {
            for (i in Formatter.Companion.MATCH.indices) for (j in p0 until p1 - Formatter.Companion.MATCH.get(i).length + 1) if (test(i, j)) {
                val fi0 = IntExp(p0, j).eval()
                val fi1 = IntExp(j + Formatter.Companion.MATCH.get(i).length, p1).eval()
                if (i == 0) return fi0 >= fi1
                if (i == 1) return fi0 <= fi1
                if (i == 2) return fi0 == fi1
                if (i == 3) return fi0 != fi1
                if (i == 4) return fi0 > fi1
                if (i == 5) return fi0 < fi1
            }
            return RefObj(p0, p1).eval() as Boolean
        }

        private fun test(i: Int, j: Int): Boolean {
            for (k in 0 until Formatter.Companion.MATCH.get(i).length) if (str[j + k] != Formatter.Companion.MATCH.get(i).get(k)) return false
            return true
        }
    }

    private inner class BoolExp(start: Int, end: Int) : Formatter.Comp(start, end) {
        private var ind: Int

        @Throws(Exception::class)
        fun eval(): Boolean {
            val stack = Stack<Boolean>()
            stack.push(nextElem())
            while (ind < p1) {
                val ch = str[ind++]
                if (ch == '&') stack.push(stack.pop() and nextElem()) else if (ch == '|') stack.push(nextElem()) else throw Exception("unknown operator " + ch + " at " + (ind - 1))
            }
            var b = false
            for (bi in stack) b = b or bi
            return b
        }

        @Throws(Exception::class)
        private fun nextElem(): Boolean {
            var ch = str[ind]
            val neg = ch == '!'
            if (neg) ch = str[++ind]
            if (ch == '!') throw Exception("double ! at $ind")
            if (ch == '(') {
                var depth = 1
                val pre = ++ind
                while (depth > 0) {
                    val chr = str[ind++]
                    if (chr == '(') depth++
                    if (chr == ')') depth--
                }
                return neg xor BoolExp(pre, ind - 1).eval()
            }
            val pre = ind
            while (ch != '&' && ch != '|' && ind < p1) ch = str[++ind]
            return neg xor BoolElem(pre, ind).eval()
        }

        init {
            ind = p0
        }
    }

    private inner class Code private constructor(private val cond: BoolExp, private val data: Formatter.Root) : IElem {
        @Throws(Exception::class)
        override fun build(sb: StringBuilder) {
            if (cond.eval()) data.build(sb)
        }
    }

    private inner class CodeBlock(start: Int, end: Int) : Cont(start, end) {
        init {
            var i = p0
            while (i < p1) {
                val ch = str[i++]
                if (ch == '(') {
                    var depth = 1
                    var pre = i
                    while (depth > 0) {
                        if (i >= p1) throw Exception("unfinished at $i")
                        val chr = str[i++]
                        if (chr == '(') depth++
                        if (chr == ')') depth--
                    }
                    val cond = BoolExp(pre, i - 1)
                    while (str[i++] != '{') if (i >= p1) throw Exception("unfinished at $i")
                    depth = 1
                    pre = i
                    while (depth > 0) {
                        if (i >= p1) throw Exception("unfinished at $i")
                        val chr = str[i++]
                        if (chr == '{') depth++
                        if (chr == '}') depth--
                    }
                    val data: Formatter.Root = Formatter.Root(pre, i - 1)
                    list.add(Formatter.Code(cond, data))
                }
            }
        }
    }

    private abstract inner class Comp(val p0: Int, val p1: Int)
    private abstract inner class Cont(start: Int, end: Int) : Elem(start, end) {
        val list: MutableList<IElem> = ArrayList()
        @Throws(Exception::class)
        override fun build(sb: StringBuilder) {
            for (e in list) e.build(sb)
        }
    }

    private abstract inner class Elem(start: Int, end: Int) : Formatter.Comp(start, end), IElem
    private interface IElem {
        @Throws(Exception::class)
        fun build(sb: StringBuilder)
    }

    private inner class IntExp(start: Int, end: Int) : Formatter.Comp(start, end) {
        private var ind: Int

        @Throws(Exception::class)
        fun eval(): Int {
            val stack = Stack<Int>()
            var opera = '\u0000'
            stack.push(nextElem())
            while (ind < p1) {
                val ch = str[ind++]
                if (ch == '*') stack.push(stack.pop() * nextElem()) else if (ch == '/') stack.push(stack.pop() / nextElem()) else if (ch == '%') stack.push(stack.pop() % nextElem()) else if (ch == '+' || ch == '-') {
                    if (opera != ' ') {
                        val b = stack.pop()
                        val a = stack.pop()
                        if (opera == '+') stack.push(a + b) else stack.push(a - b)
                    }
                    stack.push(nextElem())
                    opera = ch
                } else throw Exception("unknown operator " + ch + " at " + (ind - 1))
            }
            if (opera != '\u0000') {
                val b = stack.pop()
                val a = stack.pop()
                if (opera == '+') stack.push(a + b) else stack.push(a - b)
            }
            return stack.pop()
        }

        @Throws(Exception::class)
        private fun nextElem(): Int {
            var ch = str[ind]
            var neg = 1
            if (ch == '-') {
                neg = -1
                ch = str[++ind]
            }
            if (ch == '(') {
                var depth = 1
                val pre = ++ind
                while (depth > 0) {
                    val chr = str[ind++]
                    if (chr == '(') depth++
                    if (chr == ')') depth--
                }
                return neg * IntExp(pre, ind - 1).eval()
            }
            if (ch >= '0' && ch <= '9') return neg * readNumber()
            val pre = ind
            while (ch != '+' && ch != '-' && ch != '*' && ch != '/' && ch != '%' && ind < p1) ch = str[++ind]
            return neg * (RefObj(pre, ind).eval() as Int?)!!
        }

        @Throws(Exception::class)
        private fun readNumber(): Int {
            var ans = 0
            while (ind < p1) {
                val chr = str[ind]
                if (chr < '0' || chr > '9') break
                ind++
                ans = ans * 10 + chr.toInt() - '0'.toInt()
            }
            return ans
        }

        init {
            ind = p0
        }
    }

    private abstract inner class RefElem(start: Int, end: Int) : Formatter.Comp(start, end) {
        @Throws(Exception::class)
        abstract fun eval(parent: Any?): Any?
    }

    private inner class RefField(start: Int, end: Int) : RefElem(start, end) {
        @Throws(Exception::class)
        override fun eval(parent: Any?): Any? {
            var parent = parent
            if (str[p0] == '_' && parent != null) throw Exception("global only allowed for bottom level")
            if (parent == null) parent = if (str[p0] == '_') ctx else obj
            val name = str.substring(if (str[p0] == '_') p0 + 1 else p0, p1)
            val f = parent.javaClass.getField(name)
            return f[parent]
        }
    }

    private inner class RefFunc(start: Int, end: Int) : RefElem(start, end) {
        val list: MutableList<RefObj> = ArrayList()
        @Throws(Exception::class)
        override fun eval(parent: Any?): Any? {
            var parent = parent
            if (str[p0] == '_' && parent != null) throw Exception("global only allowed for bottom level: at $p0")
            if (parent == null) parent = if (str[p0] == '_') ctx else obj
            val name = str.substring(if (str[p0] == '_') p0 + 1 else p0, p1)
            val ms = parent.javaClass.methods
            val args = arrayOfNulls<Any>(list.size)
            for (i in args.indices) args[i] = list[i].eval()
            for (m in ms) if (m.name == name && m.parameterCount == list.size) return m.invoke(parent, *args)
            throw Exception("function " + name + " not found for class " + parent.javaClass)
        }
    }

    private inner class RefObj(start: Int, end: Int) : Elem(start, end) {
        private val list: MutableList<RefElem> = ArrayList()
        @Throws(Exception::class)
        override fun build(sb: StringBuilder) {
            sb.append("" + eval())
        }

        @Throws(Exception::class)
        fun eval(): Any? {
            var obj: Any? = null
            for (e in list) obj = e.eval(obj)
            return obj
        }

        init {
            var pre = p0
            var i = p0
            while (i < p1) {
                val ch = str[i++]
                if (ch == '.') {
                    list.add(RefField(pre, i - 1))
                    pre = i
                }
                if (ch == '(') {
                    val func = RefFunc(pre, i - 1)
                    pre = i
                    var depth = 1
                    while (depth > 0) {
                        if (i >= p1) throw Exception("unfinished at $i")
                        val chr = str[i++]
                        if (chr == '(') depth++
                        if (chr == ')') {
                            depth--
                            if (depth == 0) {
                                if (i - 1 > pre) func.list.add(RefObj(pre, i - 1))
                                pre = i
                            }
                        }
                        if (chr == ',' && depth == 1) {
                            func.list.add(RefObj(pre, i - 1))
                            pre = i
                        }
                    }
                    list.add(func)
                }
            }
            if (pre < p1) list.add(RefField(pre, p1))
        }
    }

    private inner class Root private constructor(start: Int, end: Int) : Cont(start, end) {
        init {
            var pre = p0
            var deepth = 0
            for (i in p0 until p1) {
                val ch = str[i]
                if (ch == '[') {
                    if (deepth == 0 && i > pre) {
                        list.add(TextRef(pre, i))
                        pre = i + 1
                    }
                    deepth++
                }
                if (ch == ']') {
                    deepth--
                    if (deepth == 0 && i > pre) {
                        list.add(CodeBlock(pre, i))
                        pre = i + 1
                    }
                }
            }
            if (pre < p1) list.add(TextRef(pre, p1))
        }
    }

    private inner class TextPlain(start: Int, end: Int) : Elem(start, end) {
        override fun build(sb: StringBuilder) {
            sb.append(str, p0, p1)
        }
    }

    private inner class TextRef(start: Int, end: Int) : Cont(start, end) {
        init {
            var pre = p0
            var depth = 0
            for (i in p0 until p1) {
                val ch = str[i]
                if (ch == '(') {
                    if (depth == 0 && i > pre) list.add(TextPlain(pre, i))
                    if (depth == 0) pre = i + 1
                    depth++
                }
                if (ch == ')') {
                    depth--
                    if (depth == 0 && i > pre) list.add(RefObj(pre, i))
                    if (depth == 0) pre = i + 1
                }
            }
            if (pre < p1) list.add(TextPlain(pre, p1))
        }
    }

    private val root: Formatter.Root
    private fun check(): String? {
        val stack = Stack<Int>()
        for (i in 0 until str.length) {
            val ch = str[i]
            if (ch == '(') stack.push(0)
            if (ch == '[') stack.push(1)
            if (ch == '{') stack.push(2)
            if (ch == ')' && (stack.isEmpty() || stack.pop() != 0)) return "bracket ) unexpected at $i"
            if (ch == ']' && (stack.isEmpty() || stack.pop() != 1)) return "bracket ] unexpected at $i"
            if (ch == '}' && (stack.isEmpty() || stack.pop() != 2)) return "bracket } unexpected at $i"
        }
        return if (stack.isEmpty()) null else "unenclosed bracket: $stack"
    }

    companion object {
        @StaticPermitted
        private val MATCH = arrayOf(">=", "<=", "==", "!=", ">", "<")
        fun format(pattern: String, obj: Any?, ctx: Any?): String {
            val sb = StringBuilder()
            try {
                val f = common.util.lang.Formatter(pattern, obj, ctx)
                f.root.build(sb)
            } catch (e: Exception) {
                CommonStatic.ctx.noticeErr(e, ErrType.ERROR,
                        "err during formatting " + pattern + " with " + JsonEncoder.Companion.encode(obj))
            }
            return sb.toString()
        }
    }

    init {
        val err = check()
        if (err != null) throw Exception(err)
        root = Formatter.Root(0, str.length)
    }
}
