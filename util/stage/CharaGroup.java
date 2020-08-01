package common.util.stage;

import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.TreeSet;

import common.io.InStream;
import common.io.OutStream;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.io.json.JsonClass.JCConcstructor;
import common.io.json.JsonClass.JCGeneric;
import common.io.json.JsonClass.JCGenericRead;
import common.io.json.JsonClass.JCGenericWrite;
import common.io.json.JsonClass.RType;
import common.system.files.VFile;
import common.util.Data;
import common.util.pack.Pack;
import common.util.unit.Unit;
import common.util.unit.UnitStore;
import common.CommonStatic;

@JCGeneric(int.class)
@JsonClass(read = RType.FILL)
public class CharaGroup extends Data implements Comparable<CharaGroup> {

	public static final Map<Integer, CharaGroup> map = new TreeMap<>();

	public static CharaGroup get(int ind) {
		int pac = ind / 1000;
		if (pac == 0)
			return map.get(ind % 1000);
		return Pack.map.get(pac).mc.groups.get(ind % 1000);
	}

	public static void read() {
		Queue<String> qs = VFile.readLine("./org/data/Charagroup.csv");
		qs.poll();
		for (String str : qs) {
			String[] strs = str.split(",");
			int id = CommonStatic.parseIntN(strs[0]);
			int type = CommonStatic.parseIntN(strs[2]);
			int[] units = new int[strs.length - 3];
			for (int i = 3; i < strs.length; i++)
				units[i - 3] = CommonStatic.parseIntN(strs[i]);
			map.put(id, new CharaGroup(Pack.def, id, type, units));
		}
	}

	public final Pack pack;
	@JsonField
	public int id, type = 0;
	@JsonField(generic = Unit.class)
	public final TreeSet<Unit> set = new TreeSet<>();
	@JsonField
	public String name = "";

	@JCConcstructor
	public CharaGroup(MapColc map) {
		pack = map.pack;
	}

	@JCGenericRead(value = int.class, parent = MapColc.class)
	public static CharaGroup zgen(int i, MapColc mc) {
		return i < 0 ? null : mc.groups.get(i);
	}

	@JCGenericWrite(int.class)
	public int zser() {
		return id;
	}

	public CharaGroup(CharaGroup cg) {
		pack = null;
		id = -1;
		type = cg.type;
		set.addAll(cg.set);
	}

	public CharaGroup(Pack pac, int ID, int t, int... units) {
		pack = pac;
		id = ID;
		type = t;
		for (int uid : units) {
			Unit u = UnitStore.get(uid, true);
			if (u != null)
				set.add(u);
		}
	}

	protected CharaGroup(Pack pac, InStream is) {
		pack = pac;
		zread(is);
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
		return trio(id) + "-" + name;
	}

	public boolean used() {
		if (pack == Pack.def)
			return true;
		for (LvRestrict lr : pack.mc.lvrs.getList())
			if (lr.res.containsKey(this))
				return true;
		for (StageMap sm : pack.mc.maps)
			for (Stage st : sm.list)
				if (st.lim != null && st.lim.group == this)
					return true;
		return false;
	}

	protected void write(OutStream os) {
		os.writeString("0.3.8");
		os.writeString(name);
		os.writeInt(id);
		os.writeInt(type);
		os.writeInt(set.size());
		for (Unit u : set)
			os.writeInt(u.id);
	}

	private void zread(InStream is) {
		int ver = getVer(is.nextString());
		if (ver >= 308)
			zread$000308(is);
		else if (ver >= 307)
			zread$000307(is);
	}

	private void zread$000307(InStream is) {
		id = is.nextInt();
		type = is.nextInt();
		int m = is.nextInt();
		for (int j = 0; j < m; j++) {
			Unit u = UnitStore.get(is.nextInt(), true);
			if (u != null)
				set.add(u);
		}
	}

	private void zread$000308(InStream is) {
		name = is.nextString();
		zread$000307(is);
	}

}
