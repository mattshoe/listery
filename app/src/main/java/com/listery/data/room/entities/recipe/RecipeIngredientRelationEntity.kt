package com.listery.data.room.entities.recipe

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "recipe_ingredient_relation",
    primaryKeys = ["recipeId", "ingredientId"],
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = arrayOf("name"),
            childColumns = arrayOf("recipeId"),
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = IngredientEntity::class,
            parentColumns = arrayOf("name"),
            childColumns = arrayOf("ingredientId"),
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class RecipeIngredientRelationEntity(
    val recipeId: String,
    val ingredientId: String
)