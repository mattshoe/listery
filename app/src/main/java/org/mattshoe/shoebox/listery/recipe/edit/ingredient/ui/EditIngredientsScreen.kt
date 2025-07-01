package org.mattshoe.shoebox.listery.recipe.edit.ingredient.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.mattshoe.shoebox.listery.cookbook.ui.gesturesDisabled
import org.mattshoe.shoebox.listery.recipe.common.ui.BottomSheetTextSectionTitle
import org.mattshoe.shoebox.listery.recipe.edit.ingredient.viewmodel.EditIngredientsViewModel
import org.mattshoe.shoebox.listery.recipe.edit.ingredient.viewmodel.UserIntent
import org.mattshoe.shoebox.listery.ui.common.ListeryBottomSheet
import org.mattshoe.shoebox.listery.ui.common.ListeryNumberInput
import org.mattshoe.shoebox.listery.ui.common.ListeryPrimaryButton
import org.mattshoe.shoebox.listery.ui.common.ListeryTextInput

@Composable
fun EditIngredientsScreen(
    recipeId: String,
    viewModel: EditIngredientsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    viewModel.initialize(recipeId)

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
            Row {
                Column {
                    BottomSheetTextSectionTitle("Ingredient Name")
                    ListeryTextInput(
                        value = state.name,
                        placeholder = "Give your ingredient a name (required)",
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = {
                            viewModel.handleIntent(
                                UserIntent.NameUpdated(it)
                            )
                        }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quantity:",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    ListeryNumberInput(
                        value = state.quantity.value,
                        placeholder = "",
                        enabled = state.quantity.enabled,
                        modifier = Modifier.weight(1f),
                        onValueChange = {
                            viewModel.handleIntent(
                                UserIntent.QuantityUpdated(it)
                            )
                        }
                    )
                }
                // Unit field
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Unit:",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    ListeryTextInput(
                        value = state.unit.value,
                        placeholder = "",
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f),
                        onValueChange = {
                            viewModel.handleIntent(
                                UserIntent.UnitUpdated(it)
                            )
                        }
                    )
                }
            }

            Row {
                Spacer(modifier = Modifier.height(8.dp))
                ListeryPrimaryButton(
                    text = "Add ingredient!",
                    enabled = !state.loading && state.allowSubmit
                ) {
                    viewModel.handleIntent(UserIntent.Submit(state))
                }
            }
        }
    }
}