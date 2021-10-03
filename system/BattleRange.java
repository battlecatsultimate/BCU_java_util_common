package common.system;

import common.battle.StageBasis;
import common.util.pack.bgeffect.BackgroundEffect;

import javax.annotation.Nullable;

public class BattleRange<T extends Number> {
    public enum SNAP {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
        INTERVAL,
        FRONT,
        BACK
    }

    private final T min;
    private final T max;
    private final SNAP minSnap, maxSnap;

    public BattleRange(T min, @Nullable SNAP minSnap, T max, @Nullable SNAP maxSnap) {
        this.min = min;
        this.max = max;
        this.minSnap = minSnap;
        this.maxSnap = maxSnap;

        if(
            (isXAxis(minSnap) && isYAxis(maxSnap)) ||
            (isXAxis(minSnap) && isZAxis(maxSnap)) ||
            (isYAxis(minSnap) && isXAxis(maxSnap)) ||
            (isYAxis(minSnap) && isZAxis(maxSnap)) ||
            (isZAxis(minSnap) && isXAxis(maxSnap)) ||
            (isZAxis(minSnap) && isYAxis(maxSnap))
        ) {
            throw new IllegalStateException("Snap direction must be either top/bottom or left/right or front/back! -> minSnap : "+minSnap+" | maxSnap : "+maxSnap);
        } else if(minSnap == SNAP.INTERVAL && minSnap != maxSnap) {
            throw new IllegalStateException("Both snap must be INTERVAL! -> minSnap : "+minSnap+" | maxSnap : "+maxSnap);
        } else if((minSnap == SNAP.FRONT && maxSnap == SNAP.BACK) || (minSnap == SNAP.BACK && maxSnap == SNAP.FRONT)) {
            throw new IllegalStateException("Unhandled z-order snap situation! -> minSnap : "+minSnap+" | maxSnap : "+maxSnap);
        }
    }

    public int getRangeI(StageBasis sb) {
        int mi;

        switch (minSnap) {
            case LEFT:
                mi = -BackgroundEffect.battleOffset + min.intValue();
                break;
            case RIGHT:
                mi = sb.st.len + BackgroundEffect.battleOffset + min.intValue();
                break;
            case TOP:
                mi = 1530 + min.intValue();
                break;
            default:
                mi = min.intValue();
        }

        int ma;

        switch (maxSnap) {
            case LEFT:
                ma = -BackgroundEffect.battleOffset + max.intValue();
                break;
            case RIGHT:
                ma = sb.st.len + BackgroundEffect.battleOffset + max.intValue();
                break;
            case TOP:
                ma = 1530 + max.intValue();
                break;
            default:
                ma = max.intValue();
        }

        return (int) (mi + sb.r.nextDouble() * (ma - mi));
    }

    public double getRangeD(StageBasis sb) {
        double mi;

        switch (minSnap) {
            case LEFT:
                mi = -BackgroundEffect.battleOffset + min.doubleValue();
                break;
            case RIGHT:
                mi = sb.st.len + BackgroundEffect.battleOffset + min.doubleValue();
                break;
            case TOP:
                mi = 1530 + min.doubleValue();
                break;
            default:
                mi = min.doubleValue();
        }

        double ma;

        switch (maxSnap) {
            case LEFT:
                ma = -BackgroundEffect.battleOffset + max.doubleValue();
                break;
            case RIGHT:
                ma = sb.st.len + BackgroundEffect.battleOffset + max.doubleValue();
                break;
            case TOP:
                ma = 1530 + max.doubleValue();
                break;
            default:
                ma = max.doubleValue();
        }

        return mi + sb.r.nextDouble() * (ma - mi);
    }

    private int getPureRangeI(StageBasis sb) {
        int mi = min.intValue();

        int ma = max.intValue();

        return (int) (mi + sb.r.nextDouble() * (ma - mi));
    }

    private boolean isFront() {
        return minSnap == maxSnap && maxSnap == SNAP.FRONT;
    }

    private boolean isXAxis(SNAP snap) {
        return snap == SNAP.LEFT || snap == SNAP.RIGHT;
    }

    private boolean isYAxis(SNAP snap) {
        return snap == SNAP.TOP || snap == SNAP.BOTTOM;
    }

    private boolean isZAxis(SNAP snap) {
        return snap == SNAP.FRONT || snap == SNAP.BACK;
    }
}
