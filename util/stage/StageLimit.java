package common.util.stage;

import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.util.BattleStatic;
import common.util.Data;

import java.util.HashSet;
import java.util.List;

@JsonClass
public class StageLimit extends Data implements BattleStatic {
    @JsonField
    public int maxMoney = 0;
    @JsonField
    public int globalCooldown = 0;
    @JsonField(generic = Integer.class)
    public HashSet<Integer> bannedCatCombo = new HashSet<>();

    public StageLimit() {
    }

    public StageLimit(int maxMoney, int globalCooldown, List<Integer> bannedCombo) {
        this.maxMoney = maxMoney;
        this.globalCooldown = globalCooldown;
        this.bannedCatCombo.addAll(bannedCombo);
    }

    public StageLimit clone() {
        StageLimit sl = new StageLimit();
        sl.maxMoney = maxMoney;
        sl.globalCooldown = globalCooldown;
        sl.bannedCatCombo.addAll(bannedCatCombo);
        return sl;
    }
}
