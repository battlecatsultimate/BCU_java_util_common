package common.pack;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import common.pack.FixIndexList.FixIndexMap;
import common.pack.PackData.Identifier;

public interface IndexContainer {

	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface ContGetter {
	}

	public static interface Indexable<R extends IndexContainer, T extends Indexable<R, T>> {

		@SuppressWarnings("unchecked")
		public default R getCont() {
			return (R) getID().getCont();
		}

		public Identifier<T> getID();

	}

	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface IndexCont {

		Class<? extends IndexContainer> value();

	}

	public static interface Reductor<R, T> {

		public R reduce(R r, T t);

	}

	public String getID();

	@SuppressWarnings("rawtypes")
	public <R> R getList(Class cls, Reductor<R, FixIndexMap> func, R def);

	public default <T extends R, R extends Indexable<?, R>> Identifier<R> getNextID(Class<T> cls) {
		int id = getList(cls, (r, l) -> l.nextInd(), 0);
		return new Identifier<R>(getID(), cls, id);
	}

}