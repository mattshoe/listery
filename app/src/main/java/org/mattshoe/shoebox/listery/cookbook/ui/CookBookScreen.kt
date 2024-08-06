package org.mattshoe.shoebox.listery.cookbook.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.cookbook.viewmodel.CookBookState
import org.mattshoe.shoebox.listery.cookbook.viewmodel.CookBookViewModelImpl
import org.mattshoe.shoebox.listery.model.Recipe
import org.mattshoe.shoebox.listery.ui.common.ClickableLinkText
import org.mattshoe.shoebox.listery.ui.theme.ListeryTheme
import org.mattshoe.shoebox.listery.util.bottomBorder

@Preview(showBackground = true)
@Composable
fun CookBookScreenPreview() {
    ListeryTheme {
        CookBookScreen()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CookBookScreen() {
    val viewmodel: CookBookViewModelImpl = hiltViewModel()
    val state = viewmodel.state.collectAsState()
    val recipeList by remember {
        derivedStateOf {
            when (state.value) {
                is CookBookState.Success -> (state.value as CookBookState.Success).recipes
                else -> emptyList()
            }
        }
    }

    Scaffold(
        topBar = { CookBookTopBar() },
        bottomBar = { ListeryNavigationBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_plus),
                    contentDescription = "New Recipe",
                    modifier = Modifier.size(14.dp)
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            item {
                SearchBar()
            }
            items(recipeList) {
                RecipeCard(
                    it.name,
                    it.starred,
                    it.url,
                    it.calories,
                    it.ingredientCount,
                    it.prepTime
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CookBookTopBar() {
    TopAppBar(
        title = {
            Text(text = "Cookbook")
        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White,
            scrolledContainerColor = Color.Green
        ),
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_account),
                    contentDescription = "User Profile",
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_more_options),
                    contentDescription = "More Options",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}

@Composable
fun ListeryNavigationBar() {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        NavigationIcon(icon = R.drawable.ic_meal_plan, label = "Plan")
        NavigationIcon(icon = R.drawable.ic_cookbook, label = "Cookbook")
        NavigationIcon(icon = R.drawable.ic_shopping_list, label = "Shopping")
    }
}

@Composable
fun RowScope.NavigationIcon(
    @DrawableRes icon: Int,
    label: String
) {
    NavigationBarItem(
        selected = false,
        onClick = { /*TODO*/ },
        icon = {
            Image(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )
        },
        label = {
            Text(text = label, color = MaterialTheme.colorScheme.onPrimary)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    var searchText: String by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    var text by remember { mutableStateOf(TextFieldValue()) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
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
    url: String,
    calories: Int,
    ingredientCount: Int,
    prepTime: String
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Top),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = name,
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(
                    onClick = { /*TODO*/ },
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
            ClickableLinkText(uri = url, modifier = Modifier.padding(0.dp))

            Spacer(modifier = Modifier.height(8.dp))

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
        }
    }
}





