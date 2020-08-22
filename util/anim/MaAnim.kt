package common.util.anim

import common.CommonStatic
import common.io.InStream
import common.io.OutStream
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
import common.system.files.FileData
import common.util.BattleStatic
import common.util.Data
import common.util.stage.EStage
import common.util.stage.StageMap
import common.util.stage.StageMap.StageMapInfo
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
import java.io.PrintStream
import java.util.*
import java.util.function.Function

class MaAnim : Data, BattleStatic {
    var n: Int
    var parts: Array<Part?>
    var max = 1
    var len = 1

    constructor() {
        n = 0
        parts = arrayOfNulls(0)
    }

    constructor(qs: Queue<String>) {
        qs.poll()
        qs.poll()
        n = qs.poll().trim { it <= ' ' }.toInt()
        parts = arrayOfNulls(n)
        for (i in 0 until n) parts[i] = Part(qs)
        validate()
    }

    private constructor(ma: MaAnim) {
        n = ma.n
        parts = arrayOfNulls(n)
        for (i in 0 until n) parts[i] = ma.parts[i]!!.clone()
        validate()
    }

    override fun clone(): MaAnim {
        return MaAnim(this)
    }

    fun revert() {
        for (p in parts) if (p!!.ints[1] == 11) for (move in p.moves) move[1] *= -1
    }

    fun validate() {
        max = 1
        for (i in 0 until n) if (parts[i]!!.getMax() > max) max = parts[i]!!.getMax()
        len = 1
        for (i in 0 until n) if (parts[i]!!.getMax() - parts[i]!!.off > len) len = parts[i]!!.getMax() - parts[i]!!.off
    }

    fun write(ps: PrintStream) {
        ps.println("[maanim]")
        ps.println("1")
        ps.println(parts.size)
        for (p in parts) p.write(ps)
    }

    fun restore(`is`: InStream) {
        n = `is`.nextInt()
        parts = arrayOfNulls(n)
        for (i in 0 until n) {
            parts[i] = Part()
            parts[i]!!.restore(`is`)
        }
        validate()
    }

    fun update(f: Int, eAnim: EAnimD<*>, rotate: Boolean) {
        var f = f
        if (rotate) f %= max
        if (f == 0 || rotate && f % max == 0) {
            for (e in eAnim.ent) e.setValue()
        }
        for (i in 0 until n) {
            val loop = parts[i]!!.ints[2]
            val smax = parts[i]!!.max
            val fir = parts[i]!!.fir
            val lmax = smax - fir
            val prot = rotate || loop == -1
            var frame = 0
            if (prot) {
                val mf = if (loop == -1) smax else max
                frame = if (mf == 0) 0 else (f + parts[i]!!.off) % mf
                if (loop > 0 && lmax != 0) {
                    if (frame > fir + loop * lmax) {
                        parts[i]!!.ensureLast(eAnim.ent)
                        continue
                    }
                    if (frame <= fir) ; else if (frame < fir + loop * lmax) frame = fir + (frame - fir) % lmax else frame = smax
                }
            } else {
                frame = f + parts[i]!!.off
                if (loop > 0 && lmax != 0) {
                    if (frame > fir + loop * lmax) {
                        parts[i]!!.ensureLast(eAnim.ent)
                        continue
                    }
                    if (frame <= fir) ; else if (frame < fir + loop * lmax) frame = fir + (frame - fir) % lmax else frame = smax
                }
            }
            parts[i]!!.update(frame, eAnim.ent)
        }
        eAnim.sort()
    }

    fun write(os: OutStream) {
        os.writeInt(n)
        for (p in parts) p.write(os)
    }

    companion object {
        fun newIns(f: FileData): MaAnim {
            return try {
                MaAnim(f.readLine())
            } catch (e: Exception) {
                e.printStackTrace()
                MaAnim()
            }
        }

        fun newIns(str: String?): MaAnim {
            return CommonStatic.def.readSave<MaAnim>(str, Function { f: Queue<String>? ->
                f?.let { MaAnim(it) } ?: MaAnim()
            })
        }
    }
}
