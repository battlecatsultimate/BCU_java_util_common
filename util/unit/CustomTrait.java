package common.util.unit;

import common.io.json.JsonClass;
import common.pack.Identifier;
import common.pack.IndexContainer;
import common.util.Data;
import common.pack.PackData;
import common.pack.IndexContainer.Indexable;

@IndexContainer.IndexCont(PackData.class)
@JsonClass(noTag = JsonClass.NoTag.LOAD)
@JsonClass.JCGeneric(Identifier.class)

public class CustomTrait extends Data implements Indexable<PackData, CustomTrait>, Comparable<CustomTrait> {

    public String name = "new trait";
    public boolean targetType;
    // Target type will be used to toggle whether Anti-Traited, Anti-Non Metal, or Anti-All units will target this trait or not

    @JsonClass.JCIdentifier
    public Identifier<CustomTrait> id;

    @JsonClass.JCConstructor
    public CustomTrait() {
        id = null;
    }

    public CustomTrait(CustomTrait ct) {
        name = ct.name;
        targetType = ct.targetType;
        id = ct.id;
    }

    public CustomTrait(Identifier<CustomTrait> id) {
        this.id = id;
    }

    @Override
    public Identifier<CustomTrait> getID() { return id; }

    @Override
    public int compareTo(CustomTrait ctr) { return id.compareTo(ctr.id); }

    @Override
    public String toString() {
        return id + " - " + name;
    }

    /*TODO List:
    - SOLVE READING CUSTOM TRAITS ERROR
    - Add traits field to custom entities
    - Display custom traits on info page
    - Make custom traits actually effective in battle
    - Assigning custom traits via editor, having them on the same column as vanilla traits
    - Allowing users to assign an icon to their custom traits (and at the same time making this optional)
     */
}
