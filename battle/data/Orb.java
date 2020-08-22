package common.battle.data;

import common.CommonStatic;
import common.CommonStatic.BCAuxAssets;
import common.pack.Identifier;
import common.system.VImg;
import common.system.files.VFile;
import common.util.Data;
import common.util.anim.ImgCut;
import common.util.unit.Form;
import common.util.unit.Unit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Orb extends Data {

    public static int getTrait(int trait) {
        switch (trait) {
            case 0:
                return TB_RED;
            case 1:
                return TB_FLOAT;
            case 2:
                return TB_BLACK;
            case 3:
                return TB_METAL;
            case 4:
                return TB_ANGEL;
            case 5:
                return TB_ALIEN;
            case 6:
                return TB_ZOMBIE;
            case 7:
                return TB_RELIC;
            case 8:
                return TB_WHITE;
            default:
                return 0;
        }
    }

    public static void read() {
        BCAuxAssets aux = CommonStatic.getBCAssets();
        try {
            Queue<String> traitData = VFile.readLine("./org/data/equipment_attribute.csv");
            int key = 0;
            for (String line : traitData) {
                if (line == null || line.startsWith("//") || line.isEmpty())
                    continue;
                String[] strs = line.trim().split(",");
                int value = 0;
                for (int i = 0; i < strs.length; i++) {
                    int t = CommonStatic.parseIntN(strs[i]);
                    if (t == 1)
                        value |= getTrait(i);
                }
                aux.DATA.put(key, value);
                key++;
            }
            String data = new String(VFile.get("./org/data/equipmentlist.json").getData().getBytes(),
                    StandardCharsets.UTF_8);
            JSONObject jdata = new JSONObject(data);
            JSONArray lists = jdata.getJSONArray("ID");
            for (int i = 0; i < lists.length(); i++) {
                if (!(lists.get(i) instanceof JSONObject)) {
                    continue;
                }
                JSONObject obj = (JSONObject) lists.get(i);
                int trait = obj.getInt("attribute");
                int type = obj.getInt("content");
                int grade = obj.getInt("gradeID");
                if (type == ORB_ATK) {
                    if (aux.ATKORB.get(aux.DATA.get(trait)) == null) {
                        List<Integer> grades = new ArrayList<>();
                        grades.add(grade);
                        aux.ATKORB.put(aux.DATA.get(trait), grades);
                    } else {
                        List<Integer> grades = aux.ATKORB.get(aux.DATA.get(trait));
                        if (grades != null && !grades.contains(grade)) {
                            grades.add(grade);
                        }
                        aux.ATKORB.put(aux.DATA.get(trait), grades);
                    }
                } else {
                    if (aux.RESORB.get(aux.DATA.get(trait)) == null) {
                        List<Integer> grades = new ArrayList<>();
                        grades.add(grade);
                        aux.RESORB.put(aux.DATA.get(trait), grades);
                    } else {
                        List<Integer> grades = aux.RESORB.get(aux.DATA.get(trait));
                        if (grades != null && !grades.contains(grade)) {
                            grades.add(grade);
                        }
                        aux.RESORB.put(aux.DATA.get(trait), grades);
                    }
                }
            }
            Queue<String> units = VFile.readLine("./org/data/equipmentslot.csv");
            for (String line : units) {
                if (line == null || line.startsWith("//") || line.isEmpty()) {
                    continue;
                }
                String[] strs = line.trim().split(",");
                if (strs.length != 2) {
                    continue;
                }
                int id = CommonStatic.parseIntN(strs[0]);
                int slots = CommonStatic.parseIntN(strs[1]);
                Unit u = Identifier.parseInt(id, Unit.class).get();
                if (u == null || u.forms.length != 3) {
                    continue;
                }
                Form f = u.forms[2];
                if (f == null) {
                    continue;
                }
                f.orbs = new Orb(slots);
            }
            String pre = "./org/page/orb/equipment_";
            VImg type = new VImg(pre + "effect.png");
            ImgCut it = ImgCut.newIns(pre + "effect.imgcut");
            aux.TYPES = it.cut(type.getImg());
            VImg trait = new VImg(pre + "attribute.png");
            ImgCut itr = ImgCut.newIns(pre + "attribute.imgcut");
            aux.TRAITS = itr.cut(trait.getImg());
            VImg grade = new VImg(pre + "grade.png");
            ImgCut ig = ImgCut.newIns(pre + "grade.imgcut");
            aux.GRADES = ig.cut(grade.getImg());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static int reverse(int value) {
        Map<Integer, Integer> DATA = CommonStatic.getBCAssets().DATA;
        for (int n : DATA.keySet()) {
            int v = DATA.get(n);
            if (DATA.get(n) != null && v == value)
                return n;
        }
        return -1;
    }

    private final int slots;

    public Orb(int slots) {
        this.slots = slots;
    }

    public int getAtk(int grade, MaskAtk atk) {
        return ORB_ATK_MULTI[grade] * atk.getAtk() / 100;
    }

    public int getRes(int grade, MaskAtk atk) {
        return -ORB_RES_MULTI[grade] * atk.getAtk() / 100;
    }

    public int getSlots() {
        return slots;
    }
}
