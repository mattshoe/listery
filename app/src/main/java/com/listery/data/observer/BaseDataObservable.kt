package com.listery.data.observer

import android.util.Log
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observables.ConnectableObservable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.atomic.AtomicBoolean

open class BaseDataObservable<T>(
    protected val scheduler: Scheduler
): Disposable {

    var value: T? = null
        private set

    private val isDisposed = AtomicBoolean(false)

    protected val disposableContainer = CompositeDisposable()
    protected val observable = PublishSubject.create<T>()

    protected fun addObserver(observer: (T) -> Unit): Disposable{
        return observable
            .subscribe(
                {
                    observer(it)
                },
                {
                    Log.e("MATTSHOE", it.message ?: "")
                }
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