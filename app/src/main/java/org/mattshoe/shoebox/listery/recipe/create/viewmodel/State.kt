package org.mattshoe.shoebox.listery.recipe.create.viewmodel

data class State(
    val recipeName: String = "",
    val validName: Boolean = false,
    val loading: Boolean = false,
    val invalidityReason: String? = null
)