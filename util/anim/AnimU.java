package common.util.anim;

import common.CommonStatic;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.system.files.AssetData;

public abstract class AnimU<T extends AnimU.ImageKeeper> extends AnimD {

	public static interface ImageKeeper {

		public VImg getEdi();

		public ImgCut getIC();

		public MaAnim[] getMA();

		public MaModel getMM();

		public FakeImage getNum();

		public VImg getUni();

		public void reload(AssetData data);

		public void unload();

	}

	public static String[] strs0, strs1, strs2;

	static {
		redefine();
	}

	public static void redefine() {
		CommonStatic.def.redefine(AnimU.class);
	}

	protected boolean partial = false;
	public final T loader;

	protected AnimU(String path, T load) {
		super(path);
		loader = load;
	}

	protected AnimU(T load) {
		super("");
		loader = load;
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

	public VImg getEdi() {
		return loader.getEdi();
	}

	@Override
	public FakeImage getNum() {
		return loader.getNum();
	}

	public VImg getUni() {
		return loader.getUni();
	}

	@Override
	public void load() {
		loaded = true;
		try {
			imgcut = loader.getIC();
			if (getNum() == null) {
				mamodel = null;
				return;
			}
			parts = imgcut.cut(getNum());
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
	public void unload() {
		loader.unload();
		super.unload();
	}

	protected void partial() {
		if (!partial) {
			partial = true;
			mamodel = loader.getMM();
			anims = loader.getMA();
		}
	}

}
