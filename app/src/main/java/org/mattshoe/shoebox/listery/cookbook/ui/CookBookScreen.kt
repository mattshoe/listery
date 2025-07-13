package org.mattshoe.shoebox.listery.cookbook.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.authentication.AuthenticationViewModel
import org.mattshoe.shoebox.listery.cookbook.viewmodel.CookBookState
import org.mattshoe.shoebox.listery.cookbook.viewmodel.CookBookViewModel
import org.mattshoe.shoebox.listery.cookbook.viewmodel.FilterOption
import org.mattshoe.shoebox.listery.cookbook.viewmodel.UserIntent
import org.mattshoe.shoebox.listery.ui.BottomNavItem
import org.mattshoe.shoebox.listery.ui.common.ClickableLinkText
import org.mattshoe.shoebox.listery.ui.common.FilterMenu
import org.mattshoe.shoebox.listery.ui.common.GenericErrorScreen
import org.mattshoe.shoebox.listery.ui.common.Level1AppBar
import org.mattshoe.shoebox.listery.ui.common.ListeryCard
import org.mattshoe.shoebox.listery.ui.common.ListeryScaffold
import org.mattshoe.shoebox.listery.ui.common.ListeryTextInput
import org.mattshoe.shoebox.listery.ui.common.ListeryTile
import org.mattshoe.shoebox.listery.ui.common.ShimmerPlaceholder
import org.mattshoe.shoebox.listery.ui.common.SubduedText
import org.mattshoe.shoebox.listery.navigation.LocalNavController
import org.mattshoe.shoebox.listery.navigation.Routes
import kotlin.time.Duration

@Composable
fun CookbookScreen(
    viewModel: CookBookViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val navController = LocalNavController.current

    ListeryScaffold(
        topBar = { 
            Level1AppBar(
                title = "CookBook",
                onProfileClick = { navController.navigate(Routes.Profile) },
                onMoreOptionsClick = { /* TODO: Implement more options */ }
            ) 
        },
        BottomNavItem.Cookbook,
        onFabClick =  { viewModel.handleIntent(UserIntent.NewRecipe) }
    ) { insets ->
        when (val currentState = state) {
            is CookBookState.Success -> CookbookSuccessScreen(currentState, insets) {
                viewModel.handleIntent(it)
            }
            is CookBookState.Empty -> CookbookEmptyScreen(currentState, insets) {
                viewModel.handleIntent(it)
            }
            is CookBookState.Loading -> CookbookLoadingScreen(insets)
            is CookBookState.Error -> GenericErrorScreen(currentState)
        }
    }
}

@Composable
fun CookbookEmptyScreen(
    state: CookBookState.Empty,
    insets: PaddingValues,
    handleIntent: (UserIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(insets)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MaterialTheme.colorScheme.primary
        Image(
            painter = painterResource(id = state.icon),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .wrapContentHeight()
        )
        Spacer(modifier = Modifier.height(16.dp))
        SubduedText(
            text = state.text,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun CookbookSuccessScreen(
    state: CookBookState.Success,
    insets: PaddingValues,
    handleIntent: (UserIntent) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .padding(insets)
            .fillMaxSize(),
        state = listState
    ) {
        stickyHeader {
            SearchBar(
                listState,
                state.filterOptions,
                onTextChange = {
                    handleIntent(
                        UserIntent.SearchUpdated(it)
                    )
                },
                onFilterChange = { list, option ->
                    handleIntent(
                        UserIntent.FilterUpdated(list, option)
                    )
                }
            )
        }
        items(state.recipes) {
            RecipeCard(
                it.name,
                it.starred,
                it.url,
                it.calories,
                it.ingredients.count(),
                it.prepTime,
                onStarTap = {
                    handleIntent(UserIntent.RecipeStarTapped(it))
                }
            ) {
                handleIntent(UserIntent.RecipeTapped(it))
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun CookbookLoadingScreen(insets: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(insets)
            .fillMaxSize()
    ) {
        // Search bar
        SearchBar(
            listState = rememberLazyListState(),
            filterOptions = emptyList(),
            onTextChange = { },
            onFilterChange = { _, _ -> },
            modifier = Modifier
                .alpha(0.3f)
                .gesturesDisabled()
        )
        
        // Ghost cards
        repeat(15) {
            RecipeCardGhostLoader()
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun SearchBar(
    listState: LazyListState,
    filterOptions: List<FilterOption<*>>,
    modifier: Modifier = Modifier,
    onTextChange: (String) -> Unit,
    onFilterChange: (List<FilterOption<*>>, FilterOption<*>) -> Unit
) {
    val localDensity = LocalDensity.current
    var showFilters by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(TextFieldValue()) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .then(modifier),
        shadowElevation = if (listState.isScrolledToTop()) 0.dp else 14.dp
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search Icon
            Icon(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .alpha(0.5f),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                contentDescription = "Search"
            )

            ListeryTextInput(
                modifier = Modifier.weight(1f),
                value = text,
                placeholder = "Search",
            ) {
                text = it
                onTextChange(it.text)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Filter Button + Popup
            Box {
                IconButton(
                    onClick = { showFilters = !showFilters }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_filter),
                        contentDescription = "Filter Options"
                    )
                }

                if (showFilters) {
                    FilterPopup(
                        offset = IntOffset(x = 0, y = with(localDensity) { 34.dp.roundToPx() }),
                        filterOptions = filterOptions,
                        onUpdated = { list, option ->
                            onFilterChange(list, option)
                        },
                        onDismiss = {  showFilters = false }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterPopup(
    filterOptions: List<FilterOption<*>>,
    modifier: Modifier = Modifier,
    offset: IntOffset = IntOffset(0),
    onUpdated: (List<FilterOption<*>>, FilterOption<*>) -> Unit,
    onDismiss: () -> Unit,
) {
    Popup(
        offset = offset,
        alignment = Alignment.TopEnd,
        onDismissRequest = onDismiss,
        properties = PopupProperties(
            focusable = true,
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
            clippingEnabled = true
        )
    ) {
        ListeryTile(
            modifier = modifier
                .padding(12.dp)
                .wrapContentWidth(),
            elevation = 12.dp
        ) {
            FilterMenu(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(.75f),
                filterOptions = filterOptions
            ) { list, option ->
                onUpdated(list, option)
            }
        }
    }
}

@Composable
fun RecipeCard(
    name: String,
    starred: Boolean,
    url: String?,
    calories: Int?,
    ingredientCount: Int,
    prepTime: Duration?,
    onStarTap: () -> Unit,
    onTap: () -> Unit
) {
    ListeryCard {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
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
                ClickableLinkText(text = it, modifier = Modifier.padding(0.dp))
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = "${calories ?: "--"} calories",
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
                SubduedText(
                    text = prepTime.prettyPrint(),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun RecipeCardGhostLoader() {
    ListeryCard {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Title row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShimmerPlaceholder(
                    modifier = Modifier.weight(1f),
                    height = 24
                )
                Spacer(modifier = Modifier.width(8.dp))
                ShimmerPlaceholder(
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // URL placeholder
            ShimmerPlaceholder(
                modifier = Modifier.fillMaxWidth(),
                height = 16
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Calories
            ShimmerPlaceholder(
                modifier = Modifier.width(120.dp),
                height = 16
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Ingredients and prep time row
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                ShimmerPlaceholder(
                    modifier = Modifier.weight(1f),
                    height = 16
                )
                Spacer(modifier = Modifier.width(8.dp))
                ShimmerPlaceholder(
                    modifier = Modifier.width(80.dp),
                    height = 16
                )
            }
        }
    }
}

private fun LazyListState.isScrolledToTop(): Boolean {
    return firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset == 0
}

fun Modifier.gesturesDisabled(disabled: Boolean = true) =
    if (disabled) {
        pointerInput(Unit) {
            awaitPointerEventScope {
                // we should wait for all new pointer events
                while (true) {
                    awaitPointerEvent(pass = PointerEventPass.Initial)
                        .changes
                        .forEach(PointerInputChange::consume)
                }
            }
        }
    } else {
        this
    }


fun Duration?.prettyPrint(placeHolder: String = "--"): String {
    return this?.toComponents { hours, minutes, _, _ ->
        when {
            hours > 0 -> "$hours hr $minutes min"
            minutes > 0 -> "$minutes min"
            else -> placeHolder
        }
    } ?: placeHolder
}

fun Duration?.prettyHours(placeHolder: String = "--"): String {
    return this?.toComponents { hours, _, _, _ ->
        hours.toString()
    } ?: placeHolder
}

fun Duration?.prettyMinutes(placeHolder: String = "--"): String {
    return this?.toComponents { _, minutes, _, _ ->
        minutes.toString()
    } ?: placeHolder
}
