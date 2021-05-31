package common.debug;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class DebugCore {

	public static interface Accessor {

		public List<? extends Entry> getEntries();

		public Entry getParentEntry();

		public default String getPath() {
			return getParentEntry().getParentAccessor().getPath() + " > " + getShortName();
		}

		public default String getShortName() {
			return getParentEntry().getEntryName();
		}

		public Object getTarget();

		public Class<?> getTargetClass();

	}

	public static class ArrayAccessor implements Accessor {

		public Object target;
		public Entry parent;

		public ArrayAccessor(Entry parent, Object arr) {
			this.parent = parent;
			this.target = arr;
		}

		@Override
		public List<ArrayEntry> getEntries() {
			int n = Array.getLength(target);
			List<ArrayEntry> list = new ArrayList<>();
			for (int i = 0; i < n; i++) {
				list.add(new ArrayEntry(this, i));
			}
			return list;
		}

		@Override
		public Entry getParentEntry() {
			return parent;
		}

		@Override
		public Object getTarget() {
			return target;
		}

		@Override
		public Class<?> getTargetClass() {
			return target.getClass();
		}

	}

	public static class ArrayEntry implements Entry {

		public final ArrayAccessor parent;
		public final int index;

		public ArrayEntry(ArrayAccessor accessor, int index) {
			this.parent = accessor;
			this.index = index;
		}

		@Override
		public Object get() {
			try {
				return Array.get(parent.target, index);
			} catch (Exception e) {
				return e;
			}
		}

		@Override
		public String getEntryName() {
			return "[" + index + "]";
		}

		@Override
		public ArrayAccessor getParentAccessor() {
			return parent;
		}

	}

	public static interface Entry {

		public static boolean canAccessClass(Object o) {
			return o != null && !(o instanceof Throwable) && !(o instanceof Number) && !(o instanceof Boolean)
					&& !(o instanceof String);
		}

		public default boolean canAccess() {
			Object o = get();
			return canAccessClass(o);
		}

		public Object get();

		public default Accessor getChild() {
			if (!canAccess())
				return null;
			Object o = get();
			if (o.getClass().isArray())
				return new ArrayAccessor(this, o);
			if (o instanceof List)
				return new ListAccessor(this, (List<?>) o);
			if (o instanceof Map)
				return new MapAccessor(this, (Map<?, ?>) o);
			if (o instanceof List)
				return new SetAccessor(this, (Set<?>) o);
			if (o instanceof Map.Entry<?, ?>)
				return new MapEntryAccessor(this, (Map.Entry<?, ?>) o);
			return new ObjectAccessor(this, o);
		}

		public String getEntryName();

		public Accessor getParentAccessor();

		public default String getValueText() {
			Object o = get();
			if (o instanceof Collection)
				return o.getClass().getSimpleName();
			if (o instanceof Map)
				return o.getClass().getSimpleName();
			return get() + "";
		}
	}

	public static class FieldEntry implements Entry {

		public final Accessor parent;
		public final Field field;

		public FieldEntry(Accessor parent, Field field) {
			this.parent = parent;
			this.field = field;
		}

		@Override
		public Object get() {
			try {
				field.setAccessible(true);
				return field.get(parent.getTarget());
			} catch (Exception e) {
				return e;
			}
		}

		@Override
		public String getEntryName() {
			return field.getName();
		}

		@Override
		public Accessor getParentAccessor() {
			return parent;
		}

	}

	public static class ListAccessor implements Accessor {

		public List<?> target;
		public Entry parent;

		public ListAccessor(Entry parent, List<?> list) {
			this.parent = parent;
			this.target = list;
		}

		@Override
		public List<ListEntry> getEntries() {
			int n = target.size();
			List<ListEntry> ans = new ArrayList<>();
			for (int i = 0; i < n; i++) {
				ans.add(new ListEntry(this, i));
			}
			return ans;
		}

		@Override
		public Entry getParentEntry() {
			return parent;
		}

		@Override
		public List<?> getTarget() {
			return target;
		}

		@Override
		public Class<?> getTargetClass() {
			return target.getClass();
		}

	}

	public static class ListEntry implements Entry {

		public final ListAccessor parent;
		public final int index;

		public ListEntry(ListAccessor accessor, int index) {
			this.parent = accessor;
			this.index = index;
		}

		@Override
		public Object get() {
			try {
				return parent.target.get(index);
			} catch (Exception e) {
				return e;
			}
		}

		@Override
		public String getEntryName() {
			return "[" + index + "]";
		}

		@Override
		public ListAccessor getParentAccessor() {
			return parent;
		}

	}

	public static class MapAccessor implements Accessor {

		public Map<?, ?> target;
		public Entry parent;

		public MapAccessor(Entry parent, Map<?, ?> map) {
			this.parent = parent;
			this.target = map;
		}

		@Override
		public List<MapEntry> getEntries() {
			List<MapEntry> ans = new ArrayList<>();
			int i = 0;
			for (Map.Entry<?, ?> o : target.entrySet()) {
				ans.add(new MapEntry(this, i++, o));
			}
			return ans;
		}

		@Override
		public Entry getParentEntry() {
			return parent;
		}

		@Override
		public Map<?, ?> getTarget() {
			return target;
		}

		@Override
		public Class<?> getTargetClass() {
			return target.getClass();
		}

	}

	public static class MapEntry implements Entry {

		public final MapAccessor parent;
		public final int index;
		public final Map.Entry<?, ?> entry;

		public MapEntry(MapAccessor accessor, int index, Map.Entry<?, ?> entry) {
			this.parent = accessor;
			this.index = index;
			this.entry = entry;
		}

		@Override
		public Object get() {
			return Entry.canAccessClass(entry.getKey()) ? entry : entry.getValue();
		}

		@Override
		public String getEntryName() {
			return Entry.canAccessClass(entry.getKey()) ? "[" + index + "]" : ("" + entry.getKey());
		}

		@Override
		public MapAccessor getParentAccessor() {
			return parent;
		}

	}

	public static class MapEntryAccessor implements Accessor {

		public final Entry parent;
		public final Map.Entry<?, ?> target;

		public MapEntryAccessor(Entry parent, Map.Entry<?, ?> entry) {
			this.parent = parent;
			this.target = entry;
		}

		@Override
		public List<? extends Entry> getEntries() {
			List<SupplierEntry> ans = new ArrayList<>();
			ans.add(new SupplierEntry(this, "key", () -> target.getKey()));
			ans.add(new SupplierEntry(this, "value", () -> target.getValue()));
			return ans;
		}

		@Override
		public Entry getParentEntry() {
			return parent;
		}

		@Override
		public Map.Entry<?, ?> getTarget() {
			return target;
		}

		@Override
		public Class<?> getTargetClass() {
			return target.getClass();
		}

	}

	public static class ObjectAccessor implements Accessor {

		public final Entry parent;
		public final Object target;

		public ObjectAccessor(Entry parent, Object target) {
			this.parent = parent;
			this.target = target;
		}

		@Override
		public List<FieldEntry> getEntries() {
			List<FieldEntry> list = new ArrayList<>();
			Class<?> cls = target.getClass();
			while (cls != Object.class) {
				for (Field f : cls.getDeclaredFields()) {
					if ((f.getModifiers() & Modifier.STATIC) == 0)
						list.add(new FieldEntry(this, f));
				}
				cls = cls.getSuperclass();
			}
			return list;
		}

		@Override
		public Entry getParentEntry() {
			return parent;
		}

		@Override
		public Object getTarget() {
			return target;
		}

		@Override
		public Class<?> getTargetClass() {
			return target.getClass();
		}

	}

	public static class SetAccessor implements Accessor {

		public Set<?> target;
		public Entry parent;

		public SetAccessor(Entry parent, Set<?> list) {
			this.parent = parent;
			this.target = list;
		}

		@Override
		public List<SetEntry> getEntries() {
			List<SetEntry> ans = new ArrayList<>();
			int i = 0;
			for (Object o : target) {
				ans.add(new SetEntry(this, i++, o));
			}
			return ans;
		}

		@Override
		public Entry getParentEntry() {
			return parent;
		}

		@Override
		public Set<?> getTarget() {
			return target;
		}

		@Override
		public Class<?> getTargetClass() {
			return target.getClass();
		}

	}

	public static class SetEntry implements Entry {

		public final SetAccessor parent;
		public final int index;
		public final Object value;

		public SetEntry(SetAccessor accessor, int index, Object value) {
			this.parent = accessor;
			this.index = index;
			this.value = value;
		}

		@Override
		public Object get() {
			return value;
		}

		@Override
		public String getEntryName() {
			return "[" + index + "]";
		}

		@Override
		public SetAccessor getParentAccessor() {
			return parent;
		}

	}

	public static class StaticContainer implements Accessor {

		public final List<Entry> list = new ArrayList<>();

		public StaticContainer(Class<?>... cls) {
			for (Class<?> c : cls)
				list.add(new StaticPointer(this, c));
		}

		public void setFocus(Object o) {
			list.removeIf(e -> e instanceof SupplierEntry);
			list.add(new SupplierEntry(this, "focus", () -> o));
		}

		@Override
		public List<? extends Entry> getEntries() {
			return list;
		}

		@Override
		public Entry getParentEntry() {
			return null;
		}

		@Override
		public String getPath() {
			return getShortName();
		}

		@Override
		public String getShortName() {
			return "Root";
		}

		@Override
		public Object getTarget() {
			return list;
		}

		@Override
		public Class<?> getTargetClass() {
			return null;
		}

	}

	public static class StaticPointer implements Accessor, Entry {

		public final Class<?> cls;
		public final StaticContainer cont;

		public StaticPointer(StaticContainer cont, Class<?> cls) {
			this.cls = cls;
			this.cont = cont;
		}

		@Override
		public boolean canAccess() {
			return true;
		}

		@Override
		public Object get() {
			return null;
		}

		@Override
		public Accessor getChild() {
			return this;
		}

		@Override
		public List<FieldEntry> getEntries() {
			List<FieldEntry> list = new ArrayList<>();
			for (Field f : cls.getDeclaredFields()) {
				if ((f.getModifiers() & Modifier.STATIC) > 0)
					list.add(new FieldEntry(this, f));
			}
			return list;
		}

		@Override
		public String getEntryName() {
			return "class";
		}

		@Override
		public Accessor getParentAccessor() {
			return cont;
		}

		@Override
		public Entry getParentEntry() {
			return this;
		}

		@Override
		public String getPath() {
			return cont.getPath() + " > " + getShortName();
		}

		@Override
		public String getShortName() {
			return cls.getSimpleName();
		}

		@Override
		public Object getTarget() {
			return null;
		}

		@Override
		public Class<?> getTargetClass() {
			return cls;
		}

		@Override
		public String getValueText() {
			return getShortName();
		}

	}

	public static class SupplierEntry implements Entry {

		public final Accessor parent;
		public final String name;
		public final Supplier<?> supplier;

		public SupplierEntry(Accessor parent, String name, Supplier<?> supplier) {
			this.parent = parent;
			this.name = name;
			this.supplier = supplier;
		}

		@Override
		public Object get() {
			return supplier.get();
		}

		@Override
		public String getEntryName() {
			return name;
		}

		@Override
		public Accessor getParentAccessor() {
			return parent;
		}

	}

	public static StaticContainer DEBUG_LOCK = null;

	/**
	 * This method cannot be called by listeners, which are called through Event
	 * Dispatch Threads.
	 */
	public static void sync_debug(Object o) {
		if (DEBUG_LOCK == null)
			return;
		synchronized (DEBUG_LOCK) {
			try {
				if (o != null)
					DEBUG_LOCK.setFocus(o);
				DEBUG_LOCK.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void async_debug(Object o) {
		if (DEBUG_LOCK == null)
			return;
		if (o != null)
			DEBUG_LOCK.setFocus(o);
	}

}
