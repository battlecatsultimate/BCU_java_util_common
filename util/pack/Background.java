package common.util.pack;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import common.pack.PackData.Identifier;
import common.pack.PackData.Indexable;
import common.pack.UserProfile;
import common.system.P;
import common.system.VImg;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeImage;
import common.system.fake.FakeImage.Marker;
import common.system.files.VFile;
import common.util.anim.AnimI;
import common.util.anim.EAnimD;
import common.util.anim.ImgCut;
import common.util.anim.MaAnim;
import common.util.anim.MaModel;

public class Background extends AnimI implements Indexable<Background> {

	public static MaModel ewavm, uwavm; // FIXME static fields
	public static MaAnim ewava, uwava;
	public static WaveAnim defu, defe;
	public static final List<ImgCut> iclist = new ArrayList<>();

	public static final int BG = 0, TOP = 20, shift = 65; // in pix

	public static void read() {
		String path = "./org/battle/bg/";
		for (VFile<?> vf : VFile.get("./org/battle/bg").list()) {
			String name = vf.getName();
			if (name.length() != 11 || !name.endsWith(".imgcut"))
				continue;
			iclist.add(ImgCut.newIns(path + name));
		}
		uwavm = MaModel.newIns("./org/battle/bg/bg_01.mamodel");
		ewavm = MaModel.newIns("./org/battle/bg/bg_02.mamodel");
		uwava = MaAnim.newIns("./org/battle/bg/bg_01.maanim");
		ewava = MaAnim.newIns("./org/battle/bg/bg_02.maanim");
		Queue<String> qs = VFile.readLine("./org/battle/bg/bg.csv");
		qs.poll();
		for (VFile<?> vf : VFile.get("./org/img/bg/").list())
			if (vf.getName().length() == 9) {
				int[] ints = new int[15];
				try {
					String[] strs = qs.poll().split(",");
					for (int i = 0; i < 15; i++)
						ints[i] = Integer.parseInt(strs[i]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				new Background(new VImg(vf), ints);
			}
	}

	public final Identifier<Background> id;
	public final VImg img;
	public final int[][] cs = new int[4][3];
	private final WaveAnim uwav, ewav;

	public int ic;
	public boolean top;

	public FakeImage[] parts = null;

	public Background(Identifier<Background> id, VImg vimg) {
		this.id = id;
		img = vimg;
		ic = 1;
		top = true;
		uwav = defu;
		ewav = defe;
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
		if (id <= 107) {
			uwav = new WaveAnim(this, uwavm, uwava);
			ewav = new WaveAnim(this, ewavm, ewava);
		} else {
			uwav = defu;
			ewav = defe;
		}
		if (id == 0) {
			defu = uwav;
			defe = ewav;
		}
	}

	@Override
	public void check() {
		if (parts != null)
			return;
		load();

	}

	public Background copy(Identifier<Background> id) {
		Background bg = new Background(id, new VImg(img.getImg()));
		for (int i = 0; i < 4; i++)
			bg.cs[i] = cs[i];
		bg.top = top;
		bg.ic = ic;
		return bg;
	}

	public void draw(FakeGraphics g, P rect, final int pos, final int h, final double siz) {
		check();
		final int off = (int) (pos - shift * siz);
		final int fw = (int) (768 * siz);
		final int fh = (int) (510 * siz);

		g.gradRect(0, h, (int) rect.x, (int) rect.y - h, 0, h, cs[2], 0, h + fh, cs[3]);

		if (h > fh) {
			int y = h - fh * 2;
			if (top && parts.length > TOP) {
				for (int x = off; x < rect.x; x += fw)
					if (x + fw > 0)
						g.drawImage(parts[TOP], x, y, fw, fh);
			} else {
				g.gradRect(0, 0, (int) rect.x, fh + y, 0, y, cs[0], 0, y + fh, cs[1]);
			}
		}
		for (int x = off; x < rect.x; x += fw)
			if (x + fw > 0)
				g.drawImage(parts[BG], x, h - fh, fw, fh);
	}

	@Override
	public EAnimD getEAnim(int t) {
		if (t == 1 || t == 0)
			return ewav.getEAnim(0);
		else if (t == 2)
			return uwav.getEAnim(0);
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
		parts = iclist.get(ic).cut(img.getImg());
	}

	@Override
	public String[] names() {
		return new String[] { toString(), "enemy wave", "unit wave" };
	}

	@Override
	public FakeImage parts(int i) {
		return parts[i];
	}

	@Override
	public String toString() {
		return id.toString();
	}

}
