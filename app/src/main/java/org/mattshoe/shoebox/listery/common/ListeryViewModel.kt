package org.mattshoe.shoebox.listery.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class ListeryViewModel<State: Any, Intent>(
    initialState: State
): ViewModel() {
    protected val _state = MutableStateFlow(initialState)
    protected val currentState: State?
        get() = _state.value

    val state = _state.asStateFlow()

    abstract fun handleIntent(intent: Intent)
    
    protected fun updateState(update: (currentState: State) -> State) {
        _state.update(update)
    }

    protected inline fun <reified T: State> updateStateAs(update: (currentState: T) -> State) {
        _state.update {
            if (it is T) {
                update(it)
            } else {
                it
            }
        }
    }
}