package common.util.lang

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import common.CommonStatic
import common.io.assets.Admin
import common.io.json.JsonDecoder
import common.io.json.JsonEncoder
import common.util.Data.Proc
import common.util.Data.Proc.ProcItem
import common.util.lang.Formatter
import common.util.lang.ProcLang
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

object Main {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        var args: Array<String>? = args
        CommonStatic.ctx = Admin.AdminContext()
        val sc = Scanner(System.`in`)
        var str: String
        while (sc.nextLine().also { str = it } != "exit") Main.loop(str.split(" ").toTypedArray().also { args = it })
        sc.close()
    }

    @Throws(IOException::class)
    private fun fix(path: String): File {
        var path = path
        if (!path.startsWith("./")) path = "./$path"
        if (!path.endsWith(".json")) path += ".json"
        val f = File(path)
        if (!f.parentFile.exists()) f.parentFile.mkdirs()
        if (!f.exists()) f.createNewFile()
        return f
    }

    @Throws(Exception::class)
    private fun loop(args: Array<String>) {
        if (args.size == 3 && args[0] == "generate") {
            if (args[1] == "proc") {
                val f = Main.fix(args[2])
                val e: JsonElement = JsonEncoder.Companion.encode(Proc.Companion.blank())
                e.getAsJsonObject().add("context", JsonEncoder.Companion.encode(Formatter.Context()))
                val str: String = e.toString()
                Files.write(f.toPath(), Arrays.asList<String>(str))
                println("file generated")
                return
            }
            if (args[1] == "locale") {
                val f = Main.fix(args[2])
                val jo: JsonElement = JsonEncoder.Companion.encode(ProcLang.Companion.gen(null))
                val str: String = jo.toString()
                Files.write(f.toPath(), Arrays.asList<String>(str))
                println("file generated")
                return
            }
        }
        if (args.size == 4 && args[0] == "parse") {
            val locale: JsonElement = JsonParser.parseReader(Files.newBufferedReader(Main.parse(args[1])))
            val jeproc: JsonElement = JsonParser.parseReader(Files.newBufferedReader(Main.parse(args[2])))
            var ctx: Formatter.Context? = null
            ctx = if (jeproc.getAsJsonObject().has("context")) JsonDecoder.Companion.decode<Formatter.Context>(jeproc.getAsJsonObject().get("context"), Formatter.Context::class.java) else Formatter.Context()
            val item: ProcItem = JsonDecoder.Companion.decode<Proc>(jeproc, Proc::class.java).get(args[3])
            val lang: ProcLang = JsonDecoder.Companion.decode<ProcLang>(locale, ProcLang::class.java)
            val pattern: String = lang.get(args[3])!!.format
            println(Formatter.Companion.format(pattern, item, ctx))
            return
        }
        println("you entered: " + Arrays.asList<String>(*args).toString())
        println("usage: generate proc [name] // for generate a new PROC json file")
        println("usage: generate locale [name] // for generate a new LOCALE json file")
        println("usage: parse [locale] [file] [proc] // parse a proc in a proc file with specified locale")
    }

    private fun parse(path: String): Path {
        var path = path
        if (!path.startsWith("./")) path = "./$path"
        if (!path.endsWith(".json")) path += ".json"
        return File(path).toPath()
    }
}
