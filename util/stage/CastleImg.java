package common.util.stage;

import common.pack.PackData.Identifier;
import common.pack.PackData.Indexable;
import common.system.VImg;

public class CastleImg implements Indexable {

	public final Identifier id;
	public final VImg img;

	public CastleImg(Identifier id, VImg img) {
		this.id = id;
		this.img = img;
	}

	@Override
	public Identifier getID() {
		return id;
	}

}
