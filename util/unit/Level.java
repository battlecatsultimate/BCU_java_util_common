package common.util.unit;

import common.util.BattleStatic;

public class Level implements BattleStatic {
	public Level(int [] lvs) {
		if(lvs.length == 6) {
			this.lvs = lvs;
		}
	}
	
	public Level(int [] lvs, int[][] orbs) {
		if(lvs.length == 6) {
			this.lvs = lvs;
		}
		
		if(orbs == null) {
			return;
		}
		
		boolean valid = true;
		
		for(int[] data : orbs) {
			if(data.length != 3) {
				valid = false;
			}
		}
		
		if(valid) {
			this.orbs = orbs;
		}
	}
	
	public Level clone() {
		return new Level(lvs.clone(), orbs.clone());
	}
	
	public int[] lvs = { 1, 0, 0, 0, 0, 0};
	public int[][] orbs = null;
}
