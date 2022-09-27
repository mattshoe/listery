package com.listery.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.listery.data.room.Recipe
import com.listery.data.room.RecipeDao
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val recipeDao: RecipeDao
) : ViewModel() {

    val recipes: LiveData<List<Recipe>>
        get() = recipeDao.getAll()
}