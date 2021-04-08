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
public class CustomTrait extends Data implements Indexable<PackData, CustomTrait>, Comparable<CustomTrait> {

    @JsonField
    public String name = "new trait";

    @JsonClass.JCIdentifier
    @JsonField
    public Identifier<CustomTrait> id;
    public VImg icon = null;

    @JsonField
    public boolean targetType;
    // Target type will be used to toggle whether Anti-Traited, Anti-Non Metal, or Anti-All units will target this trait or not


    @JsonClass.JCConstructor
    public CustomTrait() {}

    public CustomTrait(CustomTrait ct) {
        name = ct.name;
        targetType = ct.targetType;
        id = ct.id;
        icon = ct.icon;
    }

    public CustomTrait(Identifier<CustomTrait> id) {
        this.id = id;
    }

    @Override
    public Identifier<CustomTrait> getID() { return id; }

    @Override
    public int compareTo(CustomTrait ctr) { return id.compareTo(ctr.id); }

    @Override
    public String toString() {
        return id + " - " + name;
    }

    public ImageIcon obtainIcon() {
        BufferedImage IconTwo = VImgToIcon(icon);
        return new ImageIcon(IconTwo);
    }

    @JsonDecoder.OnInjected
    public void onInjected() {
        icon = UserProfile.getUserPack(id.pack).source.readImage(Source.TRAITICON, id.id);
    }

    private BufferedImage VImgToIcon(VImg vi) { return (BufferedImage)vi.getImg().bimg(); }

    @JsonClass.JCGetter
    public static CustomTrait getter(Identifier<?> id) {
        return (CustomTrait) Identifier.get(id);
    }
}
