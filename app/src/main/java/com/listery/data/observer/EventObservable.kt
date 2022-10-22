package com.listery.data.observer

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

open class EventObservable(
    scheduler: Scheduler = Schedulers.io()
): BaseDataObservable<Unit>(scheduler) {
    fun observe(observer: () -> Unit) {
        addObserver {
            observer()
        }
    }
}