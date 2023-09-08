package common.system.fake;

public interface FakeGraphics {

	int RED = 0, YELLOW = 1, BLACK = 2, MAGENTA = 3, BLUE = 4, CYAN = 5, WHITE = 6;
	int DEF = 0, TRANS = 1, BLEND = 2, GRAY = 3;

	void colRect(int x, int y, int w, int h, int r, int g, int b, int a);

	default void delete(FakeTransform at) {

	}

	void drawImage(FakeImage bimg, float x, float y);

	void drawImage(FakeImage bimg, float x, float y, float d, float e);

	void drawLine(int i, int j, int x, int y);

	void drawOval(int i, int j, int k, int l);

	void drawRect(int x, int y, int x2, int y2);

	void fillOval(int i, int j, int k, int l);

	void fillRect(int x, int y, int w, int h);

	FakeTransform getTransform();

	void gradRect(int x, int y, int w, int h, int a, int b, int[] c, int d, int e, int[] f);

	void gradRectAlpha(int x, int y, int w, int h, int a, int b, int al, int[] c, int d, int e, int al2, int[] f);

	void rotate(float d);

	void scale(int hf, int vf);

	void setColor(int c);

	void setColor(int r, int g, int b);

	void setComposite(int mode, int p0, int p1);

	void setRenderingHint(int key, int object);

	void setTransform(FakeTransform at);

	void translate(float x, float y);

}
