package org.mattshoe.shoebox.listery.recipe.edit.directions.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.cookbook.ui.gesturesDisabled
import org.mattshoe.shoebox.listery.recipe.edit.directions.viewmodel.EditDirectionsViewModel
import org.mattshoe.shoebox.listery.recipe.edit.directions.viewmodel.UserIntent
import org.mattshoe.shoebox.listery.recipe.edit.directions.viewmodel.State
import org.mattshoe.shoebox.listery.ui.common.ListeryBottomSheet
import org.mattshoe.shoebox.listery.ui.common.ListeryPrimaryButton
import org.mattshoe.shoebox.listery.ui.common.ListeryTextInput
import org.mattshoe.shoebox.listery.ui.common.SubduedText
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun EditDirectionsScreen(
    recipeId: String,
    viewModel: EditDirectionsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    viewModel.initialize(recipeId)

    var newStep by remember { mutableStateOf("") }
    var editedStep by remember(state.isEditInProgress()) {
        mutableStateOf(
            TextFieldValue(
                if (state.isEditInProgress())
                    state.steps[state.activeEditIndex!!].instructions
                else
                    ""
            )
        )
    }

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
                itemsIndexed(steps, key = { _, step -> step.id }) { index, step ->
                    ReorderableItem(
                        reorderableLazyListState,
                        key = step.id,
                    ) { isDragging ->
                        if (state.activeEditIndex == index) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top,
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


                                    ListeryTextInput(
                                        value = editedStep,
                                        placeholder = "Enter instructions (required)",
                                        maxLines = 99,
                                        onValueChange = { editedStep = it },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Step",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier
                                        .width(20.dp)
                                        .clickable {
                                            viewModel.handleIntent(UserIntent.DeleteStep(index))
                                        }
                                )

                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Save edits",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .width(20.dp)
                                        .gesturesDisabled(editedStep.text.isEmpty())
                                        .clickable {
                                            viewModel.handleIntent(UserIntent.UpdateStep(index, editedStep.text))
                                        }
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .alpha(if (isDragging || state.shouldDisableStepForEdit(index)) 0.6f else 1f)
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onLongPress = {
                                                if (!state.isEditInProgress()) {
                                                    viewModel.handleIntent(
                                                        UserIntent.StartEdit(index)
                                                    )
                                                }
                                            }
                                        )
                                    }
                                    .gesturesDisabled(
                                        state.shouldDisableStepForEdit(index)
                                    ),
                                verticalAlignment = Alignment.Top,
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
            }

            Column(
                modifier = Modifier
                    .gesturesDisabled(
                        state.isEditInProgress()
                    )
                    .alpha(if (state.isEditInProgress()) 0.5f else 1f)
            ) {
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
                    enabled = newStep.isNotBlank(),
                    onClick = {
                        viewModel.handleIntent(UserIntent.AddStep(newStep.trim()))
                        newStep = ""
                    }
                )
            }
        }
    }
}

private fun State.shouldDisableStepForEdit(index: Int): Boolean {
    return isEditInProgress() && activeEditIndex != index
}

private fun State.isEditInProgress(): Boolean {
    return activeEditIndex != null
}