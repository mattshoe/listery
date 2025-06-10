package org.mattshoe.shoebox.listery.recipe.create.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import org.mattshoe.shoebox.listery.data.RecipeRepository
import org.mattshoe.shoebox.listery.model.Recipe
import org.mattshoe.shoebox.listery.navigation.NavigationProvider
import org.mattshoe.shoebox.listery.navigation.Route
import javax.inject.Inject

@HiltViewModel
class CreateRecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val navigationProvider: NavigationProvider
): ListeryViewModel<State, UserIntent>(State()) {

    override fun handleIntent(intent: UserIntent) {
        viewModelScope.launch(Dispatchers.Default) {
            when (intent) {
                is UserIntent.NameUpdated -> handleNameUpdated(intent)
                is UserIntent.Submit -> handleSubmit(intent)
            }
        }
    }

    private suspend fun handleNameUpdated(intent: UserIntent.NameUpdated) {
        when {
            intent.name.isBlank() -> _state
                .update {
                    it.copy(
                        recipeName = intent.name,
                        validName = false,
                        invalidityReason = null,
                        loading = false
                    )
                }
            recipeRepository.exists(intent.name) -> _state
                .update {
                    it.copy(
                        recipeName = intent.name,
                        validName = false,
                        invalidityReason = "This recipe already exists",
                        loading = false
                    )
                }
            else -> _state
                .update {
                    it.copy(
                        recipeName = intent.name,
                        validName = true,
                        invalidityReason = null,
                        loading = false
                    )
                }
        }

    }

    private suspend fun handleSubmit(intent: UserIntent.Submit) {
        if (!recipeRepository.exists(intent.name)) {
            _state.update {
                it.copy(loading = true)
            }
            delay(2000)
            recipeRepository.upsert(
                Recipe(
                    name = intent.name,
                    starred = false,
                    url = null,
                    calories = null,
                    ingredients = listOf(),
                    prepTime = "",
                    notes = null,
                    steps = listOf()
                )
            )
            _state.update {
                State()
            }
            withContext(Dispatchers.Main) {
                navigationProvider.navController.navigate(Route.Recipe(intent.name))
            }
        } else {
            _state.update {
                it.copy(
                    recipeName = intent.name,
                    validName = false,
                    invalidityReason = "This recipe already exists",
                    loading = false
                )
            }
        }
    }
}