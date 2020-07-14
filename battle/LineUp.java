package common.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import common.io.InStream;
import common.io.OutStream;
import common.io.json.JsonClass;
import common.io.json.JsonDecoder;
import common.io.json.JsonDecoder.OnInjected;
import common.io.json.JsonEncoder;
import common.io.json.JsonException;
import common.io.json.JsonField;
import common.io.json.JsonField.GenType;
import common.io.json.JsonField.SerType;
import common.util.Data;
import common.util.unit.Combo;
import common.util.unit.EForm;
import common.util.unit.Form;
import common.util.unit.Level;
import common.util.unit.Unit;
import common.util.unit.UnitStore;

import java.util.TreeMap;

@JsonClass
public class LineUp extends Data {

	@JsonField(generic = { Unit.class,
			Level.class }, gen = GenType.GEN, ser = SerType.FUNC, generator = "zgen", serializer = "zser")
	public final TreeMap<Unit, Level> map = new TreeMap<>();

	@JsonField(gen = GenType.GEN, ser = SerType.FUNC, generator = "zgen", serializer = "zser")
	public final Form[][] fs = new Form[2][5];

	public final EForm[][] efs = new EForm[2][5];
	public int[] inc = new int[C_TOT], loc = new int[5];
	public List<Combo> coms = new ArrayList<>();

	private boolean updating = false;

	/** new LineUp object */
	protected LineUp() {
		renew();
	}

	/** read a LineUp object from data */
	protected LineUp(int ver, InStream is) {
		zread(ver, is);
		renew();
	}

	/** clone a LineUp object */
	protected LineUp(LineUp ref) {
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 5; j++)
				fs[i][j] = ref.fs[i][j];
		for (Entry<Unit, Level> e : ref.map.entrySet()) {
			map.put(e.getKey(), e.getValue().clone());
		}
		renew();
	}

	/** shift all cats to lowest index possible */
	public void arrange() {
		for (int i = 0; i < 10; i++)
			if (getFS(i) == null)
				for (int j = i + 1; j < 10; j++)
					if (getFS(j) != null) {
						setFS(getFS(j), i);
						setFS(null, j);
						break;
					} else if (j == 9)
						return;
	}

	/** test whether contains certain combo */
	public boolean contains(Combo c) {
		for (Combo com : coms)
			if (com == c)
				return true;
		return false;
	}

	/** get level of an Unit, if no date recorded, record default one */
	public synchronized Level getLv(Unit u) {
		if (!map.containsKey(u))
			setLv(u, u.getPrefLvs());
		return map.get(u);
	}

	/**
	 * return how much space from 1st row a combo will need to put in this lineup
	 */
	public int occupance(Combo c) {
		int[][] com = c.units;
		int rem = com.length;
		for (int i = 0; i < com.length; i++)
			for (int j = 0; j < 5; j++) {
				Form f = fs[0][j];
				if (f == null)
					continue;
				if (f.uid == com[i][0])
					rem--;
			}
		return rem;
	}

	@OnInjected
	public void renew() {
		validate();
		renewEForm();
		renewCombo();
	}

	/** apply a combo */
	public void set(int[][] com) {
		boolean[] rep = new boolean[5];
		boolean[] exi = new boolean[com.length];
		int rem = com.length;
		for (int i = 0; i < com.length; i++)
			for (int j = 0; j < 5; j++) {
				Form f = fs[0][j];
				if (f == null)
					continue;
				if (f.uid == com[i][0]) {
					rep[j] = true;
					exi[i] = true;
					if (f.fid < com[i][1])
						fs[0][j] = UnitStore.get(com[i]);
					loc[j]++;
					rem--;
				}
			}
		int free = 0;
		for (int i = 0; i < 5; i++)
			if (loc[i] == 0)
				free++;

		if (free < rem) {
			int del = rem - free;
			while (del > 0) {
				Combo c = coms.remove(0);
				for (int i = 0; i < c.units.length; i++) {
					if (c.units[i][0] == -1)
						break;
					for (int j = 0; j < 5; j++) {
						Form f = fs[0][j];
						if (f == null)
							break;
						if (f.uid != c.units[i][0])
							continue;
						loc[j]--;
						if (loc[j] == 0)
							del--;
						break;
					}
				}
			}
		}
		for (int i = 0; i < 5; i++)
			for (int[] is : com)
				if (fs[1][i] != null && fs[1][i].uid == is[0])
					fs[1][i] = null;
		arrange();
		int emp = 0;
		for (int i = 0; i < 10; i++)
			if (getFS(i) == null)
				emp++;
		if (emp < rem) {
			for (int i = 10 - rem; i < 10 - emp; i++)
				setFS(null, i);
			emp = rem;
		}
		int p = 0, r = 0, i = 0, j = 10 - emp;
		while (r < rem) {
			while (loc[i] != 0)
				i++;
			while (exi[p])
				p++;
			setFS(getFS(i), j++);
			setFS(UnitStore.get(com[p++]), i++);
			r++;
		}
		renew();
	}

	/** set level record of an Unit */
	public synchronized void setLv(Unit u, int[] lv) {
		boolean sub = updating;
		updating = true;

		Level l = map.get(u);

		if (l != null) {
			l.setLvs(lv);
		} else {
			l = new Level(lv);

			map.put(u, l);
		}

		if (!sub)
			renewEForm();
		updating &= sub;
	}

	/** set orb data of an Unit */
	public synchronized void setOrb(Unit u, int[] lvs, int[][] orbs) {
		// lvs must be generated before doing something with orbs
		boolean sub = updating;

		updating = true;

		Level l = map.get(u);

		if (l != null) {
			l.setLvs(lvs);
			l.setOrbs(orbs);
		} else {
			l = new Level(lvs, orbs);

			map.put(u, l);
		}

		if (!sub)
			renewEForm();

		updating &= sub;
	}

	/** return whether implementing this combo will replace other combo */
	public boolean willRem(Combo c) {
		int free = 0;
		for (int i = 0; i < 5; i++)
			if (fs[0][i] == null)
				free++;
			else if (loc[i] == 0) {
				boolean b = true;
				for (int[] is : c.units)
					if (is[0] == fs[0][i].uid) {
						b = false;
						break;
					}
				if (b)
					free++;
			}
		return free < occupance(c);
	}

	/** write data to file */
	public OutStream write() {
		validate();
		OutStream os = OutStream.getIns();
		os.writeString("0.4.0");
		for (int i = 0; i < 10; i++)
			if (getFS(i) == null) {
				os.writeInt(i);
				break;
			} else if (i == 9)
				os.writeInt(10);
		for (int i = 0; i < 10; i++) {
			Form f = getFS(i);
			if (f == null)
				break;
			os.writeInt(f.uid);
			os.writeInt(f.fid);
		}
		os.writeInt(map.size());
		for (Entry<Unit, Level> e : map.entrySet()) {
			os.writeInt(e.getKey().id);
			os.writeIntB(e.getValue().getLvs());
			if (e.getValue().getOrbs() != null) {
				os.writeInt(1);
				os.writeIntBB(e.getValue().getOrbs());
			} else {
				os.writeInt(0);
			}
		}
		os.terminate();
		return os;
	}

	/** set slot using 1 dim index */
	protected void setFS(Form f, int i) {
		fs[i / 5][i % 5] = f;
	}

	/** get Form from 1 dim index */
	private Form getFS(int i) {
		return fs[i / 5][i % 5];
	}

	/** check combo information */
	private void renewCombo() {
		List<Combo> tcom = new ArrayList<>();
		inc = new int[C_TOT];
		loc = new int[5];
		for (Combo[] cs : Combo.combos)
			for (Combo c : cs) {
				boolean b = true;
				for (int i = 0; i < c.units.length; i++) {
					if (c.units[i][0] == -1)
						break;
					boolean b0 = false;
					for (int j = 0; j < 5; j++) {
						Form f = fs[0][j];
						if (f == null)
							break;
						if (f.uid != c.units[i][0] || f.fid < c.units[i][1])
							continue;
						b0 = true;
						break;
					}
					if (b0)
						continue;
					b = false;
					break;
				}
				if (b) {
					tcom.add(c);
					inc[c.type] += Combo.values[c.type][c.lv];
					for (int i = 0; i < c.units.length; i++)
						for (int j = 0; j < 5; j++) {
							Form f = fs[0][j];
							if (f == null)
								continue;
							if (f.uid == c.units[i][0] && f.fid >= c.units[i][1])
								loc[j]++;
						}
				}
			}
		for (int i = 0; i < coms.size(); i++)
			if (!tcom.contains(coms.get(i))) {
				coms.remove(i);
				i--;
			}
		for (int i = 0; i < tcom.size(); i++)
			if (!coms.contains(tcom.get(i)))
				coms.add(tcom.get(i));
	}

	private void renewEForm() {
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 5; j++)
				if (fs[i][j] == null)
					efs[i][j] = null;
				else
					efs[i][j] = new EForm(fs[i][j], getLv(fs[i][j].unit));
	}

	private void validate() {
		for (int i = 0; i < 10; i++)
			if (getFS(i) != null) {
				int id = getFS(i).uid;
				int f = getFS(i).fid;
				if (UnitStore.get(id, f, false) == null)
					setFS(null, i);
			}
		arrange();
	}

	/** read data from file, support multiple version */
	private void zread(int ver, InStream is) {
		int val = ver;
		if (val >= 307)
			val = getVer(is.nextString());
		if (val >= 400)
			zread$000400(is);
		else if (val >= 309)
			zread$000309(is);
		else if (val >= 307)
			zread$000307(is);
		else if (val >= 203)
			zread$000203(is);
	}

	private void zread$000203(InStream is) {
		int n = is.nextByte();
		for (int i = 0; i < n; i++) {
			int uid = is.nextShort();
			int fid = is.nextByte();
			setFS(UnitStore.get(uid, fid, true), i);
		}
		int m = is.nextShort();
		for (int i = 0; i < m; i++) {
			int uid = is.nextShort();
			int lv = is.nextByte();
			int[] lvs = new int[6];
			lvs[0] = lv;
			Unit u = UnitStore.get(uid, true);
			if (u != null)
				map.put(u, new Level(lvs));
		}
	}

	private void zread$000307(InStream is) {
		int n = is.nextByte();
		for (int i = 0; i < n; i++) {
			int uid = is.nextShort();
			int fid = is.nextByte();
			setFS(UnitStore.get(uid, fid, true), i);
		}
		int m = is.nextShort();
		for (int i = 0; i < m; i++) {
			int uid = is.nextShort();
			int[] lv = is.nextIntsB();
			Unit u = UnitStore.get(uid, true);
			if (u != null)
				map.put(u, new Level(lv));
		}
	}

	private void zread$000309(InStream is) {
		int n = is.nextInt();
		for (int i = 0; i < n; i++) {
			int uid = is.nextInt();
			int fid = is.nextInt();
			setFS(UnitStore.get(uid, fid, true), i);
		}
		int m = is.nextInt();
		for (int i = 0; i < m; i++) {
			int uid = is.nextInt();
			int[] lv = is.nextIntsB();
			Unit u = UnitStore.get(uid, true);
			if (u != null)
				map.put(u, new Level(lv));
		}
		arrange();
	}

	private void zread$000400(InStream is) {
		int n = is.nextInt();
		for (int i = 0; i < n; i++) {
			int uid = is.nextInt();
			int fid = is.nextInt();
			setFS(UnitStore.get(uid, fid, true), i);
		}
		int m = is.nextInt();
		for (int i = 0; i < m; i++) {
			int uid = is.nextInt();
			int[] lv = is.nextIntsB();
			Unit u = UnitStore.get(uid, true);
			int[][] orbs = null;
			int existing = is.nextInt();
			if (existing == 1) {
				orbs = is.nextIntsBB();
			}
			if (u != null)
				map.put(u, new Level(lv, orbs));
		}
		arrange();
	}

	public Object zgen(Class<?> cls, JsonElement elem) throws JsonException {
		if (cls == Level.class)
			return JsonDecoder.decode(elem, cls);
		if (cls == Unit.class)
			return UnitStore.get(elem.getAsInt(), true);
		if (cls == Form.class)
			return UnitStore.get(JsonDecoder.decode(elem, int[].class));
		return null;
	}

	public JsonElement zser(Level lv) throws Exception {
		return JsonEncoder.encode(lv);
	}

	public JsonElement zser(Unit u) throws Exception {
		return new JsonPrimitive(u.id);
	}

	public JsonElement zser(Form f) throws Exception {
		return JsonEncoder.encode(new int[] { f.unit.id, f.fid });
	}
}
