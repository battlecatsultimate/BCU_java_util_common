package common.battle;

import common.CommonStatic;
import common.battle.attack.AttackAb;
import common.battle.attack.ContAb;
import common.battle.entity.*;
import common.pack.Identifier;
import common.util.BattleObj;
import common.util.CopRand;
import common.util.Data.Proc.THEME;
import common.util.pack.Background;
import common.util.pack.EffAnim.DefEff;
import common.util.stage.EStage;
import common.util.stage.MapColc.DefMapColc;
import common.util.stage.Stage;
import common.util.unit.EForm;
import common.util.unit.EneRand;
import common.util.unit.Form;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StageBasis extends BattleObj {

	public static boolean testing = true;

	public final BasisLU b;
	public final Stage st;
	public final EStage est;
	public final ELineUp elu;
	public final int[] nyc;
	public final boolean[][] locks = new boolean[2][5];
	public final AbEntity ebase, ubase;
	public final Cannon canon;
	public final Sniper sniper;
	public final List<Entity> le = new ArrayList<>();
	public final List<EntCont> tempe = new ArrayList<>();
	public final List<ContAb> lw = new ArrayList<>();
	public final List<ContAb> tlw = new ArrayList<>();
	public final List<EAnimCont> lea = new ArrayList<>();
	public final Set<EneRand> rege = new HashSet<>();
	public final int[] conf;
	public final CopRand r;
	public final Recorder rx = new Recorder();
	public final boolean isOneLineup;
	public int changeFrame = -1;
	public int changeDivision = -1;

	public int work_lv, max_mon, can, max_can, next_lv, max_num;
	public int frontLineup = 0;
	public boolean lineupChanging = false;
	public double mon;
	public boolean shock = false;
	public int time, s_stop, temp_s_stop;
	public Background bg;

	private final List<AttackAb> la = new ArrayList<>();
	private boolean lethal = false;
	private int themeTime;
	private Identifier<Background> theme = null;
	private THEME.TYPE themeType;

	public StageBasis(EStage stage, BasisLU bas, int[] ints, long seed) {
		b = bas;
		r = new CopRand(seed);
		nyc = bas.nyc;
		est = stage;
		st = est.s;
		elu = new ELineUp(bas.lu, this);
		est.assign(this);
		bg = Identifier.getOr(st.bg, Background.class);
		EEnemy ee = est.base(this);
		if (ee != null)
			ebase = ee;
		else
			ebase = new ECastle(this);
		ebase.added(1, 800);
		ubase = new ECastle(this, bas);
		ubase.added(-1, st.len - 800);
		int sttime = 3;
		if (st.getCont().getCont() == DefMapColc.getMap("CH")) {
			if (st.getCont().id.id == 9)
				sttime = (int) Math.round(Math.log(est.mul) / Math.log(2));
			if (st.getCont().id.id < 3)
				sttime = st.getCont().id.id;
		}
		int max = est.lim != null ? est.lim.num : 50;
		max_num = max <= 0 ? 50 : max;
		max_can = bas.t().CanonTime(sttime);

		work_lv = 1 + bas.getInc(C_M_LV);
		mon = bas.getInc(C_M_INI);
		can = max_can * bas.getInc(C_C_INI) / 100;
		canon = new Cannon(this, nyc[0]);
		conf = ints;
		if ((conf[0] & 1) > 0)
			work_lv = 8;
		if ((conf[0] & 2) > 0)
			sniper = new Sniper(this);
		else
			sniper = null;
		next_lv = bas.t().getLvCost(work_lv);

		boolean oneLine = true;
		for(Form f : b.lu.fs[1]) {
			if(f != null) {
				oneLine = false;
				break;
			}
		}

		isOneLineup = oneLine;
	}

	public void changeTheme(Identifier<Background> id, int time, THEME.TYPE type) {
		theme = id;
		themeTime = time;
		themeType = type;
	}

	public int entityCount(int d) {
		int ans = 0;
		for (int i = 0; i < le.size(); i++)
			if (le.get(i).dire == d)
				ans++;
		return ans;
	}

	public int entityCount(int d, int g) {
		int ans = 0;
		for (int i = 0; i < le.size(); i++)
			if (le.get(i).dire == d && le.get(i).group == g)
				ans++;
		return ans;
	}

	/**
	 * receive attacks and excuse together, capture targets first
	 */
	public void getAttack(AttackAb a) {
		if (a == null)
			return;
		a.capture();
		la.add(a);
	}

	/**
	 * the base that entity with this direction will attack
	 */
	public AbEntity getBase(int dire) {
		return dire == 1 ? ubase : ebase;
	}

	public double getEBHP() {
		return 1.0 * ebase.health / ebase.maxH;
	}

	/**
	 * list of entities in the range of d0~d1 that can be touched by entity with
	 * this direction and touch mode
	 */
	public List<AbEntity> inRange(int touch, int dire, double d0, double d1) {
		List<AbEntity> ans = new ArrayList<>();
		if (dire == 0)
			return ans;
		for (int i = 0; i < le.size(); i++)
			if (le.get(i).dire * dire == -1 && (le.get(i).touchable() & touch) > 0
					&& (le.get(i).pos - d0) * (le.get(i).pos - d1) <= 0)
				ans.add(le.get(i));
		AbEntity b = dire == 1 ? ubase : ebase;
		if ((b.touchable() & touch) > 0 && (b.pos - d0) * (b.pos - d1) <= 0)
			ans.add(b);

		return ans;
	}

	protected boolean act_can() {
		if (can == max_can) {
			if(canon.id == BASE_WALL && entityCount(-1) >= max_num) {
				CommonStatic.setSE(SE_SPEND_FAIL);
				return false;
			}

			CommonStatic.setSE(SE_SPEND_SUC);
			canon.activate();
			can = 0;
			return true;
		}
		CommonStatic.setSE(SE_SPEND_FAIL);
		return false;
	}

	protected void act_lock(int i, int j) {
		locks[i][j] = !locks[i][j];
	}

	protected boolean act_mon() {
		if (work_lv < 8 && mon > next_lv) {
			CommonStatic.setSE(SE_SPEND_SUC);
			mon -= next_lv;
			work_lv++;
			next_lv = b.t().getLvCost(work_lv);
			max_mon = b.t().getMaxMon(work_lv);
			if (work_lv == 8)
				next_lv = -1;
			return true;
		}
		CommonStatic.setSE(SE_SPEND_FAIL);
		return false;
	}

	protected boolean act_sniper() {
		if (sniper != null) {
			sniper.enabled = !sniper.enabled;
			return true;
		}
		return false;
	}

	protected boolean act_change() {
		if(lineupChanging || isOneLineup || ubase.health == 0)
			return false;
		lineupChanging = true;
		changeFrame = 10;
		changeDivision = changeFrame / 2;
		return true;
	}

	protected boolean act_spawn(int i, int j, boolean boo) {
		if (lineupChanging)
			return false;

		if (ubase.health == 0) {
			return false;
		}
		if (elu.cool[i][j] > 0) {
			if(boo) {
				CommonStatic.setSE(SE_SPEND_FAIL);
			}

			return false;
		}
		if (elu.price[i][j] == -1) {
			return false;
		}
		if (elu.price[i][j] > mon) {
			if(boo) {
				CommonStatic.setSE(SE_SPEND_FAIL);
			}
			return false;
		}
		if (locks[i][j] || boo) {
			if (entityCount(-1) >= max_num) {
				if(boo) {
					CommonStatic.setSE(SE_SPEND_FAIL);
				}

				return false;
			}
			EForm f = b.lu.efs[i][j];
			if (f == null) {
				return false;
			}
			CommonStatic.setSE(SE_SPEND_SUC);
			elu.get(i, j);
			EUnit eu = f.getEntity(this);
			eu.added(-1, st.len - 700);
			le.add(eu);
			mon -= elu.price[i][j];
			return true;
		}
		return false;
	}

	@Override
	protected void performDeepCopy() {
		super.performDeepCopy();
		for (EneRand er : rege)
			er.updateCopy((StageBasis) hardCopy(this), hardCopy(er.map.get(this)));
	}

	/**
	 * process actions and add enemies from stage first then update each entities
	 * and receive attacks then excuse attacks and do post update then delete dead
	 * entities
	 */
	protected void update() {
		tempe.removeIf(e -> {
			if (e.t == 0)
				le.add(e.ent);
			return e.t == 0;
		});
		if (s_stop == 0) {

			int allow = st.max - entityCount(1);
			if (time % 2 == 1 && ebase.health > 0 && allow > 0) {
				EEnemy e = est.allow();
				if (e != null) {
					e.added(1, e.mark == 1 ? 801 : 700);
					le.add(e);
				}
			}
			elu.update();
			if(can == max_can-1) {
				CommonStatic.setSE(SE_CANNON_CHARGE);
			}
			can++;
			max_mon = b.t().getMaxMon(work_lv);
			mon += b.t().getMonInc(work_lv);

			est.update();
			if (shock) {
				for (int i = 0; i < le.size(); i++)
					if (le.get(i).dire == -1 && (le.get(i).touchable() & TCH_N) > 0)
						le.get(i).interrupt(INT_SW, KB_DIS[INT_SW]);
				lea.add(new EAnimCont(700, 9, effas().A_SHOCKWAVE.getEAnim(DefEff.DEF)));
				CommonStatic.setSE(SE_BOSS);
				shock = false;
			}

			ebase.update();
			canon.update();
			if (sniper != null)
				sniper.update();

			tempe.forEach(EntCont::update);
		}
		for (int i = 0; i < le.size(); i++)
			if (s_stop == 0 || (le.get(i).getAbi() & AB_TIMEI) != 0)
				le.get(i).update();

		if (s_stop == 0) {
			lw.forEach(ContAb::update);
			lea.forEach(EAnimCont::update);
			lw.addAll(tlw);
			tlw.clear();
		}
		la.forEach(AttackAb::excuse);
		la.clear();
		if (s_stop == 0) {
			ebase.postUpdate();
			if (!lethal && ebase.health <= 0 && est.hasBoss()) {
				lethal = true;
				ebase.health = 1;
			}
			if (ebase.health <= 0)
				for (int i = 0; i < le.size(); i++)
					if (le.get(i).dire == 1)
						le.get(i).kill();
			if (ubase.health <= 0)
				for (int i = 0; i < le.size(); i++)
					if (le.get(i).dire == -1)
						le.get(i).kill();
		}
		for (int i = 0; i < le.size(); i++)
			if (s_stop == 0 || (le.get(i).getAbi() & AB_TIMEI) != 0)
				le.get(i).postUpdate();

		if (s_stop == 0) {
			le.removeIf(e -> e.anim.dead == 0);
			lw.removeIf(w -> !w.activate);
			lea.removeIf(EAnimCont::done);
		}
		updateTheme();
		if (s_stop > 0)
			s_stop--;
		s_stop = Math.max(s_stop, temp_s_stop);
		temp_s_stop = 0;
		can = Math.min(max_can, Math.max(0, can));
		mon = Math.min(max_mon, Math.max(0, mon));

		if(changeFrame != -1) {
			changeFrame--;

			if(changeFrame == 0) {
				changeFrame = -1;
				changeDivision = -1;
				lineupChanging = false;
			} else if(changeFrame == changeDivision-1) {
				frontLineup = 1 - frontLineup;
			}
		}
	}

	private void updateTheme() {
		if (theme != null) {
			bg = Identifier.getOr(theme, Background.class);
			if (themeType.kill) {
				le.removeIf(e -> (e.getAbi() & AB_THEMEI) == 0);
				lw.clear();
				la.clear();
				tlw.clear();
				lea.clear();
				tempe.removeIf(e -> (e.ent.getAbi() & AB_THEMEI) == 0);
			}
			theme = null;
		}
		if (s_stop == 0 && themeTime > 0) {
			themeTime--;
			if (themeTime == 0)
				theme = st.bg;
		}
	}

}
