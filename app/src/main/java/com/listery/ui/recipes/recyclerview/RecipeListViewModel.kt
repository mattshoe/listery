package com.listery.ui.recipes.recyclerview

import android.app.Application
import com.listery.applyIOScheduler
import com.listery.data.model.UserRecipe
import com.listery.data.repository.IRecipeRepository
import com.listery.runInBackground
import com.listery.ui.BaseViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RecipeListViewModel @Inject constructor(
    application: Application,
    private val recipeRepository: IRecipeRepository
): BaseViewModel(application) {

    fun delete(userRecipe: UserRecipe) {
        addDisposable(
            runInBackground {
                recipeRepository.deleteRecipe(userRecipe)
            }.subscribe()
        )
    }

}