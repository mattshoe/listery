package com.listery.ui

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class BaseViewModel(
    application: Application
): AndroidViewModel(application) {
    private val disposableContainer = CompositeDisposable()

    protected fun addDisposable(disposable: Disposable) {
        disposableContainer.add(disposable)
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposableContainer.clear()
    }
}