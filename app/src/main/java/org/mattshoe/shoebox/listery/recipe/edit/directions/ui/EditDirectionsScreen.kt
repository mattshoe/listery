package org.mattshoe.shoebox.listery.recipe.edit.directions.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.recipe.edit.directions.viewmodel.EditDirectionsViewModel
import org.mattshoe.shoebox.listery.recipe.edit.directions.viewmodel.UserIntent
import org.mattshoe.shoebox.listery.ui.common.ListeryBottomSheet
import org.mattshoe.shoebox.listery.ui.common.ListeryPrimaryButton
import org.mattshoe.shoebox.listery.ui.common.ListeryTextInput
import org.mattshoe.shoebox.listery.ui.common.SubduedText
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun EditDirectionsScreen(
    recipeName: String,
    viewModel: EditDirectionsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    viewModel.initialize(recipeName)

    var newStep by remember { mutableStateOf(TextFieldValue("")) }

    val steps = state.steps
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        if (from.index != to.index) {
            viewModel.handleIntent(UserIntent.MoveStep(from.index, to.index))
        }
    }

    ListeryBottomSheet(
        canDismiss = { !state.loading }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                itemsIndexed(steps, key = { _, step -> step.key }) { index, step ->
                    ReorderableItem(reorderableLazyListState, key = step.key) { isDragging ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .apply {
                                    if (isDragging)
                                        background(Color.LightGray)
                                }
                                .alpha(if (isDragging) 0.6f else 1f),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Step ${index + 1}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    overflow = TextOverflow.Clip,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                SubduedText(
                                    text = step.instructions,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Icon(
                                painter = painterResource(id = R.drawable.ic_drag_handle),
                                contentDescription = "Reorder",
                                tint = MaterialTheme.colorScheme.outline,
                                modifier = Modifier
                                    .width(20.dp)
                                    .draggableHandle()
                            )
                        }
                    }
                }
            }

            if (steps.isNotEmpty()) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            }

            Text(
                text = if (steps.isEmpty()) "First step" else "Next step",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            ListeryTextInput(
                value = newStep,
                placeholder = "Enter  your next step here",
                maxLines = 5,
                onValueChange = { newStep = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            ListeryPrimaryButton(
                text = "Add step",
                enabled = newStep.text.isNotBlank(),
                onClick = {
                    viewModel.handleIntent(UserIntent.AddStep(newStep.text.trim()))
                    newStep = TextFieldValue("")
                }
            )
        }
    }
}