package common.battle.data

import common.CommonStatic
import common.CommonStatic.BCAuxAssets
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonEncoder
import common.io.json.Test
import common.io.json.Test.JsonTest_0.JsonD
import common.io.json.Test.JsonTest_2
import common.pack.PackData
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.system.VImg
import common.system.files.VFile
import common.util.Data
import common.util.anim.ImgCut
import common.util.stage.EStage
import common.util.stage.StageMap
import common.util.stage.StageMap.StageMapInfo
import common.util.unit.Unit
import common.util.unit.UnitLevel
import io.BCPlayer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import page.JL
import page.anim.AnimBox
import page.support.ListJtfPolicy
import page.support.SortTable
import page.view.ViewBox
import page.view.ViewBox.Conf
import page.view.ViewBox.Controller
import page.view.ViewBox.VBExporter
import java.nio.charset.StandardCharsets
import java.util.*

class Orb(private val slots: Int) : Data() {
    fun getAtk(grade: Int, atk: MaskAtk): Int {
        return Data.Companion.ORB_ATK_MULTI.get(grade) * atk.getAtk() / 100
    }

    fun getRes(grade: Int, atk: MaskAtk): Int {
        return -Data.Companion.ORB_RES_MULTI.get(grade) * atk.getAtk() / 100
    }

    fun getSlots(): Int {
        return slots
    }

    companion object {
        fun getTrait(trait: Int): Int {
            return when (trait) {
                0 -> Data.Companion.TB_RED
                1 -> Data.Companion.TB_FLOAT
                2 -> Data.Companion.TB_BLACK
                3 -> Data.Companion.TB_METAL
                4 -> Data.Companion.TB_ANGEL
                5 -> Data.Companion.TB_ALIEN
                6 -> Data.Companion.TB_ZOMBIE
                7 -> Data.Companion.TB_RELIC
                8 -> Data.Companion.TB_WHITE
                else -> 0
            }
        }

        fun read() {
            val aux: BCAuxAssets = CommonStatic.getBCAssets()
            try {
                val traitData: Queue<String> = VFile.Companion.readLine("./org/data/equipment_attribute.csv")
                var key = 0
                for (line in traitData) {
                    if (line == null || line.startsWith("//") || line.isEmpty()) continue
                    val strs = line.trim { it <= ' ' }.split(",").toTypedArray()
                    var value = 0
                    for (i in strs.indices) {
                        val t: Int = CommonStatic.parseIntN(strs[i])
                        if (t == 1) value = value or getTrait(i)
                    }
                    aux.DATA.put(key, value)
                    key++
                }
                val data = String(VFile.Companion.get("./org/data/equipmentlist.json").getData().getBytes(),
                        StandardCharsets.UTF_8)
                val jdata = JSONObject(data)
                val lists: JSONArray = jdata.getJSONArray("ID")
                for (i in 0 until lists.length()) {
                    if (lists.get(i) !is JSONObject) {
                        continue
                    }
                    val obj: JSONObject = lists.get(i) as JSONObject
                    val trait: Int = obj.getInt("attribute")
                    val type: Int = obj.getInt("content")
                    val grade: Int = obj.getInt("gradeID")
                    if (type == Data.Companion.ORB_ATK) {
                        if (aux.ATKORB.get(aux.DATA.get(trait)) == null) {
                            val grades: MutableList<Int> = ArrayList()
                            grades.add(grade)
                            aux.ATKORB.put(aux.DATA.get(trait), grades)
                        } else {
                            val grades: MutableList<Int> = aux.ATKORB.get(aux.DATA.get(trait))
                            if (grades != null && !grades.contains(grade)) {
                                grades.add(grade)
                            }
                            aux.ATKORB.put(aux.DATA.get(trait), grades)
                        }
                    } else {
                        if (aux.RESORB.get(aux.DATA.get(trait)) == null) {
                            val grades: MutableList<Int> = ArrayList()
                            grades.add(grade)
                            aux.RESORB.put(aux.DATA.get(trait), grades)
                        } else {
                            val grades: MutableList<Int> = aux.RESORB.get(aux.DATA.get(trait))
                            if (grades != null && !grades.contains(grade)) {
                                grades.add(grade)
                            }
                            aux.RESORB.put(aux.DATA.get(trait), grades)
                        }
                    }
                }
                val units: Queue<String> = VFile.Companion.readLine("./org/data/equipmentslot.csv")
                for (line in units) {
                    if (line == null || line.startsWith("//") || line.isEmpty()) {
                        continue
                    }
                    val strs = line.trim { it <= ' ' }.split(",").toTypedArray()
                    if (strs.size != 2) {
                        continue
                    }
                    val id: Int = CommonStatic.parseIntN(strs[0])
                    val slots: Int = CommonStatic.parseIntN(strs[1])
                    val u: Unit = PackData.Identifier.Companion.parseInt<Unit>(id, Unit::class.java).get()
                    if (u == null || u.forms.size != 3) {
                        continue
                    }
                    val f = u.forms[2] ?: continue
                    f.orbs = Orb(slots)
                }
                val pre = "./org/page/orb/equipment_"
                val type = VImg(pre + "effect.png")
                val it: ImgCut = ImgCut.Companion.newIns(pre + "effect.imgcut")
                aux.TYPES = it.cut(type.getImg())
                val trait = VImg(pre + "attribute.png")
                val itr: ImgCut = ImgCut.Companion.newIns(pre + "attribute.imgcut")
                aux.TRAITS = itr.cut(trait.getImg())
                val grade = VImg(pre + "grade.png")
                val ig: ImgCut = ImgCut.Companion.newIns(pre + "grade.imgcut")
                aux.GRADES = ig.cut(grade.getImg())
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        fun reverse(value: Int): Int {
            val DATA: Map<Int, Int?> = CommonStatic.getBCAssets().DATA
            for (n in DATA.keys) {
                val v = DATA[n]!!
                if (DATA[n] != null && v == value) return n
            }
            return -1
        }
    }
}
