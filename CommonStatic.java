package common;

import common.io.InStream;
import common.io.assets.Admin.StaticPermitted;
import common.io.json.JsonClass;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonField;
import common.pack.Context;
import common.pack.Source;
import common.pack.UserProfile;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.system.fake.ImageBuilder;
import common.util.Data;
import common.util.anim.ImgCut;
import common.util.pack.EffAnim.EffAnimStore;
import common.util.pack.NyCastle;
import common.util.stage.Music;
import common.util.unit.Combo;
import common.util.unit.UnitLevel;

import java.io.File;
import java.math.BigInteger;
import java.util.*;

import static java.lang.Character.isDigit;

@SuppressWarnings("DeprecatedIsStillUsed")
public class CommonStatic {

	public interface BattleConst {

		double ratio = 768.0 / 2400.0;// r = p/u

	}

	public static class BCAuxAssets {

		// Res resources
		public VImg[] slot = new VImg[3];
		public VImg[][] ico = new VImg[2][];
		public VImg[][] num = new VImg[9][11];
		public VImg[][] battle = new VImg[3][];
		public VImg[][] icon = new VImg[4][];
		public VImg[] timer = new VImg[11];

		// Background resources
		public final List<ImgCut> iclist = new ArrayList<>();

		// Available data for atk/res orb, will be used for GUI
		// Map<Trait, Grades>
		public final Map<Integer, List<Integer>> ATKORB = new TreeMap<>();
		public final Map<Integer, List<Integer>> RESORB = new TreeMap<>();
		public final Map<Integer, Integer> DATA = new HashMap<>();

		public FakeImage[] TYPES;
		public FakeImage[] TRAITS;
		public FakeImage[] GRADES;

		// NyCastle
		public final VImg[][] main = new VImg[3][NyCastle.TOT];
		public final NyCastle[] atks = new NyCastle[NyCastle.TOT];

		// EffAnim
		public final EffAnimStore effas = new EffAnimStore();

		// Combo
		public final Combo[][] combos = new Combo[Data.C_TOT][];
		public final int[][] values = new int[Data.C_TOT][5];
		public int[][] filter;

		// Form cuts
		public ImgCut unicut, udicut;

		// RandStage
		public final int[][] randRep = new int[5][];

		// def unit level
		public UnitLevel defLv;

	}

	@JsonClass(noTag = NoTag.LOAD)
	public static class Config {

		@JsonField(generic = { String.class, String.class })
		public HashMap<String, String> localLangMap = new HashMap<>();

		// ImgCore
		public int deadOpa = 10, fullOpa = 90;
		public int[] ints = new int[] { 1, 1, 1, 2 };
		public boolean ref = true, battle = false, icon = false;
		public boolean twoRow = true;
		// Lang
		public int lang;
	}

	public interface EditLink {

		void review();

	}

	public interface FakeKey {

		boolean pressed(int i, int j);

		void remove(int i, int j);

	}

	@Deprecated
	public interface ImgReader {

		static File loadMusicFile(InStream is, ImgReader r, int pid, int mid) {
			if (r == null || r.isNull())
				r = CommonStatic.def.getMusicReader(pid, mid);
			return r.readFile(is);
		}

		static VImg readImg(InStream is, ImgReader r) {
			if (r != null && !r.isNull())
				return r.readImgOptional(is.nextString());
			return ImageBuilder.toVImg(is.nextBytesI());
		}

		default boolean isNull() {
			return true;
		}

		File readFile(InStream is);

		FakeImage readImg(String str);

		VImg readImgOptional(String str);
	}

	public interface Itf {

		/**
		 * exit
		 */
		void exit(boolean save);

		long getMusicLength(Music f);

		@Deprecated
		ImgReader getMusicReader(int pid, int mid);

		@Deprecated
		ImgReader getReader(File f);

		@Deprecated
		Source.AnimLoader loadAnim(InStream is, ImgReader r);

		@Deprecated
		InStream readBytes(File fi);

		@Deprecated
		File route(String path);

		void setSE(int ind);

	}

	public static class Lang {

		@StaticPermitted
		public static final String[] LOC_CODE = { "en", "zh", "kr", "jp", "ru", "de", "fr", "nl", "es", "it" };

		@StaticPermitted
		public static final int[][] pref = { { 0, 3, 1, 2 }, { 1, 3, 0, 2 }, { 2, 3, 0, 1 }, { 3, 0, 1, 2 },
				{ 0, 3, 1, 2 }, { 5, 0, 3, 1 }, { 6, 0, 3, 1 }, { 0, 3, 1, 2 }, { 8, 0, 3, 1 }, { 9, 0, 3, 1 } };

	}

	@StaticPermitted(StaticPermitted.Type.ENV)
	public static Itf def;

	@StaticPermitted(StaticPermitted.Type.ENV)
	public static Context ctx;

	@StaticPermitted(StaticPermitted.Type.FINAL)
	public static final BigInteger max = new BigInteger(String.valueOf(Integer.MAX_VALUE));
	@StaticPermitted(StaticPermitted.Type.FINAL)
	public static final BigInteger min = new BigInteger(String.valueOf(Integer.MIN_VALUE));

	public static BCAuxAssets getBCAssets() {
		return UserProfile.getStatic("BCAuxAssets", BCAuxAssets::new);
	}

	public static Config getConfig() {
		return UserProfile.getStatic("config", Config::new);
	}

	public static boolean isInteger(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				if(str.charAt(i) != '-' || i != 0)
					return false;
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

	public static int[] parseIntsN(String str) {
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

}
