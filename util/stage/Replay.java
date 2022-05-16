package common.util.stage;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import common.CommonStatic;
import common.battle.BasisLU;
import common.battle.Treasure;
import common.io.DataIO;
import common.io.PackLoader;
import common.io.json.JsonClass;
import common.io.json.JsonClass.JCConstructor;
import common.io.json.JsonDecoder;
import common.io.json.JsonEncoder;
import common.io.json.JsonField;
import common.io.json.JsonField.IOType;
import common.pack.Context;
import common.pack.Context.ErrType;
import common.pack.Identifier;
import common.pack.Source;
import common.pack.Source.ResourceLocation;
import common.pack.Source.Workspace;
import common.pack.UserProfile;
import common.util.Data;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@JsonClass
@JsonClass.JCGeneric(ResourceLocation.class)
public class Replay extends Data {

	public static Map<String, Replay> getMap() {
		return UserProfile.getRegister("Replay_local", Replay.class);
	}

	public static void read() {
		File f = CommonStatic.ctx.getWorkspaceFile("./_local/" + Source.REPLAY);
		if (f.exists())
			for (File fi : f.listFiles())
				if (fi.getName().endsWith(".replay"))
					try {
						InputStream fis = new FileInputStream(fi);
						Replay rep = read(fis);
						fis.close();
						if (rep == null)
							CommonStatic.ctx.printErr(ErrType.WARN, "corrupted replay file " + fi.getName());
						else
							getMap().put(fi.getName().replace(".replay", ""), rep);
					} catch (Exception e) {
						CommonStatic.ctx.noticeErr(e, ErrType.WARN, "failed to load replay " + fi.getName());
					}
	}

	public static Replay read(InputStream fis) throws IOException {
		byte[] header = new byte[PackLoader.HEAD_DATA.length];
		fis.read(header);
		if (Arrays.equals(header, PackLoader.HEAD_DATA)) {
			byte[] len = new byte[4];
			fis.read(len);
			int size = DataIO.toInt(DataIO.translate(len), 0);
			byte[] json = new byte[size];
			fis.read(json);
			JsonElement elem = JsonParser.parseString(new String(json, StandardCharsets.UTF_8));
			Replay rep = JsonDecoder.decode(elem, Replay.class);
			fis.read(len);
			size = DataIO.toInt(DataIO.translate(len), 0);
			int[] data = new int[size];
			for (int i = 0; i < size; i++) {
				fis.read(len);
				data[i] = DataIO.toInt(DataIO.translate(len), 0);
			}
			rep.action = data;
			return rep;
		}
		return null;
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
	@JsonField
	public Identifier<Stage> st;
	@JsonField
	public BasisLU lu;
	@JsonField
	public boolean buttonDelay = false;
	public int[] action;
	public HashMap<Integer, double[]> sniperCoords;
	public boolean marked;// FIXME mark save

	@JCConstructor
	@Deprecated
	public Replay() {

	}

	public Replay(BasisLU blu, Identifier<Stage> sta, int stars, int[] con, long se, boolean buttonDelay) {
		lu = blu;
		st = sta;
		star = stars;
		conf = con;
		seed = se;
		this.buttonDelay = buttonDelay;
	}

	@Override
	public Replay clone() {
		return new Replay(lu.copy(), st, star, conf.clone(), seed, buttonDelay);
	}

	public int getLen() {
		if (len > 0)
			return len;
		for (int i = 0; i < action.length / 2; i++) {
			len += action[i * 2 + 1];
		}
		return len;
	}

	public void localize(String pack) {
		File src = CommonStatic.ctx.getWorkspaceFile(rl.getPath(Source.REPLAY) + ".replay");
		if (rl.pack.equals(ResourceLocation.LOCAL))
			getMap().remove(rl.id);
		rl.pack = pack;
		Workspace.validate(Source.REPLAY, rl);
		File dst = CommonStatic.ctx.getWorkspaceFile(rl.getPath(Source.REPLAY) + ".replay");
		Context.renameTo(src, dst);
	}

	public void rename(String str, Boolean putInMap) {
		if (rl == null) {
			rl = new ResourceLocation(ResourceLocation.LOCAL, str);
			Workspace.validate(Source.REPLAY, rl);
			write();
			if (putInMap)
				getMap().put(rl.id, this);
			return;
		}
		if (putInMap && rl.pack.equals(ResourceLocation.LOCAL))
			getMap().remove(rl.id);
		File src = CommonStatic.ctx.getWorkspaceFile(rl.getPath(Source.REPLAY) + ".replay");
		rl.id = str;
		File dst = CommonStatic.ctx.getWorkspaceFile(rl.getPath(Source.REPLAY) + ".replay");
		Workspace.validate(Source.REPLAY, rl);
		if (putInMap && rl.pack.equals(ResourceLocation.LOCAL))
			getMap().put(rl.id, this);
		if (!src.exists()) {
			write();
			return;
		}
		Context.renameTo(dst, src);
		write();
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
			if (rl.pack.startsWith(".temp_"))
				rl.pack = rl.pack.substring(6);
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
			Data.err(() -> Context.delete(tmp));
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
