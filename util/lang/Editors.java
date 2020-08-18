package common.util.lang;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Consumer;

import org.jcodec.common.tools.MathUtil;

import common.pack.UserProfile;
import common.util.Data;
import common.util.Data.Proc;
import common.util.lang.ProcLang;
import common.util.lang.ProcLang.ItemLang;

public class Editors {

	public static class EditControl<T> {

		public final Class<T> cls;
		private final Consumer<T> regulator;

		public EditControl(Class<T> cls, Consumer<T> func) {
			this.cls = cls;
			regulator = func;
		}

		public Field getField(String f) {
			return Data.err(() -> {
				if (f.contains(".")) {
					String[] strs = f.split("\\.");
					return cls.getField(strs[0]).getType().getField(strs[1]);
				} else
					return cls.getField(f);
			});
		}

		@SuppressWarnings("unchecked")
		public final void update(EditorGroup par) {
			regulate((T) par.obj);
			par.setData(par.obj);
		}

		protected void regulate(T obj) {
			regulator.accept(obj);
		}

	}

	public static abstract class Editor {

		public final EditorGroup par;
		public final Field field;

		public Editor(EditorGroup par, Field field, String f) throws Exception {
			this.par = par;
			this.field = field;
		}

		/** notify that the data changed */
		protected abstract void setData();

		protected final void update() {
			par.ctrl.update(par);
		}

	}

	public static class EditorGroup {

		public final String proc; // Proc Title
		public final Class<?> cls; // ProcItem
		public final Editor[] list;
		public final EditControl<?> ctrl;

		public Object obj;

		public EditorGroup(String proc, boolean edit) throws Exception {
			this.proc = proc;
			this.cls = Proc.class.getDeclaredField(proc).getType();
			ctrl = map().get(proc);
			ItemLang item = ProcLang.get().get(proc);
			String[] arr = item.list();
			list = new Editor[arr.length];
			for (int i = 0; i < arr.length; i++) {
				list[i] = getEditor(ctrl, this, arr[i], edit);
			}
		}

		public void setData(Object obj) {
			this.obj = obj;
			for (Editor e : list)
				e.setData();
		}

	}

	public static interface EditorSupplier {

		public Editor getEditor(EditControl<?> ctrl, EditorGroup g, String field, boolean edit);

	}

	static {
		EditControl<Proc.PROB> prob = new EditControl<>(Proc.PROB.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
		});

		EditControl<Proc.PT> pt = new EditControl<>(Proc.PT.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0)
				t.time = 0;
			else
				t.time = Math.max(t.time, 1);
		});

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
			if (t.prob == 0)
				t.mult = 0;
			else if (t.mult == 0)
				t.mult = 200;
		}));

		map().put("WAVE", new EditControl<>(Proc.WAVE.class, (t) -> {
			if (t.prob == 0)
				t.lv = 0;
			t.lv = MathUtil.clip(t.lv, 1, 20);
		}));

		map().put("WEAK", new EditControl<>(Proc.WEAK.class, (t) -> {
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
			if (t.health == 0)
				t.mult = 0;
		}));

		map().put("LETHAL", prob);

		map().put("BURROW", new EditControl<>(Proc.BURROW.class, (t) -> {
			if (t.count == 0)
				t.dis = 0;
			else {
				t.count = Math.max(t.count, -1);
				t.dis = Math.max(t.dis, 1);
			}
		}));

		map().put("REVIVE", new EditControl<>(Proc.REVIVE.class, (t) -> {
			if (t.count == 0) {
				t.health = 0;
				t.time = 0;
				t.type.imu_zkill = false;

				t.type.revive_others = false;
				t.dis_0 = t.dis_1 = 0;
				t.type.range_type = 0;
				t.type.revive_non_zombie = false;
			} else {
				t.count = Math.max(t.count, -1);
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
		}));

		map().put("SNIPER", prob);

		map().put("TIME", pt);

		map().put("SEAL", pt);

		map().put("SUMMON", new EditControl<>(Proc.SUMMON.class, (t) -> {
			// FIXME
		}));

		map().put("MOVEWAVE", new EditControl<>(Proc.MOVEWAVE.class, (t) -> {
			// FIXME
		}));

		map().put("THEME", new EditControl<>(Proc.THEME.class, (t) -> {
			// FIXME
		}));

		map().put("POISON", new EditControl<>(Proc.POISON.class, (t) -> {
			// FIXME
		}));

		map().put("BOSS", prob);

		map().put("CRITI", new EditControl<>(Proc.CRITI.class, (t) -> {
			t.type = MathUtil.clip(0, t.type, 2);
		}));

		map().put("SATK", new EditControl<>(Proc.PM.class, (t) -> {
			if (t.prob == 0)
				t.mult = 0;
		}));

		map().put("IMUATK", pt);

		map().put("POIATK", new EditControl<>(Proc.PM.class, (t) -> {
			if (t.prob == 0)
				t.mult = 0;
		}));

		map().put("VOLC", new EditControl<>(Proc.VOLC.class, (t) -> {
			// FIXME
		}));

		map().put("ARMOR", new EditControl<>(Proc.ARMOR.class, (t) -> {
			// FIXME
		}));

		map().put("SPEED", new EditControl<>(Proc.SPEED.class, (t) -> {
			// FIXME
		}));

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
