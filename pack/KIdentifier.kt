package common.pack

fun <T : IndexContainer.Indexable<*, T>> Identifier<T>?.get(): T? {
    return this?.get()
}

fun <T> Identifier<*>?.getOr(cls : Class<T>): T {
    return Identifier.getOr(this, cls)
}