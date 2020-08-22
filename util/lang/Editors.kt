package common.util.lang

import common.pack.Context
import common.pack.Context.SupExc
import common.pack.UserProfile
import common.util.Data
import common.util.Data.Proc
import common.util.Data.Proc.*
import common.util.lang.LocaleCenter.Displayable
import common.util.lang.LocaleCenter.ObjBinder
import common.util.lang.LocaleCenter.ObjBinder.BinderFunc
import common.util.lang.ProcLang.ItemLang
import org.jcodec.common.tools.MathUtil
import java.lang.reflect.Field
import java.util.function.Consumer
import java.util.function.Supplier

object Editors {
    fun setEditorSupplier(sup: EditorSupplier?) {
        UserProfile.Companion.setStatic("Editor_Supplier", sup)
    }

    private fun getEditor(ctrl: EditControl<*>, g: EditorGroup, field: String, edit: Boolean): Editor? {
        val edi: EditorSupplier = UserProfile.Companion.getStatic<EditorSupplier>("Editor_Supplier", Supplier<EditorSupplier?> { null })
        return edi.getEditor(ctrl, g, field, edit)
    }

    private fun map(): MutableMap<String, EditControl<*>> {
        return UserProfile.Companion.getRegister<EditControl<*>>("Editor_EditControl", EditControl::class.java)
    }

    class DispItem(lang: ItemLang?, proc: Supplier<ProcItem?>, ctx: Formatter.Context) : Displayable {
        private val lang: ItemLang?
        private val proc: Supplier<ProcItem?>
        private val ctx: Formatter.Context
        override fun getName(): String? {
            return lang.full_name
        }

        override fun getTooltip(): String {
            var t0: String? = if (lang.tooltip == null) null else lang.tooltip
            val item: ProcItem? = proc.get()
            var t1: String? = if (item == null) null else Formatter.Companion.format(lang.format, item, ctx)
            if (t0 != null) t0 = "<p width = \"500\">$t0</p>"
            if (t1 != null) t1 = "<p width = \"500\">$t1</p>"
            return if (t0 == null && t1 == null) "" else if (t0 == null) "<html>$t1</html>" else if (t1 == null) "<html>$t0</html>" else "<html>$t0<hr>$t1</html>"
        }

        override fun setName(str: String) {
            lang.full_name = str
        }

        override fun setTooltip(str: String) {
            lang.tooltip = str
        }

        init {
            this.lang = lang
            this.proc = proc
            this.ctx = ctx
        }
    }

    class EdiField {
        private var f0: Field
        private var f1: Field?
        var obj: Any? = null

        constructor(f: Field) {
            f0 = f
            f1 = null
        }

        constructor(pri: Field, sec: Field) {
            f0 = sec
            f1 = pri
        }

        fun get(): Any {
            return Data.Companion.err<Any>(SupExc<Any> { f0[obj] })
        }

        fun getBoolean(): Boolean {
            return Data.Companion.err<Boolean>(SupExc<Boolean> { f0.getBoolean(obj) })
        }

        fun getInt(): Int {
            return Data.Companion.err<Int>(SupExc<Int> { f0.getInt(obj) })
        }

        fun getType(): Class<*> {
            return f0.type
        }

        fun set(data: Any?) {
            Data.Companion.err(Context.RunExc { f0[obj] = data })
        }

        fun setBoolean(data: Boolean) {
            Data.Companion.err(Context.RunExc { f0.setBoolean(obj, data) })
        }

        fun setData(obj: Any?) {
            this.obj = if (f1 == null) obj else if (obj == null) null else Data.Companion.err<Any>(SupExc<Any> { f1!![obj] })
        }

        fun setInt(data: Int) {
            Data.Companion.err(Context.RunExc { f0.setInt(obj, data) })
        }
    }

    class EditControl<T>(val cls: Class<T>, private val regulator: Consumer<T?>) {
        fun getField(f: String): EdiField {
            return Data.Companion.err<EdiField>(SupExc<EdiField> {
                if (f.contains(".")) {
                    val strs = f.split("\\.").toTypedArray()
                    val f0 = cls.getField(strs[0])
                    val f1 = f0.type.getField(strs[1])
                    return@err EdiField(f0, f1)
                } else return@err EdiField(cls.getField(f))
            })
        }

        fun update(par: EditorGroup) {
            regulate(par.obj as T?)
            par.setData(par.obj)
            if (par.callback != null) par.callback.run()
        }

        protected fun regulate(obj: T?) {
            regulator.accept(obj)
        }
    }

    abstract class Editor(val par: EditorGroup, val field: EdiField, f: String?) {
        /** notify that the data changed  */
        abstract fun setData()
        protected fun update() {
            par.ctrl.update(par)
        }
    }

    open class EditorGroup(// Proc Title
            val proc: String, edit: Boolean, cb: Runnable?) {
        val cls // ProcItem
                : Class<*>
        val list: Array<Editor?>
        val ctrl: EditControl<*>
        val callback: Runnable?
        var obj: Any? = null
        fun getItem(ctx: Formatter.Context): LocaleCenter.Binder {
            val lang: ItemLang = ProcLang.Companion.get()!!.get(proc)
            val disp: Displayable = DispItem(lang, Supplier<ProcItem?> { getProcItem() }, ctx)
            return ObjBinder(disp, proc, BinderFunc { name: String? -> getItem(ctx) })
        }

        open fun setData(obj: Any?) {
            this.obj = obj
            for (e in list) e!!.setData()
        }

        private fun getProcItem(): ProcItem? {
            return obj as ProcItem?
        }

        init {
            cls = Data.Companion.err<Field>(SupExc<Field> { Proc::class.java.getDeclaredField(proc) }).type
            callback = cb
            ctrl = map()[proc]!!
            val item: ItemLang = ProcLang.Companion.get()!!.get(proc)
            val arr: Array<String> = item.list()
            list = arrayOfNulls(arr.size)
            for (i in arr.indices) {
                list[i] = getEditor(ctrl, this, arr[i], edit)
            }
        }
    }

    interface EditorSupplier {
        fun getEditor(ctrl: EditControl<*>?, g: EditorGroup, field: String?, edit: Boolean): Editor?
    }

    init {
        val prob: EditControl<PROB> = EditControl<PROB>(PROB::class.java, Consumer<PROB> { t: PROB -> t.prob = MathUtil.clip(t.prob, 0, 100) })
        val pt: EditControl<PT> = EditControl<PT>(PT::class.java, Consumer<PT> { t: PT ->
            t.prob = MathUtil.clip(t.prob, 0, 100)
            if (t.prob == 0) t.time = 0 else t.time = Math.max(t.time, 1)
        })
        map()["KB"] = EditControl<PTD>(PTD::class.java, Consumer<PTD> { t: PTD? ->
            common.util.lang.t.prob = MathUtil.clip(common.util.lang.t.prob, 0, 100)
            if (common.util.lang.t.prob == 0) {
                common.util.lang.t.time = 0
                common.util.lang.t.dis = common.util.lang.t.time
            } else {
                if (common.util.lang.t.dis == 0) common.util.lang.t.dis = Data.Companion.KB_DIS.get(Data.Companion.INT_KB)
                if (common.util.lang.t.time <= 0) common.util.lang.t.time = Data.Companion.KB_TIME.get(Data.Companion.INT_KB)
            }
        })
        map()["STOP"] = common.util.lang.pt
        map()["SLOW"] = common.util.lang.pt
        map()["CRIT"] = EditControl<PM>(PM::class.java, Consumer<PM> { t: PM? ->
            common.util.lang.t.prob = MathUtil.clip(common.util.lang.t.prob, 0, 100)
            if (common.util.lang.t.prob == 0) common.util.lang.t.mult = 0 else if (common.util.lang.t.mult == 0) common.util.lang.t.mult = 200
        })
        map()["WAVE"] = EditControl<Wave>(Wave::class.java, Consumer<Wave> { t: Wave? ->
            common.util.lang.t.prob = MathUtil.clip(common.util.lang.t.prob, 0, 100)
            if (common.util.lang.t.prob == 0) common.util.lang.t.lv = 0
            common.util.lang.t.lv = MathUtil.clip(common.util.lang.t.lv, 1, 20)
        })
        map()["WEAK"] = EditControl<Weak>(Weak::class.java, Consumer<Weak> { t: Weak? ->
            common.util.lang.t.prob = MathUtil.clip(common.util.lang.t.prob, 0, 100)
            if (common.util.lang.t.prob == 0) {
                common.util.lang.t.time = 0
                common.util.lang.t.mult = common.util.lang.t.time
            } else {
                common.util.lang.t.time = Math.max(common.util.lang.t.time, 1)
            }
        })
        map()["BREAK"] = common.util.lang.prob
        map()["WARP"] = EditControl<PTD>(PTD::class.java, Consumer<PTD> { t: PTD? ->
            common.util.lang.t.prob = MathUtil.clip(common.util.lang.t.prob, 0, 100)
            if (common.util.lang.t.prob == 0) {
                common.util.lang.t.time = 0
                common.util.lang.t.dis = common.util.lang.t.time
            }
        })
        map()["CURSE"] = common.util.lang.pt
        map()["STRONG"] = EditControl<Strong>(Strong::class.java, Consumer<Strong> { t: Strong? ->
            common.util.lang.t.health = MathUtil.clip(common.util.lang.t.health, 0, 99)
            if (common.util.lang.t.health == 0) common.util.lang.t.mult = 0
        })
        map()["LETHAL"] = common.util.lang.prob
        map()["BURROW"] = EditControl<Burrow>(Burrow::class.java, Consumer<Burrow> { t: Burrow? ->
            common.util.lang.t.count = Math.max(common.util.lang.t.count, -1)
            if (common.util.lang.t.count == 0) common.util.lang.t.dis = 0 else common.util.lang.t.dis = Math.max(common.util.lang.t.dis, 1)
        })
        map()["REVIVE"] = EditControl<Revive>(Revive::class.java, Consumer<Revive> { t: Revive? ->
            common.util.lang.t.count = Math.max(common.util.lang.t.count, -1)
            if (common.util.lang.t.count == 0) {
                common.util.lang.t.health = 0
                common.util.lang.t.time = 0
                common.util.lang.t.type.imu_zkill = false
                common.util.lang.t.type.revive_others = false
                common.util.lang.t.dis_1 = 0
                common.util.lang.t.dis_0 = common.util.lang.t.dis_1
                common.util.lang.t.type.range_type = 0
                common.util.lang.t.type.revive_non_zombie = false
            } else {
                common.util.lang.t.health = MathUtil.clip(common.util.lang.t.health, 1, 100)
                common.util.lang.t.time = Math.max(common.util.lang.t.time, 1)
                if (!common.util.lang.t.type.revive_others) {
                    common.util.lang.t.dis_1 = 0
                    common.util.lang.t.dis_0 = common.util.lang.t.dis_1
                    common.util.lang.t.type.range_type = 0
                    common.util.lang.t.type.revive_non_zombie = false
                } else {
                    common.util.lang.t.type.range_type = MathUtil.clip(common.util.lang.t.type.range_type, 0, 3)
                }
            }
        })
        map()["SNIPER"] = common.util.lang.prob
        map()["TIME"] = common.util.lang.pt
        map()["SEAL"] = common.util.lang.pt
        map()["SUMMON"] = EditControl<Summon>(Summon::class.java, Consumer<Summon> { t: Summon? ->
            common.util.lang.t.prob = MathUtil.clip(common.util.lang.t.prob, 0, 100)
            if (common.util.lang.t.prob == 0) {
                common.util.lang.t.dis = 0
                common.util.lang.t.id = null
                common.util.lang.t.mult = 0
                common.util.lang.t.time = 0
                common.util.lang.t.type.anim_type = 0
                common.util.lang.t.type.fix_buff = false
                common.util.lang.t.type.ignore_limit = false
                common.util.lang.t.type.on_hit = false
                common.util.lang.t.type.on_kill = false
                common.util.lang.t.type.random_layer = false
                common.util.lang.t.type.same_health = false
            } else {
                common.util.lang.t.mult = Math.max(1, common.util.lang.t.mult)
                common.util.lang.t.time = Math.max(0, common.util.lang.t.time)
                common.util.lang.t.type.anim_type = MathUtil.clip(common.util.lang.t.type.anim_type, 0, 3)
            }
        })
        map()["MOVEWAVE"] = EditControl<MoveWave>(MoveWave::class.java, Consumer<MoveWave> { t: MoveWave? ->
            common.util.lang.t.prob = MathUtil.clip(common.util.lang.t.prob, 0, 100)
            if (common.util.lang.t.prob == 0) {
                common.util.lang.t.dis = 0
                common.util.lang.t.itv = 0
                common.util.lang.t.speed = 0
                common.util.lang.t.time = 0
                common.util.lang.t.width = 0
            } else {
                common.util.lang.t.width = Math.max(0, common.util.lang.t.width)
                common.util.lang.t.time = Math.max(1, common.util.lang.t.time)
                common.util.lang.t.itv = Math.max(1, common.util.lang.t.itv)
            }
        })
        map()["THEME"] = EditControl<Theme>(Theme::class.java, Consumer<Theme> { t: Theme? -> common.util.lang.t.prob = MathUtil.clip(common.util.lang.t.prob, 0, 100) })
        map()["POISON"] = EditControl<Poison>(Poison::class.java, Consumer<Poison> { t: Poison? ->
            common.util.lang.t.prob = MathUtil.clip(common.util.lang.t.prob, 0, 100)
            if (common.util.lang.t.prob == 0) {
                common.util.lang.t.damage = 0
                common.util.lang.t.itv = 0
                common.util.lang.t.time = 0
                common.util.lang.t.type.damage_type = 0
                common.util.lang.t.type.unstackable = false
            } else {
                common.util.lang.t.time = Math.max(1, common.util.lang.t.time)
                common.util.lang.t.itv = Math.max(1, common.util.lang.t.itv)
                common.util.lang.t.type.damage_type = MathUtil.clip(common.util.lang.t.type.damage_type, 0, 3)
            }
        })
        map()["BOSS"] = common.util.lang.prob
        map()["CRITI"] = EditControl<CritI>(CritI::class.java, Consumer<CritI> { t: CritI? -> common.util.lang.t.type = MathUtil.clip(0, common.util.lang.t.type, 2) })
        map()["SATK"] = EditControl<PM>(PM::class.java, Consumer<PM> { t: PM? -> if (common.util.lang.t.prob == 0) common.util.lang.t.mult = 0 })
        map()["IMUATK"] = common.util.lang.pt
        map()["POIATK"] = EditControl<PM>(PM::class.java, Consumer<PM> { t: PM? -> if (common.util.lang.t.prob == 0) common.util.lang.t.mult = 0 })
        map()["VOLC"] = EditControl<Volc>(Volc::class.java, Consumer<Volc> { t: Volc? ->
            common.util.lang.t.prob = MathUtil.clip(common.util.lang.t.prob, 0, 100)
            if (common.util.lang.t.prob == 0) {
                common.util.lang.t.dis_1 = 0
                common.util.lang.t.dis_0 = common.util.lang.t.dis_1
                common.util.lang.t.time = 0
            } else {
                common.util.lang.t.time = Math.max(1, common.util.lang.t.time / Data.Companion.VOLC_ITV) * Data.Companion.VOLC_ITV
            }
        })
        map()["ARMOR"] = EditControl<Armor>(Armor::class.java, Consumer<Armor> { t: Armor? ->
            common.util.lang.t.prob = MathUtil.clip(common.util.lang.t.prob, 0, 100)
            if (common.util.lang.t.prob == 0) {
                common.util.lang.t.time = 0
                common.util.lang.t.mult = common.util.lang.t.time
            } else {
                common.util.lang.t.time = Math.max(1, common.util.lang.t.time)
            }
        })
        map()["SPEED"] = EditControl<Speed>(Speed::class.java, Consumer<Speed> { t: Speed? ->
            common.util.lang.t.prob = MathUtil.clip(common.util.lang.t.prob, 0, 100)
            if (common.util.lang.t.prob == 0) {
                common.util.lang.t.time = 0
                common.util.lang.t.speed = common.util.lang.t.time
                common.util.lang.t.type = 0
            } else {
                common.util.lang.t.time = Math.max(1, common.util.lang.t.time)
                common.util.lang.t.type = MathUtil.clip(common.util.lang.t.type, 0, 2)
            }
        })
    }
}
