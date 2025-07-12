package org.mattshoe.shoebox.listery.recipe.create.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import org.mattshoe.shoebox.listery.data.RecipeRepository
import org.mattshoe.shoebox.listery.model.EditableField
import org.mattshoe.shoebox.listery.model.Recipe
import org.mattshoe.shoebox.listery.navigation.NavigationProvider
import org.mattshoe.shoebox.listery.navigation.Routes
import org.mattshoe.shoebox.listery.recipe.edit.overview.viewmodel.RecipeOverviewState
import org.mattshoe.shoebox.listery.recipe.edit.overview.viewmodel.toHours
import org.mattshoe.shoebox.listery.recipe.edit.overview.viewmodel.toMinutes
import javax.inject.Inject

@HiltViewModel
class CreateRecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val navigationProvider: NavigationProvider
): ListeryViewModel<RecipeOverviewState, UserIntent>(RecipeOverviewState(loading = false)) {


    override fun handleIntent(intent: UserIntent) {
        viewModelScope.launch(Dispatchers.Default) {
            when (intent) {
                is UserIntent.NameUpdated -> handleNameUpdated(intent)
                is UserIntent.WebsiteUpdated -> handleWebsiteUpdated(intent)
                is UserIntent.HoursUpdated -> handleHoursUpdated(intent)
                is UserIntent.MinutesUpdated -> handleMinutesUpdated(intent)
                is UserIntent.NotesUpdated -> handleNotesUpdated(intent)
                is UserIntent.ServingsUpdated -> handleServingsUpdated(intent)
                is UserIntent.Submit -> handleSubmit(intent)
            }
        }
    }

    private suspend fun handleNameUpdated(intent: UserIntent.NameUpdated) {
        updateState {
            it.copy(
                allowSubmit = intent.value.isNotBlank(),
                name = EditableField(intent.value)
            )
        }
    }

    private suspend fun handleWebsiteUpdated(intent: UserIntent.WebsiteUpdated) {
        updateState {
            it.copy(
                website = EditableField(intent.value)
            )
        }
    }

    private suspend fun handleHoursUpdated(intent: UserIntent.HoursUpdated) {
        updateState {
            it.copy(
                hours = EditableField(intent.value)
            )
        }
    }

    private suspend fun handleMinutesUpdated(intent: UserIntent.MinutesUpdated) {
        updateState {
            it.copy(
                minutes = EditableField(intent.value)
            )
        }
    }

    private suspend fun handleNotesUpdated(intent: UserIntent.NotesUpdated) {
        updateState {
            it.copy(
                notes = EditableField(intent.value)
            )
        }
    }

    private suspend fun handleServingsUpdated(intent: UserIntent.ServingsUpdated) {
        updateState {
            it.copy(
                servings = EditableField(intent.value)
            )
        }
    }

    private suspend fun handleSubmit(intent: UserIntent.Submit) {
        updateState {
            it.copy(loading = true)
        }

        when {
            intent.state.name.value.isNullOrBlank() -> updateState {
                it.copy(
                    loading = false,
                    allowSubmit = false
                )
            }
            else -> {
                val id = recipeRepository.upsert(
                    Recipe(
                        name = intent.state.name.value,
                        starred = false,
                        url = intent.state.website.value,
                        ingredients = listOf(),
                        prepTime = intent.state.hours.value.toHours() + intent.state.minutes.value.toMinutes(),
                        notes = intent.state.notes.value,
                        servings = intent.state.servings.value,
                        steps = listOf()
                    )
                )
                withContext(Dispatchers.Main) {
                    navigationProvider.navController.navigate(Routes.Recipe(id))
                }
            }
        }
    }
}