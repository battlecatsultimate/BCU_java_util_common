package common.util.pack;

import common.pack.Identifier;
import common.pack.IndexContainer;
import common.pack.PackData;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.util.anim.*;

@IndexContainer.IndexCont(PackData.class)
public class DemonSoul extends AnimD<DemonSoul, DemonSoul.DemonSoulType> implements IndexContainer.Indexable<PackData, DemonSoul> {
    public enum DemonSoulType implements AnimI.AnimType<DemonSoul, DemonSoulType> {
        DEF
    }

    private final Identifier<DemonSoul> id;
    private final VImg img;

    public DemonSoul(String st, int i) {
        super(st);
        img = new VImg(str + ".png");
        id = Identifier.parseInt(i, DemonSoul.class);
    }

    @Override
    public Identifier<DemonSoul> getID() {
        return id;
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
        types = DemonSoulType.values();
        parts = imgcut.cut(img.getImg());
    }

    @Override
    public String toString() {
        return "demon-soul_" + id.id;
    }
}
