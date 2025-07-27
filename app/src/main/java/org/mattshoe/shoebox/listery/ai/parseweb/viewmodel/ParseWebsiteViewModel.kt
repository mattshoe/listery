package org.mattshoe.shoebox.listery.ai.parseweb.viewmodel

import android.webkit.URLUtil
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mattshoe.shoebox.listery.ai.GenerativeResult
import org.mattshoe.shoebox.listery.ai.parseweb.usecase.ParseWebsiteUseCase
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import org.mattshoe.shoebox.listery.data.RecipeRepository
import org.mattshoe.shoebox.listery.logging.loge
import org.mattshoe.shoebox.listery.navigation.NavigationProvider
import org.mattshoe.shoebox.listery.navigation.Routes
import javax.inject.Inject

data class State(
    val url: String = "",
    val parsedRecipe: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val allowSubmit: Boolean
        get() = !isLoading && url.isNotBlank()
}

sealed interface UserIntent {
    data class ParseWebsite(val url: String) : UserIntent
    data class UpdateUrl(val url: String) : UserIntent
}

@HiltViewModel
class ParseWebsiteViewModel @Inject constructor(
    private val parseWebsiteUseCase: ParseWebsiteUseCase,
    private val recipeRepository: RecipeRepository,
    private val navigationProvider: NavigationProvider
): ListeryViewModel<State, UserIntent>(State()) {

    override fun handleIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.ParseWebsite -> {
                parseWebsite(intent.url.trim())
            }
            is UserIntent.UpdateUrl -> {
                updateState {
                    it.copy(url = intent.url)
                }
            }
        }
    }

    private fun parseWebsite(url: String) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    isLoading = true,
                    error = null,
                    parsedRecipe = null
                )
            }

            val result = parseWebsiteUseCase.execute(url)

            when (result) {
                is GenerativeResult.Success -> {
                    val recipeId = recipeRepository.upsert(result.value)
                    navigationProvider.navController.navigate(Routes.Recipe(recipeId))
                }
                is GenerativeResult.Failure -> {
                    loge("Unable to parse website: ${result.reason}")
                    updateState {
                        it.copy(
                            isLoading = false,
                            error = result.reason.explanation
                        )
                    }
                }
            }



        }
    }
}