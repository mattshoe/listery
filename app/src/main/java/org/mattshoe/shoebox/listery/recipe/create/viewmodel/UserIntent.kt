package org.mattshoe.shoebox.listery.recipe.create.viewmodel

sealed interface UserIntent {
    data class NameUpdated(val name: String): UserIntent
    data class Submit(val name: String): UserIntent
}