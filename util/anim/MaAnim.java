package common.util.anim;

import common.io.InStream;
import common.io.OutStream;
import common.system.files.FileData;
import common.system.files.VFile;
import common.util.BattleStatic;
import common.util.Data;

import java.io.PrintStream;
import java.util.Queue;

public class MaAnim extends Data implements BattleStatic {

	public static MaAnim newIns(FileData f) {
		if(f == null) {
			return new MaAnim();
		}

		try {
			return new MaAnim(f.readLine());
		} catch (Exception e) {
			e.printStackTrace();
			return new MaAnim();
		}
	}

	public static MaAnim newIns(String str) {
		return new MaAnim(VFile.readLine(str));
	}

	public int n;
	public Part[] parts;

	public int max = 1, len = 1;

	public MaAnim() {
		n = 0;
		parts = new Part[0];
	}

	public MaAnim(Queue<String> qs) {
		qs.poll();
		qs.poll();
		n = Integer.parseInt(qs.poll().trim());
		parts = new Part[n];
		for (int i = 0; i < n; i++)
			parts[i] = new Part(qs);
		validate();
	}

	private MaAnim(MaAnim ma) {
		n = ma.n;
		parts = new Part[n];
		for (int i = 0; i < n; i++)
			parts[i] = ma.parts[i].clone();
		validate();
	}

	@Override
	public MaAnim clone() {
		return new MaAnim(this);
	}

	public void revert() {
		for (Part p : parts)
			if (p.ints[1] == 11)
				for (int[] move : p.moves)
					move[1] *= -1;
	}

	public void validate() {
		for (int i = 0; i < parts.length; i++) {
			if(parts[i].ints[1] == 2) {
				for(int j = 0; j < parts[i].moves.length; j++) {
					if(parts[i].moves[j][1] < 0) {
						parts[i].moves[j][1] = 0;
					}
				}
			}
		}

		max = 1;
		for (int i = 0; i < n; i++)
			if (parts[i].getMax() > max)
				max = parts[i].getMax();
		len = 1;
		for (int i = 0; i < n; i++)
			len = Math.max(len, parts[i].getMax());
	}

	public void write(PrintStream ps) {
		ps.println("[maanim]");
		ps.println("1");
		ps.println(parts.length);
		for (Part p : parts)
			p.write(ps);
	}

	protected void restore(InStream is) {
		n = is.nextInt();
		parts = new Part[n];
		for (int i = 0; i < n; i++) {
			parts[i] = new Part();
			parts[i].restore(is);
		}
		validate();
	}

	protected void update(float f, EAnimD<?> eAnim, boolean rotate) {
		if (rotate)
			f %= max + 1;

		if (f == 0) {
			for (EPart e : eAnim.ent)
				e.setValue();
		}

		for (int i = 0; i < n; i++) {
			int loop = parts[i].ints[2];
			int smax = parts[i].max;
			int fir = parts[i].fir;
			int lmax = smax - fir;

			boolean prot = rotate || loop == -1;

			float frame;

			if (prot) {
				int mf = loop == -1 ? smax : max + 1;

				frame = mf == 0 ? 0 : (f + parts[i].off) % mf;
			} else {
				frame = f + parts[i].off;
			}

			if (loop > 0 && lmax != 0) {
				if (frame > fir + loop * lmax) {
					parts[i].ensureLast(eAnim.ent);
					continue;
				}
				if (frame <= fir)
					;
				else if (frame < fir + loop * lmax)
					frame = fir + (frame - fir) % lmax;
				else
					frame = smax;
			}

			parts[i].update(frame, eAnim.ent);
		}

		eAnim.sort();
	}

	protected void write(OutStream os) {
		os.writeInt(n);
		for (Part p : parts)
			p.write(os);
	}

}
