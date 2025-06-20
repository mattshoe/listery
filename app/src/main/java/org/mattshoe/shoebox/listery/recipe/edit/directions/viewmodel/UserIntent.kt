package org.mattshoe.shoebox.listery.recipe.edit.directions.viewmodel

sealed interface UserIntent {
    data class AddStep(val instructions: String): UserIntent
    data class MoveStep(val oldIndex: Int, val newIndex: Int): UserIntent
    data class DeleteStep(val index: Int): UserIntent
    data class StartEdit(val index: Int): UserIntent
    data class UpdateStep(val index: Int, val instructions: String): UserIntent
}