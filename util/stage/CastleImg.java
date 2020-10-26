package common.util.stage;

import common.io.json.JsonClass;
import common.io.json.JsonDecoder.OnInjected;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.pack.IndexContainer.IndexCont;
import common.pack.IndexContainer.Indexable;
import common.pack.Source;
import common.pack.UserProfile;
import common.system.VImg;
import common.util.Data;

@IndexCont(CastleList.class)
@JsonClass.JCGeneric(Identifier.class)
@JsonClass
public class CastleImg implements Indexable<CastleList, CastleImg> {

	@JsonClass.JCIdentifier
	@JsonField
	public Identifier<CastleImg> id;
	public VImg img;

	public CastleImg() {
	}

	public CastleImg(Identifier<CastleImg> id, VImg img) {
		this.id = id;
		this.img = img;
	}

	@Override
	public Identifier<CastleImg> getID() {
		return id;
	}

	@OnInjected
	public void onInjected() {
		img = UserProfile.getUserPack(id.pack).source.readImage(Source.CASTLE, id.id);
	}

	@Override
	public String toString() {
		if(id == null)
			return super.toString();

		return id.toString();
	}
}
