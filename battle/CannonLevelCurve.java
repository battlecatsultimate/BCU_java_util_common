package common.battle;

import common.util.Data;

import java.util.Map;

public class CannonLevelCurve extends Data  {
    private static final byte MIN_VALUE = 0;
    private static final byte MAX_VALUE = 1;

    public final int max;

    private final Map<Integer, int[][]> curveMap;

    public CannonLevelCurve(Map<Integer, int[][]> curveMap, int max) {
        this.curveMap = curveMap;
        this.max = max;
    }

    public double applyFormula(int type, int level) {
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
            double v;

            if(index == 0) {
                minLevel = 1;
                v = min + (max - min) * (level - minLevel) / 9.0;
            } else {
                minLevel = index * 10;
                v = min + (max - min) * (level - minLevel) / 10.0;
            }

            switch (type) {
                case BASE_RANGE:
                    return (int) v / 4.0;
                case BASE_HEALTH_PERCENTAGE:
                    return v / 10.0;
                case BASE_HOLY_ATK_SURFACE:
                case BASE_HOLY_ATK_UNDERGROUND:
                    return v / 1000.0;
                default:
                    return v;
            }
        }

        System.out.println("Warning : Unknown type " + type);

        return 0;
    }
}
