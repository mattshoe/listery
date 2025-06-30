package org.mattshoe.shoebox.listery.navigation

import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import org.mattshoe.shoebox.listery.authentication.data.SessionRepository
import org.mattshoe.shoebox.listery.authentication.model.SessionState
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val navigationProvider: NavigationProvider,
    private val sessionRepository: SessionRepository
): ListeryViewModel<Unit, NavController>(Unit) {

    val landingPage: Any =
        when (sessionRepository.state.value) {
            is SessionState.LoggedIn -> Routes.CookBook
            is SessionState.Anonymous -> Routes.Login
        }

    override fun handleIntent(intent: NavController) {
        navigationProvider.setNavController(intent)
    }

}