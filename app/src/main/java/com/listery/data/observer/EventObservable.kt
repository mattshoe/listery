package com.listery.data.observer

import io.reactivex.Scheduler

open class EventObservable(
    scheduler: Scheduler
): BaseDataObservable<Unit>(scheduler) {
    fun observe(observer: () -> Unit) {
        addObserver {
            observer()
        }
    }
}