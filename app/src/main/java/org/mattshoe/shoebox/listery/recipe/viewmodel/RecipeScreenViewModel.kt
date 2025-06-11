package org.mattshoe.shoebox.listery.recipe.viewmodel

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
import org.mattshoe.shoebox.listery.navigation.NavigationProvider
import org.mattshoe.shoebox.listery.navigation.Route
import javax.inject.Inject

@HiltViewModel
class RecipeScreenViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val navigationProvider: NavigationProvider
): ListeryViewModel<State, UserIntent>(
    State.Loading
) {

    private val recipe = MutableStateFlow<Recipe?>(null)

    init {
        recipe.onEach { recipe ->
            recipe?.let {
                _state.update {
                    State.Ready(data = recipe)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun initialize(recipeName: String) {
        recipeRepository.observe(recipeName)
            .onEach { updatedRecipe ->
                recipe.update {
                    updatedRecipe
                }
            }.launchIn(viewModelScope)
    }

    override fun handleIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.EditRecipeOverview -> handleEditRecipeOverview(intent)
            is UserIntent.EditIngredients -> handleEditIngredients(intent)
            is UserIntent.EditDirections -> handleEditDirections(intent)
            is UserIntent.ToggleStarred -> handleToggleStarred(intent)
            is UserIntent.AddToShoppingList -> handleAddToShoppingList(intent)
            is UserIntent.DeleteRecipe -> handleDeleteRecipe(intent)
        }
    }

    private fun handleEditRecipeOverview(intent: UserIntent.EditRecipeOverview) = viewModelScope.launch {
        recipe.value?.let {
            navigationProvider.navController.navigate(
                Route.EditRecipeOverviewBottomSheet(it.name)
            )
        }
    }

    private fun handleEditIngredients(intent: UserIntent.EditIngredients) = viewModelScope.launch {
        // TODO: Navigate to edit ingredients screen
    }

    private fun handleEditDirections(intent: UserIntent.EditDirections) = viewModelScope.launch {
        // TODO: Navigate to edit directions screen
    }

    private fun handleToggleStarred(intent: UserIntent.ToggleStarred) = viewModelScope.launch {
        val currentState = _state.value
        if (currentState is State.Ready) {
            val updatedRecipe = currentState.data.copy(starred = !currentState.data.starred)
            recipeRepository.upsert(updatedRecipe)
            _state.update { State.Ready(updatedRecipe) }
        }
    }

    private fun handleAddToShoppingList(intent: UserIntent.AddToShoppingList) = viewModelScope.launch {
        // TODO: Add recipe ingredients to shopping list
    }

    private fun handleDeleteRecipe(intent: UserIntent.DeleteRecipe) = viewModelScope.launch {
        recipe.value?.let {
            recipeRepository.remove(it.name)
        }
        navigationProvider.navController.popBackStack()
    }
}

