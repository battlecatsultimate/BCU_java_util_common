package common.util.unit;

import common.util.BattleStatic;

public class Level implements BattleStatic {
	public Level(int [] lvs) {
		if(lvs.length == 6) {
			this.lvs = lvs;
		} else {
			this.lvs = new int[] {1, 0, 0, 0, 0, 0};
		}
	}
	
	public Level(int [] lvs, int[][] orbs) {
		if(lvs.length == 6) {
			this.lvs = lvs;
		} else {
			this.lvs = new int[] {1, 0, 0, 0, 0, 0};
		}
		
		if(orbs == null) {
			return;
		}
		
		boolean valid = true;
		
		for(int[] data : orbs) {
			if(data == null) {
				valid = false;
				break;
			}
			
			if(data.length == 0) {
				continue;
			}
			
			if(data.length != 3) {
				valid = false;
				break;
			}
		}
		
		if(valid) {
			this.orbs = orbs;
		}
	}
	
	public Level clone() {
		if(orbs != null)
			return new Level(lvs.clone(), orbs.clone());
		
		return new Level(lvs.clone());
	}
	
	private int[] lvs = { 1, 0, 0, 0, 0, 0};
	private int[][] orbs = null;
	
	public int[] getLvs() { 
		return lvs;
	}
	
	public int[][] getOrbs() {
		return orbs;
	}
	
	public void setLvs(int [] lv) {
		if(lv.length == 6) {
			lvs = lv;
		}
	}
	
	public void setOrbs(int[][] orb) {
		if(orb == null) {
			orbs = orb;
			return;
		}
		
		boolean valid = true;
		
		for(int[] data : orb) {
			if(data == null) {
				valid = false;
				break;
			}
			
			if(data.length == 0) {
				continue;
			}
			
			if(data.length != 3) {
				valid = false;
				break;
			}
		}
		
		if(valid) {
			orbs = orb;
		}
	}
}
