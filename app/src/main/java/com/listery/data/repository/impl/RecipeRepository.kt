package com.listery.data.repository.impl

import com.listery.data.model.UserIngredient
import com.listery.data.model.UserRecipe
import com.listery.data.observer.MutableDataObservable
import com.listery.data.repository.IRecipeRepository
import com.listery.data.room.RecipeDao
import com.listery.data.room.entities.recipe.IngredientEntity
import com.listery.data.room.entities.recipe.RecipeIngredientEntity
import io.reactivex.Single
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val recipeDao: RecipeDao
) : IRecipeRepository {

    override val onDataChanged = MutableDataObservable<DataStatus>()

    override fun getRecipes(): Single<List<UserRecipe>> {
        return recipeDao.getAllRecipeEntities()
            .flattenAsObservable { it }
            .flatMap { recipe ->
                getRecipeIngredients(recipe.name)
                    .map {
                        it
                    }
                    .flattenAsObservable { it }
                    .flatMap { recipeIngredient ->
                        recipeDao.getIngredientEntity(recipeIngredient.ingredientName).flatMap { ingredient ->
                            recipeIngredient.unit?.let { unitName ->
                                recipeDao.getMeasurementUnit(unitName).map { unit ->
                                    UserIngredient(ingredient, recipeIngredient.quantity, unit)
                                }
                            } ?: Single.just(UserIngredient(ingredient, recipeIngredient.quantity, null))
                        }.toObservable()
                    }.toList()
                    .map {
                        UserRecipe(recipe, it)
                    }.toObservable()
            }.toList()
    }

    override fun getRecipeIngredients(recipeName: String): Single<List<RecipeIngredientEntity>> {
        return recipeDao.getRecipeIngredients(recipeName)
    }

    override fun getIngredients(): Single<List<IngredientEntity>> {
        return recipeDao.getAllIngredientEntities()
    }

    override fun getIngredients(recipeName: String): Single<List<IngredientEntity>> {
        return recipeDao.getIngredientEntities(recipeName)
    }

    override fun getIngredient(name: String): Single<IngredientEntity> {
        return recipeDao.getIngredientEntity(name)
    }

    override fun addRecipe(userRecipe: UserRecipe) {
        recipeDao.insertRecipeAndIngredients(userRecipe)
    }

    override fun deleteRecipe(userRecipe: UserRecipe) {
        val id = recipeDao.delete(userRecipe.entity)
        recipeDao.delete(userRecipe)
        onDataChanged.post(DataStatus.DELETED)
    }
}