package common.battle.data;

import common.io.json.JsonClass;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.util.Data;
import java.util.ArrayList;
import common.util.pack.Soul;
import common.util.unit.Trait;

@JsonClass(noTag = NoTag.LOAD)
public abstract class DataEntity extends Data implements MaskEntity {

	public int hp, hb, speed, range;
	public int abi, type = 0, width;
	public int loop = -1, shield;
	public Identifier<Soul> death;
	@JsonField(generic = Trait.class, alias = Identifier.class)
	public ArrayList<Trait> traits = new ArrayList<>();
	//Despite traits being restructured, type was left here to guarantee that traits can be transferred from the old trait structure to the new trait structure

	@Override
	public int getAbi() {
		return abi;
	}

	@Override
	public int getAtkLoop() {
		return loop;
	}

	@Override
	public Identifier<Soul> getDeathAnim() {
		return death;
	}

	@Override
	public ArrayList<Trait> getTraits() {
		return traits;
	}

	@Override
	public int getHb() {
		return hb;
	}

	@Override
	public int getHp() {
		return hp;
	}

	@Override
	public int getRange() {
		return range;
	}

	@Override
	public int getShield() {
		return shield;
	}

	@Override
	public int getSpeed() {
		return speed;
	}

	@Override
	public int getWidth() {
		return width;
	}

}
