package com.listery.ui.recipes.create

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.listery.applyIOScheduler
import com.listery.data.model.UserIngredient
import com.listery.data.model.UserRecipe
import com.listery.data.repository.IRecipeRepository
import com.listery.ui.BaseViewModel
import com.listery.ui.NoArgs
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class RecipeCreateViewModel @Inject constructor(
    application: Application,
    private val recipeRepository: IRecipeRepository
) : BaseViewModel<RecipeCreateFragmentArgs>(application) {

    val recipe = MutableLiveData<UserRecipe>()

    init {
        addDisposable(
            recipeRepository.onDataChanged.subscribe(
                {
                    loadData(arguments.recipeName)
                },
                {
                    Log.e("MATTSHOE", it.message ?: "ugh")
                }
            )
        )

        addDisposable(
            recipeRepository.onRecipeUpdated.subscribe(
                {
                    if (recipe.value?.entity?.name == it) {
                        loadData(arguments.recipeName)
                    }
                },
                {
                    Log.e("MATTSHOE", it.message ?: "ugh")
                }
            )
        )
    }

    override fun onArgumentsSet(args: RecipeCreateFragmentArgs) {
        loadData(args.recipeName)
    }

    private fun loadData(recipeName: String? = null) {
        recipeName?.let {
            addDisposable(
                getRecipe(it)
            )
        }
    }

    private fun getRecipe(name: String): Disposable {
        return recipeRepository.getRecipe(name)
            .applyIOScheduler()
            .subscribe(
                { userRecipe ->
                    recipe.postValue(userRecipe)
                },
                { }
            )
    }
}