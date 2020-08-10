package common.util.pack;

import common.pack.PackData.Identifier;
import common.pack.PackData.Indexable;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.util.anim.AnimD;
import common.util.anim.ImgCut;
import common.util.anim.MaAnim;
import common.util.anim.MaModel;

public class Soul extends AnimD implements Indexable {

	private final Identifier id;
	private final VImg img;

	public Soul(String st, int i) {
		super(st);
		img = new VImg(str + ".png");
		id = Identifier.parseInt(i);
	}

	@Override
	public Identifier getID() {
		return id;
	}

	@Override
	public FakeImage getNum() {
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
		return "soul_" + id.id;
	}

}
