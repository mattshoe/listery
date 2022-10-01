package com.listery.data.observer

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

open class MutableDataObservable<T>(
    scheduler: Scheduler = Schedulers.io()
): DataObservable<T>(scheduler) {
    fun post(data: T) {
        postData(data)
    }
}