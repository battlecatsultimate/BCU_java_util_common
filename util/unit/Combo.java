package common.util.unit;

import common.CommonStatic;
import common.io.json.JsonClass;
import common.io.json.JsonDecoder;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.pack.IndexContainer;
import common.pack.PackData;
import common.pack.UserProfile;
import common.system.files.VFile;
import common.util.Data;
import common.util.lang.MultiLangCont;
import common.util.stage.CharaGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

@IndexContainer.IndexCont(PackData.class)
@JsonClass.JCGeneric(Identifier.class)
@JsonClass
public class Combo extends Data implements IndexContainer.Indexable<IndexContainer, Combo> {

    public static void readFile() {
        CommonStatic.BCAuxAssets aux = CommonStatic.getBCAssets();
        PackData.DefPack data = UserProfile.getBCData();
        Queue<String> qs = VFile.readLine("./org/data/NyancomboData.csv");

        if (qs == null) {
            System.out.println("W/Combo::readFile - \"./org/data/NyancomboData.csv\" file hasn't been found");
            return;
        }

        int i = 0;

        for (String str : qs) {
            if (str.length() < 20)
                continue;

            String[] strs = str.trim().split(",");
            Combo c = new Combo(Identifier.parseInt(i++, Combo.class), strs);
            if (c.show > 0)
                data.combos.add(c);
        }

        qs = VFile.readLine("./org/data/NyancomboParam.tsv");

        if (qs == null) {
            System.out.println("W/Combo::readFile - \"./org/data/NyancomboParam.tsv\" file hasn't been found");
            return;
        }

        for (i = 0; i < C_TOT; i++) {
            String line = qs.poll();
            if (line == null)
                continue;

            String[] strs = line.trim().split("\t");
            if (strs.length < 5)
                continue;

            for (int j = 0; j < Math.min(strs.length, 6); j++) {
                aux.values[i][j] = Integer.parseInt(strs[j]);
            }
        }

        qs = VFile.readLine("./org/data/NyancomboFilter.tsv");
        if (qs == null) {
            System.out.println("W/Combo::readFile - \"./org/data/NyancomboFilter.tsv\" file hasn't been found");
            return;
        }

        aux.filter = new int[qs.size()][];

        for (i = 0; i < aux.filter.length; i++) {
            String line = qs.poll();
            if (line == null)
                continue;

            String[] strs = line.trim().split("\t");
            aux.filter[i] = new int[strs.length];
            for (int j = 0; j < strs.length; j++)
                aux.filter[i][j] = Integer.parseInt(strs[j]);
        }
    }

    @JsonClass.JCIdentifier
    @JsonField
    public Identifier<Combo> id;

    @JsonField
    public int lv, show, type;

    @JsonField(alias = Form.FormJson.class)
    public Form[] forms;

    @JsonField(alias = Identifier.class)
    public CharaGroup group;

    @JsonField
    public String name;

    @JsonClass.JCConstructor
    public Combo() {
        id = null;
    }

    protected Combo(Identifier<Combo> ID, String[] strs) {
        id = ID;
        name = strs[0];
        show = Integer.parseInt(strs[1]);

        int characterGroupID = Integer.parseInt(strs[2]);

        if (characterGroupID != -1) {
            CharaGroup g = UserProfile.getBCData().groups.getRaw(characterGroupID);

            if (g != null) {
                group = g;
            } else {
                System.out.println("W/Combo::constructor - Found chara group ID of " + characterGroupID + ", but no such group found in data");
            }
        }

        int n;

        for (n = 0; n < 5; n++)
            if (Integer.parseInt(strs[3 + n * 2]) == -1)
                break;

        forms = new Form[n];

        for (int i = 0; i < n; i++) {
            Identifier<Unit> u = Identifier.parseInt(Integer.parseInt(strs[3 + i * 2]), Unit.class);

            forms[i] = u.get().forms[Integer.parseInt(strs[4 + i * 2])];
        }

        type = Integer.parseInt(strs[13]);
        lv = Integer.parseInt(strs[14]);
    }

    public Combo(Identifier<Combo> ID, String n, int l, int t, int s, Form f) {
        id = ID;
        name = n;
        lv = l;
        type = t;
        show = s;
        forms = new Form[] { f };
    }

    @Override
    public String toString() {
        return Data.trio(id.id) + " - " + getName();
    }

    @Override
    public Identifier<Combo> getID() {
        return id;
    }

    public String getName() {
        String n = MultiLangCont.get(this);

        if (n != null && !n.isEmpty())
            return n;
        else if (name != null && !name.isEmpty())
            return name;
        else
            return null;
    }

    public void setType(int t) {
        type = t;
    }

    public void setLv(int l) {
        lv = l;
    }

    public void addForm(Form f) {
        forms = Arrays.copyOf(forms, forms.length + 1);
        forms[forms.length - 1] = f;
    }

    public void removeForm(int index) {
        Form[] formSrc = new Form[forms.length - 1];
        for (int i = 0, j = 0; i < forms.length; i++) {
            if (i != index)
                formSrc[j++] = forms[i];
        }
        forms = formSrc;
    }

    public boolean checkCharaGroup(Unit u) {
        if (group == null)
            return true;
        else if (group.type == 0) // includes
            return group.set.contains(u);
        else if (group.type == 2) // excludes
            return !group.set.contains(u);
        else
            return true;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    @JsonDecoder.OnInjected
    public void onInjected() {
        boolean broken = false;

        for(int i = 0; i < forms.length; i++) {
            if(forms[i] == null) {
                broken = true;
                break;
            }
        }

        if(broken) {
            List<Form> f = new ArrayList<>();

            for(int i = 0; i < forms.length; i++) {
                if(forms[i] != null)
                    f.add(forms[i]);
            }

            forms = f.toArray(new Form[0]);
        }
    }
}