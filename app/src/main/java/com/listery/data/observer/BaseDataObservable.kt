package com.listery.data.observer

import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.atomic.AtomicBoolean

open class BaseDataObservable<T>(
    protected val scheduler: Scheduler
): Disposable {

    var value: T? = null
        private set

    private val isDisposed = AtomicBoolean(false)

    protected val disposableContainer = CompositeDisposable()
    protected val observable = BehaviorSubject.create<T>()

    protected fun addObserver(observer: (T) -> Unit) {
        disposableContainer.add(
            observable
                .subscribeOn(Schedulers.io())
                .observeOn(scheduler)
                .doOnNext(observer)
                .subscribe()
        )
    }

    protected fun postData(data: T) {
        value = data
        observable.onNext(data)
    }

    override fun dispose() {
        isDisposed.set(true)
        disposableContainer.dispose()
    }

    override fun isDisposed(): Boolean {
        return isDisposed.get()
    }
}