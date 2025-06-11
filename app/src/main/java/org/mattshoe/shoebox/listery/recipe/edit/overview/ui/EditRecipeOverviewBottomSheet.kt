package org.mattshoe.shoebox.listery.recipe.edit.overview.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.cookbook.ui.gesturesDisabled
import org.mattshoe.shoebox.listery.recipe.edit.overview.viewmodel.EditRecipeViewModel
import org.mattshoe.shoebox.listery.recipe.edit.overview.viewmodel.UserIntent
import org.mattshoe.shoebox.listery.ui.common.ListeryBottomSheet
import org.mattshoe.shoebox.listery.ui.common.ListeryPrimaryButton
import org.mattshoe.shoebox.listery.ui.common.ListeryTextInput
import androidx.compose.ui.draw.alpha

@Composable
fun EditRecipeOverviewBottomSheet(
    viewModel: EditRecipeViewModel = hiltViewModel(),
    recipeName: String,
) {
    LaunchedEffect(viewModel) {
        viewModel.init(recipeName)
    }

    val state by viewModel.state.collectAsState()
    
    val nameTextFieldValue = remember(state.name) { ListeryTextFieldValue(state.name ?: "") }
    var webPageTextFieldValue by remember(state.website) { mutableStateOf(ListeryTextFieldValue(state.website.value ?: "")) }
    var hoursTextFieldValue by remember(state.hours) { mutableStateOf(ListeryTextFieldValue(state.hours.value ?: "")) }
    var minutesTextFieldValue by remember(state.minutes) { mutableStateOf(ListeryTextFieldValue(state.minutes.value ?: "")) }
    var caloriesTextFieldValue by remember(state.calories) { mutableStateOf(ListeryTextFieldValue(state.calories.value ?: "")) }
    var notesTextFieldValue by remember(state.notes) { mutableStateOf(ListeryTextFieldValue(state.notes.value ?: "")) }

    ListeryBottomSheet {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .gesturesDisabled(state.loading)
                .alpha(if (state.loading) 0.5f else 1f)
        ) {
            Row {
                Column {
                    EditRecipeSectionTitle("Recipe Name")
                    ListeryTextInput(
                        value = nameTextFieldValue,
                        placeholder = "",
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { }
                    )
                }
            }

            Row {
                Column {
                    EditRecipeSectionTitle("Web page")
                    ListeryTextInput(
                        value = webPageTextFieldValue,
                        placeholder = "Enter website for this recipe",
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = {
                            webPageTextFieldValue = it
                            viewModel.handleIntent(
                                UserIntent.WebsiteUpdated(it.text)
                            )
                        }
                    )
                }

            }

            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Icon(
                    modifier = Modifier
                        .size(32.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_prep_time),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "Prep time"
                )
                Spacer(modifier = Modifier.width(8.dp))
                ListeryTextInput(
                    value = hoursTextFieldValue,
                    placeholder = "----",
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(32.dp).padding(horizontal = 4.dp),
                    onValueChange = { 
                        hoursTextFieldValue = it
                        viewModel.handleIntent(UserIntent.HoursUpdated(it.text)) 
                    }
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "hour",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                ListeryTextInput(
                    value = minutesTextFieldValue,
                    placeholder = "----",
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(32.dp).padding(horizontal = 4.dp),
                    onValueChange = { 
                        minutesTextFieldValue = it
                        viewModel.handleIntent(UserIntent.MinutesUpdated(it.text)) 
                    }
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "minutes",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Icon(
                    modifier = Modifier
                        .size(32.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_calories),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "Calories"
                )
                Spacer(modifier = Modifier.width(8.dp))
                ListeryTextInput(
                    value = caloriesTextFieldValue,
                    placeholder = "----",
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(50.dp).padding(horizontal = 4.dp),
                    onValueChange = { 
                        caloriesTextFieldValue = it
                        viewModel.handleIntent(UserIntent.CaloriesUpdated(it.text)) 
                    }
                )
            }

            Row {
                Column {
                    EditRecipeSectionTitle("Notes")
                    ListeryTextInput(
                        value = notesTextFieldValue,
                        placeholder = "Enter some notes about your recipe",
                        maxLines = 5,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = {
                            notesTextFieldValue = it
                            viewModel.handleIntent(
                                UserIntent.NotesUpdated(it.text)
                            )
                        }
                    )
                }
            }

            Row {
                Spacer(modifier = Modifier.height(8.dp))
                ListeryPrimaryButton(
                    text = "Save updates!",
                    enabled = !state.loading && state.pageError == null
                ) {
                    viewModel.handleIntent(UserIntent.Submit(state))
                }
            }
        }
    }
}

@Composable
fun EditRecipeSectionTitle(
    text: String
) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.fillMaxWidth()
    )
}

fun ListeryTextFieldValue(value: String): TextFieldValue = TextFieldValue(value, TextRange(Int.MAX_VALUE))
