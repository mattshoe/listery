package org.mattshoe.shoebox.listery.recipe.edit.directions.viewmodel

import org.mattshoe.shoebox.listery.model.RecipeStep

data class State(
    val loading: Boolean = true,
    val activeEditIndex: Int? = null,
    val steps: List<RecipeStep> = emptyList()
)