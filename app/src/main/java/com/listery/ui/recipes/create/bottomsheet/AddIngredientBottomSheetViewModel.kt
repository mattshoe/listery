package com.listery.ui.recipes.create.bottomsheet

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.listery.applyIOScheduler
import com.listery.data.model.UserIngredient
import com.listery.data.observer.MutableEventObservable
import com.listery.data.repository.impl.RecipeRepository
import com.listery.data.room.entities.MeasurementUnitEntity
import com.listery.data.room.entities.recipe.IngredientEntity
import com.listery.runInBackground
import com.listery.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class AddIngredientBottomSheetViewModel @Inject constructor(
    application: Application,
    private val recipeRepository: RecipeRepository
): BaseViewModel<AddIngredientBottomSheetArgs>(application) {

    val ingredientAdded = MutableEventObservable(AndroidSchedulers.mainThread())
    val ingredientsList = MutableLiveData<List<IngredientEntity>>()
    val unitList = MutableLiveData<List<MeasurementUnitEntity>>()

    init {
        addDisposable(
            recipeRepository.getIngredients()
                .applyIOScheduler()
                .subscribe(
                    {
                        ingredientsList.postValue(it)
                    },
                    {}
                )
        )
        addDisposable(
            recipeRepository.getUnits()
                .applyIOScheduler()
                .subscribe(
                    {
                        unitList.postValue(it)
                    },
                    {}
                )
        )
    }

    fun addIngredient(
        name: String,
        quantity: Double,
        unit: String
    ) {
        addDisposable(
            runInBackground(AndroidSchedulers.mainThread()) {
                recipeRepository.addIngredientForRecipe(
                    UserIngredient(
                        IngredientEntity(name),
                        quantity,
                        MeasurementUnitEntity(unit)
                    ),
                    arguments.recipeName
                )
            }.subscribe(
                {
                    ingredientAdded.trigger()
                },
                {
                    Log.e("MATTSHOE", it.message ?: "wut")
                }
            )
        )

    }
}