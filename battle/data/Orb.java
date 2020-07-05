package common.battle.data;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.json.JSONArray;
import org.json.JSONObject;

import common.CommonStatic;
import common.system.files.VFile;
import common.util.Data;
import common.util.unit.Form;
import common.util.unit.Unit;
import common.util.unit.UnitStore;

public class Orb extends Data {
	//Available data for atk/res orb, will be used for GUI
	//Map<Trait, Grades>
	public static final Map<Integer, List<Integer>> ATKORB = new HashMap<Integer, List<Integer>>();
	public static final Map<Integer, List<Integer>> RESORB = new HashMap<Integer, List<Integer>>();
	
	public static void read() {
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
				if(ATKORB.get(trait) == null) {
					List<Integer> grades = new ArrayList<Integer>();
					
					grades.add(grade);
					
					ATKORB.put(trait, grades);
				} else {
					List<Integer> grades = ATKORB.get(trait);
					
					if(!grades.contains(grade)) {
						grades.add(grade);
					}
					
					ATKORB.put(trait, grades);
				}
			} else {
				if(RESORB.get(trait) == null) {
					List<Integer> grades = new ArrayList<Integer>();
					
					grades.add(grade);
					
					RESORB.put(trait, grades);
				} else {
					List<Integer> grades = RESORB.get(trait);
					
					if(!grades.contains(grade)) {
						grades.add(grade);
					}
					
					RESORB.put(trait, grades);
				}
			}
		}
		
		Queue<String> units = VFile.readLine("./org/data/equipmentslot.csv");
		
		for(String line : units) {
			if(line.startsWith("//") || line.isEmpty()) {
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
}
