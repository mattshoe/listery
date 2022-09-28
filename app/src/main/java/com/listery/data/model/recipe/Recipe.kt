package com.listery.data.model.recipe

import androidx.room.Embedded
import androidx.room.Relation

data class Recipe(
    @Embedded
    val details: RecipeMetadata,

    @Relation(
        parentColumn = "recipeName",
        entityColumn = "recipeName",
        entity = Ingredient::class
    )
    val ingredients: List<Ingredient>
)

