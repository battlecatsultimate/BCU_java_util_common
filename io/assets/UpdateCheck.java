package common.io.assets;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import com.google.gson.JsonElement;

import common.CommonStatic;
import common.io.Progress;
import common.io.WebFileIO;
import common.io.assets.UpdateCheck.UpdateJson.AssetJson;
import common.io.json.JsonClass;
import common.io.json.JsonDecoder;
import common.pack.UserProfile;
import common.io.json.JsonClass.NoTag;
import common.util.Data;

public class UpdateCheck {

	private static final String REG_REQLIB = "required_asset";

	static {
		addRequiredAssets("000001", "000002", "000003", "000004", "000005", "000006", "000007", "000008", "000009");
	}

	public static void addRequiredAssets(String... str) {
		Collections.addAll(UserProfile.getPool(REG_REQLIB, String.class), str);
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
		public String[] pc_lib;
		public long text_update;
		public int music;

	}

	public static class Downloader {

		public final String url;
		public final File target;
		public final File temp;
		public final String desc;

		private Downloader(String url, File target, File temp, String desc) {
			this.url = url;
			this.target = target;
			this.temp = temp;
			this.desc = desc;
		}

		public void run(Consumer<Progress> prog) throws Exception {
			if (target.exists())
				target.delete();
			if (temp.exists())
				temp.delete();
			WebFileIO.download(url, temp, prog);
			temp.renameTo(target);
		}

	}

	public static final String URL_UPDATE = "https://raw.githubusercontent.com/battlecatsultimate/bcu-page/master/api/updateInfo.json";
	public static final String URL_RES = "https://github.com/battlecatsultimate/bcu-resources/raw/master/resources/";
	public static final String URL_NEW = "https://github.com/battlecatsultimate/bcu-assets/raw/master/assets/";

	public static UpdateJson checkUpdate() throws Exception {
		JsonElement update = WebFileIO.read(URL_UPDATE);
		if (update == null)
			return null;
		UpdateJson json = JsonDecoder.decode(update, UpdateJson.class);
		return json;
	}

	public static List<Downloader> checkAsset(UpdateJson json, String type) {
		Set<String> local = AssetLoader.previewAssets();
		List<Downloader> set = new ArrayList<Downloader>();
		for (AssetJson aj : json.assets) {
			if (Data.getVer(aj.ver) > Data.getVer(AssetLoader.CORE_VER))
				continue;
			if (!aj.type.equals("core") && !aj.type.equals(type))
				continue;
			if (local.contains("asset_" + aj.id))
				continue;
			String url = URL_NEW + aj.id + ".asset.bcuzip";
			File temp = CommonStatic.ctx.getAssetFile("./assets/.temp.asset.bcuzip");
			File target = CommonStatic.ctx.getAssetFile("./assets/" + aj.id + ".assets.bcuzip");
			set.add(new Downloader(url, target, temp, aj.desc));
		}
		return set;
	}

	public static List<Downloader> checkMusic(int count) {
		boolean[] exi = new boolean[count];
		File music = CommonStatic.ctx.getAssetFile("./music/");
		for (File m : music.listFiles())
			if (m.getName().length() == 7 && m.getName().endsWith(".ogg")) {
				Integer id = Data.ignore(() -> Integer.parseInt(m.getName().substring(0, 3)));
				if (id != null)
					exi[id] = true;
			}
		List<Downloader> ans = new ArrayList<>();
		for (int i = 0; i < count; i++)
			if (!exi[i]) {
				File target = CommonStatic.ctx.getAssetFile("./music" + Data.trio(i) + ".ogg");
				File temp = CommonStatic.ctx.getAssetFile("./music/.temp.ogg");
				String url = URL_RES + "music/" + Data.trio(i) + ".ogg";
				ans.add(new Downloader(url, target, temp, "music " + Data.trio(i)));
			}
		return ans;
	}

}
