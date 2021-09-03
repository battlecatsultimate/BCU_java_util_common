package common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.CommonStatic;
import common.io.assets.Admin.StaticPermitted;
import common.io.json.FieldOrder;
import common.io.json.FieldOrder.Order;
import common.io.json.JsonClass;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonDecoder;
import common.io.json.JsonEncoder;
import common.pack.Context.ErrType;
import common.pack.Context.RunExc;
import common.pack.Context.SupExc;
import common.pack.Identifier;
import common.util.pack.Background;
import common.util.pack.EffAnim.EffAnimStore;

import java.lang.annotation.*;
import java.lang.reflect.Field;

@StaticPermitted
public class Data {

	@JsonClass(read = JsonClass.RType.MANUAL, write = JsonClass.WType.CLASS, generator = "genProc", serializer = "serProc")
	public static class Proc implements BattleStatic {

		@JsonClass(noTag = NoTag.LOAD)
		public static class ARMOR extends ProcItem {
			@Order(0)
			public int prob;
			@Order(1)
			public int time;
			@Order(2)
			public int mult;
		}

		@JsonClass(noTag = NoTag.LOAD)
		public static class BURROW extends ProcItem {
			@Order(0)
			public int count;
			@Order(1)
			public int dis;
		}

		@JsonClass(noTag = NoTag.LOAD)
		public static class CRITI extends ProcItem {
			@Order(0)
			public int type;
		}

		@JsonClass(noTag = NoTag.LOAD)
		public static class IMU extends ProcItem {
			@Order(0)
			public int mult;
		}

		public static abstract class IntType implements Cloneable, BattleStatic {

			@Documented
			@Retention(value = RetentionPolicy.RUNTIME)
			@Target(value = ElementType.FIELD)
			public @interface BitCount {
				int value();
			}

			@Override
			public IntType clone() throws CloneNotSupportedException {
				return (IntType) super.clone();
			}

			public Field[] getDeclaredFields() {
				return FieldOrder.getDeclaredFields(this.getClass());
			}

			public IntType load(int val) throws Exception {
				Field[] fs = getDeclaredFields();
				for (int i = 0; i < fs.length;) {
					BitCount c = fs[i].getAnnotation(BitCount.class);
					if (c == null) {
						fs[i].set(this, (val >> i & 1) == 1);
						i++;
					} else {
						fs[i].set(this, val >> i & (1 << c.value()) - 1);
						i += c.value();
					}
				}
				return this;
			}

			public int toInt() throws Exception {
				Field[] fs = getDeclaredFields();
				int ans = 0;
				for (int i = 0; i < fs.length;) {
					BitCount c = fs[i].getAnnotation(BitCount.class);
					if (c == null) {
						if (fs[i].getBoolean(this))
							ans |= 1 << i;
						i++;
					} else {
						int val = fs[i].getInt(this);
						ans |= val << i;
						i += c.value();
					}
				}
				return ans;
			}

		}

		@JsonClass(noTag = NoTag.LOAD)
		public static class MOVEWAVE extends ProcItem {
			@Order(0)
			public int prob;
			@Order(1)
			public int speed;
			@Order(2)
			public int width;
			@Order(3)
			public int time;
			@Order(4)
			public int dis;
			@Order(5)
			public int itv;
		}

		@JsonClass(noTag = NoTag.LOAD)
		public static class PM extends ProcItem {
			@Order(0)
			public int prob;
			@Order(1)
			public int mult;
		}

		@JsonClass(noTag = NoTag.LOAD)
		public static class POISON extends ProcItem {

			@JsonClass(noTag = NoTag.LOAD)
			public static class TYPE extends IntType {
				@BitCount(2)
				@Order(0)
				public int damage_type;
				@Order(1)
				public boolean unstackable;
			}

			@Order(0)
			public int prob;
			@Order(1)
			public int time;
			@Order(2)
			public int damage;
			@Order(3)
			public int itv;
			@Order(4)
			public TYPE type = new TYPE();
		}

		@JsonClass(noTag = NoTag.LOAD)
		public static class PROB extends ProcItem {
			@Order(0)
			public int prob;
		}

		public static abstract class ProcItem implements Cloneable, BattleStatic {
			public ProcItem clear() {
				try {
					Field[] fs = getDeclaredFields();
					for (Field f : fs)
						if (f.getType() == int.class)
							f.set(this, 0);
						else if (IntType.class.isAssignableFrom(f.getType()))
							f.set(this, (f.getType().newInstance()));
						else if (f.getType() == Identifier.class)
							f.set(this, null);
						else
							throw new Exception("unknown field " + f.getType() + " " + f.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return this;
			}

			@Override
			public ProcItem clone() {
				try {
					ProcItem ans = (ProcItem) super.clone();
					Field[] fs = getDeclaredFields();
					for (Field f : fs)
						if (IntType.class.isAssignableFrom(f.getType()) && f.get(this) != null) {
							f.set(ans, ((IntType) f.get(this)).clone());
						} else if (f.getType() == Identifier.class && f.get(this) != null)
							f.set(ans, ((Identifier<?>) f.get(this)).clone());
					return ans;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			public boolean exists() {
				try {
					Field[] fs = getDeclaredFields();
					for (Field f : fs)
						if (f.getType() == int.class) {
							Object o = f.get(this);

							if(f.getName().equals("prob") && ((Integer) o) == 0)
								return false;

							if (((Integer) o) != 0)
								return true;
						}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}

			@Deprecated
			public int get(int i) {
				try {
					Field f = getDeclaredFields()[i];
					return f.getType() == int.class ? f.getInt(this) : ((IntType) f.get(this)).toInt();
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}

			public Field[] getDeclaredFields() {
				return FieldOrder.getDeclaredFields(this.getClass());
			}

			@Deprecated
			public ProcItem load(int[] data) throws Exception {
				Field[] fs = getDeclaredFields();
				for (int i = 0; i < Math.min(data.length, fs.length); i++)
					if (fs[i].getType() == int.class)
						fs[i].set(this, data[i]);
					else if (IntType.class.isAssignableFrom(fs[i].getType()))
						fs[i].set(this, ((IntType) fs[i].getType().newInstance()).load(data[i]));
					else if (fs[i].getType() == Identifier.class)
						fs[i].set(this, Identifier.parseIntRaw(data[i], this.getClass()));
					else
						throw new Exception("unknown field " + fs[i].getType() + " " + fs[i].getName());
				return this;
			}

			public boolean perform(CopRand r) {
				try {
					Field f = this.getClass().getDeclaredField("prob");
					int prob = f.getInt(this);
					if (prob == 0)
						return false;
					return r.nextDouble() * 100 < prob;
				} catch (Exception e) {
					return exists();
				}
			}

			/**
			 * should not modify IntType and Identifier
			 */
			@Deprecated
			public void set(int i, int v) {
				try {
					Field f = getDeclaredFields()[i];
					if (f.getType() == int.class)
						f.set(this, v);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public void set(ProcItem pi) {
				try {
					Field[] fs = getDeclaredFields();
					for (Field f : fs)
						if (f.getType().isPrimitive())
							f.set(this, f.get(pi));
						else if (IntType.class.isAssignableFrom(f.getType()))
							f.set(this, ((IntType) f.get(pi)).clone());
						else if (f.getType() == Identifier.class) {
							Identifier<?> id = (Identifier<?>) f.get(pi);
							f.set(this, id == null ? null : id.clone());
						} else
							throw new Exception("unknown field " + f.getType() + " " + f.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public String toString() {
				return JsonEncoder.encode(this).toString();
			}

		}

		@JsonClass(noTag = NoTag.LOAD)
		public static class PT extends ProcItem {
			@Order(0)
			public int prob;
			@Order(1)
			public int time;
		}

		@JsonClass(noTag = NoTag.LOAD)
		public static class PTD extends ProcItem {
			@Order(0)
			public int prob;
			@Order(1)
			public int time;
			@Order(2)
			public int dis;
		}

		@JsonClass(noTag = NoTag.LOAD)
		public static class REVIVE extends ProcItem {

			@JsonClass(noTag = NoTag.LOAD)
			public static class TYPE extends IntType {
				@BitCount(2)
				@Order(0)
				public int range_type;
				@Order(1)
				public boolean imu_zkill;
				@Order(2)
				public boolean revive_non_zombie;
				@Order(3)
				public boolean revive_others;
			}

			@Order(0)
			public int count;
			@Order(1)
			public int time;
			@Order(2)
			public int health;
			@Order(3)
			public int dis_0;
			@Order(4)
			public int dis_1;
			@Order(5)
			public TYPE type = new TYPE();
		}

		@JsonClass(noTag = NoTag.LOAD)
		public static class SPEED extends ProcItem {
			@Order(0)
			public int prob;
			@Order(1)
			public int time;
			@Order(2)
			public int speed;
			@Order(3)
			public int type;
		}

		@JsonClass(noTag = NoTag.LOAD)
		public static class STRONG extends ProcItem {
			@Order(0)
			public int health;
			@Order(1)
			public int mult;
		}

		@JsonClass(noTag = NoTag.LOAD)
		public static class SUMMON extends ProcItem {

			@JsonClass(noTag = NoTag.LOAD)
			public static class TYPE extends IntType {

				@BitCount(2)
				@Order(0)
				public int anim_type;
				@Order(1)
				public boolean ignore_limit;
				@Order(2)
				public boolean fix_buff;
				@Order(3)
				public boolean same_health;
				@Order(4)
				public boolean random_layer;
				@Order(5)
				public boolean on_hit;
				@Order(6)
				public boolean on_kill;

			}

			@Order(0)
			public int prob;
			@Order(1)
			public Identifier<?> id;
			@Order(2)
			public int dis;
			@Order(3)
			public int mult;
			@Order(4)
			public TYPE type = new TYPE();
			@Order(5)
			public int time;
			@Order(6)
			public int form;
		}

		@JsonClass(noTag = NoTag.LOAD)
		public static class THEME extends ProcItem {

			@JsonClass(noTag = NoTag.LOAD)
			public static class TYPE extends IntType {
				@Order(0)
				public boolean kill;
			}

			@Order(0)
			public int prob;
			@Order(1)
			public int time;
			@Order(2)
			public Identifier<Background> id;
			@Order(3)
			public TYPE type = new TYPE();
		}

		@JsonClass(noTag = NoTag.LOAD)
		public static class VOLC extends ProcItem {
			@Order(0)
			public int prob;
			@Order(1)
			public int dis_0;
			@Order(2)
			public int dis_1;
			@Order(3)
			public int time;
		}

		@JsonClass(noTag = NoTag.LOAD)
		public static class WAVE extends ProcItem {
			@Order(0)
			public int prob;
			@Order(1)
			public int lv;
		}

		@JsonClass(noTag = NoTag.LOAD)
		public static class MINIWAVE extends ProcItem {
			@Order(0)
			public int prob;
			@Order(1)
			public int lv;
			@Order(2)
			public int multi;
		}

		@JsonClass(noTag = NoTag.LOAD)
		public static class WEAK extends ProcItem {
			@Order(0)
			public int prob;
			@Order(1)
			public int time;
			@Order(2)
			public int mult;
		}

		@JsonClass(noTag = NoTag.LOAD)
		public static class DSHIELD extends ProcItem {
			@Order(0)
			public int hp;
			@Order(1)
			public int regen;
		}

		public static Proc blank() {
			return new Proc();
		}

		public static Field[] getDeclaredFields() {
			return FieldOrder.getDeclaredFields(Proc.class);
		}

		public static String getName(int i) {
			return getDeclaredFields()[i].getName();
		}

		public static Proc load(int[][] data) {
			Proc ans = new Proc();
			try {
				Field[] fs = getDeclaredFields();
				for (int i = 0; i < fs.length; i++) {
					fs[i].setAccessible(true);

					if(i < data.length) {
						fs[i].set(ans, ((ProcItem) fs[i].getType().newInstance()).load(data[i]));
					} else {
						fs[i].set(ans, ((ProcItem) fs[i].getType().newInstance()).clear());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ans;
		}

		@Order(0)
		public final PTD KB = new PTD();
		@Order(1)
		public final PT STOP = new PT();
		@Order(2)
		public final PT SLOW = new PT();
		@Order(3)
		public final PM CRIT = new PM();
		@Order(4)
		public final WAVE WAVE = new WAVE();
		@Order(5)
		public final WEAK WEAK = new WEAK();
		@Order(6)
		public final PROB BREAK = new PROB();
		@Order(7)
		public final PTD WARP = new PTD();
		@Order(8)
		public final PT CURSE = new PT();
		@Order(9)
		public final STRONG STRONG = new STRONG();
		@Order(10)
		public final PROB LETHAL = new PROB();
		@Order(11)
		public final BURROW BURROW = new BURROW();
		@Order(12)
		public final REVIVE REVIVE = new REVIVE();
		@Order(13)
		public final IMU IMUKB = new IMU();
		@Order(14)
		public final IMU IMUSTOP = new IMU();
		@Order(15)
		public final IMU IMUSLOW = new IMU();
		@Order(16)
		public final IMU IMUWAVE = new IMU();
		@Order(17)
		public final IMU IMUWEAK = new IMU();
		@Order(18)
		public final IMU IMUWARP = new IMU();
		@Order(19)
		public final IMU IMUCURSE = new IMU();
		@Order(20)
		public final PROB SNIPER = new PROB();
		@Order(21)
		public final PT TIME = new PT();
		@Order(22)
		public final PT SEAL = new PT();
		@Order(23)
		public final SUMMON SUMMON = new SUMMON();
		@Order(24)
		public final MOVEWAVE MOVEWAVE = new MOVEWAVE();
		@Order(25)
		public final THEME THEME = new THEME();
		@Order(26)
		public final POISON POISON = new POISON();
		@Order(27)
		public final PROB BOSS = new PROB();
		@Order(28)
		public final CRITI CRITI = new CRITI();
		@Order(29)
		public final PM SATK = new PM();
		@Order(30)
		public final PT IMUATK = new PT();
		@Order(31)
		public final PM POIATK = new PM();
		@Order(32)
		public final VOLC VOLC = new VOLC();
		@Order(33)
		public final IMU IMUPOIATK = new IMU();
		@Order(34)
		public final IMU IMUVOLC = new IMU();
		@Order(35)
		public final ARMOR ARMOR = new ARMOR();

		@Order(36)
		public final SPEED SPEED = new SPEED();

		@Order(37)
		public final MINIWAVE MINIWAVE = new MINIWAVE();

		@Order(38)
		public final IMU IMUSUMMON = new IMU();

		@Order(39)
		public final DSHIELD DEMONSHIELD = new DSHIELD();

		@Order(40)
		public final PROB SHIELDBREAK = new PROB();
		@Order(41)
		public final VOLC DEATHSURGE = new VOLC();

		@Override
		public Proc clone() {
			try {
				Proc ans = new Proc();
				Field[] fs = getDeclaredFields();
				for (Field f : fs) {
					f.setAccessible(true);
					if(f.get(this) != null)
						f.set(ans, ((ProcItem) f.get(this)).clone());
					f.setAccessible(false);
				}
				return ans;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public ProcItem get(String id) {
			try {
				return (ProcItem) Proc.class.getField(id).get(this);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public ProcItem getArr(int i) {
			try {
				return (ProcItem) getDeclaredFields()[i].get(this);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public boolean sharable(int i) {
			if(i >= procSharable.length) {
				System.out.println("Warning : "+i+" is out of index of procSharable");
				return false;
			} else {
				return procSharable[i];
			}
		}

		@Override
		public String toString() {
			return JsonEncoder.encode(this).toString();
		}

		public JsonObject serProc() {
			JsonObject obj = new JsonObject();

			for(Field f : getDeclaredFields()) {
				try {
					String tag = f.getName();
					ProcItem proc = (ProcItem) f.get(this);

					if(proc.exists()) {
						obj.add(tag, JsonEncoder.encode(proc));
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

			return obj;
		}

		public static Proc genProc(JsonElement elem) {
			Proc proc = Proc.blank();

			if(elem == null)
				return proc;

			JsonObject obj = elem.getAsJsonObject();

			if(obj == null)
				return proc;

			for(Field f : getDeclaredFields()) {
				String tag = f.getName();

				try {
					if(obj.has(tag) && !obj.get(tag).isJsonNull()) {
						f.setAccessible(true);

						f.set(proc, JsonDecoder.decode(obj.get(tag), f.getType()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return proc;
		}

	}

	public static final int restrict_name = 32;
	public static final int SE_HIT_0 = 20;
	public static final int SE_HIT_1 = 21;
	public static final int SE_DEATH_0 = 23;
	public static final int SE_DEATH_1 = 24;
	public static final int SE_HIT_BASE = 22;
	public static final int SE_ZKILL = 59;
	public static final int SE_CRIT = 44;
	public static final int SE_SATK = 90;
	public static final int SE_WAVE = 26;
	public static final int SE_LETHAL = 50;
	public static final int SE_WARP_ENTER = 73;
	public static final int SE_WARP_EXIT = 74;
	public static final int SE_BOSS = 45;
	public static final int SE_SPEND_FAIL = 15;
	public static final int SE_SPEND_SUC = 19;
	public static final int SE_SPEND_REF = 27;
	public static final int SE_CANNON_CHARGE = 28;
	public static final int SE_BARRIER_ABI = 70;
	public static final int SE_BARRIER_NON = 71;
	public static final int SE_BARRIER_ATK = 72;
	public static final int SE_POISON = 110;
	public static final int SE_VOLC_START = 111;
	public static final int SE_VOLC_LOOP = 112;
	public static final int SE_SHIELD_HIT = 136;
	public static final int SE_SHIELD_BROKEN = 137;
	public static final int SE_SHIELD_REGEN = 138;
	public static final int SE_SHIELD_BREAKER = 139;

	public static final int[][] SE_CANNON = { { 25, 26 }, { 60 }, { 61 }, { 36, 37 }, { 65, 83 }, { 84, 85 }, { 86 },
			{ 124 } };

	public static final int[] SE_ALL = { 15, 19, 20, 21, 22, 23, 24, 25, 26, 27, 36, 37, 44, 45, 50, 59, 60, 61, 65, 73,
			74, 83, 84, 85, 86, 90, 110, 111, 112, 124 };

	public static final int RARITY_TOT = 6;

	// trait bit filter
	public static final int TB_RED = 1;
	public static final int TB_FLOAT = 2;
	public static final int TB_BLACK = 4;
	public static final int TB_METAL = 8;
	public static final int TB_ANGEL = 16;
	public static final int TB_ALIEN = 32;
	public static final int TB_ZOMBIE = 64;
	public static final int TB_RELIC = 128;
	public static final int TB_WHITE = 256;
	public static final int TB_EVA = 512;
	public static final int TB_WITCH = 1024;
	public static final int TB_INFH = 2048;
	public static final int TB_DEMON = 4096;

	// trait index
	public static final int TRAIT_RED = 0;
	public static final int TRAIT_FLOAT = 1;
	public static final int TRAIT_BLACK = 2;
	public static final int TRAIT_METAL = 3;
	public static final int TRAIT_ANGEL = 4;
	public static final int TRAIT_ALIEN = 5;
	public static final int TRAIT_ZOMBIE = 6;
	public static final int TRAIT_RELIC = 7;
	public static final int TRAIT_WHITE = 8;
	public static final int TRAIT_EVA = 9;
	public static final int TRAIT_WITCH = 10;
	public static final int TRAIT_INFH = 11;
	public static final int TRAIT_DEMON = 12;
	public static final int TRAIT_TOT = 13;

	// treasure
	public static final int T_RED = 0;
	public static final int T_FLOAT = 1;
	public static final int T_BLACK = 2;
	public static final int T_ANGEL = 3;
	public static final int T_METAL = 4;
	public static final int T_ALIEN = 5;
	public static final int T_ZOMBIE = 6;

	// default tech value
	public static final int[] MLV = new int[] { 30, 30, 30, 30, 30, 30, 30, 10, 30 };

	// tech index
	public static final int LV_RES = 0;
	public static final int LV_ACC = 1;
	public static final int LV_BASE = 2;
	public static final int LV_WORK = 3;
	public static final int LV_WALT = 4;
	public static final int LV_RECH = 5;
	public static final int LV_CATK = 6;
	public static final int LV_CRG = 7;
	public static final int LV_XP = 8;
	public static final int LV_TOT = 9;

	// default treasure value
	public static final int[] MT = new int[] { 300, 300, 300, 300, 300, 300, 600, 600, 600, 300, 300 };

	// treasure index
	public static final int T_ATK = 0;
	public static final int T_DEF = 1;
	public static final int T_RES = 2;
	public static final int T_ACC = 3;
	public static final int T_WORK = 4;
	public static final int T_WALT = 5;
	public static final int T_RECH = 6;
	public static final int T_CATK = 7;
	public static final int T_BASE = 8;
	public static final int T_XP1 = 9;
	public static final int T_XP2 = 10;
	public static final int T_TOT = 11;

	// abi bit filter
	public static final int AB_GOOD = 1 << 0;
	public static final int AB_RESIST = 1 << 1;
	public static final int AB_MASSIVE = 1 << 2;
	public static final int AB_ONLY = 1 << 3;
	public static final int AB_EARN = 1 << 4;
	public static final int AB_BASE = 1 << 5;
	public static final int AB_METALIC = 1 << 6;
	public static final int AB_MOVEI = 1 << 7;
	public static final int AB_WAVES = 1 << 8;
	public static final int AB_SNIPERI = 1 << 9;
	public static final int AB_TIMEI = 1 << 10;
	public static final int AB_GHOST = 1 << 11;
	public static final int AB_POII = 1 << 12;
	public static final int AB_ZKILL = 1 << 13;
	public static final int AB_WKILL = 1 << 14;
	public static final int AB_GLASS = 1 << 15;
	public static final int AB_THEMEI = 1 << 16;
	public static final int AB_EKILL = 1 << 17;
	public static final int AB_SEALI = 1 << 18;
	public static final int AB_IMUSW = 1 << 19;
	public static final int AB_RESISTS = 1 << 20;
	public static final int AB_MASSIVES = 1 << 21;

	// 0111 1010 1110 0001 0111 1111
	@Deprecated
	public static final int AB_ELIMINATOR = 0x7ae17f;

	// abi index
	public static final int ABI_GOOD = 0;
	public static final int ABI_RESIST = 1;
	public static final int ABI_MASSIVE = 2;
	public static final int ABI_ONLY = 3;
	public static final int ABI_EARN = 4;
	public static final int ABI_BASE = 5;
	public static final int ABI_METALIC = 6;
	public static final int ABI_MOVEI = 7;
	public static final int ABI_WAVES = 8;
	public static final int ABI_SNIPERI = 9;
	public static final int ABI_TIMEI = 10;
	public static final int ABI_GHOST = 11;
	public static final int ABI_POII = 12;
	public static final int ABI_ZKILL = 13;
	public static final int ABI_WKILL = 14;
	public static final int ABI_GLASS = 15;
	public static final int ABI_THEMEI = 16;
	public static final int ABI_EKILL = 17;
	public static final int ABI_SEALI = 18;
	public static final int ABI_IMUSW = 19;
	public static final int ABI_RESISTS = 20;
	public static final int ABI_MASSIVES = 21;
	public static final int ABI_TOT = 30;// 20 currently

	// proc index
	public static final int P_KB = 0;
	public static final int P_STOP = 1;
	public static final int P_SLOW = 2;
	public static final int P_CRIT = 3;
	public static final int P_WAVE = 4;
	public static final int P_WEAK = 5;
	public static final int P_BREAK = 6;
	public static final int P_WARP = 7;
	public static final int P_CURSE = 8;
	public static final int P_STRONG = 9;
	public static final int P_LETHAL = 10;
	public static final int P_BURROW = 11;
	/**
	 * body proc: 0: add revive time for zombies, -1 to make it infinite, revivable
	 * zombies only 1: revive time 2: revive health 3: point 1 4: point 2 5: type:
	 * 0/1/2/3: duration: in range and normal/in range/ master lifetime/permanent
	 * +4: make Z-kill unusable +8: revive non-zombie also +16: applicapable to
	 * others
	 */
	public static final int P_REVIVE = 12;
	public static final int P_IMUKB = 13;
	public static final int P_IMUSTOP = 14;
	public static final int P_IMUSLOW = 15;
	public static final int P_IMUWAVE = 16;
	public static final int P_IMUWEAK = 17;
	public static final int P_IMUWARP = 18;
	public static final int P_IMUCURSE = 19;
	public static final int P_SNIPER = 20;
	public static final int P_TIME = 21;
	public static final int P_SEAL = 22;
	/**
	 * 0:prob, 1:ID, 2:location, 3: buff, 4:conf, 5:time
	 *
	 * +0: direct, +1: warp, +2:burrow, +4:disregard limit, +8: fix buff, +16: same
	 * health, +32: diff layer, +64 on hit, +128 on kill
	 */
	public static final int P_SUMMON = 23;
	/**
	 * 0:prob, 1:speed, 2:width (left to right), 3:time, 4:origin (center), 5:itv
	 */
	public static final int P_MOVEWAVE = 24;
	/**
	 * 0:prob, 1:time (-1 means infinite), 2:ID, 3: type 0 : Change only BG 1 : Kill
	 * all and change BG
	 */
	public static final int P_THEME = 25;
	/**
	 * 0:prob, 1:time, 2:dmg, 3:itv, 4: conf +0: normal, +1: of total, +2: of
	 * current, +3: of lost, +4: unstackable
	 */
	public static final int P_POISON = 26;
	public static final int P_BOSS = 27;
	/**
	 * body proc: 1: type: protect itself only (0) or effect the attack also (1)
	 */
	public static final int P_CRITI = 28;
	public static final int P_SATK = 29;
	public static final int P_IMUATK = 30;
	/**
	 * official poison
	 */
	public static final int P_POIATK = 31;
	public static final int P_VOLC = 32;
	public static final int P_IMUPOIATK = 33;
	public static final int P_IMUVOLC = 34;
	/**
	 * Make target receive n% damage more/less 0: chance, 1: duration, 2: debuff
	 */
	public static final int P_ARMOR = 35;
	/**
	 * Make target move faster/slower 0: chance, 1: duration, 2: speed, 3: type type
	 * 0: Current speed * (100 + n)% type 1: Current speed + n type 2: Fixed speed
	 */
	public static final int P_SPEED = 36;
	public static final int P_MINIWAVE = 37;
	public static final int P_IMUSUMMON = 38;
	public static final int P_DEMONSHIELD = 39;
	public static final int P_SHIELDBREAK = 40;
	public static final int P_DEATHSURGE = 41;
	public static final int PROC_TOT = 42;// 42
	public static final int PROC_WIDTH = 6;

	public static final boolean[] procSharable = {
			false, //kb
			false, //freeze
			false, //slow
			false, //critical
			false, //wave
			false, //weaken
			false, //barrier breaker
			false, //warp
			false, //curse
			true,  //strengthen
			true,  //survive
			true,  //burrow
			true,  //revive
			true,  //imu.kb
			true,  //imu.freeze
			true,  //imu.slow
			true,  //imu.wave
			true,  //imu.weaken
			true,  //imu.warp
			true,  //imu.curse
			false, //sniper
			false, //time
			false, //seal
			false, //summon
			false, //move wave
			false, //theme
			false, //venom
			false, //boss wave
			true,  //imu. cirtical
			false, //savage blow
			true,  //invincibility
			false, //poison
			false, //volcano
			true,  //imu.poison
			true,  //imu.volcano
			false, //armor
			false, //haste
			false, //miniwave
			true,  //imu.summon
			true,  //demon shield
			false,  //shield breaker
			true  //death surge
	};

	public static final int WT_WAVE = 1;
	public static final int WT_MOVE = 2;
	public static final int WT_CANN = 2;
	public static final int WT_VOLC = 4;
	public static final int WT_MINI = 8;
	public static final int PC_P = 0, PC_AB = 1, PC_BASE = 2, PC_IMU = 3, PC_TRAIT = 4;
	public static final int PC2_HP = 0;
	public static final int PC2_ATK = 1;
	public static final int PC2_SPEED = 2;
	public static final int PC2_COST = 3;
	public static final int PC2_CD = 4;
	// -1 for None
	// 0 for Proc
	// 1 for Ability
	// 2 for Base stat
	// 3 for Immune
	// 4 for Trait
	public static final int[][] PC_CORRES = new int[][] { // NP value table
			{ -1, 0 }, // 0:
			{ 0, P_WEAK }, // 1: weak, reversed health or relic-weak
			{ 0, P_STOP }, // 2: stop
			{ 0, P_SLOW }, // 3: slow or relic-slow
			{ 1, AB_ONLY, 0 }, // 4:
			{ 1, AB_GOOD, 0 }, // 5:
			{ 1, AB_RESIST, 0 }, // 6:
			{ 1, AB_MASSIVE, 0 }, // 7:
			{ 0, P_KB }, // 8: kb
			{ 0, P_WARP, 0 }, // 9:
			{ 0, P_STRONG }, // 10: berserker, reversed health
			{ 0, P_LETHAL }, // 11: lethal
			{ 1, AB_BASE, 0 }, // 12:
			{ 0, P_CRIT }, // 13: crit
			{ 1, AB_ZKILL }, // 14: zkill
			{ 0, P_BREAK }, // 15: break
			{ 1, AB_EARN }, // 16: 2x income
			{ 0, P_WAVE }, // 17: wave
			{ 0, P_IMUWEAK }, // 18: res weak
			{ 0, P_IMUSTOP }, // 19: res stop
			{ 0, P_IMUSLOW }, // 20: res slow
			{ 0, P_IMUKB }, // 21: res kb
			{ 0, P_IMUWAVE }, // 22: res wave
			{ 1, AB_WAVES, 0 }, // 23:
			{ 0, P_IMUWARP, 0 }, // 24:
			{ 2, PC2_COST }, // 25: reduce cost
			{ 2, PC2_CD }, // 26: reduce cooldown
			{ 2, PC2_SPEED }, // 27: inc speed
			{ -1, 0 }, // 28:
			{ 3, P_IMUCURSE }, // 29: imu curse
			{ 0, P_IMUCURSE }, // 30: res curse
			{ 2, PC2_ATK }, // 31: inc ATK
			{ 2, PC2_HP }, // 32: inc HP
			{ 4, TB_RED, 0 }, // 33:
			{ 4, TB_FLOAT, 0 }, // 34:
			{ 4, TB_BLACK, 0 }, // 35: targeting black
			{ 4, TB_METAL, 0 }, // 36:
			{ 4, TB_ANGEL, 0 }, // 37: targeting angle
			{ 4, TB_ALIEN, 0 }, // 38: targeting alien
			{ 4, TB_ZOMBIE, 0 }, // 39: targeting zombie
			{ 4, TB_RELIC, 0 }, // 40: targeting relic
			{ 4, TB_WHITE, 0 }, // 41:
			{ -1, 0 }, // 42:
			{ -1, 0 }, // 43:
			{ 3, P_IMUWEAK }, // 44: immune to weak
			{ 3, P_IMUSTOP }, // 45: immune to freeze
			{ 3, P_IMUSLOW }, // 46: immune to slow
			{ 3, P_IMUKB }, // 47: immune to kb
			{ 3, P_IMUWAVE }, // 48: immune to wave
			{ 3, P_IMUWARP }, // 49: immune to warp
			{ 0, P_SATK }, // 50: savage blow
			{ 0, P_IMUATK }, // 51: immune to attack
			{ 0, P_IMUPOIATK }, // 52: resist to poison ?
			{ 3, P_IMUPOIATK }, // 53: immune to poison
			{ 0, P_IMUVOLC }, // 54: resist to surge ?
			{ 3, P_IMUVOLC }, // 55: immune to surge
			{ 0, P_VOLC }, // 56: surge, level up to chance up
			{ 4, TB_DEMON, 0}, // 57 : targeting demon
			{ 0, P_SHIELDBREAK} //58 : shield piercing
			};

	// foot icon index used in battle
	public static final int INV = -1;
	public static final int INVWARP = -2;
	public static final int STPWAVE = -3;
	public static final int BREAK_ABI = -4;
	public static final int BREAK_ATK = -5;
	public static final int BREAK_NON = -6;
	public static final int HEAL = -7;
	public static final int SHIELD_HIT = -8;
	public static final int SHIELD_BROKEN = -9;
	public static final int SHIELD_REGEN = -10;
	public static final int SHIELD_BREAKER = -11;

	// Combo index
	public static final int C_ATK = 0;
	public static final int C_DEF = 1;
	public static final int C_SPE = 2;
	public static final int C_GOOD = 14;
	public static final int C_MASSIVE = 15;
	public static final int C_RESIST = 16;
	public static final int C_KB = 17;
	public static final int C_SLOW = 18;
	public static final int C_STOP = 19;
	public static final int C_WEAK = 20;
	public static final int C_STRONG = 21;
	public static final int C_WKILL = 22;
	public static final int C_EKILL = 23;
	public static final int C_CRIT = 24;
	public static final int C_C_INI = 3;
	public static final int C_C_ATK = 6;
	public static final int C_C_SPE = 7;
	public static final int C_BASE = 10;
	public static final int C_M_INI = 5;
	public static final int C_M_LV = 4;
	public static final int C_M_MAX = 9;
	public static final int C_RESP = 11;
	public static final int C_MEAR = 12;
	public static final int C_XP = 13;// abandoned
	public static final int C_TOT = 25;

	// Effects Anim index
	public static final int A_KB = 29;
	public static final int A_CRIT = 28;
	public static final int A_SHOCKWAVE = 27;
	public static final int A_ZOMBIE = 26;
	public static final int A_EFF_INV = 18;
	public static final int A_EFF_DEF = 19;// unused
	public static final int A_Z_STRONG = 20;
	public static final int A_B = 21;
	public static final int A_E_B = 22;
	public static final int A_W = 23;
	public static final int A_W_C = 24;
	public static final int A_CURSE = 25;
	public static final int A_DOWN = 0;
	public static final int A_UP = 2;
	public static final int A_SLOW = 4;
	public static final int A_STOP = 6;
	public static final int A_SHIELD = 8;
	public static final int A_FARATTACK = 10;
	public static final int A_WAVE_INVALID = 12;
	public static final int A_WAVE_STOP = 14;
	public static final int A_WAVEGUARD = 16;// unused
	public static final int A_E_DOWN = 1;
	public static final int A_E_UP = 3;
	public static final int A_E_SLOW = 5;
	public static final int A_E_STOP = 7;
	public static final int A_E_SHIELD = 9;
	public static final int A_E_FARATTACK = 11;
	public static final int A_E_WAVE_INVALID = 13;
	public static final int A_E_WAVE_STOP = 15;
	public static final int A_E_WAVEGUARD = 17;// unused
	public static final int A_SNIPER = 30;
	public static final int A_U_ZOMBIE = 31;
	public static final int A_SEAL = 32;
	public static final int A_POI0 = 33;
	public static final int A_POI1 = 34;
	public static final int A_POI2 = 35;
	public static final int A_POI3 = 36;
	public static final int A_POI4 = 37;
	public static final int A_POI5 = 38;
	public static final int A_POI6 = 39;
	public static final int A_POI7 = 40;
	public static final int A_SATK = 41;
	public static final int A_IMUATK = 42;
	public static final int A_POISON = 43;
	public static final int A_VOLC = 44;
	public static final int A_E_VOLC = 45;
	public static final int A_E_CURSE = 46;
	public static final int A_WAVE = 47;
	public static final int A_E_WAVE = 48;
	public static final int A_ARMOR = 49;
	public static final int A_E_ARMOR = 50;
	public static final int A_SPEED = 51;
	public static final int A_E_SPEED = 52;
	public static final int A_WEAK_UP = 53;
	public static final int A_E_WEAK_UP = 54;
	public static final int A_ATK_SMOKE = 55;
	public static final int A_WHITE_SMOKE = 56;
	public static final int A_HEAL = 57;
	public static final int A_E_HEAL = 58;
	public static final int A_DEMON_SHIELD = 59;
	public static final int A_E_DEMON_SHIELD = 60;
	public static final int[] A_POIS = { A_POI0, A_POI1, A_POI2, A_POI3, A_POI4, A_POI5, A_POI6, A_POI7 };
	public static final int A_TOT = 61;

	// atk type index used in filter page
	public static final int ATK_SINGLE = 0;
	public static final int ATK_AREA = 1;
	public static final int ATK_LD = 2;
	public static final int ATK_OMNI = 4;
	public static final int ATK_TOT = 6;

	// base and canon level
	public static final int BASE_H = 0;
	public static final int BASE_SLOW = 1;
	public static final int BASE_WALL = 2;
	public static final int BASE_STOP = 3;
	public static final int BASE_WATER = 4;
	public static final int BASE_GROUND = 5;
	public static final int BASE_BARRIER = 6;
	public static final int BASE_CURSE = 7;
	public static final int BASE_TOT = 8;

	// base type
	public static final int BASE_ATK_MAGNIFICATION = 0;
	public static final int BASE_SLOW_TIME = 1;
	public static final int BASE_TIME = 2;
	public static final int BASE_WALL_MAGNIFICATION = 3;
	public static final int BASE_WALL_ALIVE_TIME = 4;
	public static final int BASE_RANGE = 5;
	//Figure out type 6
	public static final int BASE_HEALTH_PERCENTAGE = 7;
	//Figure out type 8
	public static final int BASE_HOLY_ATK_SURFACE = 9;
	public static final int BASE_HOLY_ATK_UNDERGROUND = 10;
	//Figure out type 11
	public static final int BASE_CURSE_TIME = 12;



	// touchable ID
	public static final int TCH_N = 1;
	public static final int TCH_KB = 2;
	public static final int TCH_UG = 4;
	public static final int TCH_CORPSE = 8;
	public static final int TCH_SOUL = 16;
	public static final int TCH_EX = 32;
	public static final int TCH_ZOMBX = 64;
	public static final int TCH_ENTER = 128;

	public static final String[] A_PATH = new String[] { "down", "up", "slow", "stop", "shield", "farattack",
			"wave_invalid", "wave_stop", "waveguard" };

	// After this line all number is game data

	public static final int INT_KB = 0, INT_HB = 1, INT_SW = 2, INT_ASS = 3, INT_WARP = 4;

	public static final int[] KB_PRI = new int[] { 2, 4, 5, 1, 3 };
	public static final int[] KB_TIME = new int[] { 11, 23, 47, 11, -1 };
	public static final int[] KB_DIS = new int[] { 165, 345, 705, 55, -1 };

	public static final int W_E_INI = -33;
	public static final int W_U_INI = -67;
	public static final int W_PROG = 200;
	public static final int W_E_WID = 500;
	public static final int W_U_WID = 400;
	public static final int W_TIME = 3;
	public static final int W_MINI_TIME = 2; // mini wave spawn interval
	public static final int E_IMU = -1;
	public static final int E_IWAVE = -2;
	public static final int E_SWAVE = -3;
	public static final int W_VOLC = 375;
	public static final int W_VOLC_INNER = 250; // volcano inner width
	public static final int W_VOLC_PIERCE = 125; // volcano pierce width
	public static final int VOLC_ITV = 20;

	public static final int VOLC_PRE = 15; // volcano pre-atk
	public static final int VOLC_POST = 10; // volcano post-atk
	public static final int VOLC_SE = 30; // volcano se loop duration

	public static final int[] NYPRE = new int[] { 18, 2, -1, 28, 37, 18, 10, 2 };// not sure
	public static final int[] NYRAN = new int[] { 710, 600, -1, 500, 500, 710, 100, 600 };// not sure
	public static final int SNIPER_CD = 300;// not sure
	public static final int SNIPER_PRE = 5;// not sure
	public static final int SNIPER_POS = -500;// not sure
	public static final int REVIVE_SHOW_TIME = 16;

	public static final int ORB_ATK = 0;
	public static final int ORB_RES = 1;
	public static final int ORB_STRONG = 2;
	public static final int ORB_MASSIVE = 3;
	public static final int ORB_RESISTANT = 4;
	public static final int ORB_TYPE = 0, ORB_TRAIT = 1, ORB_GRADE = 2, ORB_TOT = 3;

	public static final int[] ORB_ATK_MULTI = { 100, 200, 300, 400, 500 }; // Atk orb multiplication
	public static final int[] ORB_RES_MULTI = { 4, 8, 12, 16, 20 }; // Resist orb multiplication
	public static final int[] ORB_STR_DEF_MULTI = {2, 4, 6, 8, 10};
	public static final double[] ORB_STR_ATK_MULTI = {0.06, 0.12, 0.18, 0.24, 0.3};
	public static final double[] ORB_MASSIVE_MULTI = {0.1, 0.2, 0.3, 0.4, 0.5};
	public static final int[] ORB_RESISTANT_MULTI = {5, 10, 15, 20, 25};

	public static final int MUSIC_DELAY = 2344; //Music change delay with milliseconds accuracy

	public static final int LINEUP_CHANGE_TIME = 6; //in frame

	public static final String[] SUFX = new String[] { "f", "c", "s" };

	public static EffAnimStore effas() {
		return CommonStatic.getBCAssets().effas;
	}

	/**
	 * convenient method to log an unexpected error. Don't use it to process any
	 * expected error
	 */
	public static boolean err(RunExc s) {
		return CommonStatic.ctx.noticeErr(s, ErrType.ERROR, "unexpected error");
	}

	/**
	 * convenient method to log an unexpected error. Don't use it to process any
	 * expected error
	 */
	public static <T> T err(SupExc<T> s) {
		return CommonStatic.ctx.noticeErr(s, ErrType.ERROR, "unexpected error");
	}

	public static int getVer(String ver) {
		int ans = 0;
		int[] strs = CommonStatic.parseIntsN(ver);
		for (int str : strs) {
			ans *= 100;
			ans += str;
		}
		return ans;
	}

	public static String hex(int id) {
		return trio(id / 1000) + trio(id % 1000);
	}

	public static <T> T ignore(SupExc<T> sup) {
		try {
			return sup.get();
		} catch (Exception e) {
			return null;
		}
	}

	public static String restrict(String str) {
		if (str.length() < restrict_name)
			return str;
		return str.substring(0, restrict_name);
	}

	public static String revVer(int ver) {
		return ver / 1000000 % 100 + "-" + ver / 10000 % 100 + "-" + ver / 100 % 100 + "-" + ver % 100;
	}

	public static String trio(int i) {
		i %= 1000;
		String str = "";
		if (i < 100)
			str += "0";
		if (i < 10)
			str += "0";
		return str + i;
	}

	public static int reorderTrait(int oldTrait) {
		int newTrait = 0;

		for(int i = 0; i < TRAIT_TOT; i++) {
			if(((oldTrait >> i) & 1) > 0) {
				switch (i) {
					case 0:
						newTrait |= TB_WHITE;
						break;
					case 1:
						newTrait |= TB_RED;
						break;
					case 2:
						newTrait |= TB_FLOAT;
						break;
					case 3:
						newTrait |= TB_BLACK;
						break;
					case 4:
						newTrait |= TB_METAL;
						break;
					case 5:
						newTrait |= TB_ANGEL;
						break;
					case 6:
						newTrait |= TB_ALIEN;
						break;
					case 7:
						newTrait |= TB_ZOMBIE;
						break;
					case 8:
						newTrait |= TB_RELIC;
						break;
					default:
						newTrait |= 1 << i;
				}
			}
		}

		return newTrait;
	}
}
