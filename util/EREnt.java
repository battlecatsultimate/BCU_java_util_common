package common.util;

import common.system.Copable;
import org.jetbrains.annotations.Nullable;

public class EREnt<X> implements BattleStatic, Copable<EREnt<X>> {

    @Nullable
    public X ent;
    public int multi = 100;
    public int mula = 100;
    public int share = 1;

    @Override
    public EREnt<X> copy() {
        EREnt<X> ans = new EREnt<X>();
        ans.ent = ent;
        ans.multi = multi;
        ans.mula = mula;
        ans.share = share;
        return ans;
    }

}