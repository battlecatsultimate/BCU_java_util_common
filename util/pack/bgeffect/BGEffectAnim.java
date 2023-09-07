package common.util.pack.bgeffect;

import common.system.VImg;
import common.system.fake.FakeImage;
import common.util.anim.*;

import java.util.ArrayList;
import java.util.List;

public class BGEffectAnim extends AnimD<BGEffectAnim, BGEffectAnim.BGEffType> {

    private final String imgcutName, mamodelName, maanimName;
    private final VImg img;

    /**
     * Animation class for BG effect
     * @param st Name of png
     * @param imgcut Name of imguct
     * @param mamodel Name of mamodel
     * @param maanim Name of maanim
     */
    public BGEffectAnim(String st, String imgcut, String mamodel, String maanim) {
        super(st);
        imgcutName = imgcut;
        mamodelName = mamodel;
        maanimName = maanim;

        img = new VImg(str);
    }

    public enum BGEffType implements AnimI.AnimType<BGEffectAnim, BGEffType> {
        DEF
    }

    @Override
    public FakeImage getNum() {
        return img.getImg();
    }

    @Override
    public void load() {
        imgcut = ImgCut.newIns(imgcutName);
        mamodel = MaModel.newIns(mamodelName);
        anims = new MaAnim[] { MaAnim.newIns(maanimName) };
        types = BGEffType.values();
        parts = imgcut.cut(img.getImg());

        loaded = true;
    }

    @Override
    public boolean cantLoadAll(AnimU.ImageKeeper.AnimationType type) {
        // Background effect animation is part of BC animation, there must not be failure
        return false;
    }

    @Override
    public List<String> collectInvalidAnimation(AnimU.ImageKeeper.AnimationType type) {
        // Background effect animation is part of BC animation, there must not be failure
        return new ArrayList<>();
    }
}
