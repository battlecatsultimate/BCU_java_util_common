package common.util.anim;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import common.CommonStatic;
import common.CommonStatic.EditLink;
import common.io.InStream;
import common.io.OutStream;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.system.files.FDByte;
import common.system.files.FileData;
import common.system.files.VFile;
import common.util.Res;
import main.MainBCU;
import main.Opts;
import main.Printer;

public class AnimC extends AnimU {

	public static String getAvailable(String str) {
		File folder = new File("./res/anim/");
		if (!folder.exists())
			return str;
		File[] fs = new File("./res/anim/").listFiles();
		Set<String> strs = new HashSet<>();
		for (int i = 0; i < fs.length; i++)
			strs.add(fs[i].getName());
		while (strs.contains(str))
			str += "'";
		return str;
	}

	private boolean saved = false;
	public boolean inPool;
	public EditLink link;
	public Stack<History> history = new Stack<>();
	public String name = "";
	public String prev;

	public AnimC(InStream is) {
		name = "local animation";
		inPool = false;
		loaded = true;
		partial = true;
		saved = true;
		try {
			num = FakeImage.read(is.nextBytesI());
		} catch (IOException e) {
			e.printStackTrace();
		}
		imgcut = ImgCut.newIns(new FDByte(is.nextBytesI()));
		mamodel = MaModel.newIns(new FDByte(is.nextBytesI()));
		int n = is.nextInt();
		anims = new MaAnim[n];
		for (int i = 0; i < n; i++)
			anims[i] = MaAnim.newIns(new FDByte(is.nextBytesI()));
		parts = imgcut.cut(num);
		if (!is.end()) {
			VImg vimg = new VImg(is.nextBytesI());
			vimg.mark("uni or edi");
			if (vimg.getImg().getHeight() == 32)
				edi = vimg;
			else
				uni = vimg;
		}
		if (!is.end())
			uni = new VImg(is.nextBytesI());
		if (uni != null && uni != Res.slot[0])
			uni.mark("uni");
		if (edi != null)
			edi.mark("edi");
		standardize();
		history("initial");
	}

	public AnimC(String st) {
		inPool = true;
		prev = "./res/anim/";
		name = st;
		VFile<? extends FileData> f = VFile.getFile(prev + name + "/edi.png");
		if (f != null)
			edi = new VImg(f);
		f = VFile.getFile(prev + name + "/uni.png");
		if (f != null)
			uni = new VImg(f);
		if (uni != null && uni != Res.slot[0])
			uni.mark("uni");
		if (edi != null)
			edi.mark("edi");
	}

	public AnimC(String str, AnimD ori) {
		inPool = true;
		prev = "./res/anim/";
		name = str;
		loaded = true;
		partial = true;
		imgcut = ori.imgcut.clone();
		mamodel = ori.mamodel.clone();
		if (mamodel.confs.length < 1)
			mamodel.confs = new int[2][6];
		anims = new MaAnim[7];
		for (int i = 0; i < 7; i++)
			if (i < ori.anims.length)
				anims[i] = ori.anims[i].clone();
			else
				anims[i] = new MaAnim();
		num = ori.getNum();
		parts = imgcut.cut(num);
		File f = new File(prev + name + "/" + name + ".png");
		CommonStatic.def.check(f);
		try {
			FakeImage.write(num, "PNG", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		reloImg();
		if (ori instanceof AnimU) {
			AnimU au = (AnimU) ori;
			edi = au.edi;
			uni = au.uni;
		}
		saveIcon();
		saveUni();
		standardize();
		history("initial");
	}

	public void createNew() {
		loaded = true;
		imgcut = new ImgCut();
		mamodel = new MaModel();
		anims = new MaAnim[7];
		for (int i = 0; i < 7; i++)
			anims[i] = new MaAnim();
		parts = imgcut.cut(num);
		history("initial");
	}

	public void delete() {
		CommonStatic.def.delete(new File(prev + name + "/"));
	}

	public String getUndo() {
		return history.peek().name;
	}

	public void hardSave(String str) {
		if (prev == null)
			prev = "./res/anim/";
		if (name == null)
			name = AnimC.getAvailable(MainBCU.validate(str));
		saved = false;
		save();
		saveImg();
		saveIcon();
		saveUni();
	}

	public void ICedited() {
		check();
		parts = imgcut.cut(num);
	}

	public boolean isSaved() {
		return saved;
	}

	@Override
	public void load() {
		loaded = true;
		try {
			String pre = prev + name + "/" + name;
			num = VFile.getFile(pre + ".png").getData().getImg();
			imgcut = ImgCut.newIns(pre + ".imgcut");
			if (num == null) {
				Printer.e("AnimC", 147, "can't read png: " + pre);
				Opts.loadErr("sprite missing: " + pre + ".png");
				CommonStatic.def.exit(false);
			}
			parts = imgcut.cut(num);
			partial();
			if (edi != null)
				edi.check();
			if (uni != null)
				uni.check();
			history("initial");
		} catch (Exception e) {
			Opts.loadErr("Error in loading custom animation: " + name);
			e.printStackTrace();
			CommonStatic.def.exit(false);
		}
		validate();
	}

	public void merge(AnimC a, int x, int y) {
		ImgCut ic0 = imgcut;
		ImgCut ic1 = a.imgcut;
		int icn = ic0.n;
		ic0.n += ic1.n;
		ic0.cuts = Arrays.copyOf(ic0.cuts, ic0.n);
		for (int i = 0; i < icn; i++)
			ic0.cuts[i] = ic0.cuts[i].clone();
		ic0.strs = Arrays.copyOf(ic0.strs, ic0.n);
		for (int i = 0; i < ic1.n; i++) {
			int[] data = ic0.cuts[i + icn] = ic1.cuts[i].clone();
			data[0] += x;
			data[1] += y;
			ic0.strs[i + icn] = ic1.strs[i];
		}

		MaModel mm0 = mamodel;
		MaModel mm1 = a.mamodel;
		int mmn = mm0.n;
		mm0.n += mm1.n;
		mm0.parts = Arrays.copyOf(mm0.parts, mm0.n);
		for (int i = 0; i < mmn; i++)
			mm0.parts[i] = mm0.parts[i].clone();
		mm0.strs0 = Arrays.copyOf(mm0.strs0, mm0.n);
		int[] fir = mm0.parts[0];
		for (int i = 0; i < mm1.n; i++) {
			int[] data = mm0.parts[i + mmn] = mm1.parts[i].clone();
			if (data[0] != -1)
				data[0] += mmn;
			else {
				data[0] = 0;
				data[8] = data[8] * 1000 / fir[8];
				data[9] = data[9] * 1000 / fir[9];
				data[4] = data[6] * data[8] / 1000 - fir[6];
				data[5] = data[7] * data[9] / 1000 - fir[7];
			}
			data[2] += icn;
			mm0.strs0[i + mmn] = mm1.strs0[i];
		}

		for (int i = 0; i < 7; i++) {
			MaAnim ma0 = anims[i];
			MaAnim ma1 = a.anims[i];
			int man = ma0.n;
			ma0.n += ma1.n;
			ma0.parts = Arrays.copyOf(ma0.parts, ma0.n);
			for (int j = 0; j < man; j++)
				ma0.parts[j] = ma0.parts[j].clone();
			for (int j = 0; j < ma1.n; j++) {
				Part p = ma0.parts[j + man] = ma1.parts[j].clone();
				p.ints[0] += mmn;
				if (p.ints[1] == 2)
					for (int[] data : p.moves)
						data[1] += icn;
			}
		}
	}

	public void reloImg() {
		num = VFile.getFile(prev + name + "/" + name + ".png").getData().getImg();
		ICedited();
	}

	public void renameTo(String str) {
		if (uni != null)
			uni.check();
		if (edi != null)
			edi.check();
		CommonStatic.def.delete(new File(prev + name + "/"));
		name = str;
		saveImg();
		saveIcon();
		saveUni();
		unSave("rename (not applicapable for undo)");
	}

	public void resize(double d) {
		for (int[] l : imgcut.cuts)
			for (int i = 0; i < l.length; i++)
				l[i] *= d;
		mamodel.parts[0][8] /= d;
		mamodel.parts[0][9] /= d;
		for (int[] l : mamodel.parts) {
			l[4] *= d;
			l[5] *= d;
			l[6] *= d;
			l[7] *= d;
		}
		for (MaAnim ma : anims)
			for (Part p : ma.parts)
				if (p.ints[1] >= 4 && p.ints[1] <= 7)
					for (int[] x : p.moves)
						x[1] *= d;
	}

	public void restore() {
		history.pop();
		InStream is = history.peek().data.translate();
		imgcut.restore(is);
		ICedited();
		mamodel.restore(is);
		int n = is.nextInt();
		anims = new MaAnim[n];
		for (int i = 0; i < n; i++) {
			anims[i] = new MaAnim();
			anims[i].restore(is);
		}
		is = history.peek().mms.translate();
		n = is.nextInt();
		for (int i = 0; i < n; i++) {
			int ind = is.nextInt();
			int val = is.nextInt();
			if (ind >= 0 && ind < mamodel.n)
				mamodel.status.put(mamodel.parts[ind], val);
		}
		saved = false;
	}

	@Override
	public void revert() {
		super.revert();
		unSave("revert");
	}

	public void save() {
		if (!loaded || isSaved() || mismatch)
			return;
		saved = true;
		String pre = prev + name + "/" + name;
		save$g(pre, 0, 0);
		save$g(pre, 1, 0);

		for (int i = 0; i < 4; i++)
			save$g(pre + "0" + i, 2, i);
		if (anims.length == 7)
			for (int i = 0; i < 3; i++)
				save$g(pre + "_zombie0" + i, 2, i + 4);
	}

	public void saveIcon() {
		if (edi == null || edi.getImg() == null || prev == null)
			return;
		try {
			File f = new File(prev + name + "/edi.png");
			CommonStatic.def.check(f);
			FakeImage.write(edi.getImg(), "PNG", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveImg() {
		try {
			File f = new File(prev + name + "/" + name + ".png");
			CommonStatic.def.check(f);
			if (!FakeImage.write(num, "PNG", f))
				if (Opts.writeErr0(f.getPath()))
					if (FakeImage.write(num, "PNG", f))
						Opts.ioErr("failed to write");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveUni() {
		if (uni == null || uni.getImg() == null)
			return;
		try {
			File f = new File(prev + name + "/uni.png");
			CommonStatic.def.check(f);
			FakeImage.write(uni.getImg(), "PNG", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setNum(FakeImage fimg) {
		num = fimg;
		if (loaded)
			ICedited();
	}

	public void standardize() {
		int[] dat = mamodel.parts[0];
		int[] con = mamodel.confs[0];
		dat[6] -= con[2];
		dat[7] -= con[3];
		con[2] = con[3] = 0;

		int[] std = mamodel.ints;
		for (int[] data : mamodel.parts) {
			data[8] = data[8] * 1000 / std[0];
			data[9] = data[9] * 1000 / std[0];
			data[10] = data[10] * 3600 / std[1];
			data[11] = data[11] * 1000 / std[2];
		}
		for (MaAnim ma : anims)
			for (Part p : ma.parts) {
				if (p.ints[1] >= 8 && p.ints[1] <= 10)
					for (int[] data : p.moves)
						data[1] = data[1] * 1000 / std[0];
				if (p.ints[1] == 11)
					for (int[] data : p.moves)
						data[1] = data[1] * 3600 / std[1];
				if (p.ints[1] == 12)
					for (int[] data : p.moves)
						data[1] = data[1] * 1000 / std[2];
			}
		std[0] = 1000;
		std[1] = 3600;
		std[2] = 1000;
	}

	@Override
	public String toString() {
		return name;
	}

	public void unSave(String str) {
		saved = false;
		history(str);
		if (link != null)
			link.review();
	}

	public void updateStatus() {
		OutStream mms = OutStream.getIns();
		mms.writeInt(mamodel.status.size());
		mamodel.status.forEach((d, s) -> {
			int ind = -1;
			for (int i = 0; i < mamodel.n; i++)
				if (mamodel.parts[i] == d)
					ind = i;
			mms.writeInt(ind);
			mms.writeInt(s);
		});
		mms.terminate();
		history.peek().mms = mms;
	}

	public OutStream write() {
		OutStream osi = OutStream.getIns();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			FakeImage.write(num, "PNG", baos);
		} catch (IOException e1) {
			e1.printStackTrace();
			osi.terminate();
			return osi;
		}
		osi.writeBytesI(baos.toByteArray());
		try {
			baos = new ByteArrayOutputStream();
			imgcut.write(new PrintStream(baos, true, "UTF-8"));
			osi.writeBytesI(baos.toByteArray());
			baos = new ByteArrayOutputStream();
			mamodel.write(new PrintStream(baos, true, "UTF-8"));
			osi.writeBytesI(baos.toByteArray());
			osi.writeInt(anims.length);
			for (MaAnim ani : anims) {
				baos = new ByteArrayOutputStream();
				ani.write(new PrintStream(baos, true, "UTF-8"));
				osi.writeBytesI(baos.toByteArray());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (edi != null && edi.getImg() != null) {
			baos = new ByteArrayOutputStream();
			try {
				FakeImage.write(edi.getImg(), "PNG", baos);
			} catch (IOException e1) {
				e1.printStackTrace();
				osi.terminate();
				return osi;
			}
			osi.writeBytesI(baos.toByteArray());
		}
		if (uni != null && uni.getImg() != null) {
			baos = new ByteArrayOutputStream();
			try {
				FakeImage.write(uni.getImg(), "PNG", baos);
			} catch (IOException e1) {
				e1.printStackTrace();
				osi.terminate();
				return osi;
			}
			osi.writeBytesI(baos.toByteArray());
		}
		osi.terminate();
		return osi;
	}

	@Override
	protected void partial() {
		if (!partial) {
			partial = true;
			String pre = prev + name + "/" + name;
			mamodel = MaModel.newIns(pre + ".mamodel");
			anims = new MaAnim[7];
			for (int i = 0; i < 4; i++)
				anims[i] = MaAnim.newIns(pre + "0" + i + ".maanim");
			for (int i = 0; i < 3; i++)
				anims[i + 4] = MaAnim.newIns(pre + "_zombie0" + i + ".maanim");
			standardize();
		}
	}

	private void history(String str) {
		OutStream os = OutStream.getIns();
		imgcut.write(os);
		mamodel.write(os);
		os.writeInt(anims.length);
		for (MaAnim ma : anims)
			ma.write(os);
		os.terminate();
		History h = new History(str, os);
		history.push(h);
		updateStatus();
	}

	private void save$g(String pre, int type, int para) {
		try {
			save$s(pre, type, para);
		} catch (Exception e1) {
			e1.printStackTrace();
			try {
				save$s(pre, type, para);
			} catch (Exception e2) {
				e2.printStackTrace();
				String str = type == 0 ? ".imgcut" : type == 1 ? ".mamodel" : ".maanim";
				Opts.ioErr("cannot save " + pre + str);
			}
		}
	}

	private void save$ic(String pre) throws Exception {
		File f = new File(pre + ".imgcut");
		CommonStatic.def.check(f);
		PrintStream ps = new PrintStream(f, "UTF-8");
		imgcut.write(ps);
		ps.close();
		new ImgCut(readLine(f));
	}

	private void save$ma(String pre, int i) throws Exception {
		File f = new File(pre + ".maanim");
		CommonStatic.def.check(f);
		PrintStream ps = new PrintStream(f, "UTF-8");
		anims[i].write(ps);
		ps.close();
		new MaAnim(readLine(f));
	}

	private void save$mm(String pre) throws Exception {
		File f = new File(pre + ".mamodel");
		CommonStatic.def.check(f);
		PrintStream ps = new PrintStream(f, "UTF-8");
		mamodel.write(ps);
		ps.close();
		new MaModel(readLine(f));
	}

	private void save$s(String pre, int type, int para) throws Exception {
		if (type == 0)
			save$ic(pre);
		if (type == 1)
			save$mm(pre);
		if (type == 2)
			save$ma(pre, para);
	}

}

class History {

	protected final OutStream data;

	protected final String name;

	protected OutStream mms;

	protected History(String str, OutStream os) {
		name = str;
		data = os;
	}

}
