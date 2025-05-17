package org.mattshoe.shoebox.listery.navigation

import kotlinx.serialization.Serializable


object Routes {

    @Serializable
    object CookBook

    @Serializable
    data class Recipe(val name: String)

}