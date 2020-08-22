package common.util.stage

import common.CommonStatic
import common.io.json.JsonClass
import common.io.json.JsonField
import common.system.BasedCopable
import common.system.files.FileData
import common.util.Dataimport
import common.util.lang.MultiLangCont
import common.util.stage.Stage.StageInfo
import java.util.*

com.google.api.client.json.jackson2.JacksonFactory
@JsonClass
class StageMap : Data, BasedCopable<StageMap?, MapColc?> {
    class StageMapInfo(val sm: StageMap, ad: FileData) {
        private val qs: Queue<String>
        var rand = 0
        var time = 0
        var lim = 0
        fun getData(s: Stage) {
            val ints: IntArray = CommonStatic.parseIntsN(qs.poll().split("//").toTypedArray()[0])
            if (ints.size <= 4) return
            s.info = StageInfo(this, s, ints)
        }

        init {
            qs = ad.readLine()
            val ints: IntArray = CommonStatic.parseIntsN(qs.poll().split("//").toTypedArray()[0])
            if (ints.size > 3) {
                rand = ints[1]
                time = ints[2]
                lim = ints[3]
            }
            qs.poll()
        }
    }

    val mc: MapColc
    val lim: MutableList<Limit> = ArrayList()
    var info: StageMapInfo? = null

    @JsonField(generic = [Stage::class])
    val list: MutableList<Stage> = ArrayList()

    @JsonField
    var name = ""

    @JsonField
    var id = 0

    @JsonField
    var price = 1

    @JsonField
    var retyp = 0

    @JsonField
    var pllim = 0

    @JsonField
    var set = 0

    @JsonField
    var cast = -1

    @JsonField
    var stars = intArrayOf(100)

    constructor(map: MapColc) {
        mc = map
        name = "new stage map"
    }

    constructor(map: MapColc, ID: Int, m: FileData) {
        info = StageMapInfo(this, m)
        mc = map
        id = ID
    }

    constructor(map: MapColc, ID: Int, stn: FileData, cas: Int) : this(map, ID, stn) {
        cast = cas
    }

    fun add(s: Stage?) {
        if (s == null) return
        list.add(s)
    }

    override fun copy(mc: MapColc): StageMap {
        val sm = StageMap(mc)
        sm.name = name
        if (name.length == 0) sm.name = toString()
        sm.stars = stars.clone()
        for (st in list) sm.add(st.copy(sm))
        return sm
    }

    override fun toString(): String {
        val desp: String = MultiLangCont.Companion.get(this)
        if (desp != null && desp.length > 0) return desp
        return if (name.length == 0) mc.toString() + " - " + id + " (" + list.size + ")" else name
    }
}
