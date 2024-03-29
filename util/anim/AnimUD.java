package common.util.anim;

import common.CommonStatic;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.system.fake.FakeImage.Marker;
import common.system.files.FileData;
import common.system.files.VFile;

import java.util.ArrayList;
import java.util.List;

public class AnimUD extends AnimU<AnimUD.DefImgLoader> {

	private final String name;

	@Override
	public boolean cantLoadAll(ImageKeeper.AnimationType type) {
		return !loader.validate(type);
	}

	@Override
	public List<String> collectInvalidAnimation(ImageKeeper.AnimationType type) {
		return loader.collectInvalidAnimation(type);
	}

	static class DefImgLoader implements AnimU.ImageKeeper {

		private final String spath;
		private final VFile fnum, fedi, funi;
		private FileData dnum;
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
			if (edi != null && edi.getImg() != null && edi.getImg().bimg() != null && edi.getImg().isValid())
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
			if (VFile.get(spath + ".maanim") != null) {
				ma = new MaAnim[] { MaAnim.newIns(spath + ".maanim") };
			} else {
				if (VFile.get(spath + "_zombie00.maanim") != null)
					ma = new MaAnim[7];
				else if (VFile.get(spath + "_entry.maanim") != null)
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
			}
			ma = filterValidAnims(ma);
			return ma;
		}

		@Override
		public MaModel getMM() {
			return MaModel.newIns(spath + ".mamodel");
		}

		@Override
		public FakeImage getNum() {
			if (num != null && num.bimg() != null && num.isValid())
				return num;

			FileData fd = dnum == null ? (dnum = fnum.getData()) : dnum;

			num = fd.getImg();

			return num;
		}

		@Override
		public VImg getUni() {
			if (uni != null && uni.getImg() != null && uni.getImg().bimg() != null && uni.getImg().isValid())
				return uni;

			if (funi == null) {
				return CommonStatic.getBCAssets().slot[0];
			} else {
				uni = new VImg(funi).mark(Marker.UNI);
				uni.setCut(CommonStatic.getBCAssets().unicut);

				return uni;
			}
		}

		@Override
		public void unload() {
			dnum = null;

			if (num != null) {
				num.unload();
				num = null;
			}

			if (edi != null) {
				edi.unload();
				edi = null;
			}

			if (uni != null) {
				uni.unload();
				uni = null;
			}
		}

		@Override
		public boolean validate(AnimationType type) {
			// This is for BC animations, if validate is false, just let program crash rather
			return true;
		}

		@Override
		public List<String> collectInvalidAnimation(AnimationType type) {
			// This is for BC animations, if it contains invalid animation, just let program crash rather
			return new ArrayList<>();
		}

		private MaAnim[] filterValidAnims(MaAnim[] original) {
			int end = 0;

			for (int i = 0; i < original.length; i++) {
				// walk/attack/wait/hb must be kept no matter what
				if ((original[i] != null && original[i].n != 0) || i < 4)
					end = i;
			}

			MaAnim[] fixed = new MaAnim[end + 1];

			System.arraycopy(original, 0, fixed, 0, end + 1);

			return fixed;
		}
	}

	public AnimUD(String path, String name, String edi, String uni) {
		super(path + name, new DefImgLoader(path, name, edi, uni));
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
