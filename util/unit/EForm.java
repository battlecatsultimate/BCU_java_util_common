package common.util.unit;

import common.battle.StageBasis;
import common.battle.data.MaskUnit;
import common.battle.data.PCoin;
import common.battle.entity.EUnit;
import common.util.Data;
import common.util.anim.AnimU;
import common.util.anim.EAnimU;

public class EForm extends Data {

	private final Form f;
	private final Level level;

	public final MaskUnit du;

	public EForm(Form form, int... level) {
		f = form;
		this.level = new Level(level);
		PCoin pc = f.du.getPCoin();
		if (pc != null) {
			if (pc.full == null)
				pc.update();
			du = pc.improve(level);
		} else
			du = form.du;
	}

	public EForm(Form form, Level level) {
		f = form;
		this.level = level;
		PCoin pc = f.du.getPCoin();
		if (pc != null) {
			if (pc.full == null)
				pc.update();
			du = pc.improve(level.getLvs());
		} else
			du = form.du;
	}

	public EUnit getEntity(StageBasis b) {
		double d = f.unit.lv.getMult(level.getLvs()[0]);
		EAnimU walkAnim = f.getEAnim(AnimU.UType.WALK);
		walkAnim.setTime(0);
		return new EUnit(b, du, walkAnim, d, level, f.du.getPCoin());
	}

	public EUnit invokeEntity(StageBasis b, int Lvl) {
		double d = f.unit.lv.getMult(Lvl);
		EAnimU walkAnim = f.getEAnim(AnimU.UType.WALK);
		walkAnim.setTime(0);
		return new EUnit(b, du, walkAnim, d, level, f.du.getPCoin());
	}

	public int getPrice(int sta) {
		return (int) (du.getPrice() * (1 + sta * 0.5));
	}

}
