package common.util.lang;

import common.CommonStatic;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.util.Data;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

@JsonClass(read = JsonClass.RType.FILL)
public class MultiLangData extends Data {
    @JsonField(generic = {Integer.class, String.class})
    private final LinkedHashMap<Integer, String> dat = new LinkedHashMap<>();

    @JsonClass.JCConstructor
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

    @NotNull
    @Override
    public String toString() {
        if (dat.containsKey(lang())) {
            String temp = dat.get(lang());

            if(temp == null) {
                return "";
            } else {
                return temp;
            }
        }

        for (String i : dat.values())
            return i;
        return "";
    }

    private static int lang() {
        return CommonStatic.getConfig().lang;
    }
}
