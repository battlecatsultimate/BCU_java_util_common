package common.util.pack;

import common.pack.Identifier;
import common.pack.IndexContainer;
import common.pack.PackData;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.util.anim.*;

@IndexContainer.IndexCont(PackData.class)
public class DemonSoul extends Soul implements IndexContainer.Indexable<PackData, Soul> {

    boolean e;

    public DemonSoul(int id, AnimU<?> animS, boolean enemy) {
        super(id, animS);
        e = enemy;

        if (!enemy) {
            anim.partial();
            anim.revert();
        }
    }

    @Override
    public String toString() {
        return "demon" + super.toString() + (e ? "_e" : "");
    }
}