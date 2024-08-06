package org.mattshoe.shoebox.listery.model

data class Recipe(
    val name: String,
    val starred: Boolean,
    val url: String,
    val calories: Int,
    val ingredientCount: Int,
    val prepTime: String
)