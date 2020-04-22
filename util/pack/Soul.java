package common.util.pack;

import common.system.VImg;
import common.system.fake.FakeImage;
import common.util.anim.AnimD;
import common.util.anim.ImgCut;
import common.util.anim.MaAnim;
import common.util.anim.MaModel;

public class Soul extends AnimD {

	public static void read() {
		String pre = "./org/battle/soul/";
		String mid = "/battle_soul_";
		for (int i = 0; i < 13; i++)
			Pack.def.ss.add(new Soul(pre + trio(i) + mid + trio(i), i));
	}

	private final int index;
	private final VImg img;

	private Soul(String st, int i) {
		super(st);
		img = new VImg(str + ".png");
		index = i;
	}

	@Override
	public FakeImage getNum(boolean load) {
		return img.getImg();
	}

	@Override
	public void load() {
		loaded = true;
		imgcut = ImgCut.newIns(str + ".imgcut");
		mamodel = MaModel.newIns(str + ".mamodel");
		anims = new MaAnim[] { MaAnim.newIns(str + ".maanim") };
		parts = imgcut.cut(img.getImg());
	}

	@Override
	public String[] names() {
		return new String[] { "soul" };
	}

	@Override
	public String toString() {
		return "soul_" + trio(index);
	}

}
