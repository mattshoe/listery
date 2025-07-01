package org.mattshoe.shoebox.listery.model

import java.util.UUID
import kotlin.time.Duration

data class Recipe(
    val id: String = "",
    val name: String,
    val starred: Boolean,
    val url: String?,
    val ingredients: List<Ingredient>,
    val prepTime: Duration?,
    val notes: String?,
    val steps: List<RecipeStep>
) {
    val calories = ingredients.sumOf { it.calories }
}

data class Ingredient(
    val name: String,
    val qty: Float,
    val unit: String,
    val calories: Int
)

data class RecipeStep(
    val instructions: String,
    val key: String = UUID.randomUUID().toString()
)

