package common.util.anim;

import common.util.Animable;
import common.util.BattleStatic;
import common.util.system.fake.FakeImage;

public abstract class AnimI extends Animable<AnimI> implements BattleStatic {

	public AnimI() {
		anim = this;
	}

	public abstract void check();

	public abstract void load();

	public abstract String[] names();

	public abstract FakeImage parts(int img);

}
