package org.mattshoe.shoebox.listery.recipe.viewmodel

import org.mattshoe.shoebox.listery.model.Recipe

sealed interface State {
    data object Loading: State
    data class Error(val error: Throwable): State
    data class Ready(val data: Recipe): State
}