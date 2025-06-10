package org.mattshoe.shoebox.listery.navigation

import kotlinx.serialization.Serializable


sealed interface Route {

    @Serializable
    data object CookBook: Route

    @Serializable
    data class Recipe(val name: String): Route

    @Serializable
    data object ChooseRecipeCreationMethodBottomSheet: Route

    @Serializable
    data object CreateRecipeManuallyBottomSheet: Route

    @Serializable
    data object CreateRecipeWithAIBottomSheet: Route

    @Serializable
    data object CreateRecipeFromWebsiteBottomSheet: Route

}