package common.util.anim;

import common.system.P;
import common.system.fake.FakeGraphics;

public class EAnimD extends EAnimI {
	
	public int type;

	protected MaAnim ma;

	protected int f = -1;

	public EAnimD(AnimI ia, MaModel mm, MaAnim anim) {
		super(ia, mm);
		ma = anim;
	}

	public boolean done() {
		return f > ma.max;
	}
	
	public void changeAnim(int t) {
		if (t >= ((AnimD)anim()).anims.length)
			return;
		f = -1;
		ma = ((AnimD)anim()).anims[t];
		type = t;
	}

	@Override
	public void draw(FakeGraphics g, P ori, double siz) {
		if (f == -1) {
			f = 0;
			setup();
		}
		set(g);
		g.translate(ori.x, ori.y);
		for (EPart e : order) {
			P p = P.newP(siz, siz);
			e.drawPart(g, p);
			P.delete(p);
		}
	}

	@Override
	public int ind() {
		return f;
	}

	@Override
	public int len() {
		return ma.max + 1;
	}

	@Override
	public void setTime(int value) {
		setup();
		f = value;
		ma.update(f, this, true);
	}

	public void setup() {
		ma.update(0, this, false);
	}

	@Override
	public void update(boolean rotate) {
		f++;
		ma.update(f, this, rotate);
	}

	@Override
	protected void performDeepCopy() {
		super.performDeepCopy();
		((EAnimD) copy).setTime(f);
	}

}
