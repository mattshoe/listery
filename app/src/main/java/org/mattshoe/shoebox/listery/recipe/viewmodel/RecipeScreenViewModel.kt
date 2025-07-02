package org.mattshoe.shoebox.listery.recipe.viewmodel

import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import org.mattshoe.shoebox.listery.data.RecipeRepository
import org.mattshoe.shoebox.listery.model.Recipe
import org.mattshoe.shoebox.listery.navigation.NavigationProvider
import org.mattshoe.shoebox.listery.navigation.Routes
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
        recipe
            .onEach { recipe ->
                recipe?.let {
                    updateState {
                        State.Ready(
                            data = recipe.copy(
                                ingredients = recipe.ingredients.sortedBy { it.name.uppercase() },
                            )
                        )
                    }
                }
            }
            .catch {
                updateState {
                    State.Error()
                }
            }
            .launchIn(viewModelScope)
    }

    fun initialize(recipeId: String) {
        recipeRepository.observe(recipeId)
            .onEach { updatedRecipe ->
                recipe.update {
                    updatedRecipe
                }
            }.launchIn(viewModelScope)
    }

    override fun handleIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.EditRecipeOverview -> handleEditRecipeOverview(intent)
            is UserIntent.AddIngredient -> handleEditIngredients(intent)
            is UserIntent.EditDirections -> handleEditDirections(intent)
            is UserIntent.ToggleStarred -> handleToggleStarred(intent)
            is UserIntent.AddToShoppingList -> handleAddToShoppingList(intent)
            is UserIntent.DeleteRecipe -> handleDeleteRecipe(intent)
            is UserIntent.DeleteIngredient -> handleDeleteIngredient(intent)
            is UserIntent.EditIngredient -> handleEditIngredient(intent)
        }
    }

    private fun handleEditRecipeOverview(intent: UserIntent.EditRecipeOverview) = viewModelScope.launch {
        recipe.value?.let {
            navigationProvider.navController.navigate(
                Routes.EditRecipeOverviewBottomSheet(it.id)
            )
        }
    }

    private fun handleEditIngredients(intent: UserIntent.AddIngredient) = viewModelScope.launch {
        recipe.value?.let {
            navigationProvider.navController.navigate(
                Routes.EditIngredientsBottomSheet(it.id, null)
            )
        }
    }

    private fun handleEditDirections(intent: UserIntent.EditDirections) = viewModelScope.launch {
        recipe.value?.let {
            navigationProvider.navController.navigate(
                Routes.EditDirectionsBottomSheet(it.id)
            )
        }
    }

    private fun handleToggleStarred(intent: UserIntent.ToggleStarred) = viewModelScope.launch {
        recipe.value?.let {
            updateState { State.Loading }
            val updatedRecipe = it.copy(starred = !it.starred)
            recipeRepository.upsert(updatedRecipe)
        }
    }

    private fun handleAddToShoppingList(intent: UserIntent.AddToShoppingList) = viewModelScope.launch {
        // TODO: Add recipe ingredients to shopping list
    }

    private fun handleDeleteRecipe(intent: UserIntent.DeleteRecipe) = viewModelScope.launch {
        recipe.value?.let {
            updateState { State.Loading }
            recipeRepository.remove(it.id)
            navigationProvider.navController.popBackStack()
        }
    }

    private fun handleEditIngredient(intent: UserIntent.EditIngredient) = viewModelScope.launch {
        recipe.value?.id?.let {
            navigationProvider.navController.navigate(
                Routes.EditIngredientsBottomSheet(it, intent.id)
            )
        }

    }

    private fun handleDeleteIngredient(intent: UserIntent.DeleteIngredient) = viewModelScope.launch {
        recipe.value?.let { currentRecipe ->
            val updatedIngredients = currentRecipe.ingredients.toMutableList().apply {
                removeAt(intent.index)
            }
            val updatedRecipe = currentRecipe.copy(ingredients = updatedIngredients)
            recipeRepository.upsert(updatedRecipe)
        }
    }
}

