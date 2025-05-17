package org.mattshoe.shoebox.listery.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import org.mattshoe.shoebox.listery.cookbook.ui.CookbookScreen
import org.mattshoe.shoebox.listery.recipe.ui.RecipeScreen

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No NavController provided")
}

interface NavigateToRoute {
    val route: Any
}

@Composable
fun NavigationHandler(viewModel: ListeryViewModel<*, *>) {
    val navController = LocalNavController.current

    LaunchedEffect(viewModel) {
        viewModel.navigationRoutes
            .onEach {
                if (it is NavigateToRoute) {
                    navController.navigate(it.route)
                }
            }
            .flowOn(Dispatchers.Main.immediate)
            .launchIn(this)
    }
}

@Composable
fun ListeryNavGraph() {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController,
            startDestination = Routes.CookBook
        ) {
            composable<Routes.CookBook> {
                CookbookScreen()
            }

            composable<Routes.Recipe> {
                val navArg = it.toRoute<Routes.Recipe>()
                RecipeScreen(recipeName = navArg.name)
            }
        }
    }

}
