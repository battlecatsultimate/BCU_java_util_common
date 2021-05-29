package common.util.pack;

import common.pack.Identifier;
import common.pack.IndexContainer.IndexCont;
import common.pack.IndexContainer.Indexable;
import common.pack.PackData;
import common.util.Animable;
import common.util.anim.*;

@IndexCont(PackData.class)
public class Soul extends Animable<AnimU<?>, AnimU.UType> implements Indexable<PackData, Soul> {

	private final Identifier<Soul> id;

	public Soul(int id, AnimU<?> animS) {
		this.id = new Identifier<>(Identifier.DEF, Soul.class, id);
		anim = animS;
	}

	@Override
	public Identifier<Soul> getID() {
		return id;
	}

	@Override
	public String toString() {
		return "soul_" + id.id;
	}

	@Override
	public EAnimI getEAnim(AnimU.UType soulType) {
		return anim.getEAnim(soulType);
	}
}
