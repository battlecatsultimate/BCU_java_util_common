package common.battle.data;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

import common.CommonStatic;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.system.files.VFile;
import common.util.Data;
import common.util.anim.ImgCut;
import common.util.unit.Form;
import common.util.unit.Unit;
import common.util.unit.UnitStore;

public class Orb extends Data {
	//Available data for atk/res orb, will be used for GUI
	//Map<Trait, Grades>
	public static final Map<Integer, List<Integer>> ATKORB = new TreeMap<Integer, List<Integer>>();
	public static final Map<Integer, List<Integer>> RESORB = new TreeMap<Integer, List<Integer>>();
	public static final Map<Integer, Integer> DATA = new HashMap<>();
	
	public static FakeImage[] TYPES;
	public static FakeImage[] TRAITS;
	public static FakeImage[] GRADES;
	
	public static void read() {
		Queue<String> traitData = VFile.readLine("./org/data/equipment_attribute.csv");
		
		int key = 0;
		
		for(String line : traitData) {
			if(line == null || line.startsWith("//") || line.isEmpty())
				continue;
			
			String [] strs = line.trim().split(",");
			
			int value = 0;
			
			for(int i = 0; i < strs.length; i++) {
				int t = CommonStatic.parseIntN(strs[i]);
				
				if(t == 1)
					value |= getTrait(i);
			}
			
			DATA.put(key, value);
			
			key++;
		}
		
		String data = new String(VFile.get("./org/data/equipmentlist.json").getData().getBytes(), StandardCharsets.UTF_8);
		
		JSONObject jdata = new JSONObject(data);
		
		JSONArray lists = jdata.getJSONArray("ID");
		
		for(int i = 0; i < lists.length(); i++) {
			if(!(lists.get(i) instanceof JSONObject)) {
				continue;
			}
			
			JSONObject obj = (JSONObject) lists.get(i);
			
			int trait = obj.getInt("attribute");
			int type = obj.getInt("content");
			int grade = obj.getInt("gradeID");
			
			if(type == ORB_ATK) {
				if(ATKORB.get(DATA.get(trait)) == null) {
					List<Integer> grades = new ArrayList<Integer>();
					
					grades.add(grade);
					
					ATKORB.put(DATA.get(trait), grades);
				} else {
					List<Integer> grades = ATKORB.get(DATA.get(trait));
					
					if(!grades.contains(grade)) {
						grades.add(grade);
					}
					
					ATKORB.put(DATA.get(trait), grades);
				}
			} else {
				if(RESORB.get(DATA.get(trait)) == null) {
					List<Integer> grades = new ArrayList<Integer>();
					
					grades.add(grade);
					
					RESORB.put(DATA.get(trait), grades);
				} else {
					List<Integer> grades = RESORB.get(DATA.get(trait));
					
					if(!grades.contains(grade)) {
						grades.add(grade);
					}
					
					RESORB.put(DATA.get(trait), grades);
				}
			}
		}
		
		Queue<String> units = VFile.readLine("./org/data/equipmentslot.csv");
		
		for(String line : units) {
			if(line == null || line.startsWith("//") || line.isEmpty()) {
				continue;
			}
			
			String [] strs = line.trim().split(",");
			
			if(strs.length != 2) {
				continue;
			}
			
			int id = CommonStatic.parseIntN(strs[0]);
			int slots = CommonStatic.parseIntN(strs[1]);
			
			Unit u = UnitStore.get(id, false);
			
			if(u == null || u.forms.length != 3) {
				continue;
			}
			
			Form f = u.forms[2];
			
			if(f == null) {
				continue;
			}
			
			f.orbs = new Orb(slots);
		}
		
		String pre = "./org/page/orb/equipment_";
		
		VImg type = new VImg(pre+"effect.png");
		ImgCut it = ImgCut.newIns(pre+"effect.imgcut");
		
		TYPES = it.cut(type.getImg());
		
		VImg trait = new VImg(pre+"attribute.png");
		ImgCut itr = ImgCut.newIns(pre+"attribute.imgcut");
		
		TRAITS = itr.cut(trait.getImg());
		
		VImg grade = new VImg(pre+"grade.png");
		ImgCut ig = ImgCut.newIns(pre+"grade.imgcut");
		
		GRADES = ig.cut(grade.getImg());
	}
	
	public Orb(int slots) {
		this.slots = slots;
	}
	
	private final int slots;
	
	public int getAtk(int grade, MaskAtk atk) {
		return ORB_ATK_MULTI[grade] * atk.getAtk() / 100;
	}
	
	public int getRes(int grade, MaskAtk atk) {
		return - ORB_RES_MULTI[grade] * atk.getAtk() / 100;
	}
	
	public int getSlots() {
		return slots;
	}
	
	public static int getTrait(int trait) {
		switch(trait) {
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
	
	public static int reverse(int value) {
		for(int n : DATA.keySet()) {
			if(DATA.get(n) == value)
				return n;
		}
		
		return -1;
	}
}
