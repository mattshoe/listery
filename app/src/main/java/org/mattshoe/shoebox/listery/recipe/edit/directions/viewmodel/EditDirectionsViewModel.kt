package org.mattshoe.shoebox.listery.recipe.edit.directions.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import org.mattshoe.shoebox.listery.data.RecipeRepository
import org.mattshoe.shoebox.listery.model.Recipe
import org.mattshoe.shoebox.listery.model.RecipeStep
import org.mattshoe.shoebox.listery.navigation.NavigationProvider
import javax.inject.Inject

@HiltViewModel
class EditDirectionsViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val navigationProvider: NavigationProvider
): ListeryViewModel<State, UserIntent>(State()) {

    private val recipe = MutableStateFlow<Recipe?>(null)

    init {
        recipe
            .onEach { recipe ->
                updateState {
                    it.copy(steps = recipe?.steps ?: emptyList(), loading = false)
                }
            }.launchIn(viewModelScope)
    }

    fun initialize(recipeName: String) = viewModelScope.launch {
        recipeRepository.observe(recipeName)
            .onEach { newRecipe ->
                recipe.update { newRecipe }
            }.launchIn(viewModelScope)
    }

    override fun handleIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.AddStep -> handleAddStep(intent)
            is UserIntent.MoveStep -> handleMoveStep(intent)
            is UserIntent.DeleteStep -> handleDeleteStep(intent)
            is UserIntent.StartEdit -> handleStartEdit(intent)
            is UserIntent.UpdateStep -> handleUpdateStep(intent)
        }
    }

    private fun handleAddStep(intent: UserIntent.AddStep) = viewModelScope.launch {
        recipe.value?.let { recipe ->
            recipeRepository.upsert(
                recipe.copy(
                    steps = recipe.steps + RecipeStep(instructions = intent.instructions),
                )
            )
        }
    }

    private fun handleMoveStep(intent: UserIntent.MoveStep) = viewModelScope.launch {
        recipe.value?.let { recipe ->
            val newList = recipe.steps.toMutableList().apply {
                val step = removeAt(intent.oldIndex)
                add(intent.newIndex, step)
            }

            recipeRepository.upsert(
                recipe.copy(
                    steps = newList
                )
            )
        }
    }

    private fun handleDeleteStep(intent: UserIntent.DeleteStep) = viewModelScope.launch {
        recipe.value?.let { recipe ->
            val newList = recipe.steps.toMutableList().apply {
                removeAt(intent.index)
            }
            recipeRepository.upsert(
                recipe.copy(
                    steps = newList
                )
            )
        }
        updateState {
            it.copy(activeEditIndex = null)
        }
    }

    private fun handleStartEdit(intent: UserIntent.StartEdit) = viewModelScope.launch {
        updateState {
            it.copy(
                activeEditIndex = intent.index
            )
        }
    }

    private fun handleUpdateStep(intent: UserIntent.UpdateStep) = viewModelScope.launch {
        recipe.value?.let { recipe ->
            val newList = recipe.steps.toMutableList().apply {
                removeAt(intent.index)
                add(intent.index, RecipeStep(instructions = intent.instructions))
            }
            recipeRepository.upsert(
                recipe.copy(
                    steps = newList
                )
            )
        }
        updateState {
            it.copy(activeEditIndex = null)
        }
    }
}