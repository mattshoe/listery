package org.mattshoe.shoebox.listery.recipe.edit.ingredient.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import org.mattshoe.shoebox.listery.data.RecipeRepository
import org.mattshoe.shoebox.listery.logging.loge
import org.mattshoe.shoebox.listery.model.EditableField
import org.mattshoe.shoebox.listery.model.Ingredient
import org.mattshoe.shoebox.listery.navigation.NavigationProvider
import javax.inject.Inject
import kotlin.text.isNullOrBlank

data class State(
    val loading: Boolean = false,
    val allowSubmit: Boolean = false,
    val name: EditableField<String?> = EditableField(null),
    val quantity: EditableField<String> = EditableField("1.0"),
    val unit: EditableField<String> = EditableField("whole")
)

sealed interface UserIntent {
    data class NameUpdated(val value: String?): UserIntent
    data class QuantityUpdated(val value: String): UserIntent
    data class UnitUpdated(val value: String): UserIntent
    data class Submit(val state: State): UserIntent
}

@HiltViewModel
class EditIngredientsViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val navigationProvider: NavigationProvider
): ListeryViewModel<State, UserIntent>(State()) {

    private var recipeName: String? = null

    override fun handleIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.NameUpdated -> handleNameUpdated(intent)
            is UserIntent.QuantityUpdated -> handleQuantityUpdated(intent)
            is UserIntent.UnitUpdated -> handleUnitUpdated(intent)
            is UserIntent.Submit -> handleSubmit(intent)
        }
    }

    fun initialize(recipeName: String) {
        this.recipeName = recipeName
    }

    fun handleNameUpdated(intent: UserIntent.NameUpdated) = viewModelScope.launch {
        _state.update {
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
        _state.update {
            it.copy(
                quantity = it.quantity.copy(
                    value = intent.value,
                    error = null
                )
            )
        }
    }

    fun handleUnitUpdated(intent: UserIntent.UnitUpdated) = viewModelScope.launch {
        _state.update {
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
            _state.update {
                it.copy(
                    name = it.name.copy(
                        error = "Ingredient must have a name."
                    )
                )
            }
        } else {
            recipeName?.let { recipeName ->
                _state.update { it.copy(loading = true) }
                delay(1000)
                try {
                    recipeRepository.fetch(recipeName)?.let { recipe ->
                        recipeRepository.upsert(
                            recipe.copy(
                                ingredients = recipe.ingredients.toMutableList().apply {
                                    add(
                                        Ingredient(
                                            intent.state.name.value,
                                            intent.state.quantity.value.toFloatOrNull() ?: 0f,
                                            intent.state.unit.value
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