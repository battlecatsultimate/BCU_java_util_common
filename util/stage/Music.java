package common.util.stage;

import common.pack.PackData.Identifier;
import common.pack.PackData.Indexable;
import common.system.files.FileData;

public class Music implements Indexable {

	public final Identifier id;
	public final FileData data;

	public Music(Identifier id, FileData fd) {
		this.id = id;
		data = fd;
	}

	@Override
	public Identifier getID() {
		return id;
	}

	@Override
	public String toString() {
		return id.id;
	}

}
