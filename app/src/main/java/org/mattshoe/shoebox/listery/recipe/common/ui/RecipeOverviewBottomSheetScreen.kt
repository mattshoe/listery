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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.recipe.edit.overview.viewmodel.RecipeOverviewState
import org.mattshoe.shoebox.listery.ui.common.ListeryNumberInput
import org.mattshoe.shoebox.listery.ui.common.ListeryPrimaryButton
import org.mattshoe.shoebox.listery.ui.common.ListeryTextInput

@Composable
fun RecipeOverviewBottomSheetScreen(
    state: RecipeOverviewState,
    submitButtonText: String,
    onNameUpdated: (String) -> Unit,
    onWebsiteUpdated: (String) -> Unit,
    onHoursUpdated: (String) -> Unit,
    onMinutesUpdated: (String) -> Unit,
    onNotesUpdated: (String) -> Unit,
    onServingsUpdated: (Int) -> Unit,
    onSubmit: (RecipeOverviewState) -> Unit
) {

    Row {
        Column {
            BottomSheetTextSectionTitle("Recipe Name")
            ListeryTextInput(
                value = state.name,
                placeholder = "Give your recipe a name (required)",
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    onNameUpdated(it)
                }
            )
        }
    }

    Row {
        Column {
            BottomSheetTextSectionTitle("Web page")
            ListeryTextInput(
                value = state.website,
                placeholder = "Enter the website for this recipe (optional)",
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.None
                ),
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    onWebsiteUpdated(it)
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
        ListeryNumberInput(
            value = state.hours.value,
            enabled = state.hours.enabled,
            modifier = Modifier.width(50.dp),
            onValueChange = {
                onHoursUpdated(it)
            }
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "hour",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.width(8.dp))
        ListeryNumberInput(
            value = state.minutes.value,
            enabled = state.minutes.enabled,
            modifier = Modifier.width(50.dp),
            onValueChange = {
                onMinutesUpdated(it)
            }
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "minutes",
            style = MaterialTheme.typography.bodyMedium
        )
    }

    Row {
        Text(
            text = "Servings",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.width(8.dp))
        ListeryNumberInput(
            value = state.servings.value.toString(),
            modifier = Modifier.width(50.dp),
            onValueChange = {
                onServingsUpdated(it.toIntOrNull() ?: 1)
            }
        )
    }

    Row {
        Column {
            BottomSheetTextSectionTitle("Notes")
            ListeryTextInput(
                value = state.notes,
                placeholder = "Add some notes about your recipe (optional)",
                maxLines = 6,
                highlightOnFocus = false,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    onNotesUpdated(it)
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
