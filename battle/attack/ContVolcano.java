package common.battle.attack;

import common.CommonStatic;
import common.CommonStatic.BattleConst;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeTransform;
import common.util.anim.EAnimD;
import common.util.pack.EffAnim.VolcEff;

public class ContVolcano extends ContAb {
	protected final EAnimD<VolcEff> anim;
	protected final AttackVolcano v;
	private final Proc defProc;

	private final int aliveTime;

	private int t = 0;
	private final int ind;
	private final boolean[] performed = new boolean[4]; // [0,1] - check if curse/seal rng has passed, [2,3] - check if unit process needs to be updated

	protected ContVolcano(AttackVolcano v, double p, int lay, int alive, int ind) {
		super(v.model.b, p, lay);

		if(v.waveType == WT_VOLC) {
			anim = (v.dire == 1 ? effas().A_E_VOLC : effas().A_VOLC).getEAnim(VolcEff.START);
		} else {
			anim = (v.dire == 1 ? effas().A_E_MINIVOLC : effas().A_MINIVOLC).getEAnim(VolcEff.START);
		}

		this.v = v;
		aliveTime = alive;
		defProc = v.getProc().clone();
		this.ind = ind;
		CommonStatic.setSE(SE_VOLC_START);

		performed[0] = performed[2] = v.attacker.status[P_CURSE][0] == 0;
		performed[1] = performed[3] = v.attacker.status[P_SEAL][0] == 0;

		update();
	}

	@Override
	public void draw(FakeGraphics gra, P p, double psiz) {
		FakeTransform at = gra.getTransform();
		anim.draw(gra, p, psiz);
		gra.setTransform(at);
		drawAxis(gra, p, psiz);
	}

	@Override
	public void update() {
		updateProc();
		if (t >= VOLC_PRE && t <= VOLC_PRE + aliveTime && anim.type != VolcEff.DURING) {
			anim.changeAnim(VolcEff.DURING, false);
			CommonStatic.setSE(SE_VOLC_LOOP);
		} else if (t > VOLC_PRE + aliveTime && anim.type != VolcEff.END)
			anim.changeAnim(VolcEff.END, false);

		if (t >= VOLC_PRE && t < VOLC_PRE + aliveTime && (t - VOLC_PRE) % VOLC_SE == 0) {
			CommonStatic.setSE(SE_VOLC_LOOP);
		}

		if (t >= aliveTime + VOLC_POST + VOLC_PRE) {
			activate = false;
		} else {
			if (t >= VOLC_PRE && t < VOLC_PRE + aliveTime)
				sb.getAttack(v);
			anim.update(false);
			t++;
		}
	}

	private void updateProc() {
		if (v.attacker.anim.dead == 0) {
			if (v.attacker.status[P_CURSE][0] > 0)
				v.attacker.status[P_CURSE][0]--;
			if (v.attacker.status[P_SEAL][0] > 0)
				v.attacker.status[P_SEAL][0]--;
		}

		String[] sealp = { "CRIT", "SNIPER", "BREAK", "SUMMON", "SATK", "SHIELDBREAK"};
		if (v.attacker.status[P_SEAL][0] > 0 && performed[3]) {
			performed[3] = false;
			for (String s : sealp)
				v.proc.get(s).clear();
		} else if (v.attacker.status[P_SEAL][0] == 0 && !performed[3]) {
			AtkModelEntity aam = (AtkModelEntity) v.model;
			for (String s : sealp)
				if (!v.proc.get(s).exists() && (defProc.get(s).exists() || (!performed[1] && aam.getProc(ind).get(s).perform(aam.b.r)))) {
					defProc.get(s).set(aam.getProc(ind).get(s));
					v.proc.get(s).set(aam.getProc(ind).get(s));
				}
			performed[1] = performed[3] = true;
		}
		String[] cursep = {"KB", "STOP", "SLOW", "WEAK", "WARP", "CURSE", "SNIPER", "SEAL", "POISON", "BOSS", "POIATK", "ARMOR", "SPEED", "DMGCUT", "DMGCAP"};
		if (v.attacker.status[P_CURSE][0] > 0 || v.attacker.status[P_SEAL][0] > 0 && performed[2]) {
			performed[2] = false;
			for (String s : cursep)
				v.proc.get(s).clear();
		} else if (v.attacker.status[P_CURSE][0] == 0 && v.attacker.status[P_SEAL][0] == 0 && !performed[2]) {
			AtkModelEntity aam = (AtkModelEntity) v.model;
			for (String s : cursep)
				if (!v.proc.get(s).exists() && (defProc.get(s).exists() || (!performed[0] && aam.getProc(ind).get(s).perform(aam.b.r)))) {
					defProc.get(s).set(aam.getProc(ind).get(s));
					v.proc.get(s).set(aam.getProc(ind).get(s));
				}
			performed[0] = performed[2] = true;
		}
	}

	protected void drawAxis(FakeGraphics gra, P p, double siz) {
		if (!CommonStatic.getConfig().ref)
			return;

		// after this is the drawing of hit boxes
		siz *= 1.25;
		double rat = BattleConst.ratio;
		int h = (int) (640 * rat * siz);
		gra.setColor(FakeGraphics.MAGENTA);
		double d0 = Math.min(v.sta, v.end);
		double ra = Math.abs(v.sta - v.end);
		int x = (int) ((d0 - pos) * rat * siz + p.x);
		int y = (int) p.y;
		int w = (int) (ra * rat * siz);

		if (v.attacked) {
			gra.fillRect(x, y, w, h);
			v.attacked = !v.attacked;
		} else {
			gra.drawRect(x, y, w, h);
		}
	}

	@Override
	public boolean IMUTime() {
		return (v.attacker.getAbi() & AB_TIMEI) != 0;
	}
}
