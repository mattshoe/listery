package org.mattshoe.shoebox.listery.recipe.edit.overview.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import org.mattshoe.shoebox.listery.cookbook.ui.prettyHours
import org.mattshoe.shoebox.listery.cookbook.ui.prettyMinutes
import org.mattshoe.shoebox.listery.data.RecipeRepository
import org.mattshoe.shoebox.listery.model.EditableField
import org.mattshoe.shoebox.listery.model.Recipe
import org.mattshoe.shoebox.listery.navigation.NavigationProvider
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
class EditRecipeOverviewViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val navigationProvider: NavigationProvider
): ListeryViewModel<RecipeOverviewState, UserIntent>(RecipeOverviewState(loading = true)) {

    private lateinit var recipeName: String
    private val recipeStream = MutableSharedFlow<Recipe>(replay = 1)

    init {
        recipeStream
            .onEach { recipe ->
                updateState { currentState ->
                    currentState.copy(
                        loading = false,
                        allowSubmit = true,
                        name = EditableField(recipeName, enabled = false),
                        website = EditableField(recipe.url),
                        hours = EditableField(recipe.prepTime.prettyHours()),
                        minutes = EditableField(recipe.prepTime.prettyMinutes()),
                        calories = EditableField(recipe.calories?.toString()),
                        notes = EditableField(recipe.notes)
                    )
                }
            }.launchIn(viewModelScope)
    }

    fun init(recipeName: String) {
        this.recipeName = recipeName
        recipeRepository.observe(recipeName)
            .onEach { recipe ->
                recipe?.let {
                    recipeStream.emit(it)
                } ?: navigationProvider.navController.popBackStack()
            }.launchIn(viewModelScope)
    }

    override fun handleIntent(intent: UserIntent) {
        viewModelScope.launch(Dispatchers.Default) {
            when (intent) {
                is UserIntent.CaloriesUpdated -> handleCaloriesUpdated(intent)
                is UserIntent.HoursUpdated -> handleHoursUpdated(intent)
                is UserIntent.MinutesUpdated -> handleMinutesUpdated(intent)
                is UserIntent.NotesUpdated -> handleNotesUpdated(intent)
                is UserIntent.WebsiteUpdated -> handleWebsiteUpdated(intent)
                is UserIntent.Submit -> handleSubmit(intent)
            }
        }
    }

    private suspend fun handleHoursUpdated(intent: UserIntent.HoursUpdated) {
        updateState {
            it.copy(hours = EditableField(intent.value))
        }
    }

    private suspend fun handleMinutesUpdated(intent: UserIntent.MinutesUpdated) {
        updateState {
            it.copy(minutes = EditableField(intent.value))
        }
    }

    private suspend fun handleCaloriesUpdated(intent: UserIntent.CaloriesUpdated) {
        updateState {
            it.copy(calories = EditableField(intent.value))
        }
    }

    private suspend fun handleWebsiteUpdated(intent: UserIntent.WebsiteUpdated) {
        updateState {
            it.copy(website = EditableField(intent.value))
        }
    }

    private suspend fun handleNotesUpdated(intent: UserIntent.NotesUpdated) {
        updateState {
            it.copy(notes = EditableField(intent.value))
        }
    }

    private suspend fun handleSubmit(intent: UserIntent.Submit) {
        updateState {
            it.copy(loading = true)
        }
        delay(2000)

        recipeRepository.fetch(name = recipeName)?.let { existingRecipe ->
            val updatedRecipe = existingRecipe.copy(
                name = recipeName,
                url = intent.state.website.value,
                calories = intent.state.calories.value?.toIntOrNull(),
                prepTime = intent.state.hours.value.toHours() + intent.state.minutes.value.toMinutes(),
                notes = intent.state.notes.value
            )
            recipeRepository.upsert(updatedRecipe)
        }


        withContext(Dispatchers.Main) {
            navigationProvider.navController.popBackStack()
        }
    }
}

fun String?.toHours(): Duration {
    return this?.toIntOrNull()?.hours ?: Duration.ZERO
}

fun String?.toMinutes(): Duration {
    return this?.toIntOrNull()?.minutes ?: Duration.ZERO
}
