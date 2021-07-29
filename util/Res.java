package common.util;

import common.CommonStatic;
import common.CommonStatic.BCAuxAssets;
import common.battle.entity.AbEntity;
import common.system.P;
import common.system.SymCoord;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.util.anim.ImgCut;

public class Res extends ImgCore {

	public static P getBase(AbEntity ae, SymCoord coor, boolean dojo) {
		BCAuxAssets aux = CommonStatic.getBCAssets();

		if(ae.dire == 1 && dojo) {
			FakeImage[] input = new FakeImage[15];
			for(int i = 0; i < 7; i++)
				input[i] = aux.num[5][11].getImg();
			input[7] = aux.num[5][10].getImg();
			for(int i = 0; i < 7; i++)
				input[i+8] = aux.num[5][11].getImg();

			return coor.draw(input);
		} else {
			long h = ae.health;
			if (h < 0)
				h = 0;
			int[] val0 = getLab(h);
			int[] val1 = getLab(ae.maxH);
			FakeImage[] input = new FakeImage[val0.length + val1.length + 1];
			for (int i = 0; i < val0.length; i++)
				input[i] = aux.num[5][val0[i]].getImg();
			input[val0.length] = aux.num[5][10].getImg();
			for (int i = 0; i < val1.length; i++)
				input[val0.length + i + 1] = aux.num[5][val1[i]].getImg();
			return coor.draw(input);
		}
	}

	public static P getCost(int cost, boolean enable, SymCoord coor) {
		BCAuxAssets aux = CommonStatic.getBCAssets();
		if (cost == -1)
			return coor.draw(aux.battle[0][3].getImg());
		int[] val = getLab(cost);
		FakeImage[] input = new FakeImage[val.length];
		for (int i = 0; i < val.length; i++)
			input[i] = aux.num[enable ? 3 : 4][val[i]].getImg();
		return coor.draw(input);
	}

	public static P getMoney(int mon, int max, SymCoord sym) {
		BCAuxAssets aux = CommonStatic.getBCAssets();
		int[] val0 = getLab(mon);
		int[] val1 = getLab(max);
		FakeImage[] input = new FakeImage[val0.length + val1.length + 1];
		for (int i = 0; i < val0.length; i++)
			input[i] = aux.num[0][val0[i]].getImg();
		input[val0.length] = aux.num[0][10].getImg();
		for (int i = 0; i < val1.length; i++)
			input[val0.length + i + 1] = aux.num[0][val1[i]].getImg();

		return sym.draw(input);
	}

	public static P getWorkerLv(int lv, boolean enable, SymCoord coor) {
		BCAuxAssets aux = CommonStatic.getBCAssets();
		return coor.draw(aux.num[enable ? 1 : 2][10].getImg(), aux.num[enable ? 1 : 2][lv].getImg());
	}

	public static void readData() {
		BCAuxAssets aux = CommonStatic.getBCAssets();
		aux.unicut = ImgCut.newIns("./org/data/uni.imgcut");
		aux.udicut = ImgCut.newIns("./org/data/udi.imgcut");
		VImg uni = new VImg("./org/page/uni.png");
		uni.setCut(aux.unicut);
		aux.slot[0] = uni;

		aux.ico[0] = new VImg[7];
		aux.ico[1] = new VImg[4];
		aux.ico[0][0] = new VImg("./org/page/foreground.png");
		aux.ico[0][1] = new VImg("./org/page/starFG.png");
		aux.ico[0][2] = new VImg("./org/page/EFBG.png");
		aux.ico[0][3] = new VImg("./org/page/TFBG.png");
		aux.ico[0][4] = new VImg("./org/page/glow.png");
		aux.ico[0][5] = new VImg("./org/page/EFFG.png");
		aux.ico[0][6] = new VImg("./org/page/glow2.png");
		aux.ico[1][0] = new VImg("./org/page/uni_f.png");
		aux.ico[1][1] = new VImg("./org/page/uni_c.png");
		aux.ico[1][2] = new VImg("./org/page/uni_s.png");
		aux.ico[1][3] = new VImg("./org/page/uni_box.png");
		for (VImg vs : aux.ico[1])
			vs.setCut(aux.unicut);

		ImgCut ic029 = ImgCut.newIns("./org/page/img029.imgcut");
		VImg img029 = new VImg("./org/page/img029.png");
		FakeImage[] parts = ic029.cut(img029.getImg());
		aux.slot[1] = new VImg(parts[9]);
		aux.slot[2] = new VImg(parts[10]);
		readAbiIcon();
		readBattle();
	}

	private static int[] getLab(long cost) {
		if (cost < 0)
			cost = 0;

		int len = (int) Math.log10(cost == 0 ? 1 : cost) + 1;

		if (len < 0)
			len = 0;

		int[] input = new int[len];
		for (int i = 0; i < len; i++) {
			input[len - i - 1] = (int) (cost % 10);
			cost /= 10;
		}
		return input;
	}

	private static void readAbiIcon() {
		BCAuxAssets aux = CommonStatic.getBCAssets();
		CommonStatic.getConfig().icon = true;
		ImgCut ic015 = ImgCut.newIns("./org/page/img015.imgcut");
		VImg img015 = new VImg("./org/page/img015.png");
		FakeImage[] parts = ic015.cut(img015.getImg());
		aux.icon[0] = new VImg[ABI_TOT];
		aux.icon[1] = new VImg[PROC_TOT];
		aux.icon[2] = new VImg[ATK_TOT];
		aux.icon[3] = new VImg[TRAIT_TOT];
		aux.icon[4] = new VImg[PC2_TOT];
		aux.icon[3][TRAIT_RED] = new VImg(parts[77]);
		aux.icon[3][TRAIT_FLOAT] = new VImg(parts[78]);
		aux.icon[3][TRAIT_BLACK] = new VImg(parts[79]);
		aux.icon[3][TRAIT_METAL] = new VImg(parts[80]);
		aux.icon[3][TRAIT_ANGEL] = new VImg(parts[81]);
		aux.icon[3][TRAIT_ALIEN] = new VImg(parts[82]);
		aux.icon[3][TRAIT_ZOMBIE] = new VImg(parts[83]);
		aux.icon[3][TRAIT_RELIC] = new VImg(parts[84]);
		aux.icon[3][TRAIT_DEMON] = new VImg(parts[85]);
		aux.icon[0][ABI_EKILL] = new VImg(parts[110]);
		aux.icon[2][ATK_OMNI] = new VImg(parts[112]);
		aux.icon[1][P_IMUCURSE] = new VImg(parts[116]);
		aux.icon[1][P_WEAK] = new VImg(parts[195]);
		aux.icon[1][P_STRONG] = new VImg(parts[196]);
		aux.icon[1][P_STOP] = new VImg(parts[197]);
		aux.icon[1][P_SLOW] = new VImg(parts[198]);
		aux.icon[1][P_LETHAL] = new VImg(parts[199]);
		aux.icon[0][ABI_BASE] = new VImg(parts[200]);
		aux.icon[1][P_CRIT] = new VImg(parts[201]);
		aux.icon[0][ABI_ONLY] = new VImg(parts[202]);
		aux.icon[0][ABI_GOOD] = new VImg(parts[203]);
		aux.icon[0][ABI_RESIST] = new VImg(parts[204]);
		aux.icon[0][ABI_EARN] = new VImg(parts[205]);
		aux.icon[0][ABI_MASSIVE] = new VImg(parts[206]);
		aux.icon[1][P_KB] = new VImg(parts[207]);
		aux.icon[1][P_WAVE] = new VImg(parts[208]);
		aux.icon[0][ABI_METALIC] = new VImg(parts[209]);
		aux.icon[1][P_IMUWAVE] = new VImg(parts[210]);
		aux.icon[2][ATK_AREA] = new VImg(parts[211]);
		aux.icon[2][ATK_LD] = new VImg(parts[212]);
		aux.icon[1][P_IMUWEAK] = new VImg(parts[213]);
		aux.icon[1][P_IMUSTOP] = new VImg(parts[214]);
		aux.icon[1][P_IMUSLOW] = new VImg(parts[215]);
		aux.icon[1][P_IMUKB] = new VImg(parts[216]);
		aux.icon[2][ATK_SINGLE] = new VImg(parts[217]);
		aux.icon[0][ABI_WAVES] = new VImg(parts[218]);
		aux.icon[0][ABI_WKILL] = new VImg(parts[258]);
		aux.icon[0][ABI_RESISTS] = new VImg(parts[122]);
		aux.icon[0][ABI_MASSIVES] = new VImg(parts[114]);
		aux.icon[0][ABI_ZKILL] = new VImg(parts[260]);
		aux.icon[1][P_IMUWARP] = new VImg(parts[262]);
		aux.icon[1][P_BREAK] = new VImg(parts[264]);
		aux.icon[1][P_WARP] = new VImg(parts[266]);
		aux.icon[1][P_SATK] = new VImg(parts[229]);
		aux.icon[1][P_IMUATK] = new VImg(parts[231]);
		aux.icon[1][P_VOLC] = new VImg(parts[239]);
		aux.icon[1][P_IMUPOIATK] = new VImg(parts[237]);
		aux.icon[1][P_IMUVOLC] = new VImg(parts[243]);
		aux.icon[1][P_CURSE] = new VImg(parts[289]);
		aux.icon[1][P_MINIWAVE] = new VImg(parts[293]);
		aux.icon[1][P_SHIELDBREAK] = new VImg(parts[296]);
		aux.icon[1][P_IMUSUMMON] = new VImg("./org/page/icons/Summon.png"); // TODO - Add icons for Imu.Summon, DMGCUT, DMGCAP, Imu.Armor Break, and Imu.Haste
		aux.icon[1][P_DMGCUT] = new VImg(parts[231]);
		aux.icon[1][P_DMGCAP] = new VImg(parts[231]);

		//These are used for talent edit page icons
		aux.icon[4][PC2_HP] = new VImg(parts[120]);
		aux.icon[4][PC2_ATK] = new VImg(parts[118]);
		aux.icon[4][PC2_SPEED] = new VImg(parts[96]);
		aux.icon[4][PC2_COST] = new VImg(parts[92]);
		aux.icon[4][PC2_CD] = new VImg(parts[94]);
		aux.icon[4][PC2_HB] = new VImg(parts[98]);

		aux.icon[0][ABI_THEMEI] = new VImg("./org/page/icons/ThemeX.png");
		aux.icon[0][ABI_TIMEI] = new VImg("./org/page/icons/TimeX.png");
		aux.icon[0][ABI_IMUSW] = new VImg("./org/page/icons/BossWaveX.png");
		aux.icon[0][ABI_SNIPERI] = new VImg("./org/page/icons/SnipeX.png");
		aux.icon[1][P_IMUPOI] = new VImg("./org/page/icons/PoisonX.png");
		aux.icon[1][P_IMUSEAL] = new VImg("./org/page/icons/SealX.png");
		aux.icon[0][ABI_GHOST] = new VImg("./org/page/icons/Ghost.png");
		aux.icon[1][P_THEME] = new VImg("./org/page/icons/Theme.png");
		aux.icon[1][P_TIME] = new VImg("./org/page/icons/Time.png");
		aux.icon[1][P_BOSS] = new VImg("./org/page/icons/BossWave.png");
		aux.icon[1][P_SNIPER] = new VImg("./org/page/icons/Snipe.png");
		aux.icon[1][P_POISON] = new VImg("./org/page/icons/Poison.png");
		aux.icon[1][P_SEAL] = new VImg("./org/page/icons/Seal.png");
		aux.icon[1][P_MOVEWAVE] = new VImg("./org/page/icons/Moving.png");
		aux.icon[1][P_SUMMON] = new VImg("./org/page/icons/Summon.png");
		aux.icon[1][P_IMUMOVING] = new VImg("./org/page/icons/MovingX.png");
		aux.icon[1][P_IMUARMOR] = new VImg("./org/page/icons/ArmorBreak.png"); // Needs Icon
		aux.icon[1][P_IMUSPEED] = new VImg("./org/page/icons/Speed.png"); // Needs Icon
		aux.icon[0][ABI_GLASS] = new VImg("./org/page/icons/Suicide.png");
		aux.icon[1][P_BURROW] = new VImg("./org/page/icons/Burrow.png");
		aux.icon[1][P_REVIVE] = new VImg("./org/page/icons/Revive.png");
		aux.icon[1][P_CRITI] = new VImg("./org/page/icons/CritX.png");
		aux.icon[3][TRAIT_WHITE] = new VImg("./org/page/icons/White.png");
		aux.icon[1][P_POIATK] = new VImg("./org/page/icons/BCPoison.png");
		aux.icon[1][P_ARMOR] = new VImg("./org/page/icons/ArmorBreak.png");
		aux.icon[1][P_SPEED] = new VImg("./org/page/icons/Speed.png");
		CommonStatic.getConfig().icon = false;
	}

	private static void readBattle() {
		BCAuxAssets aux = CommonStatic.getBCAssets();
		aux.battle[0] = new VImg[4];
		aux.battle[1] = new VImg[22];
		aux.battle[2] = new VImg[5];
		ImgCut ic001 = ImgCut.newIns("./org/page/img001.imgcut");
		VImg img001 = new VImg("./org/page/img001.png");
		FakeImage[] parts = ic001.cut(img001.getImg());
		int[] vals = new int[] { 5, 19, 30, 40, 51, 62, 73, 88, 115 };
		int[] adds = new int[] { 1, 2, 2, 0, 0, 1, 1, 1, 0 };
		aux.num[5] = new VImg[12];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 10; j++)
				aux.num[i][j] = new VImg(parts[vals[i] - 5 + j]);
			if (adds[i] == 1)
				aux.num[i][10] = new VImg(parts[vals[i] + 5]);
			if (adds[i] == 2)
				aux.num[i][10] = new VImg(parts[vals[i] - 6]);
			if (i == 5)
				aux.num[i][11] = new VImg(parts[108]);
		}
		aux.battle[0][3] = new VImg(parts[81]);

		for(int i = 0; i < aux.timer.length; i++)
			aux.timer[i] = new VImg(parts[i + 83]);

		ImgCut ic002 = ImgCut.newIns("./org/page/img002.imgcut");
		VImg img002 = new VImg("./org/page/img002.png");

		parts = ic002.cut(img002.getImg());
		aux.battle[0][0] = new VImg(parts[5]);
		aux.battle[0][1] = new VImg(parts[24]);
		aux.battle[0][2] = new VImg(parts[6]);
		aux.battle[1][0] = new VImg(parts[8]);
		aux.battle[1][1] = new VImg(parts[7]);
		for (int i = 0; i < 10; i++)
			aux.battle[1][2 + i] = new VImg(parts[11 + i]);

		//jp fire
		aux.battle[1][12] = new VImg(parts[9]);
		aux.battle[1][13] = new VImg(parts[10]);

		ic002 = ImgCut.newIns("./org/page/img002_en.imgcut");
		img002 = new VImg("./org/page/img002_en.png");
		parts = ic002.cut(img002.getImg());
		//en fire
		aux.battle[1][14] = new VImg(parts[9]);
		aux.battle[1][15] = new VImg(parts[10]);

		ic002 = ImgCut.newIns("./org/page/img002_ko.imgcut");
		img002 = new VImg("./org/page/img002_ko.png");
		parts = ic002.cut(img002.getImg());
		//kr fire
		aux.battle[1][16] = new VImg(parts[9]);
		aux.battle[1][17] = new VImg(parts[10]);

		ic002 = ImgCut.newIns("./org/page/img002_tw.imgcut");
		img002 = new VImg("./org/page/img002_tw.png");
		parts = ic002.cut(img002.getImg());
		//tw fire
		aux.battle[1][18] = new VImg(parts[9]);
		aux.battle[1][19] = new VImg(parts[10]);

		aux.battle[1][20] = new VImg(parts[0]);
		aux.battle[1][21] = new VImg(parts[1]);
		aux.battle[2][0] = new VImg(parts[27]);
		aux.battle[2][1] = new VImg(parts[29]);
		aux.battle[2][2] = new VImg(parts[32]);
		aux.battle[2][3] = new VImg(parts[33]);
		aux.battle[2][4] = new VImg(parts[38]);
		// money, lv, lv dark,cost,cost dark,hp, money light,time,point
	}

}
