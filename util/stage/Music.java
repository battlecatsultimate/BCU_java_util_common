package common.util.stage;

import common.pack.PackData.Identifier;
import common.pack.PackData.Indexable;
import common.system.files.FileData;

public class Music implements Indexable<Music> {

	public final Identifier<Music> id;
	public final FileData data;

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
