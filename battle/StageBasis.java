package common.battle;

import common.CommonStatic;
import common.battle.attack.AttackAb;
import common.battle.attack.ContAb;
import common.battle.data.MaskUnit;
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
import common.util.stage.*;
import common.util.stage.MapColc.DefMapColc;
import common.util.unit.EForm;
import common.util.unit.EneRand;
import common.util.unit.Form;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class StageBasis extends BattleObj {

	public static final int[] DELAY_BASE = new int[] { 0, 0, 0, 0 };

	public final BasisLU b;
	public final Stage st;
	public final EStage est;
	public final ELineUp elu;
	public final long[][] totalDamageTaken = new long[2][5];
	public final long[][] totalDamageGiven = new long[2][5];
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
	public final float boss_spawn;
	public final int[] shakeCoolDown = {0, 0};
	public int activeGuard = -1;
	public int maxCatSpawns = -1;

	public float siz;
	public int work_lv, money, maxMoney, cannon, maxCannon, upgradeCost, maxNum, pos, score;
	public int[] maxRarityNum = { -1, -1, -1, -1, -1, -1 };
	public int frontLineup = 0;
	public boolean lineupChanging = false;
	public boolean shock = false;
	public int time, s_stop, temp_s_stop, inten, temp_inten;
	public int sn_stop, sn_temp_stop;
	public float n_inten, temp_n_inten;
	public int[] shake;
	public int shakeDuration;
	public float shakeOffset;

	public int respawnTime, unitRespawnTime;
	public Background bg;
	public BackgroundEffect bgEffect;

	public boolean leaSort = false;

	/**
	 * Real groundHeight of battle
	 */
	public float midH = -1, battleHeight = -1;
	private final List<AttackAb> la = new ArrayList<>();
	private boolean lethal = false;
	public int themeTime;
	private Identifier<Background> theme = null;
	public Identifier<Music> mus = null;
	private THEME.TYPE themeType;
	private boolean bgEffectInitialized = false;

	public final int[][] spiritCooldown = new int[2][5];
	public int[][] frameOffCd = new int[2][5];
	public final int[][][] cdDelay = new int[2][5][3];
	public final int[][][] cdDelayVisual = new int[2][5][DELAY_BASE.length];
	public final int[][] lineDelay;

	/**
	 * Flag for whether summoner has been summoned or not
	 */
	public final boolean[][] summonerSummoned = new boolean[2][5];
	/**
	 * Flag for whether spirit has been summoned or not
	 */
	public final boolean[][] spiritSummoned = new boolean[2][5];

	public final int[][] spiritEmphasizeCount = new int[2][5];
	public final int[][] spiritEmphasizeStartTime = new int[2][5];
	public final int[][][] deployDupe = new int[2][5][2]; // [count, delay]

	public StageBasis(BattleField bf, EStage stage, BasisLU bas, int[] ints, long seed, boolean buttonDelayOn) {
		b = bas;
		r = new CopRand(seed);
		nyc = bas.nyc;
		est = stage;
		st = est.s;
		lineDelay = new int[st.data.datas.length][3];
		elu = new ELineUp(bas.lu, this);
		est.assign(this);
		boss_spawn = Identifier.getOr(st.castle, CastleImg.class).boss_spawn;
		setBackground(st.bg);
		EEnemy ee = est.base(this);
		if (ee != null) {
			ebase = ee;
			shock = ee.mark == -2;
			ebase.added(1, shock ? boss_spawn : 700);
		} else {
			ebase = new ECastle(this);
			ebase.added(1, 800);

			// If enemy base is castle, no need to perform delayed first spawn
			Arrays.fill(est.first, -1);
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
		int max;
		if (est.lim != null) {
			max = est.lim.num;
			if (est.lim.stageLimit != null) {
				maxCatSpawns = est.lim.stageLimit.maxUnitSpawn;
				maxRarityNum = est.lim.stageLimit.rarityDeployLimit;
			}
		} else {
			max = 50;
		}
		maxNum = max <= 0 ? 50 : max;
		maxCannon = bas.t().CanonTime(sttime, StageLimit.isComboBanned(est.lim, C_C_SPE));

		int bank = maxBankLimit();
		if (bank > 0) {
			work_lv = 8;
			money = maxBankLimit() * 100;
		} else {
			work_lv = 1;
			if (!StageLimit.isComboBanned(est.lim, C_M_LV))
				work_lv += bas.getInc(C_M_LV);
			if (!StageLimit.isComboBanned(est.lim, C_M_INI))
				money = bas.getInc(C_M_INI) * 100;
		}
		if (est.lim != null && est.lim.stageLimit != null && est.lim.stageLimit.coolStart) {
			for (int i = 0; i < 2; i++)
				for (int j = 0; j < 5; j++)
					elu.get(i, j);
		}

		cannon = maxCannon * (StageLimit.isComboBanned(est.lim, C_C_INI) ? 0 : bas.getInc(C_C_INI)) / 100;
		canon = new Cannon(this, nyc[0], nyc[1], nyc[2]);
		conf = ints;

		if(st.minSpawn <= 0 || st.maxSpawn <= 0)
			respawnTime = 1;
		else if(st.minSpawn == st.maxSpawn)
			respawnTime = st.minSpawn;
		else
			respawnTime = st.minSpawn + (int) ((st.maxSpawn - st.minSpawn) * r.nextDouble());

		respawnTime--;

		if ((conf[0] & 1) > 0)
			work_lv = 8;
		if ((conf[0] & 2) > 0)
			sniper = new Sniper(this, bf);
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

		if (est.s.bossGuard)
			activeGuard = 0;

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 5; j++) {
				cdDelayVisual[i][j] = DELAY_BASE.clone();
			}
		}
	}

	/**
	 * returns visual money.
	 */
	public int getMoney() {
		return money / 100;
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

	public List<Entity> findEntitiesOf(int i, int j) {
		List<Entity> ans = new ArrayList<>();
		for (Entity ent : le) {
			if (ent.dire == -1 && b.lu.efs[i][j] != null && ent.data == b.lu.efs[i][j].du)
				ans.add(ent);
		}
		return ans;
	}

	public int entityCount(int d) {
		int ans = 0;
		if (ebase instanceof EEnemy && d == 1)
			ans += ((EEnemy)ebase).data.getWill() + 1;
		for (Entity ent : le) {
			if (ent.dire == d && !ent.dead)
				ans += ent.data.getWill() + 1;
		}
		return ans;
	}

	public int entityCountRar(int r) {
		int ans = 0;
		for (Entity ent : le) {
			if (ent.dire == -1 && !ent.dead && ((MaskUnit) ent.data).getPack().unit.rarity == r)
				ans += ent.data.getWill() + 1;
		}
		return ans;
	}

	public int entityCount(int d, int g) {
		int ans = 0;
		for (Entity ent : le)
			if (ent.dire == d && ent.group == g && !ent.dead)
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

	public float getEBHP() {
		return Math.min(100f, 100f * ebase.health / ebase.maxH);
	}

	/**
	 * list of entities in the range d0 ~ d1 that can be touched by entity with given direction and touch mode
	 * entity is picked if d0 <= pos <= d1 when excludeRightEdge is false
	 *                  if d0 <= pos <  d1 when excludeRightEdge is true (used by breakerblast and blast ability), TODO: waves should use it)
	 */
	public List<AbEntity> inRange(int touch, int dire, float d0, float d1, boolean excludeRightEdge) {

		List<AbEntity> ans = new ArrayList<>();

		if (dire == 0)
			return ans;

		float left = Math.min(d0, d1);
		float right = Math.max(d0, d1);

		if (excludeRightEdge)
			right -= 1;

		for (int i = 0; i < le.size(); i++)
			if (le.get(i).dire == dire && (le.get(i).touchable() & touch) != 0 && le.get(i).pos >= left && le.get(i).pos <= right)
				ans.add(le.get(i));

		AbEntity b = dire == 1 ? ebase : ubase;

		if ((b.touchable() & touch) != 0 && b.pos >= left && b.pos <= right)
			ans.add(b);

		return ans;
	}

	public List<AbEntity> inRange(int touch, int dire, float d0, float d1, boolean excludeRightEdge, float blindSpot) {
		List<AbEntity> ans = new ArrayList<>();

		if (dire == 0)
			return ans;

		float farLeft = Math.min(d0, d1); // would be furthest left (1st point) -175
		float farRight = Math.max(d0, d1); // would be furthest right (4th point) 175

		float innerLeft = (farLeft + farRight) / 2 - (blindSpot / 2); // would be second to furthest left (3rd point)
		float innerRight = (farLeft + farRight) / 2 + (blindSpot / 2); // would be second to furthest right (2nd point)

		if (excludeRightEdge) {
			innerRight -= 1;
			farRight -= 1;
		}

		for (int i = 0; i < le.size(); i++)
			if (le.get(i).dire == dire && (le.get(i).touchable() & touch) != 0
					&& (le.get(i).pos >= farLeft && le.get(i).pos <= innerLeft || le.get(i).pos >= innerRight && le.get(i).pos <= farRight))
				ans.add(le.get(i));

		AbEntity b = dire == 1 ? ebase : ubase;

		if ((b.touchable() & touch) != 0
				&& (b.pos >= farLeft && b.pos <= innerLeft || b.pos >= innerRight && b.pos <= farRight))
			ans.add(b);

		return ans;

	}

	public void registerBattleDimension(float midH, float battleHeight) {
		this.midH = midH;
		this.battleHeight = battleHeight;
	}

	public void notifyUnitDeath() {
		float percentage = ebase.health * 100f / ebase.maxH;

		for(int i = 0; i < est.killCounter.length; i++) {
			SCDef.Line line = est.s.data.datas[i];

			if(est.killCounter[i] == 0 || line.castle_0 == 0)
				continue;

			if(line.castle_0 == line.castle_1 && percentage <= line.castle_0) {
				est.killCounter[i] -= 1;
			} else if(line.castle_0 != line.castle_1 && percentage >= Math.min(line.castle_0, line.castle_1) && percentage <= Math.max(line.castle_0, line.castle_1)) {
				est.killCounter[i] -= 1;
			}
		}
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
			if(canon.id == BASE_WALL && entityCount(-1) >= maxNum) {
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
			maxMoney = b.t().getMaxMon(work_lv, StageLimit.isComboBanned(est.lim, C_M_MAX));
			return true;
		}
		CommonStatic.setSE(SE_SPEND_FAIL);
		return false;
	}

	protected boolean act_sniper() {
		if (sniper != null) {
			sniper.enabled = !sniper.enabled;
			sniper.cancel();
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
					e.lastPosition = ebase.pos;
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

	protected boolean act_spawn(int i, int j, boolean manual) {
		if (buttonDelay > 0 || ubase.health == 0)
			return false;

		if(buttonDelayOn && manual && selectedUnit[0] == -1) {
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

		EForm f = b.lu.efs[i][j];
		if (f == null)
			return false;

		List<Entity> summoners = findEntitiesOf(i, j).stream().filter(e -> e.anim.dead < 0).collect(Collectors.toList());
		if (manual && f.du.getProc().SPIRIT.exists() && summonerSummoned[i][j] && !summoners.isEmpty() && !spiritSummoned[i][j]) {
			if (spiritCooldown[i][j] > 0) {
				CommonStatic.setSE(SE_SPEND_FAIL);
				return false;
			}

			f = b.lu.spirits[i][j];
			if (f == null)
				return false;

			if (entityCount(-1) >= maxNum - f.du.getWill() * summoners.size()) {
				CommonStatic.setSE(SE_SPEND_FAIL);
				return false;
			}

			CommonStatic.setSE(SE_SPIRIT_SUMMON);

			for (Entity summoner : summoners) {
				EUnit su = f.getEntity(this, null, true, false);
				su.added(-1, Math.max(ebase.pos + su.data.getRange(), Math.min(summoner.pos + SPIRIT_SUMMON_RANGE, ubase.pos)));
				le.add(su);
			}

			le.sort(Comparator.comparingInt(e -> e.currentLayer));

			spiritSummoned[i][j] = true;
			unitRespawnTime = 1;

			return true;
		} else if (locks[i][j] || manual) {
			int rar = b.lu.fs[i][j].unit.rarity;
			if (entityCount(-1) >= maxNum - f.du.getWill()) {
				if (manual)
					CommonStatic.setSE(SE_SPEND_FAIL);

				return false;
			}
			if (maxRarityNum[rar] > -1 && entityCountRar(rar) >= maxRarityNum[rar] - b.lu.fs[i][j].du.getWill()) {
				if (manual)
					CommonStatic.setSE(SE_SPEND_FAIL);

				return false;
			}
			if (maxCatSpawns == 0) {
				if (manual)
					CommonStatic.setSE(SE_SPEND_FAIL);

				return false;
			}
			if (elu.cool[i][j] > 0) {
				if (manual) {
					CommonStatic.setSE(SE_SPEND_FAIL);
				}

				return false;
			}

			int price = elu.price[i][j];
			if (elu.price[i][j] == -1)
				return false;
			if (elu.priceDownOrb[i][j] > 0 && elu.tick[i][j] == 1)
				price -= price * elu.priceDownOrb[i][j] / 100;

			if (price > money) {
				if (manual)
					CommonStatic.setSE(SE_SPEND_FAIL);

				return false;
			}
			if (f.du.getProc().SPIRIT.exists() && summonerSummoned[i][j] && !findEntitiesOf(i, j).isEmpty()) {
				if (manual)
					CommonStatic.setSE(SE_SPEND_FAIL);

				return false;
			}

			CommonStatic.setSE(SE_SPEND_SUC);
			elu.get(i, j);
			EUnit eu = f.getEntity(this, new int[] {i, j}, false, elu.tick[i][j] == 1);
			eu.added(-1, st.len - 700);

			if (f.du.getProc().SPIRIT.exists()) {
				summonerSummoned[i][j] = true;
				spiritCooldown[i][j] = SPIRIT_SUMMON_DELAY;
			}
			if (getDupeCount(rar) > 0) {
				deployDupe[i][j][0] += getDupeCount(rar);
				if (deployDupe[i][j][1] == 0)
					deployDupe[i][j][1] = getDupeDelay(rar);
			}

			le.add(eu);
			le.sort(Comparator.comparingInt(e -> e.currentLayer));

			money -= price;
			unitRespawnTime = 1;
			if (maxCatSpawns > 0)
				maxCatSpawns--;

			if (elu.tick[i][j] != -1)
				elu.tick[i][j] = (elu.tick[i][j] + 1) % 2;

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

//	protected void processSingleProcs() {
//		Map<EEnemy, List<EUnit>> check = new HashMap<>();
//		int[][][] delay = new int[2][5][3];
//		for (Entity e : le) {
//			if (!(e instanceof EUnit))
//				continue;
//			EUnit eu = (EUnit) e;
//			for (AttackAb atk : eu.lastHitBy) {
//				if (atk.attacker instanceof EEnemy) {
//					EEnemy ee = (EEnemy) atk.attacker;
//					if (!check.containsKey(ee))
//						check.put(ee, new ArrayList<>());
//					else if (check.get(ee).contains(eu))
//						continue;
//					if (eu.index == null)
//						continue;
//					if (atk.getProc().DELAY.exists()) {
//						Proc.DELAY d = atk.getProc().DELAY;
//						Proc.IMUAD imu = eu.getProc().IMUDELAY;
//						float res = eu.getResistValue(atk, "IMUDELAY", imu.mult);
//						if (res > 0) {
//							int strength = (int) (d.strength * res);
//
//							if (strength > 0)
//								delay[eu.index[0]][eu.index[1]][d.type] = strength;
//						} else {
//							eu.anim.getEff(INV);
//						}
//					}
//					check.get(ee).add(eu);
//				}
//			}
//		}
//
//		for (int i = 0; i < 2; i++)
//			for (int j = 0; j < 5; j++)
//				if (Arrays.stream(delay[i][j]).anyMatch(s -> s != 0))
//					elu.delay(i, j, delay[i][j]);
//	}

	public boolean isActive() {
		return ebase.health > 0 && ubase.health > 0 && !isDojoOvertime();
	}

	/**
	 * process actions and add enemies from stage first then update each entity
	 * and receive attacks then excuse attacks and do post update then delete dead
	 * entities
	 */
	protected void update() {
		boolean active = isActive();
		if (midH != -1 && bgEffect != null && !bgEffectInitialized) {
			bgEffect.initialize(st.len, battleHeight, midH, bg);
			bgEffectInitialized = true;
		}

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 5; j++) {
				if (deployDupe[i][j][0] > 0)
					while (deployDupe[i][j][0] > 0 && deployDupe[i][j][1] == 0) {
						deployDupe[i][j][0]--;
						EForm f = b.lu.efs[i][j];
						EUnit eu = b.lu.efs[i][j].getEntity(this, new int[] {i, j}, false, false);
						eu.added(-1, st.len - 700);
						le.add(eu);
						deployDupe[i][j][1] = getDupeDelay(f.du.getPack().unit.rarity);
					}
				if (deployDupe[i][j][1] > 0)
					deployDupe[i][j][1]--;
			}
		}

		le.sort(Comparator.comparingInt(e -> e.currentLayer));

		// i would prefer "dev only" code to be on its own separate branch so it's not clogging main branch, im too lazy to do that, sorry  -- red

		if (buttonDelay > 0 && --buttonDelay == 0) {
			act_spawn(selectedUnit[0], selectedUnit[1], true);
			selectedUnit[0] = -1;
			selectedUnit[1] = -1;
		}

		tempe.removeIf(e -> {
			if (e.t == 0) {
				le.add(e.ent);
				le.sort(Comparator.comparingInt(en -> en.currentLayer));
			}
			return e.t == 0;
		});

		if (temp_inten > 0) {
			inten++;
			if (inten % temp_inten == 0) {
				temp_s_stop = s_stop - 1;
				s_stop = 0;
				inten = 0;
			}
		}

		if (s_stop == 0) {
			if (isDojoOvertime() && est.lim != null && score < est.lim.score)
				ubase.health = 0;

			if(bgEffect != null)
				bgEffect.update(st.len, battleHeight, midH);

			if (activeGuard == 0 && est.hasBoss(true, false))
				activeGuard = 1;

			int allow = st.max - entityCount(1);
			if (respawnTime <= 0 && active && allow > 0) {
				EEnemy e = est.allow();

				if (e != null) {
					e.added(1, e.mark >= 1 ? boss_spawn : 700f);

					le.add(e);
					le.sort(Comparator.comparingInt(en -> en.currentLayer));

					if(st.minSpawn <= 0 || st.maxSpawn <= 0)
						respawnTime = 1;
					else if(st.minSpawn == st.maxSpawn)
						respawnTime = st.minSpawn;
					else {
						respawnTime = st.minSpawn + (int) ((st.maxSpawn - st.minSpawn) * r.nextFloat());
					}
				}
			}

			if(unitRespawnTime > 0 && active)
				unitRespawnTime--;

			if(respawnTime > 0 && active)
				respawnTime--;

			elu.update();

			for (int i = 0; i < spiritCooldown.length; i++) {
				for (int j = 0; j < spiritCooldown[i].length; j++) {
					if (spiritEmphasizeCount[i][j] > 0 && (time - spiritEmphasizeStartTime[i][j]) % 4 == 0) {
						spiritEmphasizeCount[i][j]--;
					}

					if (spiritCooldown[i][j] > 0) {
						spiritCooldown[i][j]--;

						if (spiritCooldown[i][j] == 0) {
							spiritEmphasizeStartTime[i][j] = time;
							spiritEmphasizeCount[i][j] = 10;
						}
					}
				}
			}

			if(cannon == maxCannon -1) {
				CommonStatic.setSE(SE_CANNON_CHARGE);
			}
			if (active) {
				cannon++;
				int bank = maxBankLimit();
				if (bank > 0) {
					maxMoney = bank * 100;
				} else {
					maxMoney = b.t().getMaxMon(work_lv, StageLimit.isComboBanned(est.lim, C_M_MAX));
					int mon = b.t().getMonInc(work_lv);
					if (!StageLimit.isComboBanned(est.lim, C_M_INC))
						mon *= (b.getInc(C_M_INC) / 100 + 1);
					money += mon;
				}
			}

			if (active)
				est.update();

			// Cannon should be updated after entities
			// canon.update();

			if (sniper != null && active)
				sniper.update();

			tempe.forEach(EntCont::update);

			if(shakeDuration <= 0) {
				shake = null;
				shakeOffset = 0;
			}

			if(shake != null) {
				shakeOffset = getOffset();
				shakeDuration--;
			}

			for(int i = 0; i < shakeCoolDown.length; i++)
				if(shakeCoolDown[i] != 0)
					shakeCoolDown[i] -= 1;
		}

		if (temp_n_inten > 0)
			n_inten += temp_n_inten;

		updateEntities(s_stop == 0);

		while (n_inten >= 1) {
			updateEntities(false);

			n_inten--;
		}

		canon.update();

		if (s_stop == 0) {
			lea.forEach(EAnimCont::update);
			ebaseSmoke.forEach(EAnimCont::update);
			ubaseSmoke.forEach(EAnimCont::update);
			lw.addAll(tlw);
			lw.sort(Comparator.comparingInt(e -> e.layer));
			tlw.clear();
		} else {
			for (int i = 0; i < lea.size(); i++) {
				EAnimCont content = lea.get(i);

				if (content instanceof WaprCont && ((WaprCont) content).timeImmune) {
					content.update();
				}
			}
		}

		la.forEach(AttackAb::capture);
		la.forEach(AttackAb::excuse);
		la.removeIf(a -> a.duration <= 0);

//		processSingleProcs();

		if(s_stop == 0 || (ebase.getAbi() & AB_TIMEI) != 0) {
			ebase.postUpdate();

			if (!lethal && ebase instanceof ECastle && ebase.health <= 0 && est.hasBoss(false, true)) {
				lethal = true;
				ebase.health = 1;
			}
		}

		if (s_stop == 0) {
			if (ebase.health <= 0) {
				for (Entity entity : le)
					if (entity.dire == 1)
						entity.kill(Entity.KillMode.NORMAL);

				if(ebaseSmoke.size() <= 7 && time % 2 == 0) {
					int x = (int) (ebase.pos + 50 - 500 * r.irDouble());
					int y = (int) (-288 * r.irDouble());

					ebaseSmoke.add(new EAnimCont(x, 0, EffAnim.effas().A_ATK_SMOKE.getEAnim(DefEff.DEF), y));
				}
			}

			if (ubase.health <= 0) {
				for (int i = 0; i < le.size(); i++)
					if (le.get(i).dire == -1)
						le.get(i).kill(Entity.KillMode.NORMAL);

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

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 5; j++)
				if (Arrays.stream(cdDelay[i][j]).anyMatch(v -> v != 0)) {
					elu.delay(i, j, cdDelay[i][j]);
					cdDelay[i][j] = new int[] { 0, 0, 0 };
				}
		}

		for (int i = 0; i < lineDelay.length; i++) {
			if (Arrays.stream(lineDelay[i]).anyMatch(v -> v != 0)) {
				est.delay(i, lineDelay[i]);
				lineDelay[i] = new int[] { 0, 0, 0 };
			}
		}

		if (shock) {
			for (Entity entity : le) {
				if (entity.dire == -1 && (entity.touchable() & TCH_N) > 0 && (!(entity instanceof EUnit) || !((EUnit) entity).isSpirit)) {
					entity.interrupt(INT_SW, KB_DIS[INT_SW]);
					entity.postUpdate();
				}
			}
			lea.add(new EAnimCont(700, 9, effas().A_SHOCKWAVE.getEAnim(DefEff.DEF)));
			leaSort = true;
			CommonStatic.setSE(SE_BOSS);
			shock = false;
		}

		if (s_stop == 0) {
			le.removeIf(e -> {
				boolean dead = e.anim.dead == 0 && e.summoned.isEmpty();

				if (dead && e instanceof EUnit && e.getProc().SPIRIT.exists()) {
					int[] index = ((EUnit) e).index;
					if (findEntitiesOf(index[0], index[1]).stream().noneMatch(ent -> ent.anim.dead != 0)) {
						summonerSummoned[index[0]][index[1]] = false;
						spiritSummoned[index[0]][index[1]] = false;
					}
				}

				return dead;
			});
			lw.removeIf(w -> !w.activate);
			lea.removeIf(EAnimCont::done);
			ebaseSmoke.removeIf(EAnimCont::done);
			ubaseSmoke.removeIf(EAnimCont::done);
		} else {
			lea.removeIf(content -> content instanceof WaprCont && ((WaprCont) content).timeImmune && content.done());
		}
		if (leaSort) {
			lea.sort(Comparator.comparingInt(e -> e.layer));
			leaSort = false;
		}
		updateTheme();
		if (s_stop > 0)
			s_stop--;
		s_stop = Math.max(s_stop, temp_s_stop);
		temp_s_stop = 0;
		if (s_stop == 0)
			inten = temp_inten = 0;

		if (sn_stop > 0)
			sn_stop--;
		sn_stop = Math.max(sn_stop, sn_temp_stop);
		sn_temp_stop = 0;
		if (sn_stop == 0) {
			n_inten = 0;
			temp_n_inten = 0;
		}

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
	}

	protected void updateAnimation() {
		boolean active = ebase.health > 0 && ubase.health > 0;

		if (s_stop == 0 || (ebase.getAbi() & AB_TIMEI) != 0) {
			ebase.updateAnimation();
		}

		if (s_stop == 0) {
			if(bgEffect != null)
				bgEffect.updateAnimation(st.len, battleHeight, midH);

			ubase.updateAnimation();
			canon.updateAnimation();

			if (sniper != null && active)
				sniper.updateAnimation();
		}

		if (temp_n_inten > 0)
			n_inten += temp_n_inten;

		updateEntitiesAnimation(s_stop == 0);

		while (n_inten >= 1) {
			updateEntitiesAnimation(false);
			n_inten--;
		}

		if (s_stop == 0) {
			lea.forEach(EAnimCont::update);
			ebaseSmoke.forEach(EAnimCont::update);
			ubaseSmoke.forEach(EAnimCont::update);
		} else {
			for (int i = 0; i < lea.size(); i++) {
				EAnimCont content = lea.get(i);

				if (content instanceof WaprCont && ((WaprCont) content).timeImmune) {
					content.update();
				}
			}
		}
	}

	/*
	private void updateEntitiesOld(boolean time) {
		le.sort(Comparator.comparingInt(e -> e.dire));
		ebase.update();
		ubase.update();
		for (int i = 0; i < le.size(); i++)
			if (time || (le.get(i).getAbi() & AB_TIMEI) != 0)
				le.get(i).update();
		ebase.update2();
		ubase.update2();
		for (int i = 0; i < le.size(); i++)
			if (time || (le.get(i).getAbi() & AB_TIMEI) != 0)
				le.get(i).update2();
		le.sort(Comparator.comparingInt(e -> e.layer));
		for (int i = 0; i < tlw.size(); i++)
			if (time || tlw.get(i).IMUTime())
	@@ -868,6 +1011,47 @@ private void updateEntities(boolean time) {
		for (int i = 0; i < lw.size(); i++)
			if (time || lw.get(i).IMUTime())
				lw.get(i).update();
	}
	 */

	private void updateEntities(boolean time) {

		for (int i = 0; i < tlw.size(); i++)
			if (time || tlw.get(i).IMUTime())
				tlw.get(i).update();

		for (int i = 0; i < lw.size(); i++)
			if (time || lw.get(i).IMUTime())
				lw.get(i).update();

		le.sort(Comparator.comparingInt(e -> e.dire));

		ebase.update();
		ubase.update();
		for (int i = 0; i < le.size(); i++)
			if (time || (le.get(i).getAbi() & AB_TIMEI) != 0)
				le.get(i).update();

		for (int i = 0; i < le.size(); i++) {
			if (le.get(i).dire == 1) continue;
			if (time || (le.get(i).getAbi() & AB_TIMEI) != 0)
				le.get(i).update2();
		}

		la.forEach(AttackAb::capture);
		la.forEach(AttackAb::excuse);
		la.removeIf(a -> a.duration <= 0);

		ebase.update2();
		ubase.update2();
		for (int i = 0; i < le.size(); i++) {
			if (le.get(i).dire == -1) continue;
			if (time || (le.get(i).getAbi() & AB_TIMEI) != 0)
				le.get(i).update2();
		}

		le.sort(Comparator.comparingInt(e -> e.currentLayer));

	}

	private void updateEntitiesAnimation(boolean time) {
		for (int i = 0; i < le.size(); i++)
			if (time || (le.get(i).getAbi() & AB_TIMEI) != 0)
				le.get(i).updateAnimation();

		for (int i = 0; i < tlw.size(); i++)
			if (time || tlw.get(i).IMUTime())
				tlw.get(i).updateAnimation();

		for (int i = 0; i < lw.size(); i++)
			if (time || lw.get(i).IMUTime())
				lw.get(i).updateAnimation();
	}

	private void updateTheme() {
		if (theme != null) {
			setBackground(theme);
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

	private float getOffset() {
		if(shake == null)
			return 0;

		return (1 - 2 * ((shake[SHAKE_DURATION] - shakeDuration) % 2)) * (1f * (shake[SHAKE_END] - shake[SHAKE_INITIAL]) / (shake[SHAKE_DURATION] - 1) * (shake[SHAKE_DURATION] - shakeDuration) + shake[SHAKE_INITIAL]) / SHAKE_STABILIZER;
	}

	private void setBackground(Identifier<Background> id) {
		Background newBg = Identifier.getOr(id, Background.class);
		if (bg != null && bg.id.equals(newBg.id))
			return;
		if ((bg != null && bg.effect != newBg.effect) || (bg == null && newBg.effect != -1)) {
			bgEffectInitialized = false;
			if (newBg.effect == -1)
				bgEffect = null;
			else if (newBg.effect == -newBg.id.id && BackgroundEffect.mixture.containsKey(newBg.id.id))
				bgEffect = BackgroundEffect.mixture.get(newBg.id.id);
			else if (newBg.effect >= 0)
				bgEffect = CommonStatic.getBCAssets().bgEffects.get(newBg.effect);
		}
		bg = newBg;
	}

	public void checkGuard() {
		if (activeGuard != 1 || est.hasBoss(true, false))
			return;

		for (Entity e : le) {
			if (e instanceof EEnemy && ((EEnemy) e).mark >= 1 && e.anim.dead == -1)
				return;
		}

		activeGuard = 0;
		if (ebase instanceof ECastle)
			((ECastle) ebase).guardBreak();
		else
			((EEnemy) ebase).anim.getEff(Data.GUARD_BREAK);
	}

	public int maxBankLimit() {
		if (est.lim.stageLimit == null)
			return 0;
		else
			return est.lim.stageLimit.maxMoney;
	}

	public int globalCdLimit() {
		if (est.lim.stageLimit == null)
			return 0;
		else
			return est.lim.stageLimit.globalCooldown;
	}

	public int globalCost() {
		if (est.lim.stageLimit == null)
			return -1;
		else
			return est.lim.stageLimit.globalCost;
	}

	public int cannonMultiplier() {
		if (est.lim.stageLimit == null)
			return 100;
		else
			return est.lim.stageLimit.cannonMultiplier;
	}

	public int getGlobalSpeed(int dire, int speed) {
		if (est.lim.stageLimit == null)
			return -1;
		else if (dire == -1)
			return est.lim.stageLimit.unitSpeedOverrideMode == StageLimit.SpeedOverrideMode.MULTIPLY
					? speed * est.lim.stageLimit.unitSpeedOverride / 100
					: est.lim.stageLimit.unitSpeedOverride;
		else
			return est.lim.stageLimit.enemySpeedOverrideMode == StageLimit.SpeedOverrideMode.MULTIPLY
					? speed * est.lim.stageLimit.enemySpeedOverride / 100
					: est.lim.stageLimit.enemySpeedOverride;
	}

	public int getDupeCount(int rar) {
		return est.lim.stageLimit == null ? 0 : est.lim.stageLimit.deployDuplicationTimes[rar];
	}

	public int getDupeDelay(int rar) {
		return est.lim.stageLimit == null ? 0 : est.lim.stageLimit.deployDuplicationDelay[rar];
	}

	public int getDelayStrength(int current, int max, int[] delay) {
		int prog = max - current;
		int inc = 0;
		if (delay[0] != 0) { // increase by %
			int add = Math.min(prog * Math.min(100, delay[0]) / 100, max);
			if (add == 0)
				add = delay[0] < 0 ? -1 : 1;
			inc += add;
		}
		if (delay[1] != 0) { // increase direct value
			inc += Math.min(delay[1], current);
		}
		if (delay[2] != 0) { // increase by % of max C
			int add = Math.min(max * Math.min(100, delay[2]) / 100, max);
			if (add == 0)
				add = delay[2] < 0 ? -1 : 1;
			inc += add;
		}
		return inc;
	}

	public boolean isDojoOvertime() {
		return st.trail && st.timeLimit != 0 && st.timeLimit * 1800 - time < 0;
	}

	public void scoreActivated(int proc, int dire, int traits) {
		if (!st.trail)
			return;

		for (Stage.ScoreBonus bonus : st.scoreBonus) {
			if (bonus.proc == proc && (bonus.dire == 0 || bonus.dire == dire))
				score += bonus.score / Math.max(1, traits);
		}
	}
}
