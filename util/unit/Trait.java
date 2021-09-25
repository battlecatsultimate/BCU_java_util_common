package common.util.unit;

import common.io.json.JsonClass;
import common.io.json.JsonDecoder;
import common.io.json.JsonField;
import common.pack.*;
import common.system.VImg;
import common.util.Data;
import common.pack.IndexContainer.Indexable;

import java.util.ArrayList;

@IndexContainer.IndexCont(PackData.class)
@JsonClass.JCGeneric(Identifier.class)
@JsonClass
public class Trait extends Data implements Indexable<PackData, Trait> {
    public static void addBCTraits() {
        //Reads traits from BC and implements it into the main pack
        PackData.DefPack data = UserProfile.getBCData();
        String[] traitNames = {"Red", "Floating", "Black", "Metal", "Angel", "Alien", "Zombie", "Aku", "Relic", "White", "EVA", "Witch", "base", "cannon"};
        for (int i = 0; i < traitNames.length ; i++) {
            Trait t = new Trait(data.getNextID(Trait.class));
            t.BCTrait = true;
            t.name = traitNames[i];
            data.traits.add(t);
        }
    }

    @JsonField
    public String name = "new trait";

    @JsonClass.JCIdentifier
    @JsonField
    public Identifier<Trait> id;
    public VImg icon = null;
    public boolean BCTrait = false;

    @JsonField
    public boolean targetType;
    // Target type will be used to toggle whether Anti-Traited, Anti-Non Metal, or Anti-All units will target this trait or not


    @JsonClass.JCConstructor
    public Trait() {
        id = null;
    }

    public Trait(Trait t) {
        name = t.name;
        targetType = t.targetType;
        id = t.id;
        icon = t.icon;
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

    // Convert Bitmask Type format to new format
    public static ArrayList<Trait> convertType(int type) {
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

    @JsonDecoder.OnInjected
    public void onInjected() {
        icon = UserProfile.getUserPack(id.pack).source.readImage(Source.TRAITICON, id.id);
    }

    @JsonClass.JCGetter
    public static Trait getter(Identifier<?> id) { return (Trait) Identifier.get(id); }
}
