package com.listery.ui.recipes.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.listery.applyIOScheduler
import com.listery.data.model.UserRecipe
import com.listery.data.repository.IRecipeRepository
import com.listery.ui.BaseViewModel
import com.listery.ui.NoArgs
import javax.inject.Inject

class RecipeHomeViewModel @Inject constructor(
    application: Application,
    private val recipeRepository: IRecipeRepository
) : BaseViewModel<NoArgs>(application) {

    val recipes = MutableLiveData<List<UserRecipe>>()

    init {
        addDisposable(
            recipeRepository.onDataChanged.subscribe {
                loadData()
            }
        )
    }

    fun loadData() {
        addDisposable(
            recipeRepository.getRecipes()
                .applyIOScheduler()
                .subscribe(
                    {
                        recipes.postValue(it)
                    },
                    {}
                )
        )
    }
}