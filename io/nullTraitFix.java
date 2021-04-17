package common.io;

import common.battle.data.CustomEntity;
import common.pack.PackData;
import common.pack.UserProfile;
import common.util.unit.Trait;
import common.util.unit.Enemy;
import common.util.unit.Unit;
import common.util.unit.Form;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Objects;

public abstract class nullTraitFix {
    // The class name is actually misleading, since things don't load as null, but instead load the class instead of the identifier which leads to ClassCastException. This is temporary but will be left until reformat
    public static void fixNullTraits() {
        Collection<PackData.UserPack> pacs = UserProfile.getUserPacks();
        List<Trait> traits = new ArrayList<>();
        List<Enemy> enemies = new ArrayList<>();
        List<Unit> units = new ArrayList<>();
        for (PackData.UserPack pack : pacs)
            if (!pack.desc.id.equals("000000")) {
                traits.addAll(pack.traits.getList());
                enemies.addAll(pack.enemies.getList());
                units.addAll(pack.units.getList());
        }
        for (Trait trt : traits) {
            for (Enemy enemy : enemies) {
                CustomEntity en = (CustomEntity)enemy.de;
                for (int m = 0; m < Objects.requireNonNull(en).customTraits.size(); m++)
                    if (en.nullFixer.get(m).equals(trt.id.toString())) en.customTraits.set(m, trt.id);
            }
            for (Unit uni : units)
                for (Form uf : uni.forms) {
                    CustomEntity un = (CustomEntity)uf.du;
                    for (int m = 0; m < Objects.requireNonNull(un).customTraits.size(); m++)
                        if (un.nullFixer.get(m).equals(trt.id.toString())) un.customTraits.set(m, trt.id);
                }
        }
    }
}