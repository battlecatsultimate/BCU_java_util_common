package common.util.stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import common.io.InStream;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.io.json.JsonField.GenType;
import common.pack.PackData.Identifier;
import common.pack.PackData.UserPack;
import common.pack.VerFixer.VerFixerException;
import common.system.BasedCopable;
import common.system.files.VFile;
import common.util.BattleStatic;
import common.util.Data;
import common.util.lang.MultiLangCont;
import common.util.pack.Background;
import common.util.stage.SCDef.Line;
import common.util.unit.Enemy;
import common.CommonStatic;

@JsonClass
public class Stage extends Data implements BasedCopable<Stage, StageMap>, BattleStatic {

	public static class DefStage extends Stage {

		public StageInfo info;

		@SuppressWarnings("deprecation")
		protected DefStage(StageMap sm, int id, VFile<?> f, int type) {
			super(sm);
			if (sm.info != null)
				sm.info.getData(this);
			Queue<String> qs = f.getData().readLine();
			name = "" + id;
			String temp;
			if (type == 0) {
				temp = qs.poll();
				String[] strs = temp.split(",");
				int cas = CommonStatic.parseIntN(strs[0]);
				if (map.cast != -1)
					cas += map.cast * 1000;
				castle = Identifier.parseInt(cas, CastleImg.class);
				non_con = strs[1].equals("1");
			} else {
				castle = null;
				non_con = false;
			}
			int intl = type == 2 ? 9 : 10;
			String[] strs = qs.poll().split(",");
			len = Integer.parseInt(strs[0]);
			health = Integer.parseInt(strs[1]);
			bg = Identifier.parseInt(Integer.parseInt(strs[4]), Background.class);
			max = Integer.parseInt(strs[5]);
			int isBase = Integer.parseInt(strs[6]) - 2;
			List<int[]> ll = new ArrayList<>();

			while (qs.size() > 0)
				if ((temp = qs.poll()).length() > 0) {
					if (!Character.isDigit(temp.charAt(0)))
						break;
					if (temp.startsWith("0,"))
						break;
					String[] ss = temp.split(",");
					int[] data = new int[SCDef.SIZE];
					for (int i = 0; i < intl; i++)
						data[i] = Integer.parseInt(ss[i]);
					data[0] -= 2;
					data[2] *= 2;
					data[3] *= 2;
					data[4] *= 2;
					if (intl > 9 && data[5] > 100 && data[9] == 100) {
						data[9] = data[5];
						data[5] = 100;
					}
					if (ss.length > 11 && CommonStatic.isInteger(ss[11]))
						data[SCDef.M1] = Integer.parseInt(ss[11]);
					else
						data[SCDef.M1] = data[SCDef.M];

					if (data[0] == isBase)
						data[SCDef.C0] = 0;
					ll.add(data);
				}
			SCDef scd = new SCDef(ll.size());
			for (int i = 0; i < ll.size(); i++)
				scd.datas[i] = new Line(ll.get(scd.datas.length - i - 1));
			if (strs.length > 6) {
				int ano = CommonStatic.parseIntN(strs[6]);
				if (ano == 317)
					scd.datas[ll.size() - 1].castle_0 = 0;
			}
			data = scd;
			validate();
		}

	}

	public static class PackStage extends Stage {

		public List<Recd> recd = new ArrayList<>();

		public PackStage(StageMap sm) {
			super(sm);
			len = 3000;
			health = 60000;
			max = 8;
			name = "stage " + sm.list.size();
			lim = new Limit();
			data = new SCDef(0);
		}

		@Deprecated
		public PackStage(UserPack pack, StageMap sm, InStream is) throws VerFixerException {
			super(sm);
			int val = getVer(is.nextString());
			if (val != 409)
				throw new VerFixerException("stage version has to be 409, got" + val);
			name = is.nextString();
			bg = Identifier.parseInt(is.nextInt(), Background.class);
			castle = Identifier.parseInt(is.nextInt(), CastleImg.class);
			health = is.nextInt();
			len = is.nextInt();
			mus0 = Identifier.parseInt(is.nextInt(), Music.class);
			mush = is.nextInt();
			mus1 = Identifier.parseInt(is.nextInt(), Music.class);
			loop0 = is.nextLong();
			loop1 = is.nextLong();
			max = is.nextByte();
			non_con = is.nextByte() == 1;
			data = SCDef.zread(is.subStream());
			lim = new Limit.PackLimit(pack, is);
			int t = is.nextInt();
			for (int i = 0; i < t; i++) {
				String name = is.nextString();
				Recd.getRecd(this, is.subStream(), name);
			}
			validate();
		}

		public void setName(String str) {
			// TODO validate str = MainBCU.validate(str);
			while (!checkName(str))
				str += "'";
			name = str;
		}

		private boolean checkName(String str) {
			for (Stage st : map.list)
				if (st != this && st.name.equals(str))
					return false;
			return true;
		}

	}

	public static class StageInfo {

		public final Stage st;
		public final StageMap.StageMapInfo map;
		public final int energy, xp, once, rand;
		public final int[][] drop;
		public final int[][] time;
		public int diff = -1;

		protected StageInfo(StageMap.StageMapInfo info, Stage s, int[] data) {
			map = info;
			st = s;

			energy = data[0];
			xp = data[1];
			s.mus0 = Identifier.parseInt(data[2], Music.class);
			s.mush = data[3];
			s.mus1 = Identifier.parseInt(data[4], Music.class);

			once = data[data.length - 1];
			boolean isTime = data.length > 15;
			if (isTime)
				for (int i = 8; i < 15; i++)
					if (data[i] != -2)
						isTime = false;
			if (isTime) {
				time = new int[(data.length - 17) / 3][3];
				for (int i = 0; i < time.length; i++)
					for (int j = 0; j < 3; j++)
						time[i][j] = data[16 + i * 3 + j];
			} else
				time = new int[0][3];
			boolean isMulti = !isTime && data.length > 9;
			if (data.length == 6) {
				drop = new int[0][];
				rand = 0;
			} else if (!isMulti) {
				drop = new int[1][];
				rand = 0;
			} else {
				drop = new int[(data.length - 7) / 3][3];
				rand = data[8];
				for (int i = 1; i < drop.length; i++)
					for (int j = 0; j < 3; j++)
						drop[i][j] = data[6 + i * 3 + j];
			}
			if (drop.length > 0)
				drop[0] = new int[] { data[5], data[6], data[7] };
		}

		public String getHTML() {
			String ans = "<html>energy cost: " + energy + "<br> xp: " + xp + "<br> drop rewards: ";
			if (drop.length == 0)
				ans += "none";
			else if (drop.length == 1)
				ans += "{chance: " + drop[0][0] + "%, item ID: " + drop[0][1] + ", number: " + drop[0][2] + "}, once: "
						+ once;
			else {
				ans += "count: " + drop.length + ", rand mode: " + rand + ", once: " + once + "<br>";
				ans += "<table><tr><th>chance</th><th>item ID</th><th>number</th></tr>";
				for (int[] dp : drop)
					ans += "<tr><td>" + dp[0] + "%</td><td>" + dp[1] + "</td><td>" + dp[2] + "</td><tr>";
				ans += "</table>";
			}
			if (time.length > 0) {
				ans += "<br> time scores: count: " + time.length + "<br>";
				ans += "<table><tr><th>score</th><th>item ID</th><th>number</th></tr>";
				for (int[] tm : time)
					ans += "<tr><td>" + tm[0] + "</td><td>" + tm[1] + "</td><td>" + tm[2] + "</td><tr>";
				ans += "</table>";
			}
			return ans;
		}

	}

	public final StageMap map;

	@JsonField
	public String name = "";
	@JsonField
	public boolean non_con, trail;
	@JsonField
	public int len, health, max, mush;
	public Identifier<CastleImg> castle;
	public Identifier<Background> bg;
	public Identifier<Music> mus0, mus1;
	@JsonField
	public long loop0, loop1;
	@JsonField
	public SCDef data;
	@JsonField(gen = GenType.GEN)
	public Limit lim;

	private Stage(StageMap sm) {
		map = sm;
	}

	public boolean contains(Enemy e) {
		return data.contains(e);
	}

	@Override
	public PackStage copy(StageMap sm) {
		PackStage ans = new PackStage(sm);
		ans.len = len;
		ans.health = health;
		ans.max = max;
		ans.bg = bg.clone();
		ans.castle = castle.clone();
		ans.name = name;
		ans.data = data.copy();
		if (lim != null)
			ans.lim = lim.clone();
		ans.mus0 = mus0.clone();
		ans.mus1 = mus1.clone();
		ans.mush = mush;
		return ans;
	}

	public Limit getLim(int star) {
		Limit tl = new Limit();
		if (lim != null && (lim.star == -1 || lim.star == star))
			tl.combine(lim);
		for (Limit l : map.lim)
			if (l.star == -1 || l.star == star)
				if (l.sid == -1 || l.sid == id())
					tl.combine(l);
		return tl;
	}

	public int id() {
		return map.list.indexOf(this);
	}

	@Override
	public String toString() {
		String desp = MultiLangCont.get(this);
		if (desp != null && desp.length() > 0)
			return desp;
		if (name.length() > 0)
			return name;
		return map + " - " + id();
	}

	protected void validate() {
		trail = data.isTrail();
	}

}
