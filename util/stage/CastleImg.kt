package common.util.stage

import common.pack.IndexContainer.IndexCont
import common.pack.IndexContainer.Indexable
import common.pack.PackData
import common.system.VImg
import common.util.stage.CastleList

@IndexCont(CastleList::class)
class CastleImg(val id: PackData.Identifier<CastleImg>, img: VImg) : Indexable<CastleList?, CastleImg?> {
    val img: VImg
    override fun getID(): PackData.Identifier<CastleImg>? {
        return id
    }

    init {
        this.img = img
    }
}
