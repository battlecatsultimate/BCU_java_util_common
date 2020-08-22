package common.pack

import common.CommonStatic
import common.CommonStatic.ImgReader
import common.battle.data.CustomEnemy
import common.battle.data.CustomUnit
import common.io.InStream
import common.pack.Context.ErrType
import common.pack.PackData.PackDesc
import common.pack.PackData.UserPack
import common.system.VImg
import common.system.fake.FakeImage
import common.system.files.FDFile
import common.util.Data
import common.util.Data.Proc.Theme
import common.util.anim.AnimCE
import common.util.anim.AnimCI
import common.util.pack.Background
import common.util.stage.CastleImg
import common.util.stage.MapColc.PackMapColc
import common.util.stage.Music
import common.util.unit.*
import common.util.unit.Unit
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.collections.set

abstract class VerFixer private constructor(id: String) : Source(id) {
    class IdFixer(cls: Class<*>?) {
        private val ent: Class<*>
        fun parse(`val`: Int, cls: Class<*>): Class<*> {
            return if (cls == Theme::class.java) Background::class.java else if (ent == Unit::class.java) ent else if (`val` % 1000 < 500) Enemy::class.java else EneRand::class.java
        }

        init {
            ent = cls ?: AbEnemy::class.java
        }
    }

    class VerFixerException(str: String?) : Exception(str) {
        companion object {
            private const val serialVersionUID = 1L
        }
    }

    private class EnemyFixer(id: String, r: ImgReader?) : VerFixer(id) {
        @Throws(VerFixerException::class)
        override fun load() {
            loadEnemies(`is`.subStream())
            loadUnits(`is`.subStream())
            loadBackgrounds(`is`.subStream())
            loadCastles(`is`.subStream())
            loadMusics(`is`.subStream())
            data.mc = PackMapColc(data, `is`)
            `is` = null
        }

        @Throws(VerFixerException::class)
        private fun loadAnim(`is`: InStream): AnimCE {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 401) throw VerFixerException("DIYAnim expects version 401, got $ver")
            val type: Int = `is`.nextInt()
            if (type == 0) return AnimCE(`is`.nextString()) else if (type == 1) return decodeAnim(id, `is`.subStream(), r)
            throw VerFixerException("DIYAnim expects type 0 or 1, got " + 2)
        }

        @Throws(VerFixerException::class)
        private fun loadBackgrounds(`is`: InStream) {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 400) throw VerFixerException("expect bg store version to be 400, got $ver")
            val f: File = CommonStatic.def.route("./res/img/$id/bg/")
            if (f.exists()) {
                val fs = f.listFiles()
                for (fi in fs) {
                    val str = fi.name
                    if (str.length != 7) continue
                    if (!str.endsWith(".png")) continue
                    var `val` = -1
                    `val` = try {
                        str.substring(0, 3).toInt()
                    } catch (e: NumberFormatException) {
                        continue
                    }
                    val fx: File = CommonStatic.ctx.getWorkspaceFile("./$id/backgrounds/$str")
                    fi.renameTo(fx)
                    val bimg: VImg = CommonStatic.def.readReal(fx)
                    if (`val` >= 0 && bimg != null) data.bgs.set(`val`, Background(PackData.Identifier(id, Background::class.java, `val`), bimg))
                }
            }
            val n: Int = `is`.nextInt()
            for (i in 0 until n) {
                val ind: Int = `is`.nextInt()
                val bg: Background = data.bgs.get(ind) ?: continue
                bg.top = `is`.nextInt() > 0
                bg.ic = `is`.nextInt()
                for (j in 0..3) {
                    val p: Int = `is`.nextInt()
                    bg.cs[j] = intArrayOf(p shr 16 and 255, p shr 8 and 255, p and 255)
                }
            }
        }

        @Throws(VerFixerException::class)
        private fun loadCastles(`is`: InStream) {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 307) throw VerFixerException("expect castle store version to be 307, got $ver")
            `is`.nextInt()
            val f: File = CommonStatic.def.route("./res/img/$id/cas/")
            if (f.exists()) {
                val fs = f.listFiles()
                for (fi in fs) {
                    val str = fi.name
                    if (str.length != 7) continue
                    if (!str.endsWith(".png")) continue
                    var `val` = -1
                    `val` = try {
                        str.substring(0, 3).toInt()
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                        continue
                    }
                    val fx: File = CommonStatic.ctx.getWorkspaceFile("./$id/castles/$str")
                    fi.renameTo(fx)
                    val bimg: VImg = CommonStatic.def.readReal(fx)
                    if (`val` >= 0 && bimg != null) data.castles.set(`val`, CastleImg(PackData.Identifier<CastleImg>(id, CastleImg::class.java, `val`), bimg))
                }
            }
        }

        @Throws(VerFixerException::class)
        private fun loadEnemies(`is`: InStream) {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 402) throw VerFixerException("expect enemy store version to be 402, got $ver")
            UserProfile.Companion.setStatic(PackData.Identifier.Companion.STATIC_FIXER, IdFixer(AbEnemy::class.java))
            val len: Int = `is`.nextInt()
            for (i in 0 until len) {
                val ce = CustomEnemy()
                ce.fillData(402, `is`)
                val hash: Int = `is`.nextInt()
                val anim: InStream = `is`.subStream()
                val na: String = `is`.nextString()
                val ac: AnimCE = loadAnim(anim)
                val e = Enemy(PackData.Identifier<AbEnemy>(id, Enemy::class.java, hash), ac, ce)
                e.name = na
                data.enemies.set(hash % 1000, e)
            }
            val n: Int = `is`.nextInt()
            for (i in 0 until n) {
                val hash: Int = `is`.nextInt()
                val e = EneRand(PackData.Identifier<AbEnemy>(id, EneRand::class.java, hash + 500)) // TODO rand enemies
                e.zread(`is`.subStream())
                data.randEnemies.set(hash, e)
            }
        }

        private fun loadMusics(`is`: InStream) {
            val f: File = CommonStatic.def.route("./res/img/$id/music/")
            if (f.exists() && f.isDirectory) {
                val fs = f.listFiles()
                for (fi in fs) {
                    val str = fi.name
                    if (str.length != 7) continue
                    if (!str.endsWith(".ogg")) continue
                    var `val` = -1
                    `val` = try {
                        str.substring(0, 3).toInt()
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                        continue
                    }
                    val fx: File = CommonStatic.ctx.getWorkspaceFile("./$id/musics/$str")
                    fi.renameTo(fx)
                    if (`val` >= 0) data.musics.set(`val`, Music(PackData.Identifier<Music>(id, Music::class.java, `val`), FDFile(fx)))
                }
            }
        }

        @Throws(VerFixerException::class)
        private fun loadUnits(`is`: InStream) {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 401) throw VerFixerException("expect unit store version to be 401, got $ver")
            UserProfile.Companion.setStatic(PackData.Identifier.Companion.STATIC_FIXER, IdFixer(Unit::class.java))
            var n: Int = `is`.nextInt()
            for (i in 0 until n) {
                val ind: Int = `is`.nextInt()
                val ul = UnitLevel(PackData.Identifier<UnitLevel>(id, UnitLevel::class.java, ind), `is`)
                data.unitLevels.set(ind, ul)
            }
            n = `is`.nextInt()
            for (i in 0 until n) {
                val ind: Int = `is`.nextInt()
                val u = Unit(PackData.Identifier(id, Unit::class.java, ind))
                u.lv = PackData.Identifier.Companion.parseInt<UnitLevel>(`is`.nextInt(), UnitLevel::class.java).get()
                u.lv.units.add(u)
                u.max = `is`.nextInt()
                u.maxp = `is`.nextInt()
                u.rarity = `is`.nextInt()
                val m: Int = `is`.nextInt()
                u.forms = arrayOfNulls(m)
                for (j in 0 until m) {
                    val name: String = `is`.nextString()
                    val ac: AnimCE = loadAnim(`is`.subStream())
                    val cu = CustomUnit()
                    cu.fillData(401, `is`)
                    u.forms[j] = Form(u, j, name, ac, cu)
                }
                data.units.set(ind, u)
            }
            UserProfile.Companion.setStatic(PackData.Identifier.Companion.STATIC_FIXER, null)
        }

        init {
            this.r = r
        }
    }

    private class PackFixer(id: String, r: ImgReader?) : VerFixer(id) {
        @Throws(Exception::class)
        override fun load() {
            loadEnemies(`is`.subStream())
            loadUnits(`is`.subStream())
            loadBackgrounds(`is`.subStream())
            loadCastles(`is`.subStream())
            loadMusics(`is`.subStream())
            data.mc = PackMapColc(data, `is`)
            `is` = null
        }

        @Throws(Exception::class)
        private fun loadBackgrounds(`is`: InStream) {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 400) throw VerFixerException("expect bg store version to be 400, got $ver")
            val n: Int = `is`.nextInt()
            for (i in 0 until n) {
                val ind: Int = `is`.nextInt()
                val vimg: VImg = ImgReader.Companion.readImg(`is`, r)
                vimg.name = Data.Companion.trio(ind)
                writeImgs(vimg, "backgrounds", vimg.name + ".png")
                val bg = Background(PackData.Identifier(id, Background::class.java, ind), vimg)
                data.bgs.set(ind, bg)
                bg.top = `is`.nextInt() > 0
                bg.ic = `is`.nextInt()
                for (j in 0..3) {
                    val p: Int = `is`.nextInt()
                    bg.cs[j] = intArrayOf(p shr 8 and 255, p shr 8 and 255, p and 255)
                }
            }
        }

        @Throws(Exception::class)
        private fun loadCastles(`is`: InStream) {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 307) throw VerFixerException("expect castle store version to be 307, got $ver")
            val n: Int = `is`.nextInt()
            for (i in 0 until n) {
                val `val`: Int = `is`.nextInt()
                val vimg: VImg = ImgReader.Companion.readImg(`is`, r)
                vimg.name = Data.Companion.trio(`val`)
                writeImgs(vimg, "castles", vimg.name + ".png")
                data.castles.set(`val`, CastleImg(PackData.Identifier<CastleImg>(id, CastleImg::class.java, `val`), vimg))
            }
        }

        @Throws(VerFixerException::class)
        private fun loadEnemies(`is`: InStream) {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 402) throw VerFixerException("expect enemy store version to be 402, got $ver")
            UserProfile.Companion.setStatic(PackData.Identifier.Companion.STATIC_FIXER, IdFixer(AbEnemy::class.java))
            var n: Int = `is`.nextInt()
            for (i in 0 until n) {
                val hash: Int = `is`.nextInt()
                val str: String = `is`.nextString()
                val ce = CustomEnemy()
                ce.fillData(ver, `is`)
                val ac: AnimCE = decodeAnim(".temp_$id", `is`.subStream(), r)
                val e = Enemy(PackData.Identifier<AbEnemy>(id, Enemy::class.java, hash), ac, ce)
                e.name = str
                data.enemies.set(hash % 1000, e)
            }
            n = `is`.nextInt()
            for (i in 0 until n) {
                val hash: Int = `is`.nextInt()
                val e = EneRand(PackData.Identifier<AbEnemy>(id, EneRand::class.java, hash + 500)) // TODO randEnemy
                e.zread(`is`.subStream())
                data.randEnemies.set(hash, e)
            }
        }

        @Throws(VerFixerException::class)
        private fun loadMusics(`is`: InStream) {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 307) throw VerFixerException("expect music store version to be 307, got $ver")
            val n: Int = `is`.nextInt()
            for (i in 0 until n) {
                val `val`: Int = `is`.nextInt()
                val f: File = ImgReader.Companion.loadMusicFile(`is`, r, id.toInt(), `val`)
                f.renameTo(CommonStatic.ctx.getWorkspaceFile("./.temp_" + `val` + "/musics/" + Data.Companion.trio(`val`) + ".ogg"))
                data.musics.set(`val`, Music(PackData.Identifier<Music>(id, Music::class.java, `val`), FDFile(f)))
            }
        }

        @Throws(VerFixerException::class)
        private fun loadUnits(`is`: InStream) {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 401) throw VerFixerException("expect unit store version to be 401, got $ver")
            UserProfile.Companion.setStatic(PackData.Identifier.Companion.STATIC_FIXER, IdFixer(Unit::class.java))
            var n: Int = `is`.nextInt()
            for (i in 0 until n) {
                val ind: Int = `is`.nextInt()
                val ul = UnitLevel(PackData.Identifier<UnitLevel>(id, UnitLevel::class.java, ind), `is`)
                data.unitLevels.set(ind, ul)
            }
            n = `is`.nextInt()
            for (i in 0 until n) {
                val ind: Int = `is`.nextInt()
                val u = Unit(PackData.Identifier(id, Unit::class.java, ind))
                u.lv = PackData.Identifier.Companion.parseInt<UnitLevel>(`is`.nextInt(), UnitLevel::class.java).get()
                u.lv.units.add(u)
                u.max = `is`.nextInt()
                u.maxp = `is`.nextInt()
                u.rarity = `is`.nextInt()
                val m: Int = `is`.nextInt()
                u.forms = arrayOfNulls(m)
                for (j in 0 until m) {
                    val name: String = `is`.nextString()
                    val ac: AnimCE = decodeAnim(".temp_$id", `is`.subStream(), r)
                    val cu = CustomUnit()
                    cu.fillData(401, `is`)
                    u.forms[j] = Form(u, j, name, ac, cu)
                }
                data.units.set(ind, u)
            }
            UserProfile.Companion.setStatic(PackData.Identifier.Companion.STATIC_FIXER, null)
        }

        @Throws(IOException::class)
        private fun writeImgs(img: VImg, type: String, name: String) {
            val path = "./.temp_$id/$type/$name"
            val f: File = CommonStatic.ctx.getWorkspaceFile(path)
            FakeImage.Companion.write(img.getImg(), "", f)
            img.unload()
        }

        init {
            this.r = r
        }
    }

    var r: ImgReader? = null
    var data: UserPack? = null
    var `is`: InStream? = null
    override fun loadAnimation(name: String?): AnimCI? {
        return null
    }

    @Throws(Exception::class)
    override fun readImage(path: String): FakeImage? {
        return null
    }

    @Throws(Exception::class)
    override fun streamFile(path: String): InputStream? {
        return null
    }

    protected fun decodeAnim(target: String?, `is`: InStream?, r: ImgReader?): AnimCE {
        val al: AnimLoader = CommonStatic.def.loadAnim(`is`, r)
        val id = al.name
        id.pack = target
        val ce = AnimCE(al)
        SourceAnimSaver(id, ce).saveAll()
        return AnimCE(id)
    }

    @Throws(Exception::class)
    protected abstract fun load()

    companion object {
        @Throws(Exception::class)
        fun fix() {
            transform()
            readPacks()
            Context.Companion.delete(File("./res"))
            Context.Companion.delete(File("./pack"))
        }

        @Throws(VerFixerException::class)
        private fun fix_bcuenemy(`is`: InStream, r: ImgReader): VerFixer {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 400) throw VerFixerException("unexpected bcuenemy data version: $ver, requires 400")
            val desc = PackDesc(Data.Companion.hex(`is`.nextInt()))
            val n: Int = `is`.nextByte()
            for (i in 0 until n) desc.dependency.add(Data.Companion.hex(`is`.nextInt()))
            val fix = EnemyFixer(desc.id, r)
            val ans = UserPack(desc, fix)
            fix.data = ans
            fix.`is` = `is`
            return fix
        }

        @Throws(Exception::class)
        private fun fix_bcupack(`is`: InStream, r: ImgReader): VerFixer {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 402) throw VerFixerException("unexpected bcupack data version: $ver, requires 402")
            val head: InStream = `is`.subStream()
            val desc = PackDesc(Data.Companion.hex(head.nextInt()))
            val n: Int = head.nextByte()
            for (i in 0 until n) desc.dependency.add(Data.Companion.hex(head.nextInt()))
            desc.BCU_VERSION = Data.Companion.revVer(head.nextInt())
            if (!desc.BCU_VERSION.startsWith("4.11")) VerFixerException("unexpected pack BCU version: " + desc.BCU_VERSION + ", requires 4.11.x")
            desc.time = head.nextString()
            desc.version = head.nextInt()
            desc.author = head.nextString()
            val fix = PackFixer(desc.id, r)
            val ans = UserPack(desc, fix)
            fix.data = ans
            fix.`is` = `is`
            return fix
        }

        private fun move(a: String, b: String) {
            val f = File(a)
            if (!f.exists()) return
            f.renameTo(File(b))
        }

        @Throws(Exception::class)
        private fun readPacks() {
            var f: File = CommonStatic.def.route("./pack/")
            val fmap: MutableMap<String, File> = HashMap()
            val map: MutableMap<String, VerFixer> = HashMap()
            if (f.exists()) {
                for (file in f.listFiles()) {
                    val str = file.name
                    if (!str.endsWith(".bcupack")) continue
                    var g: File = CommonStatic.def.route("./res/data/" + str.replace(".bcupack", ".bcudata"))
                    if (!g.exists()) g = file
                    val pack = fix_bcupack(CommonStatic.def.readBytes(g), CommonStatic.def.getReader(g))
                    fmap[pack.id] = file
                    map[pack.id] = pack
                }
            }
            val list: MutableList<VerFixer> = ArrayList()
            list.addAll(map.values)
            f = CommonStatic.def.route("./res/enemy/")
            if (f.exists()) for (file in f.listFiles()) {
                val str = file.name
                if (!str.endsWith(".bcuenemy")) continue
                val fix = fix_bcuenemy(CommonStatic.def.readBytes(file), CommonStatic.def.getReader(file))
                list.removeIf { p: VerFixer -> p.id === fix.id }
                list.add(fix)
            }
            while (list.size > 0) {
                var rem = 0
                for (p in list) {
                    var all = true
                    for (pre in p.data.desc.dependency) if (!map.containsKey(pre) || map[pre]!!.`is` != null) all = false
                    if (all) {
                        p.load()
                        rem++
                    }
                }
                list.removeIf { p: VerFixer -> p.`is` == null }
                if (rem == 0) {
                    for (p in list) {
                        map.remove(p.id)
                        CommonStatic.ctx.printErr(ErrType.WARN, "Failed to load " + p.data.desc)
                    }
                    break
                }
            }
        }

        @Throws(IOException::class)
        private fun transform() {
            val f = File("./res/anim/")
            for (fi in f.list()) {
                val pa = "./res/anim/$fi/"
                val pb = "./workspace/_local/$fi/"
                move("$pa$fi.png", pb + SourceAnimLoader.Companion.SP)
                move(pa + "edi.png", pb + SourceAnimLoader.Companion.EDI)
                move(pa + "uni.png", pb + SourceAnimLoader.Companion.UNI)
                move("$pa$fi.imgcut", pb + SourceAnimLoader.Companion.IC)
                move("$pa$fi.mamodel", pb + SourceAnimLoader.Companion.MM)
                move(pa + fi + "00.maanim", pb + SourceAnimLoader.Companion.MA.get(0))
                move(pa + fi + "01.maanim", pb + SourceAnimLoader.Companion.MA.get(1))
                move(pa + fi + "02.maanim", pb + SourceAnimLoader.Companion.MA.get(2))
                move(pa + fi + "03.maanim", pb + SourceAnimLoader.Companion.MA.get(3))
                move(pa + fi + "_zombie00.maanim", pb + SourceAnimLoader.Companion.MA.get(4))
                move(pa + fi + "_zombie01.maanim", pb + SourceAnimLoader.Companion.MA.get(5))
                move(pa + fi + "_zombie02.maanim", pb + SourceAnimLoader.Companion.MA.get(6))
            }
        }
    }
}
