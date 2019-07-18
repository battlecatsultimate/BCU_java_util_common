package common.util.stage;

import common.io.InStream;
import common.io.OutStream;
import common.system.BasedCopable;
import common.util.Data;

public class SCGroup extends Data implements BasedCopable<SCGroup, Integer> {

	public static SCGroup zread(InStream is) {
		int ver = getVer(is.nextString());
		if (ver == 404) {
			int id = is.nextInt();
			int n = is.nextInt();
			int[] max = new int[n];
			for (int i = 0; i < n; i++)
				max[i] = is.nextInt();
			is.nextInt();
			return new SCGroup(id, max);
		}
		return null;
	}

	public final int id;
	private int[] max = { -1, -1, -1, -1 };

	public SCGroup(int ID, int... ns) {
		id = ID;
		for (int i = 0; i < ns.length; i++)
			max[i] = ns[i];
	}

	@Override
	public SCGroup copy(Integer id) {
		return new SCGroup(id, max);
	}

	public int getMax(int star) {
		if (max[star] == -1 && star > 0)
			return getMax(star - 1);
		return max[star];
	}

	public void setMax(int val, int star) {
		if(star==-1) {
			for(int i=0;i<max.length;i++)
				max[star]=val;
			return;
		}
		max[star] = val;
		if (star > 0 && max[star - 1] < 0)
			setMax(val, star - 1);
	}

	@Override
	public String toString() {
		String str = trio(id) + " - " + max[0];
		String temp = "";
		for (int i = 1; i < 4; i++)
			if (max[i] == -1)
				return str;
			else if (max[i] == max[i - 1])
				temp += "," + max[i];
			else {
				str += temp + "," + max[i];
				temp = "";
			}
		return str;
	}

	protected void write(OutStream os) {
		os.writeString("0.4.4");
		os.writeInt(id);
		os.writeInt(4);// stars
		for (int i = 0; i < 4; i++)
			os.writeInt(max[i]);
		os.writeInt(0);// parents
	}

}
