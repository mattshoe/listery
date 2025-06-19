package org.mattshoe.shoebox.listery.recipe.edit.directions.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import org.mattshoe.shoebox.listery.recipe.edit.directions.viewmodel.EditDirectionsViewModel
import org.mattshoe.shoebox.listery.ui.common.ListeryBottomSheet

@Composable
fun EditDirectionsScreen(
    recipeName: String,
    viewModel: EditDirectionsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    viewModel.initialize(recipeName)

    ListeryBottomSheet(
        canDismiss = { !state.loading }
    ) {

    }
}