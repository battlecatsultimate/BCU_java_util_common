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
    @JsonField(generic = {Lang.Locale.class, String.class})
    private final LinkedHashMap<Lang.Locale, String> dat = new LinkedHashMap<>();

    @JsonClass.JCConstructor
    public MultiLangData() {
    }

    public MultiLangData(String str) {
        dat.put(lang(), str);
    }

    public void put(String data) {
        Lang.Locale lang = lang();

        if (data != null && !data.isEmpty())
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
        Lang.Locale lang = lang();

        if (dat.containsKey(lang)) {
            String temp = dat.get(lang);

            if(temp != null)
                return temp;
        }

        Lang.Locale[] locales = Lang.Locale.values();

        for (int i = 1; i < locales.length; i++) {
            Lang.Locale[] preferences = Lang.pref[lang.ordinal()];

            if (i < preferences.length) {
                if (dat.containsKey(preferences[i])) {
                    String temp = dat.get(preferences[i]);

                    if (temp != null)
                        return temp;
                }
            } else if (dat.containsKey(locales[i])) {
                String temp = dat.get(locales[i]);

                if(temp != null)
                    return temp;
            }
        }
        return "";
    }

    public Lang.Locale getGrabbedLocale() {
        Lang.Locale lang = lang();

        Lang.Locale[] locales = Lang.Locale.values();

        for (int i = 1; i < Lang.Locale.values().length; i++) {
            Lang.Locale[] preferences = Lang.pref[lang.ordinal()];

            if (i < preferences.length) {
                if (dat.containsKey(preferences[i])) {
                    String temp = dat.get(preferences[i]);

                    if (temp != null)
                        return preferences[i];
                }
            } else if (dat.containsKey(locales[i])) {
                String temp = dat.get(locales[i]);

                if(temp != null)
                    return locales[i];
            }
        }

        return Lang.Locale.EN;
    }

    private static Lang.Locale lang() {
        return CommonStatic.getConfig().lang;
    }

    public MultiLangData copy() { //Makes a copy of this MultiLangData object
        MultiLangData ans = new MultiLangData();
        for (Lang.Locale lang : dat.keySet())
            ans.dat.put(lang, dat.get(lang));

        return ans;
    }
}
