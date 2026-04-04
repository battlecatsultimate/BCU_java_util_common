package common.util.unit;

import common.battle.data.Orb;
import common.io.json.JsonClass;
import common.io.json.JsonDecoder;
import common.io.json.JsonField;
import common.pack.*;
import common.pack.IndexContainer.Indexable;
import common.system.VImg;
import common.util.Data;

import java.util.*;

@IndexContainer.IndexCont(PackData.class)
@JsonClass.JCGeneric(Identifier.class)
@JsonClass
public class Trait extends Data implements Indexable<PackData, Trait> {

    public static List<Trait> TRAITED;

    public static void read() {
        PackData.DefPack data = UserProfile.getBCData();
        for (int i = 0; i < TRAIT_TOT; i++) {
            Trait t = new Trait(data.getNextID(Trait.class));
            data.traits.add(t);
        }
        TRAITED = UserProfile.getBCData().traits.getList().subList(TRAIT_RED,TRAIT_WHITE);
        TRAITED.remove(TRAIT_METAL);
    }

    public static ArrayList<Trait> bitmaskToTrait(int type) {
        ArrayList<Trait> traits = new ArrayList<>();
        PackData.DefPack data = UserProfile.getBCData();
        if ((type & TB_RED) != 0)
            traits.add(data.traits.get(TRAIT_RED));
        if ((type & TB_FLOAT) != 0)
            traits.add(data.traits.get(TRAIT_FLOAT));
        if ((type & TB_BLACK) != 0)
            traits.add(data.traits.get(TRAIT_BLACK));
        if ((type & TB_METAL) != 0)
            traits.add(data.traits.get(TRAIT_METAL));
        if ((type & TB_ANGEL) != 0)
            traits.add(data.traits.get(TRAIT_ANGEL));
        if ((type & TB_ALIEN) != 0)
            traits.add(data.traits.get(TRAIT_ALIEN));
        if ((type & TB_ZOMBIE) != 0)
            traits.add(data.traits.get(TRAIT_ZOMBIE));
        if ((type & TB_DEMON) != 0)
            traits.add(data.traits.get(TRAIT_DEMON));
        if ((type & TB_RELIC) != 0)
            traits.add(data.traits.get(TRAIT_RELIC));
        if ((type & TB_WHITE) != 0)
            traits.add(data.traits.get(TRAIT_WHITE));
        if ((type & TB_EVA) != 0)
            traits.add(data.traits.get(TRAIT_EVA));
        if ((type & TB_WITCH) != 0)
            traits.add(data.traits.get(TRAIT_WITCH));
        if ((type & TB_INFH) != 0)
            traits.add(data.traits.get(TRAIT_INFH));
        return traits;
    }

    public static ArrayList<Trait> talentBitmaskToTrait(int type) {
        ArrayList<Trait> traits = new ArrayList<>();
        PackData.DefPack data = UserProfile.getBCData();
        if ((type & TB_RED_T) != 0)
            traits.add(data.traits.get(TRAIT_RED));
        if ((type & TB_FLOAT_T) != 0)
            traits.add(data.traits.get(TRAIT_FLOAT));
        if ((type & TB_BLACK_T) != 0)
            traits.add(data.traits.get(TRAIT_BLACK));
        if ((type & TB_METAL_T) != 0)
            traits.add(data.traits.get(TRAIT_METAL));
        if ((type & TB_ANGEL_T) != 0)
            traits.add(data.traits.get(TRAIT_ANGEL));
        if ((type & TB_ALIEN_T) != 0)
            traits.add(data.traits.get(TRAIT_ALIEN));
        if ((type & TB_ZOMBIE_T) != 0)
            traits.add(data.traits.get(TRAIT_ZOMBIE));
        if ((type & TB_RELIC_T) != 0)
            traits.add(data.traits.get(TRAIT_RELIC));
        if ((type & TB_DEMON_T) != 0)
            traits.add(data.traits.get(TRAIT_DEMON));
        if ((type & TB_WHITE_T) != 0)
            traits.add(data.traits.get(TRAIT_WHITE));
        if ((type & TB_EVA_T) != 0)
            traits.add(data.traits.get(TRAIT_EVA));
        if ((type & TB_WITCH_T) != 0)
            traits.add(data.traits.get(TRAIT_WITCH));
        return traits;
    }

    public static List<Trait> convertOrb(int mask) {
        List<Trait> ans = new ArrayList<>();
        PackData.DefPack data = UserProfile.getBCData();

        for (int i = 0; i < Orb.orbTrait.length - 1; i++) {
            if ((mask & (1 << Orb.orbTrait[i])) > 0) {
                ans.add(data.traits.get(Orb.orbTrait[i]));
            }
        }

        return ans;
    }

    public static boolean isUsed(Trait t) {
        if (t.getCont() instanceof PackData.DefPack)
            return true;
        PackData.UserPack pack = (PackData.UserPack) t.getCont();
        Collection<PackData.UserPack> pacs = UserProfile.getUserPacks();
        for (PackData.UserPack pacc : pacs) {
            if (pacc.desc.dependency.contains(pack.desc.id) || pacc.desc.id.equals(pack.desc.id)) {
                for (Enemy en : pacc.enemies.getList())
                    if (en.de.getTraits().contains(t))
                        return true;
                for (Unit un : pacc.units.getList())
                    for (Form uf : un.forms)
                        if (uf.du.getTraits().contains(t))
                            return true;
            }
        }
        return false;
    }

    public static boolean isTargetTraited(List<Trait> traits) {
        return new HashSet<>(traits).containsAll(TRAITED);
    }

    @JsonField
    public String name = "new trait";

    @JsonClass.JCIdentifier
    @JsonField
    public Identifier<Trait> id;
    public VImg icon = null;

    @JsonField
    public boolean targetType;
    @JsonField(generic = Form.class, alias = Form.FormJson.class, tag = "others")
    public final ArrayList<Form> targetForms = new ArrayList<>();


    @JsonClass.JCConstructor
    public Trait() {
        id = null;
    }

    public Trait(Trait t) {
        name = t.name;
        targetType = t.targetType;
        id = t.id;
        icon = t.icon;
        targetForms.addAll(t.targetForms);
    }

    public Trait(Identifier<Trait> id) {
        this.id = id;
    }

    @Override
    public Identifier<Trait> getID() { return id; }

    @Override
    public String toString() {
        return id + " - " + name;
    }

    public void verify() {
        targetForms.removeIf(Objects::isNull);
    }

    @JsonDecoder.OnInjected
    public void onInjected() {
        icon = ((PackData.UserPack) getCont()).source.readImage(Source.BasePath.TRAIT.toString(), id.id);
        verify();
    }

    @JsonClass.JCGetter
    public static Trait getter(Identifier<?> id) { return (Trait) Identifier.get(id); }
}
