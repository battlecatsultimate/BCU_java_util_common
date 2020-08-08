package common.battle.data;

import common.util.unit.Form;

public interface MaskUnit extends MaskEntity {
	public int getBack();

	public int getFront();

	public Orb getOrb();

	@Override
	public Form getPack();

	public int getPrice();

	public int getRespawn();

}
