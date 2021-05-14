
package common.battle;

import common.CommonStatic;
import common.io.InStream;
import common.io.json.JsonClass;
import common.io.json.JsonDecoder.OnInjected;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.pack.PackData;
import common.pack.UserProfile;
import common.util.Data;
import common.util.unit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

@JsonClass
public class LineUp extends Data {

	@JsonField(generic = { Identifier.class, Level.class })
	public final TreeMap<Identifier<Unit>, Level> map = new TreeMap<>();

	@JsonField(alias = Form.FormJson.class)
	public final Form[][] fs = new Form[2][5];
	public final EForm[][] efs = new EForm[2][5];
	public int[] inc = new int[C_TOT], loc = new int[5];

	public List<Combo> coms = new ArrayList<>();

	private boolean updating = false;

	/**
	 * new LineUp object
	 */
	protected LineUp() {
		renew();
	}

	/**
	 * read a LineUp object from data
	 */
	protected LineUp(int ver, InStream is) {
		zread(ver, is);
		renew();
	}

	/**
	 * clone a LineUp object
	 */
	protected LineUp(LineUp ref) {
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 5; j++)
				fs[i][j] = ref.fs[i][j];
		for (Entry<Identifier<Unit>, Level> e : ref.map.entrySet()) {
			map.put(e.getKey(), e.getValue().clone());
		}
		renew();
	}

	/**
	 * shift all cats to lowest index possible
	 */
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

	/**
	 * test whether contains certain combo
	 */
	public boolean contains(Combo c) {
		for (Combo com : coms)
			if (com == c)
				return true;
		return false;
	}

	/**
	 * get level of an Unit, if no date recorded, record default one
	 */
	public synchronized Level getLv(Form u) {
		if (!map.containsKey(u.unit.id))
			setLv(u.unit, u.getPrefLvs());
		return map.get(u.unit.id);
	}

	/**
	 * return how much space from 1st row a combo will need to put in this lineup
	 */
	public int occupance(Combo c) {
		Form[] com = c.forms;
		int rem = com.length;
		for (Form form : com)
			for (int j = 0; j < 5; j++) {
				Form f = fs[0][j];
				if (f == null)
					continue;
				if (f.unit == form.unit)
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

	/**
	 * apply a combo
	 */
	public void set(Form[] com) {
		// if a unit in the lineup is present in the combo
		boolean[] rep = new boolean[5];
		// if a unit in the combo is already present in the lineup
		boolean[] exi = new boolean[com.length];
		// the number of units required to inject
		int rem = com.length;
		for (int i = 0; i < com.length; i++)
			for (int j = 0; j < 5; j++) {
				Form f = fs[0][j];
				int formID = com[i].fid;
				if (f == null)
					continue;
				if (f.unit == com[i].unit) {
					rep[j] = true;
					exi[i] = true;
					if (f.fid < formID)
						fs[0][j] = f.unit.forms[formID];
					loc[j]++;
					rem--;
				}
			}
		// number of units not present in any combo
		int free = 0;
		for (int i = 0; i < 5; i++)
			if (loc[i] == 0)
				free++;

		if (free < rem) {
			// required to remove some combo

			int del = rem - free;
			while (del > 0) {
				Combo c = coms.remove(0);
				for (int i = 0; i < c.forms.length; i++) {
					if (c.forms[i] == null)
						break;
					for (int j = 0; j < 5; j++) {
						Form f = fs[0][j];
						if (f == null)
							break;
						if (f.unit != c.forms[i].unit)
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
			for (Form form : com)
				if (fs[1][i] != null && fs[1][i].unit == form.unit) {
					fs[1][i] = null;
					break;
				}
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
			Form c = com[p++];
			setFS(c, i++);
			r++;
		}
		renew();
	}

	/**
	 * set level record of an Unit
	 */
	public synchronized void setLv(Unit u, int[] lv) {
		boolean sub = updating;
		updating = true;

		Level l = map.get(u.id);

		if (l != null) {
			l.setLvs(lv);
		} else {
			l = new Level(lv);

			map.put(u.id, l);
		}

		if (!sub)
			renewEForm();
		updating &= sub;
	}

	/**
	 * set orb data of an Unit
	 */
	public synchronized void setOrb(Unit u, int[] lvs, int[][] orbs) {
		// lvs must be generated before doing something with orbs
		boolean sub = updating;
		updating = true;
		Level l = map.get(u.id);
		if (l != null) {
			l.setLvs(lvs);
			l.setOrbs(orbs);
		} else {
			l = new Level(lvs, orbs);
			map.put(u.id, l);
		}
		if (!sub)
			renewEForm();
		updating &= sub;
	}

	/**
	 * return whether implementing this combo will replace other combo
	 */
	public boolean willRem(Combo c) {
		int free = 0;
		for (int i = 0; i < 5; i++)
			if (fs[0][i] == null)
				free++;
			else if (loc[i] == 0) {
				boolean b = true;
				for (Form is : c.forms)
					if (fs[0][i].unit == is.unit) {
						b = false;
						break;
					}
				if (b)
					free++;
			}
		return free < occupance(c);
	}

	/**
	 * set slot using 1 dim index
	 */
	protected void setFS(Form f, int i) {
		fs[i / 5][i % 5] = f;
	}

	/**
	 * get Form from 1 dim index
	 */
	private Form getFS(int i) {
		return fs[i / 5][i % 5];
	}

	/**
	 * check combo information
	 */
	private void renewCombo() {
		List<Combo> tcom = new ArrayList<>();
		inc = new int[C_TOT];
		loc = new int[5];
		for (PackData p : UserProfile.getAllPacks())
			for (Combo c : p.combos) {
				boolean b = true;
				for (int i = 0; i < c.forms.length; i++) {
					Form fu = c.forms[i];
					if (fu == null)
						break;
					boolean b0 = false;
					for (int j = 0; j < 5; j++) {
						Form f = fs[0][j];
						if (f == null)
							break;
						if (f.unit != fu.unit || f.fid < fu.fid)
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
					inc[c.type] += CommonStatic.getBCAssets().values[c.type][c.lv];
					for (int i = 0; i < c.forms.length; i++)
						for (int j = 0; j < 5; j++) {
							Form fu = c.forms[i];
							Form f = fs[0][j];
							if (f == null)
								continue;
							if (f.unit == fu.unit && f.fid >= fu.fid)
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
					efs[i][j] = new EForm(fs[i][j], getLv(fs[i][j]));
	}

	private void validate() {
		for (int i = 0; i < 10; i++)
			if (getFS(i) != null) {
				Identifier<Unit> id = getFS(i).uid;
				int f = getFS(i).fid;
				Unit u = Identifier.get(id);
				if (u == null || u.forms[f] == null)
					setFS(null, i);
			}
		arrange();
	}

	/**
	 * read data from file, support multiple version
	 */
	private void zread(int ver, InStream is) {
		int val = getVer(is.nextString());
		if (val >= 400)
			zread$000400(is);
	}

	private void zread$000400(InStream is) {
		int n = is.nextInt();
		for (int i = 0; i < n; i++) {
			int uid = is.nextInt();
			int fid = is.nextInt();
			setFS(Identifier.parseInt(uid, Unit.class).get().forms[fid], i);
		}
		int m = is.nextInt();
		for (int i = 0; i < m; i++) {
			int uid = is.nextInt();
			int[] lv = is.nextIntsB();
			Unit u = Identifier.getOr(Identifier.parseInt(uid, Unit.class), Unit.class);
			int[][] orbs = null;
			int existing = is.nextInt();
			if (existing == 1) {
				orbs = is.nextIntsBB();
			}
			if (u != null)
				map.put(u.id, new Level(lv, orbs));
		}
		arrange();
	}

}
