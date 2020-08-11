package common.battle;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;

import common.io.InStream;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.io.json.JsonField.GenType;
import common.io.json.JsonField.IOType;
import common.pack.VerFixer;
import common.pack.VerFixer.VerFixerException;
import common.system.Copable;

@JsonClass
public class BasisSet extends Basis implements Copable<BasisSet> {

	public static final List<BasisSet> list = new ArrayList<>();
	public static final BasisSet def = new BasisSet();
	public static BasisSet current;

	public static void read(InStream is) throws VerFixerException {
		zreads(is, false);
	}

	public static BasisSet[] readBackup(InStream is) throws VerFixerException {
		return zreads(is, true).toArray(new BasisSet[0]);
	}

	private static List<BasisSet> zreads(InStream is, boolean bac) throws VerFixerException {
		int ver = getVer(is.nextString());
		if (ver != 308)
			throw new VerFixer.VerFixerException("basis set has to have version 308, got " + ver);
		return zreads$000308(ver, is, bac);
	}

	private static List<BasisSet> zreads$000308(int ver, InStream is, boolean bac) throws VerFixerException {
		List<BasisSet> ans = bac ? new ArrayList<BasisSet>() : list;
		int n = is.nextInt();
		for (int i = 1; i < n; i++) {
			BasisSet bs = new BasisSet(ver, is.subStream());
			ans.add(bs);
		}
		int ind = Math.max(is.nextInt(), ans.size() - 1);
		if (!bac)
			current = list.get(ind);
		return ans;
	}

	@JsonField(gen = GenType.FILL)
	private final Treasure t;

	@JsonField(generic = BasisLU.class)
	public final List<BasisLU> lb = new ArrayList<>();

	public BasisLU sele;

	public BasisSet() {
		if (list.size() == 0)
			name = "temporary";
		else
			name = "set " + list.size();
		t = new Treasure(this);
		current = this;
		lb.add(sele = new BasisLU(this));
		list.add(this);
	}

	public BasisSet(BasisSet ref) {
		name = "set " + list.size();
		list.add(this);
		t = new Treasure(this, ref.t);
		current = this;
		for (BasisLU blu : ref.lb)
			lb.add(sele = new BasisLU(this, blu));
	}

	private BasisSet(int ver, InStream is) throws VerFixerException {
		name = is.nextString();
		t = new Treasure(this, ver, is);
		zread(ver, is);
	}

	public BasisLU add() {
		lb.add(sele = new BasisLU(this));
		return sele;
	}

	@Override
	public BasisSet copy() {
		return new BasisSet(this);
	}

	public BasisLU copyCurrent() {
		lb.add(sele = new BasisLU(this, sele));
		return sele;
	}

	/** BasisSet are used in data display, so cannot be effected by combo */
	@Override
	public int getInc(int type) {
		return 0;
	}

	public BasisLU remove() {
		lb.remove(sele);
		return sele = lb.get(0);
	}

	@Override
	public Treasure t() {
		return t;
	}

	@JsonField(tag = "sele", io = IOType.R)
	public void zgen(JsonElement elem) {
		sele = lb.get(elem.getAsInt());
	}

	@JsonField(tag = "sele", io = IOType.W)
	public int zser() {
		return lb.indexOf(sele);
	}

	private void zread(int val, InStream is) throws VerFixerException {
		val = getVer(is.nextString());
		if (val != 308)
			throw new VerFixer.VerFixerException("basis set has to have version 308, got " + val);
		int n = is.nextInt();
		for (int i = 0; i < n; i++) {
			String str = is.nextString();
			int[] ints = is.nextIntsB();
			InStream sub = is.subStream();
			BasisLU bl = new BasisLU(this, new LineUp(308, sub), str, ints);
			lb.add(bl);
		}
		int ind = is.nextInt();
		sele = lb.get(ind);
	}

}
