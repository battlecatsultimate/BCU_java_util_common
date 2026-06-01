package common.util.stage;

import common.battle.BasisLU;
import common.battle.Treasure;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.util.unit.Form;
import common.util.unit.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonClass(noTag = JsonClass.NoTag.LOAD)
public class BattlePreset {
    public static BasisLU generateBasis(BattlePreset bp) {
        BasisLU preset = new BasisLU();
        Treasure t = preset.t();

        t.tech = bp.tech.clone();
        t.trea = bp.trea.clone();
        t.bslv = bp.bslv.clone();
        t.fruit = bp.fruit.clone();
        t.gods = bp.gods.clone();
        t.alien = bp.alien;
        t.star = bp.star;

        System.arraycopy(bp.fs, 0, preset.lu.fs, 0, bp.fs.length);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                Form form = bp.fs[i][j];
                if (form == null)
                    continue;

                preset.lu.fs[i][j] = form;
                preset.lu.setLv(form.unit, bp.levels[i][j]);
            }
        }
        preset.nyc[0] = bp.cannonType;
        preset.lu.renew();
        return preset;
    }

    public enum ActivatedTreasure {
        EOC1,  // EoC Ch. 1
        EOC2,  // EoC Ch. 2
        EOC3,  // EoC Ch. 3
        ITF1,  // ItF Ch. 1
        ITF2,  // ItF Ch. 2
        ITF3,  // ItF Ch. 3
        COTC1, // CotC Ch. 1
        COTC2, // CotC Ch. 2
        COTC3, // CotC Ch. 3
        BASE   // Base health boost
    }

    public static class LevelObject {
        public int evolution;
        public int level;
        public int plusLevel;
    }
    //TODO verify customized battle preset loading

    public int level; // It seems preset can be activated per crown

    @JsonField(alias = Form.FormJson.class)
    public final Form[][] fs = new Form[2][5];
    public final Level[][] levels = new Level[2][5];

    public int cannonType; // Raw ID of cannon that is parsed into BCU ID order
    public boolean baseHealthBoost; // Add 20k to unit base health if this is true

    // Copied treasure data manually
    @JsonField(gen = JsonField.GenType.FILL)
    public int[] tech = new int[Treasure.LV_TOT],
            trea = new int[Treasure.T_TOT],
            bslv = new int[Treasure.BASE_TOT],
            fruit = new int[7],
            gods = new int[3];

    @JsonField(block = true)
    public final List<ActivatedTreasure> activatedTreasures = new ArrayList<>(); // Used for display reasons

    @JsonField
    public int alien, star;

    @Override
    public String toString() {
        return "BattlePreset{\n" +
                "level=" + level + "\n" +
                ", fs=" + Arrays.toString(fs) + "\n" +
                ", levels=" + Arrays.toString(levels) + "\n" +
                ", cannonType=" + cannonType + "\n" +
                ", tech=" + Arrays.toString(tech) + "\n" +
                ", trea=" + Arrays.toString(trea) + "\n" +
                ", bslv=" + Arrays.toString(bslv) + "\n" +
                ", fruit=" + Arrays.toString(fruit) + "\n" +
                ", gods=" + Arrays.toString(gods) + "\n" +
                ", activatedTreasures=" + activatedTreasures + "\n" +
                ", alien=" + alien + "\n" +
                ", star=" + star + "\n" +
                '}';
    }
}
