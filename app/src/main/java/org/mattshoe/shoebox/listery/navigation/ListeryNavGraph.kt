package org.mattshoe.shoebox.listery.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.navigation.ModalBottomSheetLayout
import androidx.compose.material.navigation.bottomSheet
import androidx.compose.material.navigation.rememberBottomSheetNavigator
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
import org.mattshoe.shoebox.listery.recipe.edit.directions.ui.EditDirectionsScreen
import org.mattshoe.shoebox.listery.recipe.edit.ingredient.ui.EditIngredientsScreen
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
                startDestination = Routes.CookBook
            ) {
                composable<Routes.CookBook> {
                    CookbookScreen()
                }

                composable<Routes.Recipe> {
                    val navArg = it.toRoute<Routes.Recipe>()
                    RecipeScreen(recipeName = navArg.name)
                }

                bottomSheet<Routes.ChooseRecipeCreationMethodBottomSheet> {
                    ChooseRecipeCreationMethodBottomSheet()
                }

                bottomSheet<Routes.CreateRecipeManuallyBottomSheet> {
                    CreateRecipeManuallyBottomSheet()
                }

                bottomSheet<Routes.EditRecipeOverviewBottomSheet> {
                    val navArg = it.toRoute<Routes.EditRecipeOverviewBottomSheet>()
                    EditRecipeOverviewBottomSheet(recipeName = navArg.name)
                }

                bottomSheet<Routes.EditIngredientsBottomSheet> {
                    val navArg = it.toRoute<Routes.EditIngredientsBottomSheet>()
                    EditIngredientsScreen(recipeName = navArg.recipeName)
                }

                bottomSheet<Routes.EditDirectionsBottomSheet> {
                    val navArg = it.toRoute<Routes.EditDirectionsBottomSheet>()
                    EditDirectionsScreen(recipeName = navArg.recipeName)
                }
            }
        }
    }
}
