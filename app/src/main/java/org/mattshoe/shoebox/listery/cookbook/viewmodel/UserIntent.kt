package org.mattshoe.shoebox.listery.cookbook.viewmodel

import org.mattshoe.shoebox.listery.model.Recipe

sealed interface UserIntent {
    data object NewRecipe: UserIntent
    data class RecipeTapped(val recipe: Recipe): UserIntent
    data class SearchUpdated(val searchText: String): UserIntent
    data object FilterIconTapped: UserIntent
    data class RecipeStarTapped(val recipe: Recipe): UserIntent
    data class FilterUpdated(val list: List<FilterOption<*>>, val option: FilterOption<*>): UserIntent
}