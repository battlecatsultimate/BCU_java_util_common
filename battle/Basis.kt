package common.battle

import common.io.json.JsonClass
import common.io.json.JsonClass.RType
import common.io.json.JsonField
import common.util.Data

@JsonClass(read = RType.FILL)
abstract class Basis : Data() {
    @JsonField
    var name: String? = null

    /** get combo effect data  */
    abstract fun getInc(type: Int): Int

    /** get Treasure object  */
    abstract fun t(): Treasure
    override fun toString(): String {
        return name!!
    }
}
