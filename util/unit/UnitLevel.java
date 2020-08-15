package common.util.unit;

import java.util.ArrayList;
import java.util.List;

import common.io.InStream;
import common.pack.PackData;
import common.pack.PackData.Identifier;
import common.pack.IndexContainer.IndexCont;
import common.pack.IndexContainer.Indexable;

@IndexCont(PackData.class)
public class UnitLevel implements Indexable<PackData, UnitLevel> {

	public static UnitLevel def;

	public final int[] lvs = new int[3];

	public final List<Unit> units = new ArrayList<>();

	public Identifier<UnitLevel> id;

	public UnitLevel(Identifier<UnitLevel> ID, InStream is) {
		id = ID;
		zread(is);
	}

	public UnitLevel(Identifier<UnitLevel> ID, UnitLevel ul) {
		id = ID;
		for (int i = 0; i < 3; i++)
			lvs[i] = ul.lvs[i];
	}

	public UnitLevel(int[] inp) {
		int val = -1;
		for (int i = 0; i < inp.length; i++) {
			if (val != inp[i]) {
				val = inp[i];
				if (val == 10)
					lvs[0] = i;
				if (val == 5)
					lvs[1] = i;
				if (val == 0)
					lvs[2] = i;
			}
		}
		if (lvs[1] == 0)
			lvs[1] = inp.length;
		if (lvs[2] == 0)
			lvs[2] = inp.length;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof UnitLevel))
			return false;
		UnitLevel ul = (UnitLevel) o;
		if (lvs.length != ul.lvs.length)
			return false;
		for (int i = 0; i < lvs.length; i++)
			if (lvs[i] != ul.lvs[i])
				return false;
		return id == null || ul.id == null || id.equals(ul.id);
	}

	@Override
	public Identifier<UnitLevel> getID() {
		return id;
	}

	public double getMult(int lv) {
		int dec = lv;
		int pre = 0, mul = 20;
		double d = 0.8;
		for (int i = 0; i < lvs.length; i++) {
			int dur = lvs[i] - pre;
			if (dec > dur * 10) {
				d += mul * dur * 0.1;
				dec -= dur * 10;
			} else {
				d += mul * dec * 0.01;
				break;
			}
			mul /= 2;
			pre = lvs[i];
		}
		return d;
	}

	@Override
	public String toString() {
		String ans = "{";
		for (int set : lvs) {
			if (ans.length() > 1)
				ans += ", ";
			ans += set;
		}
		ans += "}";
		return ans;
	}

	private void zread(InStream is) {
		int ver = is.nextInt();
		if (ver == 1) {
			int[] vs = is.nextIntsB();
			lvs[0] = vs[0];
			lvs[1] = vs[1];
			lvs[2] = vs[2];
		} else {
			int[][] vs = is.nextIntsBB();
			lvs[0] = vs[1][0];
			lvs[1] = vs[2][0];
			lvs[2] = vs[3][0];
		}
	}

}
