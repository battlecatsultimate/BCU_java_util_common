package common.battle.data;

import common.util.BattleStatic;
import common.util.Data;
import common.util.Data.Proc;

public interface MaskAtk extends BattleStatic {

	default int getAltAbi() {
		return 0;
	}

	int getAtk();

	default int getDire() {
		return 1;
	}

	int getLongPoint();

	default int getMove() {
		return 0;
	}

	Proc getProc();

	int getShortPoint();

	default int getTarget() {
		return Data.TCH_N;
	}

	boolean isRange();

	default int loopCount() {
		return -1;
	}

}
