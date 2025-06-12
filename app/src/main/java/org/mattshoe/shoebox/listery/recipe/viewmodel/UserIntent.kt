package org.mattshoe.shoebox.listery.recipe.viewmodel

sealed interface UserIntent {
    data object EditRecipeOverview : UserIntent
    data object EditIngredients : UserIntent
    data object EditDirections : UserIntent
    data object ToggleStarred : UserIntent
    data object AddToShoppingList : UserIntent
    data object DeleteRecipe : UserIntent
}