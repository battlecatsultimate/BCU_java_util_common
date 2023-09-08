package common.battle;

import common.util.Data;

import java.util.Map;

public class CannonLevelCurve extends Data  {
    public enum PART {
        CANNON,
        BASE,
        DECORATION
    }

    private static final byte MIN_VALUE = 0;
    private static final byte MAX_VALUE = 1;

    public final int max;
    private final PART part;

    private final Map<Integer, int[][]> curveMap;

    public CannonLevelCurve(Map<Integer, int[][]> curveMap, int max, PART part) {
        this.curveMap = curveMap;
        this.max = max;
        this.part = part;
    }

    public float applyFormula(int type, int level) {
        if(level <= 0) {
            System.out.println("Warning : Invalid level " + level);

            return 0;
        }

        if(curveMap.containsKey(type)) {
            int[][] curve = curveMap.get(type);

            int index = (level - 1) / 10;

            if(index >= curve[0].length) {
                index = curve[0].length - 1;
            }

            int min = curve[MIN_VALUE][index];
            int max = curve[MAX_VALUE][index];

            int minLevel;
            float v;

            if(index == 0) {
                minLevel = part == PART.CANNON ? 1 : 0;
                v = min + (max - min) * (level - minLevel) / 9f;
            } else {
                minLevel = index * 10;
                v = min + (max - min) * (level - minLevel) / 10f;
            }

            switch (part) {
                case CANNON:
                    switch (type) {
                        case BASE_RANGE:
                            return (int) v / 4f;
                        case BASE_HEALTH_PERCENTAGE:
                            return v / 10f;
                        case BASE_HOLY_ATK_SURFACE:
                        case BASE_HOLY_ATK_UNDERGROUND:
                            return v / 1000f;
                        default:
                            return v;
                    }
                case BASE:
                case DECORATION:
                    return 1f - v / 10000f;
            }
        }

        System.out.println("Warning : Unknown type " + type);

        return 0;
    }
}
