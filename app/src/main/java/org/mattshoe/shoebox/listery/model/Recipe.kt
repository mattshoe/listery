package org.mattshoe.shoebox.listery.model

import kotlin.time.Duration

data class Recipe(
    val name: String,
    val starred: Boolean,
    val url: String?,
    val calories: Int?,
    val ingredients: List<Ingredient>,
    val prepTime: Duration?,
    val notes: String?,
    val steps: List<RecipeStep>
)

data class Ingredient(
    val name: String,
    val qty: Float,
    val unit: String
)

data class RecipeStep(
    val instructions: String
)

