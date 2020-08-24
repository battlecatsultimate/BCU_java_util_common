package common.util;

import common.io.json.JsonClass;
import common.io.json.JsonClass.NoTag;
import common.system.Copable;

import org.jetbrains.annotations.Nullable;

@JsonClass(noTag = NoTag.LOAD)
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