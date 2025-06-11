package org.mattshoe.shoebox.listery.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.navigation.ModalBottomSheetLayout
import androidx.compose.material.navigation.bottomSheet
import androidx.compose.material.navigation.rememberBottomSheetNavigator
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.mattshoe.shoebox.listery.cookbook.ui.CookbookScreen
import org.mattshoe.shoebox.listery.recipe.create.ui.ChooseRecipeCreationMethodBottomSheet
import org.mattshoe.shoebox.listery.recipe.create.ui.CreateRecipeManuallyBottomSheet
import org.mattshoe.shoebox.listery.recipe.edit.overview.ui.EditRecipeOverviewBottomSheet
import org.mattshoe.shoebox.listery.recipe.ui.RecipeScreen

@Composable
fun ListeryNavGraph(
    navigationViewModel: NavigationViewModel = hiltViewModel()
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
    navigationViewModel.handleIntent(navController)

    CompositionLocalProvider(LocalNavController provides navController) {
        ModalBottomSheetLayout(
            bottomSheetNavigator,
            sheetShape = RoundedCornerShape(28.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            NavHost(
                navController,
                startDestination = Route.CookBook
            ) {
                composable<Route.CookBook> {
                    CookbookScreen()
                }

                composable<Route.Recipe> {
                    val navArg = it.toRoute<Route.Recipe>()
                    RecipeScreen(recipeName = navArg.name)
                }

                bottomSheet<Route.ChooseRecipeCreationMethodBottomSheet> {
                    ChooseRecipeCreationMethodBottomSheet()
                }

                bottomSheet<Route.CreateRecipeManuallyBottomSheet> {
                    CreateRecipeManuallyBottomSheet()
                }

                bottomSheet<Route.EditRecipeOverviewBottomSheet> {
                    val navArg = it.toRoute<Route.EditRecipeOverviewBottomSheet>()
                    EditRecipeOverviewBottomSheet(recipeName = navArg.name)
                }
            }
        }
    }
}
