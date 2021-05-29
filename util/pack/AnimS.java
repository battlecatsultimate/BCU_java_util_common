package common.util.pack;

import common.CommonStatic;
import common.io.json.JsonClass;
import common.pack.Source;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.util.anim.*;

@JsonClass.JCGeneric(Source.ResourceLocation.class)
public class AnimS extends AnimD<AnimS, AnimS.SoulType> {

    public enum SoulType implements AnimI.AnimType<AnimS, AnimS.SoulType> {
        DEF
    }

    @JsonClass.JCIdentifier
    private final Source.ResourceLocation id;
    private final VImg img;

    public AnimS(String name, Source.ResourceLocation rl) {
        super(name);
        img = new VImg(name + ".png");
        id = rl;
    }

    @Override
    public FakeImage getNum() {
        return img.getImg();
    }

    @Override
    public void load() {
        loaded = true;
        imgcut = ImgCut.newIns(str + ".imgcut");
        mamodel = MaModel.newIns(str + ".mamodel");
        anims = new MaAnim[] { MaAnim.newIns(str + ".maanim") };
        types = AnimS.SoulType.values();
        parts = imgcut.cut(img.getImg());
    }

    @Override
    public String toString() {
        return "soul_" + CommonStatic.parseIntsN(id.id)[1];
    }
}
