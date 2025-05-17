package org.mattshoe.shoebox.listery.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class ListeryViewModel<State: Any, Intent>(
    initialState: State
): ViewModel() {
    protected val _navigationRoutes = MutableSharedFlow<Any>(replay = 0)
    protected val _state = MutableStateFlow(initialState)

    val state = _state.asStateFlow()
    val navigationRoutes = _navigationRoutes.asSharedFlow()

    abstract fun handleIntent(intent: Intent)
}