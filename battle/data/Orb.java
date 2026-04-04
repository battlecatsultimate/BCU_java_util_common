package common.battle.data;

import common.CommonStatic;
import common.CommonStatic.BCAuxAssets;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.pack.Context;
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
import java.util.*;

@JsonClass
public class Orb extends Data {

	public static final int[] orbTrait = { // the 12 connects to the "ability orb" sprite (since no targets)
			Data.TRAIT_RED, Data.TRAIT_FLOAT, Data.TRAIT_BLACK, Data.TRAIT_METAL, Data.TRAIT_ANGEL, Data.TRAIT_ALIEN,
			Data.TRAIT_ZOMBIE, Data.TRAIT_RELIC, Data.TRAIT_WHITE, Data.TRAIT_EVA, Data.TRAIT_WITCH, Data.TRAIT_DEMON, 12
	};

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
					if (i >= orbTrait.length) {
						CommonStatic.ctx.printErr(Context.ErrType.WARN, "unknown orb trait line(s): index " + i);
						break;
					}
					if (CommonStatic.parseIntN(strs[i]) == 1)
						value |= 1 << orbTrait[i];
				}

				aux.DATA.put(key, value);
				key++;
			}

			String data = new String(VFile.get("./org/data/equipmentlist.json").getData().getBytes(), StandardCharsets.UTF_8);

			JSONObject jdata = new JSONObject(data);
			JSONArray lists = jdata.getJSONArray("ID");

			for (int i = 0; i < lists.length(); i++) {
				if (!(lists.get(i) instanceof JSONObject)) {
					continue;
				}

				JSONObject obj = (JSONObject) lists.get(i);

				int orbID = obj.getInt("content"); // main orb type
				int grade = obj.getInt("gradeID"); // grade type D to S
				int trait = obj.has("attribute") ? obj.getInt("attribute") : 12; // trait target

				Map<Integer, List<Integer>> orbData;

				if(aux.ORB.containsKey(orbID))
					orbData = aux.ORB.get(orbID);
				else
					orbData = new TreeMap<>();

				List<Integer> grades;

				if(orbData.containsKey(aux.DATA.get(trait))) {
					grades = orbData.get(aux.DATA.get(trait));
				} else {
					grades = new ArrayList<>();
				}

				if(!grades.contains(grade)) {
					grades.add(grade);
				}

				orbData.put(aux.DATA.get(trait), grades);
				aux.ORB.put(orbID, orbData);
			}

			Queue<String> units = VFile.readLine("./org/data/equipmentslot.csv");

			for (String line : units) {
				if (line == null || line.startsWith("//") || line.isEmpty())
					continue;
				String[] strs = line.trim().split(",");
				if (strs.length != 2 && strs.length != 2 + CommonStatic.parseIntN(strs[1]))
					continue;

				int id = CommonStatic.parseIntN(strs[0]);
				int slots = CommonStatic.parseIntN(strs[1]);
				Unit u = Identifier.parseInt(id, Unit.class).get();
				if (u == null || u.forms.length < 3)
					continue;

				Form f = u.forms[2];
				if (f == null)
					continue;

				if (strs.length == 2) {
					for (int i = 0; i < slots; i++)
						f.unit.orbs.add(new Orb(2, 0));
				} else {
					for (int i = 0; i < slots; i++) {
						int limitId = CommonStatic.parseIntN(strs[2 + i]);
						int minForm = limitId >= 0 ? 2 : 0;
						int minLv = limitId >= 1 ? 60 : 0;
						f.unit.orbs.add(new Orb(minForm, minLv));
					}
				}
			}

			String pre = "./org/page/orb/equipment_";
			VImg type = new VImg(pre + "effect.png");
			aux.TYPES[0] = ImgCut.newIns(pre + "effect.imgcut").cut(type.getImg());
			VImg typeS = new VImg(pre + "effect_s.png");
			aux.TYPES[1] = ImgCut.newIns(pre + "effect_s.imgcut").cut(typeS.getImg());

			VImg trait = new VImg(pre + "attribute.png");
			aux.TRAITS[0] = ImgCut.newIns(pre + "attribute.imgcut").cut(trait.getImg());
			VImg traitS = new VImg(pre + "attribute_s.png");
			aux.TRAITS[1] = ImgCut.newIns(pre + "attribute_s.imgcut").cut(traitS.getImg());

			VImg grade = new VImg(pre + "grade.png");
			aux.GRADES[0] = ImgCut.newIns(pre + "grade.imgcut").cut(grade.getImg());
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

	public static int traitToOrb(int trait) {
		for(int i = 0; i < orbTrait.length; i++) {
			if(orbTrait[i] == trait)
				return 1 << i;
		}

		return -1;
	}

	@JsonField
	public int minForm;
	@JsonField
	public int minLv;

	@JsonClass.JCConstructor
	public Orb() {

	}

	public Orb(int minimumForm, int minimumLv) { // used for data
		minForm = minimumForm;
		minLv = minimumLv;
	}

	public boolean isRestricted(int formId, int lv) {
		return formId < minForm || lv < minLv;
	}

	@Override
	public String toString() {
		return "form " + minForm + ", lv " + minLv;
	}
}
