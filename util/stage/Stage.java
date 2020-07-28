package common.util.stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import common.io.InStream;
import common.io.OutStream;
import common.system.BasedCopable;
import common.system.MultiLangCont;
import common.system.files.AssetData;
import common.system.files.VFile;
import common.util.BattleStatic;
import common.util.Data;
import common.util.pack.Pack;
import common.util.unit.Enemy;
import common.CommonStatic;
import main.MainBCU;

public class Stage extends Data implements BasedCopable<Stage, StageMap>, BattleStatic {

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
			s.mus0 = data[2];
			s.mush = data[3];
			s.mus1 = data[4];

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

	public static final MapColc clipmc = new MapColc("clip", -1);
	public static final StageMap clipsm = clipmc.maps[0];

	public final StageMap map;

	public StageInfo info;
	public String name = "";
	public boolean non_con, trail;
	public int len, health, max;
	public int bg, mus0 = -1, mush, mus1 = -1;
	public int castle;
	public SCDef data;
	public Limit lim;
	public List<Recd> recd = new ArrayList<>();

	public Stage(StageMap sm) {
		map = sm;
		len = 3000;
		health = 60000;
		max = 8;
		name = "stage " + sm.list.size();
		lim = new Limit();
		data = new SCDef(0);
	}

	public Stage(StageMap sm, String str, InStream is) {
		map = sm;
		zread(str, is);
	}

	protected Stage(StageMap sm, int id, VFile<AssetData> f, int type) {
		if (sm.info != null)
			sm.info.getData(this);
		Queue<String> qs = f.getData().readLine();
		name = "" + id;
		map = sm;
		String temp;
		if (type == 0) {
			temp = qs.poll();
			String[] strs = temp.split(",");
			castle = CommonStatic.parseIntN(strs[0]);
			non_con = strs[1].equals("1");
		} else {
			castle = -1;
			non_con = false;
		}
		int intl = type == 2 ? 9 : 10;
		String[] strs = qs.poll().split(",");
		len = Integer.parseInt(strs[0]);
		health = Integer.parseInt(strs[1]);
		bg = Integer.parseInt(strs[4]);
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
				if(ss.length > 11 && CommonStatic.isInteger(ss[11]))
					data[SCDef.M1] = Integer.parseInt(ss[11]);
				else
					data[SCDef.M1] = data[SCDef.M]; 
				
				if(data[0] == isBase)
					data[SCDef.C0] = 0;
				ll.add(data);
			}
		SCDef scd = new SCDef(ll.size());
		for (int i = 0; i < ll.size(); i++)
			scd.datas[i] = ll.get(scd.datas.length - i - 1);
		if (strs.length > 6) {
			int ano = CommonStatic.parseIntN(strs[6]);
			if (ano == 317)
				scd.datas[ll.size() - 1][5] = 0;
		}
		data = scd;
		validate();
	}

	public boolean contains(Enemy e) {
		return data.contains(e);
	}

	@Override
	public Stage copy(StageMap sm) {
		Stage ans = new Stage(sm);
		ans.len = len;
		ans.health = health;
		ans.max = max;
		ans.bg = bg;
		ans.castle = castle;
		ans.name = name;
		ans.data = data.copy();
		if (lim != null)
			ans.lim = lim.clone();
		ans.mus0 = mus0;
		ans.mus1 = mus1;
		ans.mush = mush;
		return ans;
	}

	public int getCastle() {
		int ans = castle;
		if (ans < 1000 && map.cast != -1)
			ans += map.cast * 1000;
		if (castle < 0 || Castles.getCastle(ans) == null)
			return 0;
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

	public boolean isSuitable(Pack p) {
		return data.isSuitable(p);
	}

	public void merge(int id, int pid, Pack p, int[][] inds) {
		if (castle / 1000 == pid)
			castle = inds[Pack.M_CS][castle % 1000] + id * 1000;
		if (bg / 1000 == pid)
			bg = inds[Pack.M_BG][bg % 1000] + id * 1000;
		if (mus0 / 1000 == pid)
			mus0 = inds[Pack.M_MS][mus0 % 1000] + id * 1000;
		if (mus1 / 1000 == pid)
			mus1 = inds[Pack.M_MS][mus1 % 1000] + id * 1000;
		data.merge(id, pid, inds[Pack.M_ES]);
		if (lim == null)
			return;
		if (lim.group != null && lim.group.pack == p)
			lim.group = map.mc.groups.get(inds[Pack.M_CG][lim.group.id]);
		if (lim.lvr != null && lim.lvr.pack == p)
			lim.lvr = map.mc.lvrs.get(inds[Pack.M_LR][lim.lvr.id]);
	}

	public int relyOn(int p) {
		if (mus0 / 1000 == p)
			return Pack.RELY_MUS;
		if (mus1 / 1000 == p)
			return Pack.RELY_MUS;
		if (getCastle() / 1000 == p)
			return Pack.RELY_CAS;
		if (bg / 1000 == p)
			return Pack.RELY_BG;
		Limit l = lim;
		if (l != null) {
			if (l.group != null && l.group.pack.id == p)
				return Pack.RELY_CG;
			if (l.lvr != null && l.lvr.pack.id == p)
				return Pack.RELY_LR;
		}
		int rel = data.relyOn(p);
		if (rel >= 0)
			return rel;
		return -1;
	}

	public void removePack(int p) {
		if (mus0 / 1000 == p)
			mus0 = -1;
		if (mus1 / 1000 == p)
			mus1 = -1;
		if (getCastle() / 1000 == p)
			castle = 0;
		if (bg / 1000 == p)
			bg = 0;
		if (lim != null) {
			if (lim.group != null && lim.group.pack.id == p)
				lim.group = null;
			if (lim.lvr != null && lim.lvr.pack.id == p)
				lim.lvr = null;
		}
		data.removePack(p);
	}

	public void setCast(int val) {
		castle = val;
	}

	public void setName(String str) {
		str = MainBCU.validate(str);
		while (!checkName(str))
			str += "'";
		name = str;
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

	public OutStream write() {
		OutStream os = OutStream.getIns();
		os.writeString("0.4.7");
		os.writeString(toString());
		os.writeInt(bg);
		os.writeInt(castle);
		os.writeInt(health);
		os.writeInt(len);
		os.writeInt(mus0);
		os.writeInt(mush);
		os.writeInt(mus1);
		os.writeByte((byte) max);
		os.writeByte((byte) (non_con ? 1 : 0));
		os.accept(data.write());
		lim.write(os);
		os.writeInt(recd.size());
		for (Recd r : recd) {
			os.writeString(r.name);
			os.accept(r.toOS());
		}
		os.terminate();
		return os;
	}

	protected void validate() {
		trail = data.isTrail();
	}

	private boolean checkName(String str) {
		for (Stage st : map.list)
			if (st != this && st.name.equals(str))
				return false;
		return true;
	}

	private void zread(String ver, InStream is) {
		int val = getVer(ver);
		if (val >= 407)
			zread$000407(val, is);
		else if (val >= 400)
			zread$000400(val, is);
		else if (val >= 308)
			zread$000308(val, is);
		else if (val >= 305)
			zread$000305(val, is);
		else if (val >= 203)
			zread$000203(is);
		validate();
	}

	private void zread$000203(InStream is) {
		name = is.nextString();
		bg = is.nextInt();
		castle = is.nextInt();
		health = is.nextInt();
		len = is.nextInt();
		max = is.nextByte();
		non_con = is.nextByte() == 1;
		data = new SCDef(is, 203);
		lim = new Limit(map.mc, 0, is);
	}

	private void zread$000305(int val, InStream is) {
		name = is.nextString();
		bg = is.nextInt();
		castle = is.nextInt();
		health = is.nextInt();
		len = is.nextInt();
		max = is.nextByte();
		non_con = is.nextByte() == 1;
		data = new SCDef(is, 305);
		lim = new Limit(map.mc, val, is);
	}

	private void zread$000308(int val, InStream is) {
		name = is.nextString();
		bg = is.nextInt();
		castle = is.nextInt();
		health = is.nextInt();
		len = is.nextInt();
		mus0 = is.nextInt();
		mush = is.nextInt();
		mus1 = is.nextInt();
		max = is.nextByte();
		non_con = is.nextByte() == 1;
		data = new SCDef(is, 305);
		lim = new Limit(map.mc, val, is);
	}

	private void zread$000400(int val, InStream is) {
		name = is.nextString();
		bg = is.nextInt();
		castle = is.nextInt();
		health = is.nextInt();
		len = is.nextInt();
		mus0 = is.nextInt();
		mush = is.nextInt();
		mus1 = is.nextInt();
		max = is.nextByte();
		non_con = is.nextByte() == 1;
		data = SCDef.zread(is.subStream());
		lim = new Limit(map.mc, val, is);
	}

	private void zread$000407(int val, InStream is) {
		zread$000400(val, is);
		int t = is.nextInt();
		for (int i = 0; i < t; i++) {
			String name = is.nextString();
			Recd.getRecd(this, is.subStream(), name);
		}
	}

}
