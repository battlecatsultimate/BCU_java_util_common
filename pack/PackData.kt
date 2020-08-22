package common.pack

import com.google.gson.JsonElement
import common.CommonStatic
import common.battle.data.DataEnemy
import common.battle.data.Orb
import common.battle.data.PCoin
import common.io.PackLoader.ZipDesc.FileDesc
import common.io.assets.AssetLoader
import common.io.json.JsonClass
import common.io.json.JsonClass.*
import common.io.json.JsonDecoder
import common.io.json.JsonField
import common.io.json.JsonField.GenType
import common.pack.Context.SupExc
import common.pack.FixIndexList.FixIndexMap
import common.pack.IndexContainer.*
import common.pack.PackData
import common.pack.PackData.UserPack
import common.pack.Source.Workspace
import common.pack.UserProfile
import common.pack.VerFixer.IdFixer
import common.system.files.FDFile
import common.system.files.VFile
import common.system.files.VFileRoot
import common.util.Data
import common.util.Res
import common.util.pack.Background
import common.util.pack.EffAnim
import common.util.pack.NyCastle
import common.util.pack.Soul
import common.util.stage.*
import common.util.stage.CastleList.PackCasList
import common.util.stage.Limit.DefLimit
import common.util.stage.MapColc.DefMapColc
import common.util.stage.MapColc.PackMapColc
import common.util.unit.*
import common.util.unit.Unit
import java.io.File
import java.lang.reflect.Method
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier
import kotlin.String

@JsonClass(read = RType.FILL, noTag = NoTag.LOAD)
abstract class PackData : IndexContainer {
    class DefPack : PackData() {
        var root: VFileRoot<FileDesc> = VFileRoot<FileDesc>(".")
        override fun getID(): String? {
            return common.pack.PackData.Identifier.Companion.DEF
        }

        fun load(progress: Consumer<String?>) {
            progress.accept("loading basic images")
            Res.readData()
            progress.accept("loading enemies")
            loadEnemies()
            progress.accept("loading units")
            loadUnits()
            progress.accept("loading auxiliary data")
            Combo.Companion.readFile()
            PCoin.Companion.read()
            progress.accept("loading effects")
            EffAnim.Companion.read()
            progress.accept("loading backgrounds")
            Background.Companion.read()
            progress.accept("loading cat castles")
            NyCastle.Companion.read()
            progress.accept("loading souls")
            loadSoul()
            progress.accept("loading stages")
            DefMapColc.Companion.read()
            RandStage.read()
            loadCharaGroup()
            loadLimit()
            progress.accept("loading orbs")
            Orb.Companion.read()
            progress.accept("loading musics")
            loadMusic()
        }

        private fun loadCharaGroup() {
            val qs: Queue<String> = VFile.Companion.readLine("./org/data/Charagroup.csv")
            qs.poll()
            for (str in qs) {
                val strs = str.split(",").toTypedArray()
                val id: Int = CommonStatic.parseIntN(strs[0])
                val type: Int = CommonStatic.parseIntN(strs[2])
                val units: Array<common.pack.PackData.Identifier<Unit>> = arrayOfNulls<common.pack.PackData.Identifier<*>>(strs.size - 3)
                for (i in 3 until strs.size) units[i - 3] = common.pack.PackData.Identifier.Companion.parseInt<Unit>(CommonStatic.parseIntN(strs[i]), Unit::class.java)
                groups.set(id, CharaGroup(id, type, units))
            }
        }

        private fun loadEnemies() {
            VFile.Companion.get("./org/enemy/").list().forEach(Consumer<VFile<FileDesc?>> { p: VFile<FileDesc?>? -> enemies.add(Enemy(p)) })
            var qs: Queue<String> = VFile.Companion.readLine("./org/data/t_unit.csv")
            qs.poll()
            qs.poll()
            for (e in enemies.getList()) (e.de as DataEnemy).fillData(qs.poll().split("//").toTypedArray()[0].trim { it <= ' ' }.split(",").toTypedArray())
            qs = VFile.Companion.readLine("./org/data/enemy_dictionary_list.csv")
            for (str in qs) enemies.get(str.split(",").toTypedArray()[0].toInt()).inDic = true
        }

        private fun loadLimit() {
            val qs: Queue<String> = VFile.Companion.readLine("./org/data/Stage_option.csv")
            qs.poll()
            for (str in qs) DefLimit(str.split(",").toTypedArray())
        }

        private fun loadMusic() {
            val dict: File = CommonStatic.ctx.getAssetFile("./music/")
            if (!dict.exists()) return
            val fs = dict.listFiles()
            for (f in fs) {
                val str = f.name
                if (str.length != 7) continue
                if (!str.endsWith(".ogg")) continue
                val id: Int = CommonStatic.parseIntN(str.substring(0, 3))
                if (id == -1) continue
                musics.set(id, Music(common.pack.PackData.Identifier.Companion.parseInt<Music>(id, Music::class.java), FDFile(f)))
            }
        }

        private fun loadSoul() {
            val pre = "./org/battle/soul/"
            val mid = "/battle_soul_"
            for (i in 0..12) souls.add(Soul(pre + Data.Companion.trio(i) + mid + Data.Companion.trio(i), i))
        }

        private fun loadUnits() {
            VFile.Companion.get("./org/unit").list().forEach(Consumer<VFile<FileDesc?>> { p: VFile<FileDesc?>? -> units.add(Unit(p)) })
            var qs: Queue<String> = VFile.Companion.readLine("./org/data/unitlevel.csv")
            val lu: List<Unit> = units.getList()
            val l: FixIndexList<UnitLevel> = unitLevels
            for (u in lu) {
                val strs = qs.poll().split(",").toTypedArray()
                val lv = IntArray(20)
                for (i in 0..19) lv[i] = strs[i].toInt()
                val ul = UnitLevel(lv)
                if (!l.contains(ul)) {
                    ul.id = common.pack.PackData.Identifier<UnitLevel>(common.pack.PackData.Identifier.Companion.DEF, UnitLevel::class.java, l.size())
                    l.add(ul)
                }
                val ind: Int = l.indexOf(ul)
                u.lv = l.get(ind)
                l.get(ind).units.add(u)
            }
            UnitLevel.Companion.def = l.get(2)
            qs = VFile.Companion.readLine("./org/data/unitbuy.csv")
            for (u in lu) {
                val strs = qs.poll().split(",").toTypedArray()
                u.rarity = strs[13].toInt()
                u.max = strs[50].toInt()
                u.maxp = strs[51].toInt()
                u.info.fillBuy(strs)
            }
        }
    }

    @JsonClass(noTag = NoTag.LOAD)
    class Identifier<T : Indexable<*, T>?> : Comparable<common.pack.PackData.Identifier<*>?>, Cloneable {
        var cls: Class<out T>?
        var pack: String?
        var id: Int

        @Deprecated("")
        constructor() {
            cls = null
            pack = null
            id = 0
        }

        constructor(pack: String?, cls: Class<out T>?, id: Int) {
            this.cls = cls
            this.pack = pack
            this.id = id
        }

        public override fun clone(): common.pack.PackData.Identifier<T> {
            return Data.Companion.err<Any>(SupExc<Any> { super.clone() })
        }

        override operator fun compareTo(identifier: common.pack.PackData.Identifier<*>): Int {
            val `val` = pack!!.compareTo(identifier.pack)
            return if (`val` != 0) `val` else Integer.compare(id, identifier.id)
        }

        fun equals(o: common.pack.PackData.Identifier<T>): Boolean {
            return pack == o.pack && id == o.id
        }

        @Deprecated("")
        @JCGetter
        fun get(): T? {
            val cont: IndexContainer = getCont()
            return cont.getList<Any?>(cls, Reductor<Any, FixIndexMap<*>> { r: Any?, l: FixIndexMap<*> ->
                r ?: l.get(id)
            }, null) as T
        }

        fun getCont(): IndexContainer {
            return common.pack.PackData.Identifier.Companion.getContainer(cls, pack)
        }

        fun isNull(): Boolean {
            return id == -1
        }

        override fun toString(): String {
            return "$pack/$id"
        }

        companion object {
            const val DEF = "_default"
            const val STATIC_FIXER = "id_fixer"
            operator fun <T : Indexable<*, T>?> get(id: common.pack.PackData.Identifier<T>?): T? {
                return id?.get()
            }

            /**
             * cls must be a class implementing Indexable. interfaces or other classes will
             * go through fixer
             */
            fun <T : Indexable<*, T>?> parseInt(v: Int, cls: Class<out T>?): common.pack.PackData.Identifier<T> {
                return common.pack.PackData.Identifier.Companion.parseIntRaw(v, cls)
            }

            @Deprecated("")
            fun parseIntRaw(v: Int, cls: Class<*>?): common.pack.PackData.Identifier<*> {
                var cls = cls
                if (cls == null || cls.isInterface || !Indexable::class.java.isAssignableFrom(cls)) cls = UserProfile.Companion.getStatic<IdFixer>(common.pack.PackData.Identifier.Companion.STATIC_FIXER, Supplier { IdFixer(null) }).parse(v, cls)
                val pack: String = if (cls != CastleImg::class.java && v / 1000 == 0) common.pack.PackData.Identifier.Companion.DEF else Data.Companion.hex(v / 1000)
                val id = v % 1000
                return common.pack.PackData.Identifier(pack, cls, id)
            }

            private fun getContainer(cls: Class<*>, str: String): Any? {
                var cont: IndexCont? = null
                val q: Queue<Class<*>> = ArrayDeque()
                q.add(cls)
                while (q.size > 0) {
                    val ci = q.poll()
                    if (ci.getAnnotation(IndexCont::class.java).also { cont = it } != null) break
                    if (ci.superclass != null) q.add(ci.superclass)
                    for (cj in ci.interfaces) q.add(cj)
                }
                if (cont == null) return null
                var m: Method? = null
                for (mi in cont.value().getMethods()) if (mi.getAnnotation(ContGetter::class.java) != null) m = mi
                if (m == null) return null
                val fm: Method = m
                return Data.Companion.err<Any>(SupExc<Any> { fm.invoke(null, str) })
            }
        }
    }

    @JsonClass(noTag = NoTag.LOAD)
    class PackDesc {
        var BCU_VERSION: String? = null
        var id: String? = null
        var author: String? = null
        var name: String? = null
        var desc: String? = null
        var time: String? = null
        var version = 0

        @JsonField(generic = [String::class])
        var dependency: ArrayList<String>? = null

        @JCConstructor
        @Deprecated("")
        constructor()

        constructor(id: String?) {
            BCU_VERSION = AssetLoader.CORE_VER
            this.id = id
            dependency = ArrayList()
        }
    }

    @JsonClass(read = RType.FILL)
    class UserPack : PackData {
        @JsonField
        val desc: PackDesc

        @JsonField(gen = GenType.FILL)
        var mc: PackMapColc? = null

        @JsonField(gen = GenType.FILL)
        var castles: PackCasList? = null
        val source: Source
        var editable = false
        var loaded = false
        private var elem: JsonElement? = null

        /** for old reading method only  */
        @Deprecated("")
        constructor(desc: PackDesc, s: Source) {
            this.desc = desc
            source = s
        }

        constructor(s: Source, desc: PackDesc, elem: JsonElement?) {
            this.desc = desc
            this.elem = elem
            source = s
            editable = source is Workspace
            mc = PackMapColc(this)
        }

        /** for generating new pack only  */
        constructor(id: String?) {
            desc = PackDesc(id)
            source = Workspace(id)
            castles = PackCasList(this)
            mc = PackMapColc(this)
            editable = true
            loaded = true
        }

        fun delete() {
            // FIXME Auto-generated method stub
        }

        fun forceRemoveParent(id: String?) {
            // FIXME Auto-generated method stub
        }

        override fun getID(): String? {
            return desc.id
        }

        fun <T : Indexable<*, T>?> getID(cls: Class<T>?, id: Int): common.pack.PackData.Identifier<T> {
            return common.pack.PackData.Identifier(desc.id, cls, id)
        }

        fun getReplays(): Collection<Recd>? {
            // FIXME Auto-generated method stub
            return null
        }

        fun loadMusics() {
            // FIXME
        }

        fun relyOn(id: String?): Boolean {
            // FIXME
            return false
        }

        @Throws(Exception::class)
        fun load() {
            JsonDecoder.Companion.inject<UserPack>(elem, UserPack::class.java, this)
            elem = null
            loaded = true
            loadMusics()
        }
    }

    val enemies: FixIndexMap<Enemy> = FixIndexMap<Enemy>(Enemy::class.java)
    val randEnemies: FixIndexMap<EneRand> = FixIndexMap<EneRand>(EneRand::class.java)
    val units: FixIndexMap<Unit> = FixIndexMap<Unit>(Unit::class.java)
    val unitLevels: FixIndexMap<UnitLevel> = FixIndexMap<UnitLevel>(UnitLevel::class.java)
    val souls: FixIndexMap<Soul> = FixIndexMap<Soul>(Soul::class.java)
    val bgs: FixIndexMap<Background> = FixIndexMap<Background>(Background::class.java)
    val groups: FixIndexMap<CharaGroup> = FixIndexMap<CharaGroup>(CharaGroup::class.java)
    val lvrs: FixIndexMap<LvRestrict> = FixIndexMap<LvRestrict>(LvRestrict::class.java)
    val musics: FixIndexMap<Music> = FixIndexMap<Music>(Music::class.java)
    override fun <R> getList(cls: Class<*>, func: Reductor<R, FixIndexMap<*>?>, def: R): R {
        var def = def
        if (cls == Unit::class.java) def = func.reduce(def, units)
        if (cls == Enemy::class.java || cls == AbEnemy::class.java) def = func.reduce(def, enemies)
        if (cls == EneRand::class.java || cls == AbEnemy::class.java) def = func.reduce(def, randEnemies)
        if (cls == Background::class.java) def = func.reduce(def, bgs)
        if (cls == Soul::class.java) def = func.reduce(def, souls)
        if (cls == Music::class.java) def = func.reduce(def, musics)
        if (cls == CharaGroup::class.java) def = func.reduce(def, groups)
        if (cls == LvRestrict::class.java) def = func.reduce(def, lvrs)
        return def
    }

    companion object {
        @ContGetter
        fun getPack(str: String?): PackData {
            return UserProfile.Companion.getPack(str)
        }
    }
}
