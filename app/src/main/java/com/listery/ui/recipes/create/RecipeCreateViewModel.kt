package com.listery.ui.recipes.create

import android.app.Application
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
        recipeRepository.onDataChanged.observe {
            loadData(arguments.recipeName)
        }
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