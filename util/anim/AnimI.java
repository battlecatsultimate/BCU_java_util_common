package common.util.anim;

import common.system.fake.FakeImage;
import common.util.Animable;
import common.util.BattleStatic;
import common.util.lang.MultiLangCont;

public abstract class AnimI<A extends AnimI<A, T>, T extends Enum<T> & AnimI.AnimType<A, T>> extends Animable<A, T>
        implements BattleStatic {

    public interface AnimType<A extends AnimI<A, T>, T extends Enum<T> & AnimType<A, T>> {

    }

    protected static String[] translate(AnimType<?, ?>... anim) {
        String[] ans = new String[anim.length];
        for (int i = 0; i < ans.length; i++)
            ans[i] = MultiLangCont.getStatic().ANIMNAME.getCont(anim[i]);
        return ans;
    }

    @SuppressWarnings("unchecked")
    public AnimI() {
        anim = (A) this;
    }

    public abstract void check();

    public abstract void load();

    public abstract String[] names();

    public abstract FakeImage parts(int img);

    public abstract T[] types();

}
