package common.util.stage;

import common.pack.PackData.Identifier;
import common.pack.PackData.Indexable;
import common.system.VImg;

public class CastleImg implements Indexable<CastleImg> {

	public final Identifier<CastleImg> id;
	public final VImg img;

	public CastleImg(Identifier<CastleImg> id, VImg img) {
		this.id = id;
		this.img = img;
	}

	@Override
	public Identifier<CastleImg> getID() {
		return id;
	}

}
