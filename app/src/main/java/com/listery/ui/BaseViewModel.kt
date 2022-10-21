package com.listery.ui

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import com.listery.data.observer.DataObservable
import com.listery.data.observer.MutableDataObservable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel<TArgs: Any>(
    application: Application
): AndroidViewModel(application) {
    private val disposableContainer = CompositeDisposable()

    protected val arguments: DataObservable<TArgs> = MutableDataObservable()

    fun setArguments(args: TArgs) {
        with (arguments as? MutableDataObservable) {
            this?.post(args)
        }
    }

    protected fun addDisposable(disposable: Disposable) {
        disposableContainer.add(disposable)
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposableContainer.clear()
    }
}