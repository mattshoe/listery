package org.mattshoe.shoebox.listery.ai.generate.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.mattshoe.shoebox.listery.ai.generate.usecase.GenerateRecipeRequest
import org.mattshoe.shoebox.listery.ai.generate.usecase.GenerateRecipeUseCase
import org.mattshoe.shoebox.listery.ai.parseweb.usecase.ParseWebsiteUseCase
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import org.mattshoe.shoebox.listery.config.RemoteConfigRepository
import org.mattshoe.shoebox.listery.cuisine.data.CuisineRepository
import org.mattshoe.shoebox.listery.cuisine.model.Cuisine
import org.mattshoe.shoebox.listery.data.RecipeRepository
import org.mattshoe.shoebox.listery.lifestyle.data.LifestyleRepository
import org.mattshoe.shoebox.listery.lifestyle.model.Lifestyle
import org.mattshoe.shoebox.listery.logging.loge
import org.mattshoe.shoebox.listery.navigation.NavigationProvider
import org.mattshoe.shoebox.listery.navigation.Routes
import javax.inject.Inject

data class State(
    val cuisines: List<Cuisine> = emptyList(),
    val lifestyles: List<Lifestyle> = emptyList(),
    val prepTimes: List<String> = emptyList(),
    val selectedCuisine: Cuisine? = null,
    val selectedLifestyle: Lifestyle? = null,
    val selectedPrepTime: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val allowSubmit: Boolean
        get() = !isLoading
}

sealed interface UserIntent {
    data class CuisineSelected(val value: Cuisine?): UserIntent
    data class LifestyleSelected(val value: Lifestyle?): UserIntent
    data class PrepTimeSelected(val value: String?): UserIntent
    data class Submit(val state: State) : UserIntent
}

@HiltViewModel
class GenerateRecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val generateRecipeUseCase: GenerateRecipeUseCase,
    private val cuisineRepository: CuisineRepository,
    private val lifestyleRepository: LifestyleRepository,
    private val configRepository: RemoteConfigRepository,
    private val navigationProvider: NavigationProvider
): ListeryViewModel<State, UserIntent>(State()) {

    companion object {
        private const val PREP_TIME_CONFIG_KEY = "PREP_TIME_LIST"
    }

    init {
        combine(
            cuisineRepository.data.onEach {
                println(it)
            },
            lifestyleRepository.data.onEach {
                println(it)
            }
        ) { cuisines, lifestyles ->
            val prepTimeString = configRepository.getString(PREP_TIME_CONFIG_KEY)
            val prepTimes = prepTimeString.split(",").map { it.trim() }
            updateState {
                it.copy(
                    cuisines = cuisines,
                    lifestyles = lifestyles,
                    prepTimes = prepTimes
                )
            }
        }.launchIn(viewModelScope)
    }

    override fun handleIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.CuisineSelected -> handleCuisineSelected(intent)
            is UserIntent.LifestyleSelected -> handleLifestyleSelected(intent)
            is UserIntent.PrepTimeSelected -> handlePrepTimeSelected(intent)
            is UserIntent.Submit -> handleSubmit(intent)
        }
    }

    private fun handleCuisineSelected(intent: UserIntent.CuisineSelected) = viewModelScope.launch {
        updateState {
            it.copy(
                selectedCuisine = intent.value
            )
        }
    }

    private fun handleLifestyleSelected(intent: UserIntent.LifestyleSelected) = viewModelScope.launch {
        updateState {
            it.copy(
                selectedLifestyle = intent.value
            )
        }
    }

    private fun handlePrepTimeSelected(intent: UserIntent.PrepTimeSelected) = viewModelScope.launch {
        updateState {
            it.copy(
                selectedPrepTime = intent.value
            )
        }
    }

    private fun handleSubmit(intent: UserIntent.Submit) = viewModelScope.launch {
        try {
            updateState { it.copy(isLoading = true) }
            val recipe = generateRecipeUseCase.execute(
                GenerateRecipeRequest(
                    intent.state.selectedCuisine,
                    intent.state.selectedLifestyle,
                    intent.state.selectedPrepTime
                )
            )
            val id = recipeRepository.upsert(recipe)
            navigationProvider.navController.navigate(Routes.Recipe(id))
        } catch (e: Throwable) {
            loge(e)
        }
    }
}