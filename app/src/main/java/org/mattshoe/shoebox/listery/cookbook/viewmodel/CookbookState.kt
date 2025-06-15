package org.mattshoe.shoebox.listery.cookbook.viewmodel

import org.mattshoe.shoebox.listery.model.GenericErrorScreenState
import org.mattshoe.shoebox.listery.model.Recipe

sealed interface CookBookState {
    data class Success(
        val recipes: List<Recipe>,
        val filterOptions: List<FilterOption<*>>
    ): CookBookState

    data class Error(override val icon: Int, override val message: String): CookBookState, GenericErrorScreenState

    data object Loading: CookBookState
}

