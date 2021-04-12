package common.util.unit;

import common.pack.Identifier;

public class CustomCombo extends Combo {

    public String name;
    public CustomCombo(Identifier<Combo> combo, String n, Form f) {
        super(combo);
        name = n;
        show = 1;
        lv = 0;

        forms.put(0, f);
    }

    @Override
    public String getName() {
        return name;
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

    public void removeForm(Form f) {

    }
}