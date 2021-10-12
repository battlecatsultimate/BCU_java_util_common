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

import java.util.ArrayList;
import java.util.Queue;

@JsonClass(read = JsonClass.RType.FILL)
public class PCoin extends Data {
	public static void read() {
		Queue<String> qs = VFile.readLine("./org/data/SkillAcquisition.csv");
		qs.poll();
		for (String str : qs) {
			String[] strs = str.trim().split(",");

			if (strs.length == 67)
				new PCoin(strs);
		}
	}

	private final MaskUnit du;
	public MaskUnit full = null;
	public ArrayList<Trait> trait = new ArrayList<>();

	public int[] max = new int[6];
	@JsonField(generic = int[].class)
	public final ArrayList<int[]> info = new ArrayList<>();

	public PCoin(CustomEntity ce) {
		du = (CustomUnit)ce;
		((CustomUnit)du).pcoin = this;
	}

	private PCoin(String[] strs) {
		int id = CommonStatic.parseIntN(strs[0]);
		trait = Trait.convertType(CommonStatic.parseIntN(strs[1]));

		for (int i = 0; i < 5; i++) {
			info.add(new int[13]);
			for (int j = 0; j < 13; j++)
				info.get(i)[j] = CommonStatic.parseIntN(strs[2 + i * 13 + j]);
			max[i + 1] = info.get(i)[1];
			if (max[i + 1] == 0)
				max[i + 1] = 1;
		}
		du = Identifier.parseInt(id, Unit.class).get().forms[2].du;
		((DataUnit)du).pcoin = this;
		full = improve(max);
	}

	public void update() {
		max = new int[1 + info.size()];
		for (int i = 0; i < info.size(); i++)
			max[i + 1] = info.get(i)[1];
		full = improve(max);
	}

	@SuppressWarnings("deprecation")
	public MaskUnit improve(int[] lvs) {
		MaskUnit ans = du.clone();
		if (lvs.length < max.length)
			System.arraycopy(max, 1, lvs, lvs.length, max.length - lvs.length);
		for (int i = 0; i < info.size(); i++) {
			if (du instanceof CustomUnit && max[i + 1] == 0)
				continue;
			if (info.get(i)[0] >= PC_CORRES.length) {
				CommonStatic.ctx.printErr(ErrType.NEW, "new PCoin ability not yet handled by BCU: " + info.get(i)[0] + "\nText ID is " + info.get(i)[10]);
				continue;
			}
			int[] type = PC_CORRES[info.get(i)[0]];
			if (type[0] == -1) {
				CommonStatic.ctx.printErr(ErrType.NEW, "new PCoin ability not yet handled by BCU: " + info.get(i)[0] + "\nText ID is " + info.get(i)[10]);
				continue;
			}
			if (lvs[i + 1] == 0) {
				if (type[0] == PC_TRAIT) {
					Trait types = UserProfile.getBCData().traits.get(type[1]);
					ans.getTraits().remove(types);
				}
				continue;
			}
			//Targettings that come with a talent, such as Hyper Mr's
			if (this.trait.size() > 0)
				if (!ans.getTraits().contains(this.trait.get(0)))
					ans.getTraits().add(this.trait.get(0));

			int maxlv = info.get(i)[1];
			int[] modifs = new int[4];
			if (maxlv > 1) {
				for (int j = 0; j < 4; j++) {
					int v0 = info.get(i)[2 + j * 2];
					int v1 = info.get(i)[3 + j * 2];
					modifs[j] = (v1 - v0) * (lvs[i + 1] - 1) / (maxlv - 1) + v0;
				}
			}
			else
				for (int j = 0; j < 4; j++)
					modifs[j] = info.get(i)[3 + j * 2];

			if (type[0] == PC_P) {
				ProcItem tar = ans.getProc().getArr(type[1]);

				if (type[1] == P_VOLC) {
					if (du instanceof DataUnit) {
						tar.set(0, modifs[0]);
						tar.set(1, modifs[2] / 4);
						tar.set(2, (modifs[2] + modifs[3]) / 4);
						tar.set(3, modifs[1] * 20);
					} else {
						tar.set(0, modifs[0]);
						tar.set(1, Math.min(modifs[1], modifs[2]));
						tar.set(2, Math.max(modifs[1], modifs[2]));
						tar.set(3, modifs[3]);
					}
				} else
					for (int j = 0; j < 4; j++)
						if (modifs[j] > 0)
							tar.set(j, tar.get(j) + modifs[j]);

				if (du instanceof DataUnit) {
					if (type[1] == P_STRONG && modifs[0] != 0)
						tar.set(0, 100 - tar.get(0));
					if (type[1] == P_WEAK)
						tar.set(2, 100 - tar.get(2));
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
					BCImprove((DataUnit)ans,type,modifs);
				else
					diyImprove((CustomUnit)ans,type,modifs);
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

	private void BCImprove(DataUnit ans, int[] type, int[] modifs) {
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
			}
		}
	}

	private void diyImprove(CustomUnit ans, int[] type, int[] modifs) {
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
			}
		}
	}

	public double getAtkMultiplication(int[] lvs) {
		for(int i = 0; i < info.size(); i++) {
			if(info.get(i)[0] >= PC_CORRES.length)
				continue;
			if(lvs[i + 1] == 0)
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
						modifs[j] = (v1 - v0) * (lvs[i + 1] - 1) / (maxlv - 1) + v0;
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

	public double getHPMultiplication(int[] lvs) {
		for(int i = 0; i < info.size(); i++) {
			if(info.get(i)[0] >= PC_CORRES.length)
				continue;
			if(lvs[i + 1] == 0)
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
						modifs[j] = (v1 - v0) * (lvs[i + 1] - 1) / (maxlv - 1) + v0;
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
		max = new int[1 + info.size()];
		for (int i = 0; i < info.size(); i++)
			max[i + 1] = info.get(i)[1];
	}
}
