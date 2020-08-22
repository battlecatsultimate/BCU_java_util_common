package common.battle.data;

import common.io.json.JsonClass;
import common.io.json.JsonClass.NoTag;
import common.pack.Identifier;
import common.util.Data;
import common.util.pack.Soul;

@JsonClass(noTag = NoTag.LOAD)
public abstract class DataEntity extends Data implements MaskEntity {

    public int hp, hb, speed, range;
    public int abi, type, width;
    public int loop = -1, shield;
    public Identifier<Soul> death;

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
    public int getType() {
        return type;
    }

    @Override
    public int getWidth() {
        return width;
    }

}
