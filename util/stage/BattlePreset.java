package common.util.stage;

import common.battle.BasisLU;
import common.battle.BasisSet;
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
    public static boolean isLineupPreset(BattlePreset bp) {
        BasisLU blu = BasisSet.current().sele;
        blu.lu.renew();
        Treasure t = BasisSet.current().t();

        if (!Arrays.equals(t.tech, bp.tech))
            return false;
        else if (!Arrays.equals(t.trea, bp.trea))
            return false;
        else if (!Arrays.equals(t.bslv, bp.bslv))
            return false;
        else if (!Arrays.equals(t.fruit, bp.fruit))
            return false;
        else if (!Arrays.equals(t.gods, bp.gods))
            return false;
        else if (t.alien != bp.alien || t.star != bp.star)
            return false;
        else if (blu.nyc[0] != bp.cannonType)
            return false;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                Form bpform = bp.fs[i][j];
                Form luform = blu.lu.fs[i][j];
                if (bpform == null || luform == null) {
                    if (bpform != null || luform != null)
                        return false;
                } else if (!bpform.uid.equals(luform.uid) || bpform.fid != luform.fid) {
                    return false;
                } else {
                    Level bplv = bp.levels[i][j];
                    Level lulv = blu.lu.getLv(luform);
                    if (lulv.getLv() != bplv.getLv() || lulv.getPlusLv() != bplv.getPlusLv())
                        return false;
                    else if (!Arrays.equals(Level.getInts(lulv), Level.getInts(bplv)))
                        return false;
                    // todo: check if orbs match
                }
            }
        }

        return true;
    }

    public static void generateBasis(BattlePreset bp) {
        BasisLU dest = BasisSet.current().sele;
        Treasure t = BasisSet.current().t();

        t.tech = bp.tech.clone();
        t.trea = bp.trea.clone();
        t.bslv = bp.bslv.clone();
        t.fruit = bp.fruit.clone();
        t.gods = bp.gods.clone();
        t.alien = bp.alien;
        t.star = bp.star;
        BasisSet.current().renewTreasure();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                Form form = bp.fs[i][j];
                Level lv = bp.levels[i][j];
                if (form == null) {
                    dest.lu.fs[i][j] = null;
                    continue;
                }

                dest.lu.fs[i][j] = form.unit.forms[form.fid]; // prevent form change affecting battle preset
                int[] lvs = new int[10];
                lvs[0] = lv.getLv();
                lvs[1] = lv.getPlusLv();
                System.arraycopy(lv.getTalents(), 0, lvs, 2, lv.getTalents().length);
                dest.lu.setLv(form.unit, Level.lvList(form.unit, lvs, lv.getOrbs()));
            }
        }
        
        dest.nyc[0] = bp.cannonType;
        dest.lu.renew();
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

    public static class LevelObject { // Used in reading BC data
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
