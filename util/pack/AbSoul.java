package common.util.pack;

import common.pack.Identifier;
import common.util.Animable;
import common.util.anim.AnimU;
import common.util.anim.EAnimI;

public abstract class AbSoul extends Animable<AnimU<?>, AnimU.UType> {

    public AbSoul(AnimU<?> animS) {
        anim = animS;
    }

    abstract public Identifier<?> getID();

    abstract public String toString();

    @Override
    public EAnimI getEAnim(AnimU.UType uType) {
        return anim.getEAnim(uType);
    }
}
