package common.battle.data

import common.io.InStream
import common.io.json.JsonClass
import common.io.json.JsonField
import common.util.Data
import common.util.unit.Form

@JsonClass
class CustomUnit : CustomEntity(), MaskUnit {
    var pack: Form? = null

    @JsonField
    var price: Int

    @JsonField
    var resp: Int
    fun fillData(ver: Int, `is`: InStream) {
        zread(ver, `is`)
    }

    override fun getBack(): Int {
        return 9
    }

    override fun getFront(): Int {
        return 0
    }

    override fun getOrb(): Orb? {
        return pack!!.orbs
    }

    override fun getPack(): Form? {
        return pack
    }

    override fun getPrice(): Int {
        return price
    }

    override fun getRespawn(): Int {
        return resp
    }

    override fun importData(de: MaskEntity) {
        super.importData(de)
        if (de is MaskUnit) {
            val mu: MaskUnit = de
            price = mu.getPrice()
            resp = mu.getRespawn()
        }
    }

    private fun zread(`val`: Int, `is`: InStream) {
        var `val` = `val`
        `val` = Data.Companion.getVer(`is`.nextString())
        if (`val` >= 400) `zread$000400`(`is`)
    }

    private fun `zread$000400`(`is`: InStream) {
        zreada(`is`)
        price = `is`.nextInt()
        resp = `is`.nextInt()
    }

    init {
        rep = AtkDataModel(this)
        atks = arrayOfNulls<AtkDataModel>(1)
        atks.get(0) = AtkDataModel(this)
        width = 320
        speed = 8
        hp = 1000
        hb = 1
        type = 0
        price = 50
        resp = 60
    }
}
