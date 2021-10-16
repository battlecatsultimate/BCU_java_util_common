package common.battle;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.CommonStatic;
import common.io.InStream;
import common.io.json.JsonClass;
import common.io.json.JsonDecoder;
import common.io.json.JsonEncoder;
import common.io.json.JsonField;
import common.io.json.JsonField.GenType;
import common.io.json.JsonField.IOType;
import common.pack.Context;
import common.pack.Context.ErrType;
import common.pack.Identifier;
import common.pack.UserProfile;
import common.pack.VerFixer;
import common.pack.VerFixer.VerFixerException;
import common.system.Copable;
import common.util.unit.Form;
import common.util.unit.Level;
import common.util.unit.Unit;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@JsonClass
public class BasisSet extends Basis implements Copable<BasisSet> {

	public static BasisSet current() {
		return UserProfile.getStatic("BasisSet_current", BasisSet::def);
	}

	public static BasisSet def() {
		listRaw();
		return UserProfile.getStatic("BasisSet_default", BasisSet::new);
	}

	public static List<BasisSet> list() {
		def();
		return listRaw();
	}

	public static BasisSet[] getBackupSet(JsonElement element) {
		JsonArray arr = element.getAsJsonObject().getAsJsonArray("list");

		BasisSet[] sets = new BasisSet[arr.size()];

		for(int i = 0; i < arr.size(); i++) {
			sets[i] = JsonDecoder.decode(arr.get(i), BasisSet.class);
		}

		return sets;
	}

	/**
	 * Synchronize lineup's orb data with stat change
	 * @param u Current unit, must be custom unit or it won't do anything
	 */
	public static void synchronizeOrb(Unit u) {
		//No need to change BC unit's orb status
		if (u == null || u.id.pack.equals(Identifier.DEF))
			return;

		for(BasisSet set : list()) {
			for(BasisLU lu : set.lb) {
				for(Identifier<Unit> id : lu.lu.map.keySet()) {
					if(id.equals(u.id)) {
						Level l = lu.lu.map.get(id);

						if(l.getOrbs() != null) {
							int[][] orb = l.getOrbs();

							ArrayList<int[]> filteredOrb = new ArrayList<>();

							boolean str = false;
							boolean mas = false;
							boolean res = false;

							for(Form f : u.forms) {
								str |= (f.du.getAbi() & AB_GOOD) != 0;
								mas |= (f.du.getAbi() & AB_MASSIVE) != 0;
								res |= (f.du.getAbi() & AB_RESIST) != 0;
							}

							for(int[] o : orb) {
								if(conditionalOrb(o[ORB_TYPE])) {
									if(o[ORB_TYPE] == ORB_STRONG && str) {
										filteredOrb.add(o);
									} else if(o[ORB_TYPE] == ORB_MASSIVE && mas) {
										filteredOrb.add(o);
									} else if(o[ORB_TYPE] == ORB_RESISTANT && res) {
										filteredOrb.add(o);
									}
								} else {
									filteredOrb.add(o);
								}
							}

							if(filteredOrb.size() == 0) {
								l.setOrbs(null);
							} else {
								int[][] newOrb = new int[filteredOrb.size()][];

								for(int i = 0; i < newOrb.length; i++) {
									newOrb[i] = filteredOrb.get(i);
								}

								l.setOrbs(newOrb);
							}
						}
					}
				}
			}
		}
	}

	public static void read() {
		def();
		File old = CommonStatic.ctx.getUserFile("./basis.v");
		if (old.exists()) {
			@SuppressWarnings("deprecation")
			InStream is = CommonStatic.def.readBytes(old);
			CommonStatic.ctx.noticeErr(() -> read(is), ErrType.WARN, "failed to read basis data");
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			old.delete();
			return;
		}
		File f = CommonStatic.ctx.getUserFile("./basis.json");
		if (f.exists())
			try (Reader r = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8)) {
				JsonElement je = JsonParser.parseReader(r);
				r.close();
				JsonElement jel = je.getAsJsonObject().get("list");
				JsonDecoder.decode(jel, BasisSet[].class);
				int cur = je.getAsJsonObject().get("current").getAsInt();
				setCurrent(list().get(cur));
			} catch (Exception e) {
				CommonStatic.ctx.noticeErr(e, ErrType.WARN, "failed to read basis data");
			}
	}

	@Deprecated
	public static void read(InStream is) throws VerFixerException {
		zreads(is, false);
	}

	public static void setCurrent(BasisSet cur) {
		UserProfile.setStatic("BasisSet_current", cur);
	}

	public static void write() {
		File target = CommonStatic.ctx.getUserFile("./basis.json");
		File temp = CommonStatic.ctx.getUserFile("./.temp.basis.json");
		try (Writer w = new OutputStreamWriter(new FileOutputStream(temp), StandardCharsets.UTF_8)) {
			Context.check(temp);
			List<BasisSet> list = list();
			int cur = list.indexOf(current());
			BasisSet[] arr = new BasisSet[list.size() - 1];
			for (int i = 0; i < arr.length; i++)
				arr[i] = list.get(i + 1);
			JsonObject ans = new JsonObject();
			ans.add("list", JsonEncoder.encode(arr));
			ans.addProperty("current", cur);
			w.write(ans.toString());
			w.close();
			Context.delete(target);
			temp.renameTo(target);
		} catch (Exception e) {
			CommonStatic.ctx.noticeErr(e, ErrType.ERROR, "failed to save basis data");
		}

	}

	private static boolean conditionalOrb(int type) {
		return type == ORB_STRONG || type == ORB_MASSIVE || type == ORB_RESISTANT;
	}

	private static List<BasisSet> listRaw() {
		return UserProfile.getStatic("BasisSet_list", () -> new ArrayList<>());
	}

	private static List<BasisSet> zreads(InStream is, boolean bac) throws VerFixerException {
		int ver = getVer(is.nextString());
		if (ver != 308)
			throw new VerFixer.VerFixerException("basis set has to have version 308, got " + ver);
		List<BasisSet> ans = bac ? new ArrayList<BasisSet>() : list();
		int n = is.nextInt();
		for (int i = 1; i < n; i++) {
			BasisSet bs = new BasisSet(ver, is.subStream());
			ans.add(bs);
		}
		int ind = Math.max(is.nextInt(), ans.size() - 1);
		if (!bac)
			setCurrent(list().get(ind));
		return ans;
	}

	@JsonField(gen = GenType.FILL)
	private final Treasure t;

	@JsonField(generic = BasisLU.class, gen = GenType.GEN)
	public final ArrayList<BasisLU> lb = new ArrayList<>();

	public BasisLU sele;

	public BasisSet() {
		if (listRaw().size() == 0)
			name = "temporary";
		else
			name = "set " + listRaw().size();
		t = new Treasure(this);
		setCurrent(this);
		lb.add(sele = new BasisLU(this));
		listRaw().add(this);
	}

	public BasisSet(BasisSet ref) {
		name = "set " + list().size();
		list().add(this);
		t = new Treasure(this, ref.t);
		setCurrent(this);
		for (BasisLU blu : ref.lb)
			lb.add(sele = new BasisLU(this, blu));
	}

	@Deprecated
	private BasisSet(int ver, InStream is) throws VerFixerException {
		name = is.nextString();
		t = new Treasure(this, ver, is);
		zread(ver, is);
	}

	public BasisLU add() {
		lb.add(sele = new BasisLU(this));
		return sele;
	}

	@Override
	public BasisSet copy() {
		return new BasisSet(this);
	}

	public BasisLU copyCurrent() {
		lb.add(sele = new BasisLU(this, sele));
		return sele;
	}

	/**
	 * BasisSet are used in data display, so cannot be effected by combo
	 */
	@Override
	public int getInc(int type) {
		return 0;
	}

	public BasisLU remove() {
		lb.remove(sele);
		return sele = lb.get(0);
	}

	@Override
	public Treasure t() {
		return t;
	}

	@JsonField(tag = "sele", io = IOType.R)
	public void zgen(int ind) {
		sele = lb.get(ind);
	}

	@JsonField(tag = "sele", io = IOType.W)
	public int zser() {
		return lb.indexOf(sele);
	}

	@Deprecated
	private void zread(int val, InStream is) throws VerFixerException {
		val = getVer(is.nextString());
		if (val != 308)
			throw new VerFixer.VerFixerException("basis set has to have version 308, got " + val);
		int n = is.nextInt();
		for (int i = 0; i < n; i++) {
			String str = is.nextString();
			int[] ints = is.nextIntsB();
			InStream sub = is.subStream();
			BasisLU bl = new BasisLU(this, new LineUp(308, sub), str, ints);
			lb.add(bl);
		}
		int ind = is.nextInt();
		sele = lb.get(ind);
	}

}
