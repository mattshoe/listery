package org.mattshoe.shoebox.listery.ai.generate.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.ai.generate.viewmodel.GenerateRecipeViewModel
import org.mattshoe.shoebox.listery.ai.generate.viewmodel.UserIntent
import org.mattshoe.shoebox.listery.ui.common.ListeryBottomSheet
import org.mattshoe.shoebox.listery.ui.common.ListeryPrimaryButton
import org.mattshoe.shoebox.listery.ui.common.SubduedText

private const val WILDCARD_PLACEHOLDER = "Any"

@Composable
fun GenerateRecipeScreen(
    viewModel: GenerateRecipeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val cuisines: List<String> by remember {
        derivedStateOf {
            state.cuisines.map { it.name }.toMutableList().apply {
                add(0, WILDCARD_PLACEHOLDER)
            }
        }
    }
    val lifestyles: List<String> by remember {
        derivedStateOf {
            state.lifestyles.map { it.name }.toMutableList().apply {
                add(0, WILDCARD_PLACEHOLDER)
            }
        }
    }
    val prepTimes: List<String> by remember {
        derivedStateOf {
            state.prepTimes.toMutableList().apply {
                add(0, WILDCARD_PLACEHOLDER)
            }
        }
    }

    ListeryBottomSheet {
        Column(
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HorizontalDivider()
            DropdownRow(
                label = "Cuisine:",
                value = state.selectedCuisine?.name,
                options = cuisines,
                onOptionSelected = { selectedName ->
                    val cuisine = state.cuisines.firstOrNull { it.name == selectedName }
                    viewModel.handleIntent(
                        UserIntent.CuisineSelected(cuisine)
                    )
                }
            )
            HorizontalDivider()
            DropdownRow(
                label = "Lifestyle:",
                value = state.selectedLifestyle?.name,
                options = lifestyles,
                onOptionSelected = { selectedName ->
                    val lifestyle = state.lifestyles.firstOrNull { it.name == selectedName }
                    viewModel.handleIntent(
                        UserIntent.LifestyleSelected(lifestyle)
                    )
                }
            )
            HorizontalDivider()
            DropdownRow(
                label = "Prep time:",
                value = state.selectedPrepTime,
                options = prepTimes,
                onOptionSelected = { selected ->
                    viewModel.handleIntent(
                        UserIntent.PrepTimeSelected(selected)
                    )
                }
            )
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            ListeryPrimaryButton(
                text = "Generate recipe!",
                enabled = state.allowSubmit
            ) {
                viewModel.handleIntent(UserIntent.Submit(state))
            }
        }
    }
}

@Composable
private fun DropdownRow(
    label: String,
    value: String?,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { expanded = true },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge
        )
        SubduedText(
            text = value ?: WILDCARD_PLACEHOLDER,
            textAlign = TextAlign.End,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
        )

        Row {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_dropdown_down),
                contentDescription = "Open dropdown",
                tint = MaterialTheme.colorScheme.outline
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            SubduedText(
                                option,
                                textAlign = TextAlign.End,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        },
                        onClick = {
                            expanded = false
                            onOptionSelected(option)
                        }
                    )
                }
            }
        }


    }
}
