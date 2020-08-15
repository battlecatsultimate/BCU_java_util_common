package common.util.stage;

import java.util.TreeMap;

import common.battle.LineUp;
import common.io.InStream;
import common.io.assets.Admin.StaticPermitted;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.io.json.JsonField.GenType;
import common.pack.IndexContainer.IndexCont;
import common.pack.IndexContainer.Indexable;
import common.pack.PackData;
import common.pack.PackData.Identifier;
import common.pack.PackData.UserPack;
import common.pack.VerFixer.VerFixerException;
import common.util.Data;
import common.util.unit.Form;
import common.util.unit.Level;

@IndexCont(PackData.class)
@JsonClass
public class LvRestrict extends Data implements Indexable<PackData, LvRestrict> {

	public static class PackLR extends LvRestrict {

		private final UserPack pack;

		@Deprecated
		public PackLR(UserPack mc, InStream is) throws VerFixerException {
			this.pack = mc;
			int ver = getVer(is.nextString());
			if (ver != 308)
				throw new VerFixerException("LvRestrict requires 308, got " + ver);
			name = is.nextString();
			id = Identifier.parseInt(is.nextInt(), LvRestrict.class);
			int[] tb = is.nextIntsB();
			for (int i = 0; i < tb.length; i++)
				all[i] = tb[i];
			int[][] tbb = is.nextIntsBB();
			for (int i = 0; i < tbb.length; i++)
				for (int j = 0; j < tbb[i].length; j++)
					rares[i][j] = tbb[i][j];
			int n = is.nextInt();
			for (int i = 0; i < n; i++) {
				int cg = is.nextInt();
				int[] vals = new int[6];
				tb = is.nextIntsB();
				for (int j = 0; j < tb.length; j++)
					vals[j] = tb[j];
				CharaGroup cgs = mc.groups.get(cg);
				if (cgs != null)
					res.put(cgs, vals);
			}
		}

		public boolean used() {
			for (StageMap sm : pack.mc.maps)
				for (Stage st : sm.list)
					if (st.lim != null && st.lim.lvr == this)
						return true;
			return false;
		}

	}

	@StaticPermitted
	public static final int[] MAX = new int[] { 120, 10, 10, 10, 10, 10 };

	@JsonField(generic = { CharaGroup.class, int[].class }, alias = int.class)
	public final TreeMap<CharaGroup, int[]> res = new TreeMap<>();
	@JsonField(gen = GenType.FILL)
	public int[][] rares = new int[RARITY_TOT][6];
	@JsonField
	public int[] all = new int[6];
	@JsonField
	public Identifier<LvRestrict> id;
	@JsonField
	public String name = "";

	public LvRestrict(Identifier<LvRestrict> ID) {
		all = MAX.clone();
		for (int i = 0; i < RARITY_TOT; i++)
			rares[i] = MAX.clone();
		id = ID;
	}

	public LvRestrict(Identifier<LvRestrict> ID, LvRestrict lvr) {
		id = ID;
		all = lvr.all.clone();
		for (int i = 0; i < RARITY_TOT; i++)
			rares[i] = lvr.rares[i].clone();
		for (CharaGroup cg : lvr.res.keySet())
			res.put(cg, lvr.res.get(cg).clone());
	}

	private LvRestrict() {
	}

	private LvRestrict(LvRestrict lvr) {
		for (CharaGroup cg : lvr.res.keySet())
			res.put(cg, lvr.res.get(cg).clone());
	}

	public LvRestrict combine(LvRestrict lvr) {
		LvRestrict ans = new LvRestrict(this);
		for (int i = 0; i < 6; i++)
			ans.all[i] = Math.min(lvr.all[i], all[i]);
		for (int i = 0; i < RARITY_TOT; i++)
			for (int j = 0; j < 6; j++)
				ans.rares[i][j] = Math.min(lvr.rares[i][j], rares[i][j]);
		for (CharaGroup cg : lvr.res.keySet())
			if (res.containsKey(cg)) {
				int[] lv0 = res.get(cg);
				int[] lv1 = lvr.res.get(cg);
				int[] lv = new int[6];
				for (int i = 0; i < 6; i++)
					lv[i] = Math.min(lv0[i], lv1[i]);
				ans.res.put(cg, lv);
			} else
				ans.res.put(cg, lvr.res.get(cg).clone());
		return ans;
	}

	@Override
	public Identifier<LvRestrict> getID() {
		return id;
	}

	public boolean isValid(LineUp lu) {
		for (Form[] fs : lu.fs)
			for (Form f : fs)
				if (f != null) {
					int[] mlv = valid(f).getLvs();
					int[] flv = lu.map.get(f.unit).getLvs();
					for (int i = 0; i < 6; i++)
						if (mlv[i] < flv[i])
							return false;
				}
		return true;
	}

	@Override
	public String toString() {
		return id + "-" + name;
	}

	public Level valid(Form f) {
		int[] lv = MAX.clone();
		boolean mod = false;
		for (CharaGroup cg : res.keySet())
			if (cg.set.contains(f.unit)) {
				int[] rst = res.get(cg);
				for (int i = 0; i < 6; i++)
					lv[i] = Math.min(lv[i], rst[i]);
				mod = true;
			}
		if (mod)
			return new Level(f.regulateLv(null, lv));
		for (int i = 0; i < 6; i++)
			lv[i] = Math.min(lv[i], rares[f.unit.rarity][i]);
		for (int i = 0; i < 6; i++)
			lv[i] = Math.min(lv[i], all[i]);
		return new Level(f.regulateLv(null, lv));
	}

	public void validate(LineUp lu) {
		for (Form[] fs : lu.fs)
			for (Form f : fs)
				if (f != null)
					lu.map.put(f.unit, valid(f));
		lu.renew();
	}

}
