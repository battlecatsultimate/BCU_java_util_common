package common.battle.data;

import common.io.json.JsonClass;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.util.Data;
import common.util.pack.Soul;
import common.util.unit.Trait;

import java.util.ArrayList;

@JsonClass(noTag = NoTag.LOAD)
public abstract class DataEntity extends Data implements MaskEntity {

	public int hp, hb, speed, range;
	public int abi, width;
	public int loop = -1, will;
	@JsonField(io = JsonField.IOType.R)
	public int type, shield;

	public Identifier<Soul> death;
	@JsonField(generic = Trait.class, alias = Identifier.class)
	public ArrayList<Trait> traits = new ArrayList<>();
	//Despite traits being restructured, type was left here to guarantee that traits can be transferred from the old trait structure to the new trait structure
	//type and shield should be safely removable in 0-5-1-1

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
	public int getSpeed() {
		return speed;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getWill() {
		return will;
	}

	@JsonField(tag = "type", io = JsonField.IOType.R)
	public void genType(int t) {
		type = t;
	}

	@JsonField(tag = "shield", io = JsonField.IOType.R)
	public void genShield(int s) {
		shield = s;
	}

}
