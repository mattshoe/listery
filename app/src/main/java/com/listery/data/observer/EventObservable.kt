package com.listery.data.observer

import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

open class EventObservable(
    scheduler: Scheduler = Schedulers.io()
): BaseDataObservable<Boolean>(scheduler) {
    fun observe(observer: () -> Unit): Disposable {
        return addObserver {
            observer()
        }
    }
}