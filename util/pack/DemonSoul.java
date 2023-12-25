package common.util.pack;

import common.pack.Identifier;
import common.pack.IndexContainer;
import common.pack.PackData;
import common.util.anim.AnimU;

@IndexContainer.IndexCont(PackData.class)
public class DemonSoul extends AbSoul implements IndexContainer.Indexable<PackData, DemonSoul> {

    private final Identifier<DemonSoul> id;
    boolean e;

    public DemonSoul(int id, AnimU<?> animS, boolean enemy) {
        super(animS);
        this.id = new Identifier<>(Identifier.DEF, DemonSoul.class, id);
        e = enemy;

        if (!enemy) {
            anim.partial();
            anim.revert();
        }
    }

    @Override
    public Identifier<DemonSoul> getID() {
        return id;
    }

    @Override
    public String toString() {
        return (e ? "enemy " : "") + "demonsoul " + id;
    }
}