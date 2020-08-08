package common.util.anim;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import common.CommonStatic;
import common.CommonStatic.EditLink;
import common.io.InStream;
import common.io.OutStream;
import common.pack.Source;
import common.pack.Source.Identifier;
import common.pack.Source.SourceAnimSaver;
import common.pack.Source.SourceAnimLoader;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.system.files.VFile;
import main.MainBCU;
import main.Opts;

public class AnimCE extends AnimCI {

	static class AnimCELoader implements Source.AnimLoader {

		private static VImg optional(String str) {
			VFile<?> fv = VFile.getFile(str);
			if (fv == null)
				return null;
			return new VImg(fv);
		}

		private final String pre, name;

		private AnimCELoader(String str) {
			name = str;
			pre = "./res/anim/" + str + "/";
		}

		@Override
		public VImg getEdi() {
			return optional(pre + "edi.png");
		}

		@Override
		public ImgCut getIC() {
			return ImgCut.newIns(pre + name + ".imgcut");
		}

		@Override
		public MaAnim[] getMA() {
			MaAnim[] anims = new MaAnim[7];
			for (int i = 0; i < 4; i++)
				anims[i] = MaAnim.newIns(pre + name + "0" + i + ".maanim");
			for (int i = 0; i < 3; i++)
				anims[i + 4] = MaAnim.newIns(pre + name + "_zombie0" + i + ".maanim");
			return anims;
		}

		@Override
		public MaModel getMM() {
			return MaModel.newIns(pre + name + ".mamodel");
		}

		@Override
		public Identifier getName() {
			return new Identifier("_local", name);
		}

		@Override
		public FakeImage getNum() {
			return VFile.getFile(pre + name + ".png").getData().getImg();
		}

		@Override
		public int getStatus() {
			return 0;
		}

		@Override
		public VImg getUni() {
			return optional(pre + "uni.png");
		}

	}

	public static String getAvailable(String str) {
		File folder = CommonStatic.def.route("./res/anim/");
		if (!folder.exists())
			return str;
		File[] fs = CommonStatic.def.route("./res/anim/").listFiles();
		Set<String> strs = new HashSet<>();
		for (int i = 0; i < fs.length; i++)
			strs.add(fs[i].getName());
		while (strs.contains(str))
			str += "'";
		return str;
	}

	private boolean saved = false;
	public EditLink link;
	public Stack<History> history = new Stack<>();

	public AnimCE(InStream is, CommonStatic.ImgReader r, String pack) {
		super(CommonStatic.def.loadAnim(is, r));
		name = loader.getName();
		name = new Identifier(pack == null ? "_local" : pack, loader.getName().id);
	}

	public AnimCE(String st) {
		super(new AnimCELoader(st));
		name = new Identifier("_local", st);
	}

	public AnimCE(Identifier id) {
		super(new SourceAnimLoader(id));
		name = id;
	}

	public AnimCE(String str, AnimD ori) {
		this(str);
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
		loader.setNum(ori.getNum());
		parts = imgcut.cut(ori.getNum());
		if (ori instanceof AnimU<?>) {
			AnimU<?> au = (AnimU<?>) ori;
			setEdi(au.getEdi());
			setUni(au.getUni());
		}
		standardize();
		save();
		history("initial");
	}

	public void createNew() {
		loaded = true;
		partial = true;
		imgcut = new ImgCut();
		mamodel = new MaModel();
		anims = new MaAnim[7];
		for (int i = 0; i < 7; i++)
			anims[i] = new MaAnim();
		parts = imgcut.cut(getNum());
		history("initial");
	}

	public void delete() {
		new SourceAnimSaver(name, this).delete();
	}

	public String getUndo() {
		return history.peek().name;
	}

	public void hardSave(String str) {
		if (name == null)
			name = new Identifier("_local", AnimCE.getAvailable(MainBCU.validate(str)));
		saved = false;
		save();
	}

	public void ICedited() {
		check();
		parts = imgcut.cut(getNum());
	}

	public boolean inPool() {
		return name.pack != null && name.pack.equals("_local");
	}

	public boolean isSaved() {
		return saved;
	}

	@Override
	public void load() {
		try {
			super.load();
			history("initial");
		} catch (Exception e) {
			Opts.loadErr("Error in loading custom animation: " + name);
			e.printStackTrace();
			CommonStatic.def.exit(false);
		}
		validate();
	}

	public void merge(AnimCE a, int x, int y) {
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
		setNum(loader.loader.getNum());
		ICedited();
	}

	public void renameTo(String str) {
		if (getUni() != null)
			getUni().check();
		if (getEdi() != null)
			getEdi().check();
		SourceAnimSaver saver = new SourceAnimSaver(name, this);
		saver.delete();
		name.id = str;
		saver.saveAll();
		unSave("rename (not applicapable for undo)");
	}

	public void localize(String pack) {
		name.pack = null;// TODO
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
		new SourceAnimSaver(name, this).saveAll();
	}

	public void saveIcon() {
		new SourceAnimSaver(name, this).saveIconDisplay();
	}

	public void saveImg() {
		new SourceAnimSaver(name, this).saveSprite();
	}

	public void saveUni() {
		new SourceAnimSaver(name, this).saveIconDeploy();
	}

	public void setEdi(VImg uni) {
		loader.setEdi(uni);
	}

	public void setNum(FakeImage fimg) {
		loader.setNum(fimg);
		if (loaded)
			ICedited();
	}

	public void setUni(VImg uni) {
		loader.setUni(uni);
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

	@Override
	protected void partial() {
		super.partial();
		standardize();
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

	private void standardize() {
		if (mamodel.parts.length == 0 || mamodel.confs.length == 0)
			return;
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
