package common.util.unit;

import common.io.InStream;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.pack.IndexContainer.IndexCont;
import common.pack.IndexContainer.Indexable;
import common.pack.PackData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonClass
@JsonClass.JCGeneric(Identifier.class)
@IndexCont(PackData.class)
public class UnitLevel implements Indexable<PackData, UnitLevel> {

	@JsonField
	public int[] lvs = new int[20];

	public final List<Unit> units = new ArrayList<>();

	@JsonField
	@JsonClass.JCIdentifier
	public Identifier<UnitLevel> id;

	public UnitLevel(Identifier<UnitLevel> ID, InStream is) {
		id = ID;
		zread(is);
	}

	public UnitLevel(Identifier<UnitLevel> ID, UnitLevel ul) {
		id = ID;
		lvs = ul.lvs.clone();
	}

	public UnitLevel(int[] inp) {
		lvs = Arrays.copyOf(inp, 20);
	}

	@JsonClass.JCConstructor
	public UnitLevel() {}

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

	public float getMult(int lv) {
		int dec = lv;
		float d = 0.8f;

		for (int mul : lvs) {
			if (dec >= 10) {
				d += mul * 0.1f;
				dec -= 10;
			} else {
				d += mul * dec * 0.01f;
				break;
			}
		}
		return d;
	}

	@Override
	public String toString() {
		StringBuilder ans = new StringBuilder("{");
		for (int set : lvs) {
			if (ans.length() > 1)
				ans.append(", ");
			ans.append(set);
		}
		ans.append("}");
		return ans.toString();
	}

	private void zread(InStream is) {
		int ver = is.nextInt();
		int[] lvs = new int[3];
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

		int pre = 0, mul = 20;
		for (int i = 0; i < 3; i++) {
			for (int j = pre; j < lvs[i]; j++)
				if(j < 20)
					this.lvs[j] = mul;
			mul /= 2;
			pre = lvs[i];
		}
	}

}
