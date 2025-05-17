package org.mattshoe.shoebox.listery.recipe.viewmodel

import org.mattshoe.shoebox.listery.model.Recipe

sealed interface ScreenState {
    data object Loading: ScreenState
    data class Error(val error: Throwable): ScreenState
    data class Ready(val data: Recipe): ScreenState
}