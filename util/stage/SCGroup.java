package common.util.stage;

import common.util.Data;
import common.util.system.BasedCopable;

public class SCGroup extends Data implements BasedCopable<SCGroup, Integer> {

	public int id, max;

	public SCGroup(int ID, int n) {
		id = ID;
		max = n;
	}

	@Override
	public SCGroup copy(Integer id) {
		return new SCGroup(id, max);
	}

	@Override
	public String toString() {
		return trio(id) + " - " + max;
	}

}
