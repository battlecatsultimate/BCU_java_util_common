package common.battle.data;

import common.CommonStatic;
import common.pack.Context.ErrType;
import common.pack.Identifier;
import common.system.files.VFile;
import common.util.Data;
import common.util.Data.Proc.ProcItem;
import common.util.unit.Unit;

import java.util.Queue;

public class PCoin extends Data {
	public static final int FLAG_RELIC_SLOW = 69;
	public static final int FLAG_RELIC_WEAK = 70;
	public static final int FLAG_ZOMBIE_KB = 73;
	public static final int FLAG_METAL_SLOW = 72;

	public static void read() {
		Queue<String> qs = VFile.readLine("./org/data/SkillAcquisition.csv");
		qs.poll();
		for (String str : qs) {
			String[] strs = str.trim().split(",");

			if (strs.length == 67)
				new PCoin(strs);
		}
	}

	private final int id;
	private final DataUnit du;

	public final DataUnit full;
	public final int[] max;
	public final int[][] info = new int[5][13];

	private PCoin(String[] strs) {
		id = CommonStatic.parseIntN(strs[0]);
		max = new int[6];

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 13; j++)
				info[i][j] = CommonStatic.parseIntN(strs[2 + i * 13 + j]);
			max[i + 1] = info[i][1];
			if (max[i + 1] == 0)
				max[i + 1] = 1;
		}
		du = (DataUnit) Identifier.parseInt(id, Unit.class).get().forms[2].du;
		du.pcoin = this;
		full = improve(max);
	}

	@SuppressWarnings("deprecation")
	public DataUnit improve(int[] lvs) {
		DataUnit ans = du.clone();
		for (int i = 0; i < 5; i++) {
			if (lvs[i + 1] == 0)
				continue;
			if (info[i][0] >= PC_CORRES.length) {
				CommonStatic.ctx.printErr(ErrType.NEW, "new PCoin ability not yet handled by BCU: " + info[i][0]+"\nText ID is "+info[i][10]);
				continue;
			}
			int[] type = PC_CORRES[info[i][0]];
			if (type[0] == -1) {
				CommonStatic.ctx.printErr(ErrType.NEW, "new PCoin ability not yet handled by BCU: " + info[i][0]+"\nText ID is "+info[i][10]);
				continue;
			}

			int maxlv = info[i][1];
			int[] modifs = new int[4];
			if (maxlv > 1) {
				for (int j = 0; j < 4; j++) {
					int v0 = info[i][2 + j * 2];
					int v1 = info[i][3 + j * 2];
					modifs[j] = (v1 - v0) * (lvs[i + 1] - 1) / (maxlv - 1) + v0;
				}
			}
			if (maxlv == 0)
				for (int j = 0; j < 4; j++)
					modifs[j] = info[i][3 + j * 2];

			if (type[0] == PC_P) {
				ProcItem tar = ans.proc.getArr(type[1]);

				if(info[i][10] == FLAG_RELIC_SLOW || info[i][10] == FLAG_RELIC_WEAK) {
					ans.type |= TB_RELIC;
				}

				if(info[i][10] == FLAG_ZOMBIE_KB) {
					ans.type |= TB_ZOMBIE;
				}

				if(info[i][10] == FLAG_METAL_SLOW) {
					ans.type |= TB_METAL;
				}

				if(type[1] == P_VOLC) {
					tar.set(0, modifs[0]);
					tar.set(1, modifs[2] / 4);
					tar.set(2, (modifs[2] + modifs[3]) / 4);
					tar.set(3, modifs[1] * 20);
				} else {
					for (int j = 0; j < 4; j++)
						if (modifs[j] > 0)
							tar.set(j, tar.get(j) + modifs[j]);
				}
				if (type[1] == P_STRONG && modifs[0] != 0)
					tar.set(0, 100 - tar.get(0));
				if (type[1] == P_WEAK)
					tar.set(2, 100 - tar.get(2));
			} else if (type[0] == PC_AB)
				ans.abi |= type[1];
			else if (type[0] == PC_BASE)
				if (type[1] == PC2_HP)
					ans.hp *= 1 + modifs[0] * 0.01;
				else if (type[1] == PC2_ATK) {
					double atk = 1 + modifs[0] * 0.01;
					ans.atk *= atk;
					ans.atk1 *= atk;
					ans.atk2 *= atk;
				} else if (type[1] == PC2_SPEED)
					ans.speed += modifs[0];
				else if (type[1] == PC2_CD)
					ans.respawn -= modifs[0];
				else if (type[1] == PC2_COST)
					ans.price -= modifs[0];
				else
					;
			else if (type[0] == PC_IMU)
				ans.proc.getArr(type[1]).set(0, 100);
			else if (type[0] == PC_TRAIT)
				ans.type |= type[1];

		}
		return ans;
	}

	public boolean flagRelic(int index) {
		if(index < 0 || index >= info.length)
			return false;

		return info[index][10] == FLAG_RELIC_SLOW || info[index][10] == FLAG_RELIC_WEAK;
	}

	public boolean flagZombie(int index) {
		if(index < 0 || index >= info.length)
			return false;

		return info[index][10] == FLAG_ZOMBIE_KB;
	}

	public boolean flagMetal(int index) {
		if(index < 0 || index >= info.length)
			return false;

		return info[index][10] == FLAG_METAL_SLOW;
	}

	public boolean isRelicSlow(int index) {
		if(index < 0 || index >= info.length)
			return false;

		return info[index][10] == FLAG_RELIC_SLOW && info[index][0] == 3;
	}

	public boolean isRelicWeak(int index) {
		if(index < 0 || index >= info.length)
			return false;

		return info[index][10] == FLAG_RELIC_WEAK && info[index][0] == 1;
	}

	public boolean isZombieKB(int index) {
		if(index < 0 || index >= info.length)
			return false;

		return info[index][10] == FLAG_ZOMBIE_KB && info[index][0] == 8;
	}
}
