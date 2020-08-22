package common.pack

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import common.CommonStatic
import common.io.PackLoader
import common.io.PackLoader.Preload
import common.io.PackLoader.ZipDesc
import common.io.PackLoader.ZipDesc.FileDesc
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.json.JsonDecoder
import common.pack.Context
import common.pack.Context.ErrType
import common.pack.Context.SupExc
import common.pack.FixIndexList.FixIndexMap
import common.pack.IndexContainer.Reductor
import common.pack.PackData
import common.pack.PackData.DefPack
import common.pack.PackData.PackDesc
import common.pack.PackData.UserPack
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.pack.UserProfile
import common.pack.VerFixer
import java.io.File
import java.io.FileReader
import java.io.InputStreamReader
import java.io.Reader
import java.util.*
import java.util.function.Predicate
import java.util.function.Supplier
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.get
import kotlin.collections.set

class UserProfile private constructor() {

    val def: DefPack = DefPack()
    val packmap: MutableMap<String, UserPack> = HashMap<String, UserPack>()
    val packlist: MutableSet<UserPack> = HashSet<UserPack>()
    val failed: MutableSet<UserPack> = HashSet<UserPack>()
    private val registers: MutableMap<String, Map<String, *>> = HashMap()
    private val pending: Map<String, UserPack> = HashMap<String, UserPack>()

    /**
     * return true if the pack is attempted to load and should be removed from the
     * loading queue
     */
    private fun add(pack: UserPack): Boolean {
        packlist.add(pack)
        if (!canAdd(pack)) return false
        if (!CommonStatic.ctx!!.noticeErr({ pack.load() }, ErrType.WARN, "failed to load pack " + pack.desc)) {
            failed.add(pack)
            return true
        }
        packmap[pack.desc.id!!] = pack
        return true
    }

    private fun canAdd(s: UserPack): Boolean {
        for (dep in s.desc.dependency!!) if (!packmap.containsKey(dep)) return false
        return true
    }

    companion object {
        private const val REG_POOL = "_pools"
        private const val REG_STATIC = "_statics"

        // TODO load username and password
        @StaticPermitted(Admin.StaticPermitted.Type.ENV)
        private var profile: UserProfile? = null
        fun canRemove(id: String?): Boolean {
            for ((_, value) in profile().packmap.entries) if (value.desc.dependency!!.contains(id)) return false
            return true
        }

        /** get all available items for a pack, except castle  */
        fun <T> getAll(pack: String?, cls: Class<T>): List<T> {
            val list: MutableList<PackData> = ArrayList<PackData>()
            list.add(profile().def)
            if (pack != null) {
                val userpack: UserPack = profile().packmap[pack] as UserPack
                list.add(userpack)
                for (dep in userpack.desc.dependency!!) list.add(getPack(dep))
            }
            val ans: MutableList<*> = ArrayList<Any>()
            for (data in list)
                data.getList<Any?>(cls, { _:Any?, l: FixIndexMap<*> -> ans.addAll(l.getList()) }, null)
            return ans
        }

        /** get all packs, including default pack  */
        fun getAllPacks(): Collection<PackData> {
            val ans: MutableList<PackData> = ArrayList<PackData>()
            ans.add(getBCData())
            ans.addAll(getUserPacks())
            return ans
        }

        fun getBCData(): DefPack {
            return profile().def
        }

        /** get a PackData from a String  */
        fun getPack(str: String): PackData? {
            return if (str == PackData.Identifier.DEF) profile().def else profile().packmap[str]
        }

        /** get a set registered in the Registrar  */
        fun <T> getPool(id: String, cls: Class<T>): MutableSet<T> {
            val pool: MutableMap<String, MutableSet<*>> = getRegister(REG_POOL, MutableSet::class.java)
            return (pool[id] ?: HashSet<T>().also { pool[id] = it }) as MutableSet<T>
        }

        /** get a map registered in the Registrar  */
        fun <T> getRegister(id: String, cls: Class<T>): MutableMap<String, T> {
            var ans: Map<String, *>? = UserProfile.profile().registers[id]
            if (ans == null) UserProfile.Companion.profile().registers[id] = HashMap<String, T>().also { ans = it }
            return ans as MutableMap<String, T>
        }

        /** get a variable registered in the Registrar  */
        fun <T> getStatic(id: String, def: Supplier<T>): T {
            val pool: MutableMap<String, Any> = UserProfile.Companion.getRegister<Any>(UserProfile.Companion.REG_STATIC, Any::class.java)
            var ans = pool[id] as T?
            if (ans == null) pool[id] = def.get().also { ans = it }
            return ans
        }

        /** get a UserPack from a String  */
        fun getUserPack(id: String): UserPack {
            return UserProfile.Companion.profile().packmap.get(id)
        }

        /** get all UserPack  */
        fun getUserPacks(): Collection<UserPack> {
            return UserProfile.Companion.profile().packmap.values
        }

        @Throws(Exception::class)
        fun initJsonPack(id: String): UserPack? {
            val f: File = CommonStatic.ctx!!.getWorkspaceFile("./$id/pack.json")
            val folder = f.parentFile
            if (folder.exists()) {
                if (!CommonStatic.ctx!!.confirmDelete()) return null
                Context.Companion.delete(f)
            }
            folder.mkdirs()
            Context.Companion.check(f.createNewFile(), "create", f)
            val p = UserPack(id)
            UserProfile.Companion.profile().packmap[id] = p
            return p
        }

        fun loadPacks() {
            CommonStatic.ctx.noticeErr(Context.RunExc { VerFixer.Companion.fix() }, ErrType.FATAL, "failed to convert old format")
            val packs: File = CommonStatic.ctx.getPackFolder()
            val workspace: File = CommonStatic.ctx.getWorkspaceFile(".")
            val profile: UserProfile = UserProfile.Companion.profile()
            if (packs.exists()) for (f in packs.listFiles()) if (f.name.endsWith(".pack.bcuzip")) {
                val pack: UserPack = CommonStatic.ctx.noticeErr<UserPack>(SupExc<UserPack> { UserProfile.Companion.readZipPack(f) }, ErrType.WARN,
                        "failed to load external pack $f")
                if (pack != null) profile.pending[pack.desc.id] = pack
            }
            if (workspace.exists()) for (f in packs.listFiles()) if (f.isDirectory) {
                val main: File = CommonStatic.ctx.getWorkspaceFile("./" + f.name + "/main.pack.json")
                if (!main.exists()) continue
                val pack: UserPack = CommonStatic.ctx.noticeErr<UserPack>(SupExc<UserPack> { UserProfile.Companion.readJsonPack(main) }, ErrType.WARN,
                        "failed to load workspace pack $f")
                if (pack != null) profile.pending[pack.desc.id] = pack
            }
            val queue: MutableSet<UserPack> = HashSet<UserPack>(profile.pending.values)
            while (queue.removeIf(Predicate<UserPack> { pack: UserPack? -> profile.add(pack) }));
            profile.pending = null
            profile.packlist.addAll(profile.failed)
        }

        fun profile(): UserProfile {
            if (profile == null) {
                profile = UserProfile()
                CommonStatic.ctx?.initProfile()
            }
            return profile!!
        }

        @Throws(Exception::class)
        fun readJsonPack(f: File): UserPack {
            val folder = f.parentFile
            val r: Reader = FileReader(f)
            val elem: JsonElement = JsonParser.parseReader(r)
            r.close()
            val desc: PackDesc = JsonDecoder.Companion.decode<PackDesc>(elem.asJsonObject.get("desc"), PackDesc::class.java)
            return UserPack(Workspace(folder.name), desc, elem)
        }

        @Throws(Exception::class)
        fun readZipPack(f: File?): UserPack {
            val zip: ZipDesc = PackLoader.readPack(Preload { desc: FileDesc? -> CommonStatic.ctx.preload(desc) }, f)
            val r: Reader = InputStreamReader(zip.readFile("./main.pack.json"))
            val elem: JsonElement = JsonParser.parseReader(r)
            val data = UserPack(ZipSource(zip), zip.desc, elem)
            r.close()
            return data
        }

        fun remove(pack: UserPack) {
            UserProfile.Companion.profile().packmap.remove(pack.desc.id)
            UserProfile.Companion.profile().packlist.remove(pack)
        }

        fun setStatic(id: String?, `val`: Any?) {
            UserProfile.Companion.getRegister<Any>(UserProfile.Companion.REG_STATIC, Any::class.java).put(id, `val`)
        }
    }
}
