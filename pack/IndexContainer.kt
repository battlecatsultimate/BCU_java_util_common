package common.pack

import common.pack.FixIndexList.FixIndexMap
import common.pack.IndexContainer.Reductor
import kotlin.reflect.KClass

interface IndexContainer {
    @MustBeDocumented
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
    annotation class ContGetter
    interface Indexable<R : IndexContainer?, T : Indexable<R, T>?> {
        fun getCont(): R {
            return getID().getCont() as R
        }

        fun getID(): PackData.Identifier<T>
    }

    @MustBeDocumented
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
    annotation class IndexCont(val value: KClass<out IndexContainer>)

    fun interface Reductor<R, T> {
        fun reduce(r: R, t: T): R
    }

    fun getID(): String

    fun <R> getList(cls: Class<*>, func: Reductor<R, FixIndexMap<*>>, def: R): R

    fun <T : R, R : Indexable<*, R>> getNextID(cls: Class<T>): PackData.Identifier<R> {
        val id = getList(cls, Reductor<Int, FixIndexMap<*>> { r: Int, l: FixIndexMap<*> -> l.nextInd() }, 0)
        return PackData.Identifier(getID(), cls, id)
    }
}
