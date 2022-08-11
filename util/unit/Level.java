package common.util.unit;

import common.io.json.JsonClass;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonField;
import common.util.BattleStatic;

import java.util.ArrayList;

@JsonClass(noTag = NoTag.LOAD)
public class Level implements BattleStatic {

	@JsonField(generic = Integer.class)
	private ArrayList<Integer> lvs = new ArrayList<>();

	private int[][] orbs = null;

	public static ArrayList<Integer> LvList(int[] arr) {
		ArrayList<Integer> list = new ArrayList<>();
		for (int lv : arr)
			list.add(lv);

		return list;
	}

	public Level() {
		lvs.add(1);
	}

	public Level(ArrayList<Integer> lvs) {
		if (lvs.size() >= 1) {
			this.lvs = lvs;
		} else {
			this.lvs = new ArrayList<>();
			lvs.add(1);
		}
	}

	public Level(ArrayList<Integer> lvs, int[][] orbs) {
		this(lvs);

		if (orbs == null) {
			return;
		}

		boolean valid = true;

		for (int[] data : orbs) {
			if (data == null) {
				valid = false;
				break;
			}

			if (data.length == 0) {
				continue;
			}

			if (data.length != 3) {
				valid = false;
				break;
			}
		}

		if (valid) {
			this.orbs = orbs;
		}
	}

	@Override
	public Level clone() {
		if (orbs != null)
			return new Level(new ArrayList<>(lvs), orbs.clone());

		return new Level(new ArrayList<>(lvs));
	}

	public int getLv() {
		return lvs.get(0);
	}
	public void setLv(int lv) {
		lvs.set(0, lv);
	}
	public ArrayList<Integer> getLvs() {
		return lvs;
	}

	public int[][] getOrbs() {
		return orbs;
	}

	public void setLvs(ArrayList<Integer> lv) {
		if (lv.size() >= 1)
			lvs = lv;
	}

	public void setOrbs(int[][] orb) {
		if (orb == null) {
			orbs = null;
			return;
		}

		boolean valid = true;

		for (int[] data : orb) {
			if (data == null) {
				valid = false;
				break;
			}

			if (data.length == 0) {
				continue;
			}

			if (data.length != 3) {
				valid = false;
				break;
			}
		}

		if (valid) {
			orbs = orb;
		}
	}
}