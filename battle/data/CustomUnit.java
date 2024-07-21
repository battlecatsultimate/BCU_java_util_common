package common.battle.data;

import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.util.Data;
import common.util.pack.Soul;
import common.util.unit.Form;
import org.jcodec.common.tools.MathUtil;

import java.util.ArrayList;

@JsonClass
public class CustomUnit extends CustomEntity implements MaskUnit, Cloneable {

	public Form pack;

	@JsonField
	public int price, resp, back, front, limit;

	@JsonField(gen = JsonField.GenType.GEN)
	public PCoin pcoin = null;

	public CustomUnit() {
		rep = new AtkDataModel(this);
		atks = new AtkDataModel[1];
		atks[0] = new AtkDataModel(this);
		width = 320;
		speed = 8;
		hp = 1000;
		hb = 1;
		traits = new ArrayList<>();
		price = 50;
		resp = 60;
		back = 0;
		front = 9;
		death = new Identifier<>(Identifier.DEF, Soul.class, 0);
	}

	@Override
	public int getBack() {
		return back;
	}

	@Override
	public int getFront() {
		return front;
	}

	@Override
	public Orb getOrb() {
		return pack.orbs;
	}

	@Override
	public Form getPack() {
		return pack;
	}

	@Override
	public int getPrice() {
		return price;
	}

	@Override
	public int getRespawn() {
		return resp;
	}

	@Override
	public int getLimit() {
		return limit;
	}

	@Override
	public PCoin getPCoin() { return pcoin; }

	@Override
	public void importData(MaskEntity de) {
		super.importData(de);

		if (de instanceof MaskUnit) {
			MaskUnit mu = (MaskUnit) de;

			price = mu.getPrice();
			resp = mu.getRespawn();
			back = Math.min(mu.getBack(), mu.getFront());
			front = Math.max(mu.getBack(), mu.getFront());
			limit = mu.getLimit();

			PCoin p = mu.getPCoin();
			if (p == null)
				return;

			ArrayList<int[]> info = p.info;
			pcoin = new PCoin(this);
			pcoin.max = p.max;
			for (int i = 0; i < info.size(); i++) {
				int[] j = info.get(i).clone();
				int[] data = Data.PC_CORRES[j[0]];
				j[1] = pcoin.max[i] = data[3] > -1 ? 1 : Math.max(1, j[1]);
				j[13] = MathUtil.clip(j[13], 0, 1);
				if (data[3] > -1) {
					j[0] = data[3];
					j[2] = j[3] = 100;
				}
				if (j[0] == 65 || j[0] == 56) {
					int[] lvl = new int[] { j[4], j[5] };
					int[] max = new int[] { j[6], j[7] };
					int[] min = new int[] { j[8], j[9] };
					j[4] = max[0] / 4;
					j[5] = max[1] / 4;
					j[6] = (max[0] + min[0]) / 4;
					j[7] = (max[1] + min[1]) / 4;
					j[8] = lvl[0];
					j[9] = lvl[1];
				}
				pcoin.info.add(j);
			}
		}
	}

	@Override
	public CustomUnit clone() {
		CustomUnit ans = (CustomUnit) Data.err(super::clone);
		ans.importData(this);
		ans.pack = getPack();
		ans.getPack().anim = getPack().anim;
		return ans;
	}
}
