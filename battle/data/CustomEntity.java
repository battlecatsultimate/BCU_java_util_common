package common.battle.data;

import common.io.json.JsonClass;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonDecoder;
import common.io.json.JsonField;
import common.io.json.JsonField.GenType;
import common.util.Data;
import common.util.unit.Trait;

import java.util.ArrayList;
import java.util.List;

@JsonClass(noTag = NoTag.LOAD)
public abstract class CustomEntity extends DataEntity {

	@JsonField(gen = GenType.GEN)
	public AtkDataModel rep, rev, res, cntr;

	@JsonField(gen = GenType.GEN, usePool = true)
	public AtkDataModel[] atks;

	public int tba, base, touch = TCH_N;
	public boolean common = true;

	/**
	 * This field is used to filter all the procs of units if common is false,
	 * Also used for counter
	 */
	@JsonField(block = true)
	private Proc all;

	@Override
	public int allAtk() {
		int ans = 0, temp = 0, c = 1;
		for (AtkDataModel adm : atks)
			if (adm.pre > 0) {
				ans += temp / c;
				temp = adm.getDire() > 0 ? adm.atk : 0;
				c = 1;
			} else {
				temp += adm.getDire() > 0 ? adm.atk : 0;
				c++;
			}
		ans += temp / c;
		return ans;
	}

	/**
	 * Updates the procs in all and initializes if it is null
	 */
	public void updateAllProc() {
		all = Proc.blank();
		for (int i = 0; i < Data.PROC_TOT; i++) {
			if (Data.procSharable[i]) {
				all.getArr(i).set(getProc().getArr(i));
			} else
				for (AtkDataModel adm : atks)
					if (!all.getArr(i).exists())
						all.getArr(i).set(adm.proc.getArr(i));
		}
	}

	/**
	 * Gets all procs for units without common proc
	 */
	@Override
	public Proc getAllProc() {
		if (common)
			return getProc();
		if (all == null)
			updateAllProc();
		return all;
	}

	@Override
	public int getAtkCount() {
		return atks.length;
	}

	@Override
	public MaskAtk getAtkModel(int ind) {
		if (ind < atks.length)
			return atks[ind];
		if (ind == atks.length)
			return rev;
		if (ind == atks.length + 1)
			return res;
		return null;
	}

	@Override
	public MaskAtk[] getAtks() {
		return atks;
	}

	public String getAvailable(String str) {
		while (contains(str))
			str += "'";
		return str;
	}

	@Override
	public int getItv() {
		int longPre = 0;
		for (AtkDataModel adm : atks)
			longPre += adm.pre;
		return longPre + Math.max(getTBA() - 1, getPost());
	}

	@Override
	public int getPost() {
		int ans = getAnimLen();
		for (AtkDataModel adm : atks)
			ans -= adm.pre;
		return ans;
	}

	@Override
	public Proc getProc() {
		return rep.getProc();
	}

	@Override
	public MaskAtk getRepAtk() {
		return rep;
	}

	@Override
	public AtkDataModel getResurrection() {
		return res;
	}

	@Override
	public AtkDataModel getRevenge() {
		return rev;
	}

	@Override
	public AtkDataModel getCounter() { return cntr; }

	@Override
	public int getTBA() {
		return tba;
	}

	@Override
	public int getTouch() {
		return touch;
	}

	public void importData(MaskEntity de) {
		hp = de.getHp();
		hb = de.getHb();
		speed = de.getSpeed();
		range = de.getRange();
		abi = de.getAbi();
		loop = de.getAtkLoop();
		traits = new ArrayList<>();
		for(Trait t : de.getTraits()) {
			if(!t.BCTrait)
				traits.add(t);
			else if(t.id.id != Data.TRAIT_EVA && t.id.id != Data.TRAIT_WITCH)
				traits.add(t);
		}
		width = de.getWidth();
		tba = de.getTBA();
		touch = de.getTouch();
		death = de.getDeathAnim();
		will = de.getWill();
		if (de instanceof CustomEntity) {
			importData$1((CustomEntity) de);
			return;
		}

		base = de.touchBase();
		common = ((DefaultData)de).isCommon();
		rep = new AtkDataModel(this);
		rep.proc = de.getRepAtk().getProc().clone();
		int m = de.getAtkCount();
		atks = new AtkDataModel[m];
		for (int i = 0; i < m; i++) {
			atks[i] = new AtkDataModel(this, de, i);
			for (int j : BCShareable)
				atks[i].proc.getArr(j).set(de.getProc().getArr(j));
		}
	}

	@Override
	public boolean isLD() {
		boolean ans = false;
		for (AtkDataModel adm : atks)
			ans |= adm.isLD();
		if(getRevenge() != null)
			ans |= getRevenge().isLD();
		if(getResurrection() != null)
			ans |= getResurrection().isLD();
		return ans;
	}

	/**
	 * Returns if a specific attack is LD,
	 * used to handle LD units that have an attack that isn't LD properly
	 * @param ind The attack to get.
	 */
	@Override
	public boolean isLD(int ind) {
		if (ind == atks.length)
			return rev.isLD();
		if (ind == atks.length + 1)
			return res.isLD();
		return atks[ind].isLD();
	}

	@Override
	public boolean isOmni() {
		boolean ans = false;
		for (AtkDataModel adm : atks)
			ans |= adm.isOmni();
		if(getRevenge() != null)
			ans |= getRevenge().isOmni();
		if(getResurrection() != null)
			ans |= getResurrection().isOmni();
		return ans;
	}

	/**
	 * Returns if a specific attack is Omni,
	 * used to handle Omni units that have an attack that lacks omni properly
	 * @param ind The attack to get.
	 */
	@Override
	public boolean isOmni(int ind) {
		if (ind == atks.length)
			return rev.isOmni();
		if (ind == atks.length + 1)
			return res.isOmni();
		return atks[ind].isOmni();
	}

	@Override
	public boolean isRange() {
		for (AtkDataModel adm : atks)
			if (adm.range)
				return true;
		return false;
	}

	@Override
	public int[][] rawAtkData() {
		int[][] ans = new int[atks.length][];
		for (int i = 0; i < atks.length; i++)
			ans[i] = atks[i].getAtkData();
		return ans;
	}

	@Override
	public int touchBase() {
		return base == 0 ? range : base;
	}

	private boolean contains(String str) {
		if (atks == null || atks.length == 0)
			return false;
		for (AtkDataModel adm : atks)
			if (adm != null && adm.str.equals(str))
				return true;
		return false;
	}

	private void importData$1(CustomEntity ce) {
		base = ce.base;
		common = ce.common;
		rep = new AtkDataModel(this, ce.rep);

		List<AtkDataModel> temp = new ArrayList<>();
		List<AtkDataModel> tnew = new ArrayList<>();
		int[] inds = new int[ce.atks.length];
		for (int i = 0; i < ce.atks.length; i++) {
			if (!temp.contains(ce.atks[i])) {
				temp.add(ce.atks[i]);
				tnew.add(new AtkDataModel(this, ce.atks[i]));
			}
			inds[i] = temp.indexOf(ce.atks[i]);
		}
		atks = new AtkDataModel[ce.atks.length];
		for (int i = 0; i < atks.length; i++) {
			atks[i] = tnew.get(inds[i]);

			if (!ce.getClass().equals(getClass()))
				atks[i].getProc().SUMMON.id = null;
		}
	}

	@JsonDecoder.OnInjected
	public void onInjected() {
		for (int i = 0; i < traits.size(); i++)
			if (traits.get(i) == null) {
				traits.remove(i);
				i--;
			}
	}
}
