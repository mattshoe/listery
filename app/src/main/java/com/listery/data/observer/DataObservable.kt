package com.listery.data.observer

import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

open class DataObservable<T>(
    scheduler: Scheduler = Schedulers.io()
): BaseDataObservable<T>(scheduler) {

    fun observe(observer: (T) -> Unit): Disposable {
        return addObserver(observer)
    }

}

