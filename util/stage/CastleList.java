package common.util.stage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import common.pack.FixIndexList.FixIndexMap;
import common.pack.PackData.Identifier;
import common.pack.PackData.UserPack;
import common.pack.UserProfile;
import common.system.VImg;
import common.system.files.AssetData;
import common.system.files.VFile;
import common.util.stage.MapColc.PackMapColc;

public class CastleList extends FixIndexMap<CastleImg> {

	public static class DefCasList extends CastleList {

		public final String str;
		public final String id;

		public DefCasList(String hash, String name) {
			id = hash;
			str = name;
			for (VFile<AssetData> vf : VFile.get("./org/img/" + name).list())
				add(new CastleImg(new Identifier<CastleImg>(id, CastleImg.class, size()), new VImg(vf)));
			map().put(id, this);
			defset().add(this);

		}

		@Override
		public String toString() {
			return str + " (" + size() + ")";
		}
	}

	public static class PackCasList extends CastleList {

		public final UserPack pack;

		public PackCasList(UserPack p) {
			pack = p;
			map().put(pack.desc.id, this);
		}

		@Override
		public String toString() {
			return pack.desc.name + " (" + size() + ")";
		}
	}

	private static final String REG_CASTLE = "castle";

	private static final String REG_DEF_CASTLE = "def_castle";

	public static Set<CastleList> defset() {
		return UserProfile.getPool(REG_DEF_CASTLE, CastleList.class);
	}

	public static Collection<CastleList> from(Stage sta) {
		MapColc mc = sta.map.mc;
		if (!(mc instanceof PackMapColc))
			return defset();
		List<CastleList> list = new ArrayList<>();
		list.addAll(defset());
		UserPack pack = ((PackMapColc) mc).pack;
		list.add(pack.castles);
		for (String rel : pack.desc.dependency)
			list.add(UserProfile.getUserPack(rel).castles);
		return list;
	}

	public static Map<String, CastleList> map() {
		return UserProfile.getRegister(REG_CASTLE, CastleList.class);
	}

	private CastleList() {
		super(CastleImg.class);
	}
}
