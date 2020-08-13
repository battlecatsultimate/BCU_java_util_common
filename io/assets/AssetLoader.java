package common.io.assets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import common.CommonStatic;
import common.io.PackLoader;
import common.io.PackLoader.Preload;
import common.io.PackLoader.ZipDesc;
import common.io.PackLoader.ZipDesc.FileDesc;
import common.io.assets.Admin.StaticPermitted;
import common.io.assets.AssetLoader.AssetHeader.AssetEntry;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.pack.Context;
import common.pack.Context.ErrType;
import common.pack.PackData.PackDesc;
import common.system.files.VFile;
import common.util.Data;

@StaticPermitted
public class AssetLoader {

	@JsonClass
	public static class AssetHeader {

		@JsonClass
		public static class AssetEntry {

			@JsonField
			public final PackDesc desc;

			@JsonField
			public final int size;

			public AssetEntry() {
				desc = null;
				size = 0;
			}

			private AssetEntry(File f) throws Exception {
				desc = PackLoader.readPack((fd) -> false, f).desc;
				size = (int) f.length();
			}

			@Override
			public boolean equals(Object o) {
				return (o instanceof AssetEntry) && ((AssetEntry) o).desc.id.equals(desc.id);
			}

		}

		@JsonField(generic = AssetEntry.class)
		public final ArrayList<AssetEntry> list = new ArrayList<>();

		public int offset;

		public AssetHeader() {
		}

		private boolean add(File file) throws Exception {
			AssetEntry ent = new AssetEntry(file);
			if (list.contains(ent))
				return false;
			list.add(ent);
			return true;
		}

	}

	public static final String CORE_VER = "0.5.0.0";

	private static final String[] ANIMFL = { ".imgcut", ".mamodel", ".maanim" };
	private static final String[] NONPRE = { "\\./org/img/../.....\\.png", "\\./org/enemy/.../..._.\\.png",
			"\\./org/unit/..././..._.\\.png", "\\./org/unit/..././udi..._.\\.png" };

	private static final int LEN = 1024;

	public static void load() {
		try {
			File folder = CommonStatic.ctx.getAssetFile("./assets/");
			for (File f : folder.listFiles()) {
				if (f.getName().endsWith(".assets.bcuzips")) {
					List<ZipDesc> list = PackLoader.readAssets(AssetLoader::getPreload, f);
					for (ZipDesc zip : list)
						if (Data.getVer(zip.desc.id) <= Data.getVer(CORE_VER))
							VFile.getBCFileTree().merge(zip.tree);
				}
			}
		} catch (Exception e) {
			CommonStatic.ctx.noticeErr(e, ErrType.FATAL, "failed to read asset");
		}
	}

	public static void merge() throws Exception {
		try {
			File folder = CommonStatic.ctx.getAssetFile("./assets/");
			Map<String, Map<String, File>> map = new TreeMap<>();
			for (File f : folder.listFiles()) {
				if (f.getName().endsWith(".asset.bcuzip")) {
					String pre = f.getName().substring(0, 2);
					String name = f.getName().substring(0, 6);
					Map<String, File> sub = map.get(pre);
					if (sub == null)
						map.put(pre, sub = new TreeMap<>());
					sub.put(name, f);
				}
			}
			for (Entry<String, Map<String, File>> emain : map.entrySet()) {
				File target = CommonStatic.ctx.getAssetFile("./assets/" + emain.getKey() + "xxxx.assets.bcuzips");
				File dst = CommonStatic.ctx.getAssetFile("./assets/.temp.assets.bcuzips");
				Context.check(dst);
				FileOutputStream fos = new FileOutputStream(dst);
				AssetHeader header = new AssetHeader();
				FileInputStream fis = null;
				if (target.exists()) {
					fis = new FileInputStream(target);
					PackLoader.read(fis, header);
				}
				List<File> queue = new ArrayList<>();
				for (Entry<String, File> esub : emain.getValue().entrySet())
					if (header.add(esub.getValue()))
						queue.add(esub.getValue());
					else
						Context.delete(esub.getValue());
				PackLoader.write(fos, header);
				if (fis != null) {
					stream(fos, fis);
					fis.close();
				}
				for (File efile : queue) {
					fis = new FileInputStream(efile);
					stream(fos, fis);
					fis.close();
					Context.delete(efile);
				}
				fos.close();
				Context.delete(target);
				dst.renameTo(target);
			}
		} catch (Exception e) {
			// TODO corruption prevention
			CommonStatic.ctx.noticeErr(e, ErrType.FATAL, "failed to merge asset");
		}
	}

	public static Set<String> previewAssets() {
		try {
			File folder = CommonStatic.ctx.getAssetFile("./assets/");
			Set<String> ans = new TreeSet<>();
			for (File f : folder.listFiles()) {
				if (f.getName().endsWith(".assets.bcuzips")) {
					AssetHeader header = new AssetHeader();
					FileInputStream fis = new FileInputStream(f);
					PackLoader.read(fis, header);
					fis.close();
					for (AssetEntry ent : header.list)
						ans.add(ent.desc.id);
				}
			}
			return ans;
		} catch (Exception e) {
			CommonStatic.ctx.noticeErr(e, ErrType.FATAL, "failed to preview asset");
			return null;
		}
	}

	private static Preload getPreload(ZipDesc desc) {
		return Data.getVer(desc.desc.id) > Data.getVer(CORE_VER) ? (fd) -> false : AssetLoader::preload;
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

	private static void stream(FileOutputStream fos, FileInputStream fis) throws IOException {
		byte[] data = new byte[LEN];
		int len;
		do {
			len = fis.read(data);
			fos.write(data, 0, len);
		} while (len == LEN);
	}

}
