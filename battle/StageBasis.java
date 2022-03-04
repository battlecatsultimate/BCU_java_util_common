package common.battle;

import common.CommonStatic;
import common.battle.attack.AttackAb;
import common.battle.attack.ContAb;
import common.battle.entity.*;
import common.pack.Identifier;
import common.util.BattleObj;
import common.util.CopRand;
import common.util.Data;
import common.util.Data.Proc.THEME;
import common.util.pack.Background;
import common.util.pack.EffAnim;
import common.util.pack.EffAnim.DefEff;
import common.util.pack.bgeffect.BackgroundEffect;
import common.util.stage.CastleImg;
import common.util.stage.EStage;
import common.util.stage.MapColc.DefMapColc;
import common.util.stage.Music;
import common.util.stage.Stage;
import common.util.unit.*;

import java.util.*;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class StageBasis extends BattleObj {

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
	public final List<EAnimCont> ebaseSmoke = new ArrayList<>();
	public final List<EAnimCont> ubaseSmoke = new ArrayList<>();
	public final Set<EneRand> rege = new HashSet<>();
	public final int[] conf;
	public final CopRand r;
	public final Recorder rx = new Recorder();
	public final boolean isOneLineup;
	public final boolean buttonDelayOn;
	public boolean goingUp = true;
	public int changeFrame = -1;
	public int changeDivision = -1;
	public int buttonDelay = 0;
	public int[] selectedUnit = {-1, -1};
	public final double boss_spawn;

	public int work_lv, money, maxMoney, cannon, maxCannon, upgradeCost, max_num;
	public int frontLineup = 0;
	public boolean lineupChanging = false;
	public boolean shock = false;
	public int time, s_stop, temp_s_stop;
	public int respawnTime, unitRespawnTime;
	public Background bg;
	public BackgroundEffect bgEffect;

	/**
	 * Real groundHeight of battle
	 */
	public double midH = -1, battleHeight = -1;
	private final List<AttackAb> la = new ArrayList<>();
	private boolean lethal = false;
	private int themeTime;
	private Identifier<Background> theme = null;
	public Identifier<Music> mus = null;
	private THEME.TYPE themeType;
	private boolean bgEffectInitialized = false;

	public StageBasis(EStage stage, BasisLU bas, int[] ints, long seed, boolean buttonDelayOn) {
		b = bas;
		r = new CopRand(seed);
		nyc = bas.nyc;
		est = stage;
		st = est.s;
		elu = new ELineUp(bas.lu, this);
		est.assign(this);
		bg = Identifier.getOr(st.bg, Background.class);
		boss_spawn = Identifier.getOr(st.castle, CastleImg.class).boss_spawn;
		if (bg.effect != -1) {
			if(bg.effect == -bg.id.id && BackgroundEffect.mixture.containsKey(bg.id.id)) {
				bgEffect = BackgroundEffect.mixture.get(bg.id.id);
			} else if(bg.effect >= 0) {
				bgEffect = CommonStatic.getBCAssets().bgEffects.get(bg.effect);
			}
		}
		EEnemy ee = est.base(this);
		if (ee != null) {
			ebase = ee;
			shock = ee.mark == -2;
			ebase.added(1, shock ? boss_spawn : 700);
		} else {
			ebase = new ECastle(this);
			ebase.added(1, 800);
		}
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
		maxCannon = bas.t().CanonTime(sttime);

		work_lv = 1 + bas.getInc(C_M_LV);
		money = bas.getInc(C_M_INI) * 100;
		cannon = maxCannon * bas.getInc(C_C_INI) / 100;
		canon = new Cannon(this, nyc[0]);
		conf = ints;

		if(st.minSpawn <= 0 || st.maxSpawn <= 0)
			respawnTime = 1;
		else if(st.minSpawn == st.maxSpawn)
			respawnTime = st.minSpawn;
		else
			respawnTime = st.minSpawn + (int) ((st.maxSpawn - st.minSpawn) * r.nextDouble());

		if ((conf[0] & 1) > 0)
			work_lv = 8;
		if ((conf[0] & 2) > 0)
			sniper = new Sniper(this);
		else
			sniper = null;
		upgradeCost = bas.t().getLvCost(work_lv);

		boolean oneLine = true;
		for(Form f : b.lu.fs[1]) {
			if(f != null) {
				oneLine = false;
				break;
			}
		}

		isOneLineup = oneLine;
		this.buttonDelayOn = buttonDelayOn;
	}

	/**
	 * returns visual money.
	 */
	public int getMoney() {
		return (int) Math.floor(money / 100.0);
	}

	/**
	 * returns visual max money
	 */
	public int getMaxMoney() {
		return maxMoney / 100;
	}

	/**
	 * returns visual next level.
	 */
	public int getUpgradeCost() {
		return upgradeCost == -1 ? -1 : upgradeCost / 100;
	}

	public void changeTheme(THEME th) {
		theme = th.id;
		mus = th.mus;
		themeTime = th.time;
		themeType = th.type;
	}

	public void changeBG(Identifier<Background> id) {
		theme = id;
	}

	public int entityCount(int d) {
		int ans = 0;
		if (ebase instanceof EEnemy && d == 1)
			ans += ((EEnemy)ebase).data.getWill() + 1;
		for (Entity ent : le) {
			if (ent.dire == d)
				ans += ent.data.getWill() + 1;
		}
		return ans;
	}

	public int entityCount(int d, int g) {
		int ans = 0;
		for (Entity ent : le)
			if (ent.dire == d && ent.group == g)
				ans += ent.data.getWill() + 1;
		return ans;
	}

	/**
	 * receive attacks and excuse together, capture targets first
	 */
	public void getAttack(AttackAb a) {
		if (a == null)
			return;
		la.add(a);
	}

	/**
	 * the base that entity with this direction will attack
	 */
	public AbEntity getBase(int dire) {
		return dire == 1 ? ubase : ebase;
	}

	public double getEBHP() {
		return 100.0 * ebase.health / ebase.maxH;
	}

	/**
	 * list of entities in the range of d0~d1 that can be touched by entity with
	 * this direction and touch mode
	 *
	 * excludeLastEdge : If range is d0 ~ d1, normally it's true if d0 <= x <= d1 where x is entity's position<br>If this is true, then it will become d0 <= x < d1
	 */
	public List<AbEntity> inRange(int touch, int dire, double d0, double d1, boolean excludeLastEdge) {
		double start = Math.min(d0, d1);
		double end = Math.max(d0, d1);
		List<AbEntity> ans = new ArrayList<>();
		if (dire == 0)
			return ans;
		for (int i = 0; i < le.size(); i++)
			if (le.get(i).dire * dire == -1 && (le.get(i).touchable() & touch) > 0 &&
				le.get(i).pos >= start && (excludeLastEdge ? le.get(i).pos < end : le.get(i).pos <= end))
				ans.add(le.get(i));
		AbEntity b = dire == 1 ? ubase : ebase;
		if ((b.touchable() & touch) > 0 && (b.pos - d0) * (b.pos - d1) <= 0)
			ans.add(b);

		return ans;
	}

	public void registerBattleDimension(double midH, double battleHeight) {
		this.midH = midH;
		this.battleHeight = battleHeight;
	}

	public void release() {
		if(bg != null && bg.effect != -1) {
			if(bg.effect < 0) {
				BackgroundEffect eff = BackgroundEffect.mixture.get(-bg.effect);

				if(eff != null)
					eff.release();
			} else {
				CommonStatic.getBCAssets().bgEffects.get(bg.effect).release();
			}
		}
	}

	protected boolean act_can() {
		if(buttonDelay > 0)
			return false;

		if(ubase.health <= 0 || ebase.health <= 0)
			return false;

		if (cannon == maxCannon) {
			if(canon.id == BASE_WALL && entityCount(-1) >= max_num) {
				CommonStatic.setSE(SE_SPEND_FAIL);
				return false;
			}

			CommonStatic.setSE(SE_SPEND_SUC);
			canon.activate();
			cannon = 0;
			return true;
		}
		CommonStatic.setSE(SE_SPEND_FAIL);
		return false;
	}

	protected void act_lock(int i, int j) {
		locks[i][j] = !locks[i][j];
	}

	protected boolean act_mon() {
		if(buttonDelay > 0)
			return false;

		if (work_lv < 8 && money > upgradeCost) {
			CommonStatic.setSE(SE_SPEND_SUC);
			money -= upgradeCost;
			work_lv++;
			upgradeCost = b.t().getLvCost(work_lv);
			maxMoney = b.t().getMaxMon(work_lv);
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

	protected boolean act_continue() {
		if (!st.non_con && ubase.health <= 0) {
			ubase.health = ubase.maxH;
			if (getEBHP() <= st.mush)
				CommonStatic.setBGM(st.mus1);
			else
				CommonStatic.setBGM(st.mus0);
			money = Integer.MAX_VALUE;
			while (work_lv < 8)
				act_mon();
			money = maxMoney;
			cannon = maxCannon;
			for (Entity e : le)
				if (e.dire == 1) {
					e.pos = ebase.pos;
					e.cont();
				}
			for(int[] c : elu.cool)
				Arrays.fill(c, 0);
			return true;
		}
		return false;
	}

	protected boolean act_change_up() {
		if(lineupChanging || isOneLineup || ubase.health == 0)
			return false;
		lineupChanging = true;
		goingUp = true;
		changeFrame = Data.LINEUP_CHANGE_TIME;
		changeDivision = changeFrame / 2;
		return true;
	}

	protected boolean act_change_down() {
		if(lineupChanging || isOneLineup || ubase.health == 0)
			return false;
		lineupChanging = true;
		goingUp = false;
		changeFrame = Data.LINEUP_CHANGE_TIME;
		changeDivision = changeFrame / 2;
		return true;
	}

	protected boolean act_spawn(int i, int j, boolean boo) {
		if (buttonDelay > 0)
			return false;

		if (ubase.health == 0) {
			return false;
		}

		if(buttonDelayOn && boo && selectedUnit[0] == -1) {
			if(elu.price[i][j] != -1 || b.lu.fs[i][j] == null) {
				if (lineupChanging)
					return false;

				buttonDelay = 6;
				selectedUnit[0] = i;
				selectedUnit[1] = j;

				return true;
			}
		}

		if (unitRespawnTime > 0)
			return false;

		if (elu.cool[i][j] > 0) {
			if(boo) {
				CommonStatic.setSE(SE_SPEND_FAIL);
			}

			return false;
		}
		if (elu.price[i][j] == -1) {
			return false;
		}
		if (elu.price[i][j] > money) {
			if(boo) {
				CommonStatic.setSE(SE_SPEND_FAIL);
			}
			return false;
		}
		if (locks[i][j] || boo) {
			if (entityCount(-1) >= max_num - b.lu.efs[i][j].du.getWill()) {
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
			le.sort(Comparator.comparingInt(e -> e.layer));
			money -= elu.price[i][j];
			unitRespawnTime = 1;
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
		boolean active = ebase.health > 0 && ubase.health > 0;
		if(midH != -1 && bgEffect != null && !bgEffectInitialized) {
			bgEffect.initialize(st.len, battleHeight, midH, bg);
			bgEffectInitialized = true;
		}

		tempe.removeIf(e -> {
			if (e.t == 0) {
				le.add(e.ent);
				le.sort(Comparator.comparingInt(en -> en.layer));
			}
			return e.t == 0;
		});

		if (s_stop == 0 || (ebase.getAbi() & AB_TIMEI) != 0) {
			ebase.preUpdate();
			ebase.update();
		}

		if (s_stop == 0) {
			if(bgEffect != null)
				bgEffect.update(st.len, battleHeight, midH);

			ubase.preUpdate();
			ubase.update();

			int allow = st.max - entityCount(1);
			if (respawnTime <= 0 && active && allow > 0) {
				EEnemy e = est.allow();
				if (e != null) {
					e.added(1, e.mark == 1 ? boss_spawn : 700.0);
					le.add(e);
					le.sort(Comparator.comparingInt(en -> en.layer));

					if(st.minSpawn <= 0 || st.maxSpawn <= 0)
						respawnTime = 1;
					else if(st.minSpawn == st.maxSpawn)
						respawnTime = st.minSpawn;
					else {
						respawnTime = st.minSpawn + (int) ((st.maxSpawn - st.minSpawn) * r.nextDouble());
					}
				}
			}

			if(unitRespawnTime > 0 && active)
				unitRespawnTime--;

			if(respawnTime > 0 && active)
				respawnTime--;

			elu.update();
			if(cannon == maxCannon -1) {
				CommonStatic.setSE(SE_CANNON_CHARGE);
			}
			if (active) {
				cannon++;
				maxMoney = b.t().getMaxMon(work_lv);
				money += b.t().getMonInc(work_lv);
			}

			if (active)
				est.update();

			canon.update();
			if (sniper != null && active)
				sniper.update();

			tempe.forEach(EntCont::update);
		}

		for (int i = 0; i < le.size(); i++)
			if (s_stop == 0 || (le.get(i).getAbi() & AB_TIMEI) != 0)
				le.get(i).preUpdate();

		for (int i = 0; i < le.size(); i++)
			if (s_stop == 0 || (le.get(i).getAbi() & AB_TIMEI) != 0)
				le.get(i).update();

		if (s_stop == 0) {
			tlw.forEach(ContAb::update);
			lw.forEach(ContAb::update);
			lea.forEach(EAnimCont::update);
			ebaseSmoke.forEach(EAnimCont::update);
			ubaseSmoke.forEach(EAnimCont::update);
			lw.addAll(tlw);
			lw.sort(Comparator.comparingInt(e -> e.layer));
			tlw.clear();
		}
		la.forEach(AttackAb::capture);
		la.forEach(AttackAb::excuse);
		la.removeIf(a -> a.duration <= 0);

		if(s_stop == 0 || (ebase.getAbi() & AB_TIMEI) != 0) {
			ebase.postUpdate();

			if (!lethal && ebase instanceof ECastle && ebase.health <= 0 && est.hasBoss()) {
				lethal = true;
				ebase.health = 1;
			}
		}

		if (s_stop == 0) {
			if (ebase.health <= 0) {
				for (Entity entity : le)
					if (entity.dire == 1)
						entity.kill(false);

				if(ebaseSmoke.size() <= 7 && time % 2 == 0) {
					int x = (int) (ebase.pos + 50 - 500 * r.irDouble());
					int y = (int) (-288 * r.irDouble());

					ebaseSmoke.add(new EAnimCont(x, 0, EffAnim.effas().A_ATK_SMOKE.getEAnim(DefEff.DEF), y));
				}
			}

			if (ubase.health <= 0) {
				for (int i = 0; i < le.size(); i++)
					if (le.get(i).dire == -1)
						le.get(i).kill(false);

				if(ubaseSmoke.size() <= 7 && time % 2 == 0) {
					int x = (int) (ubase.pos - 50 + 500 * r.irDouble());
					int y = (int) (-288 * r.irDouble());

					ubaseSmoke.add(new EAnimCont(x, 0, EffAnim.effas().A_ATK_SMOKE.getEAnim(DefEff.DEF), y));
				}
			}
		}

		for (int i = 0; i < le.size(); i++)
			if (s_stop == 0 || (le.get(i).getAbi() & AB_TIMEI) != 0)
				le.get(i).postUpdate();

		if (shock) {
			for (Entity entity : le) {
				if (entity.dire == -1 && (entity.touchable() & TCH_N) > 0) {
					entity.interrupt(INT_SW, KB_DIS[INT_SW]);
					entity.postUpdate();
				}
			}
			lea.add(new EAnimCont(700, 9, effas().A_SHOCKWAVE.getEAnim(DefEff.DEF)));
			lea.sort(Comparator.comparingInt(e -> e.layer));
			CommonStatic.setSE(SE_BOSS);
			shock = false;
		}

		if (s_stop == 0) {
			le.removeIf(e -> e.anim.dead == 0);
			lw.removeIf(w -> !w.activate);
			lea.removeIf(EAnimCont::done);
			ebaseSmoke.removeIf(EAnimCont::done);
			ubaseSmoke.removeIf(EAnimCont::done);
		}
		updateTheme();
		if (s_stop > 0)
			s_stop--;
		s_stop = Math.max(s_stop, temp_s_stop);
		temp_s_stop = 0;
		cannon = Math.min(maxCannon, Math.max(0, cannon));
		money = Math.min(maxMoney, Math.max(0, money));

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

		if(buttonDelay > 0) {
			buttonDelay--;

			if(buttonDelay == 0) {
				act_spawn(selectedUnit[0], selectedUnit[1], true);

				selectedUnit[0] = -1;
				selectedUnit[1] = -1;
			}
		}
	}

	private void updateTheme() {
		if (theme != null) {
			bg = Identifier.getOr(theme, Background.class);
			if (themeType != null && themeType.kill) {
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
			if (themeTime == 0) {
				if (getEBHP() < st.bgh)
					theme = st.bg1;
				else
					theme = st.bg;

				mus = null;
			}
		}
	}

}
