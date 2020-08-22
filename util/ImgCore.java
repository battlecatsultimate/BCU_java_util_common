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

	protected static List<Integer> randSeries = new ArrayList<Integer>();

	public static void set(FakeGraphics g) {
		if (CommonStatic.getConfig().battle)
			return;
		for (int i = 0; i < 4; i++)
			g.setRenderingHint(i, CommonStatic.getConfig().ints[i]);
	}

	protected static void drawImg(FakeGraphics g, FakeImage bimg, P piv, P sc, double opa, boolean glow,
			double extend) {
		if (opa < CommonStatic.getConfig().fullOpa * 0.01 - 1e-5)
			if (!glow)
				g.setComposite(FakeGraphics.TRANS, (int) (opa * 256), 0);
			else
				g.setComposite(FakeGraphics.BLEND, (int) (opa * 256), 1);
		else if (glow)
			g.setComposite(FakeGraphics.BLEND, 256, 1);
		else
			g.setComposite(FakeGraphics.DEF, 0, 0);
		if (extend == 0)
			drawImage(g, bimg, -piv.x, -piv.y, sc.x, sc.y);
		else {
			double x = -piv.x;
			while (extend > 1) {
				drawImage(g, bimg, x, -piv.y, sc.x, sc.y);
				x += sc.x;
				extend--;
			}
			int w = (int) (bimg.getWidth() * extend);
			int h = bimg.getHeight();
			if (w > 0) {
				FakeImage par = bimg.getSubimage(0, 0, w, h);
				drawImage(g, par, x, -piv.y, sc.x * extend, sc.y);
			}
		}
		g.setComposite(FakeGraphics.DEF, 0, 0);
	}

	protected static void drawRandom(FakeGraphics g, FakeImage[] bimg, P piv, P sc, double opa, boolean glow,
			double extend) {
		if (opa < CommonStatic.getConfig().fullOpa * 0.01 - 1e-5)
			if (!glow)
				g.setComposite(FakeGraphics.TRANS, (int) (opa * 256), 0);
			else
				g.setComposite(FakeGraphics.BLEND, (int) (opa * 256), 1);
		else if (glow)
			g.setComposite(FakeGraphics.BLEND, 256, 1);
		else
			g.setComposite(FakeGraphics.DEF, 0, 0);
		if (extend == 0)
			drawImage(g, bimg[0], -piv.x, -piv.y, sc.x, sc.y);
		else {
			double x = -piv.x;
			int i = 0;
			while (extend > 1) {
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
				extend--;
				i++;
			}

			int w = (int) (bimg[0].getWidth() * extend);
			int h = bimg[0].getHeight();
			if (w > 0) {
				FakeImage par;
				par = bimg[0].getSubimage(0, 0, w, h);

				drawImage(g, par, x, -piv.y, sc.x * extend, sc.y);
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

	private static void drawImage(FakeGraphics g, FakeImage bimg, double x, double y, double w, double h) {
		int ix = (int) Math.round(x);
		int iy = (int) Math.round(y);
		int iw = (int) Math.round(w);
		int ih = (int) Math.round(h);
		g.drawImage(bimg, ix, iy, iw, ih);

	}

}
