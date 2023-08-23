package common.util.stage;

import common.battle.data.MaskUnit;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.pack.UserProfile;
import common.util.BattleStatic;
import common.util.Data;
import common.util.stage.MapColc.DefMapColc;
import common.util.unit.Unit;

@JsonClass
public class Limit extends Data implements BattleStatic {

	public static class DefLimit extends Limit {

		public DefLimit(String[] strs) {
			int mid = Integer.parseInt(strs[0]);

			if(mid == 22000) {
				mid = 3015;
			} else if (mid == 22001) {
				mid = 3016;
			}

			StageMap map = DefMapColc.getMap(mid);
			map.lim.add(this);
			star = Integer.parseInt(strs[1]);
			sid = Integer.parseInt(strs[2]);
			rare = Integer.parseInt(strs[3]);
			num = Integer.parseInt(strs[4]);
			line = Integer.parseInt(strs[5]);
			min = Integer.parseInt(strs[6]);
			max = Integer.parseInt(strs[7]);
			group = UserProfile.getBCData().groups.getRaw(Integer.parseInt(strs[8]));
		}

	}

	@JsonClass
	public static class PackLimit extends Limit {

		@JsonField
		public String name = "";

		public PackLimit() {
		}
	}

	@JsonField
	public int star = -1, sid = -1;
	@JsonField
	public int rare, num, line, min, max;
	@JsonField(alias = Identifier.class)
	public CharaGroup group;
	@JsonField(alias = Identifier.class)
	public LvRestrict lvr;

	/**
	 * for copy or combine only
	 */
	public Limit() {
	}

	@Override
	public Limit clone() {
		Limit l = new Limit();
		l.star = star;
		l.sid = sid;
		l.rare = rare;
		l.num = num;
		l.line = line;
		l.min = min;
		l.max = max;
		l.group = group;
		l.lvr = lvr;
		return l;
	}

	public void combine(Limit l) {
		if (rare == 0)
			rare = l.rare;
		else if (l.rare != 0)
			rare &= l.rare;
		if (num * l.num > 0)
			num = Math.min(num, l.num);
		else
			num = Math.max(num, l.num);
		line |= l.line;
		min = Math.max(min, l.min);
		max = max > 0 && l.max > 0 ? Math.min(max, l.max) : (max + l.max);
		if (l.group != null)
			if (group != null)
				group = group.combine(l.group);
			else
				group = l.group;
		if (l.lvr != null)
			if (lvr != null)
				lvr.combine(l.lvr);
			else
				lvr = l.lvr;
	}

	public boolean unusable(MaskUnit du, int price) {
		double cost = du.getPrice() * (1 + price * 0.5);
		if ((min > 0 && cost < min) || (max > 0 && cost > max))
			return true;
		Unit u = du.getPack().unit;
		if (rare != 0 && ((rare >> u.rarity) & 1) == 0)
			return true;
		return group != null && !group.allow(u);
	}

	@Override
	public String toString() {
		return (sid == -1 ? "all stages" : ("" + sid)) + " - " + (star == -1 ? "all stars" : (star + 1) + " star");
	}

}
