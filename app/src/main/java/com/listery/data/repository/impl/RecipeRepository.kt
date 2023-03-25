package com.listery.data.repository.impl

import com.listery.data.model.UserIngredient
import com.listery.data.model.UserRecipe
import com.listery.data.observer.MutableDataObservable
import com.listery.data.repository.IRecipeRepository
import com.listery.data.room.RecipeDao
import com.listery.data.room.entities.MeasurementUnitEntity
import com.listery.data.room.entities.recipe.IngredientEntity
import com.listery.data.room.entities.recipe.RecipeEntity
import com.listery.data.room.entities.recipe.RecipeIngredientEntity
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor(
    private val recipeDao: RecipeDao
) : IRecipeRepository {

    override val onDataChanged = PublishSubject.create<DataStatus>()
    override val onRecipeUpdated = ReplaySubject.create<String>()

    override fun getRecipes(): Single<List<UserRecipe>> {
        return recipeDao.getAllRecipeEntities()
            .flattenAsObservable { it }
            .flatMap { recipe ->
                gatherRecipeIngredients(recipe).toObservable()
            }.toList()
    }

    override fun getRecipe(name: String): Single<UserRecipe> {
        return recipeDao.getRecipeEntity(name).flatMap {
            gatherRecipeIngredients(it)
        }
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

    override fun getUnits(): Single<List<MeasurementUnitEntity>> {
        return recipeDao.getMeasurementUnits()
    }

    override fun addIngredientForRecipe(ingredient: UserIngredient, recipeName: String) {
        recipeDao.insertIngredientForRecipe(
            ingredient,
            RecipeEntity(recipeName, "")
        )
        onDataChanged.onNext(DataStatus.ADDED)
        onRecipeUpdated.onNext(recipeName)
    }

    override fun addRecipe(userRecipe: UserRecipe) {
        recipeDao.insertRecipeAndIngredients(userRecipe)
        onDataChanged.onNext(DataStatus.ADDED)
    }

    override fun deleteRecipe(userRecipe: UserRecipe) {
        val id = recipeDao.delete(userRecipe.entity)
        recipeDao.delete(userRecipe)
        onDataChanged.onNext(DataStatus.DELETED)
    }

    private fun gatherRecipeIngredients(recipe: RecipeEntity): Single<UserRecipe> {
        return getRecipeIngredients(recipe.name)
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
            }
    }
}