package common.util.stage;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import common.battle.StageBasis;
import common.io.InStream;
import common.io.OutStream;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.io.json.JsonClass.JCConcstructor;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonField.GenType;
import common.system.Copable;
import common.system.FixIndexList;
import common.util.Data;
import common.util.pack.Pack;
import common.util.unit.AbEnemy;
import common.util.unit.Enemy;
import common.util.unit.EnemyStore;

@JsonClass
public class SCDef implements Copable<SCDef> {

	@JsonClass(noTag = NoTag.LOAD)
	public static class Line {
		public int enemy, number, boss, multiple, group;
		public int spawn_0, spawn_1, respawn_0, respawn_1;
		public int castle_0, castle_1, layer_0, layer_1;
		public int mult_atk;

		@JCConcstructor
		public Line() {
		}

		public Line(int[] arr) {
			enemy = arr[E];
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
					scd.smap.put(is.nextInt(), is.nextInt());
				n = is.nextInt();
				for (int i = 0; i < n; i++) {
					SCGroup scg = SCGroup.zread(is);
					scd.sub.set(scg.id, scg);
				}
				return scd;
			}
			if (ver >= 401) {
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
					scd.smap.put(is.nextInt(), is.nextInt());
				n = is.nextInt();
				int id;
				for (int i = 0; i < n; i++)
					scd.sub.set(id = is.nextInt(), new SCGroup(id, is.nextInt()));
				return scd;
			} else if (ver >= 400) {
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
				return scd;
			}
		}
		return null;
	}

	@JsonField
	public Line[] datas;
	@JsonField(gen = GenType.FILL)
	public final FixIndexList<SCGroup> sub = new FixIndexList<>(new SCGroup[1000]);
	@JsonField(generic = { Integer.class, Integer.class })
	public final Map<Integer, Integer> smap = new TreeMap<>();
	@JsonField
	public int sdef = 0;

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

	protected SCDef() {
	}

	protected SCDef(int s) {
		datas = new Line[s];
	}

	public int allow(StageBasis sb, AbEnemy e) {
		Integer o = smap.get(e.getID());
		o = o == null ? sdef : o;
		if (allow(sb, o))
			return o;
		return -1;
	}

	public boolean allow(StageBasis sb, int val) {
		if (sb.entityCount(1) >= sb.st.max)
			return false;
		if (val < 0 || val > 1000 || sub.get(val) == null)
			return true;
		SCGroup g = sub.get(val);
		return sb.entityCount(1, val) < g.getMax(sb.est.star);
	}

	public boolean contains(Enemy e) {
		for (Line dat : datas)
			if (dat.enemy == e.id)
				return true;
		return false;
	}

	@Override
	public SCDef copy() {
		SCDef ans = new SCDef(datas.length);
		for (int i = 0; i < datas.length; i++)
			ans.datas[i] = datas[i].clone();
		ans.sdef = sdef;
		smap.forEach((e, i) -> ans.smap.put(e, i));
		sub.forEach((i, e) -> ans.sub.set(i, e.copy(i)));
		return ans;
	}

	public Set<Enemy> getAllEnemy() {
		Set<Enemy> l = new TreeSet<>();
		for (Line dat : datas)
			l.addAll(EnemyStore.getAbEnemy(dat.enemy, false).getPossible());
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

	public int[][] getSMap() {
		int[][] ans = new int[smap.size()][2];
		int[] i = new int[1];
		smap.forEach((e, g) -> {
			ans[i[0]][0] = e;
			ans[i[0]++][1] = g;
		});
		return ans;
	}

	public Set<AbEnemy> getSummon() {
		Set<AbEnemy> ans = new TreeSet<>();
		Set<AbEnemy> temp = new TreeSet<>();
		Set<Enemy> pre = new TreeSet<>();
		Set<Enemy> post = new TreeSet<>();
		for (Line line : datas) {
			AbEnemy e = EnemyStore.getAbEnemy(line.enemy, false);
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

	public boolean isSuitable(Pack p) {
		for (Line ints : datas) {
			if (ints.enemy < 1000)
				continue;
			int pac = ints.enemy / 1000;
			boolean b = pac == p.id;
			for (int rel : p.rely)
				b |= pac == rel;
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

	public void merge(int id, int pid, int[] esind) {
		for (Line dat : datas)
			if (dat.enemy / 1000 == pid)
				dat.enemy = esind[dat.enemy % 1000] + id * 1000;
	}

	public int relyOn(int p) {
		for (Line data : datas)
			if (data.enemy / 1000 == p)
				return Pack.RELY_ENE;
		return -1;
	}

	public void removePack(int p) {
		for (Line data : datas)
			if (data.enemy / 1000 == p)
				data.enemy = 0;
	}

	public OutStream write() {
		OutStream os = OutStream.getIns();
		os.writeInt(0);
		os.writeString("0.4.2");
		os.writeInt(datas.length);
		os.writeInt(SIZE);
		for (int i = 0; i < datas.length; i++) {
			os.writeInt(datas[i].enemy);
			os.writeInt(datas[i].number);
			os.writeInt(datas[i].spawn_0);
			os.writeInt(datas[i].respawn_0);
			os.writeInt(datas[i].respawn_1);
			os.writeInt(datas[i].castle_0);
			os.writeInt(datas[i].layer_0);
			os.writeInt(datas[i].layer_1);
			os.writeInt(datas[i].boss);
			os.writeInt(datas[i].multiple);
			os.writeInt(datas[i].spawn_1);
			os.writeInt(datas[i].castle_1);
			os.writeInt(datas[i].group);
		}

		os.writeInt(sdef);
		os.writeInt(smap.size());
		smap.forEach((e, i) -> os.writeIntsN(e, i));
		os.writeInt(sub.size());
		sub.forEach((i, e) -> e.write(os));
		os.terminate();
		return os;
	}
}
