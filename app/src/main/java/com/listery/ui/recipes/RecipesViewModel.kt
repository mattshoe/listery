package com.listery.ui.recipes

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.listery.data.model.UserRecipe
import com.listery.data.repository.IRecipeRepository
import com.listery.ui.BaseViewModel
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RecipesViewModel @Inject constructor(
    application: Application,
    private val recipeRepository: IRecipeRepository
) : BaseViewModel(application) {

    val recipes: MutableLiveData<List<UserRecipe>> = MutableLiveData()

    init {
        addDisposable(
            recipeRepository.getRecipes()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        recipes.postValue(it)
                    },
                    {}
                )
        )
    }
}