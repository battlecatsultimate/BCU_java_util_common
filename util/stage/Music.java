package common.util.stage;

import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.pack.IndexContainer.IndexCont;
import common.pack.IndexContainer.Indexable;
import common.pack.PackData;
import common.system.files.FileData;

@JsonClass
@IndexCont(PackData.class)
@JsonClass.JCGeneric(Identifier.class)
public class Music implements Indexable<PackData, Music> {

	@JsonField
	@JsonClass.JCIdentifier
	public final Identifier<Music> id;
	public FileData data;

	@JsonClass.JCConstructor
	@Deprecated
	public Music() {
		id = null;
	}

	public Music(Identifier<Music> id, FileData fd) {
		this.id = id;
		data = fd;
	}

	@Override
	public Identifier<Music> getID() {
		return id;
	}

	@Override
	public String toString() {
		return id.toString();
	}

}
