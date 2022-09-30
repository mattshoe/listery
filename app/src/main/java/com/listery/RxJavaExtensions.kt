package com.listery

import io.reactivex.Single

fun <T, U> Single<List<T>>.flatMapEach(
    mapper: (T) -> Single<U>
): Single<List<U>> {
    return flattenAsObservable { it }
        .flatMap {
            mapper.invoke(it).toObservable()
        }.toList()
}