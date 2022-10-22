package com.listery

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

fun <T, U> Single<List<T>>.flatMapEach(
    mapper: (T) -> Single<U>
): Single<List<U>> {
    return flattenAsObservable { it }
        .flatMap {
            mapper.invoke(it).toObservable()
        }.toList()
}

fun <T> runInBackground(observeOn: Scheduler = Schedulers.io(), action: () -> T): Single<T> {
    return Single.fromCallable {
            action.invoke()
        }
        .subscribeOn(Schedulers.io())
        .observeOn(observeOn)
}

fun <T> Single<T>.applyIOScheduler(): Single<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
}

fun <T> Observable<T>.applyIOScheduler(): Observable<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
}