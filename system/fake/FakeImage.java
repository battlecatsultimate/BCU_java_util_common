package common.system.fake;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

import common.system.files.FileData;
import common.system.files.VFile;
import common.util.Data;

public interface FakeImage {

	enum Marker {
		BG, EDI, UNI, RECOLOR, RECOLORED
	}

	public static FakeImage read(byte[] bs) {
		return read((Supplier<InputStream>) () -> new ByteArrayInputStream(bs));
	}

	public static FakeImage read(File f) {
		return Data.err(() -> ImageBuilder.builder.build(f));
	}

	public static FakeImage read(FileData fd) {
		return read((Supplier<InputStream>) fd::getStream);
	}

	public static FakeImage read(Supplier<InputStream> sup) {
		return Data.err(() -> ImageBuilder.builder.build(sup));
	}

	public static FakeImage read(VFile vf) {
		return read(vf.getData());
	}

	static boolean write(FakeImage img, String str, Object o) throws IOException {
		return ImageBuilder.builder.write(img, str, o);
	}

	Object bimg();

	int getHeight();

	int getRGB(int i, int j);

	FakeImage getSubimage(int i, int j, int k, int l);

	int getWidth();

	Object gl();

	boolean isValid();

	default void mark(Marker m) {
	}

	void setRGB(int i, int j, int p);

	void unload();
}
