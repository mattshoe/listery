package org.mattshoe.shoebox.listery.recipe.viewmodel

sealed interface UserIntent {
    data object EditRecipeOverview : UserIntent
    data object AddIngredient : UserIntent
    data object EditDirections : UserIntent
    data object ToggleStarred : UserIntent
    data object AddToShoppingList : UserIntent
    data object DeleteRecipe : UserIntent
    data class EditIngredient(val id: String) : UserIntent
    data class DeleteIngredient(val index: Int) : UserIntent
}