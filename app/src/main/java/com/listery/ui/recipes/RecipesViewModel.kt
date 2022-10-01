package com.listery.ui.recipes

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.listery.applyIOScheduler
import com.listery.data.model.UserRecipe
import com.listery.data.repository.IRecipeRepository
import com.listery.ui.BaseViewModel
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RecipesViewModel @Inject constructor(
    application: Application,
    private val recipeRepository: IRecipeRepository
) : BaseViewModel(application) {

    val recipes = MutableLiveData<List<UserRecipe>>()

    init {
        addDisposable(
            recipeRepository.onDataChanged.observe {
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