package common.battle.data;

import common.io.InStream;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.util.pack.Soul;
import common.util.unit.Form;

@JsonClass
public class CustomUnit extends CustomEntity implements MaskUnit {

	public Form pack;

	@JsonField
	public int price, resp, back, front, limit;

	public CustomUnit() {
		rep = new AtkDataModel(this);
		atks = new AtkDataModel[1];
		atks[0] = new AtkDataModel(this);
		width = 320;
		speed = 8;
		hp = 1000;
		hb = 1;
		type = 0;
		price = 50;
		resp = 60;
		back = 0;
		front = 9;
		death = new Identifier<>(Identifier.DEF, Soul.class, 0);
	}

	public void fillData(int ver, InStream is) {
		zread(ver, is);
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
	public void importData(MaskEntity de) {
		super.importData(de);
		if (de instanceof MaskUnit) {
			MaskUnit mu = (MaskUnit) de;
			price = mu.getPrice();
			resp = mu.getRespawn();
			back = Math.min(mu.getBack(), mu.getFront());
			front = Math.max(mu.getBack(), mu.getFront());
		}
	}

	private void zread(int val, InStream is) {
		val = getVer(is.nextString());
		if (val >= 400)
			zread$000400(is);
	}

	private void zread$000400(InStream is) {
		zreada(is);
		price = is.nextInt();
		resp = is.nextInt();
	}

}
