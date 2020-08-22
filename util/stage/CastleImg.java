package common.util.stage;

import common.pack.Identifier;
import common.pack.IndexContainer.IndexCont;
import common.pack.IndexContainer.Indexable;
import common.system.VImg;

@IndexCont(CastleList.class)
public class CastleImg implements Indexable<CastleList, CastleImg> {

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
