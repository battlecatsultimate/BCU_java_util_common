package common.pack;

import common.battle.data.CustomEntity;
import common.io.json.JsonClass;
import common.util.Data;
import common.util.stage.CastleImg;
import common.util.unit.AbEnemy;
import common.util.unit.CustomTrait;
import common.util.unit.EneRand;
import common.util.unit.Enemy;

import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonClass(noTag = JsonClass.NoTag.LOAD)
public class Identifier<T extends IndexContainer.Indexable<?, T>> implements Comparable<Identifier<?>>, Cloneable {

	public static final String DEF = "000000";

	static final String STATIC_FIXER = "id_fixer";

	@Nullable
	public static <T extends IndexContainer.Indexable<?, T>> T get(Identifier<T> id) {
		return id == null ? null : id.get();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@NotNull
	public static <T> T getOr(Identifier<?> id, Class<T> cls) {
		if (id != null) {
			IndexContainer ic = (IndexContainer) getContainer(id.cls, id.pack);
			if (ic != null) {
				Object ans = ic.getList(id.cls, (r, l) -> r == null ? l.get(id.id) : r, null);
				if (ans != null)
					return (T) ans;
			}
		}

		if(cls == EneRand.class) {
			return (T) new Identifier(DEF, Enemy.class, 0).get();
		} else {
			return (T) new Identifier(DEF, cls, 0).get();
		}
	}

	/**
	 * cls must be a class implementing Indexable. interfaces or other classes will
	 * go through fixer
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IndexContainer.Indexable<?, T>> Identifier<T> parseInt(int v, Class<? extends T> cls) {
		return parseIntRaw(v, cls);
	}

	@Deprecated
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Identifier parseIntRaw(int v, Class<?> cls) {
		if (cls == AbEnemy.class) {
			if (v < 1000 || v % 1000 < 500)
				cls = Enemy.class;
			else {
				cls = EneRand.class;
				v -= 500;
			}
		}
		if (cls == null || cls.isInterface() || !IndexContainer.Indexable.class.isAssignableFrom(cls))
			cls = UserProfile.getStatic(STATIC_FIXER, () -> new VerFixer.IdFixer(null)).parse(v, cls);
		String pack = cls != CastleImg.class && v / 1000 == 0 ? DEF : Data.hex(v / 1000);
		int id = v % 1000;
		return new Identifier(pack, cls, id);
	}

	private static Object getContainer(Class<?> cls, String str) {
		IndexContainer.IndexCont cont = null;
		Queue<Class<?>> q = new ArrayDeque<>();
		q.add(cls);
		while (q.size() > 0) {
			Class<?> ci = q.poll();
			if ((cont = ci.getAnnotation(IndexContainer.IndexCont.class)) != null)
				break;
			if (ci.getSuperclass() != null)
				q.add(ci.getSuperclass());
			Collections.addAll(q, ci.getInterfaces());
		}
		if (cont == null)
			return null;
		Method m = null;
		for (Method mi : cont.value().getMethods())
			if (mi.getAnnotation(IndexContainer.ContGetter.class) != null)
				m = mi;
		if (m == null)
			return null;
		Method fm = m;
		return Data.err(() -> fm.invoke(null, str));
	}

	public Class<? extends T> cls;
	public String pack;
	public int id;

	@Deprecated
	public Identifier() {
		cls = null;
		pack = null;
		id = 0;
	}

	public Identifier(String pack, Class<? extends T> cls, int id) {
		this.cls = cls;
		this.pack = pack;
		this.id = id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Identifier<T> clone() {
		return (Identifier<T>) Data.err(super::clone);
	}

	@Override
	public int compareTo(Identifier<?> identifier) {
		int val = pack.compareTo(identifier.pack);
		if (val != 0)
			return val;
		val = Integer.compare(cls.hashCode(), identifier.cls.hashCode());
		if (val != 0)
			return val;
		return Integer.compare(id, identifier.id);
	}

	public boolean equals(Identifier<T> o) {
		return pack.equals(o.pack) && id == o.id && o.cls == cls;
	}

	@JsonClass.JCGetter
	@SuppressWarnings("unchecked")
	public T get() {
		IndexContainer cont = getCont();
		return (T) cont.getList(cls, (r, l) -> r == null ? l.get(id) : r, null);
	}

	public IndexContainer getCont() {
		return (IndexContainer) getContainer(cls, pack);
	}

	public boolean isNull() {
		return id == -1;
	}

	@Override
	public String toString() {
		return pack + "/" + id;
	}

}
