package org.mattshoe.shoebox.listery.navigation

import kotlinx.serialization.Serializable


sealed interface Routes {

    @Serializable
    data object CookBook: Routes

    @Serializable
    data class Recipe(val name: String): Routes

    @Serializable
    data object ChooseRecipeCreationMethodBottomSheet: Routes

    @Serializable
    data object CreateRecipeManuallyBottomSheet: Routes

    @Serializable
    data object CreateRecipeWithAIBottomSheet: Routes

    @Serializable
    data object CreateRecipeFromWebsiteBottomSheet: Routes

    @Serializable
    data class EditRecipeOverviewBottomSheet(val name: String): Routes

    @Serializable
    data class EditIngredientsBottomSheet(val recipeName: String): Routes

    @Serializable
    data class EditDirectionsBottomSheet(val recipeName: String): Routes

    @Serializable
    data object Login: Routes

    @Serializable
    data object ResetPassword: Routes

}