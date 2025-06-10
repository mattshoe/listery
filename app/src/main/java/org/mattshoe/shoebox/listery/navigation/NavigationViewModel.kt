package org.mattshoe.shoebox.listery.navigation

import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val navigationProvider: NavigationProvider
): ListeryViewModel<Unit, NavController>(Unit) {

    override fun handleIntent(intent: NavController) {
        navigationProvider.setNavController(intent)
    }

}