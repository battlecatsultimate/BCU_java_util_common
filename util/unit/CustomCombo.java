package common.util.unit;

import common.io.json.JsonClass;
import common.pack.Identifier;
import common.pack.IndexContainer;
import common.pack.PackData;

@IndexContainer.IndexCont(PackData.class)
@JsonClass.JCGeneric(Identifier.class)
@JsonClass
public class CustomCombo extends Combo {

    public String name;
    public CustomCombo(Identifier<Combo> combo, String n, Form f) {
        super(combo, 0, 0, 1, f);
        name = n;
        show = 1;
        lv = 0;

        forms.put(0, f);
    }

    public void setType(int t) {
        type = t;
    }

    public void setLv(int l) {
        lv = l;
    }

    public void addForm(Form f) {
        forms.put(forms.size(), f);
    }

    public void removeForm(int index) {
        forms.remove(index);
        for (int ind : forms.keySet())
            if (ind >= index)
                forms.replace(ind - 1, forms.remove(ind - 1));
    }
}