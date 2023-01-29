package common.battle;

import common.CommonStatic;
import common.battle.data.PCoin;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.io.json.JsonField.GenType;
import common.pack.Identifier;
import common.pack.UserProfile;
import common.system.Copable;
import common.util.BattleStatic;
import common.util.unit.Form;
import common.util.unit.Level;
import common.util.unit.Unit;

import java.util.Arrays;
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
		return lu.inc[type];
	}

	public BasisLU randomize(int n) {
		BasisLU ans = copy();
		int[] rad = getRandom(n);
		List<Unit> list = UserProfile.getBCData().units.getList();
		list.remove(Identifier.parseInt(339, Unit.class).get());
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

	public void performRealisticLeveling() {
		if(!CommonStatic.getConfig().realLevel)
			return;

		for(Form[] fs : lu.fs) {
			for(int i = 0; i < fs.length; i++) {
				if(fs[i] == null)
					continue;

				Level lv = lu.getLv(fs[i]);

				if(lv == null) {
					throw new IllegalStateException("Battle started without initializing level of form in lineup");
				}

				if(fs[i].unit.info.tfLevel != -1 && lv.getLv() + lv.getPlusLv() < fs[i].unit.info.tfLevel && fs[i].fid == 2) {
					fs[i] = fs[i].unit.forms[1];
				} else if(fs[i].fid == 2 && fs[i].du.getPCoin() != null) {
					int[] talents = lv.getTalents();
					PCoin pc = fs[i].du.getPCoin();

					for(int j = 0; j < Math.min(pc.info.size(), talents.length); j++) {
						if(pc.info.get(j)[13] == 1 && lv.getLv() + lv.getPlusLv() < 60) {
							talents[j] = 0;
						}
					}

					int[][] orbs = lv.getOrbs();

					if(orbs != null && fs[i].orbs != null && fs[i].orbs.getSlots() != -1) {
						int[] limits = fs[i].orbs.getLimits();

						for(int j = 0; j < orbs.length; j++) {
							if(limits[j] == 1 && lv.getLv() + lv.getPlusLv() < 60)
								orbs[j] = new int[0];
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
