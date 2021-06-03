package common.battle;

import common.util.Data;

import java.util.Map;

public class CannonLevelCurve extends Data  {
    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 1;

    private final Map<Integer, int[][]> curveMap;

    public CannonLevelCurve(Map<Integer, int[][]> curveMap) {
        this.curveMap = curveMap;
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

            int minLevel = index * 10 + 1;

            double v = min + (min - max) * (level - minLevel) / 9.0;

            switch (type) {
                case BASE_ATK_MAGNIFICATION:
                    return v / 100.0;
                case BASE_HOLY_ATK_SURFACE:
                case BASE_HOLY_ATK_UNDERGROUND:
                    return v + 100;
                case BASE_RANGE:
                    return v / 4.0;
                case BASE_HEALTH_PERCENTAGE:
                    return v / 10.0;
                default:
                    return v;
            }
        }

        System.out.println("Warning : Unknown type " + type);

        return 0;
    }
}
