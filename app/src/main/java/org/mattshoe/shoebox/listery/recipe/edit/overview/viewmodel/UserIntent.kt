package org.mattshoe.shoebox.listery.recipe.edit.overview.viewmodel

sealed interface UserIntent {
    data class WebsiteUpdated(val value: String): UserIntent
    data class HoursUpdated(val value: String): UserIntent
    data class MinutesUpdated(val value: String): UserIntent
    data class NotesUpdated(val value: String): UserIntent
    data class ServingsUpdated(val value: Int): UserIntent

    data class Submit(val state: RecipeOverviewState): UserIntent
}