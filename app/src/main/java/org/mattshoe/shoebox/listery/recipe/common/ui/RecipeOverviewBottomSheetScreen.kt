package org.mattshoe.shoebox.listery.recipe.common.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.recipe.edit.overview.viewmodel.RecipeOverviewState
import org.mattshoe.shoebox.listery.ui.common.ListeryPrimaryButton
import org.mattshoe.shoebox.listery.ui.common.ListeryTextFieldValue
import org.mattshoe.shoebox.listery.ui.common.ListeryTextInput

@Composable
fun RecipeOverviewBottomSheetScreen(
    state: RecipeOverviewState,
    submitButtonText: String,
    onNameUpdated: (String) -> Unit,
    onWebsiteUpdated: (String) -> Unit,
    onHoursUpdated: (String) -> Unit,
    onMinutesUpdated: (String) -> Unit,
    onCaloriesUpdated: (String) -> Unit,
    onNotesUpdated: (String) -> Unit,
    onSubmit: (RecipeOverviewState) -> Unit
) {

    var nameTextFieldValue by remember { mutableStateOf(ListeryTextFieldValue(state.name.value)) }
    var webPageTextFieldValue by remember { mutableStateOf(ListeryTextFieldValue(state.website.value)) }
    var hoursTextFieldValue by remember { mutableStateOf(ListeryTextFieldValue(state.hours.value)) }
    var minutesTextFieldValue by remember { mutableStateOf(ListeryTextFieldValue(state.minutes.value)) }
    var caloriesTextFieldValue by remember { mutableStateOf(ListeryTextFieldValue(state.calories.value)) }
    var notesTextFieldValue by remember { mutableStateOf(ListeryTextFieldValue(state.notes.value)) }

    if (state.name.value != nameTextFieldValue.text)
        nameTextFieldValue = nameTextFieldValue.copy(text = state.name.value ?: "")

    if (state.website.value != webPageTextFieldValue.text)
        webPageTextFieldValue = webPageTextFieldValue.copy(text = state.website.value ?: "")

    if (state.hours.value != hoursTextFieldValue.text)
        hoursTextFieldValue = hoursTextFieldValue.copy(text = state.hours.value ?: "")

    if (state.minutes.value != minutesTextFieldValue.text)
        minutesTextFieldValue = minutesTextFieldValue.copy(text = state.minutes.value ?: "")

    if (state.calories.value != caloriesTextFieldValue.text)
        caloriesTextFieldValue = caloriesTextFieldValue.copy(text = state.calories.value ?: "")

    if (state.notes.value != notesTextFieldValue.text)
        notesTextFieldValue = notesTextFieldValue.copy(text = state.notes.value ?: "")

    Row {
        Column {
            BottomSheetTextSectionTitle("Recipe Name")
            ListeryTextInput(
                value = nameTextFieldValue,
                placeholder = "Give your recipe a name (required)",
                enabled = state.name.enabled,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    nameTextFieldValue = it
                    onNameUpdated(it.text)
                }
            )
        }
    }

    Row {
        Column {
            BottomSheetTextSectionTitle("Web page")
            ListeryTextInput(
                value = webPageTextFieldValue,
                placeholder = "Enter the website for this recipe (optional)",
                enabled = state.website.enabled,
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.None
                ),
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    webPageTextFieldValue = it
                    onWebsiteUpdated(it.text)
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
            enabled = state.hours.enabled,
            placeholder = "----",
            textAlign = TextAlign.End,
            modifier = Modifier.width(32.dp).padding(horizontal = 4.dp),
            onValueChange = {
                hoursTextFieldValue = it
                onHoursUpdated(it.text)
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
            enabled = state.minutes.enabled,
            placeholder = "----",
            textAlign = TextAlign.End,
            modifier = Modifier.width(32.dp).padding(horizontal = 4.dp),
            onValueChange = {
                minutesTextFieldValue = it
                onMinutesUpdated(it.text)
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
            enabled = state.calories.enabled,
            placeholder = "----",
            textAlign = TextAlign.End,
            modifier = Modifier.width(50.dp).padding(horizontal = 4.dp),
            onValueChange = {
                caloriesTextFieldValue = it
                onCaloriesUpdated(it.text)
            }
        )
    }

    Row {
        Column {
            BottomSheetTextSectionTitle("Notes")
            ListeryTextInput(
                value = notesTextFieldValue,
                enabled = state.notes.enabled,
                placeholder = "Add some notes about your recipe (optional)",
                maxLines = 6,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    notesTextFieldValue = it
                    onNotesUpdated(it.text)
                }
            )
        }
    }

    Row {
        Spacer(modifier = Modifier.height(8.dp))
        ListeryPrimaryButton(
            text = submitButtonText,
            enabled = !state.loading && state.allowSubmit
        ) {
            onSubmit(state)
        }
    }
}

@Composable
fun BottomSheetTextSectionTitle(
    text: String
) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.fillMaxWidth()
    )
}
