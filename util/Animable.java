package common.util;

import common.util.anim.AnimI;
import common.util.anim.EAnimI;

public abstract class Animable<A extends AnimI<A, T>, T extends Enum<T> & AnimI.AnimType<A, T>> extends ImgCore {

    public A anim;

    public abstract EAnimI getEAnim(T t);

}
