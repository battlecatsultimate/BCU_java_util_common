package common.battle.attack;

import common.battle.data.MaskAtk;
import common.battle.entity.AbEntity;
import common.battle.entity.Sniper;
import common.util.Data.Proc.MOVEWAVE;
import common.util.Data.Proc.VOLC;

import java.util.ArrayList;
import java.util.List;

public class AttackSimple extends AttackAb {

	private final boolean range;

	public AttackSimple(AtkModelAb ent, int ATK, int t, int eab, Proc pro, double p0, double p1, boolean isr,
			MaskAtk matk) {
		super(ent, ATK, t, eab, pro, p0, p1, matk);
		range = isr;
	}

	public AttackSimple(AtkModelAb ent, int ATK, int t, int eab, Proc proc, double p0, double p1, MaskAtk mask) {
		this(ent, ATK, t, eab, proc, p0, p1, mask.isRange(), mask);
		touch = mask.getTarget();
		dire *= mask.getDire();
	}

	@Override
	public void capture() {
		double pos = model.getPos();
		List<AbEntity> le = model.b.inRange(touch, dire, sta, end);
		capt.clear();
		if (canon > -2 || model instanceof Sniper)
			le.remove(model.b.ebase);
		if ((abi & AB_ONLY) == 0)
			capt.addAll(le);
		else
			for (AbEntity e : le)
				if (e.targetable(type))
					capt.add(e);
		if (!range) {
			if (capt.size() == 0)
				return;
			List<AbEntity> ents = new ArrayList<>();
			ents.add(capt.get(0));
			double dis = Math.abs(pos - ents.get(0).pos);
			for (AbEntity e : capt)
				if (Math.abs(pos - e.pos) < dis - 0.1) {
					ents.clear();
					ents.add(e);
					dis = Math.abs(pos - e.pos);
				} else if (Math.abs(pos - e.pos) < dis + 0.1)
					ents.add(e);
			capt.clear();
			int r = (int) (model.b.r.nextDouble() * ents.size());
			capt.add(ents.get(r));
		}
	}

	@Override
	public void excuse() {
		process();
		int layer = model.getLayer();
		if (proc.MOVEWAVE.exists()) {
			MOVEWAVE mw = proc.MOVEWAVE;
			int dire = model.getDire();
			double p0 = model.getPos() + dire * mw.dis;
			new ContMove(this, p0, mw.width, mw.speed, 1, mw.time, mw.itv, layer);
			return;
		}
		for (AbEntity e : capt)
			e.damaged(this);
		if (capt.size() > 0 && proc.WAVE.exists()) {
			int dire = model.getDire();
			int wid = dire == 1 ? W_E_WID : W_U_WID;
			int addp = (dire == 1 ? W_E_INI : W_U_INI) + wid / 2;
			double p0 = model.getPos() + dire * addp;
			// generate a wave when hits somebody

			new ContWaveDef(new AttackWave(this, p0, wid, WT_WAVE), p0, layer);
		}

		if(capt.size() > 0 && proc.MINIWAVE.exists()) {
			int dire = model.getDire();
			int wid = dire == 1 ? W_E_WID : W_U_WID;
			int addp = (dire == 1 ? W_E_INI : W_U_INI) + wid / 2;
			double p0 = model.getPos() + dire * addp;

			new ContWaveDef(new AttackWave(this, p0, wid, WT_MINI), p0, layer);
		}

		if (capt.size() > 0 && proc.VOLC.exists()) {
			int dire = model.getDire();
			VOLC volc = proc.VOLC;
			int addp = volc.dis_0 + (int) (model.b.r.nextDouble() * (volc.dis_1 - volc.dis_0));
			double p0 = model.getPos() + dire * addp;
			double sta = p0 + (dire == 1 ? W_VOLC_PIERCE : W_VOLC_INNER);
			double end = p0 - (dire == 1 ? W_VOLC_INNER : W_VOLC_PIERCE);

			new ContVolcano(new AttackVolcano(this, sta, end), p0, layer, volc.time);
		}
	}

}
