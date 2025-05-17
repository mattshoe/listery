package org.mattshoe.shoebox.listery.cookbook.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.cookbook.viewmodel.CookBookState
import org.mattshoe.shoebox.listery.cookbook.viewmodel.CookBookViewModel
import org.mattshoe.shoebox.listery.cookbook.viewmodel.UserIntent
import org.mattshoe.shoebox.listery.landing.ListeryScaffold
import org.mattshoe.shoebox.listery.navigation.NavigationHandler
import org.mattshoe.shoebox.listery.ui.BottomNavItem
import org.mattshoe.shoebox.listery.ui.common.ClickableLinkText
import org.mattshoe.shoebox.listery.ui.common.Level1AppBar
import org.mattshoe.shoebox.listery.ui.common.ListeryCard
import org.mattshoe.shoebox.listery.util.bottomBorder

@Composable
fun CookbookScreen(
    viewModel: CookBookViewModel = hiltViewModel()
) {
    NavigationHandler(viewModel)

    val state by viewModel.state.collectAsState()
    val recipeList = when (state) {
        is CookBookState.Success -> (state as CookBookState.Success).recipes
        else -> emptyList()
    }

    ListeryScaffold(
        topBar = { Level1AppBar("CookBook") },
        BottomNavItem.Cookbook,
        onFabClick =  { viewModel.handleIntent(UserIntent.NewRecipe) }
    ) { padding ->
        val listState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            state = listState
        ) {
            stickyHeader {
                SearchBar(listState)
            }
            items(recipeList) {
                RecipeCard(
                    it.name,
                    it.starred,
                    it.url,
                    it.calories,
                    it.ingredientCount,
                    it.prepTime,
                    onStarTap = {
                        viewModel.handleIntent(UserIntent.RecipeStarTapped(it))
                    }
                ) {
                    viewModel.handleIntent(UserIntent.RecipeTapped(it))
                }
            }
        }
    }
}

@Composable
fun SearchBar(listState: LazyListState) {
    var searchText: String by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    var text by remember { mutableStateOf(TextFieldValue()) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shadowElevation = if (listState.isScrolledToTop()) {
            0.dp
        } else {
            14.dp
        }
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { /* Open filter options */ },
                modifier = Modifier.padding(0.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.outline
                )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                    contentDescription = "Filter Options"
                )
            }
            BasicTextField(
                modifier = Modifier
                    .bottomBorder(1.dp, Color.Gray)
                    .width(250.dp)
                    .padding(0.dp)
                    .weight(1f),
                value = text,
                onValueChange = {
                    text = it
                },
                textStyle = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.outline,
                    lineHeight = 26.sp
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                singleLine = true,
                interactionSource = interactionSource,
                decorationBox = { innerTextField ->
                    if (text.text.isEmpty()) {
                        Text(
                            text = "Search",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = MaterialTheme.colorScheme.outline,
                                lineHeight = 26.sp
                            )
                        )
                    }
                    innerTextField()
                }
            )
            Spacer(modifier = Modifier
                .width(16.dp)
            )
            IconButton(
                onClick = { /* Open filter options */ },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.outline
                )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_filter),
                    contentDescription = "Filter Options"
                )
            }
        }
    }
}

@Composable
fun RecipeCard(
    name: String,
    starred: Boolean,
    url: String?,
    calories: Int,
    ingredientCount: Int,
    prepTime: String,
    onStarTap: () -> Unit,
    onTap: () -> Unit
) {
    ListeryCard {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .clickable(true) {
                    onTap()
                }
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = { onStarTap() },
                    modifier = Modifier
                        .padding(0.dp)
                        .size(24.dp)
                ) {
                    Image(
                        painter = painterResource(
                            id = if (starred) R.drawable.ic_filled_star else R.drawable.ic_hollow_star
                        ),
                        contentDescription = "More Options",
                        modifier = Modifier
                            .size(24.dp)
                            .padding(0.dp)
                    )
                }
            }
            url?.let {
                ClickableLinkText(uri = it, modifier = Modifier.padding(0.dp))
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = "$calories calories",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "$ingredientCount ingredients",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = prepTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

private fun LazyListState.isScrolledToTop(): Boolean {
    return firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset == 0
}




