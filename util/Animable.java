package common.util;

import common.util.anim.AnimI;
import common.util.anim.EAnimI;

public abstract class Animable<T extends AnimI> extends ImgCore {

	public T anim;

	public abstract EAnimI getEAnim(int t);

}
