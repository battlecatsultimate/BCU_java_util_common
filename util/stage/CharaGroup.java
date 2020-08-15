package common.util.stage;

import java.util.TreeSet;

import common.io.InStream;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.pack.IndexContainer.IndexCont;
import common.pack.IndexContainer.Indexable;
import common.pack.PackData;
import common.pack.PackData.Identifier;
import common.pack.PackData.UserPack;
import common.util.Data;
import common.util.unit.Unit;

@IndexCont(PackData.class)
@JsonClass
public class CharaGroup extends Data implements Indexable<PackData, CharaGroup>, Comparable<CharaGroup> {

	public static class DefCG extends CharaGroup {

		public DefCG(int ID, int t, Identifier<Unit>[] units) {
			super(t, units);
			id = Identifier.parseInt(ID, CharaGroup.class);
		}

	}

	@JsonClass
	public static class PackCG extends CharaGroup {

		private final UserPack mc;

		@JsonField
		public String name = "";

		@Deprecated
		public PackCG(UserPack mc, InStream is) {
			this.mc = mc;
			int ver = getVer(is.nextString());
			if (ver == 308) {
				name = is.nextString();
				id = mc.getID(CharaGroup.class, is.nextInt());
				type = is.nextInt();
				int m = is.nextInt();
				for (int j = 0; j < m; j++) {
					Unit u = Identifier.parseInt(is.nextInt(), Unit.class).get();
					if (u != null)
						set.add(u);
				}
			}
		}

		@Override
		public String toString() {
			return id + " - " + name;
		}

		public boolean used() {
			for (LvRestrict lr : mc.lvrs.getList())
				if (lr.res.containsKey(this))
					return true;
			for (StageMap sm : mc.mc.maps)
				for (Stage st : sm.list)
					if (st.lim != null && st.lim.group == this)
						return true;
			return false;
		}

	}

	@JsonField
	public Identifier<CharaGroup> id;
	@JsonField
	public int type = 0;
	@JsonField(generic = Unit.class, alias = Identifier.class)
	public final TreeSet<Unit> set = new TreeSet<>();

	public CharaGroup(CharaGroup cg) {
		type = cg.type;
		set.addAll(cg.set);
	}

	private CharaGroup() {
	}

	@SuppressWarnings("unchecked")
	private CharaGroup(int t, Identifier<Unit>... units) {
		type = t;
		for (Identifier<Unit> uid : units) {
			Unit u = uid.get();
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
		return id.compareTo(cg.id);
	}

	@Override
	public Identifier<CharaGroup> getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return id.toString();
	}

}
