package common.util.anim;

import common.system.fake.FakeImage;

public abstract class AnimD<A extends AnimD<A, T>, T extends Enum<T> & AnimI.AnimType<A, T>> extends AnimI<A, T> {

    public ImgCut imgcut;
    public MaModel mamodel;
    public T[] types;
    public MaAnim[] anims;
    public FakeImage[] parts;

    public boolean mismatch;

    protected final String str;
    protected boolean loaded = false;

    public AnimD(String st) {
        str = st;
    }

    @Override
    public void check() {
        if (!loaded)
            load();
    }

    @Override
    public EAnimD<T> getEAnim(T t) {
        check();
        if (mamodel == null)
            return null;
        MaAnim anim = getMaAnim(t);
        return anim == null ? null : new EAnimD<T>(this, mamodel, anim);
    }

    public final MaAnim getMaAnim(T t) {
        for (int i = 0; i < types.length; i++)
            if (types[i] == t)
                return anims[i];
        return null;
    }

    public abstract FakeImage getNum();

    public final int len(T t) {
        check();
        return getMaAnim(t).max + 1;
    }

    @Override
    public abstract void load();

    @Override
    public final String[] names() {
        check();
        return translate(types);
    }

    @Override
    public FakeImage parts(int i) {
        check();
        if (i < 0 || i >= parts.length)
            return null;
        return parts[i];
    }

    public void reorderModel(int[] inds) {
        for (int[] ints : mamodel.parts)
            if (ints != null && ints[0] >= 0)
                ints[0] = inds[ints[0]];
        for (MaAnim ma : anims)
            for (Part part : ma.parts)
                part.ints[0] = inds[part.ints[0]];
    }

    public void revert() {
        mamodel.revert();
        for (MaAnim ma : anims)
            if (ma != null)
                ma.revert();
    }

    @Override
    public final T[] types() {
        check();
        return types;
    }

    public void unload() {
        if (parts != null) {
            for (int i = 0; i < parts.length; i++) {
                if (parts[i] != null) {
                    parts[i].unload();
                    parts[i] = null;
                }
            }
        }
        parts = null;
        loaded = false;
    }

    public void validate() {
        check();
        mamodel.check(this);
        for (MaAnim ma : anims) {
            for (Part p : ma.parts) {
                p.check(this);
                p.validate();
            }
            ma.validate();
        }
    }

}
