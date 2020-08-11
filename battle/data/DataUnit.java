package common.battle.data;

import common.CommonStatic;
import common.pack.PackData.Identifier;
import common.util.pack.Soul;
import common.util.unit.Form;
import common.util.unit.Unit;

public class DataUnit extends DefaultData implements MaskUnit, Cloneable {

	private final Form form;
	public int price, respawn;
	private int front, back;

	public PCoin pcoin = null;

	public DataUnit(Form f, Unit u, String[] data) {
		form = f;
		int[] ints = new int[data.length];
		for (int i = 0; i < data.length; i++)
			ints[i] = CommonStatic.parseIntN(data[i]);
		hp = ints[0];
		hb = ints[1];
		speed = ints[2];
		atk = ints[3];
		tba = ints[4];
		range = ints[5];
		price = ints[6];
		respawn = ints[7] * 2;
		width = ints[9];
		int t = 0;
		if (ints[10] == 1)
			t |= TB_RED;
		isrange = ints[12] == 1;
		pre = ints[13];
		front = ints[14];
		back = ints[15];
		if (ints[16] == 1)
			t |= TB_FLOAT;
		if (ints[17] == 1)
			t |= TB_BLACK;
		if (ints[18] == 1)
			t |= TB_METAL;
		if (ints[19] == 1)
			t |= TB_WHITE;
		if (ints[20] == 1)
			t |= TB_ANGEL;
		if (ints[21] == 1)
			t |= TB_ALIEN;
		if (ints[22] == 1)
			t |= TB_ZOMBIE;
		int a = 0;
		if (ints[23] == 1)
			a |= AB_GOOD;
		proc = Proc.blank();
		proc.KB.prob = ints[24];
		proc.STOP.prob = ints[25];
		proc.STOP.time = ints[26];
		proc.SLOW.prob = ints[27];
		proc.SLOW.time = ints[28];
		if (ints[29] == 1)
			a |= AB_RESIST;
		if (ints[30] == 1)
			a |= AB_MASSIVE;
		proc.CRIT.prob = ints[31];
		if (ints[32] == 1)
			a |= AB_ONLY;
		if (ints[33] == 1)
			a |= AB_EARN;
		if (ints[34] == 1)
			a |= AB_BASE;
		proc.WAVE.prob = ints[35];
		proc.WAVE.lv = ints[36];
		proc.WEAK.prob = ints[37];
		proc.WEAK.time = ints[38];
		proc.WEAK.mult = ints[39];
		proc.STRONG.health = ints[40];
		proc.STRONG.mult = ints[41];
		proc.LETHAL.prob = ints[42];
		if (ints[43] == 1)
			a |= AB_METALIC;
		lds = ints[44];
		ldr = ints[45];

		if (ints[46] == 1)
			proc.IMUWAVE.mult = 100;
		if (ints[47] == 1)
			a |= AB_WAVES;
		if (ints[48] == 1)
			proc.IMUKB.mult = 100;
		if (ints[49] == 1)
			proc.IMUSTOP.mult = 100;
		if (ints[50] == 1)
			proc.IMUSLOW.mult = 100;
		if (ints[51] == 1)
			proc.IMUWEAK.mult = 100;
		try {
			if (ints[52] == 1)
				a |= AB_ZKILL;
			if (ints[53] == 1)
				a |= AB_WKILL;
			loop = ints[55];
			if (ints[56] != 0)
				a |= AB_IMUSW;
			if (ints[58] == 2)
				a |= AB_GLASS;
			atk1 = ints[59];
			atk2 = ints[60];
			pre1 = ints[61];
			pre2 = ints[62];
			abi0 = ints[63];
			abi1 = ints[64];
			abi2 = ints[65];
			death = Identifier.parseInt(ints[67], Soul.class);
			proc.BREAK.prob = ints[70];
			if (ints[75] == 1)
				proc.IMUWARP.mult = 100;
			if (ints[77] == 1)
				a |= AB_EKILL;
			if (ints[78] == 1)
				t |= TB_RELIC;
			if (ints[79] == 1)
				proc.IMUCURSE.mult = 100;
			if (ints[80] == 1)
				a |= AB_RESISTS;
			if (ints[81] == 1)
				a |= AB_MASSIVES;
			proc.SATK.prob = ints[82];
			proc.SATK.mult = ints[83];
			proc.IMUATK.prob = ints[84];
			proc.IMUATK.time = ints[85];
			proc.VOLC.prob = ints[86];
			proc.VOLC.dis_0 = ints[87] / 4;
			proc.VOLC.dis_1 = ints[88] / 4 + proc.VOLC.dis_0;
			proc.VOLC.time = ints[89] * VOLC_ITV;
			if (ints[90] == 1)
				proc.IMUPOIATK.mult = 100;
			if (ints[91] == 1)
				proc.IMUVOLC.mult = 100;
			proc.CURSE.prob = ints[92];
			proc.CURSE.time = ints[93];
		} catch (IndexOutOfBoundsException e) {
		}

		type = t;
		abi = a;

		datks = new DataAtk[getAtkCount()];

		for (int i = 0; i < datks.length; i++) {
			datks[i] = new DataAtk(this, i);
		}
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
		return form.orbs;
	}

	@Override
	public Form getPack() {
		return form;
	}

	@Override
	public int getPrice() {
		return price;
	}

	@Override
	public int getRespawn() {
		return respawn;
	}

	@Override
	protected DataUnit clone() {
		DataUnit ans;
		try {
			ans = (DataUnit) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
		ans.proc = proc.clone();
		ans.datks = new DataAtk[ans.getAtkCount()];
		for (int i = 0; i < ans.getAtkCount(); i++) {
			ans.datks[i] = new DataAtk(ans, i);
		}
		return ans;
	}

}
