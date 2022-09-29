package com.listery.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.listery.data.model.UserRecipe
import com.listery.data.room.entities.recipe.RecipeEntity
import com.listery.data.repository.IRecipeRepository
import com.listery.ui.BaseViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel @Inject constructor(
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