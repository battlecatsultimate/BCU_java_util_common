package common.system

import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonEncoder
import common.io.json.Test
import common.io.json.Test.JsonTest_0.JsonD
import common.io.json.Test.JsonTest_2
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.util.stage.EStage
import common.util.stage.StageMap
import common.util.stage.StageMap.StageMapInfo
import common.util.unit.Form
import common.util.unit.Unit
import common.util.unit.UnitLevel
import io.BCPlayer
import page.JL
import page.anim.AnimBox
import page.support.ListJtfPolicy
import page.support.SortTable
import page.view.ViewBox
import page.view.ViewBox.Conf
import page.view.ViewBox.Controller
import page.view.ViewBox.VBExporter
import java.util.*

class Node<T>(val `val`: T) {
    var prev: Node<T>? = null
    var next: Node<T>? = null
    private var side: Node<T>? = null
    private var end: Node<T>? = null
    fun add(n: Node<T>): Node<T> {
        if (next != null) next.prev = n
        n.next = next
        next = n
        n.prev = this
        return n
    }

    fun adds() {
        adds(side, end)
    }

    fun len(): Int {
        return if (next == null) 1 else next.len() + 1
    }

    fun removes() {
        side!!.removes(end)
    }

    fun side(e: Node<T>?): Node<T> {
        side = next
        end = e
        side!!.removes(end)
        return this
    }

    fun sides(): List<T> {
        val ans: MutableList<T> = ArrayList()
        var i = side
        while (i != null) {
            ans.add(i.`val`)
            i = if (i === end) null else i.next
        }
        return ans
    }

    private fun adds(p: Node<T>?, e: Node<T>?) {
        if (next != null) next.prev = e
        e!!.next = next
        next = p
        p!!.prev = this
    }

    private fun removes(e: Node<T>) {
        if (prev != null) prev.next = e.next
        if (next != null) next.prev = prev
        prev = null
        e.next = null
    }

    companion object {
        fun deRep(list: List<Form>): List<Form> {
            val ans: MutableList<Form> = ArrayList()
            for (f in list) if (ans.size > 0) {
                val last = ans[ans.size - 1]
                if (f.unit === last.unit) {
                    if (f.fid > last.fid) {
                        ans.remove(last)
                        ans.add(f)
                    }
                } else ans.add(f)
            } else ans.add(f)
            return ans
        }

        fun <T> getList(l: Collection<T>, n: T): Node<T>? {
            var ans: Node<T>? = null
            var ret: Node<T>? = null
            for (v in l) {
                val temp = Node(v)
                ans?.add(temp)
                if (v === n) ret = temp
                ans = temp
            }
            return ret
        }

        fun getList(list: List<Form>, unit: Form): Node<Unit>? {
            var ans: Node<Unit>? = null
            var ret: Node<Unit>? = null
            for (v in list) {
                if (ans != null && ans.`val` === v.unit) continue
                val temp = Node(v.unit)
                ans?.add(temp)
                if (v.unit === unit.unit) ret = temp
                ans = temp
            }
            return ret
        }
    }
}
