package org.mattshoe.shoebox.listery.recipe.create.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.mattshoe.shoebox.listery.cookbook.ui.gesturesDisabled
import org.mattshoe.shoebox.listery.recipe.common.ui.RecipeOverviewBottomSheetScreen
import org.mattshoe.shoebox.listery.recipe.create.viewmodel.CreateRecipeViewModel
import org.mattshoe.shoebox.listery.recipe.create.viewmodel.UserIntent
import org.mattshoe.shoebox.listery.ui.common.ListeryBottomSheet

@Composable
fun CreateRecipeManuallyBottomSheet(
    viewModel: CreateRecipeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    ListeryBottomSheet(
        canDismiss = { !state.loading }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .gesturesDisabled(state.loading)
                .alpha(if (state.loading) 0.5f else 1f)
        ) {
           RecipeOverviewBottomSheetScreen(
               state = state,
               submitButtonText = "Start building!",
               onNameUpdated = {
                   viewModel.handleIntent(UserIntent.NameUpdated(it))
               },
               onWebsiteUpdated = {
                   viewModel.handleIntent(UserIntent.WebsiteUpdated(it))
               },
               onHoursUpdated = {
                   viewModel.handleIntent(UserIntent.HoursUpdated(it))
               },
               onMinutesUpdated = {
                   viewModel.handleIntent(UserIntent.MinutesUpdated(it))
               },
               onNotesUpdated = {
                   viewModel.handleIntent(UserIntent.NotesUpdated(it))
               },
               onServingsUpdated = {
                   viewModel.handleIntent(UserIntent.ServingsUpdated(it))
               },
               onSubmit = {
                   viewModel.handleIntent(UserIntent.Submit(it))
               }
           )
        }
    }
}
