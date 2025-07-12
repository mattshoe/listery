package org.mattshoe.shoebox.listery.ui

import androidx.annotation.DrawableRes
import org.mattshoe.shoebox.listery.R

sealed interface BottomNavItem {
    companion object {
        val values: List<BottomNavItem> = listOf(
            MealPlan,
            Cookbook,
            Shopping
        )
    }

    @get:DrawableRes
    val icon: Int
    val label: String

    data object MealPlan: BottomNavItem {
        data object Route
        override val icon = R.drawable.ic_meal_plan
        override val label = "Plan"
    }

    data object Cookbook: BottomNavItem {
        data object Route
        override val icon = R.drawable.ic_cookbook
        override val label = "Cookbook"
    }

    data object Shopping: BottomNavItem {
        data object Route
        override val icon = R.drawable.ic_shopping_list
        override val label = "Shopping"
    }
}