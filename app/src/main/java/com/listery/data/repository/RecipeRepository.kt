package com.listery.data.repository

import androidx.lifecycle.LiveData
import com.listery.data.model.recipe.Recipe
import com.listery.data.room.RecipeDao
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val recipeDao: RecipeDao
) : IRecipeRepository {

    override fun getRecipes(): LiveData<List<Recipe>> {
        return recipeDao.getAll()
    }
}