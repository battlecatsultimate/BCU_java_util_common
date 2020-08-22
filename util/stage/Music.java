package common.util.stage;

import common.pack.Identifier;
import common.pack.IndexContainer.IndexCont;
import common.pack.IndexContainer.Indexable;
import common.pack.PackData;
import common.system.files.FileData;

@IndexCont(PackData.class)
public class Music implements Indexable<PackData, Music> {

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
