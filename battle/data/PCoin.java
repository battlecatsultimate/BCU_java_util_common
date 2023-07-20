package common.battle.data;

import common.CommonStatic;
import common.io.json.JsonClass;
import common.io.json.JsonDecoder.OnInjected;
import common.io.json.JsonField;
import common.pack.Context.ErrType;
import common.pack.Identifier;
import common.pack.UserProfile;
import common.system.files.VFile;
import common.util.Data;
import common.util.Data.Proc.ProcItem;
import common.util.unit.Trait;
import common.util.unit.Unit;
import org.jcodec.common.tools.MathUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;

@JsonClass(read = JsonClass.RType.FILL)
public class PCoin extends Data {
	public static void read() {
		Queue<String> qs = VFile.readLine("./org/data/SkillAcquisition.csv");

		qs.poll();

		for (String str : qs) {
			String[] strs = str.trim().split(",");

			if (strs.length == 114)
				new PCoin(strs);
		}
	}

	private final MaskUnit du;
	public MaskUnit full = null;
	@JsonField(generic = Trait.class, alias = Identifier.class)
	public ArrayList<Trait> trait = new ArrayList<>();

	@JsonField(block = true)
	public int[] max;
	@JsonField(generic = int[].class)
	public final ArrayList<int[]> info = new ArrayList<>();

	public PCoin(CustomEntity ce) {
		du = (CustomUnit)ce;
		((CustomUnit)du).pcoin = this;
	}

	public PCoin(String[] strs, MaskUnit du) { // leaving reminder that this is unused at least in PC ver.
		trait = Trait.convertType(CommonStatic.parseIntN(strs[1]));

		for (int i = 0; i < 8; i++) {
			if(talentExist(strs, 2 + i * 14)) {
				info.add(new int[14]);

				for (int j = 0; j < 14; j++) {
					int v = CommonStatic.parseIntN(strs[2 + i * 14 + j]);
					info.get(info.size() - 1)[j] = v;
				}
			}
		}

		max = info.stream().mapToInt(i -> Math.max(1, i[1])).toArray();
		this.du = du;

		full = improve(max);
	}

	private PCoin(String[] strs) {
		int id = CommonStatic.parseIntN(strs[0]);
		trait = Trait.convertTalentType(CommonStatic.parseIntN(strs[1]));

		for (int i = 0; i < 8; i++) {
			if(!strs[2 + i * 14].equals("0")) {
				int[] data = new int[14];
				for (int j = 0; j < 14; j++)
					data[j] = CommonStatic.parseIntN(strs[2 + i * 14 + j]);
				if(data[0] >= 0 && data[0] < PC_CORRES.length && PC_CORRES[data[0]][1] == P_MINIWAVE) {
					if(data[6] == 0 && data[7] == 0) {
						data[6] = 20;
						data[7] = 20;
					}
				}
				info.add(data);
			}
		}

		max = info.stream().mapToInt(i -> Math.max(1, i[1])).toArray();
		du = Identifier.parseInt(id, Unit.class).get().forms[2].du;
		((DataUnit) du).pcoin = this;
		full = improve(max);
	}

	public void update() {
		// Apparently, if max is null, since we will update full var anyway
		// we can just re-generate whole array
		if (max == null || max.length < info.size())
			max = info.stream().mapToInt(i -> Math.max(1, i[1])).toArray();

		full = improve(max);
	}

	public void verify() { // TODO: surge
		boolean custom = du instanceof CustomUnit;

		for (int[] data : info) {
			Proc proc = du.getAllProc();
			data[1] = Math.max(data[1], 1);
			int type = Data.PC_CORRES[data[0]][1];
			switch (data[0]) {
				case 0:
					break;
				case 10:
					data[2] = MathUtil.clip(data[2], 0, 99 - proc.getArr(type).get(0));
					data[3] = MathUtil.clip(data[3], data[2], 99 - proc.getArr(type).get(0));
					data[4] = Math.max(data[4], 0);
					data[5] = Math.max(data[5], data[4]);
					break;
				case 25: case 26: case 31: case 32:
					data[2] = Math.max(data[2], 0);
					data[3] = Math.max(data[3], data[2]);
					break;
				case 64:
					data[2] = MathUtil.clip(data[2], 0, 100 - proc.getArr(type).get(1));
					data[3] = MathUtil.clip(data[3], data[2], 100 - proc.getArr(type).get(1));
					data[4] = Math.max(data[4], 0);
					data[5] = Math.max(data[5], data[4]);
					break;
				case 62: case 1:
					data[6] = Math.max(data[6], 0);
					data[7] = Math.max(data[7], data[6]);
				case 2: case 3: case 9: case 17: case 50: case 51: case 56: case 60: case 65:
					data[4] = Math.max(data[4], 0);
					data[5] = Math.max(data[5], data[4]);
				case 8: case 11: case 13: case 15: case 18: case 19: case 20: case 21: case 22: case 24: case 30:
				case 52: case 54: case 58:
					data[2] = MathUtil.clip(data[2], 0, 100 - proc.getArr(type).get(0));
					data[3] = MathUtil.clip(data[3], data[2], 100 - proc.getArr(type).get(0));
					break;
			}
		}
	}

	@SuppressWarnings("deprecation")
	public MaskUnit improve(int[] talents) {
		MaskUnit ans = du.clone();

		int[] temp;

		if (talents.length < max.length) {
			temp = new int[max.length];

			System.arraycopy(talents, 0, temp, 0, talents.length);
			System.arraycopy(max, talents.length, temp, talents.length, max.length - talents.length);
		} else {
			temp = talents.clone();
		}

		talents = temp;

		for (int i = 0; i < info.size(); i++) {
			int[] data = info.get(i);
			if (data[0] >= PC_CORRES.length) {
				CommonStatic.ctx.printErr(ErrType.NEW, "new PCoin ability not yet handled by BCU: " + info.get(i)[0] + "\nText ID is " + info.get(i)[10]+"\nData is "+Arrays.toString(info.get(i)));
				continue;
			}

			int[] type = PC_CORRES[data[0]];
			if (type[0] == -1) {
				CommonStatic.ctx.printErr(ErrType.NEW, "new PCoin ability not yet handled by BCU: " + info.get(i)[0] + "\nText ID is " + info.get(i)[10]+"\nData is "+Arrays.toString(info.get(i)));
				continue;
			}

			if (talents[i] == 0) {
				if (type[0] == PC_TRAIT) {
					Trait types = UserProfile.getBCData().traits.get(type[1]);
					ans.getTraits().remove(types);
				}
				continue;
			}

			//Targettings that come with a talent, such as Hyper Mr's
			if (data[12] > 0 && this.trait.size() > 0 && talents[i] > 0)
				for (Trait t : this.trait)
					if (!ans.getTraits().contains(t))
						ans.getTraits().add(t);

			int maxlv = data[1];

			int[] modifs = new int[4];

			if (maxlv > 1) {
				for (int j = 0; j < 4; j++) {
					int v0 = data[2 + j * 2];
					int v1 = data[3 + j * 2];
					modifs[j] = (v1 - v0) * (talents[i] - 1) / (maxlv - 1) + v0;
				}
			} else
				for (int j = 0; j < 4; j++)
					modifs[j] = data[3 + j * 2];

			if (type[0] == PC_P) {
				ProcItem tar = ans.getProc().getArr(type[1]);

				if (type[1] == P_VOLC || type[1] == P_MINIVOLC) {
					if (du instanceof DataUnit) {
						tar.set(0, modifs[0]);
						tar.set(1, modifs[2] / 4);
						tar.set(2, (modifs[2] + modifs[3]) / 4);
						tar.set(3, modifs[1] * 20);

						if (type[1] == P_MINIVOLC) {
							tar.set(4, 20);
						}
					} else { // TODO: increase core version to change how custom talents handle surge
						tar.set(0, modifs[0]);
						tar.set(1, Math.min(modifs[1], modifs[2]));
						tar.set(2, Math.max(modifs[1], modifs[2]));
						tar.set(3, modifs[3]);
					}
				} else if (type[1] == P_BSTHUNT) {
					tar.set(1, modifs[0]);
					tar.set(2, modifs[1]);
				} else {
					for (int j = 0; j < 4; j++) {
						if (modifs[j] > 0) {
							tar.set(j, tar.get(j) + modifs[j]);
						}
					}
				}

				if (du instanceof DataUnit) {
					if (type[1] == P_STRONG && modifs[0] != 0)
						tar.set(0, 100 - tar.get(0));
					else if (type[1] == P_WEAK)
						tar.set(2, 100 - tar.get(2));
					else if (type[1] == P_BOUNTY)
						tar.set(0, 100);
					else if (type[1] == P_ATKBASE)
						tar.set(0, 300);
				} else if (!((CustomEntity)du).common && !(type[1] == P_STRONG && modifs[0] != 0)) {
					for (AtkDataModel atk : ((CustomEntity)ans).atks) {
						ProcItem atks = atk.proc.getArr(type[1]);

						if (type[1] == P_VOLC) {
							atks.set(0, modifs[0]);
							atks.set(1, Math.min(modifs[1], modifs[2]));
							atks.set(2, Math.max(modifs[1], modifs[2]));
							atks.set(3, modifs[3]);
						} else
							for (int j = 0; j < 4; j++)
								if (modifs[j] > 0)
									atks.set(j, atks.get(j) + modifs[j]);
					}
				}
			} else if (type[0] == PC_AB || type[0] == PC_BASE) {
				if (du instanceof DataUnit)
					improve((DataUnit)ans,type,modifs);
				else
					improve((CustomUnit)ans,type,modifs);
			} else if (type[0] == PC_IMU)
				ans.getProc().getArr(type[1]).set(0, 100);
			else if (type[0] == PC_TRAIT) {
				Trait types = UserProfile.getBCData().traits.get(type[1]);

				if (!ans.getTraits().contains(types))
					ans.getTraits().add(types);
			}
		}

		return ans;
	}

	private void improve(DataUnit ans, int[] type, int[] modifs) {
		if (type[0] == PC_AB)
			ans.abi |= type[1];
		else {
			switch (type[1]) {
				case PC2_SPEED:
					ans.speed += modifs[0];
					break;
				case PC2_CD:
					ans.respawn -= modifs[0];
					break;
				case PC2_COST:
					ans.price -= modifs[0];
					break;
				case PC2_HB:
					ans.hb += modifs[0];
					break;
				case PC2_TBA:
					ans.tba = (int) (ans.tba * (100 - modifs[0]) / 100.0);
			}
		}
	}

	private void improve(CustomUnit ans, int[] type, int[] modifs) {
		if (type[0] == PC_AB)
			ans.abi |= type[1];
		else {
			switch (type[1]) {
				case PC2_SPEED:
					ans.speed += modifs[0];
					break;
				case PC2_CD:
					ans.resp -= modifs[0];
					break;
				case PC2_COST:
					ans.price -= modifs[0];
					break;
				case PC2_HB:
					ans.hb += modifs[0];
					break;
				case PC2_TBA:
					ans.tba = (int) (ans.tba * (100 - modifs[0]) / 100.0);
			}
		}
	}

	public double getAtkMultiplication(int[] talents) {
		for(int i = 0; i < info.size(); i++) {
			if(info.get(i)[0] >= PC_CORRES.length)
				continue;

			if(talents[i] == 0)
				continue;

			int[] type = PC_CORRES[info.get(i)[0]];

			if(type[0] == -1)
				continue;

			if(type[0] == PC_BASE && type[1] == PC2_ATK) {
				int maxlv = info.get(i)[1];
				int[] modifs = new int[4];
				if (maxlv > 1) {
					for (int j = 0; j < 4; j++) {
						int v0 = info.get(i)[2 + j * 2];
						int v1 = info.get(i)[3 + j * 2];
						modifs[j] = (v1 - v0) * (talents[i] - 1) / (maxlv - 1) + v0;
					}
				}
				if (maxlv == 0)
					for (int j = 0; j < 4; j++)
						modifs[j] = info.get(i)[3 + j * 2];

				return 1 + modifs[0] * 0.01;
			}
		}

		return 1.0;
	}

	public double getHPMultiplication(int[] talents) {
		for(int i = 0; i < info.size(); i++) {
			if(info.get(i)[0] >= PC_CORRES.length)
				continue;

			if(talents[i] == 0)
				continue;

			int[] type = PC_CORRES[info.get(i)[0]];

			if(type[0] == -1)
				continue;

			if(type[0] == PC_BASE && type[1] == PC2_HP) {
				int maxlv = info.get(i)[1];
				int[] modifs = new int[4];
				if (maxlv > 1) {
					for (int j = 0; j < 4; j++) {
						int v0 = info.get(i)[2 + j * 2];
						int v1 = info.get(i)[3 + j * 2];
						modifs[j] = (v1 - v0) * (talents[i] - 1) / (maxlv - 1) + v0;
					}
				}
				if (maxlv == 0)
					for (int j = 0; j < 4; j++)
						modifs[j] = info.get(i)[3 + j * 2];

				return 1 + modifs[0] * 0.01;
			}
		}

		return 1.0;
	}
	
	@OnInjected
	public void onInjected() {
		info.replaceAll(data -> {
			if(data.length == 14) {
				return data;
			} else {
				int[] newData = new int[14];

				System.arraycopy(data, 0, newData, 0, data.length);

				return newData;
			}
		});

		max = new int[info.size()];

		for (int i = 0; i < info.size(); i++) {
			max[i] = Math.max(1, info.get(i)[1]);
		}
	}

	private static boolean talentExist(String[] data, int index) {
		for(int i = index; i < index + 14; i++) {
			if(!data[i].trim().equals("0")) {
				return true;
			}
		}

		return false;
	}
}
