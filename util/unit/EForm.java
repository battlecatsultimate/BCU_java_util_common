package common.util.unit;

import common.CommonStatic;
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

	public EForm(Form form, int lv) {
		f = form;
		level = new Level(form.du.getPCoin() == null ? 0 : form.du.getPCoin().info.size());
		level.setLevel(lv);

		du = form.du;
	}

	public EForm(Form form, Level level) {
		f = form;
		this.level = level;
		PCoin pc = f.du.getPCoin();
		if (pc != null)
			du = pc.improve(level.getTalents());
		else
			du = form.du;
	}

	public EUnit getEntity(StageBasis b, int[] index) {
		int lv = level.getLv() + level.getPlusLv();

		if(b.st.isAkuStage())
			level.setLevel(getAkuStageLevel());

		double d = f.unit.lv.getMult(level.getLv() + level.getPlusLv());
		EAnimU walkAnim = f.getEAnim(AnimU.UType.WALK);
		walkAnim.setTime(0);

		EUnit result = new EUnit(b, du, walkAnim, d, du.getFront(), du.getBack(), level, f.du.getPCoin(), index);

		level.setLevel(lv);

		return result;
	}

	public EUnit invokeEntity(StageBasis b, int Lvl, int minLayer, int maxLayer) {
		double d = f.unit.lv.getMult(Lvl);
		EAnimU walkAnim = f.getEAnim(AnimU.UType.WALK);
		walkAnim.setTime(0);
		return new EUnit(b, du, walkAnim, d, minLayer, maxLayer, level, f.du.getPCoin(), null);
	}

	public double getPrice(int sta) {
		return du.getPrice() * (1 + sta * 0.5);
	}

	private int getAkuStageLevel() {
		if(CommonStatic.getConfig().levelLimit == 0)
			return level.getLv() + level.getPlusLv();

		int normal = level.getLv();
		int plus = level.getPlusLv();

		normal = Math.min(normal, CommonStatic.getConfig().levelLimit);

		return CommonStatic.getConfig().plus ? normal + plus : normal;
	}
}
