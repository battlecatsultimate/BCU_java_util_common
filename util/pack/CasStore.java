package common.util.pack;

import java.io.File;
import java.util.Map;

import common.CommonStatic;
import common.CommonStatic.ImgReader;
import common.CommonStatic.ImgWriter;
import common.io.InStream;
import common.io.OutStream;
import common.system.FixIndexList;
import common.system.VImg;
import common.util.Data;
import common.util.stage.AbCastle;
import common.util.stage.Castles;

public class CasStore extends FixIndexList<VImg> implements AbCastle {

	private Pack pack;

	protected CasStore(Pack p, boolean reg) {
		super(new VImg[1000]);
		pack = p;
		if (reg)
			Castles.map.put(pack.id, this);
	}

	@Override
	public void add(VImg img) {
		String name = pack.id + Data.trio(nextInd());
		img.name = name;
		super.add(img);
	}

	@Override
	public int getCasID(VImg img) {
		int ind = indexOf(img);
		if (ind < 0 || img == null)
			ind = 0;
		return pack.id * 1000 + ind;
	}

	public String nameOf(VImg img) {
		return Data.trio(indexOf(img));
	}

	@Override
	public String toString() {
		return pack.toString();
	}

	protected OutStream packup(ImgWriter w) {
		OutStream cas = OutStream.getIns();
		cas.writeString("0.3.7");
		Map<Integer, VImg> mcas = getMap();
		cas.writeInt(mcas.size());
		for (int ind : mcas.keySet()) {
			cas.writeInt(ind);
			ImgWriter.writeImg(cas, w, mcas.get(ind).getImg());
		}
		cas.terminate();
		return cas;
	}

	protected OutStream write() {
		OutStream os = OutStream.getIns();
		os.writeString("0.3.7");
		os.writeInt(0);
		os.terminate();
		return os;
	}

	protected void zread$p000306(InStream cas, ImgReader r) {
		int n = cas.nextInt();
		for (int i = 0; i < n; i++) {
			int val = cas.nextInt();
			VImg vimg = ImgReader.readImg(cas, r);
			vimg.name = Data.trio(val);
			set(val, vimg);
		}
	}

	protected void zread$t000306(InStream is) {
		is.nextInt();

		File f = CommonStatic.def.route("./res/img/" + pack.id + "/cas/");
		if (f.exists()) {
			File[] fs = f.listFiles();
			for (File fi : fs) {
				String str = fi.getName();
				if (str.length() != 7)
					continue;
				if (!str.endsWith(".png"))
					continue;
				int val = -1;

				try {
					val = Integer.parseInt(str.substring(0, 3));
				} catch (NumberFormatException e) {
					e.printStackTrace();
					continue;
				}
				VImg bimg = CommonStatic.def.readReal(fi);
				if (val >= 0 && bimg != null)
					set(val, bimg);
			}
		}
	}

	protected void zreadp(int ver, InStream cas, ImgReader r) {
		if (ver >= 307)
			ver = getVer(cas.nextString());
		if (ver >= 306)
			zread$p000306(cas, r);
	}

	protected void zreadt(int ver, InStream cas) {
		if (ver >= 307)
			ver = getVer(cas.nextString());
		if (ver >= 306)
			zread$t000306(cas);
	}

}