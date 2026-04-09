package common.util.stage;

import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.util.BattleStatic;
import common.util.Data;

import java.util.HashSet;

@JsonClass(noTag = JsonClass.NoTag.LOAD)
public class StageLimit extends Data implements BattleStatic, Cloneable {
    public static boolean isComboBanned(Limit lim, int comboId) {
        if (lim == null || lim.stageLimit == null)
            return false;
        else
            return lim.stageLimit.bannedCatCombo.contains(comboId);
    }

    public enum SpeedOverrideMode {
        SET("=", ""),
        MULTIPLY("x", "%");

        final String pre;
        final String post;

        SpeedOverrideMode(String pr, String po) {
            pre = pr;
            post = po;
        }

        public String getPre() {
            return pre;
        }

        public String getPost() {
            return post;
        }

        @Override
        public String toString() {
            return name();
        }
    }

    public int maxMoney = 0;
    public int globalCooldown = 0;
    public int globalCost = -1;
    public int maxUnitSpawn = -1;

    public int[] cooldownMultiplier = { 100, 100, 100, 100, 100, 100 };
    public int[] costMultiplier = { 100, 100, 100, 100, 100, 100 };
    public int[] rarityDeployLimit = { -1, -1, -1, -1, -1, -1 }; // -1 for none

    public int[] deployDuplicationTimes = { 0, 0, 0, 0, 0, 0 }; // 0 for deactivated
    public int[] deployDuplicationDelay = { 0, 0, 0, 0, 0, 0 }; // unit is frame

    public boolean coolStart = false;
    
    public int cannonMultiplier = 100; // percentage

    public SpeedOverrideMode unitSpeedOverrideMode = null;
    public int unitSpeedOverride = -1; // -1 for deactivated
    public SpeedOverrideMode enemySpeedOverrideMode = null;
    public int enemySpeedOverride = -1; // -1 for deactivated

    @JsonField(generic = Integer.class)
    public HashSet<Integer> bannedCatCombo = new HashSet<>();
    @JsonField(generic = Integer.class)
    public HashSet<Integer> bannedOrb = new HashSet<>();

    public StageLimit() {

    }

    @Override
    public StageLimit clone() {
        StageLimit sl;

        try {
            sl = (StageLimit) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();

            sl = new StageLimit();
        }

        sl.maxMoney = maxMoney;
        sl.globalCooldown = globalCooldown;
        sl.globalCost = globalCost;
        sl.maxUnitSpawn = maxUnitSpawn;

        sl.cooldownMultiplier = cooldownMultiplier.clone();
        sl.costMultiplier = costMultiplier.clone();
        sl.rarityDeployLimit = rarityDeployLimit.clone();

        sl.deployDuplicationTimes = deployDuplicationTimes.clone();
        sl.deployDuplicationDelay = deployDuplicationDelay.clone();

        sl.bannedCatCombo.addAll(bannedCatCombo);
        sl.bannedOrb.addAll(bannedOrb);
        sl.coolStart = coolStart;
        sl.cannonMultiplier = cannonMultiplier;

        sl.unitSpeedOverride = unitSpeedOverride;
        sl.unitSpeedOverrideMode = unitSpeedOverrideMode;
        sl.enemySpeedOverride = enemySpeedOverride;
        sl.enemySpeedOverrideMode = enemySpeedOverrideMode;

        return sl;
    }

    public StageLimit combine(StageLimit alt) {
        StageLimit c = clone();
        if (alt.maxMoney != 0)
            c.maxMoney = alt.maxMoney;
        if (alt.globalCooldown != 0)
            c.globalCooldown = alt.globalCooldown;
        if (alt.globalCost != -1)
            c.globalCost = alt.globalCost;
        if (alt.maxUnitSpawn != -1)
            c.maxUnitSpawn = alt.maxUnitSpawn;

        c.cooldownMultiplier = alt.cooldownMultiplier.clone();
        c.costMultiplier = alt.costMultiplier.clone();

        for (int i = 0; i < alt.rarityDeployLimit.length; i++)
            if (alt.rarityDeployLimit[i] != -1)
                c.rarityDeployLimit[i] = alt.rarityDeployLimit[i];

        for (int i = 0; i < 6; i++) {
            if (alt.deployDuplicationTimes[i] != 0) {
                c.deployDuplicationTimes[i] = alt.deployDuplicationTimes[i];
                c.deployDuplicationDelay[i] = alt.deployDuplicationDelay[i];
            }
        }

        c.coolStart = alt.coolStart;
        c.cannonMultiplier = alt.cannonMultiplier;

        if (alt.unitSpeedOverride != -1) {
            c.unitSpeedOverride = alt.unitSpeedOverride;
            c.unitSpeedOverrideMode = alt.unitSpeedOverrideMode;
        }
        if (alt.enemySpeedOverride != -1) {
            c.enemySpeedOverride = alt.enemySpeedOverride;
            c.enemySpeedOverrideMode = alt.enemySpeedOverrideMode;
        }

        c.bannedCatCombo.addAll(alt.bannedCatCombo);
        c.bannedOrb.addAll(alt.bannedOrb);

        return c;
    }
}