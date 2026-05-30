package common.util.anim;

import common.io.InStream;
import common.io.OutStream;
import common.util.Data;

import java.io.PrintStream;
import java.util.Queue;

public class Part extends Data implements Cloneable, Comparable<Part> {

	public int[] ints = new int[5];
	public String name;
	public int n, max, off, fir;
	public float frame, vd;// for editor only
	public int[][] moves;

	public Part() {
		this(0, 5);
	}

	public Part(int id, int modif) {
		ints = new int[] { id, modif, -1, 0, 0 };
		name = "";
		n = 0;
		moves = new int[0][];
	}

	protected Part(Queue<String> qs, boolean isOld) {
		String[] ss = qs.poll().trim().split(",");
		for (int i = 0; i < 5; i++) {
			int v = Integer.parseInt(ss[i].trim());
			ints[i] = (isOld && i == 1 && v == 8) ? 53 : v;
		}
		if (ss.length == 6)
			name = restrict(ss[5]);
		else
			name = "";
		n = Integer.parseInt(qs.poll().trim());
		moves = new int[n][4];
		for (int i = 0; i < n; i++) {
			ss = qs.poll().trim().split(",");
			for (int j = 0; j < 4; j++)
				moves[i][j] = Integer.parseInt(ss[j].trim());
		}
		validate();
	}

	private Part(Part p) {
		ints = p.ints.clone();
		name = p.name;
		n = p.n;
		moves = new int[n][];
		for (int i = 0; i < n; i++)
			moves[i] = p.moves[i].clone();
		off = p.off;
		validate();
	}

	public void check(AnimD<?, ?> anim) {
		int mms = anim.mamodel.n;
		int ics = anim.imgcut.n;
		if (ints[0] >= mms)
			ints[0] = 0;
		if (ints[0] < 0)
			ints[0] = 0;
		if (ints[1] == 2)
			for (int[] move : moves)
				if (move[1] >= ics || move[1] < 0)
					move[1] = 0;
	}

	@Override
	public Part clone() {
		return new Part(this);
	}

	@Override
	public int compareTo(Part o) {
		return Integer.compare(ints[0], o.ints[0]);
	}

	public void validate() {
		int doff = 0;
		if (n != 0 && (moves[0][0] - off < 0 || ints[2] != 1))
			doff -= moves[0][0];
		for (int i = 0; i < n; i++)
			moves[i][0] += doff;
		off += doff;
		fir = moves.length == 0 ? 0 : moves[0][0];
		max = n > 0 ? moves[n - 1][0] : 0;
	}

	protected void ensureLast(EPart[] es) {
		if (n == 0)
			return;
		frame = moves[n - 1][0];
		es[ints[0]].alter(ints[1], vd = moves[n - 1][1]);
	}

	protected int getMax() {
		if(ints[2] != -1) {
			return ints[2] > 1 ? fir + (max - fir) * ints[2] - off : max - off;
		} else {
			return max - Math.min(off, 0);
		}
	}

	protected void restore(InStream is) {
		n = is.nextInt();
		max = is.nextInt();
		off = is.nextInt();
		ints = is.nextIntsB();
		moves = is.nextIntsBB();
		name = is.nextString();
		validate();
	}

	protected void update(float f, EPart[] es) {
		frame = f;

		for (int i = 0; i < n; i++) {
			if (frame == moves[i][0]) {
				es[ints[0]].alter(ints[1], vd = moves[i][1]);
			} else if (i < n - 1 && frame > moves[i][0] && frame < moves[i + 1][0]) {
				if (ints[1] > 1) {
					int f0 = moves[i][0];
					int v0 = moves[i][1];
					int f1 = moves[i + 1][0];
					int v1 = moves[i + 1][1];

					float realFrame = frame;

					if (f1 - f0 == 1) {
						realFrame = (int) frame;
					}

					float ti;

					if (moves[i][2] == 1 || ints[1] == 13 || ints[1] == 14) {
						ti = 0;
					} else if (moves[i][2] == 0) {
						ti = (realFrame - f0) / (f1 - f0);
					} else if (moves[i][2] == 2) {
						ti = (realFrame - f0) / (f1 - f0);

						float easePower = moves[i][3] != 0 ? moves[i][3] : 1f;
						float tiClamped = Math.min(1f, Math.max(0f, ti));

						float easeFactor;

						if (easePower >= 0) {
							easeFactor = (float) (1.0 - Math.sqrt(1.0 - Math.pow(tiClamped, easePower)));
						} else {
							easeFactor = (float) (Math.sqrt(1.0 - Math.pow(1.0 - tiClamped, -easePower)));
						}

						if (!Float.isNaN(easeFactor)) {
							ti = easeFactor;
						}
					} else if (moves[i][2] == 3) {
						vd = ease3(i, realFrame);
						es[ints[0]].alter(ints[1], vd);
						break;
					} else if (moves[i][2] == 4) {
						ti = (realFrame - f0) / (f1 - f0);

						if (moves[i][3] > 0)
							ti = (float) (1 - Math.cos(ti * Math.PI / 2));
						else if (moves[i][3] < 0)
							ti = (float) (Math.sin(ti * Math.PI / 2));
						else
							ti = (float) ((1 - Math.cos(ti * Math.PI)) / 2);
					} else {
						ti = 1f;
					}

					if (ints[1] == 2)
						if (v1 - v0 < 0)
							vd = (int) Math.ceil((v1 - v0) * ti + v0);
						else
							vd = (int) ((v1 - v0) * ti + v0);
					else
						vd = v0 + (int) ((v1 - v0) * ti);

					es[ints[0]].alter(ints[1], vd);
					break;
				} else if (ints[1] == 0) {
					es[ints[0]].alter(ints[1], moves[i][1]);
				}
			}
		}

		if (n > 0 && frame > moves[n - 1][0])
			ensureLast(es);
	}

	protected void write(OutStream os) {
		os.writeInt(n);
		os.writeInt(max);
		os.writeInt(off);
		os.writeIntB(ints);
		os.writeIntBB(moves);
		os.writeString(name);
	}

	protected void write(PrintStream ps) {
		for (int val : ints)
			ps.print(val + ",");
		ps.println(name);
		ps.println(moves.length);
		for (int[] move : moves) {
			ps.print(move[0] - off + ",");
			for (int i = 1; i < move.length; i++)
				ps.print(move[i] + ",");
			ps.println();
		}
	}

	private int ease3(int i, float frame) {
		int low = i;
		int high = i;
		for (int j = i - 1; j >= 0; j--)
			if (moves[j][2] == 3)
				low = j;
			else
				break;
		for (int j = i + 1; j < moves.length; j++)
			if (moves[high = j][2] != 3)
				break;
		double sum = 0;
		for (int j = low; j <= high; j++) {
			double val = moves[j][1] * 4096;
			for (int k = low; k <= high; k++)
				if (j != k)
					val *= 1.0 * (frame - moves[k][0]) / (moves[j][0] - moves[k][0]);
			sum += val;
		}
		return (int) (sum / 4096);
	}

}
