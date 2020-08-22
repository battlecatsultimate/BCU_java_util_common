package common.io.json

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import common.io.PackLoader
import common.io.PackLoader.Preload
import common.io.PackLoader.ZipDesc
import common.io.PackLoader.ZipDesc.FileDesc
import common.io.json.JsonClass.NoTag
import common.io.json.JsonClass.RType
import common.io.json.JsonDecoder.OnInjected
import common.io.json.Testimport
import common.pack.PackData.PackDesc
import common.util.Data
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException
import java.util.*

com.google.api.client.json.jackson2.JacksonFactory
import kotlin.Throws
import kotlin.jvm.JvmStatic
import java.util.HashMap
import common.io.json.JsonField.GenType
import java.util.HashSet
import common.io.json.JsonField.IOType
import common.io.json.JsonField.SerType

object Test {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        JsonTest_2.test()
        // testIO();
    }

    @Throws(Exception::class)
    fun testIO() {
        val pd = PackDesc()
        pd.author = "test"
        pd.BCU_VERSION = Data.Companion.revVer(41100)
        pd.id = "12345678abcdefgh"
        PackLoader.writePack(File("./pack.pack"), File("./src"), pd, "password")
        val ctx = Preload { fd: FileDesc -> fd.size < 4096 }
        val desc: ZipDesc = PackLoader.readPack(ctx, File("./pack.pack"))
        for (fd in desc.files) if (!ctx.preload(fd)) {
            println("large: " + fd.path)
            val f = File("./out/" + fd.path)
            getFile(f)
            val fos = FileOutputStream(f)
            val bs = ByteArray(fd.size)
            desc.readFile(fd.path).read(bs)
            fos.write(bs)
            fos.close()
        }
    }

    @Throws(Exception::class)
    fun testJson() {
        val f = File("./../BCU-JSON-IO/testjson/test_0.json")
        val elem: JsonElement = JsonParser.parseReader(FileReader(f))
        val obj: Test.JsonTest_0.JsonA = JsonDecoder.Companion.decode<Test.JsonTest_0.JsonA>(elem, Test.JsonTest_0.JsonA::class.java)
        println(JsonEncoder.Companion.encode(obj))
    }

    private fun getFile(f: File): File {
        try {
            if (!f.parentFile.exists()) f.parentFile.mkdirs()
            if (!f.exists()) f.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return f
    }

    class JsonTest_0 {
        @JsonClass
        class JsonA {
            @JsonField(generic = [Int::class])
            val f0: ArrayList<Int>? = null

            @JsonField
            var f1: Test.JsonTest_0.JsonC? = null

            @JsonField(gen = GenType.GEN, generator = "gen", generic = [Test.JsonTest_0.JsonB::class])
            var f2: ArrayList<Test.JsonTest_0.JsonB>? = null

            @JsonField(gen = GenType.FILL)
            var f3 = Test.JsonTest_0.JsonB(this)

            @JsonField(generic = [Int::class, String::class])
            var f4: HashMap<Int, String>? = null

            @JsonField
            var data: JsonD? = null
            fun gen(cls: Class<*>?, jobj: JsonElement?): Test.JsonTest_0.JsonB {
                return Test.JsonTest_0.JsonB(this)
            }
        }

        @JsonClass(read = RType.FILL)
        class JsonB(var par: Test.JsonTest_0.JsonA) {
            @JsonField(generic = [Int::class])
            var f: HashSet<Int>? = null
            @OnInjected
            fun create() {
                println("OnInjected: " + f!!.size)
            }
        }

        @JsonClass(read = RType.MANUAL, generator = "gen")
        class JsonC {
            @JsonField(tag = "a", io = IOType.W)
            fun getA(): Int {
                return 10
            }

            @JsonField(tag = "a", io = IOType.R)
            fun setA(a: Int) {
                println(a)
            }

            companion object {
                @Throws(JsonException::class)
                fun gen(o: JsonElement?): Test.JsonTest_0.JsonC {
                    return Test.JsonTest_0.JsonC()
                }
            }
        }

        @JsonClass(noTag = NoTag.LOAD)
        class JsonD {
            var a = 0
            var b: IntArray
            var c: String? = null
            var d: Array<String>
            var e = false
        }
    }

    class JsonTest_1 {
        @JsonClass
        class JsonA {
            @JsonField
            var name: String? = null

            @JsonField(generic = [String::class, Test.JsonTest_1.JsonB::class], gen = GenType.GEN)
            var list: HashMap<String, Test.JsonTest_1.JsonB>? = null

            @JsonField(gen = GenType.FILL)
            var link = Test.JsonTest_1.JsonC(this)
        }

        @JsonClass(read = RType.FILL)
        class JsonB(val parent: Test.JsonTest_1.JsonA) {
            @JsonField
            var key: String? = null

            @JsonField
            var name: String? = null
        }

        @JsonClass
        class JsonC(val parent: Test.JsonTest_1.JsonA) {
            @JsonField(generic = [Test.JsonTest_1.JsonB::class, Int::class], gen = GenType.GEN, generator = "gen", ser = SerType.FUNC, serializer = "ser")
            var list: HashMap<Test.JsonTest_1.JsonB, Int>? = null
            fun gen(cls: Class<*>, elem: JsonElement): Any? {
                if (cls == Test.JsonTest_1.JsonB::class.java) return parent.list[elem.asString]
                return if (cls == Int::class.java) elem.asInt * 10 else null
            }

            fun ser(b: Test.JsonTest_1.JsonB): Any {
                return b.key
            }
        }
    }

    object JsonTest_2 {
        @Throws(Exception::class)
        fun test() {
            val a = JsonA()
            a.list = arrayOfNulls(4)
            val b0 = JsonB()
            b0.name = "a"
            b0.`val` = 1
            val b1 = JsonB()
            b1.name = "b"
            b1.`val` = 2
            a.list[1] = b0
            a.list[0] = a.list[1]
            a.list[3] = b1
            a.list[2] = a.list[3]
            val out: JsonElement = JsonEncoder.Companion.encode(a)
            println(out)
            val a1: JsonA = JsonDecoder.Companion.decode<JsonA>(out, JsonA::class.java)
            a1.list[0]!!.`val` = 3
            val out1: JsonElement = JsonEncoder.Companion.encode(a1)
            println(out1)
        }

        @JsonClass
        class JsonA {
            @JsonField(usePool = true)
            var list: Array<JsonB?>

            companion object {
                const val REG = "test"
            }
        }

        @JsonClass(noTag = NoTag.LOAD)
        class JsonB {
            var name: String? = null
            var `val` = 0
        }
    }
}
