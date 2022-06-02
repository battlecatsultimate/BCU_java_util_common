package common.util.stage;

import common.CommonStatic;
import common.io.assets.Admin.StaticPermitted;
import common.io.json.Dependency;
import common.io.json.JsonClass;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.pack.IndexContainer;
import common.pack.PackData.PackDesc;
import common.pack.Source.ResourceLocation;
import common.pack.UserProfile;
import common.system.BasedCopable;
import common.system.files.VFile;
import common.util.BattleStatic;
import common.util.Data;
import common.util.lang.MultiLangCont;
import common.util.lang.MultiLangData;
import common.util.pack.Background;
import common.util.stage.SCDef.Line;
import common.util.stage.info.DefStageInfo;
import common.util.stage.info.StageInfo;
import common.util.unit.Enemy;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

@IndexContainer.IndexCont(StageMap.class)
@JsonClass.JCGeneric(Identifier.class)
@JsonClass(noTag = NoTag.LOAD)
public class Stage extends Data
		implements BasedCopable<Stage, StageMap>, BattleStatic, IndexContainer.Indexable<StageMap, Stage> {

	@StaticPermitted
	public static final MapColc CLIPMC = new MapColc.ClipMapColc();
	@StaticPermitted
	public static final StageMap CLIPSM = CLIPMC.maps.get(0);
	@StaticPermitted
	private static final int[] CH_CASTLES = { 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28,
			27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 46,
			47, 45, 47, 47, 45, 45 };

	public StageInfo info;
	@JsonField(block = true)
	public boolean isBCstage = false;

	@JsonClass.JCIdentifier
	public final Identifier<Stage> id;

	@JsonField(io = JsonField.IOType.R)
	public String name = "";
	@JsonField(generic = MultiLangData.class)
	public MultiLangData names = new MultiLangData();

	public boolean non_con, trail;
	public int len, health, max, mush, bgh;
	public int timeLimit = 0;
	public int minSpawn = 1, maxSpawn = 1;
	public Identifier<CastleImg> castle;
	public Identifier<Background> bg, bg1;
	public Identifier<Music> mus0, mus1;
	public SCDef data;
	public Limit lim;
	@JsonField(generic = Replay.class, alias = ResourceLocation.class)
	public ArrayList<Replay> recd = new ArrayList<>();

	@JsonClass.JCConstructor
	public Stage() {
		id = null;
	}

	public Stage(Identifier<Stage> id) {
		this.id = id;
		len = 3000;
		health = 60000;
		max = 8;
		names.put("stage " + getCont().list.size());
		lim = new Limit();
		data = new SCDef(0);
	}

	public Stage(StageMap sm) {
		this.id = sm.getNextID();
		len = 3000;
		health = 60000;
		max = 8;
		names.put("stage " + sm.list.size());
		lim = new Limit();
		data = new SCDef(0);
	}

	@SuppressWarnings("deprecation")
	protected Stage(Identifier<Stage> id, VFile f, int type) {
		this.id = id;
		isBCstage = true;
		StageMap sm = getCont();
		if (sm.info != null)
			sm.info.getData(this);
		Queue<String> qs = f.getData().readLine();
		names.put("" + id);
		String temp;
		if (type == 0) {
			temp = qs.poll();
			String[] strs = temp.split(",");
			int cas = CommonStatic.parseIntN(strs[0]);
			if (cas == -1)
				cas = CH_CASTLES[id.id];
			if (sm.cast != -1)
				cas = sm.cast * 1000 + cas;
			castle = Identifier.parseInt(cas, CastleImg.class);
			non_con = strs[1].equals("1");

			if(info != null)
				((DefStageInfo)info).setData(strs);
		} else {
			castle = Identifier.parseInt(sm.cast * 1000 + CH_CASTLES[id.id], CastleImg.class);
			non_con = false;
		}
		int intl = type == 2 ? 9 : 10;
		String[] strs = qs.poll().split(",");
		len = Integer.parseInt(strs[0]);
		health = Integer.parseInt(strs[1]);
		minSpawn = Integer.parseInt(strs[2]);
		maxSpawn = Integer.parseInt(strs[3]);
		bg = Identifier.rawParseInt(Integer.parseInt(strs[4]), Background.class);
		max = Integer.parseInt(strs[5]);
		timeLimit = strs.length >= 8 ? Math.max(Integer.parseInt(strs[7]), 0) : 0;
		if(timeLimit != 0)
			health = Integer.MAX_VALUE;
		trail = timeLimit != 0;
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
					if(i < ss.length)
						data[i] = Integer.parseInt(ss[i]);
					else
						//Handle missing value manually
						if(i == 9)
							data[i] = 100;
				data[0] -= 2;
				data[2] *= 2;
				data[3] *= 2;
				data[4] *= 2;
				if (timeLimit == 0 && intl > 9 && data[5] > 100 && data[9] == 100) {
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

		int ano = CommonStatic.parseIntN(strs[6]);
		if (ano == 317)
			scd.datas[ll.size() - 1].castle_0 = 0;
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
		if (bg != null)
			ans.bg = bg.clone();
		if (bg1 != null)
			ans.bg1 = bg1.clone();
		if (castle != null)
			ans.castle = castle.clone();
		ans.names.put(toString());
		ans.data = data.copy();
		ans.lim = lim != null ? lim.clone() : getLim(0);
		ans.non_con = non_con;
		if (mus0 != null)
			ans.mus0 = mus0.clone();
		if (mus1 != null)
			ans.mus1 = mus1.clone();
		ans.bgh = bgh;
		ans.mush = mush;
		return ans;
	}

	@Override
	public Identifier<Stage> getID() {
		return id;
	}

	public Limit getLim(int star) {
		Limit tl = new Limit();
		if (lim != null && (lim.star == -1 || lim.star == star))
			tl.combine(lim);
		for (Limit l : getCont().lim)
			if (l.star == -1 || l.star == star)
				if (l.sid == -1 || l.sid == id())
					tl.combine(l);
		return tl;
	}

	public int id() {
		return getCont().list.indexOf(this);
	}

	public Set<String> isSuitable(String pack) {
		Dependency dep = Dependency.collect(this);
		PackDesc desc = UserProfile.getUserPack(pack).desc;
		Set<String> set = dep.getPacks();
		System.out.println("Stage: " + set);// FIXME
		set.remove(Identifier.DEF);
		set.remove(pack);
		for (String str : desc.dependency)
			set.remove(str);
		return set;
	}

	public void setNames(String str) {
		while (!checkName(str))
			str += "'";
		names.put(str);
	}

	@Override
	public String toString() {
		String desp = MultiLangCont.get(this);
		if (desp != null && desp.length() > 0)
			return desp;
		String n = names.toString();
		if (n.length() > 0)
			return n;
		return map + " - " + id();
	}

	public boolean isAkuStage() {
		if(data.datas.length == 0)
			return false;

		Line line = data.datas[data.datas.length - 1];

		if(line.enemy == null)
			return false;

		return line.enemy.id == 574 && line.enemy.pack.equals(Identifier.DEF) && line.castle_0 == 0;
	}

	protected void validate() {
		if(trail)
			return;

		if(getCont() == null || getCont().getCont() == null)
			return;

		if(getCont().getCont().getSID().equals("000006") || getCont().getCont().getSID().equals("000011"))
			trail = data.isTrail();
	}

	private boolean checkName(String str) {
		for (Stage st : getCont().list)
			if (st != this && st.names.toString().equals(str))
				return false;
		return true;
	}
}
