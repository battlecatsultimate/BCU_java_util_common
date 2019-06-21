package common.util.system.fake;

public class CommonStatic {

	public static interface BattleConst {

		public static final double ratio = 768.0 / 2400.0;// r = p/u

	}
	public static interface EditLink {

		public void review();

	}

	public static interface Lang {

		public String[] getLoc(int i, String... s);

	}

	public static final String[] LOC_CODE = { "en", "zh", "kr", "jp", "ru", "de", "fr", "nl", "es" };

	public static int lang = 0;

	public static Lang def;

	public static String[] getLoc(int i, String... s) {

		return null;
	}

}
