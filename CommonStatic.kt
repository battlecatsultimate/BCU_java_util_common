package common

import common.io.InStream
import common.io.OutStream
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.json.JsonClass
import common.io.json.JsonClass.NoTag
import common.pack.Context
import common.pack.Source.AnimLoader
import common.pack.UserProfile
import common.system.VImg
import common.system.fake.FakeImage
import common.util.Data
import common.util.anim.ImgCut
import common.util.anim.MaAnim
import common.util.anim.MaModel
import common.util.pack.EffAnim.EffAnimStore
import common.util.pack.NyCastle
import common.util.pack.WaveAnim
import common.util.stage.Music
import common.util.unit.Combo
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.util.*
import java.util.function.Function
import java.util.function.Supplier

object CommonStatic {
    @StaticPermitted(Admin.StaticPermitted.Type.ENV)
    var def: Itf? = null

    @StaticPermitted(Admin.StaticPermitted.Type.ENV)
    var ctx: Context? = null
    fun getBCAssets(): BCAuxAssets {
        return UserProfile.Companion.getStatic<BCAuxAssets>("BCAuxAssets", Supplier { BCAuxAssets() })
    }

    fun getConfig(): Config {
        return UserProfile.Companion.getStatic<Config>("config", Supplier { Config() })
    }

    fun isInteger(str: String): Boolean {
        for (i in 0 until str.length) {
            if (!Character.isDigit(str[i])) {
                return false
            }
        }
        return true
    }

    fun parseIntN(str: String): Int {
        val ans: Int
        ans = try {
            parseIntsN(str)[0]
        } catch (e: Exception) {
            -1
        }
        return ans
    }

    fun parseIntsN(str: String): IntArray {
        val lstr = ArrayList<String>()
        var t = -1
        for (i in 0 until str.length) if (t == -1) {
            if (Character.isDigit(str[i]) || str[i] == '-' || str[i] == '+') t = i
        } else if (!Character.isDigit(str[i])) {
            lstr.add(str.substring(t, i))
            t = -1
        }
        if (t != -1) lstr.add(str.substring(t))
        var ind = 0
        while (ind < lstr.size) {
            if (Character.isDigit(lstr[ind][0]) || lstr[ind].length > 1) ind++ else lstr.removeAt(ind)
        }
        val ans = IntArray(lstr.size)
        for (i in lstr.indices) ans[i] = lstr[i].toInt()
        return ans
    }

    fun parseLongN(str: String): Long {
        val ans: Long
        ans = try {
            parseLongsN(str)[0]
        } catch (e: Exception) {
            -1
        }
        return ans
    }

    fun parseLongsN(str: String): LongArray {
        val lstr = ArrayList<String>()
        var t = -1
        for (i in 0 until str.length) if (t == -1) {
            if (Character.isDigit(str[i]) || str[i] == '-' || str[i] == '+') t = i
        } else if (!Character.isDigit(str[i])) {
            lstr.add(str.substring(t, i))
            t = -1
        }
        if (t != -1) lstr.add(str.substring(t))
        var ind = 0
        while (ind < lstr.size) {
            if (Character.isDigit(lstr[ind][0]) || lstr[ind].length > 1) ind++ else lstr.removeAt(ind)
        }
        val ans = LongArray(lstr.size)
        for (i in lstr.indices) ans[i] = lstr[i].toLong()
        return ans
    }

    /** play sound effect  */
    fun setSE(ind: Int) {
        def!!.setSE(ind)
    }

    fun toArrayFormat(vararg data: Int): String {
        var res = "{"
        for (i in 0 until data.size) {
            res += if (i == data.size - 1) {
                data[i].toString() + "}"
            } else {
                data[i].toString() + ", "
            }
        }
        return res
    }

    interface BattleConst {
        companion object {
            const val ratio = 768.0 / 2400.0 // r = p/u
        }
    }

    class BCAuxAssets {
        // Res resources
        var slot: Array<VImg?> = arrayOfNulls<VImg>(3)
        var ico: Array<Array<VImg>> = arrayOfNulls<Array<VImg>>(2)
        var num: Array<Array<VImg?>> = Array<Array<VImg?>>(9) { arrayOfNulls<VImg>(11) }
        var battle: Array<Array<VImg>> = arrayOfNulls<Array<VImg>>(3)
        var icon: Array<Array<VImg>> = arrayOfNulls<Array<VImg>>(4)

        // Background resources
        var ewavm: MaModel? = null
        var uwavm: MaModel? = null
        var ewava: MaAnim? = null
        var uwava: MaAnim? = null
        var defu: WaveAnim? = null
        var defe: WaveAnim? = null
        val iclist: List<ImgCut> = ArrayList<ImgCut>()

        // Available data for atk/res orb, will be used for GUI
        // Map<Trait, Grades>
        val ATKORB: Map<Int, List<Int>> = TreeMap<Int, List<Int>>()
        val RESORB: Map<Int, List<Int>> = TreeMap<Int, List<Int>>()
        val DATA: Map<Int, Int> = HashMap()
        var TYPES: Array<FakeImage>
        var TRAITS: Array<FakeImage>
        var GRADES: Array<FakeImage>

        // NyCastle
        val main: Array<Array<VImg?>> = Array<Array<VImg?>>(3) { arrayOfNulls<VImg>(NyCastle.Companion.TOT) }
        val atks: Array<NyCastle?> = arrayOfNulls<NyCastle>(NyCastle.Companion.TOT)

        // EffAnim
        val effas: EffAnimStore = EffAnimStore()

        // Combo
        val combos: Array<Array<Combo>> = arrayOfNulls<Array<Combo>>(Data.Companion.C_TOT)
        val values = Array(Data.Companion.C_TOT) { IntArray(5) }
        var filter: Array<IntArray>

        // Form cuts
        var unicut: ImgCut? = null
        var udicut: ImgCut? = null

        // RandStage
        val randRep = arrayOfNulls<IntArray>(5)
    }

    @JsonClass(noTag = NoTag.LOAD)
    class Config {
        // ImgCore
        var deadOpa = 10
        var fullOpa = 90
        var ints = intArrayOf(1, 1, 1, 2)
        var ref = true
        var battle = false
        var icon = false

        // Lang
        var lang = 0
    }

    interface EditLink {
        fun review()
    }

    interface FakeKey {
        fun pressed(i: Int, j: Int): Boolean
        fun remove(i: Int, j: Int)
    }

    interface ImgReader {
        fun isNull(): Boolean {
            return true
        }

        fun readFile(`is`: InStream): File?
        fun readImg(str: String?): FakeImage?
        fun readImgOptional(str: String?): VImg?

        companion object {
            fun loadMusicFile(`is`: InStream, r: ImgReader?, pid: Int, mid: Int): File? {
                var r = r
                if (r == null || r.isNull()) r = def!!.getMusicReader(pid, mid)
                return r!!.readFile(`is`)
            }

            fun readImg(`is`: InStream, r: ImgReader?): VImg? {
                return if (r != null && !r.isNull()) r.readImgOptional(`is`.nextString()) else VImg(`is`.nextBytesI())
            }
        }
    }

    interface ImgWriter {
        fun saveFile(f: File?): String?
        fun writeImg(img: FakeImage?): String?
        fun writeImgOptional(img: VImg?): String?

        companion object {
            fun saveFile(os: OutStream, w: ImgWriter?, file: File): Boolean {
                if (w != null) os.writeString(w.saveFile(file)) else try {
                    val data: OutStream = OutStream.Companion.getIns()
                    val bs = ByteArray(file.length().toInt())
                    val buf = BufferedInputStream(FileInputStream(file))
                    buf.read(bs, 0, bs.size)
                    buf.close()
                    data.writeBytesI(bs)
                    data.terminate()
                    os.accept(data)
                } catch (e: Exception) {
                    e.printStackTrace()
                    return false
                }
                return true
            }

            fun writeImg(os: OutStream, w: ImgWriter?, img: FakeImage?): Boolean {
                if (w != null) os.writeString(w.writeImg(img)) else try {
                    val baos = ByteArrayOutputStream()
                    FakeImage.Companion.write(img, "PNG", baos)
                    os.writeBytesI(baos.toByteArray())
                } catch (e: Exception) {
                    e.printStackTrace()
                    return false
                }
                return true
            }
        }
    }

    interface Itf {
        /** exit  */
        fun exit(save: Boolean)
        fun getMusicLength(f: Music): Long
        fun getMusicReader(pid: Int, mid: Int): ImgReader?
        fun getReader(f: File?): ImgReader?
        fun loadAnim(`is`: InStream, r: ImgReader?): AnimLoader

        /** show loading progress, empty is fine  */
        fun prog(str: String?)

        /** read InStream from file  */
        fun readBytes(fi: File?): InStream

        /** load read image instance  */
        fun readReal(fi: File?): VImg?

        /** read strings from path  */
        fun <T> readSave(path: String, func: Function<Queue<String?>?, T>): T
        fun route(path: String?): File
        fun setSE(ind: Int)

        /** write OutStream into file  */
        fun writeBytes(os: OutStream?, path: String?): Boolean

        /** Write error log to somewhere  */
        fun writeErrorLog(e: Exception?)
    }

    open class Lang {
        companion object {
            @StaticPermitted
            val LOC_CODE = arrayOf("en", "zh", "kr", "jp", "ru", "de", "fr", "nl", "es")

            @StaticPermitted
            val pref = arrayOf(intArrayOf(0, 3, 1, 2), intArrayOf(1, 3, 0, 2), intArrayOf(2, 3, 0, 1), intArrayOf(3, 0, 1, 2), intArrayOf(0, 3, 1, 2), intArrayOf(0, 3, 1, 2), intArrayOf(0, 3, 1, 2))
        }
    }
}
