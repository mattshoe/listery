package org.mattshoe.shoebox.listery.recipe.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.mattshoe.shoebox.listery.landing.ListeryScaffold
import org.mattshoe.shoebox.listery.navigation.NavigationHandler
import org.mattshoe.shoebox.listery.recipe.viewmodel.RecipeScreenViewModel
import org.mattshoe.shoebox.listery.recipe.viewmodel.ScreenState
import org.mattshoe.shoebox.listery.ui.BottomNavItem
import org.mattshoe.shoebox.listery.ui.common.Level2AppBar
import org.mattshoe.shoebox.listery.ui.common.ListeryCard
import org.mattshoe.shoebox.listery.ui.common.TopBarIcon

@Composable
fun RecipeScreen(
    viewModel: RecipeScreenViewModel = hiltViewModel(),
    recipeName: String
) {
    NavigationHandler(viewModel)

    LaunchedEffect(recipeName) {
        viewModel.initialize(recipeName)
    }

    val viewModelState: ScreenState by viewModel.state.collectAsState()
    when (viewModelState) {
        is ScreenState.Loading -> Unit
        is ScreenState.Error -> Unit
        is ScreenState.Ready -> SuccessScreen(viewModelState as ScreenState.Ready)
    }

}

@Composable
private fun SuccessScreen(viewModelState: ScreenState.Ready) {
    ListeryScaffold(
        topBar = {
            Level2AppBar(
                actions = listOf(
                    TopBarIcon(Icons.AutoMirrored.Default.Send, "") {}
                )
            )
        },
        selectedNavItem = BottomNavItem.Cookbook,
        onFabClick = {}
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            ListeryCard {
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f),
                            text = viewModelState.data.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

}