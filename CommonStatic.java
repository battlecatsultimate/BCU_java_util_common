package common;

import static java.lang.Character.isDigit;

import java.io.File;
import java.util.ArrayList;
import java.util.Queue;
import java.util.function.Function;

import common.io.InStream;
import common.io.OutStream;
import common.system.VImg;
import common.util.pack.Background;
import common.util.pack.Pack;
import common.util.stage.Castles;
import common.util.stage.CharaGroup;
import common.util.stage.MapColc;
import common.util.stage.Recd;
import common.util.unit.DIYAnim;

public class CommonStatic {

	public static class Account {

		public static String USERNAME = "";
		public static long PASSWORD = 0;

	}

	public static interface BattleConst {

		public static final double ratio = 768.0 / 2400.0;// r = p/u

	}

	public static interface EditLink {

		public void review();

	}

	public static interface FakeKey {

		public boolean pressed(int i, int j);

		public void remove(int i, int j);

	}

	public static interface Itf {

		/** create a file if not exist */
		public void check(File f);

		/** delete a file */
		public void delete(File file);

		/** exit */
		public void exit(boolean save);

		/** show loading progress, empty is fine */
		public void prog(String str);

		/** read InStream from file */
		public InStream readBytes(File fi);

		/** load read image instance */
		public VImg readReal(File fi);

		/** read strings from path */
		public <T> T readSave(String path, Function<Queue<String>, T> func);

		public void redefine(Class<?> class1);

		public File route(String path);

		/** play sound effect */
		public void setSE(int ind);

		/** write OutStream into file */
		public boolean writeBytes(OutStream os, String path);

	}

	public static class Lang {

		public static final String[] LOC_CODE = { "en", "zh", "kr", "jp", "ru", "de", "fr", "nl", "es" };

		public static final int[][] pref = { { 0, 3, 1, 2 }, { 1, 3, 0, 2 }, { 2, 3, 0, 1 }, { 3, 0, 1, 2 } };

		public static int lang = 0;

	}

	public static Itf def;

	public static void clearData() {
		Pack.map.clear();
		MapColc.MAPS.clear();
		Background.iclist.clear();
		Castles.defcas.clear();
		Castles.map.clear();
		CharaGroup.map.clear();
		Recd.map.clear();
		DIYAnim.map.clear();
		Pack.def = new Pack();
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

}
