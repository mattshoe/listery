package org.mattshoe.shoebox.listery.recipe.edit.ingredient.viewmodel

sealed interface UserIntent {
    data class NameUpdated(val value: String?): UserIntent
    data class QuantityUpdated(val value: String): UserIntent
    data class UnitUpdated(val value: String): UserIntent
    data class CaloriesUpdated(val value: String): UserIntent
    data class Submit(val state: State): UserIntent
}