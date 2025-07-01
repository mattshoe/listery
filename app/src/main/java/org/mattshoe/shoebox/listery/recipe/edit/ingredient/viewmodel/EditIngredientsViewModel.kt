package org.mattshoe.shoebox.listery.recipe.edit.ingredient.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import org.mattshoe.shoebox.listery.data.RecipeRepository
import org.mattshoe.shoebox.listery.logging.loge
import org.mattshoe.shoebox.listery.model.Ingredient
import org.mattshoe.shoebox.listery.navigation.NavigationProvider
import javax.inject.Inject
import kotlin.text.isNullOrBlank

@HiltViewModel
class EditIngredientsViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val navigationProvider: NavigationProvider
): ListeryViewModel<State, UserIntent>(State()) {

    private var recipeId: String? = null

    override fun handleIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.NameUpdated -> handleNameUpdated(intent)
            is UserIntent.QuantityUpdated -> handleQuantityUpdated(intent)
            is UserIntent.UnitUpdated -> handleUnitUpdated(intent)
            is UserIntent.Submit -> handleSubmit(intent)
        }
    }

    fun initialize(recipeId: String) {
        this.recipeId = recipeId
    }

    fun handleNameUpdated(intent: UserIntent.NameUpdated) = viewModelScope.launch {
        updateState {
            it.copy(
                allowSubmit = !intent.value.isNullOrBlank(),
                name = it.name.copy(
                    value = intent.value,
                    error = if (intent.value.isNullOrBlank()) "Ingredient must have a name." else null
                )
            )
        }
    }

    fun handleQuantityUpdated(intent: UserIntent.QuantityUpdated) = viewModelScope.launch {
        updateState {
            it.copy(
                quantity = it.quantity.copy(
                    value = intent.value,
                    error = null
                )
            )
        }
    }

    fun handleUnitUpdated(intent: UserIntent.UnitUpdated) = viewModelScope.launch {
        updateState {
            it.copy(
                unit = it.unit.copy(
                    value = intent.value,
                    error = null
                )
            )
        }
    }

    fun handleSubmit(intent: UserIntent.Submit) = viewModelScope.launch {
        if (intent.state.name.value.isNullOrBlank()) {
            updateState {
                it.copy(
                    name = it.name.copy(
                        error = "Ingredient must have a name."
                    )
                )
            }
        } else {
            recipeId?.let { recipeName ->
                updateState { it.copy(loading = true) }
                try {
                    recipeRepository.fetch(recipeName)?.let { recipe ->
                        recipeRepository.upsert(
                            recipe.copy(
                                ingredients = recipe.ingredients.toMutableList().apply {
                                    add(
                                        Ingredient(
                                            intent.state.name.value,
                                            intent.state.quantity.value.toFloatOrNull() ?: 0f,
                                            intent.state.unit.value,
                                            intent.state.calories.value
                                        )
                                    )

                                }
                            )
                        )
                    }
                } catch (e: Throwable) {
                    loge("Error inserting ingredient", e)
                } finally {
                    navigationProvider.navController.popBackStack()
                }
            }
        }
    }
}