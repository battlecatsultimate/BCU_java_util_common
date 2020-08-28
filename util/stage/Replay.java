package common.util.stage;

import common.CommonStatic;
import common.battle.BasisLU;
import common.battle.Treasure;
import common.io.DataIO;
import common.io.InStream;
import common.io.PackLoader;
import common.io.json.JsonClass;
import common.io.json.JsonDecoder;
import common.io.json.JsonEncoder;
import common.io.json.JsonClass.JCConstructor;
import common.io.json.JsonField.IOType;
import common.io.json.JsonField;
import common.pack.Context;
import common.pack.PackData;
import common.pack.Source;
import common.pack.PackData.UserPack;
import common.pack.Source.ResourceLocation;
import common.pack.UserProfile;
import common.pack.Context.ErrType;
import common.util.Data;
import common.util.stage.MapColc.DefMapColc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Map;

import org.intellij.lang.annotations.Identifier;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@JsonClass
@JsonClass.JCGeneric(ResourceLocation.class)
public class Replay extends Data {

	public static Map<String, Replay> getMap() {
		return UserProfile.getRegister("Replay_local", Replay.class);
	}

	public static void getRecd(Stage stage, InStream is, String str) {
		getRecd(is, new ResourceLocation(stage.getCont().getCont().getSID(), str));
	}

	public static void read() {
		File fold = CommonStatic.def.route("./replay/");
		if (fold.exists()) {
			File[] fs = fold.listFiles();
			for (File fi : fs) {
				String str = fi.getName();
				if (str.endsWith(".replay")) {
					String name = str.substring(0, str.length() - 7);
					InStream is = CommonStatic.def.readBytes(fi);
					Replay rec = getRecd(is, new ResourceLocation(ResourceLocation.LOCAL, name));
					getMap().put(name, rec);
				}
			}
		}
		File f = CommonStatic.ctx.getWorkspaceFile("./_local/" + Source.REPLAY);
		if (f.exists())
			for (File fi : f.listFiles())
				if (fi.getName().endsWith(".replay"))
					try {
						FileInputStream fis = new FileInputStream(fi);
						byte[] header = new byte[PackLoader.HEAD_DATA.length];
						fis.read(header);
						if (!Arrays.equals(header, PackLoader.HEAD_DATA)) {
							byte[] len = new byte[4];
							fis.read(len);
							int size = DataIO.toInt(DataIO.translate(len), 0);
							byte[] json = new byte[size];
							fis.read(json);
							JsonElement elem = JsonParser.parseString(new String(json));
							Replay rep = JsonDecoder.decode(elem, Replay.class);
							fis.read(len);
							size = DataIO.toInt(DataIO.translate(len), 0);
							int[] data = new int[size];
							for (int i = 0; i < size; i++) {
								fis.read(len);
								data[i] = DataIO.toInt(DataIO.translate(len), 0);
							}
							rep.action = data;
							getMap().put(rep.rl.id, rep);
						} else {
							CommonStatic.ctx.printErr(ErrType.WARN, "invalid file " + fi.getName());
						}
						fis.close();
					} catch (Exception e) {
						CommonStatic.ctx.noticeErr(e, ErrType.WARN, "failed to load replay " + fi.getName());
					}
	}

	private static Replay getRecd(InStream is, ResourceLocation name) {
		int val = getVer(is.nextString());
		if (val >= 401)
			return zread$000401(is, name);
		return null;
	}

	private static Replay zread$000401(InStream is, ResourceLocation name) {
		long seed = is.nextLong();
		int[] conf = is.nextIntsB();
		int star = is.nextInt();
		BasisLU lu = BasisLU.zread(is.subStream());
		InStream action = is.subStream();
		int pid = is.nextInt();
		Stage st = null;
		if (pid == 0) {
			int id = is.nextInt();
			StageMap sm = DefMapColc.getMap(id / 1000);
			st = sm.list.get(id % 1000);
			if (st == null) {
				return null;
			}
		} else {
			st = zreads$000401(is, pid);
		}

		Replay ans = new Replay(lu, st, star, conf, seed);
		int[] act = new int[action.nextInt()];
		for (int i = 0; i < act.length; i++)
			act[i] = action.nextInt();
		ans.action = act;
		ans.rl = name;
		return ans;
	}

	private static Stage zreads$000401(InStream is, int pid) {
		String mcn = is.nextString();
		String smid = is.nextString();
		String stid = is.nextString();
		PackData pack = UserProfile.getPack(Data.hex(pid));
		if (pid != 0 && pack == null) {
			return null;
		}
		MapColc mc = null;
		if (pid == 0)
			mc = DefMapColc.getMap(mcn);
		else
			mc = ((UserPack) pack).mc;
		StageMap sm = null;
		for (StageMap map : mc.maps)
			if (map.name.equals(smid))
				sm = map;
		if (sm == null) {
			return null;
		}
		Stage st = null;
		for (Stage s : sm.list)
			if (s.name.equals(stid))
				st = s;
		return st;
	}

	@JsonClass.JCIdentifier
	@JsonField
	public ResourceLocation rl;
	@JsonField
	public long seed;
	@JsonField
	public int[] conf;
	@JsonField
	public int star, len;
	@JsonField(alias = Identifier.class)
	public Stage st;
	@JsonField
	public BasisLU lu;
	public int[] action;
	public boolean avail, marked;

	@JCConstructor
	@Deprecated
	public Replay() {

	}

	public Replay(BasisLU blu, Stage sta, int stars, int[] con, long se) {
		lu = blu;
		st = sta;
		star = stars;
		conf = con;
		seed = se;
		avail = st != null;
	}

	@Override
	public Replay clone() {
		return new Replay(lu.copy(), st, star, conf.clone(), seed);
	}

	public int getLen() {
		if (len > 0)
			return len;
		for (int i = 0; i < action.length / 2; i++) {
			len += action[i * 2 + 1];
		}
		return len;
	}

	@Override
	public String toString() {
		return rl.id;
	}

	public void write() {
		File tar = CommonStatic.ctx.getWorkspaceFile(rl.getPath(Source.REPLAY) + ".replay");
		File tmp = CommonStatic.ctx.getWorkspaceFile(rl.getPath(Source.REPLAY) + ".replay.temp");
		try {
			Context.check(tmp);
			if (tar.exists())
				Context.delete(tar);
			FileOutputStream fos = new FileOutputStream(tmp);
			fos.write(PackLoader.HEAD_DATA);
			byte[] head = JsonEncoder.encode(this).toString().getBytes();
			byte[] len = new byte[4];
			DataIO.fromInt(len, 0, head.length);
			fos.write(len);
			fos.write(head);
			byte[] data = new byte[action.length * 4 + 4];
			DataIO.fromInt(data, 0, action.length);
			for (int i = 0; i < action.length; i++)
				DataIO.fromInt(data, 4 + 4 * i, action[i]);
			fos.write(data);
			fos.close();
			tmp.renameTo(tar);
		} catch (Exception e) {
			CommonStatic.ctx.noticeErr(e, ErrType.WARN, "failed to save replay " + rl);
		}
	}

	@JsonField(tag = "treasure", io = IOType.W)
	public Treasure zgen() {
		return lu.t();
	}

	@JsonField(tag = "treasure", io = IOType.R)
	public void zser(JsonElement e) {
		Data.err(() -> JsonDecoder.inject(e, Treasure.class, lu.t()));
	}

}
