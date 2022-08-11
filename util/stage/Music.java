package common.util.stage;

import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.pack.IndexContainer.IndexCont;
import common.pack.IndexContainer.Indexable;
import common.pack.PackData;
import common.system.files.FileData;
import common.util.Data;

@JsonClass
@IndexCont(PackData.class)
@JsonClass.JCGeneric(Identifier.class)
public class Music implements Indexable<PackData, Music> {

	@JsonField
	@JsonClass.JCIdentifier
	public final Identifier<Music> id;
	@JsonField
	public long loop;

	public FileData data;

	@JsonClass.JCConstructor
	@Deprecated
	public Music() {
		id = null;
	}

	public Music(Identifier<Music> id, long loop, FileData fd) {
		this.id = id;
		this.loop = loop;
		data = fd;
	}

	@Override
	public Identifier<Music> getID() {
		return id;
	}

	@Override
	public String toString() {
		if (id != null) {
			return Data.trio(id.id) + ".ogg - " + id.pack;
		} else {
			return null;
		}
	}

}
