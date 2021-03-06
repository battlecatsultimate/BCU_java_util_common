package common.util.pack;

import common.CommonStatic;
import common.CommonStatic.BCAuxAssets;
import common.io.json.JsonClass;
import common.io.json.JsonDecoder.OnInjected;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.pack.IndexContainer.IndexCont;
import common.pack.IndexContainer.Indexable;
import common.pack.PackData;
import common.pack.Source;
import common.pack.UserProfile;
import common.system.P;
import common.system.VImg;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeImage;
import common.system.fake.FakeImage.Marker;
import common.system.files.VFile;
import common.util.anim.*;

import java.util.Queue;

@IndexCont(PackData.class)
@JsonClass.JCGeneric(Identifier.class)
@JsonClass
public class Background extends AnimI<Background, Background.BGWvType> implements Indexable<PackData, Background> {

	public enum BGWvType implements AnimI.AnimType<Background, BGWvType> {
		ENEMY, UNIT
	}

	public static final int BG = 0, TOP = 20, shift = 65; // in pix

	public static void read() {
		BCAuxAssets aux = CommonStatic.getBCAssets();
		String path = "./org/battle/bg/";
		for (VFile vf : VFile.get("./org/battle/bg").list()) {
			String name = vf.getName();
			if (name.length() != 11 || !name.endsWith(".imgcut"))
				continue;
			aux.iclist.add(ImgCut.newIns(path + name));
		}
		Queue<String> qs = VFile.readLine("./org/battle/bg/bg.csv");
		qs.poll();
		for (VFile vf : VFile.get("./org/img/bg/").list())
			if (vf.getName().length() == 9) {
				int[] ints = new int[15];
				try {
					String q = qs.poll();

					if(q == null)
						continue;

					String[] strs = q.split(",");
					for (int i = 0; i < 15; i++)
						ints[i] = Integer.parseInt(strs[i]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				new Background(new VImg(vf), ints);
			}
	}

	@JsonClass.JCIdentifier
	@JsonField
	public Identifier<Background> id;
	public VImg img;
	@JsonField
	public int[][] cs = new int[4][3];

	public int ic;
	@JsonField
	public boolean top;

	public FakeImage[] parts = null;

	@JsonClass.JCConstructor
	public Background() {
		ic = 1;
		top = true;
	}

	public Background(Identifier<Background> id, VImg vimg) {
		this.id = id;
		img = vimg;
		ic = 1;
		top = true;
	}

	private Background(VImg vimg, int[] ints) {
		int id = UserProfile.getBCData().bgs.size();
		this.id = Identifier.parseInt(id, Background.class);
		img = vimg;
		top = ints[14] == 1 || ints[13] == 8;
		ic = ints[13] == 8 ? 1 : ints[13];
		for (int i = 0; i < 4; i++)
			cs[i] = new int[] { ints[i * 3 + 1], ints[i * 3 + 2], ints[i * 3 + 3] };
		UserProfile.getBCData().bgs.add(this);
	}

	@Override
	public void check() {
		if (parts != null)
			return;
		load();

	}

	public Background copy(Identifier<Background> id) {
		Background bg = new Background(id, new VImg(img.getImg()));
		System.arraycopy(cs, 0, bg.cs, 0, 4);
		bg.top = top;
		bg.ic = ic;
		return bg;
	}

	public void draw(FakeGraphics g, P rect, final int pos, final int h, final double siz, final int groundHeight) {
		check();
		final int off = (int) (pos - shift * siz);
		int fw = (int) (parts[BG].getWidth() * siz);
		int fh = (int) (parts[BG].getHeight() * siz);

		g.gradRect(0, h, (int) rect.x, groundHeight, 0, h, cs[2], 0, h + groundHeight, cs[3]);

		if (h > fh) {
			int y = h - fh * 2;

			if (top && parts.length > TOP) {
				int tw = (int) (parts[TOP].getWidth() * siz);
				int th = (int) (parts[TOP].getHeight() * siz);

				y += fh - th;

				for (int x = off; x < rect.x; x += tw)
					if (x + tw > 0)
						g.drawImage(parts[TOP], x, y, tw, th);

				if(y > 0)
					g.gradRect(0, 0, (int) rect.x, y, 0, 0, cs[0], 0, y, cs[1]);
			} else {
				g.gradRect(0, 0, (int) rect.x, fh + y, 0, y, cs[0], 0, y + fh, cs[1]);
			}
		}

		for (int x = off; x < rect.x; x += fw)
			if (x + fw > 0) {

				g.drawImage(parts[BG], x, h - fh, fw, fh);
			}
	}

	@Override
	public EAnimD<EffAnim.DefEff> getEAnim(BGWvType t) {
		if (t == BGWvType.ENEMY)
			return effas().A_E_WAVE.getEAnim(EffAnim.DefEff.DEF);
		else if (t == BGWvType.UNIT)
			return effas().A_WAVE.getEAnim(EffAnim.DefEff.DEF);
		else
			return null;
	}

	@Override
	public Identifier<Background> getID() {
		return id;
	}

	@Override
	public void load() {
		img.mark(Marker.BG);
		BCAuxAssets aux = CommonStatic.getBCAssets();
		parts = aux.iclist.get(ic).cut(img.getImg());
	}

	@Override
	public String[] names() {
		return new String[] { toString(), "enemy wave", "unit wave" };
	}

	@SuppressWarnings("unused")
	@OnInjected
	public void onInjected() {
		img = ((PackData.UserPack) getCont()).source.readImage(Source.BG, id.id);
	}

	@Override
	public FakeImage parts(int i) {
		return parts[i];
	}

	@Override
	public String toString() {
		return id.toString();
	}

	@Override
	public BGWvType[] types() {
		return BGWvType.values();
	}

}
