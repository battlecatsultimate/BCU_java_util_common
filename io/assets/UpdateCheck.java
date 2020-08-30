package common.io.assets;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import com.google.gson.JsonElement;

import common.CommonStatic;
import common.io.WebFileIO;
import common.io.assets.UpdateCheck.UpdateJson.AssetJson;
import common.io.json.JsonClass;
import common.io.json.JsonDecoder;
import common.pack.Context;
import common.pack.UserProfile;
import common.io.json.JsonClass.NoTag;
import common.util.Data;

public class UpdateCheck {

	@JsonClass(noTag = NoTag.LOAD)
	public static class ContentJson {
		public String name, sha, download_url;
	}

	public static class Downloader {

		public final String url;
		public final File target;
		public final File temp;
		public final String desc;
		public final boolean direct;

		public Runnable post;

		private Downloader(String url, File target, File temp, String desc, boolean direct) {
			this.url = url;
			this.target = target;
			this.temp = temp;
			this.desc = desc;
			this.direct = direct;
		}

		public void run(Consumer<Double> prog) throws Exception {
			if (target.exists())
				target.delete();
			if (temp.exists())
				temp.delete();
			WebFileIO.download(url, temp, prog, direct);
			if (!target.getParentFile().exists())
				target.getParentFile().mkdirs();
			temp.renameTo(target);
			post.run();
		}

		@Override
		public String toString() {
			return desc;
		}

	}

	@JsonClass(noTag = NoTag.LOAD)
	public static class UpdateJson {

		@JsonClass(noTag = NoTag.LOAD)
		public static class AssetJson {

			public String id;
			public String ver;
			public String desc;
			public String type;

		}

		public AssetJson[] assets;
		public String[] pc_libs;
		public long text_update;
		public int music;

	}

	private static final String REG_REQLIB = "required_asset";

	static {
		addRequiredAssets("000001", "000002", "000003", "000004", "000005", "000006", "000007", "000008", "000009");
	}

	public static final String URL_UPDATE = "https://raw.githubusercontent.com/battlecatsultimate/bcu-page/master/api/updateInfo.json";
	public static final String URL_RES = "https://github.com/battlecatsultimate/bcu-resources/raw/master/resources/";
	public static final String URL_NEW = "https://github.com/battlecatsultimate/bcu-assets/raw/master/assets/";
	public static final String URL_LANG_CHECK = "https://api.github.com/repos/battlecatsultimate/bcu-assets/contents/lang";

	public static void addRequiredAssets(String... str) {
		Collections.addAll(UserProfile.getPool(REG_REQLIB, String.class), str);
	}

	public static List<Downloader> checkAsset(UpdateJson json, String type) throws Exception {
		Set<String> local = AssetLoader.previewAssets();
		Set<String> req = new HashSet<>(UserProfile.getPool(REG_REQLIB, String.class));
		req.removeIf(id -> local.contains("asset_" + id));
		if (json == null && req.size() > 0)
			throw new Exception("missing required libraries: " + req + ", internet connection required");
		List<Downloader> set = new ArrayList<Downloader>();
		if (json == null)
			return set;
		for (AssetJson aj : json.assets) {
			if (Data.getVer(aj.ver) > Data.getVer(AssetLoader.CORE_VER))
				continue;
			if (!aj.type.equals("core") && !aj.type.equals(type))
				continue;
			if (local.contains("asset_" + aj.id))
				continue;
			String url = URL_NEW + aj.id + ".asset.bcuzip";
			File temp = CommonStatic.ctx.getAssetFile("./assets/.asset.bcuzip.temp");
			File target = CommonStatic.ctx.getAssetFile("./assets/" + aj.id + ".asset.bcuzip");
			set.add(new Downloader(url, target, temp, aj.desc, false));
		}
		return set;
	}

	public static Context.SupExc<List<Downloader>> checkLang(String[] files) {
		Map<String, String> local = CommonStatic.getConfig().localLangMap;
		File f = CommonStatic.ctx.getAssetFile("./lang");
		String path = f.getPath() + "/";
		return () -> {
			JsonElement je0 = WebFileIO.read(URL_LANG_CHECK);
			ContentJson[] cont = JsonDecoder.decode(je0, ContentJson[].class);
			Map<String, ContentJson> map = new HashMap<>();
			List<Downloader> list = new ArrayList<>();
			for (ContentJson c : cont)
				map.put(c.name, c);
			for (String str : files) {
				ContentJson cj = map.get(str.replace('/', '-'));
				if (cj == null)
					continue;
				File dst = new File(path + str);
				if (!dst.exists() || !cj.sha.equals(local.get(str))) {
					File tmp = new File(path + ".temp");
					String desc = "download language file " + str;
					Downloader d = new Downloader(cj.download_url, dst, tmp, desc, false);
					list.add(d);
					d.post = () -> local.put(str, cj.sha);
				}
			}
			return list;
		};
	}

	public static List<Downloader> checkMusic(int count) {
		boolean[] exi = new boolean[count];
		File music = CommonStatic.ctx.getAssetFile("./music/");
		if (music.exists())
			for (File m : music.listFiles())
				if (m.getName().length() == 7 && m.getName().endsWith(".ogg")) {
					Integer id = Data.ignore(() -> Integer.parseInt(m.getName().substring(0, 3)));
					if (id != null && id < count && id >= 0)
						exi[id] = true;
				}
		List<Downloader> ans = new ArrayList<>();
		for (int i = 0; i < count; i++)
			if (!exi[i]) {
				File target = CommonStatic.ctx.getAssetFile("./music/" + Data.trio(i) + ".ogg");
				File temp = CommonStatic.ctx.getAssetFile("./music/.ogg.temp");
				String url = URL_RES + "music/" + Data.trio(i) + ".ogg";
				ans.add(new Downloader(url, target, temp, "music " + Data.trio(i), false));
			}
		return ans;
	}

	public static List<Downloader> checkPCLibs(UpdateJson json) throws Exception {
		File lib = new File("./BCU_lib");
		List<Downloader> libs = new ArrayList<>();
		if (json != null) {
			Set<String> str = new HashSet<>();
			Collections.addAll(str, json.pc_libs);
			if (lib.exists())
				for (File f : lib.listFiles())
					str.remove(f.getName());
			for (String s : str)
				libs.add(new Downloader(URL_RES + "jar/BCU_lib/" + s, new File("./BCU_lib/" + s),
						new File("./BCU_lib/.jar.temp"), "downloading BCU library " + s, false));
		}
		return libs;
	}

	public static UpdateJson checkUpdate() throws Exception {
		JsonElement update = WebFileIO.read(URL_UPDATE);
		if (update == null)
			return null;
		UpdateJson json = JsonDecoder.decode(update, UpdateJson.class);
		return json;
	}

}
