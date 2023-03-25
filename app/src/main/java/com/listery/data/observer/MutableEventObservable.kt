package com.listery.data.observer

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class MutableEventObservable(
    scheduler: Scheduler = Schedulers.io()
): EventObservable(scheduler) {
    fun trigger() {
        postData(true)
    }
}