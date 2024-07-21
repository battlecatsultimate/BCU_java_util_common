package common;

import common.io.assets.Admin.StaticPermitted;
import common.io.json.JsonClass;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonField;
import common.pack.Context;
import common.pack.Identifier;
import common.pack.UserProfile;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.util.Data;
import common.util.anim.ImgCut;
import common.util.anim.MaModel;
import common.util.pack.EffAnim.EffAnimStore;
import common.util.pack.NyCastle;
import common.util.pack.bgeffect.BackgroundEffect;
import common.util.stage.Music;
import common.util.unit.UnitLevel;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.isDigit;

public class CommonStatic {

	public interface BattleConst {

		float ratio = 768f / 2400f;// r = p/u

	}

	public static class BCAuxAssets {

		// Res resources
		public VImg[] slot = new VImg[3];
		public VImg[][] ico = new VImg[2][];
		public VImg[][] num = new VImg[9][11];
		public VImg[][] battle = new VImg[3][];
		public VImg[][] icon = new VImg[5][];
		public VImg[] timer = new VImg[11];
		public VImg emptyEdi = null;

		public Map<Integer, VImg> gatyaitem = new HashMap<>();
		public VImg XP;
		public VImg[][] moneySign = new VImg[4][4]; //Money on, off/Cost on, off
		public VImg[] spiritSummon = new VImg[4];
		/**
		 * Use this if trait.icon is null
		 */
		public VImg dummyTrait;

		// Background resources
		public final List<ImgCut> iclist = new ArrayList<>();

		// Available data for orb, will be used for GUI
		// Map<Type, Map<Trait, Grades>>
		public final Map<Integer, Map<Integer, List<Integer>>> ORB = new TreeMap<>();
		public final Map<Integer, Integer> DATA = new HashMap<>();

		public FakeImage[] TYPES;
		public FakeImage[] TRAITS;
		public FakeImage[] GRADES;

		// NyCastle
		public final VImg[][] main = new VImg[3][NyCastle.TOT];
		public final NyCastle[] atks = new NyCastle[NyCastle.TOT];

		// EffAnim
		public final EffAnimStore effas = new EffAnimStore();

		public final int[][] values = new int[Data.C_TOT][5];
		public int[][] filter;

		// Form cuts
		public ImgCut unicut, udicut;

		// RandStage
		public final int[][] randRep = new int[5][];

		// def unit level
		public UnitLevel defLv;

		// bg effect
		public final ArrayList<BackgroundEffect> bgEffects = new ArrayList<>();

	}

	@JsonClass(noTag = NoTag.LOAD)
	public static class Config {

		@JsonField(generic = { String.class, String.class })
		public HashMap<String, String> localLangMap = new HashMap<>();

		@JsonField(generic = { Integer.class, String.class })
		public HashMap<Integer, String> localMusicMap = new HashMap<>();

		// ImgCore
		public int deadOpa = 10, fullOpa = 90;
		public int[] ints = new int[] { 1, 1, 1, 2 };
		public boolean ref = true, battle = false, icon = false;
		public boolean twoRow = true;
		/**
		 * Use this variable to unlock plus level for aku outbreak
		 */
		public boolean plus = false;
		/**
		 * Use this variable to adjust level limit for aku outbreak
		 */
		public int levelLimit = 0;
		// Lang
		public Lang.Locale lang = Lang.Locale.EN;
		/**
		 * Restoration target backup file, null means none
		 */
		public String backupFile;
		/**
		 * Used for partial restoration
		 */
		public String backupPath;
		/**
		 * Maximum number of backups, 0 means infinite
		 */
		public int maxBackup = 5;

		/**
		 * Preferred level for units
		 */
		public int prefLevel = 50;

		/**
		 * Decide whehter draw bg effect or not
		 */
		public boolean drawBGEffect = true;

		/**
		 * Enable 6f button delay on spawn
		 */
		public boolean buttonDelay = true;

		/**
		 * Color of background in viewer
		 */
		public int viewerColor = -1;

		/**
		 * Make BCU show ex stage continuation pop-up if true
		 */
		public boolean exContinuation = false;

		/**
		 * Make EX stage pop-up shown considering real chance
		 */
		public boolean realEx = false;

		/**
		 * Make stage name image displayed in battle
		 */
		public boolean stageName = true;

		/**
		 * Make battle shaken
		 */
		public boolean shake = true;

		/**
		 * Replace old music when updated
		 */
		public boolean updateOldMusic = true;

		/**
		 * Perform realistic BC levelings
		 */
		public boolean realLevel = false;

		/**
		 * 60 fps mode if this is true for animation
		 */
		public boolean performanceModeAnimation = false;

		/**
		 * 60 fps mode if this is true for battle
		 */
		public boolean performanceModeBattle = false;
	}

	public interface EditLink {

		void review();

	}

	public interface FakeKey {

		boolean pressed(int i, int j);

		void remove(int i, int j);

	}

	public interface Itf {

		/**
		 * exit
		 */
		void save(boolean save, boolean exit);

		long getMusicLength(Music f);

		@Deprecated
		File route(String path);

		void setSE(int mus);

		void setSE(Identifier<Music> mus);

		void setBGM(Identifier<Music> mus);
	}

	public static class Lang {
		public enum Locale {
			EN("en"),
			ZH("zh"),
			KR("kr"),
			JP("jp"),
			RU("ru"),
			DE("de"),
			FR("fr"),
			ES("es"),
			IT("it"),
			TH("th");

			public final String code;

			Locale(String localeCode) {
				code = localeCode;
			}

			@Override
			public String toString() {
				return code;
			}
		}

		/**
		 * List of priorities that each language will display
		 */
		@StaticPermitted
		public static final Locale[][] pref = {
				{ Locale.EN, Locale.FR, Locale.IT, Locale.ES, Locale.DE, Locale.RU, Locale.JP, Locale.ZH, Locale.KR },
				{ Locale.ZH, Locale.JP, Locale.EN, Locale.KR },
				{ Locale.KR, Locale.JP, Locale.EN, Locale.ZH},
				{ Locale.JP, Locale.EN, Locale.ZH, Locale.KR },
				{ Locale.RU, Locale.EN, Locale.JP, Locale.ZH, Locale.KR },
				{ Locale.DE, Locale.EN, Locale.JP, Locale.ZH, Locale.KR },
				{ Locale.FR, Locale.EN, Locale.JP, Locale.ZH, Locale.KR },
				{ Locale.ES, Locale.EN, Locale.JP, Locale.ZH, Locale.KR },
				{ Locale.IT, Locale.EN, Locale.JP, Locale.ZH, Locale.KR },
				{ Locale.TH, Locale.EN, Locale.JP, Locale.ZH, Locale.KR }
		};

		/**
		 * List of languages that are supported by Ponos
		 */
		@StaticPermitted
		public static final CommonStatic.Lang.Locale[] supportedLanguage = {
				CommonStatic.Lang.Locale.EN,
				CommonStatic.Lang.Locale.ZH,
				CommonStatic.Lang.Locale.JP,
				CommonStatic.Lang.Locale.KR,
				CommonStatic.Lang.Locale.DE,
				CommonStatic.Lang.Locale.FR,
				CommonStatic.Lang.Locale.ES,
				CommonStatic.Lang.Locale.IT,
				CommonStatic.Lang.Locale.TH
		};
	}

	@StaticPermitted(StaticPermitted.Type.ENV)
	public static Itf def;

	@StaticPermitted(StaticPermitted.Type.ENV)
	public static Context ctx;

	@StaticPermitted(StaticPermitted.Type.FINAL)
	public static final BigInteger max = new BigInteger(String.valueOf(Integer.MAX_VALUE));
	@StaticPermitted(StaticPermitted.Type.FINAL)
	public static final BigDecimal maxdbl = new BigDecimal(String.valueOf(Double.MAX_VALUE));
	@StaticPermitted(StaticPermitted.Type.FINAL)
	public static final BigInteger min = new BigInteger(String.valueOf(Integer.MIN_VALUE));
	@StaticPermitted(StaticPermitted.Type.FINAL)
	public static final BigDecimal mindbl = new BigDecimal(String.valueOf(Double.MIN_VALUE));

	public static BCAuxAssets getBCAssets() {
		return UserProfile.getStatic("BCAuxAssets", BCAuxAssets::new);
	}

	public static Config getConfig() {
		return UserProfile.getStatic("config", Config::new);
	}

	public static boolean isInteger(String str) {
		str = str.trim();

		if(str.isEmpty())
			return false;

		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				if(str.charAt(i) != '-' || i != 0)
					return false;
			}
		}

		return true;
	}

	public static boolean isDouble(String str) {
		int dots = 0;
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				if((i == 0 && str.charAt(i) != '-') || str.charAt(i) != '.' || dots > 0)
					return false;
				else
					dots++;
			}
		}

		return true;
	}

	public static int parseIntN(String str) {
		int ans;
		try {
			ans = parseIntsN(str)[0];
		} catch (Exception e) {
			ans = -1;
		}
		return ans;
	}

	public static String verifyFileName(String str) {
		return str.replaceAll("[\\\\/:*<>?\"|]", "_");
	}

	public static double parseDoubleN(String str) {
		double ans;
		try {
			ans = parseDoublesN(str)[0];
		} catch (Exception e) {
			ans = -1.0;
		}
		return ans;
	}

	public static float parseFloatN(String str) {
		float ans;

		try {
			ans = parseFloatsN(str)[0];
		} catch (Exception e) {
			ans = -1f;
		}

		return ans;
	}

	public static double[] parseDoublesN(String str) {
		ArrayList<String> lstr = new ArrayList<>();
		Matcher matcher = Pattern.compile("-?(([.|,]\\d+)|\\d+([.|,]\\d*)?)").matcher(str);

		while (matcher.find())
			lstr.add(matcher.group());

		double[] result = new double[lstr.size()];
		for (int i = 0; i < lstr.size(); i++)
			result[i] = safeParseDouble(lstr.get(i));
		return result;
	}

	public static float[] parseFloatsN(String str) {
		ArrayList<String> lstr = new ArrayList<>();
		Matcher matcher = Pattern.compile("-?(([.|,]\\d+)|\\d+([.|,]\\d*)?)").matcher(str);

		while (matcher.find())
			lstr.add(matcher.group());

		float[] result = new float[lstr.size()];

		for (int i = 0; i < lstr.size(); i++)
			result[i] = safeParseFloat(lstr.get(i));

		return result;
	}

	public static int[] parseIntsN(String str) {
		ArrayList<String> lstr = new ArrayList<>();
		int t = -1;
		for (int i = 0; i < str.length(); i++)
			if (t == -1) {
				if (isDigit(str.charAt(i)) || str.charAt(i) == '-')
					t = i;
			} else if (!isDigit(str.charAt(i))) {
				lstr.add(str.substring(t, i));
				t = -1;
			}
		if (t != -1)
			lstr.add(str.substring(t));
		int ind = 0;
		while (ind < lstr.size()) {
			if (isDigit(lstr.get(ind).charAt(0)) || lstr.get(ind).length() > 1)
				ind++;
			else
				lstr.remove(ind);
		}
		int[] ans = new int[lstr.size()];
		for (int i = 0; i < lstr.size(); i++)
			ans[i] = safeParseInt(lstr.get(i));
		return ans;
	}

	public static int safeParseInt(String v) {
		if(isInteger(v)) {
			BigInteger big = new BigInteger(v);

			if(big.compareTo(max) > 0) {
				return Integer.MAX_VALUE;
			} else if(big.compareTo(min) < 0) {
				return Integer.MIN_VALUE;
			} else {
				return Integer.parseInt(v);
			}
		} else {
			throw new IllegalStateException("Value "+v+" isn't a number");
		}
	}

	public static double safeParseDouble(String v) {
		if(isDouble(v)) {
			BigDecimal big = new BigDecimal(v);

			if(big.compareTo(maxdbl) > 0) {
				return Double.MAX_VALUE;
			} else if(big.compareTo(mindbl) < 0) {
				return Double.MIN_VALUE;
			} else {
				return Double.parseDouble(v);
			}
		} else {
			throw new IllegalStateException("Value "+v+" isn't a number");
		}
	}

	public static float safeParseFloat(String v) {
		if(isDouble(v)) {
			BigDecimal big = new BigDecimal(v);

			if(big.compareTo(maxdbl) > 0) {
				return Float.MAX_VALUE;
			} else if(big.compareTo(mindbl) < 0) {
				return Float.MIN_VALUE;
			} else {
				return Float.parseFloat(v);
			}
		} else {
			throw new IllegalStateException("Value "+v+" isn't a number");
		}
	}

	public static String[] getPackContentID(String input) {
		StringBuilder packID = new StringBuilder();
		StringBuilder entityID = new StringBuilder();

		boolean packEnd = false;

		for (int i = 0; i < input.length(); i++) {
			if (!packEnd) {
				if (Character.toString(input.charAt(i)).matches("[0-9a-z]"))
					packID.append(input.charAt(i));
				else {
					packEnd = true;
				}
			} else {
				if (Character.isDigit(input.charAt(i)))
					entityID.append(input.charAt(i));
			}
		}

		return new String[] { packID.toString(), entityID.toString() };
	}

	public static String[] getPackEntityID(String input) {
		String[] result = new String[2];

		StringBuilder packID = new StringBuilder();
		StringBuilder entityID = new StringBuilder();

		boolean packEnd = false;
		boolean findDigit = false;

		for(int i = 0; i < input.length(); i++) {
			if(!packEnd) {
				if(Character.toString(input.charAt(i)).matches("[0-9a-z]"))
					packID.append(input.charAt(i));
				else {
					packEnd = true;
					findDigit = true;
				}
			} else {
				if(findDigit) {
					if(Character.isDigit(input.charAt(i))) {
						entityID.append(input.charAt(i));
						findDigit = false;
					}
				} else {
					if(Character.isDigit(input.charAt(i))) {
						entityID.append(input.charAt(i));
					} else if(input.charAt(i) == 'r') {
						entityID.append(input.charAt(i));
						break;
					} else {
						break;
					}
				}
			}
		}

		result[0] = packID.toString();
		result[1] = entityID.toString();

		return result;
	}

	public static long parseLongN(String str) {
		long ans;
		try {
			ans = parseLongsN(str)[0];
		} catch (Exception e) {
			ans = -1;
		}
		return ans;
	}

	public static long[] parseLongsN(String str) {
		ArrayList<String> lstr = new ArrayList<>();
		int t = -1;
		for (int i = 0; i < str.length(); i++)
			if (t == -1) {
				if (isDigit(str.charAt(i)) || str.charAt(i) == '-' || str.charAt(i) == '+')
					t = i;
			} else if (!isDigit(str.charAt(i))) {
				lstr.add(str.substring(t, i));
				t = -1;
			}
		if (t != -1)
			lstr.add(str.substring(t));
		int ind = 0;
		while (ind < lstr.size()) {
			if (isDigit(lstr.get(ind).charAt(0)) || lstr.get(ind).length() > 1)
				ind++;
			else
				lstr.remove(ind);
		}
		long[] ans = new long[lstr.size()];
		for (int i = 0; i < lstr.size(); i++)
			ans[i] = Long.parseLong(lstr.get(i));
		return ans;
	}

	/**
	 * play sound effect
	 */
	public static void setSE(int ind) {
		def.setSE(ind);
	}

	/**
	 * play sound effect with identifier
	 */
	public static void setSE(Identifier<Music> mus) {
		def.setSE(mus);
	}

	/**
	 * play background music
	 * @param music Music
	 */
	public static void setBGM(Identifier<Music> music) {
		def.setBGM(music);
	}

	public static String toArrayFormat(int... data) {
		StringBuilder res = new StringBuilder("{");

		for (int i = 0; i < data.length; i++) {
			if (i == data.length - 1) {
				res.append(data[i]).append("}");
			} else {
				res.append(data[i]).append(", ");
			}
		}

		return res.toString();
	}

	/**
	 * Gets the minimum position value for a data enemy.
	 */
	public static float dataEnemyMinPos(MaModel model) {
		int x = ((model.confs[0][2] - model.parts[0][6]) * model.parts[0][8]) / model.ints[0];
		return 2.5f * x;
	}

	/**
	 * Gets the minimum position value for a custom enemy.
	 */
	public static float customEnemyMinPos(MaModel model) {
		int x = ((model.confs[0][2] - model.parts[0][6]) * model.parts[0][8]) / model.ints[0];
		return 2.5f * x;
	}

	/**
	 * Gets the minimum position value for a data cat unit.
	 */
	public static int dataFormMinPos(MaModel model) {
		int x = ((model.confs[1][2] - model.parts[0][6]) * model.parts[0][8]) / model.ints[0];
		return 5 * x;
	}

	/**
	 * Gets the minimum position value for a custom cat unit.
	 */
	public static int customFormMinPos(MaModel model) {
		int x = ((model.confs[1][2] - model.parts[0][6]) * model.parts[0][8]) / model.ints[0];
		return 5 * x;
	}

	/**
	 * Gets the boss spawn point for a castle.
	 * Basically 3200 + yx/10 + 0.9*z but the 0.9*z part appears to use a quirky rounding
	 */
	public static float bossSpawnPoint(int y, int z) {
		return (float) (3200 + (y * z / 10) + (9 * z + 8 * z % 10) / 10) / 4f;
	}
}
