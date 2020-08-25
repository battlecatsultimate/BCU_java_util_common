package common.io.assets;

import com.google.gson.JsonElement;

import common.io.WebFileIO;
import common.io.json.JsonClass;
import common.io.json.JsonDecoder;
import common.io.json.JsonClass.NoTag;

public class UpdateCheck {
	
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

	public static final String URL_UPDATE = "https://raw.githubusercontent.com/battlecatsultimate/bcu-page/master/api/updateInfo.json";

	public static boolean checkUpdate() {
		JsonElement update = WebFileIO.read(URL_UPDATE);
		if(update == null)
			return false;
		UpdateJson json = JsonDecoder.decode(update, UpdateJson.class);
		
		
		return true;
	}

}
