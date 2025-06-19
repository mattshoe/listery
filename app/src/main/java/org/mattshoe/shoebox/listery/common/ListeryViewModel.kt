package org.mattshoe.shoebox.listery.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class ListeryViewModel<State: Any, Intent>(
    initialState: State
): ViewModel() {
    private val _state = MutableStateFlow(initialState)

    val state = _state.asStateFlow()

    abstract fun handleIntent(intent: Intent)
    
    protected fun updateState(update: (currentState: State) -> State) {
        _state.update(update)
    }
}