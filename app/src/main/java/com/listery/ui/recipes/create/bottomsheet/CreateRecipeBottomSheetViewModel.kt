package com.listery.ui.recipes.create.bottomsheet

import android.app.Application
import com.listery.data.model.UserRecipe
import com.listery.data.observer.MutableEventObservable
import com.listery.data.repository.impl.RecipeRepository
import com.listery.data.room.entities.recipe.RecipeEntity
import com.listery.runInBackground
import com.listery.ui.BaseViewModel
import com.listery.ui.NoArgs
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class CreateRecipeBottomSheetViewModel @Inject constructor(
    application: Application,
    private val recipeRepository: RecipeRepository
): BaseViewModel<NoArgs>(application) {

    val recipeCreatedSuccessfully = MutableEventObservable(AndroidSchedulers.mainThread())
    val recipeAlreadyExists = MutableEventObservable()

    fun addRecipe(
        name: String,
        notes: String?
    ) {
        addDisposable(
            runInBackground(AndroidSchedulers.mainThread()) {
                recipeRepository.addRecipe(
                    UserRecipe(
                        RecipeEntity(
                            name,
                            notes ?: ""
                        ),
                        emptyList()
                    )
                )
            }.subscribe(
                {
                    recipeCreatedSuccessfully.trigger()
                },
                {
                    recipeAlreadyExists.trigger()
                }
            )
        )
    }
}