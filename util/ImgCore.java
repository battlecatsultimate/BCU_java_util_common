package common.util;

import common.CommonStatic;
import common.io.assets.Admin.StaticPermitted;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeImage;

import java.util.ArrayList;
import java.util.List;

public class ImgCore extends Data {

	@StaticPermitted
	public static final String[] NAME = new String[] { "opacity", "color", "accuracy", "scale" };
	@StaticPermitted
	public static final String[] VAL = new String[] { "fast", "default", "quality" };

	protected static List<Integer> randSeries = new ArrayList<>();

	public static void set(FakeGraphics g) {
		if (CommonStatic.getConfig().battle)
			return;
		for (int i = 0; i < 4; i++)
			g.setRenderingHint(i, CommonStatic.getConfig().ints[i]);
	}

	protected static void drawImg(FakeGraphics g, FakeImage bimg, P piv, P sc, float opa, int glow,
			float extendX, float extendY) {
		boolean glowSupport = (glow >= 1 && glow <= 3) || glow == -1;
		if (opa < CommonStatic.getConfig().fullOpa * 0.01 - 1e-5) {
			if (glowSupport)
				g.setComposite(FakeGraphics.BLEND, (int) (opa * 256), glow);
			else
				g.setComposite(FakeGraphics.TRANS, (int) (opa * 256), 0);
		} else {
			if (glowSupport)
				g.setComposite(FakeGraphics.BLEND, 256, glow);
			else
				g.setComposite(FakeGraphics.DEF, 0, 0);
		}
		if (extendX == 0 && extendY == 0)
			drawImage(g, bimg, -piv.x, -piv.y, sc.x, sc.y);
		else {
			float x = -piv.x;
			float y = -piv.y;

			float oldExtendY = extendY;
			float oldExtendX = extendX;

			if(extendY == 0) {
				while (extendX > 1) {
					drawImage(g, bimg, x, y, sc.x, sc.y);
					x += sc.x;
					extendX--;
				}
			} else {
				float extendXBackup = extendX;

				while(extendY > 1) {
					if(extendX == 0) {
						drawImage(g, bimg, x, y, sc.x, sc.y);
					} else {
						x = -piv.x;
						extendX = extendXBackup;

						while(extendX > 1) {
							drawImage(g, bimg, x, y, sc.x, sc.y);
							x += sc.x;
							extendX--;
						}
					}

					y += sc.y;
					extendY--;
				}
			}
			int w = bimg.getWidth();
			int h = bimg.getHeight();
			if (w > 0) {
				if(extendY == 0) {
					FakeImage parX = bimg.getSubimage(0, 0, (int) (Math.max(1, w * extendX)), h);

					drawImage(g, parX, x, y, sc.x * extendX, sc.y);
				} else {
					FakeImage parY = bimg.getSubimage(0, 0, w, (int) (Math.max(1, h * extendY)));

					if(extendX == 0) {
						drawImage(g, parY, x, y, sc.x, sc.y * extendY);
					} else {
						FakeImage parX = bimg.getSubimage(0, 0, (int) (Math.max(1, w * extendX)), h);
						FakeImage parXY = bimg.getSubimage(0, 0, parX.getWidth(), parY.getHeight());

						y = -piv.y;

						while(oldExtendY > 1) {
							drawImage(g, parX, x, y, sc.x * extendX, sc.y);

							y += sc.y;
							oldExtendY--;
						}

						x = -piv.x;

						while(oldExtendX > 1) {
							drawImage(g, parY, x, y, sc.x, sc.y * extendY);

							x += sc.x;
							oldExtendX--;
						}

						drawImage(g, parXY, x, y, sc.x * extendX, sc.y * extendY);
					}
				}
			}
		}
		g.setComposite(FakeGraphics.DEF, 0, 0);
	}

	protected static void drawRandom(FakeGraphics g, FakeImage[] bimg, P piv, P sc, float opa, boolean glow, float extendX, float extendY) {
		if (opa < CommonStatic.getConfig().fullOpa * 0.01 - 1e-5)
			if (!glow)
				g.setComposite(FakeGraphics.TRANS, (int) (opa * 256), 0);
			else
				g.setComposite(FakeGraphics.BLEND, (int) (opa * 256), 1);
		else if (glow)
			g.setComposite(FakeGraphics.BLEND, 256, 1);
		else
			g.setComposite(FakeGraphics.DEF, 0, 0);
		if (extendX == 0)
			drawImage(g, bimg[0], -piv.x, -piv.y, sc.x, sc.y);
		else {
			float x = -piv.x;
			int i = 0;
			while (extendX > 1) {
				int data;

				if (i >= randSeries.size()) {
					data = (int) (Math.random() * 3);

					randSeries.add(data);
				} else {
					data = randSeries.get(i);
				}

				FakeImage ranImage = bimg[data];
				drawImage(g, ranImage, x, -piv.y, sc.x, sc.y);
				x += sc.x;
				extendX--;
				i++;
			}

			int w = (int) (bimg[0].getWidth() * extendX);
			int h = bimg[0].getHeight();
			if (w > 0) {
				FakeImage par;
				par = bimg[0].getSubimage(0, 0, w, h);

				drawImage(g, par, x, -piv.y, sc.x * extendX, sc.y);
			}
		}
		g.setComposite(FakeGraphics.DEF, 0, 0);
	}

	protected static void drawSca(FakeGraphics g, P piv, P sc) {
		g.setColor(FakeGraphics.RED);
		g.fillOval(-10, -10, 20, 20);
		g.drawOval(-40, -40, 80, 80);
		int x = (int) -piv.x;
		int y = (int) -piv.y;
		if (sc.x < 0)
			x += sc.x;
		if (sc.y < 0)
			y += sc.y;
		int sx = (int) Math.abs(sc.x);
		int sy = (int) Math.abs(sc.y);
		g.drawRect(x, y, sx, sy);
		g.setColor(FakeGraphics.YELLOW);
		g.drawRect(x - 40, y - 40, sx + 80, sy + 80);
	}

	private static void drawImage(FakeGraphics g, FakeImage bimg, float x, float y, float w, float h) {
		int ix = (int) Math.round(x);
		int iy = (int) Math.round(y);
		int iw = (int) Math.round(w);
		int ih = (int) Math.round(h);
		g.drawImage(bimg, ix, iy, iw, ih);

	}

}
