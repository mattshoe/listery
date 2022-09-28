package com.listery.data.repository

import androidx.lifecycle.LiveData
import com.listery.data.model.recipe.Recipe

interface IRecipeRepository {

    fun getRecipes(): LiveData<List<Recipe>>

}