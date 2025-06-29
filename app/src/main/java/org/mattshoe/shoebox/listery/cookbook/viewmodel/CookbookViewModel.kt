package org.mattshoe.shoebox.listery.cookbook.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import org.mattshoe.shoebox.listery.data.RecipeRepository
import org.mattshoe.shoebox.listery.logging.log
import org.mattshoe.shoebox.listery.logging.loge
import org.mattshoe.shoebox.listery.navigation.NavigationProvider
import org.mattshoe.shoebox.listery.navigation.Routes
import javax.inject.Inject

private const val TAG = "CookBookViewModel"

@HiltViewModel
class CookBookViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    @ApplicationContext private val applicationContext: Context,
    private val navigationProvider: NavigationProvider
): ListeryViewModel<CookBookState, UserIntent>(
    CookBookState.Loading
) {
    private val filterOptions = listOf(
        FilterOption.Toggle("Starred", null),
        FilterOption.IntRange("Calories", FilterRange(null, null)),
        FilterOption.IntMax("Ingredients", null)
    )
    private val filterCriteria = MutableStateFlow(CookBookFilterCriteria("", filterOptions))

    init {
        log("Initializing CookBookViewModel")

        viewModelScope.launch {
            combine(
                recipeRepository.userRecipes,
                filterCriteria
            ) { recipes, filters ->
                log("Received ${recipes.size} recipes from repository")
                val filteredRecipes = recipes
                    .filter { recipe ->
                        val nameMatches = filters.text.isBlank() || recipe.name.contains(filters.text, ignoreCase = true)
                        nameMatches && filters.filterOptions.all { filterOption ->
                            if (filterOption.selectedValue == null) {
                                return@all true
                            } else {
                                when (filterOption.title) {
                                    "Starred" -> recipe.starred == filterOption.selectedValue
                                    "Calories" -> {
                                        recipe.calories == null || with (filterOption as FilterOption.IntRange) {
                                            val matchesMin = filterOption.selectedValue.min == null || recipe.calories >= filterOption.selectedValue.min
                                            val matchesMax = filterOption.selectedValue.max == null || recipe.calories <= filterOption.selectedValue.max
                                            matchesMin && matchesMax
                                        }
                                    }
                                    "Ingredients" -> {
                                        recipe.ingredients.count() <= (filterOption.selectedValue as Int)
                                    }
                                    else -> true
                                }
                            }
                        }
                    }

                log("Filtered recipes count: ${filteredRecipes.count()}")

                updateState {
                    CookBookState.Success(filteredRecipes, filters.filterOptions)
                }
            }.catch {
                loge(it.stackTraceToString())
                updateState {
                    CookBookState.Error(R.drawable.ic_crossed_silverware, "Oops! Looks like the kitchen's under renovation. Try again soon.")
                }
            }.launchIn(viewModelScope)
        }
    }

    override fun handleIntent(intent: UserIntent) {
        log("Handling intent: $intent")
        viewModelScope.launch {
            when (intent) {
                is UserIntent.NewRecipe -> handleNewRecipe(intent)
                is UserIntent.RecipeTapped -> handleRecipeTapped(intent)
                is UserIntent.SearchUpdated -> handleSearchUpdated(intent)
                is UserIntent.FilterIconTapped -> handleFilterIconTapped(intent)
                is UserIntent.RecipeStarTapped -> handleStarTapped(intent)
                is UserIntent.FilterUpdated -> handleFilterUpdated(intent)
            }
        }
    }

    private suspend fun handleNewRecipe(intent: UserIntent.NewRecipe) {
        navigationProvider.navController.navigate(Routes.ChooseRecipeCreationMethodBottomSheet)
    }

    private suspend fun handleRecipeTapped(intent: UserIntent.RecipeTapped) {
        navigationProvider.navController.navigate(
            Routes.Recipe(intent.recipe.id)
        )
    }

    private suspend fun handleSearchUpdated(intent: UserIntent.SearchUpdated) {
        filterCriteria.update {
            it.copy(
                text = intent.searchText
            )
        }
    }

    private suspend fun handleFilterIconTapped(intent: UserIntent.FilterIconTapped) {

    }

    private suspend fun handleStarTapped(intent: UserIntent.RecipeStarTapped) {
        recipeRepository.upsert(
            intent.recipe.copy(
                starred = !intent.recipe.starred
            )
        )
    }

    private suspend fun handleFilterUpdated(intent: UserIntent.FilterUpdated) {
        filterCriteria.update {
            it.copy(
                filterOptions = intent.list
            )
        }
    }
}

data class CookBookFilterCriteria(
    val text: String,
    val filterOptions: List<FilterOption<*>>
)

fun <T: Any> T?.ifNull(default: T): T = this ?: default