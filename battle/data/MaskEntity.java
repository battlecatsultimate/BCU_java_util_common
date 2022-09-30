package common.battle.data;

import common.pack.Identifier;
import common.util.Animable;
import common.util.BattleStatic;
import common.util.Data;
import common.util.Data.Proc;
import common.util.anim.AnimU;
import common.util.anim.AnimU.UType;
import common.util.pack.Soul;
import common.util.unit.Trait;

import java.util.ArrayList;

public interface MaskEntity extends BattleStatic {

	int allAtk();

	int getAbi();

	Proc getAllProc();

	/**
	 * get the attack animation length
	 */
	default int getAnimLen() {
		return getPack().anim.getAtkLen();
	}

	int getAtkCount();

	int getAtkLoop();

	MaskAtk getAtkModel(int ind);

	MaskAtk[] getAtks();

	default AtkDataModel[] getSpAtks() {
		return null;
	}

	Identifier<Soul> getDeathAnim();

	ArrayList<Trait> getTraits();

	int getHb();

	int getHp();

	/**
	 * get the attack period
	 */
	int getItv();

	/**
	 * get the Enemy/Form this data represents
	 */
	Animable<AnimU<?>, UType> getPack();

	int getPost();

	Proc getProc();

	int getRange();

	MaskAtk getRepAtk();

	default AtkDataModel getResurrection() {
		return null;
	}

	default AtkDataModel getRevenge() {
		return null;
	}

	default AtkDataModel getCounter() {
		return null;
	}

	default AtkDataModel getGouge() {
		return null;
	}

	default AtkDataModel getResurface() {
		return null;
	}

	default AtkDataModel getRevive() {
		return null;
	}

	int getSpeed();

	int getWill();

	/**
	 * get waiting time
	 */
	int getTBA();

	default int getTouch() {
		return Data.TCH_N;
	}

	int getWidth();

	boolean isLD();

	default boolean isLD(int ind) {
		return isLD();
	}

	boolean isOmni();

	default boolean isOmni(int ind) {
		return isOmni();
	}

	boolean isRange();

	int[][] rawAtkData();

	int touchBase();

}
