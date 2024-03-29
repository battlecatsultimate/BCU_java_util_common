package common.battle.entity;

import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeTransform;
import common.util.anim.EAnimD;
import common.util.anim.EAnimU;
import common.util.pack.EffAnim.WarpEff;

public class WaprCont extends EAnimCont {

	private final WarpEff type;
	private final EAnimU ent;
	private final EAnimD<?> chara;
	public final int dire;
	public final boolean timeImmune;

	public WaprCont(float p, WarpEff pa, int layer, EAnimU a, int dire, boolean timeImmune) {
		super(p, layer, effas().A_W.getEAnim(pa));
		type = pa;
		ent = a;
		chara = effas().A_W_C.getEAnim(pa);
		this.dire = dire;
		this.timeImmune = timeImmune;
	}

	@Override
	public void draw(FakeGraphics gra, P p, float psiz) {
		FakeTransform at = gra.getTransform();
		p.y -= 275 * psiz;
		super.draw(gra, p, psiz);
		gra.setTransform(at);
		p.y += 275 * psiz;
		ent.paraTo(chara);
		ent.draw(gra, p, psiz);
		ent.paraTo(null);
		gra.delete(at);
	}

	@Override
	public void update() {
		super.update();
		chara.update(false);
	}

	@Override
	public boolean done() {
		if (type == WarpEff.EXIT)
			return chara.ind() == chara.len() - 2;
		else
			return super.done();
	}
}
