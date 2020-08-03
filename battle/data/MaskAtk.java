package common.battle.data;

import common.util.BattleStatic;
import common.util.Data;
import common.util.Data.Proc;

public interface MaskAtk extends BattleStatic {

	public default int getAltAbi() {
		return 0;
	}

	public default int getDire() {
		return 1;
	}

	public int getLongPoint();

	public default int getMove() {
		return 0;
	}

	public Proc getProc();

	public int getShortPoint();

	public default int getTarget() {
		return Data.TCH_N;
	}

	public boolean isRange();

	public default int loopCount() {
		return -1;
	}
	
	public int getAtk();

}
