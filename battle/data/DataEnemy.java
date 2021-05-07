package common.battle.data;

import common.battle.Basis;
import common.pack.FixIndexList.FixIndexMap;
import common.pack.Identifier;
import common.pack.UserProfile;
import common.util.pack.Soul;
import common.util.unit.Enemy;
import common.util.unit.Trait;

public class DataEnemy extends DefaultData implements MaskEnemy {

	private final Enemy enemy;

	private int earn, star;
	public int limit;

	public DataEnemy(Enemy e) {
		enemy = e;
		proc = Proc.blank();
	}

	public void fillData(String[] strs) {
		//This function serves to gather BC enemy data and give them their stats accordingly, custom units don't really use this
		int[] ints = new int[strs.length];
		for (int i = 0; i < strs.length; i++)
			ints[i] = Integer.parseInt(strs[i]);
		hp = ints[0];
		hb = ints[1];
		speed = ints[2];
		atk = ints[3];
		tba = ints[4];
		range = ints[5];
		earn = ints[6];
		width = ints[8];
		int t = 0;
		FixIndexMap<Trait> BCTraits = UserProfile.getBCData().traits;
		if (ints[10] == 1)
			//Red
			traits.add(BCTraits.get(TRAIT_RED));
		isrange = ints[11] == 1;
		pre = ints[12];
		if (ints[13] == 1)
			//Floating
			traits.add(BCTraits.get(TRAIT_FLOAT));
		if (ints[14] == 1)
			//Black
			traits.add(BCTraits.get(TRAIT_BLACK));
		if (ints[15] == 1)
			//Metal
			traits.add(BCTraits.get(TRAIT_METAL));
		if (ints[17] == 1)
			//Angel
			traits.add(BCTraits.get(TRAIT_ANGEL));
		if (ints[18] == 1)
			//Alien
			traits.add(BCTraits.get(TRAIT_ALIEN));
		if (ints[19] == 1)
			//Zombie
			traits.add(BCTraits.get(TRAIT_ZOMBIE));
		proc.KB.prob = ints[20];
		proc.STOP.prob = ints[21];
		proc.STOP.time = ints[22];
		proc.SLOW.prob = ints[23];
		proc.SLOW.time = ints[24];
		proc.CRIT.prob = ints[25];
		int a = 0;
		if (ints[26] == 1)
			a |= AB_BASE;
		if(ints.length < 87 || ints[86] != 1) {
			proc.WAVE.prob = ints[27];
			proc.WAVE.lv = ints[28];
		} else {
			proc.MINIWAVE.prob = ints[27];
			proc.MINIWAVE.lv = ints[28];
			proc.MINIWAVE.multi = 20;
		}
		proc.WEAK.prob = ints[29];
		proc.WEAK.time = ints[30];
		proc.WEAK.mult = ints[31];
		proc.STRONG.health = ints[32];
		proc.STRONG.mult = ints[33];
		proc.LETHAL.prob = ints[34];

		lds = ints[35];
		ldr = ints[36];
		if (ints[37] == 1)
			proc.IMUWAVE.mult = 100;
		if (ints[38] == 1)
			a |= AB_WAVES;
		if (ints[39] == 1)
			proc.IMUKB.mult = 100;
		if (ints[40] == 1)
			proc.IMUSTOP.mult = 100;
		if (ints[41] == 1)
			proc.IMUSLOW.mult = 100;
		if (ints[42] == 1)
			proc.IMUWEAK.mult = 100;
		proc.BURROW.count = ints[43];
		proc.BURROW.dis = ints[44] / 4;
		proc.REVIVE.count = ints[45];
		proc.REVIVE.time = ints[46];
		proc.REVIVE.health = ints[47];
		if (ints[48] == 1)
			//Witch
			traits.add(BCTraits.get(TRAIT_WITCH));
		if (ints[49] == 1)
			//Base
			traits.add(BCTraits.get(TRAIT_INFH));
		loop = ints[50];
		if (ints[52] == 2)
			a |= AB_GLASS;
		death = Identifier.parseInt(ints[54], Soul.class);
		if(ints[54] == -1 && ints[63] == 1)
			death = Identifier.parseInt(9, Soul.class);
		atk1 = ints[55];
		atk2 = ints[56];
		pre1 = ints[57];
		pre2 = ints[58];
		abi0 = ints[59];
		abi1 = ints[60];
		abi2 = ints[61];
		shield = ints[64];
		proc.WARP.prob = ints[65];
		proc.WARP.time = ints[66];
		proc.WARP.dis = ints[67] / 4;
		star = ints[69];
		if (ints[71] == 1)
			//EVA
			traits.add(BCTraits.get(TRAIT_EVA));
		if (ints[72] == 1)
			//Relic
			traits.add(BCTraits.get(TRAIT_RELIC));
		if (ints[16] == 1)
			//White
			traits.add(BCTraits.get(TRAIT_WHITE));
		proc.CURSE.prob = ints[73];
		proc.CURSE.time = ints[74];
		proc.SATK.prob = ints[75];
		proc.SATK.mult = ints[76];
		proc.IMUATK.prob = ints[77];
		proc.IMUATK.time = ints[78];
		proc.POIATK.prob = ints[79];
		proc.POIATK.mult = ints[80];
		proc.VOLC.prob = ints[81];
		proc.VOLC.dis_0 = ints[82] / 4;
		proc.VOLC.dis_1 = ints[83] / 4 + proc.VOLC.dis_0;
		proc.VOLC.time = ints[84] * VOLC_ITV;

		abi = a;

		datks = new DataAtk[getAtkCount()];

		for (int i = 0; i < datks.length; i++) {
			datks[i] = new DataAtk(this, i);
		}
	}

	@Override
	public double getDrop() {
		return earn;
	}

	@Override
	public Enemy getPack() {
		return enemy;
	}

	@Override
	public int getStar() {
		return star;
	}

	@Override
	public int getLim() { return limit * 6; }

	@Override
	public double multi(Basis b) {
		if (star > 0)
			return b.t().getStarMulti(star);
		if (traits.contains(UserProfile.getBCData().traits.get(TRAIT_ALIEN)))
			return b.t().getAlienMulti();
		return 1;
	}

}
