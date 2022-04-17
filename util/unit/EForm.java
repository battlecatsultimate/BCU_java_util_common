package common.util.unit;

import common.CommonStatic;
import common.battle.StageBasis;
import common.battle.data.MaskUnit;
import common.battle.data.PCoin;
import common.battle.entity.EUnit;
import common.util.Data;
import common.util.anim.AnimU;
import common.util.anim.EAnimU;

import java.util.ArrayList;

public class EForm extends Data {

	private final Form f;
	private final Level level;

	public final MaskUnit du;

	public EForm(Form form, int lv) {
		f = form;
		level = new Level();
		level.setLv(lv);

		du = form.du;
	}
	public EForm(Form form, ArrayList<Integer> level) {
		f = form;
		this.level = new Level(level);
		PCoin pc = f.du.getPCoin();
		if (pc != null)
			du = pc.improve(level);
		else
			du = form.du;
	}

	public EForm(Form form, Level level) {
		f = form;
		this.level = level;
		PCoin pc = f.du.getPCoin();
		if (pc != null)
			du = pc.improve(level.getLvs());
		else
			du = form.du;
	}

	public EUnit getEntity(StageBasis b) {
		int lv = level.getLv();

		if(b.st.isAkuStage())
			level.setLv(getAkuStageLevel());

		double d = f.unit.lv.getMult(level.getLv());
		EAnimU walkAnim = f.getEAnim(AnimU.UType.WALK);
		walkAnim.setTime(0);

		EUnit result = new EUnit(b, du, walkAnim, d, level, f.du.getPCoin());

		level.setLv(lv);

		return result;
	}

	public EUnit invokeEntity(StageBasis b, int Lvl) {
		double d = f.unit.lv.getMult(Lvl);
		EAnimU walkAnim = f.getEAnim(AnimU.UType.WALK);
		walkAnim.setTime(0);
		return new EUnit(b, du, walkAnim, d, level, f.du.getPCoin());
	}

	public double getPrice(int sta) {
		return du.getPrice() * (1 + sta * 0.5);
	}

	private int getAkuStageLevel() {
		if(CommonStatic.getConfig().levelLimit == 0)
			return level.getLv();

		int normal = level.getLv();
		int plus = 0;

		if(normal > f.unit.max) {
			plus = normal - f.unit.max;
			normal = f.unit.max;
		}

		normal = Math.min(normal, CommonStatic.getConfig().levelLimit);

		return CommonStatic.getConfig().plus ? normal + plus : normal;
	}
}
