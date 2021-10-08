package common.system;

import common.CommonStatic;
import common.battle.StageBasis;
import common.util.anim.EAnimD;
import common.util.pack.bgeffect.BackgroundEffect;

import javax.annotation.Nullable;

public class BattleRange<T extends Number> {
    public static final double battleRatio = 0.2;
    public static final double battleHeightOffset = 1020.0 / battleRatio;
    public static final double battleOffset = 400 / battleRatio;

    public enum SNAP {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
        INTERVAL,
        FRONT,
        BACK,
        SECOND,
        PERCENT
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
            !(isXAxis(minSnap) && isXAxis(maxSnap)) &&
            !(isYAxis(minSnap) && isYAxis(maxSnap)) &&
            !(isZAxis(minSnap) && isZAxis(maxSnap)) &&
            !(isSecond(minSnap) && isSecond(maxSnap)) &&
            !(isPercentage(minSnap) && isPercentage(maxSnap)) &&
            !(isInterval(minSnap) && isInterval(maxSnap))
        ) {
            throw new IllegalStateException("Snap direction must be either top/bottom or left/right or front/back or second or percentage! -> minSnap : "+minSnap+" | maxSnap : "+maxSnap);
        } else if((minSnap == SNAP.FRONT && maxSnap == SNAP.BACK) || (minSnap == SNAP.BACK && maxSnap == SNAP.FRONT)) {
            throw new IllegalStateException("Unhandled z-order snap situation! -> minSnap : "+minSnap+" | maxSnap : "+maxSnap);
        }
    }

    public int getRangeI(StageBasis sb) {
        int mi;

        if(minSnap != null) {
            switch (minSnap) {
                case LEFT:
                    mi = (int) (-battleOffset + min.intValue());
                    break;
                case RIGHT:
                    mi = (int) (sb.st.len + battleOffset + min.intValue());
                    break;
                case BOTTOM:
                    mi = (int) (battleHeightOffset + min.intValue());
                    break;
                case SECOND:
                    mi = (int) (min.intValue() / 30.0);
                    break;
                default:
                    mi = min.intValue();
            }
        } else {
            mi = min.intValue();
        }

        int ma;

        if(maxSnap != null) {
            switch (maxSnap) {
                case LEFT:
                    ma = (int) (-battleOffset + max.intValue());
                    break;
                case RIGHT:
                    ma = (int) (sb.st.len + battleOffset + max.intValue());
                    break;
                case BOTTOM:
                    ma = (int) (battleHeightOffset + max.intValue());
                    break;
                case SECOND:
                    ma = (int) (max.intValue() / 30.0);
                    break;
                default:
                    ma = max.intValue();
            }
        } else {
            ma = max.intValue();
        }

        if(mi == ma) {
            return mi;
        }

        return (int) (mi + sb.r.nextDouble() * (ma - mi));
    }

    public double getRangeD(StageBasis sb) {
        double mi;

        if(minSnap != null) {
            switch (minSnap) {
                case LEFT:
                    mi = -battleOffset + min.doubleValue();
                    break;
                case RIGHT:
                    mi = sb.st.len + battleOffset + min.doubleValue();
                    break;
                case BOTTOM:
                    mi = battleHeightOffset + min.doubleValue();
                    break;
                case SECOND:
                    mi = min.doubleValue() / 30.0;
                    break;
                case PERCENT:
                    mi = min.doubleValue() / 100.0;
                    break;
                default:
                    mi = min.doubleValue();
            }
        } else {
            mi = min.doubleValue();
        }

        double ma;

        if(maxSnap != null) {
            switch (maxSnap) {
                case LEFT:
                    ma = -battleOffset + max.doubleValue();
                    break;
                case RIGHT:
                    ma = sb.st.len + battleOffset + max.doubleValue();
                    break;
                case BOTTOM:
                    ma = battleHeightOffset + max.doubleValue();
                    break;
                case SECOND:
                    ma = max.doubleValue() / 30.0;
                    break;
                case PERCENT:
                    ma = max.doubleValue() / 100.0;
                    break;
                default:
                    ma = max.doubleValue();
            }
        } else {
            ma = max.doubleValue();
        }

        if(mi == ma) {
            return mi;
        }

        return mi + sb.r.nextDouble() * (ma - mi);
    }

    public double getRangeX(StageBasis sb) {
        double mi;

        if(minSnap != null) {
            if(minSnap == SNAP.LEFT) {
                mi = min.doubleValue();
            } else if(minSnap == SNAP.RIGHT) {
                mi = (sb.st.len * CommonStatic.BattleConst.ratio / battleRatio) + battleOffset + min.doubleValue();
            } else {
                mi = battleOffset / 2.0 + min.doubleValue();
            }
        } else {
            mi = battleOffset / 2.0 + min.doubleValue();
        }

        double ma;

        if(maxSnap != null) {
            if(maxSnap == SNAP.LEFT) {
                ma = max.doubleValue();
            } else if(maxSnap == SNAP.RIGHT) {
                ma = max.doubleValue() + (sb.st.len * CommonStatic.BattleConst.ratio / battleRatio) + battleOffset;
            } else {
                ma = battleOffset / 2.0 + max.doubleValue();
            }
        } else {
            ma = battleOffset / 2.0 + max.doubleValue();
        }

        if(mi == ma) {
            return mi;
        }

        return mi + sb.r.nextDouble() * (ma - mi);
    }

    public double getRangeY(StageBasis sb) {
        double mi;

        if(minSnap != null) {
            if(minSnap == SNAP.TOP) {
                mi = min.doubleValue() + (BackgroundEffect.BGHeight * 3 - sb.battleHeight + sb.midH) / battleRatio;
            } else if(minSnap == SNAP.BOTTOM) {
                mi = min.doubleValue() + (BackgroundEffect.BGHeight * 3 + sb.midH) / battleRatio;
            } else {
                mi = min.doubleValue();
            }
        } else {
            mi = battleHeightOffset + min.doubleValue();
        }

        double ma;

        if(maxSnap != null) {
            if(maxSnap == SNAP.TOP) {
                ma = max.doubleValue() + (BackgroundEffect.BGHeight * 3 - sb.battleHeight + sb.midH) / battleRatio;
            } else if(maxSnap == SNAP.BOTTOM) {
                ma = max.doubleValue() + (BackgroundEffect.BGHeight * 3 + sb.midH) / battleRatio;
            } else {
                ma = max.doubleValue();
            }
        } else {
            ma = battleHeightOffset + max.doubleValue();
        }

        if(mi == ma) {
            return mi;
        }

        return mi + sb.r.nextDouble() * (ma - mi);
    }

    public int getPureRangeI(StageBasis sb) {
        int mi = min.intValue();

        int ma = max.intValue();

        return (int) (mi + sb.r.nextDouble() * (ma - mi));
    }

    public int getAnimFrame(EAnimD<?> anim, StageBasis sb) {
        int mi;

        if (minSnap == SNAP.INTERVAL) {
            mi = min.intValue() + anim.len() * 5;
        } else {
            mi = min.intValue();
        }

        int ma;

        if(maxSnap == SNAP.INTERVAL) {
            ma = max.intValue() + anim.len() * 5;
        } else {
            ma = max.intValue();
        }

        if(mi == ma) {
            return mi;
        }

        return (int) (mi + sb.r.nextDouble() * (ma - mi));
    }

    public boolean isFront() {
        return minSnap == maxSnap && maxSnap == SNAP.FRONT;
    }

    private boolean isXAxis(SNAP snap) {
        return snap == null || snap == SNAP.LEFT || snap == SNAP.RIGHT;
    }

    private boolean isYAxis(SNAP snap) {
        return snap == null || snap == SNAP.TOP || snap == SNAP.BOTTOM;
    }

    private boolean isZAxis(SNAP snap) {
        return snap == null || snap == SNAP.FRONT || snap == SNAP.BACK;
    }

    private boolean isPercentage(SNAP snap) {
        return snap == null || snap == SNAP.PERCENT;
    }

    private boolean isSecond(SNAP snap) {
        return snap == null || snap == SNAP.SECOND;
    }

    private boolean isInterval(SNAP snap) {
        return snap == null || snap == SNAP.INTERVAL;
    }
}
