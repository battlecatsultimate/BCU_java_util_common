package common.util.anim;

import common.system.MultiLangFile;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.system.fake.FakeImage.Marker;
import common.system.files.AssetData;
import common.system.files.VFile;
import common.util.Res;
import common.util.anim.AnimU.ImageLoader;

public class AnimUD extends AnimU<AnimUD.DefImgLoader> implements MultiLangFile {

	static class DefImgLoader implements ImageLoader {

		private final String spath;
		private final VFile<AssetData> fnum, fedi, funi;
		private AnimUD anim;
		private AssetData dnum;
		private FakeImage num;
		private VImg edi, uni;

		private DefImgLoader(String path, String str, String sedi, String suni) {
			spath = path + str;
			fnum = VFile.get(spath + ".png");
			fedi = sedi == null ? null : VFile.get(path + sedi);
			funi = suni == null ? null : VFile.get(path + suni);
		}

		@Override
		public VImg getEdi() {
			if (edi != null)
				return edi;
			return fedi == null ? null : (edi = new VImg(fedi).mark(Marker.EDI));
		}

		@Override
		public ImgCut getIC() {
			return ImgCut.newIns(spath + ".imgcut");
		}

		@Override
		public MaAnim[] getMA() {
			MaAnim[] ma;
			if (VFile.getFile(spath + "_zombie00.maanim") != null)
				ma = new MaAnim[7];
			else if (VFile.getFile(spath + "_entry.maanim") != null)
				ma = new MaAnim[5];
			else
				ma = new MaAnim[4];
			for (int i = 0; i < 4; i++)
				ma[i] = MaAnim.newIns(spath + "0" + i + ".maanim");
			if (ma.length == 5)
				ma[4] = MaAnim.newIns(spath + "_entry.maanim");
			if (ma.length == 7)
				for (int i = 0; i < 3; i++)
					ma[i + 4] = MaAnim.newIns(spath + "_zombie0" + i + ".maanim");
			return ma;
		}

		@Override
		public MaModel getMM() {
			return MaModel.newIns(spath + ".mamodel");
		}

		@Override
		public FakeImage getNum() {
			if (num != null)
				return num;
			AssetData fd = dnum == null ? (dnum = fnum.getData()) : dnum;
			num = fd.getImg(anim);
			return num;
		}

		@Override
		public VImg getUni() {
			if (uni != null)
				return uni;
			return funi == null ? Res.slot[0] : (uni = new VImg(funi).mark(Marker.UNI));
		}

		@Override
		public void reload(AssetData data) {
			dnum = data;
			num.unload();
			num = null;
		}

		@Override
		public void unload() {
			dnum = null;
			num.unload();
			num = null;
		}

		private void setAnim(AnimUD a) {
			anim = a;
		}

	}

	public AnimUD(String path, String name, String edi, String uni) {
		super(path + name, new DefImgLoader(path, name, edi, uni));
		loader.setAnim(this);
	}

	@Override
	public void reload(AssetData ad) {
		if (!loaded)
			return;
		loader.reload(ad);
		parts = imgcut.cut(getNum());
	}

}
