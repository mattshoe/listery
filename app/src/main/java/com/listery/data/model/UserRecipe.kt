package com.listery.data.model

import com.listery.data.room.entities.recipe.RecipeEntity

data class UserRecipe(
    val entity: RecipeEntity,
    val userIngredients: List<UserIngredient>,
)