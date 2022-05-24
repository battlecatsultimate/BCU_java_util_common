package common.util.pack.bgeffect;

import common.system.BattleRange;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.util.anim.EAnimD;

public class BGEffectSpacer {
    public final int pos0, pos1, value;
    public final BattleRange.SNAP snap;

    public BGEffectSpacer(int pos0, int pos1, int value, BattleRange.SNAP snap) {
        this.pos0 = pos0;
        this.pos1 = pos1;
        this.value = value;
        this.snap = snap;
    }

    public void drawWithSpacer(FakeGraphics g, P rect, double siz, EAnimD<BGEffectAnim.BGEffType> anim) {
        // TODO implement segment drawing with spacer
    }

    /**
     * Convert battle unit to pixel unit
     * @param p Position in battle
     * @param siz Size of battle
     * @return Converted pixel
     */
    private int convertP(double p, double siz) {
        return (int) (p * BattleRange.battleRatio * siz);
    }
}
