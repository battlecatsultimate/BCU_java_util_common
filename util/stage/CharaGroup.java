package common.util.stage;

import java.util.TreeSet;

import common.io.InStream;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.pack.PackData.Identifier;
import common.pack.UserProfile;
import common.util.Data;
import common.util.unit.Unit;

@JsonClass
public class CharaGroup extends Data implements Comparable<CharaGroup> {

	public static class DefCG extends CharaGroup {

		public DefCG(int ID, int t, Identifier[] units) {
			super(t, units);
			id = ID;
		}

	}

	@JsonClass
	public static class PackCG extends CharaGroup {

		private final MapColc mc;

		@JsonField
		public String name = "";

		@Deprecated
		public PackCG(MapColc mc, InStream is) {
			this.mc = mc;
			int ver = getVer(is.nextString());
			if (ver == 308) {
				name = is.nextString();
				id = is.nextInt();
				type = is.nextInt();
				int m = is.nextInt();
				for (int j = 0; j < m; j++) {
					Unit u = UserProfile.getUnit(Identifier.parseInt(is.nextInt()));
					if (u != null)
						set.add(u);
				}
			}
		}

		@Override
		public String toString() {
			return trio(id) + " - " + name;
		}

		public boolean used() {
			for (LvRestrict lr : mc.lvrs.getList())
				if (lr.res.containsKey(this))
					return true;
			for (StageMap sm : mc.maps)
				for (Stage st : sm.list)
					if (st.lim != null && st.lim.group == this)
						return true;
			return false;
		}

	}

	@JsonField
	public int id = -1, type = 0;
	@JsonField(generic = Unit.class, alias = Identifier.class)
	public final TreeSet<Unit> set = new TreeSet<>();

	public CharaGroup(CharaGroup cg) {
		type = cg.type;
		set.addAll(cg.set);
	}

	private CharaGroup() {
	}

	private CharaGroup(int t, Identifier... units) {
		type = t;
		for (Identifier uid : units) {
			Unit u = UserProfile.getUnit(uid);
			if (u != null)
				set.add(u);
		}
	}

	public boolean allow(Unit u) {
		if (type == 0 && !set.contains(u) || type == 2 && set.contains(u))
			return false;
		return true;
	}

	public CharaGroup combine(CharaGroup cg) {
		CharaGroup ans = new CharaGroup(this);
		if (type == 0 && cg.type == 0)
			ans.set.retainAll(cg.set);
		else if (type == 0 && cg.type == 2)
			ans.set.removeAll(cg.set);
		else if (type == 2 && cg.type == 0) {
			ans.type = 0;
			ans.set.addAll(cg.set);
			ans.set.removeAll(set);
		} else if (type == 2 && cg.type == 2)
			ans.set.addAll(cg.set);
		return ans;
	}

	@Override
	public int compareTo(CharaGroup cg) {
		return Integer.compare(id, cg.id);
	}

	@Override
	public String toString() {
		return trio(id);
	}

}
