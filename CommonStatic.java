package common;

import java.io.File;

import common.util.system.VImg;

public class CommonStatic {

	public static interface BattleConst {

		public static final double ratio = 768.0 / 2400.0;// r = p/u

	}

	public static interface EditLink {

		public void review();

	}

	public static interface FakeKey {

		boolean pressed(int i, int j);

		void remove(int i, int j);

	}

	public static abstract class Itf {

		public abstract String get(int i, String s);

		public abstract String[] get(int i, String s, int n);

		public abstract String[] getLoc(int i, String... s);

		public abstract void prog(String str);

		public abstract VImg readReal(File fi);

	}

	public static final String[] LOC_CODE = { "en", "zh", "kr", "jp", "ru", "de", "fr", "nl", "es" };

	public static int lang = 0;

	public static Itf def;

}
