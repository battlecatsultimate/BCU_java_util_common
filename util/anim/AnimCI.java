package common.util.anim;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

import common.CommonStatic;
import common.io.InStream;
import common.io.OutStream;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.system.fake.FakeImage.Marker;
import common.system.files.AssetData;
import common.util.Res;
import main.Opts;

public class AnimCI extends AnimU<AnimCI.AnimCILoader> {

	public static interface AnimLoader {
		public VImg getEdi();

		public ImgCut getIC();

		public MaAnim[] getMA();

		public MaModel getMM();

		public String getName();

		public FakeImage getNum();

		public int getStatus();

		public VImg getUni();
	}

	protected static class AnimCILoader implements AnimU.ImageLoader {

		private final AnimCI.AnimLoader loader;
		private FakeImage num;
		private Optional<VImg> edi;
		private VImg uni;

		private AnimCILoader(AnimCI.AnimLoader al) {
			loader = al;
		}

		@Override
		public VImg getEdi() {
			if (edi != null)
				return edi.orElse(null);
			edi = Optional.ofNullable(loader.getEdi());
			if (edi.isPresent())
				edi.get().mark(Marker.EDI);
			return edi.orElse(null);
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

		public String getName() {
			return loader.getName();
		}

		@Override
		public FakeImage getNum() {
			if (num != null)
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
			edi = Optional.of(vedi);
			vedi.mark(Marker.EDI);
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

	public String name = "";

	public AnimCI(AnimCI.AnimLoader acl) {
		super(new AnimCILoader(acl));
		name = loader.getName();
	}

	public AnimCI(InStream is) {
		this(CommonStatic.def.loadAnim(is));
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
			Opts.loadErr("Error in loading custom animation: " + name);
			e.printStackTrace();
			CommonStatic.def.exit(false);
		}
		validate();
	}

	@Override
	public String toString() {
		return name;
	}

	public OutStream write() {
		check();
		OutStream osi = OutStream.getIns();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			FakeImage.write(getNum(), "PNG", baos);
		} catch (IOException e1) {
			e1.printStackTrace();
			osi.terminate();
			return osi;
		}
		osi.writeBytesI(baos.toByteArray());
		try {
			baos = new ByteArrayOutputStream();
			imgcut.write(new PrintStream(baos, true, "UTF-8"));
			osi.writeBytesI(baos.toByteArray());
			baos = new ByteArrayOutputStream();
			mamodel.write(new PrintStream(baos, true, "UTF-8"));
			osi.writeBytesI(baos.toByteArray());
			osi.writeInt(anims.length);
			for (MaAnim ani : anims) {
				baos = new ByteArrayOutputStream();
				ani.write(new PrintStream(baos, true, "UTF-8"));
				osi.writeBytesI(baos.toByteArray());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (getEdi() != null && getEdi().getImg() != null) {
			baos = new ByteArrayOutputStream();
			try {
				FakeImage.write(getEdi().getImg(), "PNG", baos);
			} catch (IOException e1) {
				e1.printStackTrace();
				osi.terminate();
				return osi;
			}
			osi.writeBytesI(baos.toByteArray());
		}
		if (getUni() != null && getUni().getImg() != null) {
			baos = new ByteArrayOutputStream();
			try {
				FakeImage.write(getUni().getImg(), "PNG", baos);
			} catch (IOException e1) {
				e1.printStackTrace();
				osi.terminate();
				return osi;
			}
			osi.writeBytesI(baos.toByteArray());
		}
		osi.terminate();
		return osi;
	}

}