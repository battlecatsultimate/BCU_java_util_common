package common;

import common.io.InStream;
import common.io.OutStream;
import common.io.assets.Admin.StaticPermitted;
import common.io.json.JsonClass;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonField;
import common.pack.Context;
import common.pack.Source;
import common.pack.UserProfile;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.util.Data;
import common.util.anim.ImgCut;
import common.util.anim.MaAnim;
import common.util.anim.MaModel;
import common.util.pack.EffAnim.EffAnimStore;
import common.util.pack.NyCastle;
import common.util.pack.WaveAnim;
import common.util.stage.Music;
import common.util.unit.Combo;
import common.util.unit.UnitLevel;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.function.Function;

import static java.lang.Character.isDigit;

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

		// Background resources
		public MaModel ewavm, uwavm;
		public MaAnim ewava, uwava;
		public WaveAnim defu, defe;
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

	public interface ImgReader {

		static File loadMusicFile(InStream is, ImgReader r, int pid, int mid) {
			if (r == null || r.isNull())
				r = CommonStatic.def.getMusicReader(pid, mid);
			return r.readFile(is);
		}

		static VImg readImg(InStream is, ImgReader r) {
			if (r != null && !r.isNull())
				return r.readImgOptional(is.nextString());
			return new VImg(is.nextBytesI());
		}

		default boolean isNull() {
			return true;
		}

		File readFile(InStream is);

		FakeImage readImg(String str);

		VImg readImgOptional(String str);
	}

	public interface ImgWriter {

		static boolean saveFile(OutStream os, ImgWriter w, File file) {
			if (w != null)
				os.writeString(w.saveFile(file));
			else
				try {
					OutStream data = OutStream.getIns();
					byte[] bs = new byte[(int) file.length()];
					BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
					buf.read(bs, 0, bs.length);
					buf.close();
					data.writeBytesI(bs);
					data.terminate();
					os.accept(data);
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			return true;
		}

		static boolean writeImg(OutStream os, ImgWriter w, FakeImage img) {
			if (w != null)
				os.writeString(w.writeImg(img));
			else
				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					FakeImage.write(img, "PNG", baos);
					os.writeBytesI(baos.toByteArray());
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			return true;
		}

		String saveFile(File f);

		String writeImg(FakeImage img);

		String writeImgOptional(VImg img);

	}

	public interface Itf {

		/**
		 * exit
		 */
		void exit(boolean save);

		long getMusicLength(Music f);

		ImgReader getMusicReader(int pid, int mid);

		ImgReader getReader(File f);

		Source.AnimLoader loadAnim(InStream is, ImgReader r);

		/**
		 * show loading progress, empty is fine
		 */
		void prog(String str);

		/**
		 * read InStream from file
		 */
		InStream readBytes(File fi);

		/**
		 * load read image instance
		 */
		VImg readReal(File fi);

		/**
		 * read strings from path
		 */
		<T> T readSave(String path, Function<Queue<String>, T> func);

		File route(String path);

		void setSE(int ind);

		/**
		 * write OutStream into file
		 */
		boolean writeBytes(OutStream os, String path);

		/**
		 * Write error log to somewhere
		 **/
		void writeErrorLog(Exception e);
	}

	public static class Lang {

		@StaticPermitted
		public static final String[] LOC_CODE = { "en", "zh", "kr", "jp", "ru", "de", "fr", "nl", "es" };

		@StaticPermitted
		public static final int[][] pref = { { 0, 3, 1, 2 }, { 1, 3, 0, 2 }, { 2, 3, 0, 1 }, { 3, 0, 1, 2 },
				{ 0, 3, 1, 2 }, { 0, 3, 1, 2 }, { 0, 3, 1, 2 } };

	}

	@StaticPermitted(StaticPermitted.Type.ENV)
	public static Itf def;

	@StaticPermitted(StaticPermitted.Type.ENV)
	public static Context ctx;

	public static BCAuxAssets getBCAssets() {
		return UserProfile.getStatic("BCAuxAssets", BCAuxAssets::new);
	}

	public static Config getConfig() {
		return UserProfile.getStatic("config", Config::new);
	}

	public static boolean isInteger(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
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
			ans[i] = Integer.parseInt(lstr.get(i));
		return ans;
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
		String res = "{";

		for (int i = 0; i < data.length; i++) {
			if (i == data.length - 1) {
				res += data[i] + "}";
			} else {
				res += data[i] + ", ";
			}
		}

		return res;
	}

}
