package common.util.anim;

import common.CommonStatic;
import common.pack.Context.ErrType;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeImage;
import common.system.fake.FakeTransform;
import common.util.ImgCore;

public class EPart extends ImgCore implements Comparable<EPart> {

	private final String name;
	private final MaModel model;
	private final AnimI<?, ?> a;
	private final int[] args;
	private final EPart[] ent;
	private final int ind;
	private EPart fa, para;
	private int id, img, gsca;
	private P pos = new P(0, 0), piv = new P(0, 0), sca = new P(0, 0);
	private int z, angle, opacity, glow, extendX, extendY, extType; // extType - 0 : Slow, 1 : Curse
	private int hf, vf;
	protected EAnimI ea;

	public int par;// temp

	protected EPart(MaModel mm, AnimI<?, ?> aa, int[] part, String str, int i, EPart[] ents) {
		model = mm;
		a = aa;
		args = part;
		ent = ents;
		name = str;
		ind = i;
		setValue();
	}

	public void alter(int m, int v) {
		if (m == 0)
			if (v < ent.length)
				fa = ent[par = v];
			else
				fa = ent[par = 0];
		else if (m == 1)
			id = v;
		else if (m == 2) {
			if (extType == 1 && img != v)
				for (int i = 0; i < randSeries.size(); i++) {
					int r = randSeries.get(i);

					r += 1;
					r = r > 3 ? 0 : r;

					randSeries.set(i, r);
				}

			img = v;
		} else if (m == 3)
			z = v * ent.length + ind;
		else if (m == 4)
			pos.x = args[4] + v;
		else if (m == 5)
			pos.y = args[5] + v;
		else if (m == 6)
			piv.x = args[6] + v;
		else if (m == 7)
			piv.y = args[7] + v;
		else if (m == 8)
			gsca = v;
		else if (m == 9)
			sca.x = 1.0 * args[8] * v / model.ints[0];
		else if (m == 10)
			sca.y = 1.0 * args[9] * v / model.ints[0];
		else if (m == 11)
			angle = args[10] + v;
		else if (m == 12)
			opacity = v * args[11] / model.ints[2];
		else if (m == 13)
			hf = v == 0 ? 1 : -1;
		else if (m == 14)
			vf = v == 0 ? 1 : -1;
		else if (m == 50) {
			extendX = v;
			extType = 0;
		} else if (m == 51) {
			extendX = v;
			extType = 1;
		} else if (m == 52) {
			extendY = v;
			extType = 0;
		} else
			CommonStatic.ctx.printErr(ErrType.NEW, "unhandled modification " + m);

	}

	@Override
	public int compareTo(EPart o) {
		return Integer.compare(z, o.z);
	}

	public int getVal(int m) {
		if (m == 0)
			return par;
		else if (m == 1)
			return id;
		else if (m == 2)
			return img;
		else if (m == 3)
			return z;
		else if (m == 4)
			return (int) pos.x;
		else if (m == 5)
			return (int) pos.y;
		else if (m == 6)
			return (int) piv.x;
		else if (m == 7)
			return (int) piv.y;
		else if (m == 8)
			return gsca;
		else if (m == 9)
			return (int) sca.x;
		else if (m == 10)
			return (int) sca.y;
		else if (m == 11)
			return angle;
		else if (m == 12)
			return opacity;
		else if (m == 13)
			return hf;
		else if (m == 14)
			return vf;
		else if (m == 50)
			return extendX;
		else if (m == 51)
			return extendY;
		else
			System.out.println("EPart modification can be: " + m);
		return -1;
	}

	@Override
	public String toString() {
		return name;
	}

	public EPart getFa() {
		return fa;
	}

	public MaModel getModel() {
		return model;
	}

	public EPart[] getParts() {
		return ent;
	}

	public int getInd() {
		return ind;
	}

	public void drawPart(FakeGraphics g, P base) {
		if (img < 0 || id < 0 || opa() < CommonStatic.getConfig().deadOpa * 0.01 + 1e-5 || a.parts(img) == null)
			return;
		FakeTransform at = g.getTransform();
		transform(g, base);
		FakeImage bimg = a.parts(img);
		int w = bimg.getWidth();
		int h = bimg.getHeight();
		P p0 = getSize();
		P tpiv = P.newP(piv).times(p0).times(base);
		P sc = P.newP(w, h).times(p0).times(base);
		P.delete(p0);
		if (extType == 0)
			drawImg(g, bimg, tpiv, sc, opa(), glow, 1.0 * extendX / model.ints[0], 1.0 * extendY / model.ints[0]);
		else if (extType == 1)
			drawRandom(g, new FakeImage[] { a.parts(3), a.parts(4), a.parts(5), a.parts(6) }, tpiv, sc, opa(),
					glow == 1, 1.0 * extendX / model.ints[0], 1.0 * extendY / model.ints[0]);
		P.delete(tpiv);
		P.delete(sc);
		g.setTransform(at);
		g.delete(at);
	}

	/**
	 * Draw part with specified opacity (Os). If part already has its own opacity (Oo), then formula is Oo * Op
	 * @param g Graphic
	 * @param base Base
	 * @param opacity Opacity, range is 0 ~ 255
	 */
	protected void drawPartWithOpacity(FakeGraphics g, P base, int opacity) {
		if (img < 0 || id < 0 || opa() < CommonStatic.getConfig().deadOpa * 0.01 + 1e-5 || a.parts(img) == null)
			return;
		FakeTransform at = g.getTransform();
		transform(g, base);
		FakeImage bimg = a.parts(img);
		int w = bimg.getWidth();
		int h = bimg.getHeight();
		P p0 = getSize();
		P tpiv = P.newP(piv).times(p0).times(base);
		P sc = P.newP(w, h).times(p0).times(base);
		P.delete(p0);
		if (extType == 0)
			drawImg(g, bimg, tpiv, sc, opa() * opacity / 255.0, glow, 1.0 * extendX / model.ints[0], 1.0 * extendY / model.ints[0]);
		else if (extType == 1)
			drawRandom(g, new FakeImage[] { a.parts(3), a.parts(4), a.parts(5), a.parts(6) }, tpiv, sc, opa(),
					glow == 1, 1.0 * extendX / model.ints[0], 1.0 * extendY / model.ints[0]);
		P.delete(tpiv);
		P.delete(sc);
		g.setTransform(at);
		g.delete(at);
	}

	protected void drawScale(FakeGraphics g, P base) {
		FakeImage bimg = a.parts(img);
		if (bimg == null)
			return;
		FakeTransform at = g.getTransform();
		transform(g, base);
		int w = bimg.getWidth();
		int h = bimg.getHeight();
		P p0 = getSize();
		P tpiv = P.newP(piv).times(p0).times(base);
		P sc = P.newP(w, h).times(p0).times(base);
		P.delete(p0);
		drawSca(g, tpiv, sc);
		P.delete(tpiv);
		P.delete(sc);
		g.setTransform(at);
		g.delete(at);
	}

	protected void setPara(EPart p) {
		if (p == null) {
			fa = para;
			para = null;
		} else {
			para = fa;
			fa = p;
		}
	}

	protected void setValue() {
		if (args[0] >= ent.length)
			args[0] = 0;
		fa = args[0] <= -1 ? null : ent[args[0]];
		id = args[1];
		img = args[2];
		z = args[3] * ent.length + ind;
		pos = pos.setTo(args[4], args[5]);
		piv = piv.setTo(args[6], args[7]);
		sca = sca.setTo(args[8], args[9]);
		angle = args[10];
		opacity = args[11];
		glow = args[12];
		extendX = args[13];
		gsca = model.ints[0];
		hf = vf = 1;
		extendX = extendY = 0;
	}

	private P getSize() {
		double mi = 1.0 / model.ints[0];
		if (fa == null)
			return P.newP(sca).times(gsca * mi * mi);
		return fa.getSize().times(sca).times(gsca * mi * mi);
	}

	private P getBaseSize(boolean parent) {
		if(model.confs.length > 0) {
			double mi = 1.0 / model.ints[0];

			if(parent) {
				if(fa != null) {
					return fa.getBaseSize(true).times(Math.signum(model.parts[ind][8]), Math.signum(model.parts[ind][9]));
				} else {
					return P.newP(Math.signum(model.parts[ind][8]), Math.signum(model.parts[ind][9]));
				}
			} else {
				if(model.confs[0][0] == -1) {
					return P.newP(model.parts[0][8] * mi, model.parts[0][9] * mi);
				} else {
					if (model.confs[0][0] == ind) {
						return P.newP(model.parts[model.confs[0][0]][8] * mi, model.parts[model.confs[0][0]][9] * mi);
					} else {
						return ent[model.confs[0][0]].getBaseSize(true).times(model.parts[model.confs[0][0]][8] * mi, model.parts[model.confs[0][0]][9] * mi);
					}
				}
			}
		} else {
			return P.newP(1.0, 1.0);
		}
	}

	public double opa() {
		if (opacity == 0)
			return 0;
		if (fa != null)
			return fa.opa() * opacity / model.ints[2];
		return 1.0 * opacity / model.ints[2];
	}

	private void transform(FakeGraphics g, P sizer) {
		P siz = sizer;
		if (fa != null) {
			fa.transform(g, sizer);
			siz = fa.getSize().times(sizer);
		}

		P tpos = P.newP(pos).times(siz);

		if (ent[0] != this) {
			g.translate(tpos.x, tpos.y);
			g.scale(hf, vf);
		} else {
			if (model.confs.length > 0) {
				int[] data = model.confs[0];
				P p0 = getBaseSize(false);

				P shi = P.newP(data[2], data[3]).times(p0);
				P.delete(p0);
				P p3 = shi.times(sizer);
				g.translate(-p3.x, -p3.y);

				P.delete(shi);
			}
			P p0 = getSize();
			P p = P.newP(piv).times(p0).times(sizer);
			P.delete(p0);
			g.translate(p.x, p.y);

			P.delete(p);
		}
		if (angle != 0)
			g.rotate(Math.PI * 2 * angle / model.ints[1]);

		P.delete(tpos);

		if (fa != null)
			P.delete(siz);
	}

}
