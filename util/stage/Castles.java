package common.util.stage;

import java.util.ArrayList;
import java.util.List;
import common.pack.UserProfile;
import common.system.VImg;
import common.system.files.AssetData;
import common.system.files.VFile;
import common.util.Data;

public class Castles implements AbCastle {

	public final List<VImg> list = new ArrayList<>();
	public final String str;

	public final int id;

	public Castles(int hash, String name) {
		id = hash;
		str = name;
		for (VFile<AssetData> vf : VFile.get("./org/img/" + name).list())
			list.add(new VImg(vf));
		UserProfile.getRegister(AbCastle.REG_CASTLE, AbCastle.class).put(Data.hex(id), this);
	}

	@Override
	public VImg get(int ind) {
		return list.get(ind);
	}

	@Override
	public int getCasID(VImg img) {
		return id * 1000 + list.indexOf(img);
	}

	@Override
	public List<VImg> getList() {
		return list;
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public String toString() {
		return str + " (" + list.size() + ")";
	}

}
