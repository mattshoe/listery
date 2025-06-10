package org.mattshoe.shoebox.listery.recipe.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.ui.common.ListeryScaffold
import org.mattshoe.shoebox.listery.recipe.viewmodel.RecipeScreenViewModel
import org.mattshoe.shoebox.listery.recipe.viewmodel.State
import org.mattshoe.shoebox.listery.ui.BottomNavItem
import org.mattshoe.shoebox.listery.ui.common.ClickableLinkText
import org.mattshoe.shoebox.listery.ui.common.Level2AppBar
import org.mattshoe.shoebox.listery.ui.common.ListeryCard
import org.mattshoe.shoebox.listery.ui.common.SubduedText
import org.mattshoe.shoebox.listery.ui.common.TopBarIcon

@Composable
fun RecipeScreen(
    viewModel: RecipeScreenViewModel = hiltViewModel(),
    recipeName: String
) {
    LaunchedEffect(recipeName) {
        viewModel.initialize(recipeName)
    }

    val viewModelState: State by viewModel.state.collectAsState()
    when (viewModelState) {
        is State.Loading -> Unit
        is State.Error -> Unit
        is State.Ready -> RecipeSuccessScreen(viewModelState as State.Ready)
    }

}

@Composable
private fun RecipeSuccessScreen(state: State.Ready) {
    ListeryScaffold(
        topBar = {
            Level2AppBar(
                actions = listOf(
                    TopBarIcon(
                        icon = ImageVector.vectorResource(
                            id = if (state.data.starred) R.drawable.ic_filled_star else R.drawable.ic_hollow_star
                        ),
                        contentDescription = "",
                        onClick = { /* Your action here */ }
                    ),
                    TopBarIcon(
                        icon = ImageVector.vectorResource(
                            id = R.drawable.ic_add_to_shopping_list
                        ),
                        contentDescription = "",
                        onClick = { /* Your action here */ }
                    ),
                    TopBarIcon(
                        icon = ImageVector.vectorResource(
                            id = R.drawable.ic_trash
                        ),
                        contentDescription = "",
                        onClick = { /* Your action here */ }
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
                RecipeOverviewTile(state)
            }
            item {
                IngredientsTile(state)
            }
            item {
                DirectionsTile(state)
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun RecipeOverviewTile(
    state: State.Ready
) {
    RecipeScreenTile(
        title = state.data.name,
        titleStyle = MaterialTheme.typography.titleLarge,
        icon = {
            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .clickable {

                    },
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit),
                tint = MaterialTheme.colorScheme.outline,
                contentDescription = "Edit recipe name."
            )
        }
    ) {
        Row {
            state.data.url?.let {
                ClickableLinkText(uri = it, modifier = Modifier.padding(0.dp))
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
                modifier = Modifier.fillMaxWidth(0.5f),
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
                    text = state.data.prepTime ?: "--",
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(0.5f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_calories),
                    tint = MaterialTheme.colorScheme.outline,
                    contentDescription = "Calories"
                )
                Spacer(modifier = Modifier.width(8.dp))
                SubduedText(
                    text = state.data.calories?.let {
                        "%,d".format(it)
                    } ?: "--",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
fun IngredientsTile(
    state: State.Ready
) {
    RecipeScreenTile(
        title = "Ingredients",
        contentTopMargin = 12.dp,
        icon = {
            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .clickable {

                    },
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit),
                tint = MaterialTheme.colorScheme.outline,
                contentDescription = "Edit ingredients."
            )
        }
    ) {
        state.data.ingredients.forEachIndexed { index, ingredient ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SubduedText(
                    text = ingredient.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                SubduedText(
                    text = ingredient.qty.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
                SubduedText(
                    text = ingredient.unit,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (index != state.data.ingredients.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 6.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline
                )
            }

        }
    }
}

@Composable
fun DirectionsTile(
    state: State.Ready
) {
    RecipeScreenTile(
        title = "Directions",
        contentTopMargin = 12.dp,
        icon = {
            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .clickable {

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
fun RecipeScreenTile(
    title: String,
    icon: @Composable () -> Unit,
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium,
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
                    fontWeight = FontWeight.Bold
                )
                icon()
            }
            Spacer(modifier = Modifier.height(contentTopMargin))
            content()
        }
    }
}
