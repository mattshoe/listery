package com.listery.data.repository

import com.listery.data.model.UserIngredient
import com.listery.data.model.UserRecipe
import com.listery.data.repository.impl.DataStatus
import com.listery.data.room.entities.recipe.IngredientEntity
import com.listery.data.room.entities.recipe.RecipeIngredientEntity
import com.listery.data.observer.DataObservable
import com.listery.data.room.entities.MeasurementUnitEntity
import io.reactivex.Single

interface IRecipeRepository: MutableRepository {

    fun getRecipes(): Single<List<UserRecipe>>
    fun getRecipe(name: String): Single<UserRecipe>
    fun getRecipeIngredients(recipeName: String): Single<List<RecipeIngredientEntity>>
    fun getIngredients(): Single<List<IngredientEntity>>
    fun getIngredients(recipeName: String): Single<List<IngredientEntity>>
    fun getIngredient(name: String): Single<IngredientEntity>
    fun getUnits(): Single<List<MeasurementUnitEntity>>
    fun addIngredientForRecipe(ingredient: UserIngredient, recipeName: String)
    fun addRecipe(userRecipe: UserRecipe)
    fun deleteRecipe(userRecipe: UserRecipe)

}