package org.mattshoe.shoebox.listery.cookbook.viewmodel

import org.mattshoe.shoebox.listery.model.Recipe
import org.mattshoe.shoebox.listery.navigation.NavigateToRoute

sealed interface CookBookState {
    data class Success(val recipes: List<Recipe>): CookBookState
    data class Error(val message: String): CookBookState
    data object Loading: CookBookState
    data class Navigate(override val route: Any): CookBookState, NavigateToRoute
}