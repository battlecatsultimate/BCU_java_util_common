package common.util.stage;

import common.CommonStatic;
import common.CommonStatic.BCAuxAssets;
import common.battle.BasisLU;
import common.battle.BasisSet;
import common.system.files.VFile;
import common.util.stage.MapColc.DefMapColc;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class RandStage {

	public static BasisLU getLU(int att) {
		BCAuxAssets aux = CommonStatic.getBCAssets();
		double r = Math.random() * 100;
		for (int i = 0; i < 10; i++)
			if (r < aux.randRep[att][i])
				return BasisSet.current().sele.randomize(10 - i);
			else
				r -= aux.randRep[att][i];
		return BasisSet.current().sele;
	}

	public static Stage getStage(int sta) {
		DefMapColc mc = DefMapColc.getMap("N");
		if (sta == 47)
			return mc.maps.get(48).list.get(0);
		List<Stage> l = new ArrayList<Stage>();
		l.addAll(mc.maps.get(sta).list.getList());
		l.addAll(mc.maps.get(sta).list.getList());
		return l.get((int) (Math.random() * l.size()));
	}

	public static void read() {
		Queue<String> qs = VFile.readLine("./org/stage/D/RandomDungeon_000.csv");
		for (int i = 0; i < 5; i++)
			CommonStatic.getBCAssets().randRep[i] = CommonStatic.parseIntsN(qs.poll());
	}

}
