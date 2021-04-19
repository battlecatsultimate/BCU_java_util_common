package common.util.unit;

import common.io.json.JsonClass;
import common.io.json.JsonDecoder;
import common.io.json.JsonField;
import common.pack.*;
import common.system.VImg;
import common.util.Data;
import common.pack.IndexContainer.Indexable;
import main.MainBCU;
import utilpc.UtilPC;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

@IndexContainer.IndexCont(PackData.class)
@JsonClass.JCGeneric(Identifier.class)
@JsonClass
public class Trait extends Data implements Indexable<PackData, Trait> {
    public static void addBCTraits() {
        //Reads traits from BC and implements it into the main pack
        PackData.DefPack data = UserProfile.getBCData();
        String[] traitNames = {"Red", "Floating", "Black", "Metal", "Angel", "Alien", "Zombie", "Relic", "White", "Witch", "EVA", "base", "cannon"};
        for (int i = 0; i < traitNames.length ; i++) {
            Trait t = new Trait(data.getNextID(Trait.class));
            t.BCTrait = true;
            t.name = traitNames[i];
            if (i < 9)
                t.icon = MainBCU.builder.toVImg(UtilPC.getIcon(3, i));
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

    public ImageIcon obtainIcon() {
        try {
            BufferedImage IconTwo = VImgToIcon(icon);
            return new ImageIcon(IconTwo);
        } catch (NullPointerException npe) {
            icon = null;
            return null;
        }
    }

    // Convert Bitmask Type format to current format
    public static ArrayList<Trait> convertType(int type) {
        ArrayList<Trait> traits = new ArrayList<>();
        PackData.DefPack data = UserProfile.getBCData();
        if ((type & TB_RED) != 0)
            traits.add(data.traits.get(Data.TRAIT_RED));
        if ((type & TB_FLOAT) != 0)
            traits.add(data.traits.get(Data.TRAIT_FLOAT));
        if ((type & TB_BLACK) != 0)
            traits.add(data.traits.get(Data.TRAIT_BLACK));
        if ((type & TB_METAL) != 0)
            traits.add(data.traits.get(Data.TRAIT_METAL));
        if ((type & TB_ANGEL) != 0)
            traits.add(data.traits.get(Data.TRAIT_ANGEL));
        if ((type & TB_ALIEN) != 0)
            traits.add(data.traits.get(Data.TRAIT_ALIEN));
        if ((type & TB_ZOMBIE) != 0)
            traits.add(data.traits.get(Data.TRAIT_ZOMBIE));
        if ((type & TB_RELIC) != 0)
            traits.add(data.traits.get(Data.TRAIT_RELIC));
        if ((type & TB_WHITE) != 0)
            traits.add(data.traits.get(Data.TRAIT_WHITE));
        return traits;
    }

    @JsonDecoder.OnInjected
    public void onInjected() {
        icon = UserProfile.getUserPack(id.pack).source.readImage(Source.TRAITICON, id.id);
    }

    private BufferedImage VImgToIcon(VImg vi) { return (BufferedImage)vi.getImg().bimg(); }

    @JsonClass.JCGetter
    public static Trait getter(Identifier<?> id) { return (Trait) Identifier.get(id); }

    //TODO: Reformat talents, implement custom entity trait loading stuffs
}
