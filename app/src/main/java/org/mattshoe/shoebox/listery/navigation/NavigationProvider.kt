package org.mattshoe.shoebox.listery.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.navigation.NavController
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationProvider @Inject constructor() {
    private lateinit var _navController: NavController

    val navController: NavController
        get() = _navController

    fun setNavController(navController: NavController) {
        _navController = navController
    }
}