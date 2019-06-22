package common.util.anim;

import common.CommonStatic;
import common.util.Res;
import common.util.system.MultiLangFile;
import common.util.system.VImg;
import common.util.system.fake.FakeImage;
import common.util.system.files.AssetData;
import common.util.system.files.VFile;
import main.Printer;

public class AnimU extends AnimD implements MultiLangFile {

	public static String[] strs0, strs1, strs2;

	static {
		redefine();
	}

	public static void redefine() {
		CommonStatic.def.redefine(AnimU.class);
	}

	public FakeImage num;

	public VImg uni = Res.slot[0], edi;

	protected boolean partial = false;

	public AnimU(String st, String ed0, String ed1) {
		super(st + ed0);
		edi = new VImg(st + ed1);
		edi.mark("edi");
	}

	protected AnimU() {
		super("");
	}

	public int getAtkLen() {
		partial();
		return anims[2].len + 1;
	}

	@Override
	public EAnimU getEAnim(int t) {
		check();
		if (mamodel == null || t >= anims.length || anims[t] == null)
			return null;
		return new EAnimU(this, t);
	}

	@Override
	public FakeImage getNum() {
		check();
		return num;
	}

	@Override
	public void load() {
		loaded = true;
		try {
			num = VFile.get(str + ".png").getData().getImg(this);
			imgcut = ImgCut.newIns(str + ".imgcut");
			if (num == null) {
				Printer.e("AnimU", 70, "can't read png: " + str);
				mamodel = null;
				mismatch = true;
				return;
			}
			parts = imgcut.cut(num);
			partial();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String[] names() {
		partial();
		if (anims.length == 4)
			return strs0;
		if (anims.length == 5)
			return strs2;
		return strs1;
	}

	@Override
	public void reload(AssetData ad) {
		if (!loaded)
			return;
		num = ad.getImg(this);
		parts = imgcut.cut(num);
	}

	protected void partial() {
		if (!partial) {
			partial = true;
			mamodel = MaModel.newIns(str + ".mamodel");
			if (VFile.getFile(str + "_zombie00.maanim") != null)
				anims = new MaAnim[7];
			else if (VFile.getFile(str + "_entry.maanim") != null)
				anims = new MaAnim[5];
			else
				anims = new MaAnim[4];
			for (int i = 0; i < 4; i++)
				anims[i] = MaAnim.newIns(str + "0" + i + ".maanim");
			if (anims.length == 5)
				anims[4] = MaAnim.newIns(str + "_entry.maanim");
			if (anims.length == 7)
				for (int i = 0; i < 3; i++)
					anims[i + 4] = MaAnim.newIns(str + "_zombie0" + i + ".maanim");
		}
	}

}
