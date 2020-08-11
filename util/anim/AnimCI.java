package common.util.anim;

import common.CommonStatic;
import common.pack.Source;
import common.pack.Source.ResourceLocation;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.system.fake.FakeImage.Marker;
import common.system.files.AssetData;
import common.util.Res;

public class AnimCI extends AnimU<AnimCI.AnimCIKeeper> {

	protected static class AnimCIKeeper implements AnimU.ImageKeeper {

		public final Source.AnimLoader loader;
		private FakeImage num;
		private boolean ediLoaded = false;
		private VImg edi;
		private VImg uni;

		private AnimCIKeeper(Source.AnimLoader al) {
			loader = al;
		}

		@Override
		public VImg getEdi() {
			if (ediLoaded)
				return edi;
			ediLoaded = true;
			edi = loader.getEdi();
			if (edi != null)
				edi.mark(Marker.EDI);
			return edi;
		}

		@Override
		public ImgCut getIC() {
			return loader.getIC();
		}

		@Override
		public MaAnim[] getMA() {
			return loader.getMA();
		}

		@Override
		public MaModel getMM() {
			return loader.getMM();
		}

		public ResourceLocation getName() {
			return loader.getName();
		}

		@Override
		public FakeImage getNum() {
			if (num != null && num.bimg() != null && num.isValid())
				return num;
			return num = loader.getNum();
		}

		public int getStatus() {
			return loader.getStatus();
		}

		@Override
		public VImg getUni() {
			if (uni != null)
				return uni;
			uni = loader.getUni();
			if (uni != null)
				uni.mark(Marker.UNI);
			else
				uni = Res.slot[0];
			return uni;
		}

		@Override
		public void reload(AssetData data) {
		}

		public void setEdi(VImg vedi) {
			edi = vedi;

			if (vedi != null)
				vedi.mark(Marker.EDI);

			ediLoaded = true;
		}

		public void setNum(FakeImage fimg) {
			num = fimg;
		}

		public void setUni(VImg vuni) {
			uni = vuni;
			uni.mark(Marker.UNI);
		}

		@Override
		public void unload() {
		}

	}

	public ResourceLocation name;

	public AnimCI(Source.AnimLoader acl) {
		super(new AnimCIKeeper(acl));
		name = loader.getName();
	}

	@Override
	public void load() {
		try {
			super.load();
			if (getEdi() != null)
				getEdi().check();
			if (getUni() != null)
				getUni().check();
		} catch (Exception e) {
			e.printStackTrace();
			CommonStatic.def.exit(false);
		}
		validate();
	}

	@Override
	public String toString() {
		return name.id;
	}

}