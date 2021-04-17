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

	Identifier<Soul> getDeathAnim();

	ArrayList<Identifier<Trait>> getTraits();

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

	int getShield();

	int getSpeed();

	/**
	 * get waiting time
	 */
	int getTBA();

	default int getTouch() {
		return Data.TCH_N;
	}

	int getType();

	int getWidth();

	boolean isLD();

	boolean isOmni();

	boolean isRange();

	int[][] rawAtkData();

	int touchBase();

}
