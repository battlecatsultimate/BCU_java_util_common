package common.util.anim;

import common.io.assets.Admin.StaticPermitted;
import common.system.VImg;
import common.system.fake.FakeImage;

public abstract class AnimU<T extends AnimU.ImageKeeper> extends AnimD<AnimU<?>, AnimU.UType> {

	public interface EditableType {
		boolean rotate();
	}

	public interface ImageKeeper {

		VImg getEdi();

		ImgCut getIC();

		MaAnim[] getMA();

		MaModel getMM();

		FakeImage getNum();

		VImg getUni();

		void unload();

	}

	public enum UType implements AnimI.AnimType<AnimU<?>, UType>, EditableType {
		WALK(false), IDLE(false), ATK(true), HB(false), ENTER(true), BURROW_DOWN(true), BURROW_MOVE(false),
		BURROW_UP(true), SOUL(true);

		private final boolean rotate;

		private UType(boolean rotate) {
			this.rotate = rotate;
		}

		@Override
		public boolean rotate() {
			return rotate;
		}
	}

	@StaticPermitted
	public static final UType[] TYPE4 = { UType.WALK, UType.IDLE, UType.ATK, UType.HB };
	@StaticPermitted
	public static final UType[] TYPE5 = { UType.WALK, UType.IDLE, UType.ATK, UType.HB, UType.ENTER };
	@StaticPermitted
	public static final UType[] TYPE7 = { UType.WALK, UType.IDLE, UType.ATK, UType.HB, UType.BURROW_DOWN,
			UType.BURROW_MOVE, UType.BURROW_UP };
	@StaticPermitted
	public static final UType[] SOUL = { UType.SOUL };

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
	public EAnimU getEAnim(UType t) {
		check();
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
	public void unload() {
		loader.unload();
		super.unload();
	}

	public void partial() {
		if (!partial) {
			partial = true;
			imgcut = loader.getIC();
			mamodel = loader.getMM();
			anims = loader.getMA();
			types = anims.length == 1 ? SOUL : anims.length == 4 ? TYPE4 : anims.length == 5 ? TYPE5 : TYPE7;
		}
	}

}
