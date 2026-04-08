package common.battle;

import common.CommonStatic;
import common.util.BattleObj;
import common.util.stage.Limit;
import common.util.stage.StageLimit;
import common.util.unit.Form;

import java.util.Arrays;

public class ELineUp extends BattleObj {

	public final int[][] price, cool, maxC, tick, cdDownOrb, priceDownOrb;
	private final StageBasis b;

	protected ELineUp(LineUp lu, StageBasis sb) {
		b = sb;
		price = new int[2][5];
		cool = new int[2][5];
		maxC = new int[2][5];
		tick = new int[2][5];
		cdDownOrb = new int[2][5];
		priceDownOrb = new int[2][5];
		Limit lim = sb.est.lim;
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 5; j++) {
				Form form = lu.fs[i][j];
				if (form == null) {
					price[i][j] = -1;
					continue;
				}
				if (lim != null && ((lim.line == 1 && i == 1) || lim.unusable(lu.efs[i][j].du, sb.st.getCont().price)))
					price[i][j] = -1;
				else
					price[i][j] = 100 * (sb.globalCost() > -1 ? sb.globalCost() : (int) (lu.efs[i][j].getPrice(sb.st.getCont().price)));
				if (!StageLimit.isComboBanned(lim, C_DISCOUNT))
					price[i][j] -= price[i][j] * b.b.getInc(C_DISCOUNT, form.du.getPack().unit) / 100;
				maxC[i][j] = sb.globalCdLimit() > 0
						? sb.b.t().getFinResGlobal(sb.globalCdLimit(), StageLimit.isComboBanned(sb.est.lim, C_RESP) ? 0 : sb.b.getInc(C_RESP, lu.efs[i][j].du.getPack().unit))
						: sb.b.t().getFinRes(lu.efs[i][j].du.getRespawn(), StageLimit.isComboBanned(sb.est.lim, C_RESP) ? 0 : sb.b.getInc(C_RESP, lu.efs[i][j].du.getPack().unit));
				if (lim != null && lim.stageLimit != null) {
					if (price[i][j] != -1)
						price[i][j] = price[i][j] * lim.stageLimit.costMultiplier[form.unit.rarity] / 100;
					maxC[i][j] = maxC[i][j] * lim.stageLimit.cooldownMultiplier[form.unit.rarity] / 100;
				}
				int[][] orbs = lu.efs[i][j].getLevel().getOrbs();
				boolean hasEveryOther = false;
				if (orbs != null) {
					for (int[] orb : orbs) {
						if (orb.length != ORB_INTS)
							continue;
						int orbId = orb[0];
						hasEveryOther |= Arrays.stream(ORB_EVERY_OTHER).anyMatch(v -> v == orbId);
						if (orbId == ORB_COOLDOWN)
							cdDownOrb[i][j] = ORB_COOLDOWN_MULT[orb[2]];
						else if (orbId == ORB_COST_DOWN)
							priceDownOrb[i][j] = ORB_COST_DOWN_MULT[orb[2]];
					}
				}
				if (!hasEveryOther)
					tick[i][j] = -1;
			}
	}

	/**
	 * reset cooldown of a unit
	 */
	protected void get(int i, int j) {
		cool[i][j] = maxC[i][j];
		if (cdDownOrb[i][j] > 0 && tick[i][j] == 0)
			cool[i][j] -= cool[i][j] * cdDownOrb[i][j] / 100;
		b.cdDelayVisual[i][j] = StageBasis.DELAY_BASE.clone();
	}

	protected void delay(int i, int j, int[] delay) {
		if (cool[i][j] == 0)
			return;

		int inc = b.getDelayStrength(cool[i][j], maxC[i][j], delay);
		if (inc > 0) {
			b.cdDelayVisual[i][j][0] = Math.max(b.cdDelayVisual[i][j][0], cool[i][j]);
		} else {
			b.cdDelayVisual[i][j][2] += inc;
		}
		cool[i][j] += inc;
		if (cool[i][j] > maxC[i][j])
			cool[i][j] = maxC[i][j];
		if (inc < 0) {
			if (cool[i][j] <= 0) {
				cool[i][j] = 0;
				CommonStatic.setSE(SE_SPEND_REF);
				b.frameOffCd[i][j] = b.time;
			} else {
				b.cdDelayVisual[i][j][3] = 10;
			}
			CommonStatic.setSE(SE_DELAY_COOLDOWN);
		} else {
			b.cdDelayVisual[i][j][1] = 10;
			CommonStatic.setSE(SE_DELAY_COOLDOWN);
		}
	}

	/**
	 * count down the cooldown
	 */
	protected void update() {
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 5; j++) {
				if (cool[i][j] > 0) {
					cool[i][j]--;

//					if (cool[i][j] == 30)
//						delay(i, j, -30, 0);

					if (cool[i][j] == 0) {
						CommonStatic.setSE(SE_SPEND_REF);
						b.frameOffCd[i][j] = b.time;
					}
				}
				if (b.cdDelayVisual[i][j][1] > 0 && --b.cdDelayVisual[i][j][1] == 0) {
					b.cdDelayVisual[i][j][0] = 0;
				}
				if (b.cdDelayVisual[i][j][3] > 0 && --b.cdDelayVisual[i][j][3] == 0) {
					b.cdDelayVisual[i][j][2] = 0;
				}
			}
	}

}
