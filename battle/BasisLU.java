package common.battle;

import common.CommonStatic;
import common.battle.data.PCoin;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.io.json.JsonField.GenType;
import common.pack.UserProfile;
import common.system.Copable;
import common.util.BattleStatic;
import common.util.stage.StageLimit;
import common.util.unit.Combo;
import common.util.unit.Form;
import common.util.unit.Level;
import common.util.unit.Unit;

import java.util.List;

@JsonClass
public class BasisLU extends Basis implements Copable<BasisLU>, BattleStatic {

	private static int[] getRandom(int n) {
		int[] ans = new int[n];
		int a = 0;
		for (int i = 0; i < n; i++) {
			int x = (int) (Math.random() * 10);
			while ((a & (1 << x)) > 0)
				x = (int) (Math.random() * 10);
			a |= 1 << x;
			ans[i] = x;
		}
		return ans;
	}

	private final Treasure t;

	@JsonField(gen = GenType.FILL)
	public final LineUp lu;

	@JsonField(gen = GenType.FILL)
	public int[] nyc = new int[3];

	public BasisLU() {
		t = new Treasure(this);
		lu = new LineUp();
	}

	public BasisLU(BasisSet bs) {
		t = new Treasure(this, bs.t());
		lu = new LineUp();
		name = "lineup " + bs.lb.size();
	}

	protected BasisLU(BasisSet bs, BasisLU bl) {
		t = new Treasure(this, bs.t());
		lu = new LineUp(bl.lu);
		name = "lineup " + bs.lb.size();
		nyc = bl.nyc.clone();
	}

	protected BasisLU(BasisSet bs, LineUp line, String str, int[] ints) {
		t = new Treasure(this, bs.t());
		name = str;
		lu = line;
		nyc = ints;
	}

	@Override
	public BasisLU copy() {
		return new BasisLU(BasisSet.current(), this);
	}

	@Override
	public int getInc(int type) {
		int inc = 0;
		for (Combo combo : lu.coms)
			if (combo.type == type)
				inc += CommonStatic.getBCAssets().values[combo.type][combo.lv];
		return inc;
	}

	public int getInc(int type, Unit unit) {
		int inc = 0;
		for (Combo combo : lu.coms) {
			if (combo.type == type && combo.checkCharaGroup(unit))
				inc += CommonStatic.getBCAssets().values[combo.type][combo.lv];
		}
		return inc;
	}

	public int getInc(int type, List<Unit> units) {
		int inc = 0;
		for (Combo combo : lu.coms) {
			if (combo.type == type && units.stream().anyMatch(combo::checkCharaGroup))
				inc += CommonStatic.getBCAssets().values[combo.type][combo.lv];
		}
		return inc;
	}

	public BasisLU randomize(int n) {
		BasisLU ans = copy();
		int[] rad = getRandom(n);
		List<Unit> list = UserProfile.getBCData().units.getList();
		list.removeIf(u -> u.forms.length == 1);
		for (Form[] fs : ans.lu.fs)
			for (Form f : fs)
				if (f != null)
					list.remove(f.unit);
		for (int i = 0; i < n; i++) {
			Unit u = list.get((int) (Math.random() * list.size()));
			list.remove(u);
			ans.lu.setFS(u.forms[u.forms.length - 1], rad[i]);
		}
		ans.lu.arrange();
		return ans;
	}

	public void performRealisticLeveling(StageLimit lim) {
		for(Form[] fs : lu.fs) {
			for(int i = 0; i < fs.length; i++) {
				if(fs[i] == null)
					continue;

				Form f = fs[i];
				Level lv = lu.getLv(f);

				if(lv == null) {
					throw new IllegalStateException("Battle started without initializing level of form in lineup");
				}

				int[][] orbs = lv.getOrbs();
				int totalLv = lv.getLv() + lv.getPlusLv();

				if(orbs != null && !f.unit.orbs.isEmpty()) {
					for (int j = 0; j < orbs.length; j++) {
						if (f.unit.orbs.get(j).isRestricted(f.fid, totalLv) || (lim != null && orbs[j].length > 0 && lim.bannedOrb.contains(orbs[j][0])))
							orbs[j] = new int[3];
					}
				}

				if(!CommonStatic.getConfig().realLevel)
					continue;

				int maxForm;
				if (totalLv < 10) {
					maxForm = 0;
				} else if (totalLv < (fs[i].unit.info.tfLevel == -1 ? 20 : fs[i].unit.info.tfLevel)) {
					maxForm = 1;
				} else if (totalLv < fs[i].unit.info.zeroLevel) {
					maxForm = 2;
				} else {
					maxForm = 3;
				}

				fs[i] = fs[i].unit.forms[Math.min(maxForm, fs[i].fid)];
				if(fs[i].fid >= 2 && fs[i].du.getPCoin() != null) {
					int[] talents = lv.getTalents();
					PCoin pc = fs[i].du.getPCoin();

					for(int j = 0; j < Math.min(pc.info.size(), talents.length); j++) {
						if(pc.info.get(j)[13] == 1 && lv.getLv() + lv.getPlusLv() < 60) {
							talents[j] = 0;
						}
					}
				}
			}
		}

		lu.renew();
	}

	/**
	 * although the Treasure information is the same, this includes the effects of
	 * combo, so need to be an independent Treasure Object
	 */
	@Override
	public Treasure t() {
		return t;
	}

}
