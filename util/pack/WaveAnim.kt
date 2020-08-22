package common.util.pack

import common.system.fake.FakeImage
import common.util.anim.AnimI
import common.util.anim.EAnimD
import common.util.anim.MaAnim
import common.util.anim.MaModel
import common.util.pack.WaveAnim.WaveType

class WaveAnim(private val bg: Background, model: MaModel, anim: MaAnim) : AnimI<WaveAnim?, WaveType?>() {
    enum class WaveType : AnimType<WaveAnim?, WaveType?> {
        DEF
    }

    private val mamodel: MaModel
    private val maanim: MaAnim
    private var parts: Array<FakeImage>?
    override fun check() {
        if (parts == null) load()
    }

    override fun getEAnim(t: WaveType): EAnimD<WaveType>? {
        return EAnimD<WaveType>(this, mamodel, maanim)
    }

    override fun load() {
        bg.check()
        parts = bg.parts
    }

    override fun names(): Array<String?> {
        return AnimI.Companion.translate(WaveType.DEF)
    }

    override fun parts(i: Int): FakeImage? {
        check()
        return parts!![i]
    }

    override fun types(): Array<WaveType> {
        return common.util.pack.WaveAnim.WaveType.values()
    }

    init {
        mamodel = model
        maanim = anim
    }
}
