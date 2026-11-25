package common.battle;

import common.CommonStatic;
import common.util.BattleObj;
import common.util.anim.EAnimD;
import common.util.pack.EffAnim;
import common.util.pack.EffAnim.DefEff;
import common.util.stage.FeverSettings;

import java.math.BigInteger;

/**
 * Fever mechanic.<br>
 * fever points are collected from every enemy kill and from bonus points for every deployed unit, depending on its cost.
 * fever mode starts automatically every time they get X fever points ahead of where they were when the last
 * fever mode ended. While fever mode runs all cooldowns are 0.
 */
public class FeverMode extends BattleObj {
	/**
	 * Cost based bonus points of a deployed unit
	 * @param costs    cost limit of every tier, -1 for tiers that aren't used, see {@link FeverSettings#bonusCost}
	 * @param percents percent of the cost every tier gives
	 * @param cost     cost of the deployed unit
	 * @return fever points the unit gives, 0 if the cost doesn't fall in any of the tiers
	 */
	public static int getBonus(int[] costs, int[] percents, int cost) {
		int start = 0;

		for (int i = 0; i < costs.length && costs[i] > 0; i++) {
			if (cost > start && cost <= costs[i])
				return cost * percents[i] / 100;

			start = costs[i];
		}

		return 0;
	}

	/**
	 * Points of an enemy kill, trunc(multiplier / 100 * timeBonus * (drop + score)).<br>
	 * The time bonus goes from startPercent at the start of the stage to 100 percent after window frames.
	 * With 200 percent and 5400 frames it is the one of the fever dojo: 2 - min(frames, 5400) / 5400
	 * @param multiplier   percent of the money drop and the score that is worth fever points
	 * @param startPercent time bonus in percent at frame 0
	 * @param window       frames the time bonus takes to get to 100 percent, 0 if there is no time bonus
	 * @param drop         base money drop of the enemy, before treasures and the magnification of the stage
	 * @param score        score of the enemy in the stage data, 0 if there is none
	 * @param frames       battle frames since the stage started
	 */
	public static int getKillPoints(int multiplier, int startPercent, int window, int drop, int score, int frames) {
		long len = Math.max(0, window);
		long elapsed = Math.max(0, Math.min(frames, len));
		// the time bonus in percent is bonus / len, all in whole numbers so there is no rounding error where it gets truncated
		long bonus = len == 0 ? 100 : 100 * len + (long) (startPercent - 100) * (len - elapsed);
		long div = len == 0 ? 1 : len;
		BigInteger ans = BigInteger.valueOf(multiplier).multiply(BigInteger.valueOf(bonus)).multiply(BigInteger.valueOf(drop + (long) score))
				.divide(BigInteger.valueOf(div * 10000L));
		return ans.min(BigInteger.valueOf(Integer.MAX_VALUE)).max(BigInteger.valueOf(Integer.MIN_VALUE)).intValue();
	}

	private final int maxPoints, duration, killMultiplier, timeBonusStart, timeBonusFrames;
	private final int[] bonusCost, bonusPercent;

	public int points; //fever points collected so far, they keep counting during fever mode and never reset
	public int time; // frames of fever mode left, 0 if it isn't running
	public EAnimD<?> effect; //the effect that is shown on the screen while fever mode is running, null if there is none
	private int base; //points the gauge counts from, 0 at first and the points at the end of the latest fever mode after that
	/**
	 * @param settings       settings of the stage, null if it has no fever
	 * @param timeBonusFrames length of the time bonus in frames, see {@link FeverSettings#getTimeBonusFrames}
	 */
	public FeverMode(FeverSettings settings, int timeBonusFrames) {
		boolean on = settings != null && settings.isEnabled();
		maxPoints = on ? settings.maxPoints : 0;
		duration = on ? settings.duration : 0;
		killMultiplier = on ? settings.killMultiplier : 0;
		timeBonusStart = on ? settings.timeBonusStart : 100;
		this.timeBonusFrames = timeBonusFrames;
		bonusCost = on ? settings.bonusCost.clone() : new int[0];
		bonusPercent = on ? settings.bonusPercent.clone() : new int[0];
	}

	public boolean isEnabled() {
		return maxPoints > 0 && duration > 0;
	}

	public boolean isActive() {
		return time > 0;
	}

	public int getMaxPoints() {
		return maxPoints;
	}

	public int getDuration() {
		return duration;
	}

	/**
	 * @return how full the gauge is, from 0 to 1, it empties while fever mode is running
	 */
	public float getGauge() {
		if (!isEnabled())
			return 0f;

		if (isActive())
			return (float) time / duration;

		return Math.max(0f, Math.min(1f, (float) (points - base) / maxPoints));
	}

	/**
	 * call when an enemy is killed
	 * @param drop   base money drop of the enemy
	 * @param score  score of the enemy in the stage data
	 * @param frames battle frames since the stage started
	 * @return the points the kill is worth, they are added to the fever points
	 */
	public int killed(int drop, int score, int frames) {
		if (!isEnabled())
			return 0;

		int ans = getKillPoints(killMultiplier, timeBonusStart, timeBonusFrames, drop, score, frames);

		points += ans;

		return ans;
	}

	/**
	 * call when the player deploys a unit
	 * @param cost cost the unit was deployed with
	 */
	public void deployed(int cost) {
		if (!isEnabled())
			return;

		points += getBonus(bonusCost, bonusPercent, cost);
	}

	/**
	 * call when fever mode starts, starts the effect that is shown on the screen until fever mode ends
	 */
	public void startEffect() {
		EffAnim<DefEff> anim = CommonStatic.getBCAssets().effas.A_FEVER;

		effect = anim == null ? null : anim.getEAnim(DefEff.DEF);
	}

	/**
	 * call once per frame
	 * @return true on the frame fever mode starts
	 */
	public boolean update() {
		if (!isEnabled())
			return false;

		if (time > 0) {
			// the effect keeps looping until the end of fever mode
			if (effect != null)
				effect.update(false);

			if (--time == 0) {
				effect = null;
				// the next fever mode needs a full gauge of points from here
				base = points;
			}

			return false;
		}

		if (points - base < maxPoints)
			return false;

		time = duration;

		return true;
	}
}
