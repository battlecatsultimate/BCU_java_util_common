package common.util.anim;

import java.io.PrintStream;
import java.util.Queue;

import common.CommonStatic;
import common.io.InStream;
import common.io.OutStream;
import common.system.fake.FakeImage;
import common.system.files.FileData;
import common.util.Data;

public class ImgCut extends Data implements Cloneable {

	public static ImgCut newIns(FileData f) {
		try {
			return new ImgCut(f.readLine());
		} catch (Exception e) {
			e.printStackTrace();
			return new ImgCut();
		}
	}

	public static ImgCut newIns(String path) {
		return CommonStatic.def.readSave(path, f -> f == null ? new ImgCut() : new ImgCut(f));
	}

	public String name;
	public int n;
	public int[][] cuts;
	public String[] strs;

	public ImgCut() {
		n = 1;
		cuts = new int[][] { { 0, 0, 1, 1 } };
		strs = new String[] { "default" };
	}

	protected ImgCut(Queue<String> qs) {
		qs.poll();
		qs.poll();
		name = restrict(qs.poll());
		n = Integer.parseInt(qs.poll().trim());
		cuts = new int[n][4];
		strs = new String[n];
		for (int i = 0; i < n; i++) {
			String[] ss = qs.poll().trim().split(",");
			for (int j = 0; j < 4; j++)
				cuts[i][j] = Integer.parseInt(ss[j].trim());
			if (ss.length == 5)
				strs[i] = restrict(ss[4]);
			else
				strs[i] = "";
		}
	}

	private ImgCut(ImgCut ic) {
		name = ic.name;
		n = ic.n;
		cuts = new int[n][];
		for (int i = 0; i < n; i++)
			cuts[i] = ic.cuts[i].clone();
		strs = ic.strs.clone();
	}

	@Override
	public ImgCut clone() {
		return new ImgCut(this);
	}

	public FakeImage[] cut(FakeImage bimg) {
		int w = bimg.getWidth();
		int h = bimg.getHeight();
		FakeImage[] parts = new FakeImage[n];
		for (int i = 0; i < n; i++) {
			int[] cut = cuts[i].clone();
			if (cut[0] < 0)
				cut[0] = 0;
			if (cut[1] < 0)
				cut[1] = 0;
			if (cut[0] > w - 2)
				cut[0] = w - 2;
			if (cut[1] > h - 2)
				cut[1] = h - 2;
			if (cut[2] <= 0)
				cut[2] = 1;
			if (cut[3] <= 0)
				cut[3] = 1;
			if (cut[2] + cut[0] > w - 1)
				cut[2] = w - 1 - cut[0];
			if (cut[3] + cut[1] > h - 1)
				cut[3] = h - 1 - cut[1];
			parts[i] = bimg.getSubimage(cut[0], cut[1], cut[2], cut[3]);
		}
		return parts;
	}

	public void write(PrintStream ps) {
		ps.println("[imgcut]");
		ps.println("0");
		ps.println(name);
		ps.println(n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < 4; j++)
				ps.print(cuts[i][j] + ",");
			ps.println(strs[i]);
		}
	}

	protected void restore(InStream is) {
		n = is.nextInt();
		cuts = is.nextIntsBB();
		strs = new String[n];
		for (int i = 0; i < n; i++)
			strs[i] = is.nextString();
	}

	protected void write(OutStream os) {
		os.writeInt(n);
		os.writeIntBB(cuts);
		for (String str : strs)
			os.writeString(str);
	}

}
