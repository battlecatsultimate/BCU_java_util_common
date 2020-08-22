package common.util.stage

import common.pack.IndexContainer.IndexCont
import common.pack.IndexContainer.Indexable
import common.pack.PackData
import common.pack.PackDataimport
import common.system.files.FileData

@IndexCont(PackData::class)
class Music(val id: PackData.Identifier<Music>, val data: FileData) : Indexable<PackData?, Music?> {
    override fun getID(): PackData.Identifier<Music>? {
        return id
    }

    override fun toString(): String {
        return id.toString()
    }
}
