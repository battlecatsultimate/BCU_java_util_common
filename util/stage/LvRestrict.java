package common.util.stage;

import common.battle.LineUp;
import common.io.assets.Admin.StaticPermitted;
import common.io.json.JsonClass;
import common.io.json.JsonClass.JCIdentifier;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.pack.IndexContainer.IndexCont;
import common.pack.IndexContainer.Indexable;
import common.pack.PackData;
import common.pack.PackData.UserPack;
import common.util.Data;
import common.util.unit.Form;
import common.util.unit.Level;

import java.util.ArrayList;
import java.util.TreeMap;

@IndexCont(PackData.class)
@JsonClass(noTag = NoTag.LOAD)
@JsonClass.JCGeneric(Identifier.class)
public class LvRestrict extends Data implements Indexable<PackData, LvRestrict> {

	@StaticPermitted
	public static final int[] MAX = new int[] { 120, 10, 10, 10, 10, 10 };

	@JsonField(generic = { CharaGroup.class, int[].class }, alias = Identifier.class)
	public final TreeMap<CharaGroup, int[]> res = new TreeMap<>();
	public int[][] rares = new int[RARITY_TOT][6];
	public int[] all = new int[6];
	@JCIdentifier
	public Identifier<LvRestrict> id;
	public String name = "";

	@JsonClass.JCConstructor
	public LvRestrict() {

	}

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
					ArrayList<Integer> mlv = valid(f).getLvs();
					ArrayList<Integer> flv = lu.map.get(f.unit.id).getLvs();
					for (int i = 0; i < Math.min(mlv.size(), flv.size()); i++)
						if (mlv.get(i) < flv.get(i))
							return false;
				}
		return true;
	}

	@Override
	public String toString() {
		return id + "-" + name;
	}

	public boolean used() {
		PackData p = getCont();

		if(p instanceof UserPack) {
			for (StageMap sm : ((UserPack) p).mc.maps)
				for (Stage st : sm.list)
					if (st.lim != null && st.lim.lvr == this)
						return true;
		} else
			return p instanceof PackData.DefPack;

		return false;
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
			return new Level(f.regulateLv(null, Level.LvList(lv)));
		for (int i = 0; i < 6; i++)
			lv[i] = Math.min(lv[i], rares[f.unit.rarity][i]);
		for (int i = 0; i < 6; i++)
			lv[i] = Math.min(lv[i], all[i]);
		return new Level(f.regulateLv(null, Level.LvList(lv)));
	}

	public void validate(LineUp lu) {
		for (Form[] fs : lu.fs)
			for (Form f : fs)
				if (f != null)
					lu.map.put(f.unit.id, valid(f));
		lu.renew();
	}

}
