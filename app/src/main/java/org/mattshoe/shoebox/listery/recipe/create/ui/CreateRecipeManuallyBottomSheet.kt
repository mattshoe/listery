package org.mattshoe.shoebox.listery.recipe.create.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.mattshoe.shoebox.listery.recipe.create.viewmodel.CreateRecipeViewModel
import org.mattshoe.shoebox.listery.recipe.create.viewmodel.UserIntent
import org.mattshoe.shoebox.listery.ui.common.ListeryBottomSheet
import org.mattshoe.shoebox.listery.ui.common.ListeryPrimaryButton
import org.mattshoe.shoebox.listery.ui.common.ListeryTextInput

@Composable
fun CreateRecipeManuallyBottomSheet(
    viewModel: CreateRecipeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var recipeName by remember {
        mutableStateOf(TextFieldValue(state.recipeName))
    }

    ListeryBottomSheet {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(modifier = Modifier.padding(8.dp)) {
                ListeryTextInput(
                    value = recipeName,
                    placeholder = "Name your recipe",
                    fullBorder = true,
                    enabled = !state.loading,
                    onValueChange = {
                        recipeName = it
                        viewModel.handleIntent(UserIntent.NameUpdated(it.text))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .padding(horizontal = 4.dp)
                )
            }

            state.invalidityReason?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            ListeryPrimaryButton(
                text = "Start Building!",
                enabled = state.validName && !state.loading
            ) {
                viewModel.handleIntent(
                    UserIntent.Submit(recipeName.text)
                )
            }
        }
    }
}
