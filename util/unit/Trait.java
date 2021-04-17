package common.util.unit;

import common.io.json.JsonClass;
import common.io.json.JsonDecoder;
import common.io.json.JsonField;
import common.pack.*;
import common.system.VImg;
import common.util.Data;
import common.pack.IndexContainer.Indexable;

import javax.swing.*;
import java.awt.image.BufferedImage;

@IndexContainer.IndexCont(PackData.class)
@JsonClass.JCGeneric(Identifier.class)
@JsonClass
public class Trait extends Data implements Indexable<PackData, Trait>, Comparable<Trait> {

    @JsonField
    public String name = "new trait";

    @JsonClass.JCIdentifier
    @JsonField
    public Identifier<Trait> id;
    public VImg icon = null;

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
    public int compareTo(Trait tr) { return id.compareTo(tr.id); }

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

    @JsonDecoder.OnInjected
    public void onInjected() {
        icon = UserProfile.getUserPack(id.pack).source.readImage(Source.TRAITICON, id.id);
    }

    private BufferedImage VImgToIcon(VImg vi) { return (BufferedImage)vi.getImg().bimg(); }

    @JsonClass.JCGetter
    public static Trait getter(Identifier<?> id) { return (Trait) Identifier.get(id); }
}
