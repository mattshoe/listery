package com.listery.ui

import android.app.Application
import android.util.Log
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

    protected lateinit var arguments: TArgs
        private set

    fun setArguments(args: TArgs) {
        this.arguments = args
        onArgumentsSet(args)
    }

    protected open fun onArgumentsSet(args: TArgs) { }

    protected fun addDisposable(disposable: Disposable) {
        disposableContainer.add(disposable)
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposableContainer.clear()
        Log.d("MATTSHOE", "Clearing viewmodel: ${this::class.java.simpleName}")
    }
}