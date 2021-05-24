package common.util.stage;

import common.battle.StageBasis;
import common.io.InStream;
import common.io.json.JsonClass;
import common.io.json.JsonClass.JCConstructor;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonField;
import common.io.json.JsonField.GenType;
import common.pack.FixIndexList;
import common.pack.Identifier;
import common.pack.PackData.PackDesc;
import common.system.Copable;
import common.util.Data;
import common.util.unit.AbEnemy;
import common.util.unit.EneRand;
import common.util.unit.Enemy;
import java.util.*;
import java.util.Map.Entry;

@JsonClass
public class SCDef implements Copable<SCDef> {

	@JsonClass(noTag = NoTag.LOAD)
	public static class Line implements Cloneable {
		public Identifier<AbEnemy> enemy;
		public int number, boss, multiple, group;
		public int spawn_0, spawn_1, respawn_0, respawn_1;
		public int castle_0, castle_1, layer_0, layer_1;
		public int mult_atk;

		@JCConstructor
		public Line() {
		}

		public Line(int[] arr) {
			enemy = Identifier.parseInt(arr[E], AbEnemy.class);
			number = arr[N];
			boss = arr[B];
			multiple = arr[M];
			group = arr[G];
			spawn_0 = arr[S0];
			respawn_0 = arr[R0];
			castle_0 = arr[C0];
			layer_0 = arr[L0];
			spawn_1 = arr[S1];
			respawn_1 = arr[R1];
			castle_1 = arr[C1];
			layer_1 = arr[L1];
			mult_atk = arr[M1];
		}

		@Override
		public Line clone() {
			try {
				return (Line) super.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	@Deprecated
	public static final int SIZE = 14, E = 0, N = 1, S0 = 2, R0 = 3, R1 = 4, C0 = 5, L0 = 6, L1 = 7, B = 8, M = 9,
			S1 = 10, C1 = 11, G = 12, M1 = 13;

	public static SCDef zread(InStream is) {
		int t = is.nextInt();
		int ver = Data.getVer(is.nextString());
		if (t == 0) {
			if (ver >= 402) {
				int n = is.nextInt();
				int m = is.nextInt();
				SCDef scd = new SCDef(n);
				int[] tmp = new int[SIZE];
				for (int i = 0; i < n; i++) {
					Arrays.fill(tmp, 0);
					for (int j = 0; j < m; j++)
						tmp[j] = is.nextInt();
					if (m < 14)
						tmp[M1] = tmp[M];
					scd.datas[i] = new Line(tmp);
				}
				scd.sdef = is.nextInt();
				n = is.nextInt();
				for (int i = 0; i < n; i++)
					scd.smap.put(Identifier.parseInt(is.nextInt(), AbEnemy.class), is.nextInt());
				n = is.nextInt();
				for (int i = 0; i < n; i++) {
					SCGroup scg = SCGroup.zread(is);
					scd.sub.set(scg.id, scg);
				}
				return scd;
			}
		}
		return null;
	}

	@JsonField
	public Line[] datas;
	@JsonField(gen = GenType.FILL)
	public final FixIndexList<SCGroup> sub = new FixIndexList<>(SCGroup.class);
	@JsonField(generic = { Identifier.class, Integer.class })
	public final TreeMap<Identifier<AbEnemy>, Integer> smap = new TreeMap<>();
	@JsonField
	public int sdef = 0;

	@JCConstructor
	public SCDef() {
	}

	protected SCDef(InStream is, int ver) {
		if (ver >= 305) {
			int n = is.nextByte();
			datas = new Line[n];
			int[] tmp = new int[SIZE];
			for (int i = 0; i < n; i++) {
				Arrays.fill(tmp, 0);
				for (int j = 0; j < 10; j++)
					tmp[j] = is.nextInt();
				tmp[M1] = tmp[M];
				datas[i] = new Line(tmp);
			}
		} else if (ver >= 203) {
			int n = is.nextByte();
			datas = new Line[n];
			int[] tmp = new int[SIZE];
			for (int i = 0; i < n; i++) {
				Arrays.fill(tmp, 0);
				for (int j = 0; j < 10; j++)
					tmp[j] = is.nextInt();
				if (tmp[5] < 100)
					tmp[2] *= -1;
				tmp[M1] = tmp[M];
				datas[i] = new Line(tmp);
			}
		} else
			datas = new Line[0];
	}

	protected SCDef(int s) {
		datas = new Line[s];
	}

	public int allow(StageBasis sb, AbEnemy e) {
		Integer o = smap.get(e.getID());
		o = o == null ? sdef : o;
		if (allow(sb, o, e))
			return o;
		return -1;
	}

	public boolean allow(StageBasis sb, int val, AbEnemy en) {
		if (sb.entityCount(1) >= sb.st.max - ((Enemy)en).de.getWill())
			return false;
		if (val < 0 || val > 1000 || sub.get(val) == null)
			return true;
		SCGroup g = sub.get(val);
		return sb.entityCount(1, val) < g.getMax(sb.est.star);
	}

	public boolean contains(Enemy e) {
		for (Line dat : datas) {
			if(dat.enemy.cls == EneRand.class) {
				EneRand rand = (EneRand) Identifier.get(dat.enemy);

				if(rand != null && rand.contains(e.id, dat.enemy))
					return true;
			} else if(dat.enemy.cls == Enemy.class) {
				if (dat.enemy.equals(e.id))
					return true;
			}
		}
		return false;
	}

	@Override
	public SCDef copy() {
		SCDef ans = new SCDef(datas.length);
		for (int i = 0; i < datas.length; i++)
			ans.datas[i] = datas[i].clone();
		ans.sdef = sdef;
		smap.forEach(ans.smap::put);
		sub.forEach((i, e) -> ans.sub.set(i, e.copy(i)));
		return ans;
	}

	public Set<Enemy> getAllEnemy() {
		Set<Enemy> l = new TreeSet<>();
		for (Line dat : datas)
			l.addAll(Identifier.getOr(dat.enemy, AbEnemy.class).getPossible());
		for (AbEnemy e : getSummon())
			l.addAll(e.getPossible());
		return l;
	}

	public Line[] getSimple() {
		return datas;
	}

	public Line getSimple(int i) {
		return datas[i];
	}

	@SuppressWarnings("unchecked")
	public Entry<Identifier<AbEnemy>, Integer>[] getSMap() {
		return smap.entrySet().toArray(new Entry[0]);
	}

	public Set<AbEnemy> getSummon() {
		Set<AbEnemy> ans = new TreeSet<>();
		Set<AbEnemy> temp = new TreeSet<>();
		Set<Enemy> pre = new TreeSet<>();
		Set<Enemy> post = new TreeSet<>();
		for (Line line : datas) {
			AbEnemy e = Identifier.get(line.enemy);
			if (e != null)
				pre.addAll(e.getPossible());
		}
		while (pre.size() > 0) {
			for (Enemy e : pre)
				temp.addAll(e.de.getSummon());
			ans.addAll(temp);
			post.addAll(pre);
			pre.clear();
			for (AbEnemy e : temp)
				pre.addAll(e.getPossible());
			pre.removeAll(post);
			temp.clear();
		}
		return ans;
	}

	public boolean isSuitable(PackDesc pack) {
		for (Line ints : datas) {
			if (ints.enemy.pack.equals(Identifier.DEF))
				continue;
			boolean b = ints.enemy.pack.equals(pack.id);
			for (String rel : pack.dependency)
				b |= ints.enemy.pack.equals(rel);
			if (!b)
				return false;
		}
		return true;
	}

	public boolean isTrail() {
		for (Line data : datas)
			if (data.castle_0 > 100)
				return true;
		return false;
	}

}
