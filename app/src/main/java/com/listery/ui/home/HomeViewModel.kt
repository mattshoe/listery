package com.listery.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.listery.data.repository.IRecipeRepository
import com.listery.data.model.recipe.Recipe
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val recipeRepository: IRecipeRepository
) : ViewModel() {

    val recipes: LiveData<List<Recipe>>
        get() = recipeRepository.getRecipes()

}