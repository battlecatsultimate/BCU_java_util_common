package common.battle.data;

import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.util.Data;
import common.util.pack.Soul;
import common.util.unit.Form;

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

			if (p != null) {
				pcoin = new PCoin(this);

				for (int[] i : p.info) {
					int[] j = new int[14];

					System. arraycopy(i, 0, j, 0, 12);

					j[13] = -1;

					j[1] = Math.max(1, j[1]);

					pcoin.info.add(j);
				}
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
