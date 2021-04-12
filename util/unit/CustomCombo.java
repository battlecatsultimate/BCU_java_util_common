package common.util.unit;

import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.pack.IndexContainer;
import common.util.Data;
import common.pack.PackData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

@IndexContainer.IndexCont(PackData.class)
@JsonClass.JCGeneric(Identifier.class)
@JsonClass
public class CustomCombo extends Data implements IndexContainer.Indexable<PackData, CustomCombo>, Comparable<CustomCombo> {

    @JsonField
    public String comboName = "";
    public ArrayList<Identifier<Unit>> Units = new ArrayList<>();
    public int[] reqForm = {0,0,0,0,0};
    public Identifier<CustomCombo> id;
    public int Lv, effect;

    @JsonClass.JCConstructor
    public CustomCombo() {

    }

    public CustomCombo(CustomCombo cb) {
        comboName = cb.comboName;
        Units = cb.Units;
        id = cb.id;
        Lv = cb.Lv;
        effect = cb.effect;
    }

    @Override
    public Identifier<CustomCombo> getID() {
        return id;
    }

    @Override
    public int compareTo(@NotNull CustomCombo o) {
        return id.compareTo(o.id);
    }
}
