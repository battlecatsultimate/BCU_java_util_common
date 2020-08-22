package common.util.pack;

import common.pack.Identifier;
import common.pack.IndexContainer.IndexCont;
import common.pack.IndexContainer.Indexable;
import common.pack.PackData;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.util.anim.*;

@IndexCont(PackData.class)
public class Soul extends AnimD<Soul, Soul.SoulType> implements Indexable<PackData, Soul> {

	public enum SoulType implements AnimI.AnimType<Soul, SoulType> {
		DEF
	}

	private final Identifier<Soul> id;
	private final VImg img;

	public Soul(String st, int i) {
		super(st);
		img = new VImg(str + ".png");
		id = Identifier.parseInt(i, Soul.class);
	}

	@Override
	public Identifier<Soul> getID() {
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
		types = SoulType.values();
		parts = imgcut.cut(img.getImg());
	}

	@Override
	public String toString() {
		return "soul_" + id.id;
	}

}
