package common.util.unit;

import common.CommonStatic;
import common.CommonStatic.BCAuxAssets;
import common.system.files.VFile;
import common.util.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Combo extends Data {

	public static void readFile() {
		BCAuxAssets aux = CommonStatic.getBCAssets();
		Queue<String> qs = VFile.readLine("./org/data/NyancomboData.csv");
		List<Combo> list = new ArrayList<>();
		int i = 0;
		int[] ns = new int[C_TOT];
		for (String str : qs) {
			if (str.length() < 20)
				continue;
			Combo c = new Combo(i++, str.trim());
			if (c.show > 0) {
				list.add(c);
				ns[c.type]++;
			}
		}
		for (i = 0; i < C_TOT; i++)
			aux.combos[i] = new Combo[ns[i]];
		ns = new int[C_TOT];
		for (Combo c : list)
			aux.combos[c.type][ns[c.type]++] = c;

		qs = VFile.readLine("./org/data/NyancomboParam.tsv");
		for (i = 0; i < C_TOT; i++) {
			String[] strs = qs.poll().trim().split("\t");
			if (strs.length < 5)
				continue;
			for (int j = 0; j < 5; j++) {
				aux.values[i][j] = Integer.parseInt(strs[j]);
				if (i == C_C_SPE)
					aux.values[i][j] = (aux.values[i][j] - 10) * 15;
			}
		}
		qs = VFile.readLine("./org/data/NyancomboFilter.tsv");
		aux.filter = new int[qs.size()][];
		for (i = 0; i < aux.filter.length; i++) {
			String[] strs = qs.poll().trim().split("\t");
			aux.filter[i] = new int[strs.length];
			for (int j = 0; j < strs.length; j++)
				aux.filter[i][j] = Integer.parseInt(strs[j]);
		}
	}

	public final int name, show, id, type, lv;
	public final int[][] units;

	private Combo(int ID, String str) {
		id = ID;
		String[] strs = str.split(",");
		name = Integer.parseInt(strs[0]);
		show = Integer.parseInt(strs[1]);
		int n = 0;
		for (n = 0; n < 5; n++)
			if (Integer.parseInt(strs[2 + n * 2]) == -1)
				break;
		units = new int[n][2];
		for (int i = 0; i < n; i++) {
			units[i][0] = Integer.parseInt(strs[2 + i * 2]);
			units[i][1] = Integer.parseInt(strs[3 + i * 2]);
		}
		type = Integer.parseInt(strs[12]);
		lv = Integer.parseInt(strs[13]);
	}

	@Override
	public String toString() {
		return name + "," + show;
	}

}
