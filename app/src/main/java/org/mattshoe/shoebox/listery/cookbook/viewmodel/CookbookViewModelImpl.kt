package org.mattshoe.shoebox.listery.cookbook.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import org.mattshoe.shoebox.listery.data.RecipeRepository
import org.mattshoe.shoebox.listery.navigation.Routes
import javax.inject.Inject

@HiltViewModel
class CookBookViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    @ApplicationContext private val applicationContext: Context
): ListeryViewModel<CookBookState, UserIntent>(
    CookBookState.Loading
) {

    init {
        recipeRepository.recipes
            .onEach { recipeEmission ->
                _state.update {
                    CookBookState.Success(recipeEmission)
                }
            }.launchIn(viewModelScope)

        viewModelScope.launch {
            recipeRepository.fetch()
        }
    }

    override fun handleIntent(intent: UserIntent) {
        viewModelScope.launch {
            when (intent) {
                is UserIntent.NewRecipe -> handleNewRecipe(intent)
                is UserIntent.RecipeTapped -> handleRecipeTapped(intent)
                is UserIntent.SearchUpdated -> handleSearchUpdated(intent)
                is UserIntent.FilterIconTapped -> handleFilterIconTapped(intent)
                is UserIntent.RecipeStarTapped -> handleStarTapped(intent)
            }
        }
    }

    private suspend fun handleNewRecipe(intent: UserIntent.NewRecipe) {
        Toast.makeText(applicationContext, "Tapped FAB", Toast.LENGTH_LONG).show()
    }

    private suspend fun handleRecipeTapped(intent: UserIntent.RecipeTapped) {
        _navigationRoutes.emit(
            CookBookState.Navigate(
                Routes.Recipe(intent.recipe.name)
            )
        )
    }

    private suspend fun handleSearchUpdated(intent: UserIntent.SearchUpdated) {

    }

    private suspend fun handleFilterIconTapped(intent: UserIntent.FilterIconTapped) {

    }

    private suspend fun handleStarTapped(intent: UserIntent.RecipeStarTapped) {

    }
}