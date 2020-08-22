package common.pack;

import common.pack.FixIndexList.FixIndexMap;

import java.lang.annotation.*;

public interface IndexContainer {

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
	@interface ContGetter {
    }

    interface Indexable<R extends IndexContainer, T extends Indexable<R, T>> {

        @SuppressWarnings("unchecked")
		default R getCont() {
            return (R) getID().getCont();
        }

        Identifier<T> getID();

    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
	@interface IndexCont {

        Class<? extends IndexContainer> value();

    }

    interface Reductor<R, T> {

        R reduce(R r, T t);

    }

    String getID();

    @SuppressWarnings("rawtypes")
	<R> R getList(Class cls, Reductor<R, FixIndexMap> func, R def);

    default <T extends R, R extends Indexable<?, R>> Identifier<R> getNextID(Class<T> cls) {
        int id = getList(cls, (r, l) -> l.nextInd(), 0);
        return new Identifier<R>(getID(), cls, id);
    }

}