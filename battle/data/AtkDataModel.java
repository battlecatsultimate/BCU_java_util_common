package common.battle.data;

import common.io.InStream;
import common.io.json.JsonClass;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonClass.RType;
import common.io.json.JsonField;
import common.system.BasedCopable;
import common.util.Data;

@JsonClass(read = RType.FILL, noTag = NoTag.LOAD)
public class AtkDataModel extends Data implements MaskAtk, BasedCopable<AtkDataModel, CustomEntity> {

	@JsonField(block = true)
	public final CustomEntity ce;
	public String str = "";
	public int atk, pre = 1, ld0, ld1, targ = TCH_N, count = -1, dire = 1, alt = 0, move = 0;
	public boolean range = true;
	
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
		dire = adm.dire;
		count = adm.count;
		targ = adm.targ;
		alt = adm.alt;
		move = adm.move;
		proc = proc.clone();
	}

	protected AtkDataModel(CustomEntity ent, InStream is) {
		ce = ent;
		proc = Proc.blank();
		zread("0.3.7", is);
	}

	@Deprecated
	protected AtkDataModel(CustomEntity ent, InStream is, String ver) {
		ce = ent;
		proc = Proc.blank();
		zread(ver, is);
	}

	protected AtkDataModel(CustomEntity ene, MaskEntity me, int i) {
		ce = ene;
		str = ce.getAvailable("copied");
		int[][] dat = me.rawAtkData();
		MaskAtk am = me.getAtkModel(i);
		if (dat[i][2] == 1)
			proc = am.getProc().clone();
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
	public int getDire() {
		return dire;
	}

	@Override
	public int getLongPoint() {
		return isLD() ? ld1 : ce.range;
	}

	@Override
	public int getMove() {
		return move;
	}

	@Override
	public Proc getProc() {
		if (ce.rep != this && ce.common)
			return ce.rep.getProc();
		return proc;
	}

	@Override
	public int getShortPoint() {
		return isLD() ? ld0 : -ce.width;
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
		return new int[] { atk, pre, 1 };
	}

	protected boolean isLD() {
		return ld0 != 0 || ld1 != 0;
	}

	protected boolean isOmni() {
		return ld0 * ld1 < 0;
	}

	protected void zread$000301(InStream is) {
		str = is.nextString();
		atk = is.nextInt();
		pre = is.nextInt();
		ld0 = is.nextShort();
		ld1 = is.nextShort();
		proc = Proc.load(is.nextIntsBB());
	}

	protected void zread$000400(InStream is) {
		str = is.nextString();
		atk = is.nextInt();
		pre = is.nextInt();
		ld0 = is.nextInt();
		ld1 = is.nextInt();
		targ = is.nextInt();
		dire = is.nextInt() == 1 ? -1 : 1;
		proc = Proc.load(is.nextIntsBB());
	}

	private void zread(String ver, InStream is) {
		int val = getVer(ver);
		if (val >= 307)
			val = getVer(is.nextString());
		else if (val >= 404)
			zread$000404(is);
		else if (val >= 403)
			zread$000403(is);
		else if (val >= 402)
			zread$000402(is);
		else if (val >= 401)
			zread$000401(is);
		else if (val >= 400)
			zread$000400(is);
		else if (val >= 301)
			zread$000301(is);
	}

	private void zread$000401(InStream is) {
		str = is.nextString();
		atk = is.nextInt();
		pre = is.nextInt();
		ld0 = is.nextInt();
		ld1 = is.nextInt();
		targ = is.nextInt();
		int bm = is.nextInt();
		dire = (bm & 1) > 0 ? -1 : 1;
		range = (bm & 2) > 0;
		proc = Proc.load(is.nextIntsBB());
	}

	private void zread$000402(InStream is) {
		str = is.nextString();
		atk = is.nextInt();
		pre = is.nextInt();
		ld0 = is.nextInt();
		ld1 = is.nextInt();
		targ = is.nextInt();
		count = is.nextInt();
		int bm = is.nextInt();
		dire = (bm & 1) > 0 ? -1 : 1;
		range = (bm & 2) > 0;
		proc = Proc.load(is.nextIntsBB());
	}

	private void zread$000403(InStream is) {
		str = is.nextString();
		atk = is.nextInt();
		pre = is.nextInt();
		ld0 = is.nextInt();
		ld1 = is.nextInt();
		targ = is.nextInt();
		count = is.nextInt();
		dire = is.nextInt();
		int bm = is.nextInt();
		range = (bm & 1) > 0;
		proc = Proc.load(is.nextIntsBB());
	}

	private void zread$000404(InStream is) {
		str = is.nextString();
		atk = is.nextInt();
		pre = is.nextInt();
		ld0 = is.nextInt();
		ld1 = is.nextInt();
		targ = is.nextInt();
		count = is.nextInt();
		dire = is.nextInt();
		alt = is.nextInt();
		move = is.nextInt();
		int bm = is.nextInt();
		range = (bm & 1) > 0;
		proc = Proc.load(is.nextIntsBB());
	}

	@Override
	public int getAtk() {
		return atk;
	}

}
