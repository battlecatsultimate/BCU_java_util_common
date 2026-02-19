package common.util.stage;

import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.util.BattleStatic;
import common.util.Data;

import java.util.HashSet;

@JsonClass(noTag = JsonClass.NoTag.LOAD)
public class StageLimit extends Data implements BattleStatic {
    public enum SpeedOverrideMode {
        SET,
        MULTIPLY
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
        sl.coolStart = coolStart;
        sl.cannonMultiplier = cannonMultiplier;

        sl.unitSpeedOverride = unitSpeedOverride;
        sl.enemySpeedOverride = enemySpeedOverride;


        return sl;
    }

    public StageLimit combine(StageLimit sec) {
        StageLimit c = clone();
        if (sec.maxMoney != 0)
            c.maxMoney = sec.maxMoney;
        if (sec.globalCooldown != 0)
            c.globalCooldown = sec.globalCooldown;
        if (sec.globalCost != -1)
            c.globalCost = sec.globalCost;
        if (sec.maxUnitSpawn != -1)
            c.maxUnitSpawn = sec.maxUnitSpawn;

        c.cooldownMultiplier = sec.cooldownMultiplier.clone();
        c.costMultiplier = sec.costMultiplier.clone();
        for (int i = 0; i < sec.rarityDeployLimit.length; i++)
            if (sec.rarityDeployLimit[i] != -1)
                c.rarityDeployLimit[i] = sec.rarityDeployLimit[i];

        c.deployDuplicationTimes = sec.deployDuplicationTimes.clone();
        c.deployDuplicationDelay = sec.deployDuplicationDelay.clone();

        c.coolStart = sec.coolStart;
        c.cannonMultiplier = sec.cannonMultiplier;

        if (sec.unitSpeedOverride != -1)
            c.unitSpeedOverride = sec.unitSpeedOverride;
        if (sec.enemySpeedOverride != -1)
            c.enemySpeedOverride = sec.enemySpeedOverride;

        return c;
    }
}