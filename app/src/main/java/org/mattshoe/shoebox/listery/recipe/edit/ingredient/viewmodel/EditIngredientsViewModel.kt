package org.mattshoe.shoebox.listery.recipe.edit.ingredient.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import org.mattshoe.shoebox.listery.data.RecipeRepository
import org.mattshoe.shoebox.listery.logging.loge
import org.mattshoe.shoebox.listery.model.EditableField
import org.mattshoe.shoebox.listery.model.Ingredient
import org.mattshoe.shoebox.listery.model.Recipe
import org.mattshoe.shoebox.listery.navigation.NavigationProvider
import java.util.UUID
import javax.inject.Inject
import kotlin.text.isNullOrBlank

@HiltViewModel
class EditIngredientsViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val navigationProvider: NavigationProvider
): ListeryViewModel<State, UserIntent>(State()) {

    private var recipeId: String? = null
    private var ingredientId: String? = null
    private lateinit var recipe: Recipe

    fun initialize(recipeId: String, ingredientId: String?) {
        this.recipeId = recipeId
        this.ingredientId = ingredientId

        viewModelScope.launch {
            recipeRepository.fetch(recipeId)?.let {
                recipe = it
                try {
                    ingredientId?.let { id ->
                        recipe.ingredients.find { it.id == id }?.let { ingredient ->
                            updateState {
                                it.copy(
                                    loading = false,
                                    name = EditableField(ingredient.name),
                                    quantity = EditableField(ingredient.qty.toString()),
                                    unit = EditableField(ingredient.unit),
                                    calories = EditableField(ingredient.calories.toString()),
                                    allowSubmit = ingredient.name.isNotBlank()
                                )
                            }
                        }
                    }
                } catch (e: Throwable) {
                    loge(e)
                }
            } ?: run {
                loge("Recipe could not be found for ingredient")
                navigationProvider.navController.popBackStack()
            }
        }
    }

    override fun handleIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.NameUpdated -> handleNameUpdated(intent)
            is UserIntent.QuantityUpdated -> handleQuantityUpdated(intent)
            is UserIntent.UnitUpdated -> handleUnitUpdated(intent)
            is UserIntent.CaloriesUpdated -> handleCaloriesUpdated(intent)
            is UserIntent.Submit -> handleSubmit(intent)
        }
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

    fun handleCaloriesUpdated(intent: UserIntent.CaloriesUpdated) = viewModelScope.launch {
        updateState {
            it.copy(
                calories = EditableField(intent.value)
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
            if (this@EditIngredientsViewModel::recipe.isInitialized) {
                updateState { it.copy(loading = true) }
                try {
                    val newIngredients = recipe.ingredients.toMutableList().apply {
                        val index = indexOfFirst { it.id == ingredientId }
                        val oldIngredient = if (index > -1) removeAt(index) else null
                        add(
                            Ingredient(
                                oldIngredient?.id ?: UUID.randomUUID().toString(),
                                intent.state.name.value,
                                intent.state.quantity.value.toFloatOrNull() ?: 0f,
                                intent.state.unit.value,
                                intent.state.calories.value.toIntOrNull() ?: 0
                            )
                        )

                    }
                    recipeRepository.upsert(
                        recipe.copy(
                            ingredients = newIngredients
                        )
                    )
                } catch (e: Throwable) {
                    loge("Error inserting ingredient", e)
                } finally {
                    navigationProvider.navController.popBackStack()
                }
            }
        }
    }
}