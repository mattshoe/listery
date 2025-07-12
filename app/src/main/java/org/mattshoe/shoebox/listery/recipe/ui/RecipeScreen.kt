package org.mattshoe.shoebox.listery.recipe.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.cookbook.ui.prettyPrint
import org.mattshoe.shoebox.listery.recipe.viewmodel.RecipeScreenViewModel
import org.mattshoe.shoebox.listery.recipe.viewmodel.State
import org.mattshoe.shoebox.listery.recipe.viewmodel.UserIntent
import org.mattshoe.shoebox.listery.ui.BottomNavItem
import org.mattshoe.shoebox.listery.ui.common.ClickableLinkText
import org.mattshoe.shoebox.listery.ui.common.GenericErrorScreen
import org.mattshoe.shoebox.listery.ui.common.Level2AppBar
import org.mattshoe.shoebox.listery.ui.common.ListeryCard
import org.mattshoe.shoebox.listery.ui.common.ListeryScaffold
import org.mattshoe.shoebox.listery.ui.common.ShimmerPlaceholder
import org.mattshoe.shoebox.listery.ui.common.SubduedText
import org.mattshoe.shoebox.listery.ui.common.TopBarIcon
import org.mattshoe.shoebox.listery.util.pretty

@Composable
fun RecipeScreen(
    viewModel: RecipeScreenViewModel = hiltViewModel(),
    recipeName: String
) {
    LaunchedEffect(recipeName) {
        viewModel.initialize(recipeName)
    }

    val viewModelState: State by viewModel.state.collectAsState()
    when (val state = viewModelState) {
        is State.Ready -> RecipeSuccessScreen(state) {
            viewModel.handleIntent(it)
        }
        is State.Loading -> RecipeLoadingScreen()
        is State.Error -> GenericErrorScreen(state)
    }
}

@Composable
private fun RecipeSuccessScreen(
    state: State.Ready,
    handleIntent: (UserIntent) -> Unit
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Delete Recipe") },
            text = { Text("Are you sure you want to delete this recipe? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmation = false
                        handleIntent(UserIntent.DeleteRecipe)
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirmation = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    ListeryScaffold(
        topBar = {
            Level2AppBar(
                actions = listOf(
                    TopBarIcon(
                        icon = ImageVector.vectorResource(
                            id = if (state.data.starred) R.drawable.ic_filled_star else R.drawable.ic_hollow_star
                        ),
                        contentDescription = "",
                        onClick = { handleIntent(UserIntent.ToggleStarred) }
                    ),
                    TopBarIcon(
                        icon = ImageVector.vectorResource(
                            id = R.drawable.ic_add_to_shopping_list
                        ),
                        contentDescription = "",
                        onClick = { handleIntent(UserIntent.AddToShoppingList) }
                    ),
                    TopBarIcon(
                        icon = ImageVector.vectorResource(
                            id = R.drawable.ic_trash
                        ),
                        contentDescription = "Delete recipe",
                        onClick = { showDeleteConfirmation = true }
                    ),
                )
            )
        },
        selectedNavItem = BottomNavItem.Cookbook,
        showFab = false
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                RecipeOverviewTile(state, handleIntent)
            }
            item {
                IngredientsTile(state, handleIntent)
            }
            item {
                DirectionsTile(state, handleIntent)
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun RecipeOverviewTile(
    state: State.Ready,
    handleIntent: (UserIntent) -> Unit
) {
    RecipeScreenTile(
        title = state.data.name,
        titleStyle = MaterialTheme.typography.titleLarge,
        icon = {
            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        handleIntent(
                            UserIntent.EditRecipeOverview
                        )
                    },
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit),
                tint = MaterialTheme.colorScheme.outline,
                contentDescription = "Edit recipe details."
            )
        }
    ) {
        Row {
            state.data.url?.let {
                ClickableLinkText(text = it, modifier = Modifier.padding(0.dp))
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        state.data.notes?.let {
            Spacer(Modifier.height(8.dp))
            SubduedText(
                text = state.data.notes,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_prep_time),
                    tint = MaterialTheme.colorScheme.outline,
                    contentDescription = "Prep time"
                )
                Spacer(modifier = Modifier.width(8.dp))
                SubduedText(
                    text = state.data.prepTime.prettyPrint(),
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.width(24.dp))
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_calories),
                    tint = MaterialTheme.colorScheme.outline,
                    contentDescription = "Calories"
                )
                Spacer(modifier = Modifier.width(8.dp))
                SubduedText(
                    text = state.data.calories.let {
                        if (it == 0) "--" else "%,d".format(it)
                    },
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.width(4.dp))
                SubduedText(
                    text = "per serving",
                    style = MaterialTheme.typography.labelMedium,
                    softWrap = false
                )
            }
        }
    }
}

@Composable
fun IngredientsTile(
    state: State.Ready,
    handleIntent: (UserIntent) -> Unit
) {
    RecipeScreenTile(
        title = "Ingredients",
        contentTopMargin = 12.dp,
        icon = {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(24.dp)
                    .background(MaterialTheme.colorScheme.tertiary)
                    .clickable {
                        handleIntent(UserIntent.AddIngredient)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(8.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_plus),
                    tint = MaterialTheme.colorScheme.onTertiary,
                    contentDescription = "Add ingredient."
                )
            }

        }
    ) {
        state.data.ingredients.forEachIndexed { index, ingredient ->
            SwipeBox(
                onDelete = {
                    handleIntent(UserIntent.DeleteIngredient(index))
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .clickable {
                            handleIntent(UserIntent.EditIngredient(ingredient.id))
                        },
                ) {
                    Row(
                        modifier = Modifier.padding(top = if (index == 0) 0.dp else 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SubduedText(
                            text = ingredient.name,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        SubduedText(
                            text = if (ingredient.qty == 0f) "" else ingredient.qty.pretty(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        SubduedText(
                            text = ingredient.unit,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    if (index != state.data.ingredients.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 6.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DirectionsTile(
    state: State.Ready,
    handleIntent: (UserIntent) -> Unit
) {
    RecipeScreenTile(
        title = "Directions",
        contentTopMargin = 12.dp,
        icon = {
            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        handleIntent(UserIntent.EditDirections)
                    },
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit),
                tint = MaterialTheme.colorScheme.outline,
                contentDescription = "Edit ingredients."
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            state.data.steps.forEachIndexed { index, step ->
                Row {
                    Column {
                        Text(
                            text = "Step ${index + 1}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(0.dp))
                        SubduedText(
                            text = step.instructions,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                }
            }
        }
    }
}

@Composable
private fun RecipeLoadingScreen() {
    ListeryScaffold(
        topBar = {
            Level2AppBar(
                actions = listOf(
                    TopBarIcon(
                        icon = ImageVector.vectorResource(id = R.drawable.ic_hollow_star),
                        contentDescription = "",
                        onClick = { }
                    ),
                    TopBarIcon(
                        icon = ImageVector.vectorResource(id = R.drawable.ic_add_to_shopping_list),
                        contentDescription = "",
                        onClick = { }
                    ),
                    TopBarIcon(
                        icon = ImageVector.vectorResource(id = R.drawable.ic_trash),
                        contentDescription = "",
                        onClick = { }
                    ),
                )
            )
        },
        selectedNavItem = BottomNavItem.Cookbook,
        showFab = false
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                RecipeOverviewTileGhost()
            }
            item {
                IngredientsTileGhost()
            }
            item {
                DirectionsTileGhost()
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun RecipeOverviewTileGhost() {
    RecipeScreenTile(
        title = "",
        titleStyle = MaterialTheme.typography.titleLarge,
        icon = {
            ShimmerPlaceholder(
                modifier = Modifier.size(20.dp)
            )
        }
    ) {
        Column {
            // URL placeholder
            ShimmerPlaceholder(
                modifier = Modifier.fillMaxWidth(),
                height = 16
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Notes placeholder
            ShimmerPlaceholder(
                modifier = Modifier.fillMaxWidth(),
                height = 16
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ShimmerPlaceholder(
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    ShimmerPlaceholder(
                        modifier = Modifier.width(80.dp),
                        height = 16
                    )
                }
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ShimmerPlaceholder(
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    ShimmerPlaceholder(
                        modifier = Modifier.width(60.dp),
                        height = 16
                    )
                }
            }
        }
    }
}

@Composable
private fun IngredientsTileGhost() {
    RecipeScreenTile(
        title = "Ingredients",
        contentTopMargin = 12.dp,
        titleColor = MaterialTheme.colorScheme.surfaceVariant,
        icon = {
            ShimmerPlaceholder(
                modifier = Modifier.size(20.dp)
            )
        }
    ) {
        repeat(3) { index ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ShimmerPlaceholder(
                    modifier = Modifier.weight(1f),
                    height = 16
                )
                ShimmerPlaceholder(
                    modifier = Modifier.width(40.dp),
                    height = 16
                )
                ShimmerPlaceholder(
                    modifier = Modifier.width(60.dp),
                    height = 16
                )
            }
            if (index != 2) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 6.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}

@Composable
private fun DirectionsTileGhost() {
    RecipeScreenTile(
        title = "Directions",
        contentTopMargin = 12.dp,
        titleColor = MaterialTheme.colorScheme.surfaceVariant,
        icon = {
            ShimmerPlaceholder(
                modifier = Modifier.size(20.dp)
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(3) { index ->
                Column {
                    ShimmerPlaceholder(
                        modifier = Modifier.width(80.dp),
                        height = 16
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ShimmerPlaceholder(
                        modifier = Modifier.fillMaxWidth(),
                        height = 24
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeScreenTile(
    title: String,
    icon: @Composable () -> Unit,
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium,
    titleColor: Color = Color.Unspecified,
    contentTopMargin: Dp = 4.dp,
    content: @Composable () -> Unit
) {
    ListeryCard(
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier.wrapContentHeight()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = title,
                    style = titleStyle,
                    color = titleColor,
                    fontWeight = FontWeight.Bold
                )
                icon()
            }
            Spacer(modifier = Modifier.height(contentTopMargin))
            content()
        }
    }
}

@Composable
private fun SwipeBox(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    val swipeState = rememberSwipeToDismissBoxState(
        positionalThreshold = with(LocalDensity.current) { { 32.dp.toPx() } }
    )

    Box(
        modifier = modifier
            .fillMaxSize()// Fixed width for the delete button
            .background(MaterialTheme.colorScheme.error)
    ) {


        // Content that can be swiped
        SwipeToDismissBox(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            state = swipeState,
            enableDismissFromEndToStart = true,
            enableDismissFromStartToEnd = false,
            backgroundContent = { }
        ) {
            content()
        }
    }

    when (swipeState.currentValue) {
        SwipeToDismissBoxValue.EndToStart -> {
            onDelete()
            LaunchedEffect(swipeState) {
                swipeState.snapTo(SwipeToDismissBoxValue.Settled)
            }
        }
        SwipeToDismissBoxValue.StartToEnd -> {
            LaunchedEffect(swipeState) {
                swipeState.snapTo(SwipeToDismissBoxValue.Settled)
            }
        }
        SwipeToDismissBoxValue.Settled -> {}
    }
}
