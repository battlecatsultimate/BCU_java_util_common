package common.util.stage;

import common.battle.LineUp;
import common.io.assets.Admin.StaticPermitted;
import common.io.json.JsonClass;
import common.io.json.JsonClass.JCIdentifier;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonDecoder;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.pack.IndexContainer.IndexCont;
import common.pack.IndexContainer.Indexable;
import common.pack.PackData;
import common.pack.PackData.UserPack;
import common.util.Data;
import common.util.unit.Form;
import common.util.unit.Level;

import java.util.TreeMap;

@IndexCont(PackData.class)
@JsonClass(noTag = NoTag.LOAD)
@JsonClass.JCGeneric(Identifier.class)
public class LvRestrict extends Data implements Indexable<PackData, LvRestrict> {

	@JsonClass(noTag = NoTag.LOAD)
	public static class GroupRestrict {
		public int[] lv;
		public int orb;

		@JsonClass.JCConstructor
		public GroupRestrict() {

		}

		public GroupRestrict(GroupRestrict gr) {
			lv = gr.lv;
			orb = gr.orb;
		}

		public GroupRestrict(int[] maxlv, int maxorb) {
			lv = maxlv;
			this.orb = maxorb;
		}
	}

	@StaticPermitted
	public static final int[] MAX = new int[] { 50, 90, 10, 10, 10, 10, 10 };

	@JsonField(io = JsonField.IOType.R, generic = { CharaGroup.class, int[].class }, alias = Identifier.class)
	public final TreeMap<CharaGroup, int[]> res = new TreeMap<>();

	@JsonField(generic = { CharaGroup.class, GroupRestrict.class }, alias = Identifier.class)
	public final TreeMap<CharaGroup, GroupRestrict> groups = new TreeMap<>();
	public int[][] rares = new int[RARITY_TOT][7];
	public int[] all = new int[7];
	public int[] orb = new int[] { -1, -1, -1, -1, -1, -1 };
	public int allOrb = -1;
	@JCIdentifier
	public Identifier<LvRestrict> id;
	public String name = "new level restrict";

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
		allOrb = lvr.allOrb;
		for (int i = 0; i < RARITY_TOT; i++) {
			rares[i] = lvr.rares[i].clone();
			orb[i] = lvr.orb[i];
		}
		for (CharaGroup cg : lvr.groups.keySet())
			groups.put(cg, new GroupRestrict(lvr.groups.get(cg)));
	}

	private LvRestrict(LvRestrict lvr) {
		for (CharaGroup cg : lvr.groups.keySet())
			groups.put(cg, new GroupRestrict(lvr.groups.get(cg)));
	}

	public LvRestrict combine(LvRestrict lvr) {
		LvRestrict ans = new LvRestrict(this);
		ans.allOrb = allOrb == -1 ? lvr.allOrb : lvr.allOrb == -1 ? allOrb : Math.min(lvr.allOrb, allOrb);
		for (int i = 0; i < ans.all.length; i++) {
			ans.all[i] = Math.min(lvr.all[i], all[i]);
		}
		int[] orbc = new int[6];
		for (int i = 0; i < RARITY_TOT; i++)
			for (int j = 0; j < ans.rares[i].length; j++) {
				ans.rares[i][j] = Math.min(lvr.rares[i][j], rares[i][j]);
				orbc[i] = orb[i] == -1 ? lvr.orb[i] : lvr.orb[i] == -1 ? orb[i] : Math.min(lvr.orb[i], orb[i]);
			}
		ans.orb = orbc;
		for (CharaGroup cg : lvr.groups.keySet())
			if (groups.containsKey(cg)) {
				GroupRestrict lv0 = groups.get(cg);
				GroupRestrict lv1 = lvr.groups.get(cg);
				int[] lv = new int[7];
				int o = lv0.orb == -1 ? lv1.orb : lv1.orb == -1 ? lv0.orb : Math.min(lv0.orb, lv1.orb);
				for (int i = 0; i < 7; i++) {
					lv[i] = Math.min(lv0.lv[i], lv1.lv[i]);
				}
				ans.groups.put(cg, new GroupRestrict(lv, o));
			} else
				ans.groups.put(cg, new GroupRestrict(lvr.groups.get(cg)));
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
					Level maxLv = valid(f);
					Level curLv = lu.map.get(f.unit.id);

					if (maxLv.getLv() < curLv.getLv() || maxLv.getPlusLv() < curLv.getPlusLv())
						return false;

					int[] mt = maxLv.getTalents();
					int[] ft = curLv.getTalents();

					for (int i = 0; i < Math.min(mt.length, ft.length); i++)
						if (mt[i] < ft[i])
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
		for (CharaGroup cg : groups.keySet())
			if (cg.set.contains(f.unit)) {
				GroupRestrict rst = groups.get(cg);
				for (int i = 0; i < 6; i++) {
					lv[i] = Math.min(lv[i], rst.lv[i]);
				}
				mod = true;
			}
		if (mod)
			return f.regulateLv(null, Level.lvList(f.unit, lv, null));
		for (int i = 0; i < 7; i++)
			lv[i] = Math.min(lv[i], rares[f.unit.rarity][i]);
		for (int i = 0; i < 7; i++)
			lv[i] = Math.min(lv[i], all[i]);
		return f.regulateLv(null, Level.lvList(f.unit, lv, null));
	}

	public void validate(LineUp lu) {
		for (Form[] fs : lu.fs)
			for (Form f : fs)
				if (f != null)
					lu.map.put(f.unit.id, valid(f));
		lu.renew();
	}

	@JsonDecoder.OnInjected
	public void onInjected() {
		res.replaceAll((k, v) -> {
			if(v == null)
				return MAX.clone();

			if(v.length == 6) {
				int[] l = new int[7];

				l[0] = v[0];

				System.arraycopy(v, 1, l, 2, l.length - 2);

				return l;
			}

			return v;
		});

		for(int i = 0; i < rares.length; i++) {
			if (rares[i].length == 6) {
				int[] l = new int[7];

				l[0] = rares[i][0];

				System.arraycopy(rares[i], 1, l, 2, l.length - 2);

				rares[i] = l;
			}
		}

		if (all.length == 6) {
			int[] l = new int[7];

			l[0] = all[0];

			System.arraycopy(all, 1, l, 2, l.length - 2);

			all = l;
		}

		if (!res.isEmpty()) {
			for (CharaGroup g : res.keySet()) {
				int[] lv = res.get(g);
				groups.put(g, new GroupRestrict(lv, -1));
			}
		}
	}
}
