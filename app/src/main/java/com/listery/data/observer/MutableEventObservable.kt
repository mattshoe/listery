package com.listery.data.observer

import io.reactivex.Scheduler

class MutableEventObservable(
    scheduler: Scheduler
): EventObservable(scheduler) {
    fun trigger() {
        postData(Unit)
    }
}