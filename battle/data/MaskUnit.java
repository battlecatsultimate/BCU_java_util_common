package common.battle.data;

import common.util.unit.Form;

public interface MaskUnit extends MaskEntity {
	int getBack();

	int getFront();

	Orb getOrb();

	@Override
	Form getPack();

	int getPrice();

	int getRespawn();

	PCoin getPCoin();

	MaskUnit clone();

	int getLimit();
}
