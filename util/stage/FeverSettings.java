package common.util.stage;

import common.io.json.JsonClass;
import common.util.stage.info.DefStageInfo;

import java.util.Arrays;

/**
 * settings of the fever mechanic of a stage, it works in any kind of stage.<br>
 * The default values are the ones of the first stage of the fever dojo.
 */
@JsonClass(noTag = JsonClass.NoTag.LOAD)
public class FeverSettings implements Cloneable {

    public static final int TIERS = 10; //amount of unit cost bonus tiers
    public static final int DEFAULT_TIME_BONUS_FRAMES = 5400; //time bonus length that is used if there is no dojo timer to follow
    /**
     * Settings of the fever mechanic that apply to a stage: the ones of its limit if they are enabled, otherwise the
     * ones of the official fever dojo if it is part of it
     * @return the settings, null if the stage has no fever
     */
    public static FeverSettings get(Stage st, Limit lim) {
        if (lim != null && lim.stageLimit != null && lim.stageLimit.fever.isEnabled())
            return lim.stageLimit.fever;
        if (st != null && st.info instanceof DefStageInfo) {
            FeverSettings fs = from((DefStageInfo) st.info);
            if (fs.isEnabled())
                return fs;
        }
        return null;
    }

    // the settings of a stage of the official fever dojo, the time bonus is always 3:00 there
    public static FeverSettings from(DefStageInfo info) {
        return from(info.feverMaxScore, info.feverDuration, info.feverBarMultiplier);
    }
    /**
     * @param table rows of [ cost range start (exclusive), cost range end (inclusive), percent of the cost ],
     *              the first row is [ -1, -1, kill multiplier ], see {@link DefStageInfo#parseFeverBar(int[])}
     */
    public static FeverSettings from(int maxPoints, int duration, int[][] table) {
        FeverSettings ans = new FeverSettings();
        ans.enabled = maxPoints > 0 && duration > 0;
        ans.maxPoints = maxPoints;
        ans.duration = duration;
        ans.timeBonusStart = 200;
        ans.timeBonusFrames = DEFAULT_TIME_BONUS_FRAMES;
        Arrays.fill(ans.bonusCost, -1);
        Arrays.fill(ans.bonusPercent, 0);
        if (table != null && table.length > 0) {
            ans.killMultiplier = table[0][2];

            for (int i = 1; i < table.length && i <= TIERS; i++) {
                ans.bonusCost[i - 1] = table[i][1];
                ans.bonusPercent[i - 1] = table[i][2];
            }
        }

        return ans;
    }
    public boolean enabled = false; //if fever is used in the stage
    public int maxPoints = 25000; //points needed for a full gauge
    public int duration = 600; //length of fever mode in frames
    public int killMultiplier = 7; //percent of the money drop and the score of an enemy that its kill gives as fever points
    /**
     * the unit cost bonus tiers, a unit with a cost up to bonusCost[i] (and above the one of the tier before) gives
     * bonusPercent[i] percent of its cost as fever points, tiers that aren't used have -1 as cost
     * bonus tiers up to 10 exist because of customizability for custom stages
     */
    public int[] bonusCost = { 2000, 4500, 5500, 7000, 10000, -1, -1, -1, -1, -1 };
    public int[] bonusPercent = { 15, 17, 25, 30, 0, 0, 0, 0, 0, 0 };
    public int timeBonusStart = 200; //percent that kills are worth at the start of the stage, it goes to 100 over the time bonus length
    public int timeBonusFrames = -1; //the time bonus length in frames, 0 for no time bonus, -1 to follow the timer of the dojo or 3:00 if there is none

    @Override
    public FeverSettings clone() {
        try {
            FeverSettings fs = (FeverSettings) super.clone();
            fs.bonusCost = bonusCost.clone();
            fs.bonusPercent = bonusPercent.clone();
            return fs;
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean isEnabled() {
        return enabled && maxPoints > 0 && duration > 0;
    }

    //the time bonus length in frames for the stage
    public int getTimeBonusFrames(Stage st) {
        if (timeBonusFrames >= 0)
            return timeBonusFrames;

        return st != null && st.trail && st.timeLimit > 0 ? st.timeLimit * 1800 : DEFAULT_TIME_BONUS_FRAMES;
    }

    //Puts the tiers in order: the ones that are used first, with the costs going up
    public void normalize() {
        int[][] used = new int[TIERS][];
        int count = 0;

        for (int i = 0; i < TIERS; i++)
            if (bonusCost[i] > 0)
                used[count++] = new int[] { bonusCost[i], Math.max(0, bonusPercent[i]) };

        used = Arrays.copyOf(used, count);
        // stable, so of two tiers with the same cost the first one stays
        Arrays.sort(used, (a, b) -> Integer.compare(a[0], b[0]));
        Arrays.fill(bonusCost, -1);
        Arrays.fill(bonusPercent, 0);
        int n = 0;

        for (int[] tier : used) {
            if (n > 0 && bonusCost[n - 1] == tier[0])
                continue;

            bonusCost[n] = tier[0];
            bonusPercent[n] = tier[1];
            n++;
        }
    }

}
