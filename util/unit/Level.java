package common.util.unit;

import common.battle.data.PCoin;
import common.io.json.JsonClass;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonField;
import common.util.BattleStatic;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("unused")
@JsonClass(noTag = NoTag.LOAD)
public class Level implements BattleStatic, LevelInterface {
	private int level, plusLevel;
	@Nonnull
	private int[] talents;

	private int[][] orbs = null;

	public static Level lvList(Unit u, int[] arr, int[][] orbs) {
		int talentNumber = 0;

		for(Form f : u.forms) {
			PCoin pc = f.du.getPCoin();

			if(pc != null) {
				talentNumber = Math.max(talentNumber, pc.max.length);
			}
		}

		Level lv = new Level(talentNumber);

		lv.level = Math.max(1, Math.min(arr[0], u.max));
		lv.plusLevel = Math.max(0, Math.min(arr[1], u.maxp));

		int[] talents = new int[arr.length - 2];
		System.arraycopy(arr, 2, talents, 0, arr.length - 2);

		lv.talents = talents;

		lv.orbs = orbs;

		return lv;
	}

	@JsonClass.JCConstructor
	public Level() {
		level = 1;
		talents = new int[0];
	}

	public Level(int talentNumber) {
		level = 1;
		talents = new int[talentNumber];
	}

	public Level(int level, int plusLevel, @Nonnull int[] talents) {
		this.level = Math.max(1, level);
		this.plusLevel = plusLevel;

		this.talents = talents.clone();
	}

	public Level(int level, int plusLevel, @Nonnull int[] talents, int[][] orbs) {
		this(level, plusLevel, talents);

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
		try {
			return (Level) super.clone();
		} catch (CloneNotSupportedException ignored) {
			if (orbs != null)
				return new Level(level, plusLevel, talents, orbs.clone());

			return new Level(level, plusLevel, talents);
		}
	}

	public int getLv() {
		return level;
	}

	public int getPlusLv() {
		return plusLevel;
	}

	public int[][] getOrbs() {
		return orbs;
	}

	public int[] getTalents() {
		return talents;
	}

	public void setLevel(int lv) {
		level = lv;
	}

	public void setPlusLevel(int plusLevel) {
		this.plusLevel = plusLevel;
	}

	public void setTalents(@Nonnull int[] talents) {
		this.talents = talents.clone();
	}

	public void setLvs(Level lv) {
		System.out.println("Setting level : " + lv);

		level = Math.max(1, lv.level);
		plusLevel = lv.plusLevel;

		if(lv.talents.length < talents.length) {
			System.arraycopy(lv.talents, 0, talents, 0, lv.talents.length);
		} else {
			talents = lv.talents.clone();
		}

		if (lv.orbs != null) {
			orbs = lv.orbs.clone();
		}

		System.out.println("Setting result : " + this);
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

	@JsonField(tag = "lvs", io = JsonField.IOType.R, generic = Integer.class)
	public void parseOldLevel(ArrayList<Integer> levels) {
		if (levels.size() > 0) {
			level = levels.get(0);
		}

		if (levels.size() > 1) {
			talents = new int[levels.size() - 1];

			for (int i = 0; i < talents.length; i++) {
				talents[i] = levels.get(i + 1);
			}
		}
	}

	@Override
	public String toString() {
		return "Level{" +
				"level=" + level +
				", plusLevel=" + plusLevel +
				", talents=" + Arrays.toString(talents) +
				", orbs=" + Arrays.deepToString(orbs) +
				'}';
	}
}