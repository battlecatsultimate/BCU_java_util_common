package common.util.unit;

import common.pack.Identifier;

import java.util.HashMap;

public class DataCombo extends Combo {

    public int name;
    public HashMap<Integer, Form> forms;

    protected DataCombo(Identifier<Combo> ID, String str) {
        super(ID, str);
        String[] strs = str.split(",");
        name = Integer.parseInt(strs[0]);
    }
}