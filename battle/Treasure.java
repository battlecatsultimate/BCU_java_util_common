package common.battle;

import com.google.common.primitives.Ints;
import common.CommonStatic;
import common.battle.data.MaskUnit;
import common.battle.data.Orb;
import common.io.InStream;
import common.io.OutStream;
import common.io.json.JsonClass;
import common.io.json.JsonClass.RType;
import common.io.json.JsonField;
import common.io.json.JsonField.GenType;
import common.system.files.VFile;
import common.util.Data;
import common.util.unit.Level;

import java.util.*;

@SuppressWarnings("ForLoopReplaceableByForEach")
@JsonClass(read = RType.FILL)
public class Treasure extends Data {
	public static void readCannonCurveData() {
		VFile vf = VFile.get("./org/data/CC_AllParts_growth.csv");

		if(vf != null) {
			Queue<String> q = vf.getData().readLine();

			q.poll();

			Map<Integer, Map<Integer, ArrayList<ArrayList<Integer>>>> initCurve = new HashMap<>();
			Map<Integer, Integer> maxLevels = new HashMap<>();
			Map<Integer, Integer> previousMaxLevel = new HashMap<>();

			int previousType = -1;

			String line;

			while((line = q.poll()) != null) {
				int[] data = CommonStatic.parseIntsN(line);

				int id = data[0];

				//Skip analyzing data about normal cannon
				if(id == 0)
					continue;

				Map<Integer, ArrayList<ArrayList<Integer>>> curveData;

				if(initCurve.containsKey(id)) {
					curveData = initCurve.get(id);
				} else {
					curveData = new HashMap<>();
				}

				int type = data[1];

				if(type != previousType)
					previousMaxLevel.clear();

				ArrayList<ArrayList<Integer>> curves;

				if(curveData.containsKey(type)) {
					curves = curveData.get(type);
				} else {
					curves = new ArrayList<>();

					curves.add(new ArrayList<>());
					curves.add(new ArrayList<>());
				}

				int maxLevel = data[2];

				if(!maxLevels.containsKey(id) || maxLevels.get(id) < maxLevel) {
					maxLevels.put(id, maxLevel);
				}

				int difference;

				if(previousMaxLevel.containsKey(id)) {
					difference = maxLevel - previousMaxLevel.get(id);
				} else {
					difference = maxLevel;
				}

				int min = data[3];
				int max = data[4];

				double segment = (max - min) * 1.0 / (difference / 10.0);

				int mn;
				int mx;

				for(int i = 0; i < difference; i += 10) {
					mn = min + (int) (segment * i);
					mx = min + (int) (segment * (i + 1));

					curves.get(0).add(mn);
					curves.get(1).add(mx);
				}

				curveData.put(type, curves);

				initCurve.put(id, curveData);

				previousMaxLevel.put(id, maxLevel);
				previousType = type;
			}

			for(int id : initCurve.keySet()) {
				Map<Integer, ArrayList<ArrayList<Integer>>> curveData = initCurve.get(id);

				Map<Integer, int[][]> filteredData = new HashMap<>();

				for(int type : curveData.keySet()) {
					ArrayList<ArrayList<Integer>> curves = curveData.get(type);

					int[][] filteredCurves = new int[2][];

					filteredCurves[0] = Ints.toArray(curves.get(0));
					filteredCurves[1] = Ints.toArray(curves.get(1));

					filteredData.put(type, filteredCurves);
				}

				Treasure.curveData.put(id, new CannonLevelCurve(filteredData, maxLevels.get(id)));
			}
		}
	}

	public static final Map<Integer, CannonLevelCurve> curveData = new HashMap<>();

	public final Basis b;

	@JsonField(gen = GenType.FILL)
	public int[] tech = new int[LV_TOT], trea = new int[T_TOT], bslv = new int[BASE_TOT], fruit = new int[7],
			gods = new int[3];

	@JsonField
	public int alien, star;

	/**
	 * new Treasure object
	 */
	protected Treasure(Basis bas) {
		b = bas;
		zread$000000();
	}

	/**
	 * read Treasure from data
	 */
	protected Treasure(Basis bas, int ver, InStream is) {
		b = bas;
		zread(ver, is);
	}

	/**
	 * copy Treasure object
	 */
	protected Treasure(Basis bas, Treasure t) {
		b = bas;
		tech = t.tech.clone();
		trea = t.trea.clone();
		fruit = t.fruit.clone();
		gods = t.gods.clone();
		alien = t.alien;
		star = t.star;
		bslv = t.bslv.clone();
	}

	/**
	 * get multiplication of non-starred alien
	 */
	public double getAlienMulti() {
		return 7 - alien * 0.01;
	}

	/**
	 * get cat attack multiplication
	 */
	public double getAtkMulti() {
		return 1 + trea[T_ATK] * 0.005;
	}

	/**
	 * get base health
	 */
	public int getBaseHealth() {
		int t = tech[LV_BASE];
		int base = t < 6 ? t * 1000 : t < 8 ? 5000 + (t - 5) * 2000 : 9000 + (t - 7) * 3000;
		base += trea[T_BASE] * 70;
		if (bslv[0] > 10)
			base += 36000 + 4000 * (bslv[0] - 10);
		else
			base += 3600 * bslv[0];
		return base * (100 + b.getInc(C_BASE)) / 100;
	}

	/**
	 * get normal canon attack
	 */
	public int getCanonAtk() {
		int base = 50 + tech[LV_CATK] * 50 + trea[T_CATK] * 5;
		return base * (100 + b.getInc(C_C_ATK)) / 100;
	}

	public double getCannonMagnification(int id, int type) {
		if(curveData.containsKey(id)) {
			CannonLevelCurve levelCurve = curveData.get(id);

			return levelCurve.applyFormula(type, bslv[id]);
		}

		System.out.println("Warning : Unknown ID : "+ id);

		return 0;
	}

	/**
	 * get cat health multiplication
	 */
	public double getDefMulti() {
		return 1 + trea[T_DEF] * 0.005;
	}

	/**
	 * get accounting multiplication
	 */
	public double getDropMulti() {
		return (0.95 + 0.05 * tech[LV_ACC] + 0.005 * trea[T_ACC]) * (1 + b.getInc(C_MEAR) * 0.01);
	}

	/**
	 * get EVA kill ability attack multiplication
	 */
	public double getEKAtk() {
		return 0.05 * (100 + b.getInc(C_EKILL));
	}

	/**
	 * get EVA kill ability reduce damage multiplication
	 */
	public double getEKDef() {
		return 20.0 / (100 + b.getInc(C_EKILL));
	}

	/**
	 * get processed cat cool down time
	 * max treasure & level should lead to -264f recharge
	 */
	public int getFinRes(int ori) {
		double research = (tech[LV_RES] - 1) * 6 + trea[T_RES] * 0.3;
		double deduction = research + Math.floor(research * b.getInc(C_RESP) / 100);
		return (int) Math.max(60, ori - deduction);
	}

	/**
	 * get reverse cat cool down time
	 */
	public int getRevRes(int res) {
		double research = (tech[LV_RES] - 1) * 6 + trea[T_RES] * 0.3;
		double addition = research + Math.floor(research * b.getInc(C_RESP) / 100);
		return (int) Math.max(60, res + addition);

	}

	/**
	 * get maximum fruit of certain trait bitmask
	 */
	public double getFruit(int type) {
		double ans = 0;
		if ((type & TB_RED) != 0)
			ans = Math.max(ans, fruit[T_RED]);
		if ((type & TB_BLACK) != 0)
			ans = Math.max(ans, fruit[T_BLACK]);
		if ((type & TB_ANGEL) != 0)
			ans = Math.max(ans, fruit[T_ANGEL]);
		if ((type & TB_FLOAT) != 0)
			ans = Math.max(ans, fruit[T_FLOAT]);
		if ((type & TB_ALIEN) != 0)
			ans = Math.max(ans, fruit[T_ALIEN]);
		if ((type & TB_METAL) != 0)
			ans = Math.max(ans, fruit[T_METAL]);
		if ((type & TB_ZOMBIE) != 0)
			ans = Math.max(ans, fruit[T_ZOMBIE]);
		return ans * 0.01;
	}

	/**
	 * get attack multiplication from strong against ability
	 */
	public double getGOODATK(int type) {
		double ini = 1.5 * (1 + 0.2 / 3 * getFruit(type));
		double com = 1 + b.getInc(C_GOOD) * 0.01;
		return ini * com;
	}

	public double getGoodAtkWithOrb(int type, MaskUnit data, Level l) {
		Orb orb = data.getOrb();

		double ini = 1.5 * (1 + 0.2 / 3 * getFruit(type));

		if(orb != null && l.getOrbs() != null) {
			int[][] orbs = l.getOrbs();

			for(int i = 0; i < orbs.length; i++) {
				if(orbs[i].length < ORB_TOT)
					continue;

				if(orbs[i][ORB_TYPE] == ORB_STRONG && (type & orbs[i][ORB_TRAIT]) != 0) {
					ini += ORB_STR_ATK_MULTI[orbs[i][ORB_GRADE]];
				}
			}
		}

		double com = 1 + b.getInc(C_GOOD) * 0.01;

		return ini * com;
	}

	/**
	 * get damage reduce multiplication from strong against ability
	 */
	public double getGOODDEF(int type, MaskUnit data, Level level) {
		Orb orb = data.getOrb();

		double ini = 0.5 - 0.1 / 3 * getFruit(type);

		if(orb != null && level.getOrbs() != null) {
			int[][] orbs = level.getOrbs();

			for(int i = 0; i < orbs.length; i++) {
				if(orbs[i].length < ORB_TOT)
					continue;

				if(orbs[i][ORB_TYPE] == ORB_STRONG && (type & orbs[i][ORB_TRAIT]) != 0) {
					ini *= 1 - ORB_STR_DEF_MULTI[orbs[i][ORB_GRADE]] / 100.0;
				}
			}
		}

		double com = 1 - b.getInc(C_GOOD) * 0.01;

		return ini * com;
	}

	/**
	 * get attack multiplication from massive damage ability
	 */
	public double getMASSIVEATK(int type) {
		double ini = 3 + 1.0 / 3 * getFruit(type);
		double com = 1 + b.getInc(C_MASSIVE) * 0.01;
		return ini * com;
	}

	public double getMassiveAtkWithOrb(int type, MaskUnit data, Level l) {
		Orb orb = data.getOrb();

		double ini = 3 + 1.0 / 3 * getFruit(type);

		if(orb != null && l.getOrbs() != null) {
			int[][] orbs = l.getOrbs();

			for(int i = 0; i < orbs.length; i++) {
				if(orbs[i].length < ORB_TOT)
					continue;

				if(orbs[i][ORB_TYPE] == ORB_MASSIVE && (type & orbs[i][ORB_TRAIT]) != 0) {
					ini += ORB_MASSIVE_MULTI[orbs[i][ORB_GRADE]];
				}
			}
		}

		double com = 1 + b.getInc(C_MASSIVE) * 0.01;

		return ini * com;
	}

	/**
	 * get attack multiplication from super massive damage ability
	 */
	public double getMASSIVESATK(int type) {
		return 5 + 1.0 / 3 * getFruit(type);
	}

	/**
	 * get damage reduce multiplication from resistant ability
	 */
	public double getRESISTDEF(int type, MaskUnit data, Level level) {
		double ini = 0.25 - 0.05 / 3 * getFruit(type);

		Orb orb = data.getOrb();

		if(orb != null && level.getOrbs() != null) {
			int[][] orbs = level.getOrbs();

			for(int i = 0; i < orbs.length; i++) {
				if(orbs[i].length < ORB_TOT)
					continue;

				if(orbs[i][ORB_TYPE] == ORB_RESISTANT && (orbs[i][ORB_TRAIT] & type) != 0) {
					ini *= 1 - ORB_RESISTANT_MULTI[orbs[i][ORB_GRADE]] / 100.0;
				}
			}
		}

		double com = 1 - b.getInc(C_RESIST) * 0.01;
		return ini * com;
	}

	/**
	 * get damage reduce multiplication from super resistant ability
	 */
	public double getRESISTSDEF(int type) {
		return 1.0 / 6 - 1.0 / 126 * getFruit(type);
	}

	/**
	 * get multiplication of starred enemy
	 */
	public double getStarMulti(int st) {
		if (st == 1)
			return 16 - star * 0.01;
		else
			return 11 - 0.1 * gods[st - 2];
	}

	/**
	 * get witch kill ability attack multiplication
	 */
	public double getWKAtk() {
		return 0.05 * (100 + b.getInc(C_WKILL));
	}

	/**
	 * get witch kill ability reduce damage multiplication
	 */
	public double getWKDef() {
		return 10.0 / (100 + b.getInc(C_WKILL));
	}

	public double getXPMult() {
		int txp1 = trea[T_XP1];
		int txp2 = trea[T_XP2];
		double tm = txp1 * 0.005 + txp2 * 0.0025;
		return 0.95 + tech[LV_XP] * 0.05 + tm;
	}

	/**
	 * get canon recharge time
	 */
	protected int CanonTime(int map) {
		int base = 1500 + 50 * (tech[LV_CATK] - tech[LV_RECH]);
		if (trea[T_RECH] <= 300)
			base -= (int) (1.5 * trea[T_RECH]);
		else
			base -= 3 * trea[T_RECH] - 450;
		base -= b.getInc(C_C_SPE);
		base = Math.max(950, base + map * 450);
		return base;
	}

	/**
	 * get the cost to upgrade worker cat
	 */
	protected int getLvCost(int lv) {
		int t = tech[LV_WORK];
		int base = t < 8 ? 30 + 10 * t : 20 * t - 40;
		return lv == 8 ? -1 : base * lv * 100;
	}

	/**
	 * get wallet capacity
	 */
	protected int getMaxMon(int lv) {
		int base = Math.max(25, 50 * tech[LV_WALT]);
		base = base * (1 + lv);
		base += trea[T_WALT] * 10;
		return base * (100 + b.getInc(C_M_MAX));
	}

	/**
	 * get money increase rate
	 */
	protected int getMonInc(int lv) {
		double output = (0.15 + 0.1 * tech[LV_WORK]) * (1 + (lv - 1) * 0.1) + trea[T_WORK] * 0.01;
		return (int) Math.round(output * 100);
	}

	/**
	 * save data to file
	 */
	protected void write(OutStream os) {
		os.writeString("0.4.0");
		os.writeIntB(tech);
		os.writeIntB(trea);
		os.writeInt(alien);
		os.writeInt(star);
		os.writeIntB(fruit);
		os.writeIntB(gods);
		os.writeIntB(bslv);
	}

	/**
	 * read date from file, support multiple versions
	 */
	private void zread(int val, InStream is) {
		zread$000000();

		if (val >= 305)
			val = getVer(is.nextString());

		if (val >= 400)
			zread$000400(is);
		else if (val >= 305)
			zread$000305(is);
		else if (val >= 304)
			zread$000304(is);
		else if (val >= 301)
			zread$000301(is);
		else if (val >= 203)
			zread$000203(is);
	}

	private void zread$000000() {
		System.arraycopy(MLV, 0, tech, 0, LV_TOT);
		System.arraycopy(MT, 0, trea, 0, T_TOT);
		fruit[T_RED] = fruit[T_BLACK] = fruit[T_FLOAT] = fruit[T_ANGEL] = 300;
		fruit[T_METAL] = fruit[T_ZOMBIE] = fruit[T_ALIEN] = 300;
		bslv[0] = 20;
		for (int i = 1; i < BASE_TOT; i++)
			bslv[i] = curveData.get(i).max;
		gods[0] = gods[1] = gods[2] = 100;
		alien = 600;
		star = 1500;
	}

	private void zread$000203(InStream is) {
		for (int i = 0; i < 8; i++)
			tech[i] = is.nextByte();
		for (int i = 0; i < 9; i++)
			trea[i] = is.nextShort();
		alien = is.nextInt();
		star = is.nextInt();
		fruit = is.nextIntsB();
		gods = is.nextIntsB();
	}

	private void zread$000301(InStream is) {
		zread$000203(is);
		for (int i = 0; i < 5; i++)
			bslv[i] = is.nextByte();
	}

	private void zread$000304(InStream is) {
		zread$000203(is);
		for (int i = 0; i < 6; i++)
			bslv[i] = is.nextByte();

	}

	private void zread$000305(InStream is) {
		zread$000203(is);
		int[] temp = is.nextIntsB();
		System.arraycopy(temp, 0, bslv, 0, temp.length);
	}

	private void zread$000400(InStream is) {
		int[] lv = is.nextIntsB();
		int[] tr = is.nextIntsB();
		System.arraycopy(lv, 0, tech, 0, Math.min(LV_TOT, lv.length));
		System.arraycopy(tr, 0, trea, 0, Math.min(T_TOT, tr.length));
		alien = is.nextInt();
		star = is.nextInt();
		fruit = is.nextIntsB();
		gods = is.nextIntsB();
		int[] bs = is.nextIntsB();
		System.arraycopy(bs, 0, bslv, 0, bs.length);
	}

}
