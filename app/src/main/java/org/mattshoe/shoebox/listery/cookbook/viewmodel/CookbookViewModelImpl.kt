package org.mattshoe.shoebox.listery.cookbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mattshoe.shoebox.listery.data.RecipeRepository
import javax.inject.Inject

@HiltViewModel
class CookBookViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
): ViewModel() {
    private val _state = MutableStateFlow<CookBookState>(CookBookState.Loading)
    val state: StateFlow<CookBookState> = _state.asStateFlow()

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

    fun handleUserIntent(intent: UserIntent) {
        // TODO: when -> ...
    }
}