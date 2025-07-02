package org.mattshoe.shoebox.listery.recipe.viewmodel

import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.model.GenericErrorScreenState
import org.mattshoe.shoebox.listery.model.Recipe

sealed interface State {
    data object Loading: State
    data class Error(
        override val icon: Int = R.drawable.ic_crossed_silverware,
        override val message: String = "Oops! Looks like the kitchen's under renovation. Try again soon."
    ): State, GenericErrorScreenState
    data class Ready(
        val data: Recipe,
        val editIngredient: Int? = null
    ): State
}