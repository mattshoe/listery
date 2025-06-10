package org.mattshoe.shoebox.listery.recipe.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import org.mattshoe.shoebox.listery.data.RecipeRepository
import javax.inject.Inject

@HiltViewModel
class RecipeScreenViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
): ListeryViewModel<State, UserIntent>(
    State.Loading
) {

    fun initialize(recipeName: String) = viewModelScope.launch {
        recipeRepository.fetch(recipeName)?.let { recipe ->
            _state.update {
                State.Ready(recipe)
            }
        }
    }

    override fun handleIntent(intent: UserIntent) {

    }
}

