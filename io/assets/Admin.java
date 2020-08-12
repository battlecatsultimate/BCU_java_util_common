package common.io.assets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import common.io.PackLoader;
import common.io.PackLoader.ZipDesc;
import common.io.PackLoader.ZipDesc.FileDesc;
import common.pack.Context;
import common.pack.PackData.PackDesc;
import common.pack.Source;
import common.util.Data;

public class Admin {

	private static class AdminContext implements Context {

		@Override
		public boolean confirmDelete() {
			System.out.println("skip delete confirmation");
			return true;
		}

		@Override
		public File getAssetFile(String string) {
			return new File("./assets/" + string);
		}

		@Override
		public File getLangFile(String file) {
			return new File("./assets/lang/en/" + file);
		}

		@Override
		public File getPackFolder() {
			return new File("./packs");
		}

		@Override
		public File getWorkspaceFile(String relativePath) {
			return new File("./workspace/" + relativePath);
		}

		@Override
		public void noticeErr(Exception e, ErrType t, String str) {
			printErr(t, str);
			e.printStackTrace(t == ErrType.INFO ? System.out : System.err);
		}

		@Override
		public boolean preload(FileDesc desc) {
			return Admin.preload(desc);
		}

		@Override
		public void printErr(ErrType t, String str) {
			(t == ErrType.INFO ? System.out : System.err).println(str);
		}

	}

	private static final String[] ANIMFL = { ".imgcut", ".mamodel", ".maanim" };

	private static final String[] NONPRE = { "\\./org/img/../.....\\.png", "\\./org/enemy/.../..._.\\.png",
			"\\./org/unit/..././..._.\\.png", "\\./org/unit/..././udi..._.\\.png" };

	public static void main(String[] args) throws Exception {
		Source.ctx = new AdminContext();
		AssetLoader.merge();
		System.out.println(AssetLoader.previewAssets());
	}

	public static List<ZipDesc> read() throws Exception {
		File folder = new File("./assets/assets/");
		List<ZipDesc> list = new ArrayList<>();
		for (File f : folder.listFiles()) {
			if (f.getName().endsWith(".asset.bcuzip")) {
				ZipDesc zd = PackLoader.readPack(Admin::preload, f);
				list.add(zd);
			}
		}
		return list;
	}

	public static void write() throws Exception {
		for (int i = 1; i <= 9; i++) {
			File f = new File("./assets/" + Data.hex(i));
			File dst = new File("./assets/assets/" + Data.hex(i) + ".asset.bcuzip");
			Context.check(dst);
			PackDesc pd = new PackDesc("asset_" + Data.hex(i));
			pd.author = "PONOS";
			pd.BCU_VERSION = "0.5.0.0";
			pd.desc = "default required asset " + Data.hex(i);
			PackLoader.writePack(dst, f, pd, "battlecatsultimate");
		}
	}

	private static boolean preload(FileDesc fd) {
		for (String str : ANIMFL)
			if (fd.path.endsWith(str))
				return false;
		for (String str : NONPRE)
			if (fd.path.length() == str.length() - 2 && fd.path.matches(str))
				return false;
		return true;
	}

}
