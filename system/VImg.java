package common.system;

import common.system.fake.FakeImage;
import common.system.fake.FakeImage.Marker;
import common.system.files.VFile;
import common.util.ImgCore;
import common.util.anim.ImgCut;

public class VImg extends ImgCore {

	private final VFile file;

	public String name = "";

	public FakeImage bimg = null;

	private boolean loaded = false;
	private ImgCut ic;
	private Marker marker;

	public VImg(Object o) {
		if (o instanceof String)
			file = VFile.getFile((String) o);
		else if (o instanceof VFile)
			file = (VFile) o;
		else
			file = null;
		if (file == null)
			bimg = err(() -> FakeImage.read(o));
		loaded = bimg != null;
	}

	public synchronized void check() {
		if (!loaded)
			load();
	}

	public FakeImage getImg() {
		check();
		return bimg;
	}

	public VImg mark(Marker string) {
		marker = string;
		if (bimg != null)
			bimg.mark(string);
		return this;
	}

	public void setCut(ImgCut cut) {
		ic = cut;
	}

	public void setImg(Object img) {
		bimg = err(() -> FakeImage.read(img));
		if (ic != null)
			bimg = ic.cut(bimg)[0];
		loaded = true;
	}

	@Override
	public String toString() {
		return file == null ? name.length() == 0 ? "img" : name : file.getName();
	}

	public void unload() {
		// FIXME Auto-generated method stub

	}

	private void load() {
		loaded = true;
		if (file == null)
			return;
		bimg = file.getData().getImg();
		if (bimg == null)
			return;
		if (marker != null)
			bimg.mark(marker);
		if (ic != null)
			bimg = ic.cut(bimg)[0];
		try {
			bimg.getWidth();
		} catch (Exception e) {
			bimg = null;
		}
	}

}
