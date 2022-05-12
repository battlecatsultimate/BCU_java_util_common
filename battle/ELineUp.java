package common.battle;

import common.CommonStatic;
import common.util.BattleObj;
import common.util.stage.Limit;

public class ELineUp extends BattleObj {

	public final int[][] price, cool, maxC;

	protected ELineUp(LineUp lu, StageBasis sb) {
		price = new int[2][5];
		cool = new int[2][5];
		maxC = new int[2][5];
		Limit lim = sb.est.lim;
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 5; j++) {
				if (lu.fs[i][j] == null) {
					price[i][j] = -1;
					continue;
				}
				price[i][j] = (int) (lu.efs[i][j].getPrice(sb.st.getCont().price) * 100);
				maxC[i][j] = sb.b.t().getFinRes(lu.efs[i][j].du.getRespawn());
				if (lim != null && ((lim.line == 1 && i == 1) || lim.unusable(lu.efs[i][j].du, sb.st.getCont().price)))
					price[i][j] = -1;
			}
	}

	/**
	 * reset cool down time of an unit
	 */
	protected void get(int i, int j) {
		cool[i][j] = maxC[i][j];
	}

	/**
	 * count down the cool down time
	 */
	protected void update() {
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 5; j++)
				if (cool[i][j] > 0) {
					cool[i][j]--;

					if(cool[i][j] == 0)
						CommonStatic.setSE(SE_SPEND_REF);
				}
	}

}
