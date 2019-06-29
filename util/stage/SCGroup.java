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
			is.nextInt();
			int max = is.nextInt();
			is.nextInt();
			return new SCGroup(id, max);
		}
		return null;
	}

	public final int id;
	private int max;

	public SCGroup(int ID, int n) {
		id = ID;
		max = n;
	}

	@Override
	public SCGroup copy(Integer id) {
		return new SCGroup(id, max);
	}

	public int getMax(int star) {
		return max;
	}

	public void setMax(int val, int star) {
		max = val;
	}

	@Override
	public String toString() {
		return trio(id) + " - " + max;
	}

	protected void write(OutStream os) {
		os.writeString("0.4.4");
		os.writeInt(id);
		os.writeInt(1);// stars
		os.writeInt(max);
		os.writeInt(0);// parents
	}

}
