package com.listery.data.repository

import androidx.lifecycle.LiveData
import com.listery.data.room.Recipe

interface IRecipeRepository {

    fun getRecipes(): LiveData<List<Recipe>>

}