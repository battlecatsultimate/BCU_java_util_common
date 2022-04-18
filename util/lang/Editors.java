package common.util.lang;

import com.google.common.primitives.Ints;
import common.pack.Identifier;
import common.pack.UserProfile;
import common.util.Data;
import common.util.Data.Proc;
import common.util.Data.Proc.ProcItem;
import common.util.lang.LocaleCenter.Displayable;
import common.util.lang.ProcLang.ItemLang;
import common.util.unit.Unit;
import org.jcodec.common.tools.MathUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Editors {

	private static final Map<String, EditorGroup> eg = new HashMap<>();

	public static class DispItem implements Displayable {

		private final ProcLang.ItemLang lang;
		private final Supplier<Proc.ProcItem> proc;
		private final Formatter.Context ctx;

		public DispItem(ProcLang.ItemLang lang, Supplier<Proc.ProcItem> proc, Formatter.Context ctx) {
			this.lang = lang;
			this.proc = proc;
			this.ctx = ctx;
		}

		@Override
		public String getName() {
			return lang.full_name;
		}

		@Override
		public String getTooltip() {
			String t0 = lang.tooltip == null ? null : lang.tooltip;
			Proc.ProcItem item = proc.get();
			String t1 = item == null ? null : Formatter.format(lang.format, item, ctx);
			if (t0 != null)
				t0 = "<p width = \"500\">" + t0 + "</p>";
			if (t1 != null)
				t1 = "<p width = \"500\">" + t1 + "</p>";
			if (t0 == null && t1 == null)
				return "";
			else if (t0 == null)
				return "<html>" + t1 + "</html>";
			else if (t1 == null)
				return "<html>" + t0 + "</html>";
			else
				return "<html>" + t0 + "<hr>" + t1 + "</html>";
		}

		@Override
		public void setName(String str) {
			lang.full_name = str;
		}

		@Override
		public void setTooltip(String str) {
			lang.tooltip = str;
		}

	}

	public static class EdiField {

		private final Field f0;
		private final Field f1;

		public Object obj;

		private EdiField(Field f) {
			f0 = f;
			f1 = null;
		}

		private EdiField(Field pri, Field sec) {
			f0 = sec;
			f1 = pri;
		}

		public Object get() {
			return Data.err(() -> f0.get(obj));
		}

		public boolean getBoolean() {
			return Data.err(() -> f0.getBoolean(obj));
		}

		public int getInt() {
			return Data.err(() -> f0.getInt(obj));
		}

		public Class<?> getType() {
			return f0.getType();
		}

		public void set(Object data) {
			Data.err(() -> f0.set(obj, data));
		}

		public void setBoolean(boolean data) {
			Data.err(() -> f0.setBoolean(obj, data));
		}

		public void setData(Object obj) {
			this.obj = f1 == null ? obj : obj == null ? null : Data.err(() -> f1.get(obj));
		}

		public void setInt(int data) {
			Data.err(() -> f0.setInt(obj, data));
		}

	}

	public static class EditControl<T> {

		public final Class<T> cls;
		private final Consumer<T> regulator;

		public EditControl(Class<T> cls, Consumer<T> func) {
			this.cls = cls;
			regulator = func;
		}

		public EdiField getField(String f) {
			return Data.err(() -> {
				if (f.contains(".")) {
					String[] strs = f.split("\\.");
					Field f0 = cls.getField(strs[0]);
					Field f1 = f0.getType().getField(strs[1]);
					return new EdiField(f0, f1);
				} else
					return new EdiField(cls.getField(f));
			});
		}

		@SuppressWarnings("unchecked")
		public final void update(EditorGroup par) {
			regulate((T) par.obj);
			par.setData(par.obj);
			if (par.callback != null)
				par.callback.run();
		}

		protected void regulate(T obj) {
			regulator.accept(obj);
		}

	}

	public static abstract class Editor {

		public final EditorGroup par;
		public final EdiField field;
		public final String name;

		public Editor(EditorGroup par, EdiField field, String f) {
			this.par = par;
			this.field = field;
			this.name = f;
		}

		/**
		 * notify that the data changed
		 */
		protected abstract void setData();

		protected final void update() {
			setComponentVisibility(par, true, 1);
			par.ctrl.update(par);
			par.updateVisibility();
		}

	}

	public static class EditorGroup {

		public final String proc; // Proc Title
		public final Class<?> cls; // ProcItem
		public final Editor[] list;
		public final EditControl<?> ctrl;
		public final Runnable callback;

		public Object obj;

		public EditorGroup(String proc, boolean edit, Runnable cb) {
			this.proc = proc;
			this.cls = Data.err(() -> Proc.class.getDeclaredField(proc)).getType();
			this.callback = cb;
			ctrl = map().get(proc);
			ItemLang item = ProcLang.get().get(proc);
			String[] arr = item.list();
			list = new Editor[arr.length];
			for (int i = 0; i < arr.length; i++) {
				list[i] = getEditor(ctrl, this, arr[i], edit);
			}
			eg.put(proc, this);
		}

		public LocaleCenter.Binder getItem(Formatter.Context ctx) {
			ProcLang.ItemLang lang = ProcLang.get().get(proc);
			Displayable disp = new DispItem(lang, this::getProcItem, ctx);
			return new LocaleCenter.ObjBinder(disp, proc, (name) -> getItem(ctx));
		}

		public void setData(Object obj) {
			this.obj = obj;
			for (Editor e : list)
				e.setData();
		}

		public void updateVisibility() {
			ProcItem item = getProcItem();
			if (item instanceof Proc.IMUAD)
				setComponentVisibility(this, item.exists(), 2);
			else if (!(item instanceof Proc.IMU)) {
				ArrayList<Integer> visFields = new ArrayList<>();
				EditorSupplier edi = UserProfile.getStatic("Editor_Supplier", () -> null);
				for (int i = 1; i < list.length; i++) {
					if (edi.EditorVisible(list[i]))
						visFields.add(i);
				}

				if (item instanceof Proc.REVIVE)
					System.out.println(visFields);
				if (visFields.size() == list.length - 1)
					setComponentVisibility(this, item.exists(), 1);
				if (visFields.size() > 0)
					setComponentVisibility(this, item.exists(), Ints.toArray(visFields));
			}
		}

		private ProcItem getProcItem() {
			return (ProcItem) obj;
		}

	}

	public interface EditorSupplier {

		Editor getEditor(EditControl<?> ctrl, EditorGroup g, String field, boolean edit);

		void setEditorVisibility(Editor e, boolean b);

		boolean EditorVisible(Editor e);

		boolean isEnemy();

	}

	static {
		EditControl<Proc.PROB> prob = new EditControl<>(Proc.PROB.class, (t) -> t.prob = MathUtil.clip(t.prob, 0, 100));

		EditControl<Proc.PT> pt = new EditControl<>(Proc.PT.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0)
				t.time = 0;
			else if (t.time == 0)
				t.time = 1;
		});

		EditControl<Proc.IMU> imu = new EditControl<>(Proc.IMU.class, (t) -> {
			t.mult = Math.min(t.mult, 100);
			t.block = Math.min(t.block, 100);
		});

		EditControl<Proc.IMUAD> imuad = new EditControl<>(Proc.IMUAD.class, (t) -> {
			t.mult = Math.min(t.mult, 100);
			t.block = Math.min(t.block, 100);
			if (t.mult != 0 || t.block != 0)
				t.smartImu = MathUtil.clip(t.smartImu, -1, 1);
			else
				t.smartImu = 0;
		});

		EditControl<Proc.WAVEI> wavei = new EditControl<>(Proc.WAVEI.class, (t) -> t.mult = Math.min(t.mult, 100));

		map().put("KB", new EditControl<>(Proc.PTD.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0) {
				t.dis = t.time = 0;
			} else {
				if (t.dis == 0)
					t.dis = Data.KB_DIS[Data.INT_KB];
				if (t.time <= 0)
					t.time = Data.KB_TIME[Data.INT_KB];
			}
		}));

		map().put("STOP", pt);

		map().put("SLOW", pt);

		map().put("CRIT", new EditControl<>(Proc.PM.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0)
				t.mult = 0;
			else if (t.mult == 0)
				t.mult = 200;
		}));

		map().put("WAVE", new EditControl<>(Proc.WAVE.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			t.lv = MathUtil.clip(t.lv, 1, 20);
			if (t.prob == 0)
				t.lv = 0;
		}));

		map().put("WEAK", new EditControl<>(Proc.WEAK.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0)
				t.mult = t.time = 0;
			else {
				t.time = Math.max(t.time, 1);
			}
		}));

		map().put("BREAK", prob);

		map().put("WARP", new EditControl<>(Proc.PTD.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0) {
				t.dis = t.time = 0;
			}
		}));

		map().put("CURSE", pt);

		map().put("STRONG", new EditControl<>(Proc.STRONG.class, (t) -> {
			t.health = MathUtil.clip(t.health, 0, 99);
			if (t.health == 0)
				t.mult = 0;
		}));

		map().put("LETHAL", prob);

		map().put("BURROW", new EditControl<>(Proc.BURROW.class, (t) -> {
			t.count = Math.max(t.count, -1);
			if (t.count == 0)
				t.dis = 0;
			else
				t.dis = Math.max(t.dis, 1);
		}));

		map().put("REVIVE", new EditControl<>(Proc.REVIVE.class, (t) -> {
			t.count = Math.max(t.count, -1);
			if (t.count == 0) {
				t.health = 0;
				t.time = 0;
				t.type.imu_zkill = false;
				t.type.revive_others = false;
				t.dis_0 = t.dis_1 = 0;
				t.type.range_type = 0;
				t.type.revive_non_zombie = false;
			} else {
				t.health = MathUtil.clip(t.health, 1, 100);
				t.time = Math.max(t.time, 1);
				if (!t.type.revive_others) {
					t.dis_0 = t.dis_1 = 0;
					t.type.range_type = 0;
					t.type.revive_non_zombie = false;
				} else {
					t.type.range_type = MathUtil.clip(t.type.range_type, 0, 3);
				}
			}
			setComponentVisibility("REVIVE", t.type.revive_others, 3, 4, 5, 7);
		}));

		map().put("SNIPER", prob);

		map().put("TIME", pt);

		map().put("SEAL", pt);

		map().put("SUMMON", new EditControl<>(Proc.SUMMON.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0) {
				t.dis = 0;
				t.id = null;
				t.mult = 0;
				t.time = 0;
				t.form = 0;
				t.type.anim_type = 0;
				t.type.fix_buff = false;
				t.type.ignore_limit = false;
				t.type.on_hit = false;
				t.type.on_kill = false;
				t.type.random_layer = false;
				t.type.same_health = false;
			} else {
				t.time = Math.max(0, t.time);
				EditorSupplier edi = UserProfile.getStatic("Editor_Supplier", () -> null);
				if (!edi.isEnemy()) {
					Unit u = Identifier.getOr(t.id, Unit.class);
					t.form = MathUtil.clip(t.form, 1, u.forms.length);
					if (!t.type.fix_buff)
						t.mult = MathUtil.clip(t.mult, -u.max - u.maxp, u.max + u.maxp);
					else
						t.mult = MathUtil.clip(t.mult, 1, u.max + u.maxp);
					setComponentVisibility("SUMMON", true, 12);
				} else {
					t.form = 1;
					t.mult = Math.max(1, t.mult);
					setComponentVisibility("SUMMON", false, 12);
				}
				t.type.anim_type = MathUtil.clip(t.type.anim_type, 0, 3);
			}
		}));

		map().put("MOVEWAVE", new EditControl<>(Proc.MOVEWAVE.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0) {
				t.dis = 0;
				t.itv = 0;
				t.speed = 0;
				t.time = 0;
				t.width = 0;
			} else {
				t.width = Math.max(0, t.width);
				t.time = Math.max(1, t.time);
				t.itv = Math.max(1, t.itv);
			}
		}));

		map().put("THEME", new EditControl<>(Proc.THEME.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0) {
				t.time = 0;
				t.id = null;
				t.mus = null;
				t.type = new Proc.THEME.TYPE();
			}
		}));

		map().put("POISON", new EditControl<>(Proc.POISON.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0) {
				t.damage = 0;
				t.itv = 0;
				t.time = 0;
				t.type.damage_type = 0;
				t.type.unstackable = false;
				t.type.ignoreMetal = false;
				t.type.modifAffected = false;
			} else {
				t.time = Math.max(1, t.time);
				t.itv = Math.max(1, t.itv);
				t.type.damage_type = MathUtil.clip(t.type.damage_type, 0, 3);
			}
		}));

		map().put("BOSS", prob);

		map().put("CRITI", imu);

		map().put("SATK", new EditControl<>(Proc.PM.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0)
				t.mult = 0;
		}));

		map().put("COUNTER", new EditControl<>(Proc.COUNTER.class, (t) -> {
			t.prob = MathUtil.clip(t.prob,0,100);
			if (t.prob > 0) {
				t.type.procType = MathUtil.clip(t.type.procType,0,3);
				t.type.counterWave = MathUtil.clip(t.type.counterWave, 0, 2);
				int min = t.minRange;
				t.minRange = Math.min(min, t.maxRange);
				t.maxRange = Math.max(min, t.maxRange);
			} else {
				t.damage = 0;
				t.minRange = 0;
				t.maxRange = 0;
				t.type.procType = 0;
				t.type.counterWave = 0;
				t.type.useOwnDamage = false;
				t.type.outRange = false;
				t.type.areaAttack = false;
			}
		}));

		map().put("IMUATK", pt);

		map().put("DMGCUT", new EditControl<>(Proc.DMGCUT.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0) {
				t.dmg = 0;
				t.reduction = 0;
				t.type.traitIgnore = false;
				t.type.procs = false;
				t.type.magnif = false;
			} else
				t.dmg = Math.max(t.dmg,0);
		}));

		map().put("DMGCAP", new EditControl<>(Proc.DMGCAP.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0) {
				t.dmg = 0;
				t.type.traitIgnore = false;
				t.type.nullify = false;
				t.type.procs = false;
				t.type.magnif = false;
			} else
				t.dmg = Math.max(t.dmg,0);
		}));

		map().put("POIATK", new EditControl<>(Proc.PM.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0)
				t.mult = 0;
		}));

		map().put("VOLC", new EditControl<>(Proc.VOLC.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0) {
				t.dis_0 = t.dis_1 = 0;
				t.time = 0;
			} else {
				t.time = Math.max(1, t.time / Data.VOLC_ITV) * Data.VOLC_ITV;
			}
		}));

		map().put("ARMOR", new EditControl<>(Proc.ARMOR.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0) {
				t.mult = t.time = 0;
			} else {
				t.time = Math.max(1, t.time);
			}
		}));

		map().put("SPEED", new EditControl<>(Proc.SPEED.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0) {
				t.speed = t.time = 0;
				t.type = 0;
			} else {
				t.time = Math.max(1, t.time);
				t.type = MathUtil.clip(t.type, 0, 2);
			}
		}));

		map().put("MINIWAVE", new EditControl<>(Proc.MINIWAVE.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);

			if (t.prob == 0) {
				t.lv = 0;
				t.multi = 0;
			} else {
				t.lv = MathUtil.clip(t.lv, 1, 20);

				if(t.multi == 0)
					t.multi = 20;
			}
		}));

		map().put("IMUKB", imu);

		map().put("IMUSTOP", imu);

		map().put("IMUSLOW", imu);

		map().put("IMUWAVE", wavei);

		map().put("IMUWEAK", imuad);

		map().put("IMUWARP", imu);

		map().put("IMUCURSE", imu);

		map().put("IMUPOIATK", imu);

		map().put("IMUVOLC", wavei);

		map().put("IMUSUMMON", imu);

		map().put("IMUSEAL", imu);

		map().put("IMUMOVING", wavei);

		map().put("IMUCANNON", new EditControl<>(Proc.CANNI.class, (t) -> {
			t.mult = Math.min(t.mult, 100);
			if (t.mult != 0)
				t.type = MathUtil.clip(t.type, 1, 127);
			else
				t.type = 0;
		}));

		map().put("IMUPOI", imuad);

		map().put("IMUARMOR", imuad);

		map().put("IMUSPEED", imuad);

		map().put("BARRIER", new EditControl<>(Proc.BARRIER.class, (t) -> {
			t.health = Math.max(0, t.health);
			if (t.health > 0) {
				t.regentime = Math.max(0, t.regentime);
				t.timeout = Math.max(0, t.timeout);
			} else {
				t.regentime = 0;
				t.timeout = 0;
				t.type.magnif = false;
			}
		}));

		map().put("DEMONSHIELD", new EditControl<>(Proc.DSHIELD.class, (t) -> {
			t.hp = Math.max(0, t.hp);

			if(t.hp == 0)
				t.regen = 0;
			else
				t.regen = Math.max(0, t.regen);
		}));

		map().put("SHIELDBREAK", prob);

		map().put("DEATHSURGE", new EditControl<>(Proc.VOLC.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0) {
				t.dis_0 = t.dis_1 = 0;
				t.time = 0;
			} else {
				t.time = Math.max(1, t.time / Data.VOLC_ITV) * Data.VOLC_ITV;
			}
		}));

		map().put("BOUNTY", new EditControl<>(Proc.MULT.class, (t) -> {}));
		map().put("ATKBASE", new EditControl<>(Proc.MULT.class, (t) -> {}));
	}

	private static void setComponentVisibility(EditorGroup egg, boolean boo, int... fields) {
		EditorSupplier edi = UserProfile.getStatic("Editor_Supplier", () -> null);
		if (fields.length > 2)
			for (int field : fields)
				edi.setEditorVisibility(egg.list[field], boo);
		else {
			int l1 = fields.length == 2 ? fields[1] : egg.list.length;
			for (int i = fields[0]; i < l1; i++)
				edi.setEditorVisibility(egg.list[i], boo);
		}
	}

	private static void setComponentVisibility(String proc, boolean boo, int... fields) {
		EditorGroup egg = eg.getOrDefault(proc, null);
		if (egg == null) {
			System.out.println("There is no proc named " + proc);
			return;
		}

		setComponentVisibility(egg, boo, fields);
	}

	public static void setEditorSupplier(EditorSupplier sup) {
		UserProfile.setStatic("Editor_Supplier", sup);
	}

	private static Editor getEditor(EditControl<?> ctrl, EditorGroup g, String field, boolean edit) {
		EditorSupplier edi = UserProfile.getStatic("Editor_Supplier", () -> null);
		return edi.getEditor(ctrl, g, field, edit);
	}

	@SuppressWarnings("rawtypes")
	private static Map<String, EditControl> map() {
		return UserProfile.getRegister("Editor_EditControl", EditControl.class);
	}

}
