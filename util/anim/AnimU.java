package common.util.anim;

import common.system.VImg;
import common.system.fake.FakeImage;

public abstract class AnimU<T extends AnimU.ImageKeeper> extends AnimD<AnimU<?>, AnimU.UType> {

    public interface ImageKeeper {

        VImg getEdi();

        ImgCut getIC();

        MaAnim[] getMA();

        MaModel getMM();

        FakeImage getNum();

        VImg getUni();

        void unload();

    }

    public enum UType implements AnimI.AnimType<AnimU<?>, UType> {
        WALK, IDLE, ATK, HB, ENTER, BURROW_DOWN, BURROW_MOVE, BURROW_UP
    }

    public static final UType[] TYPE4 = {UType.WALK, UType.IDLE, UType.ATK, UType.HB};
    public static final UType[] TYPE5 = {UType.WALK, UType.IDLE, UType.ATK, UType.HB, UType.ENTER};

    public static final UType[] TYPE7 = {UType.WALK, UType.IDLE, UType.ATK, UType.HB, UType.BURROW_DOWN,
            UType.BURROW_MOVE, UType.BURROW_UP};

    protected boolean partial = false;
    public final T loader;

    protected AnimU(String path, T load) {
        super(path);
        loader = load;
    }

    protected AnimU(T load) {
        super("");
        loader = load;
    }

    public int getAtkLen() {
        partial();
        return anims[2].len + 1;
    }

    @Override
    public EAnimU getEAnim(UType t) {
        check();
        return new EAnimU(this, t);
    }

    public VImg getEdi() {
        return loader.getEdi();
    }

    @Override
    public FakeImage getNum() {
        return loader.getNum();
    }

    public VImg getUni() {
        return loader.getUni();
    }

    @Override
    public void load() {
        loaded = true;
        try {
            imgcut = loader.getIC();
            if (getNum() == null) {
                mamodel = null;
                return;
            }
            parts = imgcut.cut(getNum());
            partial();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unload() {
        loader.unload();
        super.unload();
    }

    protected void partial() {
        if (!partial) {
            partial = true;
            imgcut = loader.getIC();
            mamodel = loader.getMM();
            anims = loader.getMA();
            types = anims.length == 4 ? TYPE4 : anims.length == 5 ? TYPE5 : TYPE7;
        }
    }

}
