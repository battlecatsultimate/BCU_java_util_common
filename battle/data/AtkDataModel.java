package common.battle.data;

import common.io.json.JsonClass;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonClass.RType;
import common.io.json.JsonField;
import common.pack.PackData;
import common.pack.UserProfile;
import common.system.BasedCopable;
import common.util.Data;

@JsonClass(read = RType.FILL, noTag = NoTag.LOAD)
public class AtkDataModel extends Data implements MaskAtk, BasedCopable<AtkDataModel, CustomEntity> {

	@JsonField(block = true)
	public final CustomEntity ce;
	public String str = "";
	public int atk, pre = 1, ld0, ld1, targ = TCH_N, count = -1, dire = 1, alt = 0, move = 0;
	public boolean range = true, specialTrait = false; //Special trait makes attacks that ignore traits consider traits, and attacks that don't do

	@JsonField
	public Proc proc;

	public AtkDataModel(CustomEntity ent) {
		ce = ent;
		str = ce.getAvailable(str);
		proc = Proc.blank();
	}

	protected AtkDataModel(CustomEntity ene, AtkDataModel adm) {
		ce = ene;
		str = ce.getAvailable(adm.str);
		atk = adm.atk;
		pre = adm.pre;
		ld0 = adm.ld0;
		ld1 = adm.ld1;
		range = adm.range;
		specialTrait = adm.specialTrait;
		dire = adm.dire;
		count = adm.count;
		targ = adm.targ;
		alt = adm.alt;
		move = adm.move;
		proc = adm.proc == null ? Proc.blank() : adm.proc.clone();
	}

	protected AtkDataModel(CustomEntity ene, MaskEntity me, int i) {
		ce = ene;
		str = ce.getAvailable("copied");
		int[][] dat = me.rawAtkData();
		MaskAtk am = me.getAtkModel(i);
		if (dat[i][2] == 1)
			proc = am.getProc().clone();
		else
			proc = Proc.blank();
		ld0 = am.getShortPoint();
		ld1 = am.getLongPoint();
		pre = dat[i][1];
		atk = dat[i][0];
		range = am.isRange();
		dire = am.getDire();
		count = am.loopCount();
		targ = am.getTarget();
		alt = am.getAltAbi();
		move = am.getMove();
	}

	@Override
	public AtkDataModel clone() {
		return new AtkDataModel(ce, this);
	}

	@Override
	public AtkDataModel copy(CustomEntity nce) {
		return new AtkDataModel(nce, this);
	}

	@Override
	public int getAltAbi() {
		return alt;
	}

	@Override
	public int getAtk() {
		return atk;
	}

	@Override
	public int getDire() {
		return dire;
	}

	@Override
	public int getLongPoint() {
		return ld1;
	}

	@Override
	public int getMove() {
		return move;
	}

	@Override
	public boolean getSPtrait() { return specialTrait; }

	@Override
	public Proc getProc() {
		if (ce.rep != this && ce.common)
			return ce.rep.getProc();
		return proc;
	}

	@Override
	public int getShortPoint() {
		return ld0;
	}

	@Override
	public int getTarget() {
		return targ;
	}

	@Override
	public boolean isRange() {
		return range;
	}

	@Override
	public int loopCount() {
		return count;
	}

	@Override
	public String toString() {
		return str;
	}

	protected int[] getAtkData() {
		return new int[] { atk, pre, 1, dire };
	}

	protected boolean isLD() {
		return ld0 > 0 || ld1 < 0;
	}

	@Override
	public boolean isOmni() {
		return ld0 * ld1 < 0 || (ld0 == 0 && ld1 > 0) || (ld0 < 0 && ld1 == 0);
	}

	public void inject(PackData.UserPack pack) { // // TODO: atkdatamodel.onInject cannot reliably get userpack at this time; this is done so procs properly apply on ALL attacks
		if (UserProfile.isOlderPack(pack, "0.7.4.1") && proc.WARP.prob > 0)
			proc.WARP.dis_1 = proc.WARP.dis_0;
	}
}
