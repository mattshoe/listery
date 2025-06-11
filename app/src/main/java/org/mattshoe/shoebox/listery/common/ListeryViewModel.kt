package org.mattshoe.shoebox.listery.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class ListeryViewModelWithArgs<State: Any, Intent, Args: Any>(
    initialState: State
): ListeryViewModel<State, Intent>(
    initialState
) {
    protected lateinit var args: Args

    fun init(args: Args) {
        this.args = args
    }
}

abstract class ListeryViewModel<State: Any, Intent>(
    initialState: State
): ViewModel() {
    protected val _state = MutableStateFlow(initialState)

    val state = _state.asStateFlow()

    abstract fun handleIntent(intent: Intent)
}