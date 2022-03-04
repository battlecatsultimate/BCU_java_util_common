package common.util.lang;

import common.CommonStatic;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.util.Data;

import java.util.LinkedHashMap;

@JsonClass
public class MultiLangData extends Data {
    @JsonField(generic = {Integer.class, String.class})
    private final LinkedHashMap<Integer, String> dat = new LinkedHashMap<>();

    public MultiLangData() {
    }
    public MultiLangData(String str) {
        dat.put(lang(), str);
    }

    public void put(String data) {
        dat.put(lang(), data);
    }
    public void remove() {
        dat.remove(lang());
    }
    @Override
    public String toString() {
        if (dat.containsKey(lang()))
            return dat.get(lang());

        for (String i : dat.values())
            return i;
        return "";
    }

    private static int lang() {
        return CommonStatic.getConfig().lang;
    }
}
