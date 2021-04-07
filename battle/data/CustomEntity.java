package common.battle.data;

import common.io.InStream;
import common.io.json.JsonClass;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonField;
import common.io.json.JsonField.GenType;
import common.pack.Identifier;
import common.util.Data;
import common.util.pack.Soul;
import common.util.unit.CustomTrait;

import java.util.ArrayList;
import java.util.List;

@JsonClass(noTag = NoTag.LOAD)
public abstract class CustomEntity extends DataEntity {

	@JsonField(gen = GenType.GEN)
	public AtkDataModel rep, rev, res;

	@JsonField(gen = GenType.GEN, usePool = true)
	public AtkDataModel[] atks;

	public int tba, base, touch = TCH_N;
	public boolean common = true;
	@JsonField(generic = CustomTrait.class, alias = Identifier.class)
	public ArrayList<Identifier<CustomTrait>> customTraits = new ArrayList<>();
	@JsonField(generic = String.class)
	public ArrayList<String> nullFixer = new ArrayList<>();

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

	@Override
	public Proc getAllProc() {
		if (all != null)
			return all;
		all = rep.getProc().clone();
		for (AtkDataModel adm : atks) {
			for (int i = 0; i < Data.PROC_TOT; i++)
				if (!all.getArr(i).exists())
					all.getArr(i).set(adm.proc.getArr(i));
		}
		return all;
	}

	public Proc[] getAllProcs() {
		int n = atks.length + 1;
		Proc[] ans = new Proc[n];
		ans[0] = rep.proc;
		for (int i = 0; i < atks.length; i++)
			ans[i + 1] = atks[i].proc;
		return ans;
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
		type = de.getType();
		width = de.getWidth();
		shield = de.getShield();
		tba = de.getTBA();
		touch = de.getTouch();
		death = de.getDeathAnim();
		if (de instanceof CustomEntity) {
			importData$1((CustomEntity) de);
			return;
		}

		base = de.touchBase();
		common = false;
		rep = new AtkDataModel(this);
		rep.proc = de.getRepAtk().getProc().clone();
		int m = de.getAtkCount();
		atks = new AtkDataModel[m];
		for (int i = 0; i < m; i++)
			atks[i] = new AtkDataModel(this, de, i);
	}

	@Override
	public boolean isLD() {
		boolean ans = false;
		for (AtkDataModel adm : atks)
			ans |= adm.isLD();
		return ans;
	}

	@Override
	public boolean isOmni() {
		boolean ans = false;
		for (AtkDataModel adm : atks)
			ans |= adm.isOmni();
		return ans;
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

	protected void zreada(InStream is) {
		int ver = getVer(is.nextString());
		if (ver >= 404)
			zreada$000404(is);
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
		customTraits = ce.customTraits;

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
		for (int i = 0; i < atks.length; i++)
			atks[i] = tnew.get(inds[i]);

	}

	private void zreada$000404(InStream is) {
		hp = is.nextInt();
		hb = is.nextInt();
		speed = is.nextInt();
		range = is.nextInt();
		abi = is.nextInt();
		type = is.nextInt();
		type = Data.reorderTrait(type);
		width = is.nextInt();
		shield = is.nextInt();
		tba = is.nextInt();
		base = is.nextInt();
		touch = is.nextInt();
		loop = is.nextInt();
		death = Identifier.parseInt(is.nextInt(), Soul.class);
		common = is.nextInt() > 0;
		rep = new AtkDataModel(this, is);
		int m = is.nextInt();
		AtkDataModel[] set = new AtkDataModel[m];
		for (int i = 0; i < m; i++)
			set[i] = new AtkDataModel(this, is);
		int n = is.nextInt();
		atks = new AtkDataModel[n];
		for (int i = 0; i < n; i++)
			atks[i] = set[is.nextInt()];
		int adi = is.nextInt();
		if ((adi & 1) > 0)
			rev = new AtkDataModel(this, is);
		if ((adi & 2) > 0)
			res = new AtkDataModel(this, is);
	}
}
