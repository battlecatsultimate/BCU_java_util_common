package common.battle.attack

import common.battle.StageBasis
import common.system.P
import common.system.fake.FakeGraphics
import common.util.BattleObj

abstract class ContAb protected constructor(b: StageBasis, p: Double, lay: Int) : BattleObj() {
    protected val sb: StageBasis
    var pos: Double
    var activate = true
    var layer: Int
    abstract fun draw(gra: FakeGraphics, p: P, psiz: Double)
    abstract fun update()

    init {
        sb = b
        pos = p
        layer = lay
        sb.tlw.add(this)
    }
}
