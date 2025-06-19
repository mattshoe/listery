package org.mattshoe.shoebox.listery.recipe.edit.ingredient.ui

import androidx.compose.foundation.border
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
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
import org.mattshoe.shoebox.listery.ui.common.ListeryTextFieldValue
import org.mattshoe.shoebox.listery.ui.common.ListeryTextInput

@Composable
fun EditIngredientsScreen(
    recipeName: String,
    viewModel: EditIngredientsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    viewModel.initialize(recipeName)

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
            var nameTextFieldValue by remember { mutableStateOf(ListeryTextFieldValue(state.name.value)) }
            var quantityTextFieldValue by remember { mutableStateOf(ListeryTextFieldValue(state.quantity.value.toString())) }
            var unitTextFieldValue by remember { mutableStateOf(ListeryTextFieldValue(state.unit.value)) }

            if (state.name.value != nameTextFieldValue.text)
                nameTextFieldValue = nameTextFieldValue.copy(text = state.name.value ?: "")

            if (state.quantity.value != quantityTextFieldValue.text)
                quantityTextFieldValue = quantityTextFieldValue.copy(text = state.quantity.value.toString())

            if (state.unit.value != unitTextFieldValue.text)
                unitTextFieldValue = unitTextFieldValue.copy(text = state.unit.value)

            Row {
                Column {
                    BottomSheetTextSectionTitle("Ingredient Name")
                    ListeryTextInput(
                        value = nameTextFieldValue,
                        placeholder = "Give your ingredient a name (required)",
                        enabled = state.name.enabled,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = {
                            nameTextFieldValue = it
                            viewModel.handleIntent(
                                UserIntent.NameUpdated(it.text)
                            )
                        }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Quantity field
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
                        value = quantityTextFieldValue,
                        placeholder = "",
                        enabled = state.quantity.enabled,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp),
                        onValueChange = {
                            quantityTextFieldValue = it
                            viewModel.handleIntent(
                                UserIntent.QuantityUpdated(it.text)
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
                        value = unitTextFieldValue,
                        placeholder = "",
                        enabled = state.unit.enabled,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp),
                        onValueChange = {
                            unitTextFieldValue = it
                            viewModel.handleIntent(
                                UserIntent.UnitUpdated(it.text)
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