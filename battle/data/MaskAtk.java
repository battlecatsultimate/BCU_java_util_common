package common.battle.data;

import common.util.BattleStatic;
import common.util.Data;
import common.util.Data.Proc;
import common.util.unit.Trait;

import java.util.ArrayList;

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

	ArrayList<Trait> getATKTraits();

	Proc getProc();

	int getShortPoint();

	default int getTarget() {
		return Data.TCH_N;
	}

	boolean isOmni();

	boolean isRange();

	default int loopCount() {
		return -1;
	}

}
