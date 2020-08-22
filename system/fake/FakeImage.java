package common.system.fake;

import java.io.IOException;

public interface FakeImage {

	enum Marker {
		BG, EDI, UNI, RECOLOR, RECOLORED
	}

	static FakeImage read(Object o) throws IOException {
		return ImageBuilder.builder.build(o);
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
