package common.util.pack;

import common.CommonStatic;
import common.CommonStatic.BCAuxAssets;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.util.anim.*;

public class NyCastle extends AnimI<NyCastle, NyCastle.NyType> {

	public enum NyType implements AnimI.AnimType<NyCastle, NyType> {
		BASE, ATK, EXT
	}

	public static final int TOT = 8;

	public static void read() {
		BCAuxAssets aux = CommonStatic.getBCAssets();
		String pre = "./org/castle/00";
		String mid = "/nyankoCastle_00";
		int[] type = new int[] { 0, 2, 3 };
		for (int t = 0; t < 3; t++)
			for (int i = 0; i < TOT; i++) {
				String str = pre + type[t] + mid + type[t] + "_0" + i;
				aux.main[t][i] = new VImg(str + ".png");
			}
		for (int i = 0; i < TOT; i++) {
			String str = pre + 1 + mid + 1 + "_0";
			aux.atks[i] = new NyCastle(str, i);
		}
	}

	private final int id;
	private final VImg sprite;
	private final ImgCut ic;
	private final MaModel model, atkm, extm;
	private final MaAnim manim, atka, exta;
	private FakeImage[] parts;

	private NyCastle(String str, int t) {
		anim = this;
		id = t;
		sprite = new VImg(str + t + "_00.png");
		ic = ImgCut.newIns(str + t + "_00.imgcut");
		model = MaModel.newIns(str + t + "_01.mamodel");
		manim = MaAnim.newIns(str + t + "_01.maanim");
		if (t != 1 && t != 2 && t != 7) {
			atkm = MaModel.newIns(str + t + "_00.mamodel");
			atka = MaAnim.newIns(str + t + "_00.maanim");
		} else {
			atkm = null;
			atka = null;
		}
		if (t == 6) {
			extm = MaModel.newIns(str + t + "_02.mamodel");
			exta = MaAnim.newIns(str + t + "_02.maanim");
		} else {
			extm = null;
			exta = null;
		}
	}

	@Override
	public void check() {
		if (parts == null)
			load();
	}

	@Override
	public EAnimD<NyType> getEAnim(NyType t) {
		check();
		if (t == NyType.BASE)
			return new EAnimD<>(this, model, manim, t);
		if (t == NyType.ATK)
			return new EAnimD<>(this, atkm, atka, t);
		if (t == NyType.EXT)
			return new EAnimD<>(this, extm, exta, t);
		return null;
	}

	@Override
	public void load() {
		parts = ic.cut(sprite.getImg());
	}

	@Override
	public String[] names() {
		if (atkm == null)
			return new String[] { "castle" };
		if (extm == null)
			return new String[] { "castle", "atk" };
		return new String[] { "castle", "atk", "ext" };
	}

	@Override
	public FakeImage parts(int i) {
		return parts[i];
	}

	@Override
	public String toString() {
		return "castle " + id;
	}

	@Override
	public NyType[] types() {
		return NyType.values();
	}

}
