package com.listery.data.repository

import com.listery.data.model.UserRecipe
import com.listery.data.room.entities.recipe.IngredientEntity
import com.listery.data.room.entities.recipe.RecipeEntity
import com.listery.data.room.entities.recipe.RecipeIngredientEntity
import io.reactivex.Single

interface IRecipeRepository {

    fun getRecipes(): Single<List<UserRecipe>>
    fun getRecipeIngredients(recipeName: String): Single<List<RecipeIngredientEntity>>
    fun getIngredients(): Single<List<IngredientEntity>>
    fun getIngredients(recipeName: String): Single<List<IngredientEntity>>
    fun getIngredient(name: String): Single<IngredientEntity>
    fun addRecipe(userRecipe: UserRecipe)

}