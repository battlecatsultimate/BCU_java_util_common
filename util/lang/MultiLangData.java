package common.util.lang;

import common.CommonStatic;
import common.CommonStatic.Lang;
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
        int lang = lang();

        if (data != null && data.length() > 0)
            dat.put(lang, data);
        else
            dat.remove(lang);
    }
    public void remove() {
        dat.remove(lang());
    }

    @NotNull
    @Override
    public String toString() {
        int lang = lang();

        if (dat.containsKey(lang)) {
            String temp = dat.get(lang);

            if(temp != null)
                return temp;
        }

        for (int i = 1; i < Lang.LOC_CODE.length; i++) {
            if (i < Lang.pref[lang].length) {
                if (dat.containsKey(Lang.pref[lang][i])) {
                    String temp = dat.get(Lang.pref[lang][i]);

                    if (temp != null)
                        return temp;
                }
            } else if (dat.containsKey(i)) {
                String temp = dat.get(i);

                if(temp != null)
                    return temp;
            }
        }
        return "";
    }

    public int getGrabbedLocale() {
        int lang = lang();

        for (int i = 1; i < Lang.LOC_CODE.length; i++) {
            if (i < Lang.pref[lang].length) {
                if (dat.containsKey(Lang.pref[lang][i])) {
                    String temp = dat.get(Lang.pref[lang][i]);

                    if (temp != null)
                        return Lang.pref[lang][i];
                }
            } else if (dat.containsKey(i)) {
                String temp = dat.get(i);

                if(temp != null)
                    return Lang.pref[lang][i];
            }
        }

        return -1;
    }

    private static int lang() {
        return CommonStatic.getConfig().lang;
    }

    public MultiLangData copy() { //Makes a copy of this MultiLangData object
        MultiLangData ans = new MultiLangData();
        for (int lang : dat.keySet())
            ans.dat.put(lang, dat.get(lang));

        return ans;
    };
}
